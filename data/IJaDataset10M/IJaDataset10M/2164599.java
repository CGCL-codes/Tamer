package org.subrecord.gui;

import java.awt.Color;

/**
 * Colors to use when displaying different types of messages in the status bar
 * 
 * @author Adyh
 * 
 */
public class StatusMessageColor {

    /**
	 * Color for regular messages
	 */
    public static final Color REGULAR = Color.BLACK;

    /**
	 * Warning... something bad has happened
	 */
    public static final Color WARNING = Color.MAGENTA;

    /**
	 * Requested operation failed (might change name? )
	 */
    public static final Color ERROR = Color.RED;

    /**
	 * Requested operation succeeded
	 */
    public static final Color SUCCESS = Color.BLUE;

    private StatusMessageColor() {
    }
}
