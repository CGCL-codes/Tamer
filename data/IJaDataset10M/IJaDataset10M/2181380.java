package org.metastatic.jessie.pki.provider;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAParams;
import java.security.spec.DSAParameterSpec;
import java.util.ArrayList;
import org.metastatic.jessie.pki.der.OID;
import org.metastatic.jessie.pki.der.DER;
import org.metastatic.jessie.pki.der.DERValue;
import org.metastatic.jessie.pki.der.DERWriter;

class GnuDSAPrivateKey implements DSAPrivateKey {

    private byte[] encodedKey;

    BigInteger x;

    BigInteger p;

    BigInteger q;

    BigInteger g;

    public GnuDSAPrivateKey(BigInteger x, BigInteger p, BigInteger q, BigInteger g) {
        this.x = x;
        this.p = p;
        this.q = q;
        this.g = g;
    }

    public String getAlgorithm() {
        return "DSA";
    }

    public String getFormat() {
        return "PKCS#8";
    }

    /**
   * Encodes this key as a <code>PrivateKeyInfo</code>, as described in
   * PKCS #8. The ASN.1 specification for this structure is:
   *
   * <blockquote><pre>
   * PrivateKeyInfo ::= SEQUENCE {
   *   version Version,
   *   privateKeyAlgorithm PrivateKeyAlgorithmIdentifier,
   *   privateKey PrivateKey,
   *   attributes [0] IMPLICIT Attributes OPTIONAL }
   *
   * Version ::= INTEGER
   *
   * PrivateKeyAlgorithmIdentifier ::= AlgorithmIdentifier
   *
   * PrivateKey ::= OCTET STRING
   *
   * Attributes ::= SET OF Attribute
   * </pre></blockquote>
   *
   * <p>DSA private keys (in Classpath at least) have no attributes.
   */
    public byte[] getEncoded() {
        if (encodedKey != null) return (byte[]) encodedKey.clone();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ArrayList pki = new ArrayList(3);
            pki.add(new DERValue(DER.INTEGER, BigInteger.ZERO));
            ArrayList algId = new ArrayList(2);
            algId.add(new DERValue(DER.OBJECT_IDENTIFIER, new OID("1.2.840.10040.4.1")));
            ArrayList algParams = new ArrayList(3);
            algParams.add(new DERValue(DER.INTEGER, p));
            algParams.add(new DERValue(DER.INTEGER, q));
            algParams.add(new DERValue(DER.INTEGER, g));
            algId.add(new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, algParams));
            pki.add(new DERValue(DER.OCTET_STRING, x.toByteArray()));
            DERWriter.write(out, new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, pki));
            return (byte[]) (encodedKey = out.toByteArray()).clone();
        } catch (IOException ioe) {
            return null;
        }
    }

    public DSAParams getParams() {
        return (DSAParams) (new DSAParameterSpec(p, q, g));
    }

    public BigInteger getX() {
        return x;
    }

    public String toString() {
        return "GnuDSAPrivateKey: x=" + (x != null ? x.toString(16) : "null") + " p=" + (p != null ? p.toString(16) : "null") + " q=" + (q != null ? q.toString(16) : "null") + " g=" + (g != null ? g.toString(16) : "null");
    }
}
