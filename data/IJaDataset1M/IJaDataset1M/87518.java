package org.hardtokenmgmt.core.util;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.ejbca.core.EjbcaException;
import org.ejbca.core.model.ca.crl.RevokedCertInfo;
import org.ejbca.core.protocol.ws.common.CertificateHelper;
import org.ejbca.util.Base64;
import org.hardtokenmgmt.core.InterfaceFactory;
import org.hardtokenmgmt.core.log.LocalLog;
import org.hardtokenmgmt.core.ui.UIHelper;
import org.hardtokenmgmt.ws.gen.AuthorizationDeniedException_Exception;
import org.hardtokenmgmt.ws.gen.CertificateResponse;
import org.hardtokenmgmt.ws.gen.EjbcaException_Exception;
import org.hardtokenmgmt.ws.gen.NotFoundException_Exception;
import org.hardtokenmgmt.ws.gen.RevokeStatus;

/**
 * Special help class performing non-client SSL required requests
 * towards EJBCA. 
 * 
 * 
 * @author Philip Vendil 11 jul 2009
 *
 * @version $Id$
 */
public class NonClientSSLRequest {

    public static final String BEGIN_CERTIFICATE_REQUEST = "-----BEGIN CERTIFICATE REQUEST-----\n";

    public static final String END_CERTIFICATE_REQUEST = "\n-----END CERTIFICATE REQUEST-----\n";

    public static final int ENCODED_CERTIFICATE = 1;

    public static X509Certificate requestCertificate(String username, String password, PKCS10CertificationRequest pkcs10) throws org.ejbca.core.EjbcaException {
        X509Certificate retval = null;
        try {
            initHttpURLConnection();
            CertificateResponse certenv = InterfaceFactory.getPublicHTMFAdminInterface().pkcs10Request(username, password, new String(Base64.encode(pkcs10.getEncoded())), null, CertificateHelper.RESPONSETYPE_CERTIFICATE);
            retval = (X509Certificate) CertificateHelper.getCertificate(certenv.getData());
        } catch (NoSuchAlgorithmException e) {
            throw new EjbcaException(e);
        } catch (KeyManagementException e) {
            throw new EjbcaException(e);
        } catch (CertificateException e) {
            throw new EjbcaException(e);
        } catch (AuthorizationDeniedException_Exception e) {
            throw new EjbcaException(e);
        } catch (EjbcaException_Exception e) {
            if (e.getMessage().startsWith("AuthStatusException")) {
                throw new EjbcaException(UIHelper.getText("nonsslreq.wrongstatus"));
            } else {
                LocalLog.getLogger().log(Level.SEVERE, "Error requesting certificate througn non-SSL connection, message : " + e.getMessage());
                throw new EjbcaException(UIHelper.getText("nonsslreq.erroroccurred"));
            }
        } catch (NotFoundException_Exception e) {
            throw new EjbcaException(e);
        }
        return retval;
    }

    public static boolean isCertificateValid(X509Certificate cert) throws IOException {
        boolean retval = false;
        try {
            initHttpURLConnection();
            String issuerdn = cert.getIssuerDN().toString();
            String serno = cert.getSerialNumber().toString(16);
            RevokeStatus revokestatus = InterfaceFactory.getPublicHTMFAdminInterface().checkRevokationStatus(issuerdn, serno);
            return revokestatus != null && revokestatus.getReason() == RevokedCertInfo.NOT_REVOKED;
        } catch (KeyManagementException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error: Key management exception when verifying logon certificate : " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error: No such algorithm when verifying logon certificate : " + e.getMessage());
        } catch (AuthorizationDeniedException_Exception e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error: Authorization Denied Exception when verifying logon certificate : " + e.getMessage());
        } catch (EjbcaException_Exception e) {
            LocalLog.getLogger().log(Level.SEVERE, "Error: EJBCA Exception when verifying logon certificate : " + e.getMessage());
        }
        return retval;
    }

    private static void initHttpURLConnection() throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] tolimaTrustManager = new TrustManager[] { new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, tolimaTrustManager, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
    }
}
