package net.sourceforge.omov.core;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class FatalException extends RuntimeException {

    private static final long serialVersionUID = 5469867679628436702L;

    private final FatalReason reason;

    public static enum FatalReason {

        UNDEF, DB_LOCKED
    }

    public FatalException(String msg) {
        this(msg, FatalReason.UNDEF);
    }

    public FatalException(String msg, FatalReason reason) {
        super(msg);
        this.reason = reason;
    }

    public FatalException(String msg, Exception t) {
        this(msg, t, FatalReason.UNDEF);
    }

    public FatalException(String msg, Exception t, FatalReason reason) {
        super(msg, t);
        this.reason = reason;
    }

    public FatalReason getReason() {
        return this.reason;
    }
}
