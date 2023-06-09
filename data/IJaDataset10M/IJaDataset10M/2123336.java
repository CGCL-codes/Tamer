package org.apache.geronimo.crypto.asn1.pkcs;

import org.apache.geronimo.crypto.asn1.ASN1Encodable;
import org.apache.geronimo.crypto.asn1.ASN1EncodableVector;
import org.apache.geronimo.crypto.asn1.ASN1Sequence;
import org.apache.geronimo.crypto.asn1.DERBitString;
import org.apache.geronimo.crypto.asn1.DERObject;
import org.apache.geronimo.crypto.asn1.DERSequence;
import org.apache.geronimo.crypto.asn1.x509.AlgorithmIdentifier;

/**
 * PKCS10 Certification request object.
 * 
 * <pre>
 * CertificationRequest ::= SEQUENCE {
 *   certificationRequestInfo  CertificationRequestInfo,
 *   signatureAlgorithm        AlgorithmIdentifier{{ SignatureAlgorithms }},
 *   signature                 BIT STRING
 * }
 * </pre>
 */
public class CertificationRequest extends ASN1Encodable {

    protected CertificationRequestInfo reqInfo = null;

    protected AlgorithmIdentifier sigAlgId = null;

    protected DERBitString sigBits = null;

    protected CertificationRequest() {
    }

    public CertificationRequest(CertificationRequestInfo requestInfo, AlgorithmIdentifier algorithm, DERBitString signature) {
        this.reqInfo = requestInfo;
        this.sigAlgId = algorithm;
        this.sigBits = signature;
    }

    public CertificationRequest(ASN1Sequence seq) {
        reqInfo = CertificationRequestInfo.getInstance(seq.getObjectAt(0));
        sigAlgId = AlgorithmIdentifier.getInstance(seq.getObjectAt(1));
        sigBits = (DERBitString) seq.getObjectAt(2);
    }

    public CertificationRequestInfo getCertificationRequestInfo() {
        return reqInfo;
    }

    public AlgorithmIdentifier getSignatureAlgorithm() {
        return sigAlgId;
    }

    public DERBitString getSignature() {
        return sigBits;
    }

    public DERObject toASN1Object() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(reqInfo);
        v.add(sigAlgId);
        v.add(sigBits);
        return new DERSequence(v);
    }
}
