package com.hadeslee.audiotag.audio.exceptions;

/**
 * This is the exception when try and access a read only file
 */
public class ReadOnlyFileException extends Exception {

    /**
     * Creates a new ReadOnlyException datatype.
     */
    public ReadOnlyFileException() {
    }

    public ReadOnlyFileException(Throwable ex) {
        super(ex);
    }

    /**
     * Creates a new ReadOnlyException datatype.
     *
     * @param msg the detail message.
     */
    public ReadOnlyFileException(String msg) {
        super(msg);
    }

    public ReadOnlyFileException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
