package de.jardas.chessworld;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("Authentication failed.");
    }
}
