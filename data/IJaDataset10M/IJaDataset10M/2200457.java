package gnu.javax.crypto.key.dh;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import gnu.java.security.OID;
import gnu.java.security.Registry;
import gnu.java.security.der.BitString;
import gnu.java.security.der.DER;
import gnu.java.security.der.DERReader;
import gnu.java.security.der.DERValue;
import gnu.java.security.der.DERWriter;
import gnu.java.security.key.IKeyPairCodec;
import gnu.java.security.util.DerUtil;

public class DHKeyPairX509Codec implements IKeyPairCodec {

    private static final OID DH_ALG_OID = new OID(Registry.DH_OID_STRING);

    public int getFormatID() {
        return X509_FORMAT;
    }

    /**
   * Returns the DER-encoded form of the X.509 ASN.1 <i>SubjectPublicKeyInfo</i>
   * representation of a DH public key. The ASN.1 specification, as defined in
   * RFC-3280, and RFC-2459, is as follows:
   *
   * <pre>
   *   SubjectPublicKeyInfo ::= SEQUENCE {
   *     algorithm         AlgorithmIdentifier,
   *     subjectPublicKey  BIT STRING
   *   }
   *
   *   AlgorithmIdentifier ::= SEQUENCE {
   *     algorithm   OBJECT IDENTIFIER,
   *     parameters  ANY DEFINED BY algorithm OPTIONAL
   *   }
   *
   *   DhParams ::= SEQUENCE {
   *     p  INTEGER, -- odd prime, p=jq +1
   *     g  INTEGER, -- generator, g
   *     q  INTEGER  -- factor of p-1
   *   }
   * </pre>
   * 
   * <p>The <i>subjectPublicKey</i> field, which is a BIT STRING, contains the
   * DER-encoded form of the DH public key as an INTEGER.</p>
   * 
   * <pre>
   *       DHPublicKey ::= INTEGER -- public key, y = g^x mod p
   * </pre>
   * <p>
   * <b>IMPORTANT</b>: with RI's {@link javax.crypto.spec.DHGenParameterSpec}
   * and {@link javax.crypto.spec.DHParameterSpec} classes, we may end up with
   * Diffie-Hellman keys that have a <code>null</code> for the <code>q</code>
   * parameter. RFC-2631 DOES NOT allow for an <i>optional</i> value for that
   * parameter, hence we replace such null values with <code>0</code>, and do
   * the reverse in the corresponding decode method.
   * 
   * @param key the {@link PublicKey} instance to encode. MUST be an instance of
   *          {@link GnuDHPublicKey}.
   * @return the DER-encoded form of the ASN.1 representation of the
   *         <i>SubjectPublicKeyInfo</i> in an X.509 certificate.
   * @throw InvalidParameterException if <code>key</code> is not an instance
   *        of {@link GnuDHPublicKey} or if an exception occurs during the
   *        marshalling process.
   */
    public byte[] encodePublicKey(PublicKey key) {
        if (!(key instanceof GnuDHPublicKey)) throw new InvalidParameterException("Wrong key type");
        DERValue derOID = new DERValue(DER.OBJECT_IDENTIFIER, DH_ALG_OID);
        GnuDHPublicKey dhKey = (GnuDHPublicKey) key;
        BigInteger p = dhKey.getParams().getP();
        BigInteger g = dhKey.getParams().getG();
        BigInteger q = dhKey.getQ();
        if (q == null) q = BigInteger.ZERO;
        BigInteger y = dhKey.getY();
        DERValue derP = new DERValue(DER.INTEGER, p);
        DERValue derG = new DERValue(DER.INTEGER, g);
        DERValue derQ = new DERValue(DER.INTEGER, q);
        ArrayList params = new ArrayList(3);
        params.add(derP);
        params.add(derG);
        params.add(derQ);
        DERValue derParams = new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, params);
        ArrayList algorithmID = new ArrayList(2);
        algorithmID.add(derOID);
        algorithmID.add(derParams);
        DERValue derAlgorithmID = new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, algorithmID);
        DERValue derDHPublicKey = new DERValue(DER.INTEGER, y);
        byte[] yBytes = derDHPublicKey.getEncoded();
        DERValue derSPK = new DERValue(DER.BIT_STRING, new BitString(yBytes));
        ArrayList spki = new ArrayList(2);
        spki.add(derAlgorithmID);
        spki.add(derSPK);
        DERValue derSPKI = new DERValue(DER.CONSTRUCTED | DER.SEQUENCE, spki);
        byte[] result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            DERWriter.write(baos, derSPKI);
            result = baos.toByteArray();
        } catch (IOException x) {
            InvalidParameterException e = new InvalidParameterException();
            e.initCause(x);
            throw e;
        }
        return result;
    }

    /**
   * @throws InvalidParameterException ALWAYS.
   */
    public byte[] encodePrivateKey(PrivateKey key) {
        throw new InvalidParameterException("Wrong format for private keys");
    }

    /**
   * @param input the byte array to unmarshall into a valid DH
   *          {@link PublicKey} instance. MUST NOT be null.
   * @return a new instance of a {@link GnuDHPublicKey} decoded from the
   *         <i>SubjectPublicKeyInfo</i> material in an X.509 certificate.
   * @throw InvalidParameterException if an exception occurs during the
   *        unmarshalling process.
   */
    public PublicKey decodePublicKey(byte[] input) {
        if (input == null) throw new InvalidParameterException("Input bytes MUST NOT be null");
        BigInteger p, g, q, y;
        DERReader der = new DERReader(input);
        try {
            DERValue derSPKI = der.read();
            DerUtil.checkIsConstructed(derSPKI, "Wrong SubjectPublicKeyInfo field");
            DERValue derAlgorithmID = der.read();
            DerUtil.checkIsConstructed(derAlgorithmID, "Wrong AlgorithmIdentifier field");
            DERValue derOID = der.read();
            if (!(derOID.getValue() instanceof OID)) throw new InvalidParameterException("Wrong Algorithm field");
            OID algOID = (OID) derOID.getValue();
            if (!algOID.equals(DH_ALG_OID)) throw new InvalidParameterException("Unexpected OID: " + algOID);
            DERValue derParams = der.read();
            DerUtil.checkIsConstructed(derParams, "Wrong DH Parameters field");
            DERValue val = der.read();
            DerUtil.checkIsBigInteger(val, "Wrong P field");
            p = (BigInteger) val.getValue();
            val = der.read();
            DerUtil.checkIsBigInteger(val, "Wrong G field");
            g = (BigInteger) val.getValue();
            val = der.read();
            DerUtil.checkIsBigInteger(val, "Wrong Q field");
            q = (BigInteger) val.getValue();
            if (q.compareTo(BigInteger.ZERO) == 0) q = null;
            val = der.read();
            if (!(val.getValue() instanceof BitString)) throw new InvalidParameterException("Wrong SubjectPublicKey field");
            byte[] yBytes = ((BitString) val.getValue()).toByteArray();
            DERReader dhPub = new DERReader(yBytes);
            val = dhPub.read();
            DerUtil.checkIsBigInteger(val, "Wrong Y field");
            y = (BigInteger) val.getValue();
        } catch (IOException x) {
            InvalidParameterException e = new InvalidParameterException();
            e.initCause(x);
            throw e;
        }
        return new GnuDHPublicKey(Registry.X509_ENCODING_ID, q, p, g, y);
    }

    /**
   * @throws InvalidParameterException ALWAYS.
   */
    public PrivateKey decodePrivateKey(byte[] input) {
        throw new InvalidParameterException("Wrong format for private keys");
    }
}
