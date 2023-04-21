package net.sf.beezle.sushi.fs;

public class GetLastModifiedException extends NodeException {

    public GetLastModifiedException(Node node, Throwable e) {
        super(node, "getLastModified failed", e);
    }
}
