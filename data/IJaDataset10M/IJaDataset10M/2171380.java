package net.sf.magicmap.client.gui.dialogs;

import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.sf.magicmap.client.gui.MainGUI;
import net.sf.magicmap.client.gui.utils.GUIConstants;
import net.sf.magicmap.client.gui.utils.GUIUtils;
import net.sf.magicmap.client.gui.utils.RelativePanelBuilder;
import net.sf.magicmap.client.meta.ServerConnectionInfo;
import net.sf.magicmap.client.utils.Settings;
import com.brunchboy.util.swing.relativelayout.RelativeLayout;

/**
 * Stellt Dialog f�r die Verbindung zu einem Server bereit.
 * @author thuebner
 */
public class ConnectServerDialog extends JDialog implements ActionListener {

    /**
     * serial version id
     */
    private static final long serialVersionUID = 221520511929814197L;

    private JPanel mainPanel;

    private ServerConnectionInfo serverConnectionInfo;

    private JComboBox hostname;

    private JTextField clientname;

    private JTextField port;

    private JPasswordField password;

    private JRadioButton useNoServer;

    private JButton ok;

    private JButton cancel;

    private JRadioButton useServer;

    private JLabel useNoServerLabel;

    private JSeparator seperator;

    private ButtonGroup radioButtonGroup;

    /**
     * Static method to create and open a ConnectServerDialog 
     * @param owner - the owner frame for the dialog
     * @param info - the server connection info
     * @return
     */
    public static ServerConnectionInfo showDialog(Frame owner, ServerConnectionInfo info) {
        ConnectServerDialog d = new ConnectServerDialog(owner, info);
        GUIUtils.locateOnScreen(d);
        d.setModal(true);
        d.setVisible(true);
        return d.getServerConnectionInfo();
    }

