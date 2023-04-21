package net.bioclipse.xws.exceptions;

public class XmppException extends Exception {

    private static final long serialVersionUID = -4236878976065532451L;

    public XmppException(String message) {
        super(message);
    }

    public XmppException(Throwable t) {
        super(t);
    }
}
