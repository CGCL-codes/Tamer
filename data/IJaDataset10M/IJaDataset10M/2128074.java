package fr.soleil.mambo.actions.listeners;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TabbedPaneListener implements ChangeListener {

    public TabbedPaneListener() {
    }

    public void stateChanged(ChangeEvent change) {
        if (change != null) {
            Object source = change.getSource();
            if (source != null && source instanceof JTabbedPane) {
                ((JTabbedPane) source).repaint();
            }
        }
    }
}
