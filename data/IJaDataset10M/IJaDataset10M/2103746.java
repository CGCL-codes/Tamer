package net.sourceforge.xconf.toolbox.ssl;

import java.security.cert.X509Certificate;
import com.sun.net.ssl.X509TrustManager;

/**
 * Old JSSE version of an X509TrustManager that accepts all certificates.
 *
 * @author Tom Czarniecki
 */
public class OldEasyX509TrustManager implements X509TrustManager {

    /**
     * Returns <code>true</code>.
     */
    public boolean isClientTrusted(X509Certificate[] arg0) {
        return true;
    }

    /**
     * Returns <code>true</code>.
     */
    public boolean isServerTrusted(X509Certificate[] arg0) {
        return true;
    }

    /**
     * @see com.sun.net.ssl.X509TrustManager#getAcceptedIssuers()
     */
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
