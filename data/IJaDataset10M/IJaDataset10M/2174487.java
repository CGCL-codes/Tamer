package com.oneandone.sushi.fs;

public class CopyException extends NodeException {

    public final Node dest;

    public CopyException(Node src, Node dest, Throwable e) {
        this(src, dest, e.getMessage(), e);
    }

    public CopyException(Node src, Node dest, String msg, Throwable e) {
        super(src, "copy failed: " + dest + ": " + msg);
        this.dest = dest;
        initCause(e);
    }
}
