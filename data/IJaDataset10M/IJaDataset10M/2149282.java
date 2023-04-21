package org.slasoi.businessManager.postSale.reporting.exception;

public class ImageException extends Exception {

    private static final long serialVersionUID = -5831809679141782069L;

    public ImageException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ImageException(Throwable throwable) {
        super(throwable);
    }

    public ImageException(String msg) {
        super(msg);
    }
}
