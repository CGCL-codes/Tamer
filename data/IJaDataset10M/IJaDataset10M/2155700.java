package com.nepxion.swing.style.button.lite;

import java.awt.Color;
import com.nepxion.swing.style.button.IButtonStyle;

public interface ILiteButtonStyle extends IButtonStyle {

    /**
	 * Gets the roll over background.
	 * @return the instance of Color
	 */
    public Color getRolloverBackground();

    /**
	 * Gets the roll over border color.
	 * @return the instance of Color
	 */
    public Color getRolloverBorderColor();

    /**
	 * Gets the selection background.
	 * @return the instance of Color
	 */
    public Color getSelectionBackground();

    /**
	 * Gets the selection border color.
	 * @return the instance of Color
	 */
    public Color getSelectionBorderColor();

    /**
	 * Gets the check color.
	 * @return the instance of Color
	 */
    public Color getCheckColor();

    /**
	 * Gets the check focus color.
	 * @return the instance of Color
	 */
    public Color getCheckFocusColor();
}
