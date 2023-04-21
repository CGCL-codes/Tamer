package de.admedia.util;

public class InternalException extends AdmediaEjbException {

    private static final long serialVersionUID = 7294078700257616332L;

    public InternalException(String key, Object... args) {
        super(MessagesUtil.getMessage(key, args));
    }
}
