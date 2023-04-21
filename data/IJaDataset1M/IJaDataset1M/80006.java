package org.gudy.azureus2.core3.security.impl;

import java.io.*;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.Key;
import java.security.KeyStore;
import java.security.Permission;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.*;
import javax.net.ssl.*;
import org.gudy.azureus2.core3.config.COConfigurationManager;
import org.gudy.azureus2.core3.logging.LogAlert;
import org.gudy.azureus2.core3.logging.LogEvent;
import org.gudy.azureus2.core3.logging.LogIDs;
import org.gudy.azureus2.core3.logging.Logger;
import org.gudy.azureus2.core3.security.SECertificateListener;
import org.gudy.azureus2.core3.security.SEKeyDetails;
import org.gudy.azureus2.core3.security.SEPasswordListener;
import org.gudy.azureus2.core3.security.SESecurityManager;
import org.gudy.azureus2.core3.util.AEMonitor;
import org.gudy.azureus2.core3.util.Debug;
import org.gudy.azureus2.core3.util.FileUtil;
import org.gudy.azureus2.core3.util.RandomUtils;
import com.aelitis.azureus.core.util.CopyOnWriteList;

public class SESecurityManagerImpl {

    private static final LogIDs LOGID = LogIDs.NET;

    protected static SESecurityManagerImpl singleton = new SESecurityManagerImpl();

    protected static String KEYSTORE_TYPE;

    static {
        String[] types = { "JKS", "GKR" };
        for (int i = 0; i < types.length; i++) {
            try {
                KeyStore.getInstance(types[i]);
                KEYSTORE_TYPE = types[i];
                break;
            } catch (Throwable e) {
            }
        }
        if (KEYSTORE_TYPE == null) {
            KEYSTORE_TYPE = "JKS";
        }
        Logger.log(new LogEvent(LOGID, "Keystore type is " + KEYSTORE_TYPE));
    }

    protected String keystore_name;

    protected String truststore_name;

    protected List certificate_listeners = new ArrayList();

    protected CopyOnWriteList password_listeners = new CopyOnWriteList();

    private static ThreadLocal tls = new ThreadLocal() {

        public Object initialValue() {
            return (null);
        }
    };

    protected Map password_handlers = new HashMap();

    protected Map certificate_handlers = new HashMap();

    protected boolean exit_vm_permitted = false;

    private AzureusSecurityManager my_sec_man;

    protected AEMonitor this_mon = new AEMonitor("SESecurityManager");

    public static SESecurityManagerImpl getSingleton() {
        return (singleton);
    }

    private boolean initialized = false;

    private List stoppable_threads = new ArrayList();

    public void initialise() {
        synchronized (this) {
            if (initialized) return;
            initialized = true;
        }
        keystore_name = FileUtil.getUserFile(SESecurityManager.SSL_KEYS).getAbsolutePath();
        truststore_name = FileUtil.getUserFile(SESecurityManager.SSL_CERTS).getAbsolutePath();
        System.setProperty("javax.net.ssl.trustStore", truststore_name);
        System.setProperty("javax.net.ssl.trustStorePassword", SESecurityManager.SSL_PASSWORD);
        installAuthenticator();
        String[] providers = { "com.sun.net.ssl.internal.ssl.Provider", "org.metastatic.jessie.provider.Jessie" };
        String provider = null;
        for (int i = 0; i < providers.length; i++) {
            try {
                Class.forName(providers[i]).newInstance();
                provider = providers[i];
                break;
            } catch (Throwable e) {
            }
        }
        if (provider == null) {
            Debug.out("No SSL provider available");
        }
        try {
            SESecurityManagerBC.initialise();
        } catch (Throwable e) {
            Logger.log(new LogEvent(LOGID, LogEvent.LT_ERROR, "Bouncy Castle not available"));
        }
        installSecurityManager();
        ensureStoreExists(keystore_name);
        ensureStoreExists(truststore_name);
        initEmptyTrustStore();
    }

    private void initEmptyTrustStore() {
        try {
            File target = new File(truststore_name);
            if (target.exists() && target.length() > 2 * 1024) {
                return;
            }
            KeyStore keystore = getTrustStore();
            if (keystore.size() == 0) {
                File cacerts = new File(new File(new File(System.getProperty("java.home"), "lib"), "security"), "cacerts");
                if (cacerts.exists()) {
                    FileUtil.copyFile(cacerts, target);
                    try {
                        getTrustStore();
                    } catch (Throwable e) {
                        target.delete();
                        ensureStoreExists(truststore_name);
                    }
                }
            }
        } catch (Throwable e) {
        }
    }

