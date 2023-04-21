package org.jostraca.comp.apache.oro.text.regex;

/**
 * StringSubstitution implements a Substitution consisting of a simple
 * literal string.  This class is intended for use with
 * {@link Util#substitute Util.substitute}.
 *
 * @author <a href="mailto:oro-dev@jakarta.apache.org">Daniel F. Savarese</a>
 * @version @version@
 * @since 1.1
 * @see Substitution
 * @see Util
 * @see Util#substitute
 * @see Substitution
 * @see Perl5Substitution
 */
public class StringSubstitution implements Substitution {

    int _subLength;

    String _substitution;

    /**
   * Default constructor initializing substitution to a zero length
   * String.
   */
    public StringSubstitution() {
        this("");
    }

    /**
   * Creates a StringSubstitution representing the given string.
   * <p>
   * @param substitution The string to use as a substitution.
   */
    public StringSubstitution(String substitution) {
        setSubstitution(substitution);
    }

    /**
   * Sets the substitution represented by this StringSubstitution.  You
   * should use this method in order to avoid repeatedly allocating new
   * StringSubstitutions.  It is recommended that you allocate a single
   * StringSubstitution and reuse it by using this method when appropriate.
   * <p>
   * @param substitution The string to use as a substitution.
   */
    public void setSubstitution(String substitution) {
        _substitution = substitution;
        _subLength = substitution.length();
    }

    /**
   * Returns the string substitution represented by this object.
   * <p>
   * @return The string substitution represented by this object.
   */
    public String getSubstitution() {
        return _substitution;
    }

    /**
   * Returns the same value as {@link #getSubstitution()}.
   * <p>
   * @return The string substitution represented by this object.
   */
    public String toString() {
        return getSubstitution();
    }

    /**
   * Appends the substitution to a buffer containing the original input
   * with substitutions applied for the pattern matches found so far.
   * See 
   * {@link Substitution#appendSubstitution Substitution.appendSubstition()}
   * for more details regarding the expected behavior of this method.
   * <p>
   * @param appendBuffer The buffer containing the new string resulting
   * from performing substitutions on the original input.
   * @param match The current match causing a substitution to be made. 
   * @param substitutionCount  The number of substitutions that have been
   *  performed so far by Util.substitute.
   * @param originalInput The original input upon which the substitutions are
   * being performed.  This is a read-only parameter and is not modified.
   * @param matcher The PatternMatcher used to find the current match.
   * @param pattern The Pattern used to find the current match.
   */
    public void appendSubstitution(StringBuffer appendBuffer, MatchResult match, int substitutionCount, PatternMatcherInput originalInput, PatternMatcher matcher, Pattern pattern) {
        if (_subLength == 0) return;
        appendBuffer.append(_substitution);
    }
}
