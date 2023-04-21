package xades4j.providers;

/**
 * Thrown when digests mismatch during a time-stamp token verification.
 * @author Luís
 */
public class TimeStampTokenDigestException extends TimeStampTokenVerificationException {

    public TimeStampTokenDigestException() {
        super("Token imprint doesn't match the input");
    }
}
