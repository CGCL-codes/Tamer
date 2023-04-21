package edu.ucla.cs.rpc.multicast.util.token;

/**
 * An exception, thrown if a TokenManager attempts to perform some type of
 * operation on a Token without holding it. Specifically, if the internal token
 * is NULL.
 * 
 * @author Philip Russell Chase Covello
 * 
 */
public class TokenNotHeldException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String infoString = "Token is not held, cannot perform this operation!";

    public TokenNotHeldException() {
        super();
    }

    public String toString() {
        return this.infoString;
    }

    public TokenNotHeldException(String arg0) {
        super(arg0);
    }

    public TokenNotHeldException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public TokenNotHeldException(Throwable arg0) {
        super(arg0);
    }
}
