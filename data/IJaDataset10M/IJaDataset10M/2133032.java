package org.xieqi.paper.ui;

import java.awt.Toolkit;
import javax.swing.JFrame;

public class MyFrame extends JFrame {

    protected void setPosCenter() {
        Toolkit tool = Toolkit.getDefaultToolkit();
        int screen_w = tool.getScreenSize().width;
        int screen_h = tool.getScreenSize().height;
        int frame_w = this.getSize().width;
        int frame_h = this.getSize().height;
        this.setLocation((screen_w - frame_w) / 2, (screen_h - frame_h) / 2);
    }
}
