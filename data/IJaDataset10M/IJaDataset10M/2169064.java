package com.nepxion.swing.style.button.lite;

import java.awt.Color;

public class JEclipseLiteButtonStyle extends AbstractLiteButtonStyle {

    /**
	 * The identity value.
	 */
    public static final String ID = JEclipseLiteButtonStyle.class.getName();

    /**
	 * Constructs with the default.
	 */
    public JEclipseLiteButtonStyle() {
        rolloverBackground = new Color(182, 189, 210, 150);
        rolloverBorderColor = new Color(51, 71, 125, 225);
        selectionBackground = new Color(182, 189, 210);
        selectionBorderColor = new Color(51, 71, 125);
        checkColor = new Color(59, 172, 58);
        checkFocusColor = new Color(245, 165, 16);
    }
}