    /**
     * Constructor
     * @param owner - owner frame of this dialog (!= null)
     * @param info - server connnection info (!= null)
     */
    public ConnectServerDialog(Frame owner, ServerConnectionInfo info) {
        super(owner, GUIUtils.i18n("connectserver"));
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                cancelConnect();
            }
        });
        setSize(360, 300);
        this.setResizable(false);
        this.serverConnectionInfo = info;
        RelativeLayout layout = new RelativeLayout();
        RelativePanelBuilder builder = new RelativePanelBuilder(layout);
        builder.addDialogHeader("<html><b>" + GUIUtils.i18n("createconnectiontopacwserver") + "</b><br>" + GUIUtils.i18n("createconnectiontopacwserverhint") + "</html>", GUIConstants.ICON_SERVER_BIG, "header");
        builder.setTop("header", 0);
        builder.setLeft("header", 0);
        builder.setRightRightDistance("header", null, 0);
        useNoServer = new JRadioButton();
        builder.add(useNoServer, "noserver");
        useNoServer.setSelected(serverConnectionInfo.useNoServer);
        builder.setLeft("noserver", 20);
        builder.setTopBottomDistance("noserver", "header", 20);
        useNoServer.setActionCommand("USENOSERVER");
        useNoServer.addActionListener(this);
        useNoServerLabel = builder.addLabel(GUIUtils.i18n("nonserverconnect"), "noserverlabel");
        builder.setLeftRightDistance("noserverlabel", "noserver", 10);
        builder.setTopTopDistance("noserverlabel", "noserver", 0);
        seperator = builder.addSeparator("seperator");
        builder.setLeft("seperator", 3);
        builder.setRightRightDistance("seperator", null, -3);
        builder.setTopBottomDistance("seperator", "noserver,noserverlabel", 10);
        useServer = new JRadioButton();
        builder.add(useServer, "server");
        useServer.setSelected(!serverConnectionInfo.useNoServer);
        builder.setLeftLeftDistance("server", "noserver", 0);
        builder.setTopBottomDistance("server", "seperator", 10);
        useServer.setActionCommand("USESERVER");
        useServer.addActionListener(this);
        hostname = builder.addComboBox(Settings.getDefaultServerList(), "hostcombo");
        builder.addLabel(GUIUtils.i18n("hostname"), "hostlabel", hostname);
        builder.setLeftLeftDistance("hostlabel", "noserverlabel", 0);
        hostname.setEditable(true);
        builder.setLeftRightDistance("hostcombo", "hostlabel", 15);
        builder.setRightLeftDistance("hostcombo", "hostcombo", 178);
        builder.setTopTopDistance("hostcombo", "server", 0);
        builder.setTopTopDistance("hostlabel", "server", 0);
        hostname.setActionCommand("HOSTNAME");
        hostname.addActionListener(this);
        hostname.setToolTipText(GUIUtils.i18n("hostnametooltip"));
        port = builder.addTextField("portedit");
        builder.addLabel(GUIUtils.i18n("port"), "portlabel", port);
        builder.setLeftRightDistance("portlabel", "hostcombo", 10);
        builder.setLeftRightDistance("portedit", "portlabel", 5);
        builder.setTopTopDistance("portlabel", "hostlabel,hostcombo", 0);
        builder.setTopTopDistance("portedit", "hostlabel,hostcombo", 0);
        builder.setRightRightDistance("portedit", null, -10);
        port.setToolTipText(GUIUtils.i18n("porttooltip"));
        clientname = builder.addTextField("clientname");
        builder.addLabel(GUIUtils.i18n("clientname"), "clientlabel", clientname);
        builder.setLeftLeftDistance("clientlabel", "hostlabel", 0);
        builder.setLeftLeftDistance("clientname", "hostcombo", 0);
        builder.setRightRightDistance("clientname", "hostcombo", 0);
        builder.setTopBottomDistance("clientlabel", "hostlabel,hostcombo", 10);
        builder.setTopBottomDistance("clientname", "hostlabel,hostcombo", 10);
        clientname.setToolTipText(GUIUtils.i18n("clientnametooltip"));
        password = new JPasswordField();
        builder.add(password, "password");
        builder.addLabel(GUIUtils.i18n("password"), "passwordlabel", password);
        builder.setLeftLeftDistance("passwordlabel", "clientlabel", 0);
        builder.setLeftLeftDistance("password", "clientname", 0);
        builder.setRightRightDistance("password", "hostcombo", 0);
        builder.setTopBottomDistance("passwordlabel", "clientlabel,clientname", 10);
        builder.setTopBottomDistance("password", "clientlabel,clientname", 10);
        password.setToolTipText(GUIUtils.i18n("passwordtooltip"));
        radioButtonGroup = new ButtonGroup();
        radioButtonGroup.add(useNoServer);
        radioButtonGroup.add(useServer);
        if (info.useNoServer) ok = builder.createButton(GUIUtils.i18n("continue"), "OK", this); else ok = builder.createButton(GUIUtils.i18n("connect"), "OK", this);
        cancel = builder.createButton(GUIUtils.i18n("cancel"), "CANCEL", this);
        builder.addOKCancelButtonBar(ok, cancel, "okcancel");
        builder.setLeftLeftDistance("okcancel", null, 10);
        builder.setRightRightDistance("okcancel", null, -10);
        builder.setBottomBottomDistance("okcancel", null, -10);
        this.clientname.setText(serverConnectionInfo.name);
        this.hostname.setSelectedItem(serverConnectionInfo.hostname);
        this.port.setText("" + serverConnectionInfo.port);
        if (info.useNoServer) {
            this.useNoServer.doClick();
        }
        mainPanel = builder.getPanel();
        setContentPane(mainPanel);
        getRootPane().setDefaultButton(ok);
    }

    public void actionPerformed(ActionEvent e) {
        if ("OK".equals(e.getActionCommand())) {
            serverConnectionInfo = new ServerConnectionInfo();
            serverConnectionInfo.hostname = (String) this.hostname.getSelectedItem();
            serverConnectionInfo.password = new String(this.password.getPassword());
            serverConnectionInfo.name = this.clientname.getText();
            serverConnectionInfo.useNoServer = this.useNoServer.isSelected();
            if (useNoServer.isSelected()) {
                if (serverConnectionInfo.name.trim().equalsIgnoreCase("")) serverConnectionInfo.name = System.getProperty("user.name");
                serverConnectionInfo.password = System.getProperty("user.name");
            }
            try {
                serverConnectionInfo.port = Integer.parseInt(this.port.getText());
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(this, GUIUtils.i18n("invalidportnumber"));
                return;
            }
            this.setVisible(false);
        } else if ("CANCEL".equals(e.getActionCommand())) {
            cancelConnect();
        } else if ("USENOSERVER".equals(e.getActionCommand())) {
            boolean b = !useNoServer.isSelected();
            this.hostname.setEnabled(b);
            this.port.setEnabled(b);
            this.clientname.setEnabled(b);
            this.password.setEnabled(b);
            if (!b) {
                this.ok.setText(GUIUtils.filterMnemonic(GUIUtils.i18n("continue")));
                this.ok.setMnemonic(GUIUtils.getMnemonic(GUIUtils.i18n("continue")));
            }
        } else if ("USESERVER".equals(e.getActionCommand())) {
            boolean b = useServer.isSelected();
            this.hostname.setEnabled(b);
            this.port.setEnabled(b);
            this.clientname.setEnabled(b);
            this.password.setEnabled(b);
            if (b) {
                this.ok.setText(GUIUtils.filterMnemonic(GUIUtils.i18n("connect")));
                this.ok.setMnemonic(GUIUtils.getMnemonic(GUIUtils.i18n("connect")));
            }
        } else if ("HOSTNAME".equals(e.getActionCommand())) {
            Settings.setLastSelectedServer(this.hostname.getSelectedIndex());
            this.port.setText(String.valueOf(Settings.getPort()));
        }
    }

    /**
     * local method for cancelling a connect dialog
     */
    private void cancelConnect() {
        serverConnectionInfo = null;
        this.setVisible(false);
        MainGUI.getInstance().getMainFrame().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * get the ServerConnectionInfo
     * @return the serverConnectionInfo 
     */
    public ServerConnectionInfo getServerConnectionInfo() {
        return serverConnectionInfo;
    }

    /**
     * set the ServerConnectionInfo
     * @param serverConnectionInfo - the new serverConnectionInfo
     */
    public void setServerConnectionInfo(ServerConnectionInfo serverConnectionInfo) {
        this.serverConnectionInfo = serverConnectionInfo;
    }
}
