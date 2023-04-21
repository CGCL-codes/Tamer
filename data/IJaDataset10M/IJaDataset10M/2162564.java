package org.mmt.gui.home;

import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.mmt.core.MysqlServer;
import org.mmt.core.ServerStatusListener;

public class HomeServerPanel extends JPanel implements ServerStatusListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private MysqlServer server;

    private JLabel serverText = new JLabel();

    public HomeServerPanel(MysqlServer server) {
        this.server = server;
        this.server.addServerStatusListener(this);
        setBorder(BorderFactory.createTitledBorder(server.getName()));
        setLayout(new FlowLayout());
        add(new JLabel(new ImageIcon(ClassLoader.getSystemResource("resources/server.png"))));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        onServerStatusChange(server);
        textPanel.add(serverText);
        add(textPanel);
    }

    @Override
    public void onServerStatusChange(MysqlServer server) {
        serverText.setText("<html><center><hr/><b>Server hostname:</b> " + server.getHost() + "<br/><hr/>" + "<b>Server port:</b> " + server.getPort() + "<br/><hr/>" + "<b>Username:</b> " + server.getUsername() + "<br/><hr/>" + "<b>Status:</b> " + server.getStatus().name().toLowerCase() + "<br/><hr/>" + ((server.getErrorMessage() != null) ? "<b>Error:</b> " + server.getErrorMessage() + "<br/><hr/>" : "") + "</center></html>");
    }
}
