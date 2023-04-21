package com.bluesky.javawebbrowser.ui;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class FlowLayoutFrame extends JFrame {

    public FlowLayoutFrame() {
        setSize(800, 400);
        JPanel panel0 = new JPanel();
        panel0.setLayout(new FlowLayout());
        panel0.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        panel0.setAlignmentX(LEFT_ALIGNMENT);
        add(panel0);
        for (int j = 0; j < 15; j++) {
            Dimension dimButton = new Dimension(80, 30 + j * 5);
            Button b = new Button("B" + j);
            b.setPreferredSize(dimButton);
            b.setMinimumSize(dimButton);
            panel0.add(b);
        }
        for (int j = 0; j < 5; j++) {
            JPanel panel = new JPanel();
            panel.setSize(300, 200);
            Rectangle r = panel.getBounds();
            r.height = r.height * 2;
            panel.setBounds(r);
            panel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
            panel.setLayout(new FlowLayout());
            for (int i = 0; i < 1 + j; i++) {
                Dimension dimButton = new Dimension(80, 30);
                Button b = new Button("B" + j + "," + i);
                b.setPreferredSize(dimButton);
                b.setMinimumSize(dimButton);
                panel.add(b);
                JTextPane textPane1 = new JTextPane();
                textPane1.setText("aaa bbb ccc h");
                panel.add(textPane1);
            }
            panel0.add(panel);
        }
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] argv) {
        new FlowLayoutFrame();
    }
}
