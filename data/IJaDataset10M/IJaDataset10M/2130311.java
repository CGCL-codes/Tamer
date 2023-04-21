package gnu.java.security.provider;

import gnu.java.security.OID;
import gnu.java.security.der.BitString;
import gnu.java.security.der.DER;
import gnu.java.security.der.DERValue;
import gnu.java.security.der.DERWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.DSAParameterSpec;
import java.util.ArrayList;

public class GnuDSAPublicKey implements DSAPublicKey {

    private byte[] encodedKey;

    BigInteger y;

    BigInteger p;

    BigInteger q;

    BigInteger g;

    public GnuDSAPublicKey(BigInteger y, BigInteger p, BigInteger q, BigInteger g) {
        this.y = y;
        this.p = p;
        this.q = q;
        this.g = g;
    }

    public String getAlgorithm() {
        return "DSA";
    }

    public String getFormat() {
        return "X.509";
    }

    /**
   * The encoded form of DSA public keys is:
   *
   * <blockquote><pre>
   * SubjectPublicKeyInfo ::= SEQUENCE {
   *   algorithm AlgorithmIdentifier,
   *   subjectPublicKey BIT STRING }
   * </pre></blockquote>
   */
    public byte[] getEncoded() {
        if (encodedKey != null) return (byte[]) encodedKey.clone();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ArrayList spki = new ArrayList(2);
            ArrayList alg = new ArrayList(2);
            alg.add(new DERValue(DER.OBJECT_IDENTIFIER, new OID("1.2.840.113549.1.1.1")));
            ArrayList params = new ArrayList(3);
            params.add(new DERValue(DER.INTEGER, p));
            params.add(new DERValue(DER.INTEGER, q));
            params.add(new DERValue(DER.INTEGER, g));
            alg.add(new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, params));
            spki.add(new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, alg));
            spki.add(new DERValue(DER.BIT_STRING, new BitString(y.toByteArray())));
            DERWriter.write(out, new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, spki));
            return (byte[]) (encodedKey = out.toByteArray()).clone();
        } catch (IOException ioe) {
            return null;
        }
    }

    public DSAParams getParams() {
        if (p == null || q == null || g == null) return null;
        return (DSAParams) (new DSAParameterSpec(p, q, g));
    }

    public BigInteger getY() {
        return y;
    }

    public String toString() {
        return "GnuDSAPublicKey: y=" + (y != null ? y.toString(16) : "(null)") + " p=" + (p != null ? p.toString(16) : "(null)") + " q=" + (q != null ? q.toString(16) : "(null)") + " g=" + (g != null ? g.toString(16) : "(null)");
    }
}
