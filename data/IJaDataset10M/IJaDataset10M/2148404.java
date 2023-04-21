package clustermines.gui;

import javax.swing.JApplet;
import javax.swing.JTabbedPane;

public class SweeperApplet extends JApplet {

    public void init() {
        JTabbedPane tabbedPane = new JTabbedPane();
        SweeperPanel sweeperPanel = new SweeperPanel();
        sweeperPanel.setApplet();
        tabbedPane.addTab("Game", SweeperPanel.createPaddedPanel(sweeperPanel));
        tabbedPane.addTab("Preferences", SweeperPanel.createPaddedPanel(sweeperPanel.getPreferencesPanel()));
        this.add(tabbedPane);
    }
}
