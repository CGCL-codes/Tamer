package net.sf.sbcc.status;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class JStatusBar extends JPanel {

    private JPanel leftPanels;

    private JPanel rightPanels;

    private Map<String, JLabel> panels;

    public JStatusBar() {
        super(new BorderLayout());
        panels = new HashMap<String, JLabel>(5);
        leftPanels = new JPanel();
        add(leftPanels, BorderLayout.WEST);
        rightPanels = new JPanel();
        add(rightPanels, BorderLayout.EAST);
        Dimension prefSize = new Dimension(10, new JLabel("�j").getPreferredSize().height + 8);
        setPreferredSize(prefSize);
    }

    public void addPanel(String panelName, StatusBarConstraints location, StatusBarStyle style) {
        JLabel newLabel = new JLabel();
        switch(style) {
            case NOBORDER:
                newLabel.setBorder(null);
                break;
            case BORDER_FLAT:
                newLabel.setBorder(new LineBorder(SystemColor.controlLtHighlight));
                break;
            case BORDER_3D:
                newLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                break;
        }
        panels.put(panelName, newLabel);
        if (location == StatusBarConstraints.LEFT) {
            leftPanels.add(newLabel);
        } else {
            rightPanels.add(newLabel);
        }
        newLabel.invalidate();
    }

    public void setPanelText(String panelName, String newText) {
        JLabel panel = panels.get(panelName);
        panel.setText(newText);
    }
}
