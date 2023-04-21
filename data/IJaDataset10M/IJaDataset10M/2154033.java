package com.zerocool.editor;

import javax.swing.*;

public class TileLevelEditor {

    public static void main(String args[]) {
        JFrame frame = new JFrame("TileLevelEditor v1.0 by Revention Software(tm) for Project Zero Cool");
        frame.setResizable(false);
        TLEditor panel = new TLEditor();
        frame.setJMenuBar(panel.getMenuBar());
        frame.addWindowListener(panel);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
