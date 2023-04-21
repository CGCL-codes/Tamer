package edu.psu.its.lionshare.security;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.KeyManagementException;
import java.security.PrivateKey;
import java.security.Principal;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509KeyManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Wrapper class for SSLSocket factory so that we can easily create sockets
 * that use the appropriate KeystoreManager and TruststoreManager.
 *
 * @author Lorin Metzger
 */
public class BasicSecureSocketFactory {

    private static final Log LOG = LogFactory.getLog(BasicSecureSocketFactory.class);

    private SSLSocketFactory factory;

    private HashMap<String, X509Certificate[]> certs = null;

    private HashMap<String, SSLSocketFactory> factories = null;

    private HashMap<String, KeyManager> keyManagers = null;

    private String selected_guid = null;

    private static BasicSecureSocketFactory instance = null;

    private BasicSecureSocketFactory() {
        certs = new HashMap<String, X509Certificate[]>();
        factories = new HashMap<String, SSLSocketFactory>();
        keyManagers = new HashMap<String, KeyManager>();
    }

    public static BasicSecureSocketFactory getInstance() {
        if (instance == null) {
            instance = new BasicSecureSocketFactory();
        }
        return instance;
    }

    public void addOpaqueCertChain(String guid, X509Certificate[] chain, PrivateKey key) {
        certs.put(guid, chain);
        KeyManager km = new BasicKeyManager(guid, chain, key);
        keyManagers.put(guid, km);
        factories.put(guid, createFactory(guid, km));
    }

    /**
   * Remove an opaque cert chain from the pool when it's no longer needed.
   */
    public void removeOpaqueCertChain(final String guid) {
        this.certs.remove(guid);
        this.factories.remove(guid);
    }

    private SSLSocketFactory createFactory(String guid, KeyManager keyManager) {
        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            KeyManager[] kms = new KeyManager[] { keyManager };
            sslcontext.init(kms, null, new java.security.SecureRandom());
            factory = sslcontext.getSocketFactory();
            return factory;
        } catch (NoSuchAlgorithmException e) {
            LOG.trace("Unable to obtain TLS SSLContext provider", e);
        } catch (KeyManagementException e) {
            LOG.trace("Unable to create KeyManager for opaque credentials", e);
        }
        return null;
    }

    /**
   * Return a reference to the {@KeyManger} for a guid.
   *
   * This method should only be called after adding the opaque 
   * cert chain via {@link #addOpaqueCertChain()}
   */
    public KeyManager getKeyManager(final String guid) {
        return this.keyManagers.get(guid);
    }

    public Socket createSocket(String host, int port, String guid) throws IOException {
        if (factories.get(guid) != null) {
            Socket sock = factories.get(guid).createSocket(host, port);
            ((SSLSocket) sock).setNeedClientAuth(true);
            return sock;
        } else {
            Socket sock = createFactory(null, null).createSocket(host, port);
            ((SSLSocket) sock).setNeedClientAuth(true);
            return sock;
        }
    }

    private class BasicKeyManager implements X509KeyManager {

        String guid = null;

        X509Certificate[] chain = null;

        PrivateKey key = null;

        public BasicKeyManager(String guid, X509Certificate[] chain, PrivateKey key) {
            this.guid = guid;
            this.chain = chain;
            this.key = key;
        }

        public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket) {
            if (guid != null) return guid;
            return KeystoreManager.getInstance().getOpaqueAlias();
        }

        public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket) {
            if (guid != null) return guid;
            return KeystoreManager.getInstance().getOpaqueAlias();
        }

        public X509Certificate[] getCertificateChain(String alias) {
            if (chain != null) {
                return chain;
            } else {
                try {
                    X509Certificate[] certs = (X509Certificate[]) KeystoreManager.getInstance().getKeyStore().getCertificateChain(KeystoreManager.getInstance().getOpaqueAlias());
                    return certs;
                } catch (Exception e) {
                }
            }
            return null;
        }

        public String[] getClientAliases(String keyType, Principal[] issuers) {
            return new String[] { guid };
        }

        public String[] getServerAliases(String keyType, Principal[] issuers) {
            return new String[] { KeystoreManager.getInstance().getIdentityAlias() };
        }

        public PrivateKey getPrivateKey(String alias) {
            if (key != null) {
                return key;
            } else {
                try {
                    PrivateKey key = (PrivateKey) KeystoreManager.getInstance().getKeyStore().getKey((alias), KeystoreManager.getInstance().getPassword().toCharArray());
                    return key;
                } catch (Exception e) {
                }
            }
            return null;
        }
    }
}
