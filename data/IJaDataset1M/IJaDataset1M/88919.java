package org.rg.scanner.loaders;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * All we do is normalize urls here.
 * @author redman
 */
public class UrlNormalizer {

    /** logger */
    private static final Log log = LogFactory.getLog(UrlNormalizer.class);

    /** turn this on to see how urls are normalized. */
    static final boolean test = false;

    /**
    * normalize a url, convert single quotes and illegal ascii, and make sure
    * the port number is specified.
    * @param url the url to normalize.
    * @return the normalized url.
    * @throws MalformedURLException problem with the url semantics.
    * @throws URISyntaxException problem with the url syntax.
    */
    public static URL normalize(URL url) throws MalformedURLException, URISyntaxException {
        if (url.getProtocol().equals("file")) {
            url = new URL(url.getProtocol(), "localhost", 24, url.getFile());
        }
        String path = url.getFile();
        int idx = path.indexOf("#");
        if (idx >= 0) {
            path = path.substring(0, idx);
        }
        StringBuffer tmp = new StringBuffer(path.length() + 50);
        boolean inslashes = false;
        for (int ci = 0; ci < path.length(); ci++) {
            char c = path.charAt(ci);
            switch(c) {
                case '/':
                    if (!inslashes) {
                        tmp.append(c);
                        inslashes = true;
                    }
                    break;
                case '\'':
                    tmp.append("%27");
                    inslashes = false;
                    break;
                case ' ':
                    tmp.append("%20");
                    inslashes = false;
                    break;
                default:
                    tmp.append(c);
                    inslashes = false;
                    break;
            }
        }
        if (path.length() == 0) path = "/"; else path = tmp.toString();
        String protocol = url.getProtocol();
        int port = url.getPort();
        if (port == -1) {
            if (protocol.equals("https")) port = 443; else port = 80;
        }
        URL nurl = new URL(url.getProtocol(), url.getHost(), port, path);
        if (test && !nurl.toString().equals(url.toString())) {
            log.info("\n" + "     orig:" + url.toString() + "\n" + "    fixed+" + nurl.toString());
        }
        return nurl.toURI().toURL();
    }

    /**
    * normalize a url, convert single quotes and illegal ascii, and make sure
    * the port number is specified.
    * @param url the string to normalize.
    * @return the normalized url.
    * @throws MalformedURLException problem with the url semantics.
    * @throws URISyntaxException problem with the url syntax.
    */
    public static URL normalize(String url) throws MalformedURLException, URISyntaxException {
        return UrlNormalizer.normalize(new URL(url));
    }

    /**
    * format a list of urls
    * @param urlList an array of urls to normalize
    * @return formatted urls or null if nothing to return     
    * @throws MalformedURLException if any url is bad.
    * @throws URISyntaxException if any URI is bad
    */
    public static URL[] normalize(URL[] urlList) throws MalformedURLException, URISyntaxException {
        if (urlList != null && urlList.length > 0) {
            log.debug("Formatting top level urls");
            for (int i = 0; i < urlList.length; i++) {
                urlList[i] = normalize(urlList[i]);
            }
            log.debug("Done formatting top level urls");
            return urlList;
        } else return null;
    }

    /**
    * Returns the normalized URL for a given file.
    * @param f the file.
    * @return the URL object for the file.
    * @throws MalformedURLException if the URL cannot be determined.
    * TODO reconcile with the normalize() method above.
    */
    public static final URL getFileUrl(final File f) throws MalformedURLException {
        URL url = f.toURI().toURL();
        if (url.getHost() == null || url.getHost().length() == 0) {
            String hostName = "localhost";
            if (url.getRef() == null) url = new URL(url.getProtocol(), hostName, 80, url.getFile()); else url = new URL(url.getProtocol(), hostName, 80, url.getFile() + "#" + url.getRef());
        }
        return url;
    }

    /**
    * Returns the normalized URL for a given file.
    * @param URL a (not yet normalized) URL for the file. 
    * @return the URL object for the file.
    * @throws MalformedURLException if the URL cannot be determined.
    * TODO reconcile with the normalize() method above.
    */
    public static final URL getFileUrl(URL url) throws MalformedURLException {
        if (url.getHost() == null || url.getHost().length() == 0) {
            String hostName = "localhost";
            if (url.getRef() == null) url = new URL(url.getProtocol(), hostName, 80, url.getFile()); else url = new URL(url.getProtocol(), hostName, 80, url.getFile() + "#" + url.getRef());
        }
        return url;
    }
}
