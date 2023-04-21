package org.apache.harmony.security.tests.support.spec;

import java.security.spec.EncodedKeySpec;

/**
 * Support class for abstract base class testing
 */
public class MyEncodedKeySpec extends EncodedKeySpec {

    /**
     * Constructor
     * @param encodedKey
     */
    public MyEncodedKeySpec(byte[] encodedKey) {
        super(encodedKey);
    }

    /**
     * Returns format - "My"
     * @see java.security.spec.EncodedKeySpec#getFormat()
     */
    public String getFormat() {
        return "My";
    }
}
