package org.jpos.q2.nserver;

import org.jpos.core.ConfigurationException;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.filter.ssl.KeyStoreFactory;
import org.apache.mina.filter.ssl.SslContextFactory;
import org.apache.mina.filter.ssl.SslFilter;
import java.io.File;

public class NServerSSL extends NServer implements NServerSSLMBean {

    String keystore;

    String keystorePassword;

    String keyPassword;

    public String getKeystore() {
        return keystore;
    }

    public void setKeystore(String keystore) {
        this.keystore = keystore;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    @Override
    protected void startService() throws Exception {
        if (keystore == null || keystore.trim().length() == 0) throw new ConfigurationException("keystore is a required parameter");
        if (keystorePassword == null || keystorePassword.trim().length() == 0) throw new ConfigurationException("keystore-password is a required parameter");
        if (keyPassword == null || keyPassword.trim().length() == 0) throw new ConfigurationException("key-password is a required parameter");
        super.startService();
    }

    @Override
    protected void preBindAcceptorHook(NioSocketAcceptor acceptor) throws Exception {
        final KeyStoreFactory keyStoreFactory = new KeyStoreFactory();
        final SslContextFactory f = new SslContextFactory();
        keyStoreFactory.setDataFile(new File(keystore));
        keyStoreFactory.setPassword(keystorePassword);
        f.setProtocol("TLS");
        f.setKeyManagerFactoryAlgorithm("SunX509");
        f.setKeyManagerFactoryKeyStore(keyStoreFactory.newInstance());
        f.setKeyManagerFactoryKeyStorePassword(keyPassword);
        SslFilter ssl = new SslFilter(f.newInstance());
        acceptor.getFilterChain().addFirst("sslFilter", ssl);
    }
}
