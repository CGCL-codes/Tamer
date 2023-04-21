package ranab.server.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import ranab.gui.GuiUtils;
import ranab.server.ServerEngine;

/**
 * Generic server admin gui. We should be able to 
 * add any server gui panel here.
 *
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
 */
public class ServerFrame extends JFrame {

    private static final ImageIcon ICON_IMG = GuiUtils.createImageIcon("ranab/server/gui/server.gif");

    private JTabbedPane mjTabPane;

    private ServerPane[] mjPanelArray = { new ranab.server.ftp.gui.FtpPanel() };

    /** 
     * Creates new form MyServerFrame 
     */
    public ServerFrame() {
        initComponents();
        pack();
        setTitle("Server");
        if (ICON_IMG != null) {
            setIconImage(ICON_IMG.getImage());
        }
        setSize(new Dimension(605, 450));
        GuiUtils.setLocation(this);
        show();
        toFront();
    }

    /** 
     * This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        mjTabPane = new JTabbedPane();
        for (int i = 0; i < mjPanelArray.length; i++) {
            mjTabPane.addTab(mjPanelArray[i].getServerName(), mjPanelArray[i].getPanel());
        }
        getContentPane().add(mjTabPane, BorderLayout.CENTER);
    }

    protected void processWindowEvent(WindowEvent e) {
        int id = e.getID();
        if (id == WindowEvent.WINDOW_CLOSING) {
            if (!GuiUtils.getConfirmation(this, "Do you really want to exit?")) {
                return;
            }
            super.processWindowEvent(e);
            terminate();
        } else {
            super.processWindowEvent(e);
        }
    }

    /**
     * Terminate application - stop all servers.
     */
    public void terminate() {
        for (int i = 0; i < mjPanelArray.length; i++) {
            ServerEngine serverEngine = mjPanelArray[i].getServerEngine();
            if (serverEngine != null) {
                System.out.println("Stopping " + mjPanelArray[i].getServerName() + "...");
                serverEngine.stopServer();
            }
        }
        dispose();
        System.exit(0);
    }

    /**
     * Server GUI starting point.
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        System.out.println("Opening UI window, please wait...");
        new ServerFrame();
    }
}
