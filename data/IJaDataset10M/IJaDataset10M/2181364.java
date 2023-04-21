package org.vikamine.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import javax.swing.JDialog;
import javax.swing.JProgressBar;

/**
 * 
 * @author Alex Plischke
 * 
 */
public class ProgressBarPopup extends JDialog {

    private static final long serialVersionUID = -4173156641773240002L;

    private JProgressBar progressBar;

    public ProgressBarPopup(Frame aFrame) {
        super(aFrame, true);
        setTitle("Loading...");
        setLayout(new BorderLayout());
        setResizable(false);
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        add(progressBar, BorderLayout.NORTH);
        setModal(false);
        pack();
        center(aFrame);
    }

    /**
     * center dialog on main frame
     */
    private void center(Frame owner) {
        if (owner != null) {
            Dimension frameSize = owner.getSize();
            Dimension dialogSize = getPreferredSize();
            Point framePos = owner.getLocationOnScreen();
            setLocation(framePos.x + (frameSize.width / 2 - (dialogSize.width / 2)), framePos.y + (frameSize.height / 2 - (dialogSize.height / 2)));
        }
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }
}
