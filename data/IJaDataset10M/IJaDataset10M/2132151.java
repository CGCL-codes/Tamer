package org.sodeja.swing.renderer;

import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.sodeja.functional.Pair;
import org.sodeja.swing.context.ApplicationContext;

public class RendererUtils {

    private RendererUtils() {
    }

    public static final String ODD_COLOR = "ODD_COLOR";

    public static final String TABLE_ROW_HEIGHT = "TABLE_ROW_HEIGHT";

    private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    protected static Pair<Color, Color> makeColors(ApplicationContext ctx, JComponent component) {
        Color defaultBackground = component.getBackground();
        Color rowBackground = Color.LIGHT_GRAY;
        if (rowBackground == null) {
            rowBackground = defaultBackground;
        }
        return Pair.of(defaultBackground, rowBackground);
    }

    protected static void updateView(JComponent component, Pair<Color, Color> scheme, int row) {
        if (row % 2 == 1) {
            component.setBackground(scheme.second);
        } else {
            component.setBackground(scheme.first);
        }
    }

    protected static void setProperBorder(JComponent component, boolean isSelected, boolean hasFocus) {
        if (hasFocus) {
            Border border = null;
            if (isSelected) {
                border = UIManager.getBorder("Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = UIManager.getBorder("Table.focusCellHighlightBorder");
            }
            component.setBorder(border);
        } else {
            component.setBorder(noFocusBorder);
        }
    }
}
