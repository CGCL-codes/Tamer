package a03.swing.plaf.venus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import a03.swing.plaf.style.A03ToolTipStyle;

public class A03VenusToolTipStyle implements A03ToolTipStyle, A03VenusConstants {

    public Color getBackgroundColor() {
        return popupMenuBackground;
    }

    public Font getFont() {
        return font11;
    }

    public Paint getBorderPaint(int x, int y, int width, int height) {
        return popupMenuBorder;
    }
}
