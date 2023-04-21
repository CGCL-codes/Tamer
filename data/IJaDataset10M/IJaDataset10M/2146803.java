package com.joe.gui.jdk6.windows.reflection;

import java.awt.*;
import javax.swing.*;

public class SimpleFrame extends JReflectionFrame {

    public SimpleFrame() {
        super("Reflection");
        this.setLayout(new BorderLayout());
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(new JButton("sample"));
        this.add(bottom, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Window w = new SimpleFrame();
                w.setVisible(true);
            }
        });
    }
}
