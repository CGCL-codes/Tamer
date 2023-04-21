package net.sf.ofx4j;

import net.sf.ofx4j.domain.data.common.Status;

/**
 * Exception based on a StatusCode response
 *
 * @author Michael Mosseri
 */
public class OFXStatusException extends OFXException {

    private Status status;

    public OFXStatusException() {
    }

    public OFXStatusException(Status status, String message) {
        super(message);
        this.status = status;
    }

    public OFXStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public OFXStatusException(Throwable cause) {
        super(cause);
    }

    public Status getStatus() {
        return this.status;
    }
}
