package org.vmaster.common.exceptions;

public class FileLoadException extends RepositoryException {

    public FileLoadException(String s) {
        super(s);
    }

    public FileLoadException(String s, Throwable e) {
        super(s, e);
    }
}
