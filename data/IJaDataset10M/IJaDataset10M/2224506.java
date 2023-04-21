package org.apache.harmony.jretools.policytool.model;

/**
 * Abstract ancestor to represent a policy entry.
 */
public abstract class PolicyEntry {

    /** Terminator character of the policy entry texts. */
    public static final char TERMINATOR_CHAR = ';';

    /**
     * Returns the policy entry text.<br>
     * Should not contain a line separator in the end but <code>TERMINATOR_CHAR</code> must be included.
     * @return the policy entry text
     */
    public abstract String getText();
}
