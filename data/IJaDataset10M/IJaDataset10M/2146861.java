package net.sourceforge.rconx.view.panel;

import java.awt.GridLayout;
import javax.swing.JPanel;
import net.sourceforge.rconx.view.panel.rcon.RconPanel;

/**
 * @author RconX Project
 */
public class MainPanel extends JPanel {

    private static final ServerListPanel serverListPanel = new ServerListPanel();

    private static final ServerRulesPanel serverDetailPanel = new ServerRulesPanel();

    private static final RconPanel rconPanel = new RconPanel();

    public MainPanel() {
        this.setLayout(new GridLayout(3, 1));
        this.add(serverListPanel);
        this.add(serverDetailPanel);
        this.add(rconPanel);
    }
}
