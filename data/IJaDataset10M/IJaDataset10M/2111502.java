package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.lang.StringUtils;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * The JavaScript object that represents an anchor.
 *
 * @version $Revision: 2829 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gousseff@netscape.net">Alexei Goussev</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLAnchorElement extends HTMLElement {

    private static final long serialVersionUID = -816365374422492967L;

    /**
     * Create an instance.
     */
    public HTMLAnchorElement() {
    }

    /**
     * JavaScript constructor. This must be declared in every JavaScript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Sets the href property.
     * @param href href attribute value
     */
    public void jsxSet_href(final String href) {
        getHtmlElementOrDie().setAttributeValue("href", href);
    }

    /**
     * Returns the value of the href property of this link.
     * @return the href property
     * @throws Exception if an error occurs
     */
    public String jsxGet_href() throws Exception {
        try {
            return getUrl().toString();
        } catch (final MalformedURLException e) {
            return ((HtmlAnchor) getHtmlElementOrDie()).getHrefAttribute();
        }
    }

    /**
     * Sets the name property.
     * @param name name attribute value
     */
    public void jsxSet_name(final String name) {
        getHtmlElementOrDie().setAttributeValue("name", name);
    }

    /**
     * Returns the value of the name property of this link.
     * @return the name property
     * @throws Exception if an error occurs
     */
    public String jsxGet_name() throws Exception {
        return getHtmlElementOrDie().getAttributeValue("name");
    }

    /**
     * Sets the target property of this link.
     * @param target target attribute value
     */
    public void jsxSet_target(final String target) {
        getHtmlElementOrDie().setAttributeValue("target", target);
    }

    /**
     * Returns the value of the target property of this link.
     * @return the href property
     */
    public String jsxGet_target() {
        return getHtmlElementOrDie().getAttributeValue("target");
    }

    /**
     * Returns this link's current URL.
     * @return this link's current URL
     * @throws Exception if an error occurs
     */
    private URL getUrl() throws Exception {
        final HtmlAnchor anchor = (HtmlAnchor) getHtmlElementOrDie();
        return anchor.getPage().getFullyQualifiedUrl(anchor.getHrefAttribute());
    }

    /**
     * Sets the href attribute of this link to the specified URL.
     */
    private void setUrl(final URL url) {
        getHtmlElementOrDie().setAttributeValue("href", url.toString());
    }

    /**
     * Returns the search portion of the link's URL (the portion starting with
     * '?' and up to but not including any '#').
     * @return the search portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/search.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_search() throws Exception {
        final String query = getUrl().getQuery();
        if (query == null) {
            return "";
        } else {
            return "?" + query;
        }
    }

    /**
     * Sets the search portion of the link's URL (the portion starting with '?'
     * and up to but not including any '#')..
     * @param search the new search portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/search.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_search(final String search) throws Exception {
        final String query;
        if (search == null || "?".equals(search) || "".equals(search)) {
            query = null;
        } else if (search.charAt(0) == '?') {
            query = search.substring(1);
        } else {
            query = search;
        }
        setUrl(UrlUtils.getUrlWithNewQuery(getUrl(), query));
    }

    /**
     * Returns the hash portion of the link's URL (the portion following the '#').
     * @return the hash portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/hash.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_hash() throws Exception {
        final String hash = getUrl().getRef();
        if (hash == null) {
            return "";
        } else {
            return hash;
        }
    }

    /**
     * Sets the hash portion of the link's URL (the portion following the '#').
     * @param hash the new hash portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/hash.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_hash(final String hash) throws Exception {
        setUrl(UrlUtils.getUrlWithNewRef(getUrl(), hash));
    }

    /**
     * Returns the host portion of the link's URL (the '[hostname]:[port]' portion).
     * @return the host portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_host() throws Exception {
        final URL url = getUrl();
        final int port = url.getPort();
        final String host = url.getHost();
        if (port == -1) {
            return host;
        } else {
            return host + ":" + port;
        }
    }

    /**
     * Sets the host portion of the link's URL (the '[hostname]:[port]' portion).
     * @param host the new host portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_host(final String host) throws Exception {
        final String hostname;
        final int port;
        final int index = host.indexOf(':');
        if (index != -1) {
            hostname = host.substring(0, index);
            port = Integer.parseInt(host.substring(index + 1));
        } else {
            hostname = host;
            port = -1;
        }
        final URL url1 = UrlUtils.getUrlWithNewHost(getUrl(), hostname);
        final URL url2 = UrlUtils.getUrlWithNewPort(url1, port);
        setUrl(url2);
    }

    /**
     * Returns the hostname portion of the link's URL.
     * @return the hostname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_hostname() throws Exception {
        return getUrl().getHost();
    }

    /**
     * Sets the hostname portion of the link's URL.
     * @param hostname the new hostname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_hostname(final String hostname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
    }

    /**
     * Returns the pathname portion of the link's URL.
     * @return the pathname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/pathname.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_pathname() throws Exception {
        return getUrl().getPath();
    }

    /**
     * Sets the pathname portion of the link's URL.
     * @param pathname the new pathname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/pathname.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_pathname(final String pathname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPath(getUrl(), pathname));
    }

    /**
     * Returns the port portion of the link's URL.
     * @return the port portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/port.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_port() throws Exception {
        final int port = getUrl().getPort();
        if (port == -1) {
            return "";
        } else {
            return String.valueOf(port);
        }
    }

    /**
     * Sets the port portion of the link's URL.
     * @param port the new port portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/port.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_port(final String port) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPort(getUrl(), Integer.parseInt(port)));
    }

    /**
     * Returns the protocol portion of the link's URL, including the trailing ':'.
     * @return the protocol portion of the link's URL, including the trailing ':'
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/protocol.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_protocol() throws Exception {
        return getUrl().getProtocol() + ":";
    }

    /**
     * Sets the protocol portion of the link's URL.
     * @param protocol the new protocol portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/protocol.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_protocol(final String protocol) throws Exception {
        final String bareProtocol = StringUtils.substringBefore(protocol, ":");
        setUrl(UrlUtils.getUrlWithNewProtocol(getUrl(), bareProtocol));
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object getDefaultValue(final Class hint) {
        final HtmlAnchor link = (HtmlAnchor) getHtmlElementOrDie();
        final String href = link.getHrefAttribute();
        final String response;
        if (href == HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            response = "";
        } else {
            final int indexAnchor = href.indexOf('#');
            final String beforeAnchor;
            final String anchorPart;
            if (indexAnchor == -1) {
                beforeAnchor = href;
                anchorPart = "";
            } else {
                beforeAnchor = href.substring(0, indexAnchor);
                anchorPart = href.substring(indexAnchor);
            }
            try {
                response = link.getPage().getFullyQualifiedUrl(beforeAnchor).toExternalForm() + anchorPart;
            } catch (final MalformedURLException e) {
                return href;
            }
        }
        return response;
    }
}
