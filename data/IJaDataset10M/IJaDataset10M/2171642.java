package com.jme.util.resource;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * A conservative ResourceLocator implementation that only resolves resources
 * with absolute resource name paths.
 * It may be used in two ways <UL>
 *   <LI>Specify no base URI, and requested resource names must be in the form
 *       of absolute URL strings.
 *   <LI>Specify a base URI, and requested resources names may be of the form
 *       above (which must match or be a sub-URL of the base URI), or be an
 *       absolute path like "/a/b/c" the beginning of which must match the
 *       path portion of the base URI.
 * </UL>
 * Either way, lookups succeed only for resources requested with absolute
 * paths, and there is no recursion.
 * <P>
 * This ResourceLocator is specifically NOT for loading resources from the
 * Classpath.  Use *Classpath*ResourceLocator class(es) for that purpose.
 * </P>
 *
 * @see com.jme.util.resource.ResourceLocator
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 */
public class AbsoluteResourceLocator implements ResourceLocator {

    private static final Logger logger = Logger.getLogger(AbsoluteResourceLocator.class.getName());

    private Pattern baseUriPattern;

    private URL pathlessBaseUrl;

    /**
     * Instantiate a locator for any resource present at the specified
     * absolute resource path.
     */
    public AbsoluteResourceLocator() {
        try {
            pathlessBaseUrl = new URL("file:");
        } catch (MalformedURLException mue) {
            throw new RuntimeException(mue);
        }
    }

    /**
     * Instantiate a locator for resources residing underneath the specified
     * base URI, and requested with absolute path.
     * <P>
     * To restrict to any absolute paths within a URI base, use a URI path of
     * just "/", like <CODE>new URI("http://acme.com/")</CODE> or
     * <CODE>new File("/").toURI()</CODE>.
     * </P> <P>
     *   A rather non-intuitive aspect of Java file URIs, is that a
     *   trailing "/" will be truncated unless the item is present and is a
     *   directory...
     *   And also that resolution of URL paths ending with /, like
     *   "/home/blaine/info.txt/" will succeed and can read the file.
     * </P>
     *
     * @param baseURI <B>IMPORTANT</B>:  The whole purpose here is to specify
     *        a path with an absolute path to validate against.
     *        Therefore, to cover an entire web site, you must use
     *        <CODE>http://pub.admc.com/</CODE>, not
     *        <CODE>http://pub.admc.com</CODE>.
     * @throws IllegalArgumentException if the specified baseUri does not have
     *         an absolute URI path, or is otherwise unacceptable.
     */
    public AbsoluteResourceLocator(URI baseUri) {
        String basePath = baseUri.getPath();
        if (basePath == null || basePath.length() < 1) throw new IllegalArgumentException("Specified URI has no path: " + baseUri);
        try {
            URL tmpUrl = baseUri.toURL();
            pathlessBaseUrl = new URL(tmpUrl.getProtocol(), tmpUrl.getHost(), tmpUrl.getPort(), "");
        } catch (MalformedURLException mue) {
            throw new IllegalArgumentException(mue);
        }
        String matchString = basePath.matches(".*[/\\\\]") ? ("\\Q" + basePath + "\\E.*") : ("\\Q" + basePath + "\\E(?:[/\\\\].*)?");
        logger.fine("URL path-matching pattern for AbsoluteResourceLocator instance: '" + matchString + "'");
        baseUriPattern = Pattern.compile(matchString);
    }

    public URL locateResource(String resourceName) {
        if (resourceName == null) return null;
        String spec = resourceName;
        URL resourceUrl = null;
        try {
            resourceUrl = new URL(resourceName);
        } catch (Exception e) {
            if (pathlessBaseUrl == null) return null;
            try {
                resourceUrl = new URL(pathlessBaseUrl, spec);
            } catch (MalformedURLException mue) {
                logger.warning("Malformatted URL: " + mue);
                return null;
            }
        }
        String resourcePath = resourceUrl.getPath();
        if (resourcePath == null || resourcePath.length() < 1 || (resourcePath.charAt(0) != '/' && resourcePath.charAt(0) != '\\')) return null;
        if (baseUriPattern != null && !baseUriPattern.matcher(resourcePath).matches()) return null;
        try {
            resourceUrl.openConnection().connect();
            return resourceUrl;
        } catch (MalformedURLException mue) {
        } catch (IOException ioe) {
        }
        return null;
    }
}
