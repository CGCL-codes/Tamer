package org.apache.tiles.context;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

/**
 * Encapsulation of request information.
 *
 * @since 2.0
 * @version $Rev: 527536 $ $Date: 2007-04-11 17:44:51 +0200 (Wed, 11 Apr 2007) $
 */
public interface TilesRequestContext {

    /**
     * Return an immutable Map that maps header names to the first (or only)
     * header value (as a String).
     *
     * @return The header map.
     */
    Map<String, String> getHeader();

    /**
     * Return an immutable Map that maps header names to the set of all values
     * specified in the request (as a String array). Header names must be
     * matched in a case-insensitive manner.
     *
     * @return The header values map.
     */
    Map<String, String[]> getHeaderValues();

    /**
     * Return a mutable Map that maps request scope attribute names to their
     * values.
     *
     * @return The request scope map.
     */
    Map<String, Object> getRequestScope();

    /**
     * Return a mutable Map that maps session scope attribute names to their
     * values.
     *
     * @return The request scope map.
     */
    Map<String, Object> getSessionScope();

    /**
     * Dispatches the request to a specified path.
     *
     * @param path The path to dispatch to.
     * @throws IOException If something goes wrong during dispatching.
     */
    void dispatch(String path) throws IOException;

    /**
     * Includes the response from the specified URL in the current response output.
     *
     * @param path The path to include.
     * @throws IOException If something goes wrong during inclusion.
     */
    void include(String path) throws IOException;

    /**
     * Return an immutable Map that maps request parameter names to the first
     * (or only) value (as a String).
     *
     * @return The parameter map.
     */
    Map<String, String> getParam();

    /**
     * Return an immutable Map that maps request parameter names to the set of
     * all values (as a String array).
     *
     * @return The parameter values map.
     */
    Map<String, String[]> getParamValues();

    /**
     * Return the preferred Locale in which the client will accept content.
     *
     * @return The current request locale. It is the locale of the request
     * object itself and it is NOT the locale that the user wants to use. See
     * {@link org.apache.tiles.locale.LocaleResolver} to implement strategies to
     * resolve locales.
     */
    Locale getRequestLocale();

    /**
     * Determine whether or not the specified user is in the given role.
     * @param role the role to check against.
     * @return <code>true</code> if the user is in the given role.
     */
    boolean isUserInRole(String role);

    /**
     * Get the underlying request.
     *
     * @return The current request object.
     */
    Object getRequest();

    /**
     * Get the underlying response.
     *
     * @return The current request object.
     */
    Object getResponse();
}
