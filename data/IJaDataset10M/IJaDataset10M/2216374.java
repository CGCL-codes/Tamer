package org.xito.boot;

import java.util.*;
import java.util.logging.*;
import java.net.*;
import java.security.*;
import java.security.cert.*;
import java.io.*;
import java.beans.*;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Encoder;
import sun.misc.BASE64Decoder;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import org.xito.reflect.*;

/**
 *
 * @author DRICHAN
 */
public class XMLPolicyStore implements PolicyStore {

    private static final Logger logger = Logger.getLogger(XMLPolicyStore.class.getName());

    private final String ALL_PERMISSION_KEY = "allPermissionkey";

    private final String DEFAULT_KEYSTORE_PASS = "xitokeypass";

    private File storeDir;

    private HashSet policyCache = new HashSet();

    private SecretKey secretKey;

    private DocumentBuilderFactory docBuilderFactory;

    private DocumentBuilder docBuilder;

    private TransformerFactory transformerFactory;

    private Transformer transformer;

    /** Creates a new instance of XMLPolicyStore */
    protected XMLPolicyStore() {
        storeDir = new File(System.getProperty(Boot.APP_BASEDIR), "/security");
        if (storeDir.exists()) {
            if (storeDir.isFile()) {
                throw new RuntimeException("Can't create security policy storage dir: " + storeDir.toString());
            }
        } else {
            if (storeDir.mkdir() == false) {
                throw new RuntimeException("The security policy storage dir: " + storeDir.toString() + " could not be created.");
            }
        }
        try {
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
        } catch (Exception e) {
            throw new RuntimeException("Error creating XML parsers:" + e.toString(), e);
        }
    }

