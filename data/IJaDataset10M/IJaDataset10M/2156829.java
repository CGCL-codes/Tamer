package intel.management.wsman;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

/**
 * Verifies certificate trust for TLS connections
 *
 * <P>
 *
 * The DefaultTrustManager is primarily intended for testing and application development.
 * This trust manager <b><u>trusts any</u></b> certificate returned during a TLS connection.
 * In a secure deployment, an application would implement its own trust manager that
 * only trusts specific certificates found in its own certificate store.
 *
 *
 * @see #getAcceptedIssuers()
 * @see #verify(String,SSLSession)
 *
 */
public class DefaultTrustManager implements X509TrustManager, HostnameVerifier {

    /**
     * Returns an array of certificate authorities to trust.
     *
     *
     *
     * @return
     * The list of certificate issuers or authorities to trust.
     *
     */
    public X509Certificate[] getAcceptedIssuers() {
        X509Certificate[] certs = {};
        return certs;
    }

    /**
     * Returns <code>true</code> if a client certificate is trusted.
     *
     *
     * @param certs The certificates sent by the client
     * @param authType The type of authorization requested
     *
     *
     *
     */
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    /**
     * Returns <code>true</code> if a server certificate is trusted.
     *
     * @param certs The certificates received from the service
     * @param authType The type of authorization requested
     *
     *
     *
     */
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }

    /**
     * Returns <code>true</code> if the host name is trusted for the SSLSession.
     *
     *
     * @param hostname The name of the host
     * @param session The SSLSession being used
     *
     * @return
     * <code>true</code> if the host is trusted for SSLSession
     */
    public boolean verify(String hostname, SSLSession session) {
        System.out.println(session.toString());
        System.out.println(WsmanUtils.getBase64String(session.getId()));
        return true;
    }
}
