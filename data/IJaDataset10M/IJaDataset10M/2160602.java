package gov.cdc.ncphi.phgrid.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import org.apache.log4j.Logger;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.GlobusCredentialException;
import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleCertProcessingFactory;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;
import com.sun.security.auth.module.UnixSystem;

/***
 * This utility library interacts with the Globus Security Infrastructure (GSI)
 * to create and manage proxy certificates.
 * 
 * @author Felicia Rosemond (frosemond@gmail.com)
 *
 */
public class Proxy {

    private static final Logger logger = Logger.getLogger(Proxy.class);

    private final String USERCERT_NAME = "usercert.pem";

    private final String USERKEY_NAME = "userkey.pem";

    private final String GLOBUS_LOCATION = ".globus";

    private final String UNIX_PATH = "/home/";

    private final String UNIX_SEPERATOR = "/";

    private final String UNIX_PROXY_PATH = "/tmp/x509up_u";

    private final String WINDOWS_PATH = "C:\\Documents and Settings\\";

    private final String WINDOWS_SEPERATOR = "\\";

    private final String WINDOWS_PROXY_PATH = "C:\\Documents and Settings\\Local Settings\\Temp";

    private static String OS = null;

    private static String USERNAME = null;

    private static String UID = null;

    private String userCert;

    private String userKey;

    private String userProxy;

    private GlobusCredential proxy;

    /**
	 *  Initialize the class by setting paths for the userCert, userKey and the userProxy based 
	 *  on the operating system being used. 
	 */
    public Proxy() {
        if (isWindows()) {
            logger.debug("creating Windows proxy");
            setUserCert(WINDOWS_PATH + getUSERNAME() + WINDOWS_SEPERATOR + GLOBUS_LOCATION + WINDOWS_SEPERATOR + USERCERT_NAME);
            setUserKey(WINDOWS_PATH + getUSERNAME() + WINDOWS_SEPERATOR + GLOBUS_LOCATION + WINDOWS_SEPERATOR + USERKEY_NAME);
            setUserProxy(WINDOWS_PROXY_PATH + getUID());
        } else {
            logger.debug("creating Linux proxy");
            setUserCert(UNIX_PATH + getUSERNAME() + UNIX_SEPERATOR + GLOBUS_LOCATION + UNIX_SEPERATOR + USERCERT_NAME);
            setUserKey(UNIX_PATH + getUSERNAME() + UNIX_SEPERATOR + GLOBUS_LOCATION + UNIX_SEPERATOR + USERKEY_NAME);
            setUserProxy(UNIX_PROXY_PATH + getUID());
        }
    }

    /***
	 * Determine the operating system that is being used.
	 * 
	 * @return
	 */
    private String getOS() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    /***
	 * Determine the username of the current user.
	 * 
	 * @return
	 */
    private String getUSERNAME() {
        if (USERNAME == null) {
            USERNAME = System.getProperty("user.name");
        }
        logger.debug("username = " + USERNAME);
        return USERNAME;
    }

    /***
     * Determine the uid of the current user
     * 
     * @return
     */
    private String getUID() {
        if (UID == null) {
            if (isWindows()) {
                ;
            } else {
                UnixSystem sys = new UnixSystem();
                UID = Long.toString(sys.getUid());
            }
        }
        logger.debug("uid =" + UID);
        return UID;
    }

    /***
	 * Check if the current operating system is Windows.
	 * 
	 * @return
	 */
    private boolean isWindows() {
        return getOS().startsWith("Windows");
    }

    /***
	 * Retrieve the path for the usercert.
	 * 
	 * @return
	 */
    private String getUserCert() {
        return userCert;
    }

    /***
	 * Set the path for the usercert.
	 * 
	 * @param userCert
	 */
    private void setUserCert(String userCert) {
        this.userCert = userCert;
    }

    /***
	 * Retrieve the path for the userkey
	 * 
	 * @return
	 */
    private String getUserKey() {
        return userKey;
    }

    /***
	 * Set the path for the userkey.
	 * 
	 * * @param userKey
	 */
    private void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    /***
	 * Retrieve the path for the userproxy.
	 * 
	 * @return
	 */
    private String getUserProxy() {
        return userProxy;
    }

    /***
	 * Set the path for the userproxy.
	 * 
	 * @param userProxy
	 */
    private void setUserProxy(String userProxy) {
        this.userProxy = userProxy;
    }

