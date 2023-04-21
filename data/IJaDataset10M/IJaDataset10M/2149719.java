package codec.x509.extensions;

import java.io.IOException;
import codec.asn1.ASN1Enumerated;
import codec.asn1.ASN1Exception;
import codec.asn1.ASN1ObjectIdentifier;
import codec.asn1.ASN1Type;
import codec.asn1.Decoder;
import codec.x509.X509Exception;
import codec.x509.X509Extension;

/**
 * @author mal
 * 
 * <pre>
 *  
 * id-ce-cRLReason OBJECT IDENTIFIER ::= { id-ce 21 }
 * 
 * reasonCode = { CRLReason }
 * 
 * CRLReason ::= ENUMERATED {
 *    unspecified             (0),
 *    keyCompromise           (1),
 *    cACompromise            (2),
 *    affiliationChanged      (3),
 *    superseded              (4),
 *    cessationOfOperation    (5),
 *    certificateHold         (6),
 *    removeFromCRL           (8) }
 *  id-ce OBJECT IDENTIFIER  ::=  {joint-iso-ccitt(2) ds(5) 29}
 * }
 * </pre>
 * 
 */
public class ReasonCodeExtension extends X509Extension {

    /**
     * This is the object identifier (OID) of this extension
     */
    protected static final String ID_CE_CRL_REASON = new String("2.5.29.21");

    protected ASN1Enumerated theReason;

    /**
     * These are the possible reason codes
     */
    public static final int REASON_UNSPECIFIED = 0;

    public static final int REASON_KEY_COMPROMISE = 1;

    public static final int REASON_CA_COMPROMISE = 2;

    public static final int REASON_AFFILIATION_CHANGE = 3;

    public static final int REASON_SUPERSEDED = 4;

    public static final int REASON_CESSATION_OF_OPERATION = 5;

    public static final int REASON_CERTIFICATE_HOLD = 6;

    public static final int REASON_REMOVE_FROM_CRL = 8;

    /**
     * Constructor for ReasonCodeExtension.
     * 
     * @throws Exception
     */
    public ReasonCodeExtension() throws Exception {
        this(REASON_UNSPECIFIED);
    }

    public ReasonCodeExtension(int aReason) throws Exception {
        super.setOID(new ASN1ObjectIdentifier(ID_CE_CRL_REASON));
        setReasonCode(aReason);
    }

    /**
     * This constructor basically calls the related constructor in the base
     * class.
     * 
     * @param ext
     * @throws ASN1Exception
     * @throws IOException
     */
    public ReasonCodeExtension(byte[] ext) throws ASN1Exception, IOException {
        super(ext);
    }

    public void setReasonCode(int aReason) throws Exception {
        if ((aReason < 0) || (aReason == 7) || (aReason > 8)) {
            throw new X509Exception("Reasoncode unknown");
        }
        theReason = new ASN1Enumerated(aReason);
        super.setValue(theReason);
    }

    public void decode(Decoder dec) throws ASN1Exception, IOException {
        super.decode(dec);
        ASN1Type inner = (ASN1Type) super.getValue();
        if (!(inner instanceof ASN1Enumerated)) {
            throw new ASN1Exception("unexpected extension value " + inner.toString());
        }
        theReason = (ASN1Enumerated) inner;
    }

    public String toString() {
        return toString("");
    }

    public String toString(String offset) {
        StringBuffer buf = new StringBuffer(offset + "ReasonCodeExtension [" + ID_CE_CRL_REASON + "] {");
        if (isCritical()) {
            buf.append(" (CRITICAL)\n");
        } else {
            buf.append(" (NOT CRITICAL)\n");
        }
        buf.append(offset + "reason: ");
        switch(((ASN1Enumerated) getValue()).getBigInteger().intValue()) {
            case REASON_UNSPECIFIED:
                buf.append("unspecified");
                break;
            case REASON_KEY_COMPROMISE:
                buf.append("key compromise");
                break;
            case REASON_AFFILIATION_CHANGE:
                buf.append("affiliation change");
                break;
            case REASON_SUPERSEDED:
                buf.append("superseded");
                break;
            case REASON_CESSATION_OF_OPERATION:
                buf.append("cessation of operation");
                break;
            case REASON_CERTIFICATE_HOLD:
                buf.append("certificate hold");
                break;
            case REASON_REMOVE_FROM_CRL:
                buf.append("remove from crl");
                break;
            default:
                buf.append("unknown reason code");
                break;
        }
        buf.append("\n" + offset + "}\n");
        return buf.toString();
    }
}
