package org.syrup;

/**
 * A Template example object. Used to match objects contained in a WorkSpace
 * based on the Template's qualifiers.
 * 
 * @author Robbert van Dalen
 * @see WorkSpace#match
 */
public interface Template {

    static final String COPYRIGHT = "Copyright 2005 Robbert van Dalen." + "At your option, you may copy, distribute, or make derivative works under " + "the terms of The Artistic License. This License may be found at " + "http://www.opensource.org/licenses/artistic-license.php. " + "THERE IS NO WARRANTY; USE THIS PRODUCT AT YOUR OWN RISK.";

    /**
     * Ignores the matching attribute.
     */
    public static final int IGNORE = 0;

    /**
     * Match an attribute on the 'is equal' qualifier.
     */
    public static final int EQUAL = 1;

    /**
     * Match an attribute on the 'greater then' qualifier.
     */
    public static final int GREATER = 2;

    /**
     * Match an attribute on the 'greater or equal' qualifier.
     */
    public static final int GREATER_EQUAL = 3;

    /**
     * Match an attribute on the 'less then' qualifier.
     */
    public static final int LESS = 4;

    /**
     * Match an attribute on the 'less or equal' qualifier.
     */
    public static final int LESS_EQUAL = 5;

    /**
     * Match an attribute on the 'like' qualifier.
     */
    public static final int LIKE = 6;

    /**
     * Match an attribute on the 'not' qualifier.
     */
    public static final int NOT = 7;

    /**
     * Match an attribute on the 'not like' qualifier.
     */
    public static final int NOT_LIKE = 8;
}
