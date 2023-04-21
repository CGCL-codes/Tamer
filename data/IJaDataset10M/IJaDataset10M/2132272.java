package net.kortsoft.gameportlet.model.impl;

import net.kortsoft.gameportlet.model.GameType;

public class UncompatibleGameTypeException extends RuntimeException {

    private GameType uncompatibleGameType;

    public UncompatibleGameTypeException(GameType gameType, String message) {
        super(message);
        this.uncompatibleGameType = gameType;
    }

    public GameType getUncompatibleGameType() {
        return uncompatibleGameType;
    }

    private static final long serialVersionUID = 6643745899130008949L;
}
