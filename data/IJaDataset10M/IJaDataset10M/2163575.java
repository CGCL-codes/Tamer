package org.crypthing.things.cert;

import java.io.Serializable;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.util.encoders.Hex;

/**
 * A facility to list X509 certificate fields. All fields refers to the first certificate within chain.
 * @author yorickflannagan
 * @version 1.0
 *
 */
public abstract class CertificateViewer implements Serializable {

    private static final long serialVersionUID = -5315976635322024801L;

    private static ConcurrentHashMap<String, CertificateViewer> registry = new ConcurrentHashMap<String, CertificateViewer>();

    /**
   * Gets an instance of CertificateViewer implementation.
   * @param implementation - the registration name of the implementation.
   * @return - a new instance of CertificateViewer implementation, if it was registered; otherwise, null.
   */
    public static CertificateViewer getInstance(String implementation) {
        return registry.get(implementation);
    }

    /**
   * Register a new CertificateViewer implementation.
   * @param implementation - the implementation name.
   * @param instance - an instance of the CertificateViewer implementation.
   */
    public static void register(String implementation, Object instance) {
        registry.put(implementation, (CertificateViewer) instance);
    }

    private X509Certificate[] chain = null;

    /**
   * Creates a new CertificateViewer instance.
   */
    protected CertificateViewer() {
    }

    /**
   * Gets the underlying certificate chain.
   * @return - the underlying X509 certificate chain.
   */
    public X509Certificate[] getCertificateChain() {
        return chain;
    }

    /**
   * Sets the underlying certificate chain.
   * @param certificate - the underlying X509 certificate chain.
   */
    public void setCertificateChain(X509Certificate[] chain) {
        this.chain = chain;
    }

    /**
   * Gets the subject X500 name.
   * @return - the thing itself.
   */
    public String getSubjectName() {
        return chain[0].getSubjectX500Principal().getName();
    }

    /**
   * Gets the issuer X500 name.
   * @return - the thing itself.
   */
    public String getIssuerName() {
        return chain[0].getIssuerX500Principal().getName();
    }

    /**
   * Gets the certificate security level, if any.
   * @return - the thing itself.
   */
    public abstract String getSecurityLevel();

    /**
   * Gets the certificate purpose.
   * @return - the thing itself.
   */
    public KeyUsage getKeyUsage() {
        return new KeyUsage(chain[0]);
    }

    /**
   * Gets the serial number as an hexadecimal string.
   * @return - the thing itself.
   */
    public String getSerialNumber() {
        return new String(Hex.encode(chain[0].getSerialNumber().toByteArray()));
    }

    /**
   * Gets the localized certificate validity.
   * @return - the NotBefore certificate field.
   */
    public String getNotBefore() {
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        return format.format(chain[0].getNotBefore());
    }

    /**
   * Gets the localized certificate validity.
   * @return - the NotAfter certificate field.
   */
    public String getNotAfter() {
        DateFormat format = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());
        return format.format(chain[0].getNotAfter());
    }

    /**
   * Gets the certificate policies.
   * @return - the thing itself.
   */
    public CertificatePolicies getPolicies() {
        return new CertificatePolicies(chain[0].getExtensionValue(X509Extensions.CertificatePolicies.getId()));
    }

    /**
   * Gets the certification path.
   * @return - the thing itself.
   */
    public String[] getCertificationPath() {
        String[] path = new String[chain.length - 1];
        for (int i = 1; i < chain.length; i++) {
            DistinguishedName name = new DistinguishedName(chain[i].getSubjectX500Principal().getName());
            path[i - 1] = name.getCN();
        }
        return path;
    }

    /**
   * Gets the PKI dependent certificate attributes.
   * @return - an iterator to PKI attributes.
   */
    public abstract Iterator<ExtensionAttribute> getSubjectAlternativeNames();
}
