package org.stummi.swpb;

public class WikimediaApiException extends Exception {

    /**
	 *
	 */
    private static final long serialVersionUID = -6855626293188226243L;

    public WikimediaApiException() {
        super();
    }

    public WikimediaApiException(Throwable t) {
        super(t);
    }

    public WikimediaApiException(String s) {
        super(s);
    }

    public WikimediaApiException(String s, Throwable t) {
        super(s, t);
    }
}
