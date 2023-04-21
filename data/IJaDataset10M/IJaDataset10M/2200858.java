package org.retro.gis.tools;

public class InvalidBotException extends Exception {

    public InvalidBotException() {
        super("The bot you are trying to use is invalid or not set properly");
    }
}
