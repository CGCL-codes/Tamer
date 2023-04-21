package swingextras.gui;

import org.communications.CommunicationManager.STATUS;
import javax.swing.Icon;
import swingextras.icons.IconManager;

/**
 * A panel that displays connection information from a connection manager
 * @author Joao Leal
 */
public abstract class ConnectionInformationPanel<ConInfo> extends javax.swing.JPanel {

    private boolean displayStatusIcon = true;

    /**
     * Handles a connection status changes of the database manager
     * @param status the connection status
     */
    public void setConnectionStatus(STATUS status) {
        if (status == null) {
            status = STATUS.DISCONNECTED;
        }
        boolean isConnected = status == STATUS.CONNECTED;
        Icon statusIcon = null;
        if (displayStatusIcon) {
            statusIcon = IconManager.getIcon("16x16/" + getStatusIcon(status));
        }
        connectionChanged(isConnected, status, statusIcon);
    }

    public boolean isDisplayStatusIcon() {
        return displayStatusIcon;
    }

    public void setDisplayStatusIcon(boolean displayStatusIcon) {
        this.displayStatusIcon = displayStatusIcon;
    }

    /**
     * Triggered when the connection status has changed
     * @param isConnected Whether or not the manager is connected
     * @param status The current connection status
     * @param statusIcon The icon used to display the connection status
     */
    protected abstract void connectionChanged(boolean isConnected, STATUS status, Icon statusIcon);

    /**
     * Sets the connection information
     * @param info the connection information
     */
    public abstract void setConnectionInformation(ConInfo info);

    public static String getStatusIcon(STATUS status) {
        String iconStatus = null;
        switch(status) {
            case CONNECTING:
                iconStatus = "connect_creating.png";
                break;
            case CONNECTED:
                iconStatus = "connect_established.png";
                break;
            case RECONNECTING:
                iconStatus = "connect_reestablishing.png";
                break;
            case DISCONNECTING:
                iconStatus = "connect_no.png";
                break;
            case DISCONNECTED:
                iconStatus = "connect_no.png";
        }
        return "actions/" + iconStatus;
    }
}
