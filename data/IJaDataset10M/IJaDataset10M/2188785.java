package net.sourceforge.vigilog.ui;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * 
 */
class SplitPaneComponentAdapter extends ComponentAdapter {

    private final JSplitPane _splitPane;

    public SplitPaneComponentAdapter(JSplitPane splitPane) {
        _splitPane = splitPane;
    }

    public void componentResized(ComponentEvent e) {
        _splitPane.setDividerLocation(0.7);
    }
}
