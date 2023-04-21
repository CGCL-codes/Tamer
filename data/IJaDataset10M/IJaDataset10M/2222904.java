package de.iritgo.aktera.webservices;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HTTPSHackUtil {

    /**
	 * Used in getAllowAllSocketFactory()
	 *
	 *
	 */
    protected static class _FakeX509TrustManager implements X509TrustManager {

        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        public boolean isClientTrusted(@SuppressWarnings("unused") X509Certificate[] chain) {
            return true;
        }

        public boolean isServerTrusted(@SuppressWarnings("unused") X509Certificate[] chain) {
            return true;
        }

        public X509Certificate[] getAcceptedIssuers() {
            return _AcceptedIssuers;
        }
    }

    private static final String TAG = HTTPSHackUtil.class.getSimpleName();

    private static final String ORIGINAL_HTTPS_SCHEME_BACKUP = "originalhttps";

    private static final String HACKED_HTTPS_SCHEME_BACKUP = "hackedhttps";

    private boolean allSSLAllowed = false;

    private boolean allHostnameAllowed = false;

    private TrustManager[] trustManagers;

    private SSLSocketFactory hackedSocketFactory;

    private SSLSocketFactory originalSocketFactory;

    private HostnameVerifier hackedHostnameVerifier;

    private HostnameVerifier originalHostnameVerifier;

    /**
	 * This method allow all hostnames, disabling verifying of hostnames during
	 * handshake
	 */
    public void allowAllHostname() {
        if (!allHostnameAllowed) {
            allHostnameAllowed = true;
            if (hackedHostnameVerifier == null) {
                hackedHostnameVerifier = new AllowAllHostnameVerifier();
            }
            if (originalHostnameVerifier == null) {
                originalHostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            }
            HttpsURLConnection.setDefaultHostnameVerifier(hackedHostnameVerifier);
        }
    }

    /**
	 * Removes the effects of allowAllHostname()
	 */
    public void disableAllowAllHostname() {
        if (allHostnameAllowed) {
            disableAllowAllSSL();
            HttpsURLConnection.setDefaultHostnameVerifier(originalHostnameVerifier);
            allHostnameAllowed = false;
        }
    }

    /**
	 * Allow all SSL certificates for HttpsURLConnection.
	 */
    public void allowAllSSL() {
        if (!allSSLAllowed) {
            allowAllHostname();
            if (originalSocketFactory == null) {
                originalSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
            }
            HttpsURLConnection.setDefaultSSLSocketFactory(getAllowAllSocketFactory());
            allSSLAllowed = true;
        }
    }

    /**
	 * Removes the effects of allowAllSSL()
	 */
    public void disableAllowAllSSL() {
        if (allSSLAllowed) {
            HttpsURLConnection.setDefaultSSLSocketFactory(originalSocketFactory);
            allSSLAllowed = false;
        }
    }

    /**
	 * Allow all SSL certificates for an HttpClient instance. We need to do this
	 * on a per-instance base since I didn't find any way to do this statically.
	 *
	 * @param client
	 */
    public void httpClientAllowAllSSL(HttpClient client) {
        SchemeRegistry registry = client.getConnectionManager().getSchemeRegistry();
        Scheme originalScheme = registry.get("https");
        if (!(originalScheme.getSocketFactory() instanceof AllowingAllSSLSocketFactory)) {
            allowAllSSL();
            Scheme newHackedScheme;
            Scheme hackedScheme = registry.get(HACKED_HTTPS_SCHEME_BACKUP);
            if (hackedScheme == null) {
                AllowingAllSSLSocketFactory socketFactory;
                try {
                    socketFactory = new AllowingAllSSLSocketFactory(getAllowAllSocketFactory());
                } catch (Exception e) {
                    return;
                }
                newHackedScheme = new Scheme("https", socketFactory, originalScheme.getDefaultPort());
            } else {
                newHackedScheme = new Scheme("https", hackedScheme.getSocketFactory(), hackedScheme.getDefaultPort());
                registry.unregister(HACKED_HTTPS_SCHEME_BACKUP);
            }
            Scheme newOriginalScheme = new Scheme(ORIGINAL_HTTPS_SCHEME_BACKUP, originalScheme.getSocketFactory(), originalScheme.getDefaultPort());
            registry.register(newHackedScheme);
            registry.register(newOriginalScheme);
        }
    }

    /**
	 * Removes the effects of httpClientAllowAllSSL()
	 *
	 * @param client
	 */
    public void httpClientDisableAllSSL(HttpClient client) {
        SchemeRegistry registry = client.getConnectionManager().getSchemeRegistry();
        Scheme hackedScheme = registry.get("https");
        if (hackedScheme.getSocketFactory() instanceof AllowingAllSSLSocketFactory) {
            disableAllowAllSSL();
            Scheme originalScheme = registry.get(ORIGINAL_HTTPS_SCHEME_BACKUP);
            Scheme newHackedScheme = new Scheme(HACKED_HTTPS_SCHEME_BACKUP, hackedScheme.getSocketFactory(), hackedScheme.getDefaultPort());
            Scheme newOriginalScheme = new Scheme("https", originalScheme.getSocketFactory(), originalScheme.getDefaultPort());
            registry.unregister(ORIGINAL_HTTPS_SCHEME_BACKUP);
            registry.register(newHackedScheme);
            registry.register(newOriginalScheme);
        }
    }

    protected SSLSocketFactory getAllowAllSocketFactory() {
        if (hackedSocketFactory == null) {
            if (trustManagers == null) {
                trustManagers = new TrustManager[] { new _FakeX509TrustManager() };
            }
            SSLContext context;
            try {
                context = SSLContext.getInstance("TLS");
                context.init(null, trustManagers, new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                return null;
            } catch (KeyManagementException e) {
                return null;
            }
            hackedSocketFactory = context.getSocketFactory();
        }
        return hackedSocketFactory;
    }

    public boolean getAllSSLAllowed() {
        return allSSLAllowed;
    }

    public boolean getAllHostnameAllowed() {
        return allHostnameAllowed;
    }
}
