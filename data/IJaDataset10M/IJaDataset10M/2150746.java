package com.vision.gui;

import ade.ADEGuiPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import com.vision.*;

public class VisionRightServerPanel extends ADEGuiPanel {

    private static String prg = "VisionRightServerPanel";

    private static boolean debug = false;

    private JLabel label;

    private Dimension dim = new Dimension(330, 310);

    public VisionRightServerPanel(VisionServer2 server) {
        super(server);
        title = "Right Frame";
        label = new JLabel();
        contentPane.add(label);
        contentPane.add(close);
        setTitle(title);
        setMinimumSize(dim);
        setPreferredSize(dim);
        setMaximumSize(dim);
        setUpdateTime(500);
    }

    protected void updatePanel() {
        try {
            byte buf[] = (byte[]) call("getFrame", "/tmp/frame2.jpg");
            ImageIcon icon = new ImageIcon(buf);
            label.setIcon(icon);
        } catch (Exception e) {
        }
    }
}
