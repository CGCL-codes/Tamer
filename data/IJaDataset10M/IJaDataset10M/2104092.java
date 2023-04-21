package org.skunk.dav.client.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.skunk.dav.client.gui.Explorer;
import org.skunk.dav.client.gui.ExplorerMenuBar;
import org.skunk.dav.client.gui.ResourceManager;
import org.skunk.dav.client.gui.ServerData;
import org.skunk.dav.client.gui.StateMonitor;
import org.skunk.dav.client.gui.StateProperties;

public class DisconnectionAction extends AbstractAction {

    final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(this.getClass());

    Explorer ex;

    ServerData sd;

    public DisconnectionAction(Explorer ex, ServerData sd) {
        this.ex = ex;
        this.sd = sd;
    }

    public DisconnectionAction(Explorer ex) {
        this(ex, null);
    }

    public void actionPerformed(ActionEvent ae) {
        if (sd != null) disconnect(sd); else if (ex.getConnectedServerCount() == 1) {
            log.trace("only one connected server, will disconnect");
            ServerData[] allServers = ex.getConnectedServers();
            assert allServers.length == 1 : "length of array of connected servers is 1";
            disconnect(allServers[0]);
        } else {
            log.trace("finding selected connection");
            ServerData selectedServer = ex.getSelectedConnection();
            if (selectedServer != null) disconnect(selectedServer); else {
                String message = ResourceManager.getMessage(ResourceManager.DISCONNECTION_ERROR_MESSAGE);
                String title = ResourceManager.getMessage(ResourceManager.DISCONNECTION_ERROR_TITLE);
                JOptionPane.showMessageDialog(ex, message, title, JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void disconnect(ServerData sd) {
        ex.removeConnectionNode(sd);
        if (ex.getConnectedServerCount() == 0) {
            StateMonitor.setProperty(StateProperties.CONNECTED, new Boolean(false), ex);
        }
    }
}
