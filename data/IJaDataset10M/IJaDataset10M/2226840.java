package jnocatan.panel;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * JnoCatanConnectDialog
 *
 * @author  Don Seiler <don@NOSPAM.seiler.us>
 * @version $Id: JnoCatanConnectPanel.java,v 1.1 2004/10/21 19:11:27 rizzo Exp $
 * @since   0.1.0
 */
public class JnoCatanConnectPanel extends JPanel {

    public static final String CONNECT_PRIVATE = "conn_private";

    public static final String CONNECT_PUBLIC = "conn_public";

    JLabel label;

    JFrame frame;

    public JComboBox serverList;

    public JTextField serverPortField;

    public JButton privateConnectButton;

    public JTextField metaserverField;

    public JButton publicConnectButton;

    String publicTabLabel = "Join Public Game";

    String publicTabToolTip = "Join a public gnocatan game found on the meta-server.";

    String privateTabLabel = "Join Private Game";

    String privateTabToolTip = "Join a private gnocatan game";

    /**
     * Creates connection dialog and handles actions
     *
     * @access  private
     */
    public JnoCatanConnectPanel(Frame aFrame) {
        super(new BorderLayout());
        this.frame = frame;
        JPanel publicGamePanel = createPublicDialogBox();
        JPanel privateGamePanel = createPrivateDialogBox();
        label = new JLabel("Choose meta server or game server connect!");
        Border padding = BorderFactory.createEmptyBorder(20, 20, 5, 20);
        publicGamePanel.setBorder(padding);
        privateGamePanel.setBorder(padding);
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(privateTabLabel, null, privateGamePanel, privateTabToolTip);
        add(tabbedPane, BorderLayout.CENTER);
        add(label, BorderLayout.PAGE_END);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    /** Sets the text displayed in the frame */
    void setLabel(String newText) {
        label.setText(newText);
    }

    private JPanel createPublicDialogBox() {
        String defaultMetaServer = "gnocatan.debian.net";
        metaserverField = new JTextField(defaultMetaServer, 10);
        String metaserverLabel = "Meta Server";
        publicConnectButton = new JButton("Connect!");
        publicConnectButton.setActionCommand(CONNECT_PUBLIC);
        JPanel box = new JPanel();
        JLabel label = new JLabel("Connect to Meta Server");
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        box.add(label);
        box.add(metaserverField);
        JPanel pane = new JPanel(new BorderLayout());
        pane.add(box, BorderLayout.PAGE_START);
        pane.add(publicConnectButton, BorderLayout.PAGE_END);
        return pane;
    }

    private JPanel createPrivateDialogBox() {
        String[] servers = { "localhost" };
        serverList = new JComboBox(servers);
        serverList.setEditable(true);
        serverPortField = new JTextField("5556", 1);
        JLabel portLabel = new JLabel("Port");
        String serverLabel = "Game Server";
        privateConnectButton = new JButton("Connect!");
        privateConnectButton.setActionCommand(CONNECT_PRIVATE);
        JPanel box = new JPanel();
        JLabel label = new JLabel("Connect to Game Server");
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        box.add(label);
        box.add(serverList);
        box.add(portLabel);
        box.add(serverPortField);
        JPanel pane = new JPanel(new BorderLayout());
        pane.add(box, BorderLayout.PAGE_START);
        pane.add(privateConnectButton, BorderLayout.PAGE_END);
        return pane;
    }
}
