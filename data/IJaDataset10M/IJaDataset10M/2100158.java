package org.apache.http.nio.protocol;

import java.util.Map;
import org.apache.http.protocol.UriPatternMatcher;

/**
 * Maintains a map of HTTP request handlers keyed by a request URI pattern.
 * <br>
 * Patterns may have three formats:
 * <ul>
 *   <li><code>*</code></li>
 *   <li><code>*&lt;uri&gt;</code></li>
 *   <li><code>&lt;uri&gt;*</code></li>
 * </ul>
 * <br>
 * This class can be used to resolve an instance of 
 * {@link NHttpRequestHandler} matching a particular request URI. Usually the 
 * resolved request handler will be used to process the request with the 
 * specified request URI.
 *
 *
 * @version $Revision: 744543 $
 *
 * @since 4.0
 */
public class NHttpRequestHandlerRegistry implements NHttpRequestHandlerResolver {

    private final UriPatternMatcher matcher;

    public NHttpRequestHandlerRegistry() {
        matcher = new UriPatternMatcher();
    }

    /**
     * Registers the given {@link NHttpRequestHandler} as a handler for URIs
     * matching the given pattern.
     * 
     * @param pattern the pattern to register the handler for.
     * @param handler the handler.
     */
    public void register(final String pattern, final NHttpRequestHandler handler) {
        matcher.register(pattern, handler);
    }

    /**
     * Removes registered handler, if exists, for the given pattern.
     *  
     * @param pattern the pattern to unregister the handler for.
     */
    public void unregister(final String pattern) {
        matcher.unregister(pattern);
    }

    /**
     * Sets handlers from the given map.
     * @param map the map containing handlers keyed by their URI patterns.
     */
    public void setHandlers(final Map<String, ? extends NHttpRequestHandler> map) {
        matcher.setHandlers(map);
    }

    public NHttpRequestHandler lookup(String requestURI) {
        return (NHttpRequestHandler) matcher.lookup(requestURI);
    }
}
