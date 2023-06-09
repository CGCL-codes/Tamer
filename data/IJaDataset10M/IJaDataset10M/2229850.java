package org.apache.harmony.xnet.provider.jsse;

import org.apache.harmony.xnet.provider.jsse.SSLSocketFactoryImpl;
import org.apache.harmony.xnet.provider.jsse.SSLEngineImpl;
import org.apache.harmony.xnet.provider.jsse.SSLParameters;
import org.apache.harmony.xnet.provider.jsse.SSLServerSocketFactoryImpl;
import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContextSpi;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSessionContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/**
 * Implementation of SSLContext service provider interface.
 */
public class SSLContextImpl extends SSLContextSpi {

    private SSLSessionContextImpl clientSessionContext = new SSLSessionContextImpl();

    private SSLSessionContextImpl serverSessionContext = new SSLSessionContextImpl();

    protected SSLParameters sslParameters;

    public SSLContextImpl() {
        super();
    }

    @Override
    public void engineInit(KeyManager[] kms, TrustManager[] tms, SecureRandom sr) throws KeyManagementException {
        sslParameters = new SSLParameters(kms, tms, sr, clientSessionContext, serverSessionContext);
    }

    @Override
    public SSLSocketFactory engineGetSocketFactory() {
        if (sslParameters == null) {
            throw new IllegalStateException("SSLContext is not initiallized.");
        }
        return new SSLSocketFactoryImpl(sslParameters);
    }

    @Override
    public SSLServerSocketFactory engineGetServerSocketFactory() {
        if (sslParameters == null) {
            throw new IllegalStateException("SSLContext is not initiallized.");
        }
        return new SSLServerSocketFactoryImpl(sslParameters);
    }

    @Override
    public SSLEngine engineCreateSSLEngine(String host, int port) {
        if (sslParameters == null) {
            throw new IllegalStateException("SSLContext is not initiallized.");
        }
        return new SSLEngineImpl(host, port, (SSLParameters) sslParameters.clone());
    }

    @Override
    public SSLEngine engineCreateSSLEngine() {
        if (sslParameters == null) {
            throw new IllegalStateException("SSLContext is not initiallized.");
        }
        return new SSLEngineImpl((SSLParameters) sslParameters.clone());
    }

    @Override
    public SSLSessionContext engineGetServerSessionContext() {
        return serverSessionContext;
    }

    @Override
    public SSLSessionContext engineGetClientSessionContext() {
        return clientSessionContext;
    }
}
