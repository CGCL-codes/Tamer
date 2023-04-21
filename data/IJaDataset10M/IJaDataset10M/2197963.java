package edu.iastate.cs.designlab.utilities;

/**
 * Represents a command line option that is numerical in natures
 * @author Sean Mooney
 *
 * @param <T> The number type. May be Double, Float, Integer, etc.
 */
public class NumberOption<T extends Number> extends Option<T> {

    /**
     * Create a number option with the flag set to -n
     */
    public NumberOption() {
        super(new Flag("-n"));
    }

    /**
     * Create a number option with the flag to set to flag
     * @param flag
     */
    public NumberOption(String flag) {
        super(new Flag(flag));
    }
}
