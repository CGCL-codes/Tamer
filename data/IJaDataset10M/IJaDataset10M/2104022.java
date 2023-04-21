package it.unibz.izock.exceptions;

public class MoveNotAllowedException extends WattenException {

    private static final long serialVersionUID = 5301351520183339573L;

    public MoveNotAllowedException() {
        super("Move not allowed!");
    }
}
