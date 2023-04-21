package net.grinder.util;

/**
 * Parse URI for interesting things, such as name-value pairs.
 *
 * @author Philip Aston
 * @version $Revision: 3762 $
 */
public interface URIParser {

    /**
   * Parse a URI.
   *
   * @param uri
   *          The URI.
   * @param listener
   *          Callback interface.
   */
    void parse(String uri, ParseListener listener);

    /**
   * Callback interface for use with {@link URIParser#parse}.
   *
   * <p>
   * Methods are called with the maximum amount of text possible. This means
   * that {@link #fragment} will only be called once for a parse.
   * </p>
   *
   * <p>
   * All of the strings that are passed to the methods are unescaped.
   * </p>
   *
   * <p>
   * Each of the methods returns a <code>boolean</code>, which should be
   * interpreted as "continue parse". That is, if a method returns
   * <code>false</code> the parse is terminated.
   * </p>
   */
    interface ParseListener {

        /**
     * Called with the scheme for absolute URIs.
     *
     * @param scheme The scheme, without ':'.
     * @return See {ParseListener}.
     */
        boolean scheme(String scheme);

        /**
     * Called with the authority.
     *
     * @param authority The authority, without leading '//'.
     * @return See {ParseListener}.
     */
        boolean authority(String authority);

        /**
     * Called with path text. Called multiple times with everything in a path
     * (net_path, abs_path, rel_path from RFC 2396), except name-value tokens
     * found in path parameters.
     *
     * @param path
     *          The text. Includes any delimiting punctuation.
     * @return See {ParseListener}.
     */
        boolean path(String path);

        /**
     * Called with a name-value pair that was found in the path.
     *
     * @param name The name. URL decoded.
     * @param value The value. URL decoded.
     * @return See {ParseListener}.
     */
        boolean pathParameterNameValue(String name, String value);

        /**
     * Called with query string text.
     *
     * @param queryString
     *          The text. The leading '?' is stripped.
     * @return See {ParseListener}.
     */
        boolean queryString(String queryString);

        /**
     * Called with a name-value pair that was found in the query string.
     *
     * @param name The name. URL decoded.
     * @param value The value. URL decoded.
     * @return See {ParseListener}.
     */
        boolean queryStringNameValue(String name, String value);

        /**
     * Called with fragment.
     *
     * @param fragment The fragment. The leading '#' is stripped.
     * @return See {ParseListener}.
     */
        boolean fragment(String fragment);
    }

    /**
   * Null implementation of {@link ParseListener}.
   */
    abstract class AbstractParseListener implements ParseListener {

        public boolean scheme(String scheme) {
            return true;
        }

        public boolean authority(String authority) {
            return true;
        }

        public boolean path(String path) {
            return true;
        }

        public boolean pathParameterNameValue(String name, String value) {
            return true;
        }

        public boolean queryString(String queryString) {
            return true;
        }

        public boolean queryStringNameValue(String name, String value) {
            return true;
        }

        public boolean fragment(String fragment) {
            return true;
        }
    }
}
