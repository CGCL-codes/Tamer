package org.bresearch.websec.net.win;

import javax.swing.JFrame;

public class HttpConnectFileLoadWin {

    public static void main(final String[] args) {
        final FrameBuilder builder = new FrameBuilder();
        final JFrame frame = builder.buildFileLoadFrame();
        frame.setVisible(true);
    }
}
