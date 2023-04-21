package com.paracel.tt.util;

import java.awt.*;

/** This class creates the fonts used by TraceTuner launcher and viewer. */
public class Fonts {

    public static final Font BASE_FONT = new Font("Courier", Font.PLAIN, 12);

    public static final Font INDEX_FONT = new Font("Arial", Font.PLAIN, 10);

    public static final Font QV_FONT = new Font("Arial", Font.PLAIN, 8);

    public static final Font LABEL_FONT1 = new Font("Dialog", Font.BOLD, 11);

    public static final Font LABEL_FONT2 = new Font("Arial", Font.PLAIN, 11);

    public static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 8);

    public static final Font CONTENT_FONT = new Font("Arial", Font.PLAIN, 8);

    public static final Font MENU_FONT = new Font("Dialog", Font.BOLD, 11);

    public static final Font TEXT_FONT = new Font("Courier", Font.PLAIN, 12);

    /**
     * Gets the <code>FontMetrics</code> of the specified font.
     * @return 	the fontMetrics.
     * @param	f	the specified font.
     */
    public static FontMetrics getFontMetrics(Font f) {
        Container c = new Container();
        FontMetrics fm = c.getFontMetrics(f);
        c = null;
        return fm;
    }
}
