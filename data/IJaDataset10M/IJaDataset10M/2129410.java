package org.ctor.dev.llrps2.session;

public class RpsSessionException extends Exception {

    private static final long serialVersionUID = 1;

    public RpsSessionException(String msg) {
        super(msg);
    }

    public RpsSessionException(String msg, Throwable t) {
        super(msg, t);
    }

    public RpsSessionException(Throwable t) {
        super(t);
    }
}
