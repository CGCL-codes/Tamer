package keystores;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Vector;

/**
 * 
 * @author eduard
 * @see    http://finger-in-the-eye.blogspot.com/2007/03/cmo-acceder-al-keystore-de-firefox-con.html
 */
public class FirefoxForWindowsKeyStore {

    /**
	 * Returns a vector of all the info of the certificates in the Mozilla Firefox keystore
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 * @throws UnrecoverableKeyException
	 */
    public static Vector<KeyStoreEntry> getKeyStoreEntries() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        Vector<KeyStoreEntry> ksentries = new Vector<KeyStoreEntry>();
        String firefoxKsDir = SearchFirefoxKeystoreFolder.getFirefoxKeyStoreDirectory(new File("C:\\Documents and Settings"), 0).replace("\\", "/");
        System.out.println("Directory Firefox=" + firefoxKsDir);
        String pkcs11config = "name = NSS" + "\n" + "nssLibraryDirectory = " + "C:/Archivos de programa/Mozilla Firefox" + "\n" + "nssSecmodDirectory = \"" + firefoxKsDir + "\" \n" + "nssDbMode = readOnly" + "\n" + "nssModule = keystore" + "\n" + "\r";
        System.load("C:/Archivos de programa/Mozilla Firefox/mozcrt19.dll");
        System.load("C:/Archivos de programa/Mozilla Firefox/sqlite3.dll");
        System.load("C:/Archivos de programa/Mozilla Firefox/nspr4.dll");
        System.load("C:/Archivos de programa/Mozilla Firefox/plc4.dll");
        System.load("C:/Archivos de programa/Mozilla Firefox/plds4.dll");
        System.load("C:/Archivos de programa/Mozilla Firefox/nssutil3.dll");
        byte[] pkcs11configBytes = pkcs11config.getBytes();
        ByteArrayInputStream configStream = new ByteArrayInputStream(pkcs11configBytes);
        Provider pkcs11Provider = new sun.security.pkcs11.SunPKCS11(configStream);
        Security.addProvider(pkcs11Provider);
        KeyStore ks = null;
        ks = KeyStore.getInstance("PKCS11", pkcs11Provider);
        ks.load(null, "".toCharArray());
        Enumeration<String> enumeration = ks.aliases();
        while (enumeration.hasMoreElements()) {
            boolean hasErrors = false;
            String alias = enumeration.nextElement().toString();
            X509Certificate[] certs1 = (X509Certificate[]) ks.getCertificateChain(alias);
            try {
                System.out.println(certs1[0].getSubjectDN().toString());
            } catch (Exception e) {
                hasErrors = true;
            }
            if (!hasErrors) {
                PrivateKey pk = (PrivateKey) ks.getKey(alias, null);
                ksentries.add(new KeyStoreEntry(certs1, alias, pk, ks));
            }
        }
        return ksentries;
    }

    public static void main(String[] args) {
        Vector<KeyStoreEntry> ksEntry = null;
        try {
            ksEntry = FirefoxForWindowsKeyStore.getKeyStoreEntries();
            for (int i = 0; i < ksEntry.size(); i++) {
                ksEntry.get(i).showInfo("" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
