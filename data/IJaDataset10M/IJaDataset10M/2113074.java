package org.vous.facelib.tests.util;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class FrameViewingApp extends TestApp {

    private static final long serialVersionUID = 6779148083479241055L;

    private FramePanel mPanel;

    public FrameViewingApp(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        mPanel = new FramePanel();
        setPreferredSize(new Dimension(800, 600));
        getContentPane().add(mPanel);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                setVisible(true);
            }
        });
    }

    public void updateFrame(org.vous.facelib.bitmap.Bitmap frame) {
        mPanel.updateFrame(frame);
    }
}
