package org.jsresources.apps.cdplayer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.jsresources.apps.jmvp.manager.RM;

public class AboutPane extends JTabbedPane {

    public AboutPane() {
        super();
        JPanel panel;
        JComponent component;
        panel = new JPanel();
        component = new JLabel(RM.getResourceString("aboutDialog.programText"));
        panel.add(component);
        this.add(panel, RM.getResourceString("aboutDialog.programTabLabel"));
        panel = new JPanel();
        component = new JLabel(RM.getResourceString("aboutDialog.authorsText"));
        panel.add(component);
        this.add(panel, RM.getResourceString("aboutDialog.authorsTabLabel"));
        component = new JPanel();
        this.add(component, RM.getResourceString("aboutDialog.licenseTabLabel"));
    }
}
