package org.parsel.model.rules;

import java.io.Serializable;

/**
 * Replacement (or rewrite) rule.
 */
public interface IReplacementRule extends Serializable {

    /**
     * <code>1</code><br>
     * Default probability of applying this rule. The probability is one, so as
     * default a rule will always be rewritten.
     */
    public static final double DEFAULT_PROBABILITY = 1D;

    /**
     * Sets the comment. Can be null.
     */
    void setComment(String comment);

    /**
     * Gets the comment. Can be null.
     */
    String getComment();

    /**
     * Sets the original
     *
     * @see #setReplacement(StringBuffer)
     */
    void setOriginal(StringBuffer original);

    /**
     * Gets the original
     *
     * @see #getReplacement()
     */
    StringBuffer getOriginal();

    /**
     * Sets the replacement
     */
    void setReplacement(StringBuffer replacement);

    /**
     * Gets the replacement, ie the string which the original are to be replaced
     * with.
     *
     * @see #getOriginal()
     */
    StringBuffer getReplacement();

    /**
     * Tests if this rule would be rewritten
     *
     * @param c
     *            String to check
     * @param random
     *            Random stream to use
     * @return true if
     *         <ul>
     *         <li>the first char of <code>original</code> equals
     *         <code>c</code> and
     *         <li><code>random.nextDouble()</code> returns a number smaller
     *         than or equal to the probability of this rule
     *         </ul>
     */
    boolean applies(char c, org.parsel.model.Random random);

    /**
     * Gets the probability between 0 and 1 inclusive
     */
    double getProbability();

    /**
     * Sets the probability between 0 and 1 inclusive
     */
    void setProbability(double probability);
}
