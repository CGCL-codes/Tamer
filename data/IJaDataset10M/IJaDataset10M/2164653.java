package org.privale.coreclients.cryptoclient;

public class EncodeFailedException extends Exception {

    private static final long serialVersionUID = 1L;

    public EncodeFailedException() {
        super();
    }

    public EncodeFailedException(String msg) {
        super(msg);
    }
}
