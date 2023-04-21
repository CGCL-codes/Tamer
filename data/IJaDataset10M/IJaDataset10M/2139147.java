package org.signserver.validationservice.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.jce.provider.X509CertificateObject;
import org.ejbca.util.CertTools;

/**
 * ValidationService implementation of a X509Certificate.
 * The only difference is the implementation of the
 *
 * @author Philip Vendil 29 nov 2007
 * @version $Id: X509Certificate.java 1829 2011-08-10 11:50:45Z netmackan $
 */
@SuppressWarnings("unchecked")
public class X509Certificate extends X509CertificateObject implements ICertificate {

    private static final long serialVersionUID = 1L;

    public X509Certificate(X509CertificateStructure certificateStructure) throws CertificateParsingException {
        super(certificateStructure);
    }

    public String getIssuer() {
        return CertTools.getIssuerDN(this);
    }

    public String getSubject() {
        return CertTools.getSubjectDN(this);
    }

    public static X509Certificate getInstance(java.security.cert.X509Certificate x509Cert) throws CertificateEncodingException, CertificateParsingException, IOException {
        ByteArrayInputStream bIn = new ByteArrayInputStream(x509Cert.getEncoded());
        ASN1InputStream aIn = new ASN1InputStream(bIn);
        return new X509Certificate(new X509CertificateStructure((ASN1Sequence) aIn.readObject()));
    }
}
