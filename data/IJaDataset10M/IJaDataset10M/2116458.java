package org.w3c.tidy.servlet.sample;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.InetAddress;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 */
public class SiteSecurityHelper {

    /**
     * Logger.
     */
    private static Log log = LogFactory.getLog(SiteSecurityHelper.class);

    private static final String RESOURCE_SECURE_FLAG = "org.w3c.tidy.servlet.sample.secure";

    public static boolean isSiteSecure() {
        if ((SiteSecurityHelper.class.getClassLoader().getResourceAsStream(RESOURCE_SECURE_FLAG)) == null) {
            log.debug("Security disabled flag no found in classpath:" + RESOURCE_SECURE_FLAG);
            return false;
        }
        return true;
    }

    public static void verifyUrl(String paramUrl, HttpServletRequest request) throws MalformedURLException, IOException {
        log.info("URL [" + paramUrl + "] for " + request.getRemoteAddr());
        if (log.isDebugEnabled()) {
            log.debug("Verify URL:" + paramUrl);
            log.debug("http.proxyHost:" + System.getProperty("http.proxyHost"));
            log.debug("http.proxyPort:" + System.getProperty("http.proxyPort"));
        }
        URL url = new URL(paramUrl);
        log.debug("host:" + url.getHost());
        if ((url.getHost() == null) || (url.getHost().length() == 0)) {
            throw new MalformedURLException("Host must not be null; URL [" + paramUrl + "]");
        }
        if (!isSiteSecure()) {
            return;
        }
        String protocol = url.getProtocol();
        if (!(protocol.equalsIgnoreCase("http") || protocol.equalsIgnoreCase("https"))) {
            throw new IOException("Permission denied for Protocol [" + protocol + "]; URL [" + paramUrl + "]");
        }
        InetAddress remoteIP = InetAddress.getByName(url.getHost());
        InetAddress localIP = InetAddress.getLocalHost();
        if (log.isDebugEnabled()) {
            log.debug("Local IP:" + localIP.getHostAddress());
            log.debug("Remote IP is Loopback" + remoteIP.isLoopbackAddress());
            log.debug("Remote IP is SiteLocal" + remoteIP.isSiteLocalAddress());
            log.debug("Remote IP is AnyLocal" + remoteIP.isAnyLocalAddress());
        }
        if (remoteIP.equals(localIP) || remoteIP.isLoopbackAddress() || remoteIP.isSiteLocalAddress() || remoteIP.isAnyLocalAddress()) {
            throw new IOException("Permission denied to access  " + remoteIP.getHostAddress() + "; URL [" + paramUrl + "]");
        }
        InetAddress[] allLocalIP = InetAddress.getAllByName("localhost");
        for (int i = 0; i < allLocalIP.length; i++) {
            if (log.isDebugEnabled()) {
                log.debug("Local IP:" + allLocalIP[i].getHostAddress());
            }
            if (remoteIP.equals(allLocalIP[i])) {
                throw new IOException("Permission denied to access  " + remoteIP.getHostAddress() + "; URL [" + paramUrl + "]");
            }
        }
    }

    public static String getApplicationURL(HttpServletRequest request) {
        String url = request.getHeader("Referer");
        if (url == null) {
            return "";
        }
        try {
            URL u = new URL(url);
            url = u.getProtocol() + "://" + u.getHost();
            int port = u.getPort();
            if (!((port == -1) || (port == 80))) {
                url = url + ":" + port;
            }
        } catch (MalformedURLException e) {
            log.debug("Malformed URL", e);
            url = "";
        }
        return url;
    }
}
