package org.infoeng.ofbiz.ltans.scep;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERString;
import org.bouncycastle.asn1.pkcs.CertificationRequest;
import org.bouncycastle.asn1.pkcs.CertificationRequestInfo;
import org.infoeng.ofbiz.ltans.LTANSObject;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.cms.EnvelopedData;
import org.bouncycastle.asn1.cms.SignedData;
import java.io.IOException;
import java.math.BigInteger;
import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.cms.*;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.nist.*;
import org.bouncycastle.asn1.pkcs.*;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.jce.*;
import org.bouncycastle.jce.provider.*;
import java.security.*;
import java.security.spec.*;
import java.security.cert.*;
import java.security.interfaces.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.*;
import org.infoeng.ofbiz.ltans.util.*;

/**
 *
  <pre>
  pkcsCertReq CertificationRequest ::= {   -- PKCS#10 
      version 0 
      subject  "the requester's subject name" 
      subjectPublicKeyInfo { 
          algorithm {pkcs-1 1}  -- rsa encryption 
          subjectPublicKey "DER encoding of the requester's public key"
      } 
      attributes { 
          challengePassword {{pkcs-9 7} "password string" } 
          extensions 
      } 
      signatureAlgorithm {pkcs-1 4} -- MD5WithRSAEncryption 
      signature "bit string which is created by signing inner content 
                of the defined pkcsCertReq using requester's private
                key, corresponding to the public key included in 
                subjectPublicKeyInfo." 
  } 
  </pre>
  *
  */
public class PKCSRequest extends LTANSObject implements DEREncodable {

    private CertificationRequest pkcsCertReq;

    SignedData pkcsCertReqSigned;

    private X509Name requestorName;

    private DERInteger serialNumber;

    DERInteger transactionId;

    public static final BigInteger requestValidity = BigInteger.valueOf(1000000);

    public PKCSRequest(String challengePasswordIn, X509Name requestorName, KeyPair requestorKeyPair, X509Certificate cert) throws Exception {
        this.transactionId = new DERInteger(new BigInteger(20, new Random()));
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(PKCSObjectIdentifiers.pkcs_9_at_challengePassword);
        v.add(new DERPrintableString(challengePasswordIn));
        DERSet attrSet = new DERSet(v);
        Attribute attr = new Attribute(PKCSObjectIdentifiers.pkcs_9_at_challengePassword, attrSet);
        CertificationRequestInfo cri = new CertificationRequestInfo(requestorName, new SubjectPublicKeyInfo(LtansUtils.getAlgorithmIdentifier(requestorKeyPair.getPublic().getAlgorithm()), requestorKeyPair.getPublic().getEncoded()), new DERSet(attrSet));
        Signature pkcsCertSig = Signature.getInstance(LtansUtils.getJavaAlgorithmIdentifier(requestorKeyPair.getPrivate().getAlgorithm()));
        pkcsCertSig.initSign(requestorKeyPair.getPrivate());
        pkcsCertSig.update(LtansUtils.getDERObjectBytes(cri));
        this.pkcsCertReq = new CertificationRequest(cri, LtansUtils.getSignatureAlgorithmIdentifier(requestorKeyPair.getPrivate().getAlgorithm()), new DERBitString(pkcsCertSig.sign()));
        this.pkcsCertReqSigned = LtansEnvelopeUtils.getSignedEnvelopedData(this, transactionId, transactionId, requestorName, new Date(), requestValidity, "PKCSRequest", requestorKeyPair, cert);
    }

    public PKCSRequest(Object obj) throws Exception {
        if (obj instanceof ASN1Sequence) {
            ASN1Sequence seq = (ASN1Sequence) obj;
            ContentInfo ctntInfo = ContentInfo.getInstance(seq);
            if (CMSObjectIdentifiers.data.toString().equals(ctntInfo.getContentType().toString())) {
                this.pkcsCertReqSigned = SignedData.getInstance(ctntInfo.getContent());
                ASN1Set certSet = pkcsCertReqSigned.getCertificates();
                X509CertificateStructure certStruct = X509CertificateStructure.getInstance(certSet.getObjectAt(0));
                this.transactionId = new DERInteger(new X509CertificateObject(certStruct).getSerialNumber());
            }
        } else throw new IllegalArgumentException();
    }

    public CertificationRequest openSignedEnvelopedRequest(X509Certificate cert, KeyPair kp) throws Exception {
        return (CertificationRequest) LtansEnvelopeUtils.openSignedEnvelopedData(this, cert, kp);
    }

    public DERInteger getTransactionId() {
        return transactionId;
    }

    public CertificationRequest getPkcsCertReq() {
        return this.pkcsCertReq;
    }

    public SignedData getPkcsCertReqSigned() {
        return this.pkcsCertReqSigned;
    }

    public static PKCSRequest getInstance(byte[] objBytes) throws Exception {
        ASN1InputStream inStr = new ASN1InputStream(objBytes);
        return new PKCSRequest(inStr.readObject());
    }

    public static PKCSRequest getInstance(Object obj) throws Exception {
        if ((obj == null) || (obj instanceof PKCSRequest)) {
            return (PKCSRequest) obj;
        } else if (obj instanceof ASN1Sequence) return new PKCSRequest(obj); else throw new IllegalArgumentException("Object obj (class " + obj.getClass().getName() + ") cannot be used as a constructor.");
    }

    public DERObject getDERObject() {
        return new ContentInfo(CMSObjectIdentifiers.signedData, pkcsCertReqSigned).getDERObject();
    }
}
