package customer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import customer.GUI.NetPlayFrameFactory;
import customer.GUI.components.NetPlayCustomStatusBar;
import customer.GUI.components.panels.NetPlayCustomPanel;
import customer.core.InitManager;

public class NetPlayGGXCustomer {

    private static JFrame mainFrame;

    private static NetPlayCustomPanel panelMain;

    private static NetPlayCustomStatusBar status;

    /**
	 * Software starting process
	 * Performs argument control
	 * @param args : TODO : information about main args
	 */
    public static void main(String[] args) {
        NetPlayFrameFactory guiController = NetPlayFrameFactory.getInstance();
        mainFrame = guiController.getMainFrame();
        mainFrame.setVisible(true);
        mainFrame.setSize(new Dimension(850, 600));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        status = new NetPlayCustomStatusBar();
        mainFrame.add(status, BorderLayout.SOUTH);
        panelMain = guiController.getMainPanel();
        mainFrame.add(panelMain);
        mainFrame.setJMenuBar(guiController.getMainMenuBar());
        mainFrame.validate();
        InitManager manager = new InitManager();
        manager.init();
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }

    public static void setMainFrame(JFrame mainFrame) {
        NetPlayGGXCustomer.mainFrame = mainFrame;
    }

    public static NetPlayCustomPanel getPanelMain() {
        return panelMain;
    }

    public static void setPanelMain(NetPlayCustomPanel panelMain) {
        NetPlayGGXCustomer.panelMain = panelMain;
    }

    public static NetPlayCustomStatusBar getStatus() {
        return status;
    }

    public static void setStatus(NetPlayCustomStatusBar status) {
        NetPlayGGXCustomer.status = status;
    }
}
