package org.apache.harmony.security.pkcs7;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import javax.security.auth.x500.X500Principal;
import org.apache.harmony.security.asn1.ASN1Implicit;
import org.apache.harmony.security.asn1.ASN1Integer;
import org.apache.harmony.security.asn1.ASN1OctetString;
import org.apache.harmony.security.asn1.ASN1Sequence;
import org.apache.harmony.security.asn1.ASN1SetOf;
import org.apache.harmony.security.asn1.ASN1Type;
import org.apache.harmony.security.asn1.BerInputStream;
import org.apache.harmony.security.internal.nls.Messages;
import org.apache.harmony.security.x501.AttributeTypeAndValue;
import org.apache.harmony.security.x501.Name;
import org.apache.harmony.security.x509.AlgorithmIdentifier;

/**
 * As defined in PKCS #7: Cryptographic Message Syntax Standard
 * (http://www.ietf.org/rfc/rfc2315.txt)
 * 
 * SignerInfo ::= SEQUENCE {
 *   version Version,
 *   issuerAndSerialNumber IssuerAndSerialNumber,
 *   digestAlgorithm DigestAlgorithmIdentifier,
 *   authenticatedAttributes
 *     [0] IMPLICIT Attributes OPTIONAL,
 *   digestEncryptionAlgorithm
 *     DigestEncryptionAlgorithmIdentifier,
 *   encryptedDigest EncryptedDigest,
 *   unauthenticatedAttributes
 *     [1] IMPLICIT Attributes OPTIONAL
 *  }
 * 
 */
public class SignerInfo {

    private int version;

    private X500Principal issuer;

    private BigInteger serialNumber;

    private AlgorithmIdentifier digestAlgorithm;

    private AuthenticatedAttributes authenticatedAttributes;

    private AlgorithmIdentifier digestEncryptionAlgorithm;

    private byte[] encryptedDigest;

    private List unauthenticatedAttributes;

    public SignerInfo(int version, Object[] issuerAndSerialNumber, AlgorithmIdentifier digestAlgorithm, AuthenticatedAttributes authenticatedAttributes, AlgorithmIdentifier digestEncryptionAlgorithm, byte[] encryptedDigest, List unauthenticatedAttributes) {
        this.version = version;
        this.issuer = ((Name) issuerAndSerialNumber[0]).getX500Principal();
        this.serialNumber = BigInteger.valueOf(ASN1Integer.toIntValue(issuerAndSerialNumber[1]));
        this.digestAlgorithm = digestAlgorithm;
        this.authenticatedAttributes = authenticatedAttributes;
        this.digestEncryptionAlgorithm = digestEncryptionAlgorithm;
        this.encryptedDigest = encryptedDigest;
        this.unauthenticatedAttributes = unauthenticatedAttributes;
    }

    public X500Principal getIssuer() {
        return issuer;
    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public String getDigestAlgorithm() {
        return digestAlgorithm.getAlgorithm();
    }

    public String getdigestAlgorithm() {
        return digestAlgorithm.getAlgorithm();
    }

    public String getDigestEncryptionAlgorithm() {
        return digestEncryptionAlgorithm.getAlgorithm();
    }

    public List getAuthenticatedAttributes() {
        if (authenticatedAttributes == null) {
            return null;
        }
        return authenticatedAttributes.getAttributes();
    }

    public byte[] getEncodedAuthenticatedAttributes() {
        if (authenticatedAttributes == null) {
            return null;
        }
        return authenticatedAttributes.getEncoded();
    }

    public byte[] getEncryptedDigest() {
        return encryptedDigest;
    }

    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("-- SignerInfo:");
        res.append("\n version : ");
        res.append(version);
        res.append("\nissuerAndSerialNumber:  ");
        res.append(issuer);
        res.append("   ");
        res.append(serialNumber);
        res.append("\ndigestAlgorithm:  ");
        res.append(digestAlgorithm.toString());
        res.append("\nauthenticatedAttributes:  ");
        if (authenticatedAttributes != null) {
            res.append(authenticatedAttributes.toString());
        }
        res.append("\ndigestEncryptionAlgorithm: ");
        res.append(digestEncryptionAlgorithm.toString());
        res.append("\nunauthenticatedAttributes: ");
        if (unauthenticatedAttributes != null) {
            res.append(unauthenticatedAttributes.toString());
        }
        res.append("\n-- SignerInfo End\n");
        return res.toString();
    }

    public static final ASN1Sequence ISSUER_AND_SERIAL_NUMBER = new ASN1Sequence(new ASN1Type[] { Name.ASN1, ASN1Integer.getInstance() }) {

        public void getValues(Object object, Object[] values) {
            Object[] issAndSerial = (Object[]) object;
            values[0] = issAndSerial[0];
            values[1] = issAndSerial[1];
        }
    };

    public static final ASN1Sequence ASN1 = new ASN1Sequence(new ASN1Type[] { ASN1Integer.getInstance(), ISSUER_AND_SERIAL_NUMBER, AlgorithmIdentifier.ASN1, new ASN1Implicit(0, AuthenticatedAttributes.ASN1), AlgorithmIdentifier.ASN1, ASN1OctetString.getInstance(), new ASN1Implicit(1, new ASN1SetOf(AttributeTypeAndValue.ASN1)) }) {

        {
            setOptional(3);
            setOptional(6);
        }

        protected void getValues(Object object, Object[] values) {
            SignerInfo si = (SignerInfo) object;
            values[0] = new byte[] { (byte) si.version };
            try {
                values[1] = new Object[] { new Name(si.issuer.getName()), si.serialNumber.toByteArray() };
            } catch (IOException e) {
                throw new RuntimeException(Messages.getString("security.1A2"), e);
            }
            values[2] = si.digestAlgorithm;
            values[3] = si.authenticatedAttributes;
            values[4] = si.digestEncryptionAlgorithm;
            values[5] = si.encryptedDigest;
            values[6] = si.unauthenticatedAttributes;
        }

        protected Object getDecodedObject(BerInputStream in) {
            Object[] values = (Object[]) in.content;
            return new SignerInfo(ASN1Integer.toIntValue(values[0]), (Object[]) values[1], (AlgorithmIdentifier) values[2], (AuthenticatedAttributes) values[3], (AlgorithmIdentifier) values[4], (byte[]) values[5], (List) values[6]);
        }
    };
}
