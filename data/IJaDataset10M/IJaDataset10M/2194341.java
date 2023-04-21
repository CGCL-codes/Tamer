package br.usp.ime.dojo.core.exceptions;

public class DuplicatedRoomException extends Exception {

    private static final long serialVersionUID = 1L;

    public DuplicatedRoomException() {
        super("This room name is already being used.");
    }
}
