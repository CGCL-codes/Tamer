package customer.GUI;

import javax.swing.JFrame;
import customer.NetPlayGGXCustomer;
import customer.GUI.components.NetPlayCustomMenuBar;
import customer.GUI.components.panels.NetPlayCustomLoginPanel;
import customer.GUI.components.panels.NetPlayCustomPanel;

/**
 * This class gives instances of frames on the fly.
 * 
 * @author NeNiuM
 *
 */
public class NetPlayFrameFactory {

    private static NetPlayFrameFactory instance;

    private NetPlayFrameFactory() {
    }

    public static NetPlayFrameFactory getInstance() {
        if (instance == null) {
            instance = new NetPlayFrameFactory();
        }
        return instance;
    }

    public NetPlayCustomPanel getMainPanel() {
        NetPlayCustomPanel frame = new NetPlayCustomPanel();
        return frame;
    }

    public NetPlayCustomMenuBar getMainMenuBar() {
        return new NetPlayCustomMenuBar();
    }

    public NetPlayCustomLoginPanel getLoginPanel() {
        return new NetPlayCustomLoginPanel(NetPlayGGXCustomer.getMainFrame());
    }

    public JFrame getMainFrame() {
        return new JFrame("GGX Slip NetPlay Client");
    }
}
