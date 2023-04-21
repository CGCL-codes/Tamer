package net.flysource.client.exceptions;

public class CantMoveLibraryException extends Exception {

    public CantMoveLibraryException(String message) {
        super("Cannot move library.\n\n" + message);
    }
}
