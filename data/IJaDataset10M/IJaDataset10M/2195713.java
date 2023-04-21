package net.sf.traser.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.CredentialException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.Merlin;
import org.apache.ws.security.util.Loader;

/**
 *
 * @author Marcell Szathmári
 */
public class CryptoHandler implements Crypto, CryptoManager {

    /**
     * The password to open and store the keystore with.
     */
    private String password;

    /**
     * The timestamp of the loaded keystore file.
     */
    private long timestamp;

    /**
     * The file name of the keystore to use.
     */
    private final File keyStoreFile;

    /**
     * The crypto implementation to be used. 
     */
    private Merlin crypto;

    /**
     * The fair lock to serialize the requests and 
     */
    private final ReentrantLock lock = new ReentrantLock(true);

    /**
     * Creates an instance of Merlin with the same properties.
     * @param properties
     * @throws org.apache.ws.security.components.crypto.CredentialException
     * @throws java.io.IOException
     */
    public CryptoHandler(Properties properties) throws CredentialException, IOException {
        this(properties, CryptoHandler.class.getClassLoader());
    }

    /**
     * 
     * @param properties
     * @param loader
     * @throws org.apache.ws.security.components.crypto.CredentialException
     * @throws java.io.IOException
     */
    public CryptoHandler(Properties properties, ClassLoader loader) throws CredentialException, IOException {
        String location = properties.getProperty("org.apache.ws.security.crypto.merlin.file");
        password = properties.getProperty("org.apache.ws.security.crypto.merlin.keystore.password");
        File file = null;
        timestamp = 0;
        try {
            file = getKeyStoreFile(loader, location);
            timestamp = file.lastModified();
        } catch (Exception e) {
            throw new CredentialException(3, "proxyNotFound", new Object[] { location });
        } finally {
            keyStoreFile = file;
        }
        crypto = new Merlin(properties, loader);
    }

    /** The set of already opened keystore files. */
    public static HashMap<ClassLoader, HashMap<String, File>> keystores = new HashMap<ClassLoader, HashMap<String, File>>();

    /**
     * Locates the KeyStore file using the provided class loader.
     * @param loader the classloader to use to locate the keystore.
     * @param location the location of the keystore.
     * @return the keystore file.
     * @throws java.net.URISyntaxException 
     */
    public static File getKeyStoreFile(ClassLoader loader, String location) throws URISyntaxException {
        HashMap<String, File> files = keystores.get(loader);
        if (files == null) {
            keystores.put(loader, files = new HashMap<String, File>());
        }
        if (files.containsKey(location)) {
            return files.get(location);
        } else {
            File result;
            java.net.URL url = Loader.getResource(loader, location);
            if (url != null) {
                result = new File(url.toURI());
            } else {
                result = new File(location);
            }
            files.put(location, result);
            return result;
        }
    }

    /**
     * Locates the KeyStore file using the class loader that loaded this class.
     * @param location
     * @return
     * @throws java.net.URISyntaxException
     */
    public static File getKeyStoreFile(String location) throws URISyntaxException {
        return getKeyStoreFile(CryptoHandler.class.getClassLoader(), location);
    }

    /**
     * Returns the timestamp of the loaded keystore file.
     * @return the timestamp of the loaded keystore file.
     */
    public long getTimestamp() {
        return timestamp;
    }

