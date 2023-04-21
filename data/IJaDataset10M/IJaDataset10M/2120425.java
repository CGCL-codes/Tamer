package com.nepxion.swing.style.texture.shrink;

import java.awt.Color;
import com.nepxion.swing.style.texture.basic.JGrayTextureStyle;

public class JGrayHeaderTextureStyle extends JBasicHeaderTextureStyle {

    /**
	 * The style path.
	 */
    public static final String STYLE_PATH = "skin/shrink/gray/";

    /**
	 * Constructs with the default.
	 */
    public JGrayHeaderTextureStyle() {
        super(JGrayTextureStyle.STYLE_PATH, STYLE_PATH);
        foreground = new Color(59, 59, 59);
        selectionForeground = new Color(59, 59, 59);
        borderColor = new Color(161, 169, 179);
    }
}
