package sunlabs.brazil.util;

import sunlabs.brazil.util.regexp.Regexp;
import sunlabs.brazil.util.Glob;
import sunlabs.brazil.util.Format;
import java.util.Properties;

/**
 * Utility class for handlers to determine, based on the URL,
 * if the current request should be processed.
 * <p>
 * Properties:
 * <dl class=props>
 * <dt>prefix
 * <dd>The url prefix the url must match (defaults to "/").
 * <dt>suffix
 * <dd>The url suffix the url must match (defaults to "").
 * <dt>glob
 * <dd>The glob pattern the url must match. If defined, this
 * overrides both <code>prefix</code> and <code>suffix</code>.
 * <dt>match
 * <dd>The reqular expression pattern the url must match.  If defined, 
 * this overrides <code>glob</code>.
 * <dt>ignoreCase
 * <dd>If present and <code>match</code> is defined, this causes the
 * regular expression match to be case insensitive. By default, case counts.
 * <dt>invert
 * <dd>If true, the sense of the comparison is reversed
 * </dl>
 */
public class MatchString {

    String propsPrefix;

    Regexp re = null;

    String glob = null;

    boolean invert = false;

    static final String PREFIX = "prefix";

    static final String SUFFIX = "suffix";

    static final String MATCH = "match";

    static final String GLOB = "glob";

    static final String CASE = "ignoreCase";

    static final String INVERT = "invert";

    /**
     * Create a matcher for per-request URL checking.
     * This constructer is used if the
     * properties are to be evaluated on each request.
     *
     * @param propsPrefix	The prefix to use in the properties object.
     */
    public MatchString(String propsPrefix) {
        this.propsPrefix = propsPrefix;
    }

    /**
     * Create a matcher for one-time-only checking. 
     * This constructor is used if the
     * properties are to be computed only once, at "init" time.
     *
     * @param propsPrefix	The prefix to use in the properties object.
     * @param props	The table to find the properties in.
     */
    public MatchString(String propsPrefix, Properties props) {
        this.propsPrefix = propsPrefix;
        setup(props);
    }

    /**
     * Extract and setup the properties
     */
    private void setup(Properties props) {
        invert = Format.isTrue(props.getProperty(propsPrefix + INVERT));
        String exp = props.getProperty(propsPrefix + MATCH);
        if (exp != null) {
            boolean ignoreCase = (props.getProperty(propsPrefix + CASE) != null);
            try {
                re = new Regexp(exp, ignoreCase);
            } catch (Exception e) {
            }
        }
        if (re == null) {
            glob = props.getProperty(propsPrefix + GLOB, props.getProperty(propsPrefix + PREFIX, "/") + "*" + props.getProperty(propsPrefix + SUFFIX, ""));
        }
    }

    /**
     * See if this is our url.  Use this version for properties
     * evaluated only at init time.
     */
    public boolean match(String url) {
        if (re != null) {
            return (invert ^ (re.match(url) != null));
        } else if (glob != null) {
            return invert ^ Glob.match(glob, url);
        } else {
            throw new IllegalArgumentException("no properties provided");
        }
    }

    /**
     * See if this is our url.  Use this version for properties
     * evaluated at each request.
     */
    public boolean match(String url, Properties props) {
        setup(props);
        return match(url);
    }

    /**
     * Return our prefix
     */
    public String prefix() {
        return propsPrefix;
    }

    /**
     * print nicely
     */
    public String toString() {
        return (propsPrefix + ":" + glob + ", " + re);
    }
}
