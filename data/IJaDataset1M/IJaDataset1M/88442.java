package com.amarphadke.chess.server.domain;

public interface Notation {

    public String encode(BoardPositionProvider boardPositionProvider, Move move);
}
