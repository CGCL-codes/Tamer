package net.sf.vex.css;

import org.w3c.css.sac.LexicalUnit;

/**
 * Represents a CSS property.
 */
public interface IProperty {

    /** Constant indicating the length is along the horizontal axis. */
    public static final byte AXIS_HORIZONTAL = 0;

    /** Constant indicating the length is along the vertical axis. */
    public static final byte AXIS_VERTICAL = 1;

    /**
     * Returns the name of the property.
     */
    public String getName();

    /**
     * Calculates the value of a property given a LexicalUnit.
     * @param lu LexicalUnit to interpret.
     * @param parentStyles Styles of the parent element. These are used
     * when the property inherits a value.
     * @param styles Styles currently in effect. Often, the calculated
     * value depends on previously calculated styles such as font size 
     * and color.
     */
    public Object calculate(LexicalUnit lu, Styles parentStyles, Styles styles);
}
