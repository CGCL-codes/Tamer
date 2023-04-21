package gov.lanl.resource.openurl;

import gov.lanl.resource.ResourceException;

public class ResolverException extends ResourceException {

    private static final long serialVersionUID = 6532963984240949392L;

    public ResolverException(String message) {
        super(message);
    }

    public ResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResolverException(Throwable cause) {
        super(cause);
    }
}