    /**
    * Get a set of Permissions from the Policy Store
    */
    public PermissionCollection getPermissions(ExecutableDesc execDesc) {
        if (execDesc == null) return null;
        String perm = null;
        String fileName = execDesc.getSerialNumber();
        if (fileName == null) return null;
        fileName = generateFilename(fileName.getBytes());
        File f = getFile(fileName);
        logger.info("Looking for app security file:" + fileName);
        if (f == null || f.exists() == false) {
            return null;
        }
        PermissionCollection perms = null;
        try {
            FileInputStream in = new FileInputStream(f);
            Document doc = docBuilder.parse(in);
            NodeList nodes = doc.getElementsByTagName("application");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element appE = (Element) nodes.item(i);
                String name = appE.getAttribute("name");
                String serialNum = appE.getAttribute("serial-num");
                String execDescSerialNum = execDesc.getSerialNumber();
                if (!serialNum.equals(execDescSerialNum)) continue;
                NodeList permElements = appE.getElementsByTagName("permission");
                perms = createPermissionsFromElements(permElements);
                if (perms != null) break;
            }
            in.close();
        } catch (Exception exp) {
            BootSecurityManager.securityLogger.log(Level.SEVERE, "Invalid stored permission for " + execDesc.getName(), exp);
        }
        return perms;
    }

    /**
    * Get a set of Permissions for the Certification Path. 
    * This will return null if no permissions can be found stored for the cert
    * @return PermissionCollection for this cert
    */
    public PermissionCollection getPermissions(X509Certificate cert) {
        if (cert == null) return null;
        String perm = null;
        String fileName = generateFilename(cert.getSerialNumber().toByteArray());
        if (fileName == null) return null;
        logger.info("Looking for cert security file:" + fileName);
        File f = getFile(fileName);
        if (f == null || f.exists() == false) {
            return null;
        }
        PermissionCollection perms = null;
        try {
            FileInputStream in = new FileInputStream(f);
            Document doc = docBuilder.parse(in);
            NodeList nodes = doc.getElementsByTagName("certificate");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element certE = (Element) nodes.item(i);
                String name = certE.getAttribute("name");
                String serialNum = new String(new BASE64Decoder().decodeBuffer(certE.getAttribute("serial-num")));
                if (!name.equals(cert.getSubjectX500Principal().getName())) continue;
                if (!serialNum.equals(new String(cert.getSerialNumber().toByteArray()))) continue;
                NodeList permElements = certE.getElementsByTagName("permission");
                perms = createPermissionsFromElements(permElements);
                if (perms != null) break;
            }
            in.close();
        } catch (Exception exp) {
            BootSecurityManager.securityLogger.log(Level.SEVERE, "Invalid stored permission for " + cert.getSubjectDN().toString(), exp);
        }
        return perms;
    }

    /**
    * Get the Permissions for a given set of Permission Elements in a NodeList
    */
    private PermissionCollection createPermissionsFromElements(NodeList nodeList) {
        Permissions perms = new Permissions();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element permE = (Element) nodeList.item(i);
            String className = permE.getAttribute("class");
            String name = permE.getAttribute("name");
            String actions = permE.getAttribute("actions");
            try {
                Reflection kit = Reflection.getToolKit();
                Permission p = (Permission) kit.newInstance(Class.forName(className), new String[] { name, actions });
                perms.add(p);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.toString(), e);
            }
        }
        return perms;
    }

    /**
    * Store Permissions for an Application
    */
    public void storePermissions(ExecutableDesc execDesc, PermissionCollection permCollection) throws PolicyStoreException {
        String fileName = generateFilename(execDesc.getSerialNumber().getBytes());
        if (fileName == null) throw new PolicyStoreException("Can't generate filename for permission.", null);
        logger.info("Storing file:" + fileName);
        File f = new File(storeDir, fileName);
        BootPolicy.addPermissions(getPermissions(execDesc), permCollection);
        try {
            Document doc = docBuilder.newDocument();
            Element securityE = doc.createElement("xito-security");
            Element appE = doc.createElement("application");
            if (execDesc.getDisplayName() != null) appE.setAttribute("name", execDesc.getDisplayName());
            appE.setAttribute("serial-num", execDesc.getSerialNumber());
            securityE.appendChild(appE);
            Enumeration perms = permCollection.elements();
            while (perms.hasMoreElements()) {
                Permission perm = (Permission) perms.nextElement();
                appE.appendChild(createPermissionElement(doc, perm));
            }
            FileOutputStream out = new FileOutputStream(f);
            StreamResult result = new StreamResult(out);
            transformer.transform(new DOMSource(securityE), result);
            out.flush();
            out.close();
        } catch (Exception exp) {
            throw new PolicyStoreException(exp.toString(), exp);
        }
    }

    /**
    * Create a DOM element for a Permission Object
    * @param Document to create Element from
    * @param permission to write
    */
    private Element createPermissionElement(Document doc, Permission perm) {
        Element permE = doc.createElement("permission");
        permE.setAttribute("class", perm.getClass().getName());
        permE.setAttribute("name", perm.getName());
        permE.setAttribute("actions", perm.getActions());
        return permE;
    }

    /**
    * Attempt to encode the FileName and locate the file if it failes to find a
    * file it will call itself recursively encode again until it finds one or reaches the retry count
    */
    private File getFile(String name) {
        File f = new File(storeDir, name);
        return f;
    }

    /**
    * Generate a filename from a byte[] by using an MD5 Digest
    */
    private String generateFilename(byte[] data) {
        String encoded = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            String digestSTR = new BASE64Encoder().encode(digest);
            encoded = URLEncoder.encode(digestSTR, "UTF-8");
        } catch (NoSuchAlgorithmException noAlg) {
            noAlg.printStackTrace();
        } catch (UnsupportedEncodingException badEnc) {
            badEnc.printStackTrace();
        }
        return encoded;
    }

    /**
    * Store Permissions for the Certification Path
    */
    public void storePermissions(X509Certificate cert, PermissionCollection permCollection) throws PolicyStoreException {
        if (cert == null) {
            throw new PolicyStoreException("Can not store null Certificate", null);
        }
        String fileName = generateFilename(cert.getSerialNumber().toByteArray());
        logger.info("Storing file:" + fileName);
        File f = new File(storeDir, fileName);
        BootPolicy.addPermissions(getPermissions(cert), permCollection);
        try {
            Document doc = docBuilder.newDocument();
            Element securityE = doc.createElement("xito-security");
            Element certE = doc.createElement("certificate");
            certE.setAttribute("name", cert.getSubjectX500Principal().getName());
            certE.setAttribute("serial-num", new BASE64Encoder().encode(cert.getSerialNumber().toByteArray()));
            securityE.appendChild(certE);
            Enumeration perms = permCollection.elements();
            while (perms.hasMoreElements()) {
                Permission perm = (Permission) perms.nextElement();
                certE.appendChild(createPermissionElement(doc, perm));
            }
            FileOutputStream out = new FileOutputStream(f);
            StreamResult result = new StreamResult(out);
            transformer.transform(new DOMSource(securityE), result);
            out.flush();
            out.close();
        } catch (Exception exp) {
            throw new PolicyStoreException(exp.toString(), exp);
        }
    }

    /**
    * Get the KeyStore associated with this PolicyStore
    */
    public synchronized KeyStore getKeyStore() throws KeyStoreException {
        if (storeDir.exists() == false) throw new RuntimeException("Can't access security storage dir: " + storeDir.toString());
        File keyStoreFile = new File(storeDir, ".keystore");
        if (keyStoreFile.exists() == false) {
            buildDefaultKeyStore(keyStoreFile);
        }
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(keyStoreFile), DEFAULT_KEYSTORE_PASS.toCharArray());
            return keyStore;
        } catch (Exception exp) {
            KeyStoreException keyStoreExp = new KeyStoreException("Could not load KeyStore");
            keyStoreExp.initCause(exp);
            throw keyStoreExp;
        }
    }

    /**
    * Build a new Default KeyStore
    */
    private void buildDefaultKeyStore(File keyStoreFile) throws KeyStoreException {
        try {
            String sep = File.separator;
            String cacertsPath = System.getProperty("java.home") + sep + "lib" + sep + "security" + sep + "cacerts";
            File cacertsFile = new File(cacertsPath);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(cacertsFile), "changeit".toCharArray());
            keyStore.store(new FileOutputStream(keyStoreFile), DEFAULT_KEYSTORE_PASS.toCharArray());
        } catch (Exception exp) {
            KeyStoreException keyStoreExp = new KeyStoreException("Could not load KeyStore");
            keyStoreExp.initCause(exp);
            throw keyStoreExp;
        }
    }
}
