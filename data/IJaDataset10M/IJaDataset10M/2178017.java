package org.hardtokenmgmt.core.logon;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;
import org.ejbca.util.Base64;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.settings.AdministratorSettings;
import org.hardtokenmgmt.core.util.CertUtils;

/**
 * ToLiMa Trust Manager is used during login in using a stand-alone application.
 * 
 * It works in the following way, It checks the trusted issuer against the issuer
 * of the server certificate used the first time. It it is the first time
 * the Root certificate of the server certificate is stored in administrator setting
 * using the property : TRUSTEDCACERT
 * 
 * If the issuer is changed a dialog will be displayed using the ILogonGUICallback interface
 * asking the user if the new issuer should be trusted.
 * 
 * 
 * @author Philip Vendil 2 feb 2008
 *
 * @version $Id$
 */
public class TolimaTrustManager implements X509TrustManager {

    private ILogonGUICallback gUICallBack;

    public TolimaTrustManager(ILogonGUICallback gUICallBack) {
        this.gUICallBack = gUICallBack;
    }

    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
    }

    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) throws CertificateException {
        AdministratorSettings as = InterfaceFactory.getAdministratorSettings();
        X509Certificate serverRootCert = findRootCert(certs);
        if (as.getProperty(AdministratorSettings.LOGON_TRUSTEDCACERT) == null) {
            LocalLog.debug("Server Root CA Certificate not configured, adding it as trusted");
            as.setProperty(AdministratorSettings.LOGON_TRUSTEDCACERT, new String(Base64.encode(serverRootCert.getEncoded())));
        } else {
            X509Certificate rootCert = CertUtils.getCertfromByteArray(Base64.decode(as.getProperty(AdministratorSettings.LOGON_TRUSTEDCACERT).getBytes()));
            if (!CertUtils.getFingerprintAsString(rootCert).equals(CertUtils.getFingerprintAsString(serverRootCert))) {
                gUICallBack.clearMessages();
                if (gUICallBack.confirmTrustChanged()) {
                    LocalLog.debug("Server Root CA Certificate changed, setting it as trusted");
                    as.setProperty(AdministratorSettings.LOGON_TRUSTEDCACERT, new String(Base64.encode(serverRootCert.getEncoded())));
                } else {
                    throw new CertificateException("Error issuer : " + CertUtils.getIssuerDN(serverRootCert) + " isn't trusted.");
                }
            }
        }
    }

    private X509Certificate findRootCert(java.security.cert.X509Certificate[] certs) throws CertificateException {
        X509Certificate retval = null;
        for (X509Certificate cert : certs) {
            if (CertUtils.getSubjectDN(cert).equals(CertUtils.getIssuerDN(cert))) {
                retval = cert;
                break;
            }
        }
        if (retval == null) {
            throw new CertificateException("Error no Root certificate found among server certificates");
        }
        return retval;
    }
}
