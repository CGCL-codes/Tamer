package org.stummi.swpb;

public class WikimediaApiLoginException extends WikimediaApiException {

    private static final long serialVersionUID = -4052505212426288576L;

    public WikimediaApiLoginException() {
        super();
    }

    public WikimediaApiLoginException(Throwable t) {
        super(t);
    }

    public WikimediaApiLoginException(String s) {
        super(s);
    }

    public WikimediaApiLoginException(String s, Throwable t) {
        super(s, t);
    }
}
