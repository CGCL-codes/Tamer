package net.sf.karatasi;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.io.UnsupportedEncodingException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A RequestURI wraps the URI of a HTTP Request and allows easy access to its significant fields.
 *
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 * @see URI
 */
public class RequestURI {

    /** The URI. */
    private final URI uri;

    /** The query parameters. */
    private final Map<String, String> queryParameters;

    /** Create a RequestURI.
     * @param requestUriString String with the requestURI directly from the HTTP Request.
     * @throws URISyntaxException in case the requestUriString is malformed.
     */
    public RequestURI(@NotNull final String requestUriString) throws URISyntaxException {
        this.uri = new URI(requestUriString);
        queryParameters = getQueryParameters(uri);
    }

    /** Returns a map of the query parameters.
     * @param uri URI for which the query parameters shall be returned.
     * @return Map with the query parameters (empty map if the uri did not contain any query parameters).
     * @throws URISyntaxException in case decoding the parameters as UTF-8 failed.
     */
    public static Map<String, String> getQueryParameters(@NotNull final URI uri) throws URISyntaxException {
        try {
            final Map<String, String> queryParameters = new HashMap<String, String>();
            if (uri.getRawQuery() != null) {
                for (final String queryPart : uri.getRawQuery().split("\\&")) {
                    final String[] keyValue = queryPart.split("=");
                    queryParameters.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
                }
            }
            return queryParameters;
        } catch (final UnsupportedEncodingException e) {
            throw new URISyntaxException(uri.getRawQuery(), e.toString());
        }
    }

    /** Returns the path of this RequestURI.
     * @return The path of this RequestURI.
     */
    public String getPath() {
        return uri.getPath();
    }

    /** Returns the fragment of this RequestURI.
     * @return The fragment of this RequestURI.
     */
    public String getFragment() {
        return uri.getFragment();
    }

    /** Returns the map of query parameters.
     * @return The map of query parameters (unmodifiable).
     */
    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    /** Stores or overrides a query parameter.
     * @param key Query parameter key.
     * @param value Query parameter value.
     * @return Value of the previous mapping if any, or <code>null</code>.
     * @see Map#put(Object, Object)
     */
    @Nullable
    public String put(@NotNull final String key, final String value) {
        return queryParameters.put(key, value);
    }

    /** Retrieves a query parameter.
     * @param key Query parameter key.
     * @return Value mapped to key or <code>null</code> if there is no such mapping.
     * @see Map#get(Object)
     */
    @Nullable
    public String get(final String key) {
        return queryParameters.get(key);
    }
}
