package net.sf.jnclib.tp.ssh2.ber;

import java.io.IOException;

/**
 * Exception thrown when decoding garbled BER data.
 */
public class BERException extends IOException {

    public BERException(String description) {
        super(description);
    }
}
