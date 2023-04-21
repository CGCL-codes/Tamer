package com.cbsgmbh.xi.af.edifact.crypt.exceptions;

public class CryptExceptionKeyInvalid extends CryptException {

    public CryptExceptionKeyInvalid(Exception e) {
        super(e);
    }

    public String getErrorMessage() {
        return "ERR_KEY_INVALID - key is invalid";
    }
}
