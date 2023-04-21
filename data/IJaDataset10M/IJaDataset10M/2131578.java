package org.ejbca.cvc;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import org.ejbca.cvc.exception.ConstructionException;
import org.ejbca.cvc.util.BCECUtil;

/**
 * Represents a CVC-request having an outer signature
 * 
 * @author Keijo Kurkinen, Swedish National Police Board
 *
 */
public class CVCAuthenticatedRequest extends AbstractSequence implements Signable {

    private static final long serialVersionUID = -5015578320373149733L;

    private static CVCTagEnum[] allowedFields = new CVCTagEnum[] { CVCTagEnum.CV_CERTIFICATE, CVCTagEnum.CA_REFERENCE, CVCTagEnum.SIGNATURE };

    @Override
    CVCTagEnum[] getAllowedFields() {
        return allowedFields;
    }

    /**
    * Default constructor
    */
    CVCAuthenticatedRequest() {
        super(CVCTagEnum.REQ_AUTHENTICATION);
    }

    /**
    * Creates an instance 
    * @param cvcert
    * @param caReference
    */
    public CVCAuthenticatedRequest(CVCertificate cvcert, CAReferenceField caReference) throws ConstructionException {
        this();
        addSubfield(cvcert);
        addSubfield(caReference);
    }

    /**
    * Adds signature
    * @param signatureData
    * @throws ConstructionException
    */
    public void setSignature(byte[] signatureData) throws ConstructionException {
        addSubfield(new ByteField(CVCTagEnum.SIGNATURE, signatureData));
    }

    /**
    * Returns the data To Be Signed
    */
    public byte[] getTBS() throws ConstructionException {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            DataOutputStream dout = new DataOutputStream(bout);
            CVCertificate cert = getRequest();
            cert.encode(dout);
            CAReferenceField caref = getAuthorityReference();
            caref.encode(dout);
            dout.close();
            byte[] res = bout.toByteArray();
            return res;
        } catch (NoSuchFieldException e) {
            throw new ConstructionException(e);
        } catch (IOException e) {
            throw new ConstructionException(e);
        }
    }

    /**
    * Returns the embedded request (as an instance of CVCertificate)
    * @return
    */
    public CVCertificate getRequest() throws NoSuchFieldException {
        return (CVCertificate) getSubfield(CVCTagEnum.CV_CERTIFICATE);
    }

    /**
    * Returns CA_REFERENCE
    * @return
    */
    public CAReferenceField getAuthorityReference() throws NoSuchFieldException {
        return (CAReferenceField) getSubfield(CVCTagEnum.CA_REFERENCE);
    }

    /**
    * Returns signature
    * @return
    */
    public byte[] getSignature() throws NoSuchFieldException {
        return ((ByteField) getSubfield(CVCTagEnum.SIGNATURE)).getData();
    }

    /**
    * Verifies the signature
    * @param pubKey
    * @throws CertificateException
    * @throws NoSuchAlgorithmException
    * @throws InvalidKeyException
    * @throws NoSuchProviderException
    * @throws SignatureException
    */
    public void verify(PublicKey pubKey) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        try {
            String algorithm = "";
            if (pubKey instanceof CVCPublicKey) {
                CVCPublicKey cvcKey = (CVCPublicKey) pubKey;
                algorithm = AlgorithmUtil.getAlgorithmName(cvcKey.getObjectIdentifier());
            } else {
                CVCPublicKey cvcKey = getRequest().getCertificateBody().getPublicKey();
                algorithm = AlgorithmUtil.getAlgorithmName(cvcKey.getObjectIdentifier());
            }
            Signature sign = Signature.getInstance(algorithm);
            sign.initVerify(pubKey);
            sign.update(getTBS());
            byte[] sig = BCECUtil.convertCVCSigToX962(algorithm, getSignature());
            if (!sign.verify(sig)) {
                throw new SignatureException("Signature verification failed!");
            }
        } catch (NoSuchFieldException e) {
            throw new CertificateException("CV-Certificate is corrupt", e);
        } catch (ConstructionException e) {
            throw new CertificateException("CV-Certificate is corrupt", e);
        }
    }

    /**
    * Helper method, returns this request as text
    */
    public String toString() {
        return getAsText("", true);
    }
}
