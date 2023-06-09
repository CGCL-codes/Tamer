package codec.x509;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.cert.CertificateEncodingException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;
import codec.asn1.ASN1BitString;
import codec.asn1.ASN1Boolean;
import codec.asn1.ASN1Choice;
import codec.asn1.ASN1Exception;
import codec.asn1.ASN1GeneralizedTime;
import codec.asn1.ASN1Integer;
import codec.asn1.ASN1Sequence;
import codec.asn1.ASN1SequenceOf;
import codec.asn1.ASN1TaggedType;
import codec.asn1.ASN1Time;
import codec.asn1.ASN1UTCTime;
import codec.asn1.ConstraintException;
import codec.asn1.DERDecoder;
import codec.asn1.DEREncoder;
import codec.x501.Name;

/**
 * Implements a X.509v3 certificate TBS block according to the following ASN.1
 * data structure:
 * <p>
 * 
 * <pre>
 * TBSCertificate  ::=  SEQUENCE  {
 *  version         		[0]	EXPLICIT Version DEFAULT v1,
 *  serialNumber			CertificateSerialNumber,
 *  signature				AlgorithmIdentifier,
 *  issuer					Name,
 *  validity				Validity,
 *  subject					Name,
 *  subjectPublicKeyInfo	SubjectPublicKeyInfo,
 *  issuerUniqueID			[1]	IMPLICIT UniqueIdentifier OPTIONAL,  -- If present, version must be v2 or v3
 *  subjectUniqueID			[2] IMPLICIT UniqueIdentifier OPTIONAL, -- If present, version must be v2 or v3
 *  extensions      		[3] EXPLICIT Extensions OPTIONAL          -- If present, version must be v3
 * }
 * UniqueIdentifier  ::=  BIT STRING
 * Validity ::= SEQUENCE {
 *       notBefore      Time,
 *       notAfter       Time
 * }
 * Time ::= CHOICE {
 *      utcTime        UTCTime,
 *      generalTime    GeneralizedTime
 * }
 * </pre>
 * 
 * If you want to create a certificate, you should create a
 * {@link X509TBSCertificate X509TBSCertificate}, fill it with useful data
 * (certificate serial number, validity period, subject and issuer DN, subject
 * public key) and the signature algorithm!
 * <p>
 * Note that you have to set the signature algorithm before encoding a
 * X509TBSCertificate or putting it into a X509Certificate!
 * <p>
 * Certificate version will be set automatically to "V2" if issuerUniqueID or
 * subjectUniqueID is set and to "V3" if any extension is added. Version
 * defaults to "V1".
 * <p>
 * Example:
 * 
 * <pre>
 * ... tbd
 * </pre>
 * 
 * @author Markus Tak
 */
public class X509TBSCertificate extends ASN1Sequence implements Externalizable {

    /**
     * The default version identifier for this class, which is v3(2).
     */
    public static final int DEFAULT_VERSION = 2;

    private ASN1Integer version_ = null;

    private ASN1TaggedType versionTag_ = null;

    private ASN1Integer serialNumber_ = null;

    private AlgorithmIdentifier signature_ = null;

    private Name issuer_ = null;

    private ASN1Choice notBefore_ = null;

    private ASN1Choice notAfter_ = null;

    private Name subject_ = null;

    private SubjectPublicKeyInfo subjectPublicKeyInfo_ = null;

    private ASN1BitString issuerUniqueID_ = null;

    private ASN1TaggedType issuerUniqueIDTag_ = null;

    private ASN1BitString subjectUniqueID_ = null;

    private ASN1TaggedType subjectUniqueIDTag_ = null;

    private ASN1SequenceOf extensions_ = null;

    private ASN1TaggedType extensionsTag_ = null;