    public String getKeystoreName() {
        return (keystore_name);
    }

    public String getKeystorePassword() {
        return (SESecurityManager.SSL_PASSWORD);
    }

    protected void installSecurityManager() {
        String prop = System.getProperty("azureus.security.manager.install", "1");
        if (prop.equals("0")) {
            Debug.outNoStack("Not installing security manager - disabled by system property");
            return;
        }
        try {
            final SecurityManager old_sec_man = System.getSecurityManager();
            my_sec_man = new AzureusSecurityManager(old_sec_man);
            System.setSecurityManager(my_sec_man);
        } catch (Throwable e) {
            Debug.printStackTrace(e);
        }
    }

    public void stopThread(Thread t) {
        synchronized (stoppable_threads) {
            stoppable_threads.add(Thread.currentThread());
        }
        try {
            t.stop();
        } finally {
            synchronized (stoppable_threads) {
                stoppable_threads.remove(Thread.currentThread());
            }
        }
    }

    public void exitVM(int status) {
        try {
            exit_vm_permitted = true;
            try {
                System.exit(status);
            } catch (Throwable t) {
            }
        } finally {
            exit_vm_permitted = false;
        }
    }

    public void installAuthenticator() {
        Authenticator.setDefault(new Authenticator() {

            protected AEMonitor auth_mon = new AEMonitor("SESecurityManager:auth");

            protected PasswordAuthentication getPasswordAuthentication() {
                try {
                    auth_mon.enter();
                    PasswordAuthentication res = getAuthentication(getRequestingPrompt(), getRequestingProtocol(), getRequestingHost(), getRequestingPort());
                    return (res);
                } finally {
                    auth_mon.exit();
                }
            }
        });
    }

    public PasswordAuthentication getAuthentication(String realm, String protocol, String host, int port) {
        if (protocol.toLowerCase().startsWith("socks")) {
            String socks_user = COConfigurationManager.getStringParameter("Proxy.Username").trim();
            String socks_pw = COConfigurationManager.getStringParameter("Proxy.Password").trim();
            if (socks_user.equalsIgnoreCase("<none>")) {
                return (new PasswordAuthentication("", "".toCharArray()));
            }
            if (socks_user.length() == 0) {
                Logger.log(new LogAlert(false, LogAlert.AT_WARNING, "Socks server is requesting authentication, please setup user and password in config"));
            }
            return (new PasswordAuthentication(socks_user, socks_pw.toCharArray()));
        }
        try {
            URL tracker_url = new URL(protocol + "://" + host + ":" + port + "/");
            return (getPasswordAuthentication(realm, tracker_url));
        } catch (MalformedURLException e) {
            Debug.printStackTrace(e);
            return (null);
        }
    }

