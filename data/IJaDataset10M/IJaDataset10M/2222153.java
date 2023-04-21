package net.sf.beezle.sushi.fs;

public class MkfileException extends NodeException {

    public MkfileException(Node node) {
        super(node, "mkfile failed");
    }

    public MkfileException(Node node, Throwable e) {
        this(node);
        initCause(e);
    }
}