    /**
     * Constructor that builds the data structure
     */
    public X509TBSCertificate() {
        super(10);
        version_ = new ASN1Integer(DEFAULT_VERSION);
        versionTag_ = new ASN1TaggedType(0, version_, true, false);
        add(versionTag_);
        serialNumber_ = new ASN1Integer();
        add(serialNumber_);
        signature_ = new codec.x509.AlgorithmIdentifier();
        add(signature_);
        issuer_ = new codec.x501.Name();
        add(issuer_);
        ASN1Sequence validity = new ASN1Sequence();
        notBefore_ = new ASN1Choice();
        notBefore_.addType(new ASN1UTCTime());
        notBefore_.addType(new ASN1GeneralizedTime());
        validity.add(notBefore_);
        notAfter_ = new ASN1Choice();
        notAfter_.addType(new ASN1UTCTime());
        notAfter_.addType(new ASN1GeneralizedTime());
        validity.add(notAfter_);
        add(validity);
        subject_ = new codec.x501.Name();
        add(subject_);
        subjectPublicKeyInfo_ = new SubjectPublicKeyInfo();
        add(subjectPublicKeyInfo_);
        issuerUniqueID_ = new ASN1BitString();
        issuerUniqueIDTag_ = new ASN1TaggedType(1, issuerUniqueID_, false, true);
        add(issuerUniqueIDTag_);
        subjectUniqueID_ = new ASN1BitString();
        subjectUniqueIDTag_ = new ASN1TaggedType(2, subjectUniqueID_, false, true);
        add(subjectUniqueIDTag_);
        extensions_ = new ASN1SequenceOf(X509Extension.class);
        extensionsTag_ = new ASN1TaggedType(3, extensions_, true, true);
        add(extensionsTag_);
    }

    /**
     * Adds an extension to this certificate. Note that calling this method
     * automatically means setting the version field to "2" (X.509 version V3)
     * 
     * @param ext
     *                the extension to be added.
     */
    public void addExtension(X509Extension ext) {
        extensions_.add(ext);
        extensionsTag_.setOptional(false);
    }

