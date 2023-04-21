package org.shava.core.exceptions;

/**
 * @author Julián Gutierrez Oschmann.
 *
 */
public class NoBufferReaderException extends BrokenBufferException {

    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 3741429514151256369L;

    public NoBufferReaderException(String cause) {
        super(cause);
    }
}
