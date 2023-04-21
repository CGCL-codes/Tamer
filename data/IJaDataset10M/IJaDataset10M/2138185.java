package org.squabble.exception;

public class SiteDisabledException extends SquabbleException {

    private static final long serialVersionUID = -1241642012907894142L;

    public SiteDisabledException() {
        super();
    }

    public SiteDisabledException(String msg) {
        super(msg);
    }
}
