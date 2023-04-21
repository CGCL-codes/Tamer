package it.unibz.izock;

import java.io.Serializable;

/**
 * The Class Color is used for composing the games' card(able)s and contains
 * all the colors defined in "Salzburger Karten".
 */
public class Color implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 82818402931477532L;

    /** The Constant EICHEL. */
    public static final Color EICHEL = new Color("Eichel");

    /** The Constant HERZ. */
    public static final Color HERZ = new Color("Herz");

    /** The Constant SCHELLEN. */
    public static final Color SCHELLEN = new Color("Schellen");

    /** The Constant LAUB. */
    public static final Color LAUB = new Color("Laub");

    /** The _display name. */
    private String _displayName;

    /**
	 * Instantiates a new color.
	 * 
	 * @param aName the a name
	 */
    private Color(String aName) {
        _displayName = aName;
    }

    /**
	 * Gets the name.
	 * 
	 * @return the name
	 */
    public String getName() {
        return _displayName;
    }

    public String toString() {
        return getName();
    }

    /**
	 * Equals.
	 * 
	 * @param aColor the a color
	 * 
	 * @return true, if successful
	 */
    public boolean equals(Color aColor) {
        return _displayName.equals(aColor.getName());
    }
}
