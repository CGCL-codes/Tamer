package commons.ngen.utils.ssl;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeyStore {

    private java.security.KeyStore keyStore = null;

    private String privateKey = null;

    private String cert = null;

    private String caCert = null;

    private String crl = null;

    private boolean initialized = false;

    private CertificateAuthority ca;

    private PrivateKey pkey;

    private KeyStore(java.security.KeyStore keyStore) {
        this.keyStore = keyStore;
        this.initialized = false;
    }

    private void load() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        pkey = PrivateKey.getInstance();
        ca = CertificateAuthority.getInstance();
        pkey.init(privateKey);
        ca.init(cert, caCert, crl);
        keyStore.load(null);
        keyStore.setCertificateEntry("ca.zetta-core.net", ca.getAuthorityCertificate());
        keyStore.setKeyEntry("lic.zetta-core.net", pkey.getKey(), PrivateKey.getPassword(), ca.getServerChain());
    }

    private static class SingletonHolder {

        private static KeyStore instance = null;
    }

    public java.security.KeyStore getStore() {
        return keyStore;
    }

    public void init(String privateKey, String cert, String caCert, String crl) {
        if (initialized) {
            throw new RuntimeException("KeyStore already initialized");
        }
        this.privateKey = privateKey;
        this.cert = cert;
        this.caCert = caCert;
        this.crl = crl;
        try {
            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KeyStore getInstance() {
        if (SingletonHolder.instance == null) {
            try {
                SingletonHolder.instance = new KeyStore(java.security.KeyStore.getInstance("JKS", "SUN"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return SingletonHolder.instance;
    }

    public CertificateAuthority getCertificateAuthority() {
        return this.ca;
    }
}
