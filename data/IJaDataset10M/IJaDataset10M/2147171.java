package net.sourceforge.ssl;

import javax.net.ssl.SSLSession;

/**
 * @author Matthias Balke
 *
 */
public class HostnameVerifier implements javax.net.ssl.HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession sslSession) {
        return true;
    }
}
