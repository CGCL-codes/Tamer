package com.extentech.swingtools.tabpanel;

import com.extentech.swingtools.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 */
public class SingleRowTabbedPaneExample4 extends JPanel {

    public SingleRowTabbedPaneExample4() {
        setLayout(new BorderLayout());
        SingleRowTabbedPane tabbedPane = new SingleRowTabbedPane(SingleRowTabbedPane.FOUR_BUTTONS, SwingConstants.LEFT);
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
        String tabs[] = { "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten" };
        for (int i = 0; i < tabs.length; i++) {
            tabbedPane.addTab(tabs[i], createPane(tabs[i]));
        }
        tabbedPane.setSelectedIndex(0);
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPane(String s) {
        JPanel p = new JPanel();
        p.add(new JLabel(s));
        return p;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Four buttons Example");
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(new SingleRowTabbedPaneExample4());
        frame.setSize(250, 100);
        frame.setVisible(true);
    }
}
