package net.rptools.maptool.client.ui.macrobuttons.panels;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TabPopupListener extends MouseAdapter {

    private JComponent component;

    private int index;

    public TabPopupListener(JComponent component, int index) {
        this.component = component;
        this.index = index;
    }

    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            new TabPopupMenu(component, index).show(component, e.getX(), e.getY());
        } else {
        }
    }
}
