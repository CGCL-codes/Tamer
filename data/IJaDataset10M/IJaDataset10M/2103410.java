package org.ros.worldwind.ui;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UITools {

    public static JPanel generateTitleLabel(String title, JPanel parentPanel, Color titleBackgroundColor, Color titleFontColor) {
        setPanelColor(parentPanel, titleBackgroundColor);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(titleFontColor);
        parentPanel.add(titleLabel);
        return parentPanel;
    }

    public static void setPanelColor(JPanel panel, Color color) {
        panel.setOpaque(true);
        panel.setBackground(color);
    }
}
