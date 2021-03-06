package com.rrpictureproductions.chess.figures

import com.rrpictureproductions.chess.Board
import com.rrpictureproductions.chess.Position
import com.rrpictureproductions.chess.crossproduct
import com.rrpictureproductions.chess.figures.Color.*
import com.rrpictureproductions.chess.figures.Initials.BISHOP
import com.rrpictureproductions.chess.figures.Initials.KING
import com.rrpictureproductions.chess.figures.Initials.KNIGHT
import com.rrpictureproductions.chess.figures.Initials.PAWN
import com.rrpictureproductions.chess.figures.Initials.QUEEN
import com.rrpictureproductions.chess.figures.Initials.ROOK
import kotlin.collections.asSequence as lazy


abstract class ChessPiece(val initial: String, val color: Color) {
    open val canJump = false
    abstract fun getReachablePositionsFrom(position: Position): Set<Position>
    override fun toString(): String = unicodePiece(initial, color)
}

enum class Color { WHITE, BLACK }

class King(color: Color) : ChessPiece(KING, color) {
    override fun getReachablePositionsFrom(position: Position) =
            Board.getAdjacentPositions(position)
}

class Queen(color: Color) : ChessPiece(QUEEN, color) {
    override fun getReachablePositionsFrom(position: Position) = Board.run {
        getPositionsOnFile(position.file)
                .plus(getPositionsOnRank(position.rank))
                .plus(getDiagonalPositionsFrom(position))
                .minus(position)
    }
}

class Rook(color: Color) : ChessPiece(ROOK, color) {
    override fun getReachablePositionsFrom(position: Position) = Board.run {
        getPositionsOnFile(position.file)
                .plus(getPositionsOnRank(position.rank))
                .minus(position)
    }
}

class Bishop(color: Color) : ChessPiece(BISHOP, color) {
    override fun getReachablePositionsFrom(position: Position) =
            Board.getDiagonalPositionsFrom(position)
}

class Knight(color: Color) : ChessPiece(KNIGHT, color) {
    override val canJump = true
    override fun getReachablePositionsFrom(position: Position) =
            setOf(-2, -1, 1, 2).lazy().crossproduct(setOf(-2, -1, 1, 2).lazy())
                    .filter { Math.abs(it.first) != Math.abs(it.second) }
                    .map { position.move(it.first, it.second) }
                    .filterNotNull()
                    .toSet()
}

class Pawn(color: Color) : ChessPiece(PAWN, color) {
    override fun getReachablePositionsFrom(position: Position) = TODO()
}

object Initials {
    val KING = "K"
    val QUEEN = "Q"
    val ROOK = "R"
    val BISHOP = "B"
    val KNIGHT = "N"
    val PAWN = "P"
}

fun unicodePiece(initial: String, color: Color) = when(initial to color) {
    KING to WHITE -> "♔"
    QUEEN to WHITE -> "♕"
    ROOK to WHITE -> "♖"
    BISHOP to WHITE -> "♗"
    KNIGHT to WHITE -> "♘"
    PAWN to WHITE -> "♙"
    KING to BLACK -> "♚"
    QUEEN to BLACK -> "♛"
    ROOK to BLACK -> "♜"
    BISHOP to BLACK -> "♝"
    KNIGHT to BLACK -> "♞"
    PAWN to BLACK -> "♟"
    else -> throw IllegalArgumentException("Invalid chess piece: $color $initial")
}