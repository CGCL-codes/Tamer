package joptsimple.util;

import java.util.regex.Pattern;
import static java.util.regex.Pattern.*;
import joptsimple.ValueConversionException;
import joptsimple.ValueConverter;

/**
 * Ensures that values entirely match a regular expression.
 *
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 * @version $Id: RegexMatcher.java,v 1.4 2009/04/04 01:24:26 pholser Exp $
 */
public class RegexMatcher implements ValueConverter<String> {

    private final Pattern pattern;

    /**
     * Creates a matcher that uses the given regular expression, modified by the given
     * flags.
     *
     * @param pattern the regular expression pattern
     * @param flags modifying regex flags
     * @throws IllegalArgumentException if bit values other than those corresponding to
     * the defined match flags are set in {@code flags}
     * @throws java.util.regex.PatternSyntaxException if the expression's syntax is
     * invalid
     */
    public RegexMatcher(String pattern, int flags) {
        this.pattern = compile(pattern, flags);
    }

    /**
     * Gives a matcher that uses the given regular expression.
     *
     * @param pattern the regular expression pattern
     * @return the new converter
     * @throws java.util.regex.PatternSyntaxException if the expression's syntax is
     * invalid
     */
    public static ValueConverter<String> regex(String pattern) {
        return new RegexMatcher(pattern, 0);
    }

    /**
     * {@inheritDoc}
     */
    public String convert(String value) {
        if (!pattern.matcher(value).matches()) {
            throw new ValueConversionException("Value [" + value + "] did not match regex [" + pattern.pattern() + ']');
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public Class<String> valueType() {
        return String.class;
    }

    /**
     * {@inheritDoc}
     */
    public String valuePattern() {
        return pattern.pattern();
    }
}
