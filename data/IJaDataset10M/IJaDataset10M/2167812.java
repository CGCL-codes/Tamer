package org.tolven.sso;

import java.io.ByteArrayInputStream;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.NewCookie;
import org.apache.commons.codec.binary.Base64;
import org.tolven.security.hash.SSHA;
import com.iplanet.sso.SSOException;
import com.iplanet.sso.SSOToken;
import com.iplanet.sso.SSOTokenManager;

/**
 * A class which supplies SSO functionality
 * 
 * @author Joseph Isaac
 *
 */
public class TolvenSSO {

    private static TolvenSSO tolvenSSO;

    private SSOTokenManager ssoTokenManager;

    private CertificateFactory certificateFactory;

    private KeyFactory keyFactory;

    private TolvenSSO() {
    }

    public static TolvenSSO getInstance() {
        if (tolvenSSO == null) {
            tolvenSSO = new TolvenSSO();
        }
        return tolvenSSO;
    }

    public SSOTokenManager getTokenManager() {
        if (ssoTokenManager == null) {
            try {
                ssoTokenManager = SSOTokenManager.getInstance();
            } catch (SSOException ex) {
                throw new RuntimeException("Could not get an instance of SSOTokenManager", ex);
            }
        }
        return ssoTokenManager;
    }

    public CertificateFactory getCertificateFactory() {
        if (certificateFactory == null) {
            try {
                certificateFactory = CertificateFactory.getInstance("X509");
            } catch (CertificateException ex) {
                throw new RuntimeException("Could not get instance of CertificateFactory", ex);
            }
        }
        return certificateFactory;
    }

    public KeyFactory getKeyFactory(String keyAlgorithm) {
        if (keyFactory == null) {
            try {
                keyFactory = KeyFactory.getInstance(keyAlgorithm);
            } catch (Exception ex) {
                throw new RuntimeException("Could not get instance of KeyFactory", ex);
            }
        }
        return keyFactory;
    }

    public SSOToken getSSOToken(HttpServletRequest request) {
        try {
            return getTokenManager().createSSOToken(request);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create SSO token from request", ex);
        }
    }

    public String getSessionProperty(String name, SSOToken ssoToken) {
        try {
            return ssoToken.getProperty(name);
        } catch (SSOException ex) {
            throw new RuntimeException("Could not get SSO property: " + name, ex);
        }
    }

    public String getSessionProperty(String name, HttpServletRequest request) {
        return getSessionProperty(name, getSSOToken(request));
    }

    public void setSessionProperty(String name, String value, SSOToken ssoToken) {
        try {
            if (value == null) {
                ssoToken.setProperty(name, "");
            } else {
                ssoToken.setProperty(name, value);
            }
        } catch (SSOException ex) {
            throw new RuntimeException("Could not set SSO property: " + name, ex);
        }
    }

    public void setSessionProperty(String name, String value, HttpServletRequest request) {
        setSessionProperty(name, value, getSSOToken(request));
    }

    public void removeSessionProperty(String name, SSOToken ssoToken) {
        setSessionProperty(name, null, ssoToken);
    }

    public void removeSessionProperty(String name, HttpServletRequest request) {
        removeSessionProperty(name, getSSOToken(request));
    }

    public NewCookie getSSOCookie(SSOToken ssoToken) {
        try {
            return new NewCookie("iPlanetDirectoryPro", URLEncoder.encode(ssoToken.getTokenID().toString(), "UTF-8"));
        } catch (Exception ex) {
            throw new RuntimeException("Could not create NewCookie for RESTful call", ex);
        }
    }

    public X509Certificate getUserX509Certificate(HttpServletRequest request) {
        return getUserX509Certificate(getSSOToken(request));
    }

    public X509Certificate getUserX509Certificate(SSOToken ssoToken) {
        String userX509Certificate = null;
        try {
            userX509Certificate = ssoToken.getProperty("userX509Certificate");
        } catch (SSOException ex) {
            throw new RuntimeException("Could not get userX509Certificate from SSOToken", ex);
        }
        if (userX509Certificate == null) {
            return null;
        } else {
            try {
                byte[] userX509CertificateBytes = userX509Certificate.getBytes("UTF-8");
                return (X509Certificate) getCertificateFactory().generateCertificate(new ByteArrayInputStream(Base64.decodeBase64(userX509CertificateBytes)));
            } catch (Exception ex) {
                throw new RuntimeException("Could not get X509Certificate from SSO userCertificate", ex);
            }
        }
    }

    public PublicKey getUserPublicKey(HttpServletRequest request) {
        return getUserPublicKey(getSSOToken(request));
    }

    public PublicKey getUserPublicKey(SSOToken ssoToken) {
        X509Certificate x509Certificate = getUserX509Certificate(ssoToken);
        if (x509Certificate == null) {
            return null;
        } else {
            return x509Certificate.getPublicKey();
        }
    }

    public PrivateKey getUserPrivateKey(HttpServletRequest request, String keyAlgorithm) {
        return getUserPrivateKey(getSSOToken(request), keyAlgorithm);
    }

    public PrivateKey getUserPrivateKey(SSOToken ssoToken, String keyAlgorithm) {
        String userPKCS8EncodedKey = null;
        try {
            userPKCS8EncodedKey = ssoToken.getProperty("userPKCS8EncodedKey");
        } catch (SSOException ex) {
            throw new RuntimeException("Could not get userPKCS8EncodedKey from SSOToken", ex);
        }
        if (userPKCS8EncodedKey == null) {
            return null;
        } else {
            try {
                byte[] userPKCS8EncodedKeyBytes = userPKCS8EncodedKey.getBytes("UTF-8");
                PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(userPKCS8EncodedKeyBytes));
                return getKeyFactory(keyAlgorithm).generatePrivate(pkcs8EncodedKeySpec);
            } catch (Exception ex) {
                throw new RuntimeException("Could not get PrivateKey from pkcs8EncodedKey", ex);
            }
        }
    }

    public boolean verifyUserPassword(char[] password, HttpServletRequest request) {
        return verifyUserPassword(password, getSSOToken(request));
    }

    public boolean verifyUserPassword(char[] password, SSOToken ssoToken) {
        String sshaUserPassword = null;
        try {
            sshaUserPassword = ssoToken.getProperty("userPassword");
        } catch (SSOException ex) {
            throw new RuntimeException("Could not get userPassword from SSOToken", ex);
        }
        return SSHA.checkPassword(password, sshaUserPassword);
    }

    public boolean isValid(HttpServletRequest request) {
        return isValid(getSSOToken(request));
    }

    public boolean isValid(SSOToken ssoToken) {
        return getTokenManager().isValidToken(ssoToken);
    }

    public void logout(HttpServletRequest request) {
        logout(getSSOToken(request));
    }

    public void logout(SSOToken ssoToken) {
        String principal = "unknown principal";
        if (getTokenManager().isValidToken(ssoToken)) {
            try {
                principal = ssoToken.getPrincipal().getName();
            } catch (Exception e) {
            }
        }
        try {
            getTokenManager().destroyToken(ssoToken);
        } catch (Exception ex) {
            throw new RuntimeException("Fail to logout: " + principal, ex);
        }
    }
}
