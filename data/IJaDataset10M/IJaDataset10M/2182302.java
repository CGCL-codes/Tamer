package de.jardas.chessworld;

public class WrongUserIdException extends RuntimeException {

    public WrongUserIdException() {
        super("Wrong user ID.");
    }
}
