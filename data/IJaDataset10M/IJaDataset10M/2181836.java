package org.signserver.common;

/**
 * General implementation of a ISignerCertReqData that
 * contains a Base64 encoded byte array containing the certificate
 * request data
 * 
 * @author Philip Vendil 2007 feb 19
 * @version $Id: Base64SignerCertReqData.java 1829 2011-08-10 11:50:45Z netmackan $
 */
public class Base64SignerCertReqData implements ICertReqData {

    private static final long serialVersionUID = 1L;

    byte[] base64CertReq = null;

    /**
     * No-arg constructor used by JAXB.
     */
    public Base64SignerCertReqData() {
    }

    public Base64SignerCertReqData(byte[] base64CertReq) {
        super();
        this.base64CertReq = base64CertReq;
    }

    public byte[] getBase64CertReq() {
        return base64CertReq;
    }

    public void setBase64CertReq(byte[] base64CertReq) {
        this.base64CertReq = base64CertReq;
    }
}