    public boolean validateCertPath(X509Certificate[] certs) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.validateCertPath(certs);
        } finally {
            lock.unlock();
        }
    }

    public X509Certificate loadCertificate(InputStream in) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.loadCertificate(in);
        } finally {
            lock.unlock();
        }
    }

    public X509Certificate[] getX509Certificates(byte[] data, boolean reverse) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getX509Certificates(data, reverse);
        } finally {
            lock.unlock();
        }
    }

    public byte[] getSKIBytesFromCert(X509Certificate cert) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getSKIBytesFromCert(cert);
        } finally {
            lock.unlock();
        }
    }

    public PrivateKey getPrivateKey(String alias, String password) throws Exception {
        lock.lock();
        try {
            return crypto.getPrivateKey(alias, password);
        } finally {
            lock.unlock();
        }
    }

    public KeyStore getKeyStore() {
        lock.lock();
        try {
            return crypto.getKeyStore();
        } finally {
            lock.unlock();
        }
    }

    public String getDefaultX509Alias() {
        lock.lock();
        try {
            return crypto.getDefaultX509Alias();
        } finally {
            lock.unlock();
        }
    }

    public X509Certificate[] getCertificates(String alias) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getCertificates(alias);
        } finally {
            lock.unlock();
        }
    }

    public CertificateFactory getCertificateFactory() throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getCertificateFactory();
        } finally {
            lock.unlock();
        }
    }

    public byte[] getCertificateData(boolean reverse, X509Certificate[] certs) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getCertificateData(reverse, certs);
        } finally {
            lock.unlock();
        }
    }

    public String[] getAliasesForDN(String subjectDN) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getAliasesForDN(subjectDN);
        } finally {
            lock.unlock();
        }
    }

    public String getAliasForX509CertThumb(byte[] thumb) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getAliasForX509CertThumb(thumb);
        } finally {
            lock.unlock();
        }
    }

    public String getAliasForX509Cert(byte[] skiBytes) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getAliasForX509Cert(skiBytes);
        } finally {
            lock.unlock();
        }
    }

    public String getAliasForX509Cert(String issuer, BigInteger serialNumber) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getAliasForX509Cert(issuer, serialNumber);
        } finally {
            lock.unlock();
        }
    }

    public String getAliasForX509Cert(String issuer) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getAliasForX509Cert(issuer);
        } finally {
            lock.unlock();
        }
    }

    public String getAliasForX509Cert(Certificate cert) throws WSSecurityException {
        lock.lock();
        try {
            return crypto.getAliasForX509Cert(cert);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 
     * @param cert
     * @throws java.security.KeyStoreException
     */
    public String saveCertificate(X509Certificate cert) throws KeyStoreException {
        lock.lock();
        try {
            String alias = null;
            KeyStore ks = crypto.getKeyStore();
            String subjectDN = cert.getSubjectDN().getName();
            String[] aliases = crypto.getAliasesForDN(subjectDN);
            if (aliases.length > 0) {
                for (String a : aliases) {
                    ks.setCertificateEntry(a, cert);
                }
                alias = aliases[0];
            } else {
                int equalsIndex = Math.max(subjectDN.indexOf('='), 0);
                int commaIndex = subjectDN.indexOf(',');
                commaIndex = commaIndex < 0 ? subjectDN.length() : commaIndex;
                int spaceIndex = subjectDN.indexOf(' ');
                spaceIndex = spaceIndex < 0 ? subjectDN.length() : spaceIndex;
                subjectDN = subjectDN.substring(equalsIndex, Math.min(commaIndex, spaceIndex));
                int cnt = (int) Math.ceil(Math.random() * 99);
                while (ks.containsAlias(subjectDN + cnt++)) {
                }
                alias = subjectDN + cnt;
                ks.setCertificateEntry(alias, cert);
            }
            ks.store(new FileOutputStream(keyStoreFile), password.toCharArray());
            crypto.setKeyStore(ks);
            timestamp = keyStoreFile.lastModified();
            return alias;
        } catch (Exception ex) {
            Logger.getLogger(CryptoHandler.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Could not save certificate into keystore.", ex);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 
     * @param properties
     * @param loader
     */
    void reloadKeyStore(Properties properties, ClassLoader loader) throws CredentialException, IOException {
        lock.lock();
        try {
            crypto = new Merlin(properties, loader);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 
     * @param properties
     */
    void reloadKeyStore(Properties properties) throws CredentialException, IOException {
        lock.lock();
        try {
            crypto = new Merlin(properties);
        } finally {
            lock.unlock();
        }
    }
}
