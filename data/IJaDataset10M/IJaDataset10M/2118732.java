package org.monet.docservice.core.exceptions;

public class FilesystemException extends ApplicationException {

    private static final long serialVersionUID = 1L;

    public FilesystemException(String msg) {
        super(msg);
    }

    public FilesystemException(String msg, Exception oReason) {
        super(msg, oReason);
    }
}
