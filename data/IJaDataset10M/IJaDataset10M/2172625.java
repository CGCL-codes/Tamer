package jws.commons.util;

import java.util.regex.Pattern;

/**
 * The regexp-based string constratint
 */
public final class RegexpConstraint implements IConstraint<String> {

    private Pattern _pattern;

    /**
     * Initializes this {@link RegexpConstraint} with the given pattern 
     * @param pattern Regualr expression to check the strings
     */
    public RegexpConstraint(String pattern) {
        _pattern = Pattern.compile(pattern);
    }

    /**
     * Returns regexp pattern this constraint was configured with
     * @return The regexp pattern
     */
    public String getPattern() {
        return _pattern.pattern();
    }

    /**
     * Checks if the specified string matches the configured regular expression
     * @param item The string to check
     * @return <code>true</code> if <code>item</code> matches the regexp, <code>false</code> otherwise
     */
    public boolean accept(String item) {
        return _pattern.matcher(item).matches();
    }
}
