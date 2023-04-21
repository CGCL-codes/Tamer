package jm.booklib.exceptions;

public class InvalidISBNException extends Exception {

    public InvalidISBNException() {
        super("The given ISBN is not valid.");
    }
}
