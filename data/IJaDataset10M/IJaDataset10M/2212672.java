package com.triplyx.volume;

import java.io.InputStream;

/**
 * An InputStream which reads the ciphertext component from a data stream
 * on a ThirdVolume. It does this by skipping over the stripes of the key
 * component (B), and just returning the ciphertext component (D XOR A).
 * The data is stored in the order: Stripe of D XOR A, Stripe of B.
 * 
 */
public class CiphertextStripeInputStream extends StripeHandlerInputStream {

    public CiphertextStripeInputStream(final long stripeSize, final InputStream stripedInputStream) {
        super(stripeSize, stripedInputStream, false);
    }
}
