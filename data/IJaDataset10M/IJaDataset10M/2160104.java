package newgen.presentation.cataloguing.advanced;

import java.awt.*;
import javax.swing.*;

public class MyUI extends javax.swing.plaf.metal.MetalTabbedPaneUI {

    protected JButton createScrollButton(int direction) {
        if (direction != SOUTH && direction != NORTH && direction != EAST && direction != WEST) {
            throw new IllegalArgumentException("Direction must be one of: SOUTH, NORTH, EAST or WEST");
        }
        return new ScrollableTabButton(direction);
    }

    private class ScrollableTabButton extends javax.swing.plaf.basic.BasicArrowButton implements javax.swing.plaf.UIResource, SwingConstants {

        public ScrollableTabButton(int direction) {
            super(direction, UIManager.getColor("TabbedPane.selected"), UIManager.getColor("TabbedPane.shadow"), UIManager.getColor("TabbedPane.darkShadow"), UIManager.getColor("TabbedPane.highlight"));
            if (direction == WEST) setToolTipText("<<<<<"); else if (direction == EAST) setToolTipText(">>>>>");
        }
    }
}
