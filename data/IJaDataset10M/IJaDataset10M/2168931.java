package org.radeox.regex;

import org.apache.oro.text.regex.Perl5Matcher;
import org.apache.oro.text.regex.Util;
import org.apache.oro.text.regex.Perl5Substitution;

public class OroMatcher extends Matcher {

    private OroPattern pattern;

    private String input;

    private org.apache.oro.text.regex.Perl5Matcher internalMatcher;

    public OroMatcher(String input, Pattern pattern) {
        this.input = input;
        this.pattern = (OroPattern) pattern;
        internalMatcher = new Perl5Matcher();
    }

    public String substitute(Substitution substitution) {
        return Util.substitute(internalMatcher, pattern.getPattern(), new OroActionSubstitution(substitution), input, Util.SUBSTITUTE_ALL);
    }

    public String substitute(String substitution) {
        return Util.substitute(internalMatcher, pattern.getPattern(), new Perl5Substitution(substitution, Perl5Substitution.INTERPOLATE_ALL), input, Util.SUBSTITUTE_ALL);
    }

    protected Perl5Matcher getMatcher() {
        return internalMatcher;
    }

    public boolean contains() {
        return internalMatcher.contains(input, pattern.getPattern());
    }

    public boolean matches() {
        return internalMatcher.matches(input, pattern.getPattern());
    }
}