    /**
     * From java.security.cert.X509Certificate. Returns the value of the
     * pathLenConstraint in a BC extension if present and cA set to true. If the
     * Basic Constraints extension (OID 2.5.29.19) is not present in this
     * certificate, null is returned.
     * 
     * <pre>
     * BasicConstraints ::= SEQUENCE {
     * cA                  BOOLEAN DEFAULT FALSE,
     *  pathLenConstraint   INTEGER (0..MAX) OPTIONAL
     * }
     * </pre>
     * 
     * @return the value of pathLenConstraint if present and cA set to true or
     *          null if the extension is not present
     */
    public int getBasicConstraints() {
        int res = -1;
        byte[] ext_value;
        DERDecoder dec;
        ByteArrayInputStream bais;
        ASN1Sequence seq;
        ASN1Integer pathLen;
        ASN1Boolean ca;
        String bc_oid = "2.5.29.19";
        ext_value = getExtensionValue(bc_oid);
        if (ext_value != null) {
            bais = new ByteArrayInputStream(ext_value);
            try {
                seq = new ASN1Sequence();
                ca = new ASN1Boolean();
                ca.setOptional(true);
                seq.add(ca);
                pathLen = new ASN1Integer();
                pathLen.setOptional(true);
                seq.add(pathLen);
                dec = new DERDecoder(bais);
                seq.decode(dec);
                bais.close();
                if (ca.isTrue()) {
                    res = pathLen.getBigInteger().intValue();
                }
            } catch (Exception e) {
                System.out.println("gbc Internal Error.Shouldnt happen");
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * From java.security.cert.X509Extension. Gets a set of Strings containing
     * all extension oids present being marked as critical.
     */
    public Set getCriticalExtensionOIDs() {
        X509Extension theEx;
        HashSet res = new HashSet();
        if (extensionsTag_.isOptional()) return null;
        try {
            Iterator it = extensions_.iterator();
            while (it.hasNext()) {
                theEx = (X509Extension) it.next();
                if (theEx.isCritical()) res.add(theEx.getOID().toString());
            }
        } catch (Exception ignore) {
            System.out.println("gcritoid Internal Error.Shouldnt happen");
            ignore.printStackTrace();
        }
        return res;
    }

    /**
     * returns the DER-encoded bytearray of this certificate
     * 
     * @throws CertificateEncodingException
     *                 if TBSCertificate could not be encoded correctly
     */
    public byte[] getEncoded() throws CertificateEncodingException {
        byte[] res = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            encode(new DEREncoder(baos));
            res = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            System.out.println("internal error:");
            e.printStackTrace();
        } catch (ASN1Exception e) {
            throw new CertificateEncodingException(e.getMessage());
        }
        return res;
    }

    /**
     * Returns a Collection containing all extensions
     */
    public Collection getExtensions() {
        if (extensionsTag_.isOptional()) return null;
        return extensions_;
    }

    /**
     * From java.security.cert.X509Extension. Gets the value of the extensions
     * denoted by ex or null if not present.
     */
    public byte[] getExtensionValue(String ex) {
        X509Extension theEx;
        byte[] res = null;
        if (extensionsTag_.isOptional()) return null;
        try {
            Iterator it = extensions_.iterator();
            while (it.hasNext()) {
                theEx = (X509Extension) it.next();
                if (theEx.getOID().toString().equals(ex)) {
                    res = (byte[]) theEx.getValue();
                    break;
                }
            }
        } catch (Exception ignore) {
            System.out.println("getextval Internal Error.Shouldnt happen");
            ignore.printStackTrace();
        }
        return res;
    }

    /**
     * From java.security.cert.X509Certificate. Returns this certificate's
     * issuer as a Principal.
     */
    public java.security.Principal getIssuerDN() {
        return issuer_;
    }

    /**
     * From java.security.cert.X509Certificate. Returns the issuer's Unique ID
     * or null if not present.
     */
    public boolean[] getIssuerUniqueID() {
        if (issuerUniqueIDTag_.isOptional()) return null;
        return issuerUniqueID_.getBits();
    }

    /**
     * From java.security.cert.X509Certificate. Returns the bits of the KeyUsage
     * extension (OID 2.5.29.15) if present in this certificate or null
     * otherwise.
     * 
     * <pre>
     * KeyUsage ::= BIT STRING {
     *        digitalSignature        (0),
     *        nonRepudiation          (1),
     *        keyEncipherment         (2),
     *        dataEncipherment        (3),
     *        keyAgreement            (4),
     *        keyCertSign             (5),
     *        cRLSign                 (6),
     *        encipherOnly            (7),
     *        decipherOnly            (8)
     * }
     * </pre>
     * 
     * @return the key usage bits if present in this certificate, otherwise
     *          null.
     */
    public boolean[] getKeyUsage() {
        boolean[] res = null;
        byte[] ext_value;
        DERDecoder dec;
        ByteArrayInputStream bais;
        ASN1BitString bits;
        String ku_oid = "2.5.29.15";
        ext_value = getExtensionValue(ku_oid);
        if (ext_value != null) {
            bais = new ByteArrayInputStream(ext_value);
            try {
                bits = new ASN1BitString();
                dec = new DERDecoder(bais);
                bits.decode(dec);
                bais.close();
                res = bits.getBits();
            } catch (Exception e) {
                System.out.println("Internal Error.Shouldnt happen");
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * From java.security.cert.X509Extension. Gets a set of Strings containing
     * all extension oids present being marked as critical.
     */
    public Set getNonCriticalExtensionOIDs() {
        HashSet res = new HashSet();
        if (extensionsTag_.isOptional()) return null;
        try {
            Iterator it = extensions_.iterator();
            while (it.hasNext()) {
                X509Extension theEx = (X509Extension) it.next();
                if (!theEx.isCritical()) res.add(theEx.getOID().getOID());
            }
        } catch (Exception ignore) {
        }
        return res;
    }

    /**
     * From java.security.cert.X509Certificate. Returns the Date after which
     * this certificate is not valid anymore.
     */
    public Date getNotAfter() {
        return ((ASN1Time) notAfter_.getInnerType()).getDate();
    }

    /**
     * From java.security.cert.X509Certificate. Returns the Date before which
     * this certificate is not valid.
     */
    public Date getNotBefore() {
        return ((ASN1Time) notBefore_.getInnerType()).getDate();
    }

    /**
     * From java.security.cert.X509Certificate. Returns the Public Key inside
     * this certificate
     */
    public java.security.PublicKey getPublicKey() throws NoSuchAlgorithmException {
        return subjectPublicKeyInfo_.getPublicKey();
    }

    /**
     * From java.security.cert.X509Certificate. Returns the Serial Number of
     * this certificate
     */
    public BigInteger getSerialNumber() {
        return serialNumber_.getBigInteger();
    }

    /**
     * From java.security.cert.X509Certificate. Returns the JCA-compliant
     * Algorithm Name of the signature algorithm.
     */
    public String getSigAlgName() {
        return codec.util.JCA.getName(getSigAlgOID());
    }

    /**
     * From java.security.cert.X509Certificate. Returns the Object Identifier
     * (OID) of the signature algorithm.
     */
    public String getSigAlgOID() {
        return signature_.getAlgorithmOID().toString();
    }

    /**
     * From java.security.cert.X509Certificate. Returns the Algorithm Parameters
     * for the signature algorithm in a DER encoded form.
     */
    public byte[] getSigAlgParams() {
        byte[] res = null;
        try {
            res = signature_.getParameters().getEncoded();
        } catch (Exception intern) {
            System.out.println("internal Error:");
            intern.printStackTrace();
        }
        return res;
    }

    /**
     * From java.security.cert.X509Certificate. Returns this certificate's
     * subject as a Principal.
     */
    public java.security.Principal getSubjectDN() {
        return subject_;
    }

    /**
     * From java.security.cert.X509Certificate. Returns the subject's Unique ID
     * or null if not present.
     */
    public boolean[] getSubjectUniqueID() {
        if (issuerUniqueIDTag_.isOptional()) return null;
        return issuerUniqueID_.getBits();
    }

    /**
     * Returns the version of this X509 certificate (0=v1, 1=v2, 2=v3)
     */
    public int getVersion() {
        if (versionTag_.isOptional()) {
            return 0;
        }
        return version_.getBigInteger().intValue();
    }

    /**
     * Sets the version number of this instance explicitly. If the version
     * number is '0' then the version representation is set to OPTIONAL. Please
     * note that the version number passed to this method is the internal
     * version number identifier. Hence, a '2' must be passed if the certificate
     * shall be a version 3 certificate.
     * 
     * @param version
     *                The internal version number, one of v1(0), v2(1), v3(2).
     * @throws IllegalArgumentException
     *                 if version number is smaller than 2 but extensions are
     *                 present.
     */
    public void setVersion(int version) {
        ASN1TaggedType tt;
        boolean opt;
        if (version < 0 || version > 2) {
            throw new IllegalArgumentException("illegal version number: " + version);
        }
        if (version < 2 && !extensionsTag_.isOptional()) {
            throw new IllegalArgumentException("can't set to " + version + ", extensions are present");
        }
        opt = (version == 0);
        tt = new ASN1TaggedType(0, new ASN1Integer(version), true, opt);
        set(0, tt);
    }

    /**
     * From java.security.cert.X509Extension. Returns true if this certificate
     * contains any extension being marked as critical but not supported by this
     * implementation.
     * <p>
     * Currently, this function will always return false since extensions are
     * managed in an abstract way.
     */
    public boolean hasUnsupportedCriticalExtension() {
        return false;
    }

    /**
     * needed to explicitly name an encoding method in order to change between
     * encodings during runtime.
     * 
     * @param nissuer
     *                the name of the issuer
     * @param encType
     *                ITU Tag of the Stringtype
     */
    public void setIssuerDN(Principal nissuer, int encType) {
        if (nissuer instanceof codec.x501.Name) {
            issuer_ = (codec.x501.Name) nissuer;
        } else {
            try {
                issuer_ = new codec.x501.Name(nissuer.getName(), encType);
            } catch (Exception e) {
                System.out.println("Internal Error:");
                e.printStackTrace();
                return;
            }
        }
        set(3, issuer_);
    }

    /**
     * Sets the issuers distinguished name (DN). This method is especially for
     * issuing a certificate.
     * 
     * @param nissuer
     *                the Principal object describing the issuer.
     */
    public void setIssuerDN(Principal nissuer) {
        if (nissuer instanceof codec.x501.Name) {
            issuer_ = (codec.x501.Name) nissuer;
        } else {
            try {
                issuer_ = new codec.x501.Name(nissuer.getName(), codec.x501.Name.PRINTABLE_ENCODING);
            } catch (Exception e) {
                System.out.println("Internal Error:");
                e.printStackTrace();
                return;
            }
        }
        set(3, issuer_);
    }

    /**
     * Sets the issuer's unique id. This method is especially for issuing a
     * certificate.
     * 
     * @param nid
     *                the issuer's unique id
     */
    public void setIssuerUniqueID(byte[] nid) {
        try {
            issuerUniqueID_.setBits(nid, 0);
            issuerUniqueIDTag_.setOptional(false);
        } catch (ConstraintException e) {
            System.out.println("internal error. shouldnt happen:");
            e.printStackTrace();
        }
    }

    /**
     * Sets the "not after" field. This method is especially for issuing a
     * certificate.
     * 
     * @param nnaf
     *                "not after" date
     */
    public void setNotAfter(Calendar nnaf) {
        ASN1Time nai = (ASN1Time) notAfter_.getInnerType();
        if (nai == null) {
            nai = new ASN1UTCTime();
            notAfter_.setInnerType(nai);
        }
        nai.setDate(nnaf);
    }

    /**
     * Sets the "not after" field. This method is especially for issuing a
     * certificate.
     * 
     * @param nnaf
     *                "not after" date
     */
    public void setNotAfter(Date nnaf) {
        Calendar x = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        x.setTime(nnaf);
        setNotAfter(x);
    }

    /**
     * Sets the "not before" field. This method is especially for issuing a
     * certificate.
     * 
     * @param nnbf
     *                "not before" date
     */
    public void setNotBefore(Calendar nnbf) {
        ASN1Time nai = (ASN1Time) notBefore_.getInnerType();
        if (nai == null) {
            nai = new ASN1UTCTime();
            notBefore_.setInnerType(nai);
        }
        nai.setDate(nnbf);
    }

    /**
     * Sets the "not before" field. This method is especially for issuing a
     * certificate.
     * 
     * @param nnbf
     *                "not before" date
     */
    public void setNotBefore(Date nnbf) {
        Calendar x = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        x.setTime(nnbf);
        setNotBefore(x);
    }

    /**
     * Sets the serial number of this certificate
     */
    public void setSerialNumber(int nsnr) {
        setSerialNumber(new BigInteger(String.valueOf(nsnr), 10));
    }

    /**
     * Sets the serial number of this certificate
     */
    public void setSerialNumber(BigInteger nsnr) {
        try {
            serialNumber_.setBigInteger(nsnr);
        } catch (ConstraintException ignore) {
        }
    }

    /**
     * Sets the signature algorithm. Note that the AlgorithmIdentifier will be
     * cloned in order to prevent side-effects
     * 
     * @param aid
     *                AlgorithmID of the signature algorithm
     */
    public void setSignatureAlgorithm(codec.x509.AlgorithmIdentifier aid) {
        signature_ = (codec.x509.AlgorithmIdentifier) aid.clone();
        set(2, signature_);
    }

    /**
     * Sets the subject's distinguished name (DN). This method is especially for
     * issuing a certificate.
     * 
     * @param nsubject
     *                the Principal object describing the subject.
     */
    public void setSubjectDN(Principal nsubject) {
        if (nsubject instanceof codec.x501.Name) {
            subject_ = (codec.x501.Name) nsubject;
        } else {
            try {
                subject_ = new codec.x501.Name(nsubject.getName(), Name.IA5_ENCODING);
            } catch (Exception e) {
                System.out.println("Internal Error:");
                e.printStackTrace();
                return;
            }
        }
        set(5, subject_);
    }

    /**
     * same as above but with an explicit name encoding
     * 
     * @param nsubject
     * @param encType
     *                (the String Type the Name shall be encoded.
     */
    public void setSubjectDN(Principal nsubject, int encType) {
        if (nsubject instanceof codec.x501.Name) {
            subject_ = (codec.x501.Name) nsubject;
        } else {
            try {
                subject_ = new codec.x501.Name(nsubject.getName(), encType);
            } catch (Exception e) {
                System.out.println("Internal Error:");
                e.printStackTrace();
                return;
            }
        }
        set(5, subject_);
    }

    /**
     * Sets the subject's public key
     */
    public void setSubjectPublicKey(java.security.PublicKey pk) throws InvalidKeyException {
        subjectPublicKeyInfo_.setPublicKey(pk);
    }

    /**
     * Sets the subjects's unique id. This method is especially for issuing a
     * certificate.
     * 
     * @param nid
     *                the subjects's unique id
     */
    public void setSubjectUniqueID(byte[] nid) {
        try {
            subjectUniqueID_.setBits(nid, 0);
            subjectUniqueIDTag_.setOptional(false);
        } catch (ConstraintException e) {
            System.out.println("internal error. shouldnt happen:");
            e.printStackTrace();
        }
        if (extensionsTag_.isOptional() || versionTag_.isOptional()) {
            versionTag_.setOptional(false);
            try {
                version_.setBigInteger(new BigInteger("1"));
            } catch (ConstraintException ignore) {
            }
        }
    }

    /**
     * human-readable String representation of this certificate
     */
    public String toString() {
        String res = "";
        int vrs;
        if (versionTag_.isOptional()) vrs = 1; else vrs = version_.getBigInteger().intValue() + 1;
        res = "TBS Certificate {";
        res = res + "\nX509 certificate V" + vrs;
        res = res + "\nserial number:" + serialNumber_.toString();
        res = res + "\nsignature algorithm:" + signature_.toString();
        res = res + "\nissuer:" + issuer_.getName();
        res = res + "\nsubject:" + subject_.getName();
        res = res + "\nvalidity:" + ((ASN1Time) notBefore_.getInnerType()).toString();
        res = res + " - " + ((ASN1Time) notAfter_.getInnerType()).toString();
        res = res + "\nsubject public key algorithm:" + subjectPublicKeyInfo_.getAlgorithmIdentifier().toString();
        res = res + "\nsubject public key:";
        try {
            res = res + "\n" + subjectPublicKeyInfo_.getPublicKey().toString();
        } catch (codec.InconsistentStateException ise) {
            res = res + "<key data corrupted!>";
        } catch (NoSuchAlgorithmException cce) {
            res = res + "<key algorithm not supported!>";
        }
        if (!issuerUniqueIDTag_.isOptional()) res = res + "\n(V2)issuer unique ID:" + issuerUniqueID_.toString();
        if (!subjectUniqueIDTag_.isOptional()) res = res + "\n(V2)subject unique ID:" + subjectUniqueID_.toString();
        if (!extensionsTag_.isOptional() && !extensions_.isEmpty()) {
            res = res + "\n(V3)extensions (" + extensions_.size() + ")\n";
            Iterator it = extensions_.iterator();
            int j = 1;
            while (it.hasNext()) {
                res = res + " " + String.valueOf(j++) + ":";
                try {
                    X509Extension mye = (X509Extension) it.next();
                    res = res + mye.toString(" ") + "\n";
                } catch (Exception e) {
                    res = res + " not handled:" + e.getMessage() + "\n";
                }
            }
        }
        return res;
    }
}
