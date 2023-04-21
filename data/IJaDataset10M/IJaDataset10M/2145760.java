package com.explodingpixels.macwidgets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class DPreferencesTabBar {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                Icon preferences = new ImageIcon(DPreferencesTabBar.class.getResource("/com/explodingpixels/macwidgets/icons/PreferencesGeneral.png"));
                Icon userAccounts = new ImageIcon(DPreferencesTabBar.class.getResource("/com/explodingpixels/macwidgets/icons/UserAccounts.png"));
                Icon mobileMe = new ImageIcon(DPreferencesTabBar.class.getResource("/com/explodingpixels/macwidgets/icons/DotMac.png"));
                PreferencesTabBar tabBar = new PreferencesTabBar();
                tabBar.addTab("General", preferences, null);
                tabBar.addTab("Accounts", userAccounts, null);
                tabBar.addTab("MobileMe", mobileMe, null);
                JFrame frame = new JFrame("A Java Preferences Window");
                tabBar.installWindowDraggerOnWindow(frame);
                MacUtils.makeWindowLeopardStyle(frame.getRootPane());
                frame.add(tabBar.getComponent(), BorderLayout.NORTH);
                frame.add(new JTextArea(), BorderLayout.CENTER);
                frame.setSize(500, 400);
                frame.setLocationRelativeTo(null);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}
