package org.makumba;

/** An unauthenticated exception, typically due to a missing attribute */
public class UnauthenticatedException extends UnauthorizedException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    String attName;

    public UnauthenticatedException() {
        super("");
    }

    public UnauthenticatedException(String message) {
        super(message);
    }

    public String getAttributeName() {
        return attName;
    }

    /** set the missing attribute name */
    public void setAttributeName(String s) {
        this.attName = s;
    }

    public String getMessage() {
        return super.getMessage();
    }
}
