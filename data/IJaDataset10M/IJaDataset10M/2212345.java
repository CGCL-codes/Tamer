package ui.panel;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import ui.Messages;

@SuppressWarnings("serial")
public class ICNetworkPanel extends JPanel {

    private JTabbedPane tabbedPanel;

    private JPanel clientPanel;

    private JPanel serverPanel;

    public ICNetworkPanel() {
        initialize();
    }

    protected void initialize() {
        this.setName(Messages.getString("common.network"));
        this.setLayout(new BorderLayout());
        this.add(getTabbedPanel(), BorderLayout.CENTER);
    }

    /**
	 * @return Returns the clientPanel.
	 */
    protected JPanel getClientPanel() {
        if (clientPanel == null) {
            clientPanel = new TabledClientWorkstationPanel();
        }
        return clientPanel;
    }

    /**
	 * @return Returns the serverPanel.
	 */
    protected JPanel getServerPanel() {
        if (serverPanel == null) {
            serverPanel = new TabledServerWorkstationPanel();
        }
        return serverPanel;
    }

    /**
	 * @return Returns the tabbedPanel.
	 */
    protected JTabbedPane getTabbedPanel() {
        if (tabbedPanel == null) {
            tabbedPanel = new JTabbedPane();
            tabbedPanel.addTab(Messages.getString("common.clientworkstations"), new ImageIcon(this.getClass().getResource("/icon/16x16/devices/computer.png")), getClientPanel(), Messages.getString("panel.icnetworkpanel.manageclientworkstations"));
            tabbedPanel.addTab(Messages.getString("common.serverworkstations"), new ImageIcon(this.getClass().getResource("/icon/16x16/places/network-server.png")), getServerPanel(), Messages.getString("panel.icnetworkpanel.manageserverworkstations"));
        }
        return tabbedPanel;
    }
}
