package org.vmaster.common.exceptions;

public class WrappedException extends VMasterException {

    public WrappedException(String s, Throwable e) {
        super(s, e);
    }
}
