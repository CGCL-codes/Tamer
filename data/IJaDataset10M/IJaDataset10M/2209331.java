package com.mindbright.security.pkcs1;

import java.math.BigInteger;
import com.mindbright.asn1.ASN1Integer;

/**
 * <pre>
 * Dss-Pub-Key ::= INTEGER  -- Y
 * </pre>
 */
public class DSAPublicKey extends ASN1Integer {

    public DSAPublicKey() {
    }

    public DSAPublicKey(BigInteger y) {
        setValue(y);
    }

    public BigInteger getY() {
        return getValue();
    }
}
