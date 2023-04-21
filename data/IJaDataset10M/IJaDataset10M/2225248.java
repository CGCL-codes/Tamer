package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import java.awt.BorderLayout;

public class PresetExample {

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Preset Example");
        frame.getContentPane().setLayout(new BorderLayout());
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("a>a2b-~b");
        layout.addRow("...6....");
        layout.addRow("c>c2d-~d");
        layout.addRow("...7....");
        layout.addRow("e......e");
        JPanel basePanel = new JPanel(layout);
        basePanel.add(new JLabel("First Name:"), "a");
        basePanel.add(new JTextField(20), "b");
        basePanel.add(new JLabel("Last Name:"), "c");
        basePanel.add(new JTextField(20), "d");
        basePanel.add(new JButton("OK"), "e");
        frame.getContentPane().add(basePanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
