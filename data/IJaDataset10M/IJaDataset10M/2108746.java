package edu.upmc.opi.caBIG.caTIES.security;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Properties;
import java.util.Vector;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.globus.gsi.CertUtil;
import org.globus.gsi.GSIConstants;
import org.globus.gsi.GlobusCredential;
import org.globus.gsi.OpenSSLKey;
import org.globus.gsi.bc.BouncyCastleCertProcessingFactory;
import org.globus.gsi.bc.BouncyCastleOpenSSLKey;
import org.globus.gsi.proxy.ext.ProxyCertInfo;
import COM.claymoresystems.cert.CertRequest;
import COM.claymoresystems.cert.X509Name;
import cryptix.util.mime.Base64OutputStream;

/**
 * Generates Globus Credentials based on local configuration files.
 * 
 * @author mitchellkj@upmc.edu
 * @version $Id: CaTIES_CertificateGenerator.java,v 1.2 2006/01/06 20:13:42
 *          chavgx Exp $
 * @since 1.4.2_04
 * @deprecated CaTIES now uses the GUMS server for authentication
 */
public class CaTIES_CertificateGenerator implements Runnable {

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_CertificateGenerator.class);

    static {
        logger.setLevel(Level.DEBUG);
    }

    /**
	 * Field DEBUG.
	 */
    private final boolean DEBUG = false;

    /**
	 * Field m_CommonName. Common Name + Org Unit used to create X509 DNs
	 */
    private String m_CommonName = null;

    /**
	 * Field m_OrgUnit.
	 */
    private String m_OrgUnit = null;

    /**
	 * Field m_PEMUserCert. Certs in PEM format.
	 */
    private String m_PEMUserCert = null;

    /**
	 * Field m_PEMUserKey.
	 */
    private String m_PEMUserKey = null;

    /**
	 * Field isRunning.
	 */
    private boolean isRunning = true;

    /**
	 * Field hasTried.
	 */
    private boolean hasTried = false;

    /**
	 * The is server mode.
	 */
    private boolean isServerMode = true;

    /**
	 * Constructor for CaTIES_CertificateGenerator.
	 * 
	 * @param cn
	 *            X509 Common Name, such as an email address
	 * @param orgUnit
	 *            String
	 * 
	 * @throws Exception
	 *             the exception
	 */
    public CaTIES_CertificateGenerator(String cn, String orgUnit) throws Exception {
        m_CommonName = cn;
        m_OrgUnit = orgUnit;
    }

    /**
	 * Simple Constructor for CaTIES_CertificateGenerator.
	 */
    public CaTIES_CertificateGenerator() {
        m_CommonName = "";
        m_OrgUnit = "";
        BasicConfigurator.configure();
        logger.setLevel(Level.DEBUG);
    }

    /**
	 * Method getHasTried.
	 * 
	 * @return boolean
	 */
    public boolean getHasTried() {
        return this.hasTried;
    }

    /**
	 * Method setHasTried.
	 * 
	 * @param hasTried
	 *            boolean
	 */
    protected void setHasTried(boolean hasTried) {
        this.hasTried = hasTried;
    }

    /**
	 * Sets the server mode.
	 * 
	 * @param isServerMode
	 *            the is server mode
	 */
    public void setServerMode(boolean isServerMode) {
        this.isServerMode = isServerMode;
    }

    /**
	 * Method run.
	 * 
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        Properties cogProperties = new Properties();
        try {
            String cogPropertiesFileName = System.getProperty("user.home") + File.separator + "caTIESPvtKeys" + File.separator + "cog.properties";
            logger.debug("Looking for cog.properties at " + cogPropertiesFileName);
            cogProperties.load(new FileInputStream(cogPropertiesFileName));
        } catch (IOException e) {
            logger.fatal("Security cannot be established. cog.properties not found.");
            isRunning = false;
        }
        String certFileName = cogProperties.getProperty("usercert");
        String keyFileName = cogProperties.getProperty("userkey");
        String proxyFileName = cogProperties.getProperty("proxy");
        while (this.isRunning) {
            try {
                createGlobusCredentialFromFiles(certFileName, keyFileName, proxyFileName);
                setHasTried(true);
                if (!isServerMode) {
                    this.isRunning = false;
                } else {
                    Thread.sleep(10 * 60 * 60 * 1000);
                }
            } catch (InterruptedException ie) {
                isRunning = false;
                setHasTried(true);
            } catch (Exception x) {
                logger.fatal("Failed making credential.");
                isRunning = false;
                setHasTried(true);
            }
        }
    }

    /**
	 * Method createCertificateRequest.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    protected static void createCertificateRequest() throws Exception {
        String rqPEM = CaTIES_CertificateGenerator.createCertRequest("mitchellkj@upmc.edu", "caTIES", "caTIES");
        logger.debug("Cert Request\n" + rqPEM);
        String userHome = (String) System.getProperties().get("user.home");
        String fileName = userHome + File.separator + ".globus" + File.separator + "usercert_request.pem";
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.write(rqPEM);
        out.close();
    }

    /**
	 * Method createUserPrivateKeyAndCertificate.
	 * 
	 * @throws Exception
	 *             the exception
	 */
    protected static void createUserPrivateKeyAndCertificate() throws Exception {
        CaTIES_CertificateGenerator gen = new CaTIES_CertificateGenerator("mitchellkj@upmc.edu", "caTIES");
        gen.createUserCertAndKey("caTIES");
        logger.debug("User Cert\n" + gen.getUserKey());
        String fileName = System.getProperty("user.home") + File.separator + ".globus" + File.separator + "userkey.pem";
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.write(gen.getUserKey());
        out.close();
        logger.debug("User Certificate\n" + gen.getUserCert());
        fileName = System.getProperty("user.home") + File.separator + ".globus" + File.separator + "usercert.pem";
        out = new BufferedWriter(new FileWriter(fileName));
        out.write(gen.getUserKey());
        out.close();
    }

    /**
	 * Method createGlobusCredentialFromFiles.
	 * 
	 * @param proxyFileName
	 *            String
	 * @param inKeyFileName
	 *            String
	 * @param inCertFileName
	 *            String
	 * 
	 * @throws Exception
	 *             the exception
	 */
    protected static void createGlobusCredentialFromFiles(String inCertFileName, String inKeyFileName, String proxyFileName) throws Exception {
        logger.debug("Creating new credential from files \n\t" + inCertFileName + "\n\t" + inKeyFileName + "\n\t" + proxyFileName);
        InputStream inCert = new FileInputStream(inCertFileName);
        InputStream inKey = new FileInputStream(inKeyFileName);
        GlobusCredential cred = createGlobusProxy(inCert, inKey, null, 512, 12);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        cred.save(bos);
        logger.debug("Globus Credential Info\n" + cred.toString());
        logger.debug("Globus Credential PEM\n" + bos.toString());
        BufferedWriter out = new BufferedWriter(new FileWriter(proxyFileName));
        out.write(bos.toString());
        out.close();
    }

    /**
	 * Method createGlobusCredentital.
	 * 
	 * @param gen
	 *            CaTIES_CertificateGenerator
	 * 
	 * @throws Exception
	 *             the exception
	 */
    protected static void createGlobusCredentital(CaTIES_CertificateGenerator gen) throws Exception {
        ByteArrayInputStream inCert = new ByteArrayInputStream(gen.getUserCert().getBytes());
        ByteArrayInputStream inKey = new ByteArrayInputStream(gen.getUserKey().getBytes());
        GlobusCredential cred = createGlobusProxy(inCert, inKey, "caTIES", 512, 12);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        cred.save(bos);
        logger.debug("Globus Credential Info\n" + cred.toString());
        logger.debug("Globus Credential PEM\n" + bos.toString());
        String temporaryDirectory = (String) System.getProperties().get("java.io.tmpdir");
        String fileName = temporaryDirectory + File.separator + "x509up_u_mitchellkj";
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        out.write(bos.toString());
        out.close();
    }

    /**
	 * Method loadGlobusCredential.
	 * 
	 * @param bos
	 *            ByteArrayOutputStream
	 * 
	 * @throws Exception
	 *             the exception
	 */
    protected static void loadGlobusCredential(ByteArrayOutputStream bos) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toString().getBytes());
        GlobusCredential cred1 = new GlobusCredential(bis);
        logger.debug("Globus Credential Verification\n" + cred1.toString());
    }

    /**
	 * CertKeyGenerator: Creates a Signed User certificate and a private key by
	 * generating a self signed user certificate. Private key is encrypted w/
	 * Pwd
	 * 
	 * @param Pwd =
	 *            Challenge pwd (used to encrypt pirv key)
	 * 
	 * @throws Exception
	 *             the exception
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
    public void createUserCertAndKey(String Pwd) throws NoSuchAlgorithmException, Exception {
        debug("CertKeyGenerator OrgUnit=" + m_OrgUnit + " CommonName=" + m_CommonName + " Pwd=" + Pwd);
        StringWriter sw = new StringWriter();
        BufferedWriter bw = new BufferedWriter(sw);
        KeyPair kp = CertRequest.generateKey("RSA", 512, Pwd, bw, true);
        byte[] reqBytes = CertRequest.makeSelfSignedCert(kp, makeGridCertDN(m_OrgUnit, m_CommonName), 315360000);
        m_PEMUserKey = sw.toString();
        m_PEMUserCert = writePEM(reqBytes, "-----BEGIN CERTIFICATE-----\n", "-----END CERTIFICATE-----\n");
    }

    /**
	 * Create a Globus proxy required to run jobs on the Grid. This method is
	 * used to create a proxy if you have a cert file and a key file encoded in
	 * PEM format. Certs come from input streams.
	 * 
	 * @param inUserKey
	 *            private key
	 * @param inUserCert
	 *            User Cert input stream
	 * @param bits
	 *            Strength of the proxy
	 * @param hours
	 *            Proxy lifetime
	 * @param pwd
	 *            Password used to decrypt the private key
	 * 
	 * @return GlobusCredential
	 * 
	 * @throws Exception
	 *             the exception
	 */
    public static GlobusCredential createGlobusProxy(InputStream inUserCert, InputStream inUserKey, String pwd, int bits, int hours) throws Exception {
        X509Certificate userCert = CertUtil.loadCertificate(inUserCert);
        OpenSSLKey key = new BouncyCastleOpenSSLKey(inUserKey);
        if (key.isEncrypted()) {
            try {
                key.decrypt(pwd);
            } catch (GeneralSecurityException e) {
                throw new Exception("Wrong password or other security error");
            }
        }
        PrivateKey userKey = key.getPrivateKey();
        BouncyCastleCertProcessingFactory factory = BouncyCastleCertProcessingFactory.getDefault();
        return factory.createCredential(new X509Certificate[] { userCert }, userKey, bits, hours * 3600, GSIConstants.GSI_2_PROXY, (ProxyCertInfo) null);
    }

    /**
	 * Cert request Generator.
	 * 
	 * @param OrgUnit
	 *            Organization Unit
	 * @param Pwd
	 *            Password
	 * @param CommonName
	 *            Common name
	 * 
	 * @return a PEM encoded string (Certificate request)
	 * 
	 * @throws NoSuchProviderException
	 *             the no such provider exception
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 * @throws IOException
	 *             the IO exception
	 */
    public static String createCertRequest(String CommonName, String OrgUnit, String Pwd) throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
        StringWriter sw = new StringWriter();
        BufferedWriter bw = new BufferedWriter(sw);
        KeyPair kp = CertRequest.generateKey("RSA", 512, Pwd, bw, true);
        byte[] req = CertRequest.makePKCS10Request(kp, makeGridCertDN(OrgUnit, CommonName));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        org.globus.util.PEMUtils.writeBase64(bos, "-----BEGIN CERTIFICATE REQUEST-----\n", req, "-----END CERTIFICATE REQUEST-----\n");
        return writePEM(req, "-----BEGIN CERTIFICATE REQUEST-----\n", "-----END CERTIFICATE REQUEST-----\n");
    }

    /**
	 * Write a PEM encoded string.
	 * 
	 * @param ftr
	 *            String
	 * @param bytes
	 *            byte[]
	 * @param hdr
	 *            String
	 * 
	 * @return String
	 * 
	 * @throws IOException
	 *             the IO exception
	 */
    public static String writePEM(byte[] bytes, String hdr, String ftr) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Base64OutputStream b64os = new Base64OutputStream(bos);
        b64os.write(bytes);
        b64os.flush();
        b64os.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        InputStreamReader irr = new InputStreamReader(bis);
        BufferedReader r = new BufferedReader(irr);
        StringBuffer buff = new StringBuffer();
        String line;
        buff.append(hdr);
        while ((line = r.readLine()) != null) {
            buff.append(line + "\n");
        }
        buff.append(ftr);
        return buff.toString();
    }

    /**
	 * Write a PEM encoded string.
	 * 
	 * @param fileName
	 *            String
	 * @param ftr
	 *            String
	 * @param bytes
	 *            byte[]
	 * @param hdr
	 *            String
	 * 
	 * @throws IOException
	 *             the IO exception
	 */
    public static void writePEMToFile(byte[] bytes, String hdr, String ftr, String fileName) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Base64OutputStream b64os = new Base64OutputStream(bos);
        b64os.write(bytes);
        b64os.flush();
        b64os.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        InputStreamReader irr = new InputStreamReader(bis);
        BufferedReader r = new BufferedReader(irr);
        PrintWriter out = new PrintWriter(new FileOutputStream(fileName));
        out.print(hdr);
        String line;
        while ((line = r.readLine()) != null) {
            out.write(line + "\n");
        }
        out.write(ftr);
        out.close();
    }

    /**
	 * Create an X509 Certificate DN.
	 * 
	 * @param OrgUnit
	 *            String
	 * @param CommonName
	 *            String
	 * 
	 * @return X509Name
	 */
    private static X509Name makeGridCertDN(String OrgUnit, String CommonName) {
        Vector tdn = new Vector();
        String[] o1 = new String[2];
        String[] o2 = new String[2];
        String[] ou = new String[2];
        String[] cn = new String[2];
        String[] cn1 = new String[2];
        o1[0] = "O";
        o1[1] = "UPMC";
        o2[0] = "O";
        o2[1] = "caBIG";
        ou[0] = "OU";
        ou[1] = "caTIES";
        cn[0] = "CN";
        cn[1] = CommonName;
        tdn.addElement(o1);
        tdn.addElement(o2);
        tdn.addElement(ou);
        tdn.addElement(cn);
        return CertRequest.makeSimpleDN(tdn);
    }

    /**
	 * Method debug.
	 * 
	 * @param text
	 *            String
	 */
    private void debug(String text) {
        if (DEBUG) logger.debug(this.getClass() + ": " + text);
    }

    /**
	 * Method getUserCert.
	 * 
	 * @return String
	 */
    public String getUserCert() {
        return m_PEMUserCert;
    }

    /**
	 * Method getUserKey.
	 * 
	 * @return String
	 */
    public String getUserKey() {
        return m_PEMUserKey;
    }

    /**
	 * Main.
	 * 
	 * @param args
	 *            String[]
	 */
    public static void main(String[] args) {
        CaTIES_CertificateGenerator certificateGenerator = new CaTIES_CertificateGenerator();
        (new Thread(certificateGenerator)).start();
    }
}
