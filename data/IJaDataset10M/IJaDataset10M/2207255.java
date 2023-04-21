package com.mindbright.jca.security.spec;

import com.mindbright.jca.security.GeneralSecurityException;

public class InvalidKeySpecException extends GeneralSecurityException {

    public InvalidKeySpecException() {
        super();
    }

    public InvalidKeySpecException(String msg) {
        super(msg);
    }
}
