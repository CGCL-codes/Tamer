package de.martinsland.phpbt.core;

/**
 * Indicates an error while parsing a ticket retrieved from a repository.
 * 
 */
public class InvalidTicketException extends PhpbtException {

    private static final long serialVersionUID = 7716941243394876876L;

    public InvalidTicketException(String message) {
        super(message);
    }

    public InvalidTicketException() {
    }
}
