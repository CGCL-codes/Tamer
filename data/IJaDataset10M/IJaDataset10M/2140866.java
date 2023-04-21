package com.novosec.pkix.asn1.cmp;

import java.util.Enumeration;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1TaggedObject;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;

/**
 * ASN.1 structure DER En/DeCoder.
 *
 * <pre>
 *  CertResponse ::= SEQUENCE {
 *      certReqId           INTEGER,                      -- to match this response with corresponding request (a value of -1 is to be used if certReqId is not specified in the corresponding request)
 *      status              PKIStatusInfo,
 *      certifiedKeyPair    CertifiedKeyPair    OPTIONAL,
 *      rspInfo             OCTET STRING        OPTIONAL  -- analogous to the id-regInfo-asciiPairs OCTET STRING defined for regInfo in CertReqMsg [CRMF]
 *  }
 *
 * </pre>
 */
public class CertResponse implements DEREncodable {

    DERInteger certReqId;

    PKIStatusInfo status;

    CertifiedKeyPair certifiedKeyPair;

    DEROctetString rspInfo;

    public static CertResponse getInstance(ASN1TaggedObject obj, boolean explicit) {
        return getInstance(ASN1Sequence.getInstance(obj, explicit));
    }

    public static CertResponse getInstance(Object obj) {
        if (obj instanceof CertResponse) {
            return (CertResponse) obj;
        } else if (obj instanceof ASN1Sequence) {
            return new CertResponse((ASN1Sequence) obj);
        }
        throw new IllegalArgumentException("unknown object in factory");
    }

    public CertResponse(ASN1Sequence seq) {
        Enumeration e = seq.getObjects();
        certReqId = DERInteger.getInstance(e.nextElement());
        status = PKIStatusInfo.getInstance(e.nextElement());
        Object obj = null;
        if (e.hasMoreElements()) obj = e.nextElement();
        if (obj instanceof ASN1Sequence) {
            certifiedKeyPair = CertifiedKeyPair.getInstance(obj);
            if (e.hasMoreElements()) obj = e.nextElement();
        }
        if (obj instanceof DEROctetString) rspInfo = (DEROctetString) obj;
    }

    public CertResponse(DERInteger certReqId, PKIStatusInfo status) {
        this.certReqId = certReqId;
        this.status = status;
    }

    public DERInteger getCertReqId() {
        return certReqId;
    }

    public PKIStatusInfo getStatus() {
        return status;
    }

    public void setCertifiedKeyPair(CertifiedKeyPair certifiedKeyPair) {
        this.certifiedKeyPair = certifiedKeyPair;
    }

    public CertifiedKeyPair getCertifiedKeyPair() {
        return certifiedKeyPair;
    }

    public void setRspInfo(DEROctetString rspInfo) {
        this.rspInfo = rspInfo;
    }

    public DEROctetString getRspInfo() {
        return rspInfo;
    }

    public DERObject getDERObject() {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(certReqId);
        v.add(status);
        if (certifiedKeyPair != null) v.add(certifiedKeyPair);
        if (rspInfo != null) v.add(rspInfo);
        return new DERSequence(v);
    }

    public String toString() {
        String s = "CertResponse: ( certReqId: " + this.getCertReqId() + ", status: " + this.getStatus() + ", ";
        if (this.getCertifiedKeyPair() != null) s += "certifiedKeyPair: " + this.getCertifiedKeyPair() + ", ";
        if (this.getRspInfo() != null) s += "rspInfo: " + this.getRspInfo() + ", ";
        s += ")";
        return s;
    }
}
