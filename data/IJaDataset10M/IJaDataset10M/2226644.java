package org.xmlvm.demo.java.photovm.ui;

import java.awt.BorderLayout;
import java.awt.Panel;

/**
 * Combining the left-hand side panel with the main thumbnail grid panel.
 * 
 */
public class MainPanel extends Panel {

    private ThumbnailGridPanel thumbnailGridPanel = new ThumbnailGridPanel();

    private LeftHandSidePanel leftHandSidePanel = new LeftHandSidePanel(thumbnailGridPanel);

    public MainPanel() {
        setLayout(new BorderLayout());
        add(leftHandSidePanel, BorderLayout.WEST);
        add(thumbnailGridPanel, BorderLayout.CENTER);
    }
}
