package de.dgrid.bisgrid.services.proxy.redirect.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extended version of the PatternEx class.
 */
public class PatternEx {

    private final String regExp;

    private final Pattern base;

    public static final int CASE_INSENSITIVE = Pattern.CASE_INSENSITIVE;

    private PatternEx(Pattern base, String regExp) {
        super();
        this.base = base;
        this.regExp = regExp;
    }

    /**
     * Compile an expression
     */
    public static PatternEx compile(String regExp) {
        Pattern p = Pattern.compile(regExp);
        return new PatternEx(p, regExp);
    }

    /**
     * Compile an expression. Support additional flags
     */
    public static PatternEx compile(String regExp, int flags) {
        Pattern p = Pattern.compile(regExp, flags);
        return new PatternEx(p, regExp);
    }

    /**
     * Get a matcher for a given value
     */
    public Matcher matcher(CharSequence v) {
        return base.matcher(v);
    }

    /**
     */
    @Override
    public String toString() {
        return regExp;
    }
}