    protected boolean checkKeyStoreHasEntry() {
        File f = new File(keystore_name);
        if (!f.exists()) {
            Logger.logTextResource(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_ERROR, "Security.keystore.empty"), new String[] { keystore_name });
            return (false);
        }
        try {
            KeyStore key_store = loadKeyStore();
            Enumeration enumx = key_store.aliases();
            if (!enumx.hasMoreElements()) {
                Logger.logTextResource(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_ERROR, "Security.keystore.empty"), new String[] { keystore_name });
                return (false);
            }
        } catch (Throwable e) {
            Logger.logTextResource(new LogAlert(LogAlert.UNREPEATABLE, LogAlert.AT_ERROR, "Security.keystore.corrupt"), new String[] { keystore_name });
            return (false);
        }
        return (true);
    }

    protected boolean ensureStoreExists(String name) {
        try {
            this_mon.enter();
            KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
            if (!new File(name).exists()) {
                keystore.load(null, null);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(name);
                    keystore.store(out, SESecurityManager.SSL_PASSWORD.toCharArray());
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                return (true);
            } else {
                return (false);
            }
        } catch (Throwable e) {
            Debug.printStackTrace(e);
            return (false);
        } finally {
            this_mon.exit();
        }
    }

    public KeyStore getKeyStore() throws Exception {
        return (loadKeyStore());
    }

    public KeyStore getTrustStore() throws Exception {
        KeyStore keystore = KeyStore.getInstance(KEYSTORE_TYPE);
        if (!new File(truststore_name).exists()) {
            keystore.load(null, null);
        } else {
            FileInputStream in = null;
            try {
                in = new FileInputStream(truststore_name);
                keystore.load(in, SESecurityManager.SSL_PASSWORD.toCharArray());
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
        return (keystore);
    }

    protected KeyStore loadKeyStore() throws Exception {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        return (loadKeyStore(keyManagerFactory));
    }

    protected KeyStore loadKeyStore(KeyManagerFactory keyManagerFactory) throws Exception {
        KeyStore key_store = KeyStore.getInstance(KEYSTORE_TYPE);
        if (!new File(keystore_name).exists()) {
            key_store.load(null, null);
        } else {
            InputStream kis = null;
            try {
                kis = new FileInputStream(keystore_name);
                key_store.load(kis, SESecurityManager.SSL_PASSWORD.toCharArray());
            } finally {
                if (kis != null) {
                    kis.close();
                }
            }
        }
        keyManagerFactory.init(key_store, SESecurityManager.SSL_PASSWORD.toCharArray());
        return (key_store);
    }

    public SSLServerSocketFactory getSSLServerSocketFactory() throws Exception {
        if (!checkKeyStoreHasEntry()) {
            return (null);
        }
        SSLContext context = SSLContext.getInstance("SSL");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        loadKeyStore(keyManagerFactory);
        context.init(keyManagerFactory.getKeyManagers(), null, RandomUtils.SECURE_RANDOM);
        SSLServerSocketFactory factory = context.getServerSocketFactory();
        return (factory);
    }

    public SEKeyDetails getKeyDetails(String alias) throws Exception {
        KeyStore key_store = loadKeyStore();
        final Key key = key_store.getKey(alias, SESecurityManager.SSL_PASSWORD.toCharArray());
        if (key == null) {
            return (null);
        }
        java.security.cert.Certificate[] chain = key_store.getCertificateChain(alias);
        final X509Certificate[] res = new X509Certificate[chain.length];
        for (int i = 0; i < chain.length; i++) {
            if (!(chain[i] instanceof X509Certificate)) {
                throw (new Exception("Certificate chain must be comprised of X509Certificate entries"));
            }
            res[i] = (X509Certificate) chain[i];
        }
        return (new SEKeyDetails() {

            public Key getKey() {
                return (key);
            }

            public X509Certificate[] getCertificateChain() {
                return (res);
            }
        });
    }

    public Certificate createSelfSignedCertificate(String alias, String cert_dn, int strength) throws Exception {
        return (SESecurityManagerBC.createSelfSignedCertificate(this, alias, cert_dn, strength));
    }

    public SSLSocketFactory getSSLSocketFactory() {
        try {
            this_mon.enter();
            KeyStore keystore = getTrustStore();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keystore);
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, tmf.getTrustManagers(), null);
            SSLSocketFactory factory = ctx.getSocketFactory();
            return (factory);
        } catch (Throwable e) {
            Debug.printStackTrace(e);
            return ((SSLSocketFactory) SSLSocketFactory.getDefault());
        } finally {
            this_mon.exit();
        }
    }

    public SSLSocketFactory installServerCertificates(URL https_url) {
        try {
            this_mon.enter();
            String host = https_url.getHost();
            int port = https_url.getPort();
            if (port == -1) {
                port = 443;
            }
            SSLSocket socket = null;
            try {
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                } };
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, RandomUtils.SECURE_RANDOM);
                SSLSocketFactory factory = sc.getSocketFactory();
                socket = (SSLSocket) factory.createSocket(host, port);
                socket.startHandshake();
                java.security.cert.Certificate[] serverCerts = socket.getSession().getPeerCertificates();
                if (serverCerts.length == 0) {
                    return (null);
                }
                java.security.cert.Certificate cert = serverCerts[0];
                java.security.cert.X509Certificate x509_cert;
                if (cert instanceof java.security.cert.X509Certificate) {
                    x509_cert = (java.security.cert.X509Certificate) cert;
                } else {
                    java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
                    x509_cert = (java.security.cert.X509Certificate) cf.generateCertificate(new ByteArrayInputStream(cert.getEncoded()));
                }
                String resource = https_url.toString();
                int param_pos = resource.indexOf("?");
                if (param_pos != -1) {
                    resource = resource.substring(0, param_pos);
                }
                String url_s = https_url.getProtocol() + "://" + https_url.getHost() + ":" + https_url.getPort() + "/";
                Object[] handler = (Object[]) certificate_handlers.get(url_s);
                String alias = host.concat(":").concat(String.valueOf(port));
                KeyStore keystore = getTrustStore();
                byte[] new_encoded = x509_cert.getEncoded();
                int count = 0;
                while (count < 256) {
                    String test_alias = count == 0 ? alias : (alias + "." + count);
                    Certificate existing = keystore.getCertificate(test_alias);
                    if (existing != null) {
                        if (Arrays.equals(new_encoded, existing.getEncoded())) {
                            alias = test_alias;
                            break;
                        }
                    } else {
                        alias = test_alias;
                        break;
                    }
                    count++;
                }
                if (handler != null) {
                    if (((SECertificateListener) handler[0]).trustCertificate(resource, x509_cert)) {
                        return (addCertToTrustStore(alias, cert, true));
                    }
                }
                for (int i = 0; i < certificate_listeners.size(); i++) {
                    if (((SECertificateListener) certificate_listeners.get(i)).trustCertificate(resource, x509_cert)) {
                        return (addCertToTrustStore(alias, cert, true));
                    }
                }
                return (null);
            } catch (Throwable e) {
                Debug.printStackTrace(e);
                return (null);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Throwable e) {
                        Debug.printStackTrace(e);
                    }
                }
            }
        } finally {
            this_mon.exit();
        }
    }

    public SSLSocketFactory installServerCertificates(String alias, String host, int port) {
        try {
            this_mon.enter();
            SSLSocket socket = null;
            try {
                TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                } };
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, RandomUtils.SECURE_RANDOM);
                SSLSocketFactory factory = sc.getSocketFactory();
                socket = (SSLSocket) factory.createSocket(host, port);
                socket.startHandshake();
                java.security.cert.Certificate[] serverCerts = socket.getSession().getPeerCertificates();
                if (serverCerts.length == 0) {
                    return (null);
                }
                java.security.cert.Certificate cert = serverCerts[0];
                java.security.cert.X509Certificate x509_cert;
                if (cert instanceof java.security.cert.X509Certificate) {
                    x509_cert = (java.security.cert.X509Certificate) cert;
                } else {
                    java.security.cert.CertificateFactory cf = java.security.cert.CertificateFactory.getInstance("X.509");
                    x509_cert = (java.security.cert.X509Certificate) cf.generateCertificate(new ByteArrayInputStream(cert.getEncoded()));
                }
                return (addCertToTrustStore(alias, cert, false));
            } catch (Throwable e) {
                Debug.printStackTrace(e);
                return (null);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (Throwable e) {
                        Debug.printStackTrace(e);
                    }
                }
            }
        } finally {
            this_mon.exit();
        }
    }

    protected void addCertToKeyStore(String alias, Key public_key, java.security.cert.Certificate[] certChain) throws Exception {
        try {
            this_mon.enter();
            KeyStore key_store = loadKeyStore();
            if (key_store.containsAlias(alias)) {
                key_store.deleteEntry(alias);
            }
            key_store.setKeyEntry(alias, public_key, SESecurityManager.SSL_PASSWORD.toCharArray(), certChain);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(keystore_name);
                key_store.store(out, SESecurityManager.SSL_PASSWORD.toCharArray());
            } catch (Throwable e) {
                Debug.printStackTrace(e);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
        } finally {
            this_mon.exit();
        }
    }

    protected SSLSocketFactory addCertToTrustStore(String alias, java.security.cert.Certificate cert, boolean update_https_factory) throws Exception {
        try {
            this_mon.enter();
            KeyStore keystore = getTrustStore();
            if (cert != null) {
                if (keystore.containsAlias(alias)) {
                    keystore.deleteEntry(alias);
                }
                keystore.setCertificateEntry(alias, cert);
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(truststore_name);
                    keystore.store(out, SESecurityManager.SSL_PASSWORD.toCharArray());
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keystore);
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, tmf.getTrustManagers(), null);
            SSLSocketFactory factory = ctx.getSocketFactory();
            if (update_https_factory) {
                HttpsURLConnection.setDefaultSSLSocketFactory(factory);
            }
            return (factory);
        } finally {
            this_mon.exit();
        }
    }

    public PasswordAuthentication getPasswordAuthentication(String realm, URL tracker) {
        SEPasswordListener thread_listener = (SEPasswordListener) tls.get();
        if (thread_listener != null) {
            return (thread_listener.getAuthentication(realm, tracker));
        }
        Object[] handler = (Object[]) password_handlers.get(tracker.toString());
        if (handler != null) {
            try {
                return (((SEPasswordListener) handler[0]).getAuthentication(realm, (URL) handler[1]));
            } catch (Throwable e) {
                Debug.printStackTrace(e);
            }
        }
        Iterator it = password_listeners.iterator();
        while (it.hasNext()) {
            try {
                PasswordAuthentication res = ((SEPasswordListener) it.next()).getAuthentication(realm, tracker);
                if (res != null) {
                    return (res);
                }
            } catch (Throwable e) {
                Debug.printStackTrace(e);
            }
        }
        return (null);
    }

    public void setPasswordAuthenticationOutcome(String realm, URL tracker, boolean success) {
        SEPasswordListener thread_listener = (SEPasswordListener) tls.get();
        if (thread_listener != null) {
            thread_listener.setAuthenticationOutcome(realm, tracker, success);
        }
        Iterator it = password_listeners.iterator();
        while (it.hasNext()) {
            ((SEPasswordListener) it.next()).setAuthenticationOutcome(realm, tracker, success);
        }
    }

    public void addPasswordListener(SEPasswordListener l) {
        try {
            this_mon.enter();
            password_listeners.add(l);
        } finally {
            this_mon.exit();
        }
    }

    public void removePasswordListener(SEPasswordListener l) {
        try {
            this_mon.enter();
            password_listeners.remove(l);
        } finally {
            this_mon.exit();
        }
    }

    public void clearPasswords() {
        SEPasswordListener thread_listener = (SEPasswordListener) tls.get();
        if (thread_listener != null) {
            thread_listener.clearPasswords();
        }
        Iterator it = password_listeners.iterator();
        while (it.hasNext()) {
            try {
                ((SEPasswordListener) it.next()).clearPasswords();
            } catch (Throwable e) {
                Debug.printStackTrace(e);
            }
        }
    }

    public void setThreadPasswordHandler(SEPasswordListener l) {
        tls.set(l);
    }

    public void unsetThreadPasswordHandler() {
        tls.set(null);
    }

    public void setPasswordHandler(URL url, SEPasswordListener l) {
        String url_s = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + "/";
        if (l == null) {
            password_handlers.remove(url_s);
        } else {
            password_handlers.put(url_s, new Object[] { l, url });
        }
    }

    public void addCertificateListener(SECertificateListener l) {
        try {
            this_mon.enter();
            certificate_listeners.add(l);
        } finally {
            this_mon.exit();
        }
    }

    public void setCertificateHandler(URL url, SECertificateListener l) {
        String url_s = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + "/";
        if (l == null) {
            certificate_handlers.remove(url_s);
        } else {
            certificate_handlers.put(url_s, new Object[] { l, url });
        }
    }

    public void removeCertificateListener(SECertificateListener l) {
        try {
            this_mon.enter();
            certificate_listeners.remove(l);
        } finally {
            this_mon.exit();
        }
    }

    public Class[] getClassContext() {
        if (my_sec_man == null) {
            return (new Class[0]);
        }
        return (my_sec_man.getClassContext());
    }

    private final class AzureusSecurityManager extends SecurityManager {

        private SecurityManager old_sec_man;

        private AzureusSecurityManager(SecurityManager _old_sec_man) {
            old_sec_man = _old_sec_man;
        }

        public void checkAccept(String host, int port) {
        }

        public void checkRead(String file) {
        }

        public void checkWrite(String file) {
        }

        public void checkConnect(String host, int port) {
        }

        public void checkExit(int status) {
            if (old_sec_man != null) {
                old_sec_man.checkExit(status);
            }
            if (!exit_vm_permitted) {
                String prop = System.getProperty("azureus.security.manager.permitexit", "0");
                if (prop.equals("0")) {
                    throw (new SecurityException("VM exit operation prohibited"));
                }
            }
        }

        public void checkPermission(Permission perm) {
            checkPermission(perm, null);
        }

        public void checkPermission(Permission perm, Object context) {
            if (perm instanceof RuntimePermission) {
                String name = perm.getName();
                if (name.equals("stopThread")) {
                    synchronized (stoppable_threads) {
                        if (stoppable_threads.contains(Thread.currentThread())) {
                            return;
                        }
                    }
                    throw (new SecurityException("Thread.stop operation prohibited"));
                } else if (name.equals("setSecurityManager")) {
                    throw (new SecurityException("Permission Denied"));
                }
            }
            if (old_sec_man != null) {
                if (context == null) {
                    old_sec_man.checkPermission(perm);
                } else {
                    old_sec_man.checkPermission(perm, context);
                }
            }
        }

        public Class[] getClassContext() {
            Class[] res = super.getClassContext();
            if (res.length <= 3) {
                return (new Class[0]);
            }
            Class[] trimmed = new Class[res.length - 3];
            System.arraycopy(res, 3, trimmed, 0, trimmed.length);
            return (trimmed);
        }
    }

    ;

    public static void main(String[] args) {
        SESecurityManagerImpl man = SESecurityManagerImpl.getSingleton();
        man.initialise();
        try {
            man.createSelfSignedCertificate("SomeAlias", "CN=fred,OU=wap,O=wip,L=here,ST=there,C=GB", 1000);
        } catch (Throwable e) {
            Debug.printStackTrace(e);
        }
    }
}
