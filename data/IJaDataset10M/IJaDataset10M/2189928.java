package org.tranche.gui;

import java.awt.BorderLayout;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class AnnotationFrame extends GenericFrame {

    private AnnotationPanel annotationPanel = new AnnotationPanel();

    public AnnotationFrame() {
        setTitle("Annotations");
        setLayout(new BorderLayout());
        add(annotationPanel, BorderLayout.CENTER);
        setSize(annotationPanel.getSize());
    }

    public AnnotationPanel getPanel() {
        return annotationPanel;
    }
}
