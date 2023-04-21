package net.sourceforge.urlbuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A utility that constructs <code>URL</code>s. This class makes no guarantee
 * for synchronisation.
 *
 * @author Daniel Kuan
 * @version
 */
public class URLBuilder {

    private static final char AND = '&';

    private static final char COLON = ':';

    private static final char EQUAL = '=';

    private static final int DEFAULT_PORT = -1;

    private String scheme;

    private String userName;

    private String userPassword;

    private String host;

    private int port;

    private String path;

    private final Map<String, String> queryParameters;

    private String fragment;

    /**
   * Constructs an <code>URLBuilder</code> object.
   */
    public URLBuilder() {
        this(null, null, null);
    }

    /**
   * Constructs an <code>URLBuilder</code> with the specified <code>URL</code>.
   *
   * @param url
   */
    public URLBuilder(final URL url) {
        this(url.getProtocol(), url.getHost(), url.getPath());
        parseUserInfo(url.getUserInfo());
        port = url.getPort();
        parseQueryParameters(url.getQuery());
        fragment = url.getRef();
    }

    /**
   * Constructs an <code>URLBuilder</code> with the specified protocol, host and
   * file.
   *
   * @param protocol the name of the protocol to use.
   * @param host the name of the host.
   * @param file the file on the host.
   */
    public URLBuilder(final String protocol, final String host, final String file) {
        scheme = protocol;
        this.host = host;
        port = DEFAULT_PORT;
        path = file;
        queryParameters = new LinkedHashMap<String, String>();
    }

    /**
   * Parses <code>url</code> as an URL.
   *
   * @param url the <code>String</code> to parse as an <code>URL</code>.
   * @throws MalformedURLException if <code>url</code> specifies an unknown
   *           protocol.
   */
    public void parseURL(final String url) throws MalformedURLException {
        URL parsedUrl = new URL(url);
        scheme = parsedUrl.getProtocol();
        parseUserInfo(parsedUrl.getUserInfo());
        host = parsedUrl.getHost();
        port = parsedUrl.getPort();
        path = parsedUrl.getPath();
        parseQueryParameters(parsedUrl.getQuery());
        fragment = parsedUrl.getRef();
    }

    private void parseUserInfo(final String userInfo) {
        if (userInfo == null) {
            userName = null;
            userPassword = null;
        } else {
            int colonIndex = userInfo.indexOf(COLON, 0);
            if (colonIndex >= 0) {
                userName = userInfo.substring(0, colonIndex);
                userPassword = userInfo.substring(colonIndex + 1);
            } else {
                userName = userInfo;
                userPassword = null;
            }
        }
    }

    /**
   * Builds an <code>URL</code>.
   *
   * @return an <code>URL</code>.
   * @throws MalformedURLException if a protocol handler for the
   *           <code>URL</code> could not be found, or if some other error
   *           occurred while constructing the <code>URL</code>.
   * @throws URISyntaxException if both a scheme and a path are given but the
   *           path is relative, if the <code>URI</code> string constructed from
   *           the given components violates RFC 2396, or if the authority
   *           component of the string is present but cannot be parsed as a
   *           server-based authority.
   * @throws IllegalArgumentException if the <code>URL</code> to be formed is
   *           not absolute.
   */
    public URL buildURL() throws MalformedURLException, URISyntaxException, IllegalArgumentException {
        String userInfo = null;
        if (userName != null) {
            StringBuilder userInfoBuilder = new StringBuilder(userName);
            if (userPassword != null) {
                userInfoBuilder.append(COLON).append(userPassword);
            }
            userInfo = userInfoBuilder.toString();
        }
        String query = getQuery();
        URI uri = new URI(scheme, userInfo, host, port, path, query, fragment);
        return uri.toURL();
    }

    /**
   * Returns the query.
   *
   * @return Returns the query, or <code>null</code> if it does not exist.
   */
    public String getQuery() {
        String query = null;
        if (!queryParameters.isEmpty()) {
            StringBuilder queryBuilder = new StringBuilder();
            for (Entry<String, String> parameter : queryParameters.entrySet()) {
                queryBuilder.append(parameter.getKey());
                String value = parameter.getValue();
                if ((value != null) && !value.isEmpty()) {
                    queryBuilder.append(EQUAL).append(value);
                }
                queryBuilder.append(AND);
            }
            query = queryBuilder.deleteCharAt(queryBuilder.length() - 1).toString();
        }
        return query;
    }

    private void parseQueryParameters(final String query) {
        queryParameters.clear();
        if (query != null) {
            for (int parameterStart = 0, length = query.length(); parameterStart < length; ) {
                String name = null;
                String value = null;
                int indexOfAnd = query.indexOf(AND, parameterStart);
                int indexOfEqual = query.indexOf(EQUAL, parameterStart);
                if (indexOfAnd >= 0) {
                    if ((indexOfEqual >= 0) && (indexOfEqual < indexOfAnd)) {
                        name = query.substring(parameterStart, indexOfEqual);
                        value = query.substring(indexOfEqual + 1, indexOfAnd);
                    } else {
                        name = query.substring(parameterStart, indexOfAnd);
                    }
                    parameterStart = indexOfAnd + 1;
                } else {
                    if (indexOfEqual >= 0) {
                        name = query.substring(parameterStart, indexOfEqual);
                        value = query.substring(indexOfEqual + 1);
                    } else {
                        name = query.substring(parameterStart);
                    }
                    parameterStart = length;
                }
                queryParameters.put(name, value);
            }
        }
    }

    /**
   * Adds a parameter to the query with the given <code>name</code> and
   * <code>value</code>. If the parameter already exists, the old value is
   * replaced by the specified value.
   *
   * @param name of the parameter.
   * @param value of the parameter.
   * @return the previous value associated with <code>name</code>, or
   *         <code>null</code> if the parameter does not exist.
   * @see {@link Map#put(K, V)}
   * @throws IllegalArgumentException if <code>name</code> is null or empty.
   */
    public String addQueryParameter(final String name, final String value) {
        if ((name == null) || name.isEmpty()) {
            throw new IllegalArgumentException("query name cannot be null or empty");
        }
        return queryParameters.put(name, value);
    }

    /**
   * Removes an existing parameter in the query with the given
   * <code>name</code>.
   *
   * @param name of the parameter.
   * @return the previous value associated with <tt>name</tt>, or
   *         <tt>null</tt> if there was no mapping for <tt>name</tt>.
   * @see {@link Map#remove(Object)}
   */
    public String removeQueryParameter(final String name) {
        return queryParameters.remove(name);
    }

    /**
   * Removes all existing parameters in the query.
   *
   * @see {@link Map#clear()}
   */
    public void removeAllQueryParameters() {
        queryParameters.clear();
    }

    public String getProtocol() {
        return scheme;
    }

    public void setProtocol(final String protocol) {
        scheme = protocol;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String name) {
        userName = name;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(final String password) {
        userPassword = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(final int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String file) {
        path = file;
    }

    public String getFragment() {
        return fragment;
    }

    public void setFragment(final String anchor) {
        fragment = anchor;
    }
}
