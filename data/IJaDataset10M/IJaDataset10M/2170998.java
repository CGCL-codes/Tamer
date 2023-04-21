package aurora.plugin.bill99.pos;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

public class SavingTrustManager implements X509TrustManager {

    private final X509TrustManager tm;

    private java.security.cert.X509Certificate[] chain;

    SavingTrustManager(X509TrustManager tm) {
        this.tm = tm;
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
