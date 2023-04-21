package com.googlecode.maratische.google.testBrowser;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import com.sun.java.swing.plaf.motif.MotifLookAndFeel;

public class Error1 extends JFrame {

    public Error1() {
        this.setSize(800, 200);
        this.setLayout(new BorderLayout());
        JTextArea textArea = new JTextArea();
        this.add(textArea, BorderLayout.CENTER);
        JToolBar toolBar = new JToolBar();
        toolBar.setBackground(Color.green);
        Box boxToolBar = Box.createHorizontalBox();
        boxToolBar.add(new Button("Start"));
        boxToolBar.add(Box.createHorizontalStrut(10));
        boxToolBar.add(new Button("Offline"));
        boxToolBar.add(Box.createHorizontalGlue());
        boxToolBar.add(Box.createHorizontalStrut(10));
        boxToolBar.add(new Button("help"));
        toolBar.add(boxToolBar);
        this.add(toolBar, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        try {
        } catch (Exception unused) {
        }
        Error1 errorFrame = new Error1();
        errorFrame.setVisible(true);
    }
}
