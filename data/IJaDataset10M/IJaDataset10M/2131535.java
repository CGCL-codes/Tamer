package com.volantis.mcs.protocols;

/**
 * A type safe enumerator for describing menu orientation.
 */
public class MenuOrientation {

    /**
     * An unknown menu orientation i.e. leave the orientation up to the
     * browser. Typically this will result in a menu with no separators
     * between the menu items.
     */
    public static final MenuOrientation UNKNOWN = new MenuOrientation();

    /**
     * A vertical menu orientation. Typically this will result in a menu
     * with line breaking separators that force the menu to display vertically 
     * e.g. a br tags.
     */
    public static final MenuOrientation VERTICAL = new MenuOrientation();

    /**
     * A horizontal menu orientation. Typically this will result in a menu
     * with non-line breaking separators such as spaces.
     */
    public static final MenuOrientation HORIZONTAL = new MenuOrientation();

    private MenuOrientation() {
    }
}
