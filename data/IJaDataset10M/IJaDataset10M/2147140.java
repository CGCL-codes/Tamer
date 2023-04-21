package au.edu.archer.metadata.mde.validator;

import java.util.WeakHashMap;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * This singleton class provides a light-weight cache for compiled regexes.
 * (Surprisingly, the standard implementation of
 * {@link java.util.regex.Pattern#compile(String)}
 * does not do any result caching.)
 *
 * @author scrawley@itee.uq.edu.au
 */
public class PatternCache {

    private static final WeakHashMap<String, Pattern> patternCache = new WeakHashMap<String, Pattern>();

    public static synchronized Pattern compile(String str) throws PatternSyntaxException {
        Pattern res = patternCache.get(str);
        if (res == null) {
            res = Pattern.compile(str);
            patternCache.put(str, res);
        }
        return res;
    }
}
