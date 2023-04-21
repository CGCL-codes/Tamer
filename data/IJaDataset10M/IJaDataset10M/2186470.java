package org.logview4j.ui.popup;

import java.awt.*;
import javax.swing.*;
import org.logview4j.ui.action.*;

/**
 * A popup menu for the log table 
 */
public class LogTablePopupMenu extends JPopupMenu {

    public LogTablePopupMenu() {
        init();
    }

    private void init() {
    }

    public void showMenu(Component parent, int x, int y) {
        removeAll();
        add(new JMenuItem(new DeleteAboveAction()));
        add(new JMenuItem(new DeleteBelowAction()));
        show(parent, x, y);
    }
}
