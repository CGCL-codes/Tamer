package com.orientechnologies.odbms.enterprise;

import javax.jdo.JDOUserException;

public class ConnectionException extends JDOUserException {

    public ConnectionException() {
    }

    public ConnectionException(String iMessage) {
        super(iMessage);
    }

    public ConnectionException(String iMessage, Exception iInternal) {
        super(iMessage, iInternal);
    }

    public String toString() {
        return super.toString() + ": Connection error ";
    }
}
