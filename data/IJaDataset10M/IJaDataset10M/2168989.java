package org.openconcerto.ui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class FrameUtil {

    public static void show(final Window frame) {
        frame.setVisible(true);
        if (frame instanceof Frame) ((Frame) frame).setState(Frame.NORMAL);
        frame.toFront();
    }

    public static String getNimbusClassName() {
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                return info.getClassName();
            }
        }
        return null;
    }

    public static void showPacked(Frame frame) {
        frame.pack();
        frame.setMinimumSize(new Dimension(frame.getWidth(), frame.getHeight()));
        FrameUtil.show(frame);
    }
}
