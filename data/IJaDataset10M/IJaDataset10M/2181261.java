package hu.bme.aait.picstore.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class NoSuchImageException extends Exception {

    private static final long serialVersionUID = 1L;

    public NoSuchImageException(int id) {
        super("Image with " + id + " id not found!");
    }
}
