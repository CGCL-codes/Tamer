package org.avis.router;

import java.net.InetSocketAddress;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.util.ExceptionMonitor;
import org.junit.Before;
import org.junit.Test;
import org.avis.io.ClientFrameCodec;
import org.avis.io.ExceptionMonitorLogger;

public class JUTestRouterTLS {

    private static final int DEFAULT_PORT = JUTestRouter.PORT;

    private static final int SECURE_PORT = DEFAULT_PORT + 1;

    @Before
    public void setup() {
        ExceptionMonitor.setInstance(ExceptionMonitorLogger.INSTANCE);
    }

    @Test
    public void connect() throws Exception {
        ExceptionMonitor.setInstance(ExceptionMonitorLogger.INSTANCE);
        RouterOptions options = new RouterOptions();
        URI keystore = getClass().getResource("tls_test.ks").toURI();
        options.set("Listen", "elvin://127.0.0.1:" + DEFAULT_PORT + " " + "elvin:/secure/127.0.0.1:" + SECURE_PORT);
        options.set("TLS.Keystore", keystore);
        options.set("TLS.Keystore-Passphrase", "testing");
        Router router = new Router(options);
        SimpleClient standardClient = new SimpleClient(new InetSocketAddress("127.0.0.1", DEFAULT_PORT));
        standardClient.connect();
        standardClient.close();
        SimpleClient secureClient = new SimpleClient(new InetSocketAddress("127.0.0.1", SECURE_PORT), createTLSFilters());
        secureClient.connect();
        secureClient.close();
        router.close();
    }

    private static DefaultIoFilterChainBuilder createTLSFilters() throws Exception {
        DefaultIoFilterChainBuilder filters = new DefaultIoFilterChainBuilder();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { ACCEPT_ALL_MANAGER }, null);
        SslFilter sslFilter = new SslFilter(sslContext);
        sslFilter.setUseClientMode(true);
        filters.addLast("ssl", sslFilter);
        filters.addLast("codec", ClientFrameCodec.FILTER);
        return filters;
    }

    static final X509TrustManager ACCEPT_ALL_MANAGER = new X509TrustManager() {

        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };
}