    /***
	 * Convert the time left on the proxy certificate to hours/minutes/seconds
	 * 
	 * @param timeLeft - time left on proxy in seconds
	 * 
	 * @return
	 */
    private String convertTimeLeft(long timeLeft) {
        StringBuffer sb = new StringBuffer();
        int hours, minutes, seconds;
        hours = (int) timeLeft / 3600;
        minutes = (int) ((timeLeft - (hours * 3600)) / 60);
        seconds = (int) (timeLeft - (hours * 3600) - (minutes * 60));
        sb.append(hours);
        sb.append(":");
        sb.append(minutes);
        sb.append(":");
        sb.append(seconds);
        return sb.toString();
    }

    /***
	 * Determine if the proxy file exists.
	 * 	
	 * @return
	 */
    private boolean isProxyCreated() {
        String filename = getUserProxy();
        File f = new File(filename);
        if (!f.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /***
	 * Determine if the proxy file is writable.
	 * 
	 * @return
	 */
    private boolean isProxyWritable() {
        String filename = getUserProxy();
        File f = new File(filename);
        if (!f.canWrite()) {
            return false;
        } else {
            return true;
        }
    }

    /***
	 * Delete the proxy file.
	 * 
	 * @return
	 */
    private boolean isProxyDeleted() {
        String filename = getUserProxy();
        File f = new File(filename);
        return f.delete();
    }

    /**
	 *  Programmatically create a user proxy just as the command line tool
	 *  grid-proxy-init.
	 *  
	 * @param passwd
	 * @return
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
    public GlobusCredential createProxy(String passwd, int timeLength) throws IOException, GeneralSecurityException {
        try {
            X509Certificate userCertificate = CertUtil.loadCertificate(getUserCert());
            OpenSSLKey key = new BouncyCastleOpenSSLKey(getUserKey());
            if (key.isEncrypted()) {
                logger.debug("key for the certificate is encrypted");
                key.decrypt(passwd);
            }
            PrivateKey userKey = key.getPrivateKey();
            BouncyCastleCertProcessingFactory factory = BouncyCastleCertProcessingFactory.getDefault();
            int strength = 512;
            int lifeTime = timeLength * 3600;
            proxy = factory.createCredential(new X509Certificate[] { userCertificate }, userKey, strength, lifeTime, GSIConstants.GSI_3_IMPERSONATION_PROXY);
            FileOutputStream fos = new FileOutputStream(getUserProxy());
            proxy.save(fos);
            fos.close();
            Runtime.getRuntime().exec("chmod 600 " + getUserProxy());
        } catch (IOException ex) {
            throw ex;
        } catch (GeneralSecurityException ex) {
            throw ex;
        }
        return proxy;
    }

    /**
	 * Delete generated proxy from the system
	 * 
	 * @return
	 * @throws IllegalArgumentException
	 */
    public boolean deleteProxy() {
        boolean success = false;
        if (isProxyCreated() && this.isProxyWritable()) {
            success = isProxyDeleted();
        }
        return success;
    }

    /**
	 * Build proxy information string 
	 * 
	 * @return String
	 */
    public String getProxyInfo() {
        if (this.isProxyCreated()) {
            StringBuffer sb = new StringBuffer();
            sb.append("subject : " + proxy.getSubject() + "\n");
            sb.append("issuer : " + proxy.getIssuer() + "\n");
            sb.append("identity : " + proxy.getIdentity() + "\n");
            sb.append("type : " + proxy.getProxyType() + "\n");
            sb.append("strength : " + proxy.getStrength() + "\n");
            sb.append("path : " + getUserProxy() + "\n");
            sb.append("timeleft : " + convertTimeLeft(proxy.getTimeLeft()) + "\n");
            return sb.toString();
        } else {
            return null;
        }
    }

    /***
	 * Verify that the proxy is valid any errors returned are interpreted
	 * as the proxy being invalid.
	 * 
	 * @return
	 */
    public boolean isProxyValid() {
        if (!isProxyCreated()) return false;
        logger.debug("proxy file exists");
        if (proxy == null) {
            try {
                if (GlobusCredential.getDefaultCredential().getTimeLeft() == 0) {
                    return false;
                } else {
                    GlobusCredential.getDefaultCredential().verify();
                    proxy = GlobusCredential.getDefaultCredential();
                    return true;
                }
            } catch (GlobusCredentialException e) {
                return false;
            }
        } else if (proxy.getTimeLeft() == 0) {
            return false;
        }
        return true;
    }

    /***
	 * 
	 * @return
	 */
    public String timeLeft() {
        return convertTimeLeft(proxy.getTimeLeft());
    }
}
