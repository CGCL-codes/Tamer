package ces.platform.infoplat.service.ftpserver.ftp.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import ces.platform.infoplat.utils.gui.GuiUtils;
import ces.platform.infoplat.utils.io.IoUtils;
import ces.platform.infoplat.service.ftpserver.ftp.FtpConfig;
import ces.platform.infoplat.service.ftpserver.ftp.FtpUser;

/**
 * This panel holds all connection spy panels.
 *
 * @author <a href="mailto:rana_b@yahoo.com">Rana Bhattacharyya</a>
 */
public class FtpSpyContainerPanel extends PluginPanel implements ChangeListener {

    public static final String CLEAR_IMG = "ces/platform/infoplat/service/ftpserver/res/img/clear.gif";

    public static final String CLOSE_IMG = "ces/platform/infoplat/service/ftpserver/res/img/close.gif";

    public static final String DISCONNECT_IMG = "ces/platform/infoplat/service/ftpserver/res/img/disconnect.gif";

    public static final String SPY_PAGE = "ces/platform/infoplat/service/ftpserver/res/img/spy.html";

    private JTabbedPane mjTabbedPane = null;

    private JButton mjClearButton = null;

    private JButton mjCloseButton = null;

    private JButton mjDisconnectButton = null;

    private JScrollPane mjAboutPane = null;

    private FtpConfig mConfig = null;

    /**
     * Constructor - create empty tabbed frame
     */
    public FtpSpyContainerPanel(FtpTree tree) {
        super(tree);
        initComponents();
    }

    /**
     * Initialize all components
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        mjTabbedPane = new JTabbedPane();
        mjTabbedPane.setPreferredSize(new Dimension(470, 340));
        mjTabbedPane.addChangeListener(this);
        add(mjTabbedPane, BorderLayout.CENTER);
        JPanel bottomPane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        mjClearButton = new JButton("Clear", GuiUtils.createImageIcon(CLEAR_IMG));
        bottomPane.add(mjClearButton);
        mjClearButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                clearLog();
            }
        });
        mjDisconnectButton = new JButton("Disconnect", GuiUtils.createImageIcon(DISCONNECT_IMG));
        bottomPane.add(mjDisconnectButton);
        mjDisconnectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                disconnectUser();
            }
        });
        mjCloseButton = new JButton("Close", GuiUtils.createImageIcon(CLOSE_IMG));
        bottomPane.add(mjCloseButton);
        mjCloseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                closePane();
            }
        });
        add(bottomPane, BorderLayout.SOUTH);
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        InputStream is = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(SPY_PAGE);
            if (is != null) {
                editorPane.read(is, null);
            }
        } catch (IOException ex) {
        } finally {
            IoUtils.close(is);
        }
        mjAboutPane = new JScrollPane(editorPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mjTabbedPane.addTab("Spy", mjAboutPane);
    }

    /**
     * Clear user log
     */
    private void clearLog() {
        Component selComp = mjTabbedPane.getSelectedComponent();
        if ((selComp != null) && (selComp != mjAboutPane)) {
            ((SpyPanel) selComp).clearLog();
        }
    }

    /**
     * Close connection spy panel.
     */
    private void closePane() {
        Component selComp = mjTabbedPane.getSelectedComponent();
        if ((selComp != null) && (selComp != mjAboutPane)) {
            ((SpyPanel) selComp).closePane();
            mjTabbedPane.remove(selComp);
            if (mjTabbedPane.getTabCount() == 0) {
                mjTabbedPane.addTab("Spy", mjAboutPane);
            }
        }
    }

    /**
     * Disconnected user connection
     */
    private void disconnectUser() {
        Component selComp = mjTabbedPane.getSelectedComponent();
        if ((selComp != null) && (selComp != mjAboutPane)) {
            boolean bConf = GuiUtils.getConfirmation(getTree().getTopFrame(), "Do you want to close the connection?");
            if (bConf) {
                ((SpyPanel) selComp).disconnect();
            }
        }
    }

    /**
     * Monitor connection
     */
    public void monitorConnection(FtpUser user) {
        String userName = getCaption(user);
        String userSession = user.getSessionId();
        int tabCount = mjTabbedPane.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            Component selComp = mjTabbedPane.getComponentAt(i);
            if ((selComp != null) && (selComp != mjAboutPane)) {
                String tabUserSessionId = ((SpyPanel) selComp).getSessionId();
                if (tabUserSessionId.equals(userSession)) {
                    mjTabbedPane.setTitleAt(i, userName);
                    mjTabbedPane.setSelectedIndex(i);
                    return;
                }
            }
        }
        SpyPanel spyPane = new SpyPanel(mConfig, userSession);
        mjTabbedPane.remove(mjAboutPane);
        mjTabbedPane.add(userName, spyPane);
        mjTabbedPane.setSelectedComponent(spyPane);
    }

    /**
     * Get tab caption.
     */
    private String getCaption(FtpUser user) {
        String name = "";
        if (user != null) {
            name = user.getName();
            if (name == null) {
                name = "UNKNOWN";
            }
        }
        return name;
    }

    /**
     * Tab change notification
     */
    public void stateChanged(ChangeEvent e) {
        Component selComp = mjTabbedPane.getSelectedComponent();
        boolean isUserPane = selComp != mjAboutPane;
        mjClearButton.setEnabled(isUserPane);
        mjCloseButton.setEnabled(isUserPane);
        mjDisconnectButton.setEnabled(isUserPane);
    }

    /**
     * Refresh panel
     */
    public void refresh(FtpConfig config) {
        mConfig = config;
        int tabCount = mjTabbedPane.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            Component selComp = mjTabbedPane.getComponentAt(i);
            if ((selComp != null) && (selComp != mjAboutPane)) {
                ((SpyPanel) selComp).closePane();
                mjTabbedPane.remove(selComp);
            }
        }
        if (mjTabbedPane.getTabCount() == 0) {
            mjTabbedPane.addTab("Spy", mjAboutPane);
        }
    }

    /**
     * Not displayable when server stopped
     */
    public boolean isDisplayable() {
        return mConfig != null;
    }
}
