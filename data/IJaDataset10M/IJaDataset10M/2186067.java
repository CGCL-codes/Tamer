package com.hyper9.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.hyper9.common.text.Base64;

/**
 * A generic HTTP client used to retrieve the content at a given URL.
 * 
 * @author akutz
 * @remarks This is a modified version of the WSClient written by Steve Jin at
 *          VMware which can be obtained at http://tr.im/kulf.
 */
public final class HttpClient {

    private URL baseUrl = null;

    private String encodedCredentials = null;

    /**
     * Initializes a new instance of the HttpClient class.
     * 
     * @param serverUrl The server URL.
     * @throws MalformedURLException When the URL is not a valid pattern.
     */
    public HttpClient(String serverUrl) throws MalformedURLException {
        this(serverUrl, true);
    }

    /**
     * Initializes a new instance of the HttpClient class.
     * 
     * @param serverUrl The server URL.
     * @param ignoreCert Set to true to ignore the SSL warnings.
     * @throws MalformedURLException When the URL is not a valid pattern.
     */
    public HttpClient(String serverUrl, boolean ignoreCert) throws MalformedURLException {
        this(serverUrl, null, null, ignoreCert);
    }

    /**
     * Initializes a new instance of the HttpClient class.
     * 
     * @param serverUrl The server URL.
     * @param userName The user name used for URL authentication.
     * @param password The password used for URL authentication.
     * @throws MalformedURLException When the URL is not a valid pattern.
     */
    public HttpClient(String serverUrl, String userName, String password) throws MalformedURLException {
        this(serverUrl, userName, password, true);
    }

    /**
     * Initializes a new instance of the HttpClient class.
     * 
     * @param serverUrl The server URL.
     * @param userName The user name used for URL authentication.
     * @param password The password used for URL authentication.
     * @param ignoreCert Set to true to ignore the SSL warnings.
     * @throws MalformedURLException When the URL is not a valid pattern.
     */
    public HttpClient(String serverUrl, String userName, String password, boolean ignoreCert) throws MalformedURLException {
        if (serverUrl.endsWith("/")) {
            serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
        }
        this.baseUrl = new URL(serverUrl);
        if (userName != null && password != null) {
            String up = userName + ":" + password;
            this.encodedCredentials = Base64.to(up);
        }
        if (ignoreCert) {
            try {
                trustAllHttpsCertificates();
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                    public boolean verify(String urlHostName, SSLSession session) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }

    /**
     * Gets the content of the URL.
     * 
     * @return The content of the URL.
     * @throws IOException When an error occurs.
     */
    public String getContent() throws IOException {
        StringBuffer sb = new StringBuffer();
        URLConnection uc = this.baseUrl.openConnection();
        uc.setRequestProperty("Authorization", "Basic " + this.encodedCredentials);
        InputStream is = uc.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String lineStr;
        while ((lineStr = in.readLine()) != null) {
            sb.append(lineStr);
        }
        in.close();
        String sbsz = sb.toString();
        return sbsz;
    }

    private static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = new TrustAllManager();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    private static class TrustAllManager implements X509TrustManager {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }
    }
}
