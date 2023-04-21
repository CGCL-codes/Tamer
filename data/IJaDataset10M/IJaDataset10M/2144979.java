package com.peterhi.client.ui.events;

import java.util.EventObject;

/**
 * Style bar event object.
 * @author YUN TAO
 *
 */
public class StyleBarEvent extends EventObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4107636393639912944L;

    /**
	 * Font changed.
	 */
    public static final int FONT_CHANGE = 1;

    /**
	 * Font size changed.
	 */
    public static final int SIZE_CHANGE = 2;

    /**
	 * Font style changed.
	 */
    public static final int STYLE_CHANGE = 3;

    /**
	 * Underline changed.
	 */
    public static final int UNDERLINE_CHANGE = 4;

    /**
	 * Strikeout changed.
	 */
    public static final int STRIKEOUT_CHANGE = 5;

    /**
	 * Align changed.
	 */
    public static final int ALIGN_CHANGE = 6;

    /**
	 * Justify changed.
	 */
    public static final int JUSTIFY_CHANGE = 7;

    /**
	 * Text color changed.
	 */
    public static final int COLOR_CHANGE = 8;

    private int type;

    /**
	 * Constructor.
	 * @param source Event source.
	 * @param type Style update type.
	 */
    public StyleBarEvent(Object source, int type) {
        super(source);
        this.type = type;
    }

    /**
	 * Gets the style update type.
	 * @return The type integer.
	 */
    public int getType() {
        return type;
    }
}
