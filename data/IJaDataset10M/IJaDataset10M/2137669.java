package net.sourceforge.jpalm.palmdb;

import net.sourceforge.juint.UInt8;

/**
 * Category constants.
 */
public class Category {

    /**
     * The maximum number of categories.<br>
     * {@value}
     */
    public static final int NUMBER_OF_CATEGORIES = 16;

    /**
     * The maximum length of a category label, including 1 for the null terminator.<br>
     * {@value}
     */
    public static final int CATEGORY_LENGTH = 16;

    /**
     * The "Unfiled" category.
     */
    public static final UInt8 CATEGORY_UNFILED = new UInt8(0x00);

    protected Category() {
    }
}
