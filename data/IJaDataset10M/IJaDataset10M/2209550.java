package org.cilogon.d2.util;

import edu.uiuc.ncsa.security.core.util.DoubleHashMap;
import java.io.Serializable;
import java.net.URI;

/**
 * Utility for managing/manipulating serial strings. Note that since this is only for managing serial strings,
 * the prefix resolution should include the entire serial string, e.g. <br><br></br></br>
 * http://cilogon.org/serverA/user/delegation2/ ----> A
 * <br><Br></Br></br>
 * rather than just the server name.
 * <p>Created by Jeff Gaynor<br>
 * on 3/1/12 at  2:00 PM
 */
public class SerialStrings implements Serializable {

    public SerialStrings(DoubleHashMap<URI, String> prefixMap) {
        this.prefixMap = prefixMap;
    }

    DoubleHashMap<URI, String> prefixMap;

    /**
     * Converts a uri to a serial string by replacing the head of it with a given prefix. Note that the
     * result is not a uri. E.g.<br><br>
     * <code>toSerialString(URI.create("https://my.org/foo/222))</code>
     * would, assuming that https://my.org/foo is associated with the prefix "A" return <br><br>
     * <code>A222</code>
     * <br><br>
     * The syntax of serial strings never varies: letters + digits, and the letters are just
     * the prefix. This is how we isomorphically map uris to certificate serial strings.
     *
     * @param x
     * @return
     */
    public String toSerialString(URI x) {
        String y = x.toString();
        y = y.substring(0, y.lastIndexOf("/") + 1);
        URI newX = URI.create(y);
        String munged = mungePrefix(newX, x).toString();
        int loc = munged.indexOf(":");
        return munged.substring(0, loc) + munged.substring(loc + 1);
    }

    /**
     * Takes a serial string, e.g. A123 and converts it to the prefixed form, e.g. A:123. This can be
     * resolved into the correct namespace.
     *
     * @param x
     * @return
     */
    public URI fromSerialString(String x) {
        String[] splitAlpha = x.split("[A-Za-z]+");
        String[] splitNum = x.split("[0-9]+");
        if (splitAlpha.length != 2) {
            throw new IllegalArgumentException("Error: the string \"" + x + "\" is not of the form (letters)+(digits).");
        }
        if (splitNum[0] == null || splitAlpha[1] == null) {
            throw new IllegalArgumentException("Error: could not parse string \"" + x + "\" to get the serial string");
        }
        String y = splitNum[0] + ":" + splitAlpha[1];
        return demungePrefix(URI.create("https://cilogon.org/serverA/users"), URI.create(y));
    }

    /**
     * Replaces the head part of a URI with the prefix.
     * <p/>
     * E.g. if xmlns:a=http://www.qqq.com/fnord <br><br>
     * then
     * <code>mungePrefix("a", http://www.qqq.com/fnord, http://www.qqq.com/fnord/fnibble);</code>
     * returns <br><br>
     * a:fnibble
     *
     * @param prefix
     * @param target
     * @return
     */
    public URI mungePrefix(URI prefix, URI target) {
        String prefixDef = prefixMap.get(prefix);
        if (prefixDef == null) {
            throw new IllegalArgumentException("Error: Could not resolve prefix \"" + prefix + "\" to a namespace for \"" + target + "\". Check your arguments");
        }
        String ts = target.toString();
        String ps = prefix.toString();
        int loc = ps.length() + (ps.endsWith("/") ? 0 : 1);
        return URI.create(prefixDef + ":" + ts.substring(loc));
    }

    /**
     * Undoes what {@link #mungePrefix( java.net.URI, java.net.URI)} does.<br><br>
     * E.g.
     * <code>
     * demungePrefix("http://foo/bar/baz", "A:321");
     * </code>
     * (the arguments are actuall URIs!) then this would return<br><br>
     * <code>http://foo/bar/baz/321</code> <br><br>
     * Note that this requires the prefix so this is just attending to the details of replacing the
     * prefix, not looking it up or something else. Also note the added / before the argument!
     *
     * @param prefix
     * @param target
     * @return
     */
    public URI demungePrefix(URI prefix, URI target) {
        String ts = target.toString();
        String ps = prefix.toString();
        int loc = ts.indexOf(":");
        if (loc <= 0) {
            throw new IllegalArgumentException("Error, no prefix found in \"" + ts + "\"");
        }
        ts = ts.substring(1 + loc);
        boolean addSlash = !(ts.startsWith("/") || ps.endsWith("/"));
        return URI.create(ps + (addSlash ? "/" : "") + ts);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[ns count=" + (prefixMap == null ? 0 : prefixMap.size()) + "]";
    }
}
