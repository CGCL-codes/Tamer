package com.markpiper.tvtray.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.prefs.Preferences;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import com.markpiper.tvtray.ChannelManager;
import com.markpiper.tvtray.PrefsChannelListModel;

/**
 * @author mark
 */
public class PreferencesWindow extends JFrame {

    private Preferences prefs;

    private Properties props;

    private ChannelManager channelManager;

    private JButton btn_ok;

    private JButton btn_cancel;

    private PrefsChannelListModel listModel;

    private JTable channelList;

    private JTextField baseurlField;

    private CardLayout cardLayout = new CardLayout();

    private JPanel rightCardPanel;

    private JCheckBox hideNotOnAir;

    private JCheckBox useProxy;

    private JTextField proxyHost;

    private JTextField proxyPort;

    private JCheckBox useProxyUser;

    private JTextField proxyUser;

    private JPasswordField proxyPassword;

    public static final String PREFS_BASE_URL = "baseURL";

    public static final String PREFS_USE_PROXY = "useProxy";

    public static final String PREFS_PROXY_HOST = "proxyHost";

    public static final String PREFS_PROXY_PORT = "proxyPort";

    public static final String PREFS_USE_PROXY_USER = "useProxyUser";

    public static final String PREFS_PROXY_USER = "proxyUser";

    public static final String PREFS_PROXY_PASSWORD = "proxyPassword";

    public static final String PREFS_HIDE_NOA = "hideNotOnAir";

    public PreferencesWindow(ChannelManager channelManager) {
        this.channelManager = channelManager;
        prefs = Preferences.userNodeForPackage(this.getClass());
        setUp();
        pack();
        setVisible(true);
    }

    public void addButtonListener(ActionListener l) {
        btn_ok.addActionListener(l);
        btn_cancel.addActionListener(l);
    }

    public PrefsChannelListModel getListModel() {
        return listModel;
    }

    public String getBaseURL() {
        return baseurlField.getText();
    }

    public boolean getHideNotOnAir() {
        return hideNotOnAir.isSelected();
    }

    private void setUp() {
        Toolkit kit = Toolkit.getDefaultToolkit();
        setIconImage(kit.createImage("images/tv.gif"));
        setTitle("Preferences");
        setLayout(new BorderLayout());
        JPanel leftPanel = new JPanel(new GridLayout(1, 0));
        leftPanel.setPreferredSize(new Dimension(100, 300));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JPanel realRightPanel = new JPanel(new BorderLayout());
        rightCardPanel = new JPanel();
        rightCardPanel.setLayout(cardLayout);
        rightCardPanel.setPreferredSize(new Dimension(300, 300));
        JPanel channelPanel = new JPanel(new BorderLayout());
        channelPanel.setPreferredSize(new Dimension(300, 300));
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("root");
        DefaultMutableTreeNode channelNode = new DefaultMutableTreeNode("Channels");
        DefaultMutableTreeNode otherNode = new DefaultMutableTreeNode("Other");
        DefaultMutableTreeNode httpNode = new DefaultMutableTreeNode("HTTP");
        rootNode.add(channelNode);
        rootNode.add(otherNode);
        rootNode.add(httpNode);
        JTree tree = new JTree(rootNode);
        tree.setRootVisible(false);
        tree.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);
        tree.setCellRenderer(renderer);
        tree.setSize(100, 300);
        tree.addTreeSelectionListener(new PrefsTreeListener());
        leftPanel.add(tree);
        JPanel channelListPanel = new JPanel(new BorderLayout());
        JPanel baseURLPanel = new JPanel(new BorderLayout());
        baseurlField = new JTextField();
        baseURLPanel.add(new JLabel("Base URL:"), BorderLayout.WEST);
        baseURLPanel.add(baseurlField, BorderLayout.CENTER);
        baseURLPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        channelListPanel.add(baseURLPanel, BorderLayout.NORTH);
        channelListPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Available Channels"));
        listModel = new PrefsChannelListModel(channelManager);
        TableSorter sorter = new TableSorter(listModel);
        channelList = new JTable(sorter);
        sorter.setTableHeader(channelList.getTableHeader());
        channelList.setEnabled(true);
        JScrollPane channelScroll = new JScrollPane(channelList);
        channelListPanel.add(channelScroll, BorderLayout.CENTER);
        JPanel updownPanel = new JPanel();
        GridLayout gl = new GridLayout(2, 0);
        gl.setVgap(5);
        updownPanel.setLayout(gl);
        updownPanel.setPreferredSize(new Dimension(20, 50));
        updownPanel.setMaximumSize(new Dimension(50, 100));
        Dimension ud_btnSize = new Dimension(20, 20);
        ImageIcon upIcon = new ImageIcon(kit.createImage("images/up.gif"));
        ImageIcon downIcon = new ImageIcon(kit.createImage("images/down.gif"));
        JButton moveUp = new JButton(upIcon);
        moveUp.setActionCommand("Up");
        moveUp.setPreferredSize(ud_btnSize);
        MoveUpDownListener mudListener = new MoveUpDownListener();
        moveUp.addActionListener(mudListener);
        JButton moveDown = new JButton(downIcon);
        moveDown.setActionCommand("Down");
        moveDown.addActionListener(mudListener);
        moveDown.setPreferredSize(ud_btnSize);
        updownPanel.add(moveUp);
        updownPanel.add(moveDown);
        channelListPanel.add(updownPanel, BorderLayout.EAST);
        channelPanel.add(channelListPanel, BorderLayout.CENTER);
        rightCardPanel.add("[root, Channels]", channelPanel);
        JPanel otherPanel = new JPanel(new BorderLayout());
        JPanel optionsPanel = new JPanel();
        FlowLayout otherFl = new FlowLayout();
        otherFl.setAlignment(FlowLayout.LEFT);
        optionsPanel.setLayout(otherFl);
        optionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Other Options"));
        hideNotOnAir = new JCheckBox("Hide channels Not On Air");
        optionsPanel.add(hideNotOnAir);
        otherPanel.add(optionsPanel, BorderLayout.CENTER);
        JPanel httpPanel = new JPanel(new BorderLayout());
        JPanel httpOptsPanel = new JPanel();
        GridLayout httpGl = new GridLayout(6, 2);
        httpGl.setVgap(10);
        httpOptsPanel.setLayout(httpGl);
        httpOptsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "HTTP Options"));
        useProxy = new JCheckBox("Use a proxy server");
        JLabel space1 = new JLabel();
        JLabel proxyHostlbl = new JLabel("Host:");
        proxyHost = new JTextField();
        JLabel proxyPortlbl = new JLabel("Port:");
        proxyPort = new JTextField();
        useProxyUser = new JCheckBox("Requires authentication");
        JLabel space2 = new JLabel();
        JLabel proxyUserlbl = new JLabel("User:");
        proxyUser = new JTextField();
        JLabel proxyPasswordlbl = new JLabel("Password:");
        proxyPassword = new JPasswordField();
        httpOptsPanel.add(useProxy);
        httpOptsPanel.add(space1);
        httpOptsPanel.add(proxyHostlbl);
        httpOptsPanel.add(proxyHost);
        httpOptsPanel.add(proxyPortlbl);
        httpOptsPanel.add(proxyPort);
        httpOptsPanel.add(useProxyUser);
        httpOptsPanel.add(space2);
        httpOptsPanel.add(proxyUserlbl);
        httpOptsPanel.add(proxyUser);
        httpOptsPanel.add(proxyPasswordlbl);
        httpOptsPanel.add(proxyPassword);
        proxyHost.setEnabled(false);
        proxyPort.setEnabled(false);
        useProxyUser.setEnabled(false);
        proxyUser.setEnabled(false);
        proxyPassword.setEnabled(false);
        useProxy.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    proxyHost.setEnabled(true);
                    proxyPort.setEnabled(true);
                    useProxyUser.setEnabled(true);
                    if (useProxyUser.isSelected()) {
                        proxyUser.setEnabled(true);
                        proxyPassword.setEnabled(true);
                    }
                } else {
                    proxyHost.setEnabled(false);
                    proxyPort.setEnabled(false);
                    useProxyUser.setEnabled(false);
                    proxyUser.setEnabled(false);
                    proxyPassword.setEnabled(false);
                }
            }
        });
        useProxyUser.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    proxyUser.setEnabled(true);
                    proxyPassword.setEnabled(true);
                } else {
                    proxyUser.setEnabled(false);
                    proxyPassword.setEnabled(false);
                }
            }
        });
        httpPanel.add(httpOptsPanel, BorderLayout.CENTER);
        rightCardPanel.add("[root, HTTP]", httpPanel);
        FlowLayout f = new FlowLayout();
        f.setAlignment(FlowLayout.RIGHT);
        JPanel buttonPanel = new JPanel(f);
        buttonPanel.setPreferredSize(new Dimension(300, 40));
        Dimension btnSize = new Dimension(68, 22);
        btn_ok = new JButton("OK");
        btn_ok.setActionCommand("Prefs OK");
        btn_ok.setPreferredSize(btnSize);
        btn_cancel = new JButton("Cancel");
        btn_cancel.setActionCommand("Prefs Cancel");
        btn_cancel.setPreferredSize(btnSize);
        buttonPanel.add(btn_ok);
        buttonPanel.add(btn_cancel);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        rightCardPanel.add("[root, Other]", otherPanel);
        realRightPanel.add(rightCardPanel, BorderLayout.CENTER);
        realRightPanel.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(leftPanel, BorderLayout.WEST);
        getContentPane().add(realRightPanel, BorderLayout.CENTER);
        loadDefaults();
        pack();
    }

    private Properties getProperties() {
        props = new Properties();
        try {
            props.load(new FileInputStream("channels.txt"));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Cannot find the Channel list", "TVTray Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return props;
    }

    private void loadDefaults() {
        baseurlField.setText(prefs.get(PREFS_BASE_URL, "http://www.bleb.org/tv/data/listings/0/"));
        hideNotOnAir.setSelected(prefs.getBoolean(PREFS_HIDE_NOA, false));
        useProxy.setSelected(prefs.getBoolean(PREFS_USE_PROXY, false));
        proxyHost.setText(prefs.get(PREFS_PROXY_HOST, ""));
        proxyPort.setText(prefs.get(PREFS_PROXY_PORT, ""));
        useProxyUser.setSelected(prefs.getBoolean(PREFS_USE_PROXY_USER, false));
        proxyUser.setText(prefs.get(PREFS_PROXY_USER, ""));
        proxyPassword.setText(prefs.get(PREFS_PROXY_PASSWORD, ""));
    }

    public void savePreferences() {
        prefs.put(PREFS_BASE_URL, baseurlField.getText());
        prefs.putBoolean(PREFS_HIDE_NOA, hideNotOnAir.isSelected());
        prefs.putBoolean(PREFS_USE_PROXY, useProxy.isSelected());
        prefs.putBoolean(PREFS_USE_PROXY_USER, useProxyUser.isSelected());
        prefs.put(PREFS_PROXY_HOST, proxyHost.getText());
        prefs.put(PREFS_PROXY_PORT, proxyPort.getText());
        prefs.put(PREFS_PROXY_USER, proxyUser.getText());
        String pwd = new String(proxyPassword.getPassword());
        prefs.put(PREFS_PROXY_PASSWORD, pwd);
    }

    private class MoveUpDownListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            int selRow = channelList.getSelectedRow();
            if (selRow == -1) {
                return;
            }
            if (evt.getActionCommand().equals("Up")) {
                if (selRow != 0) {
                    listModel.swapRows(selRow, selRow - 1);
                    channelList.getSelectionModel().setSelectionInterval(selRow - 1, selRow - 1);
                }
            }
            if (evt.getActionCommand().equals("Down")) {
                if (selRow != channelList.getRowCount() - 1) {
                    listModel.swapRows(selRow, selRow + 1);
                    channelList.getSelectionModel().setSelectionInterval(selRow + 1, selRow + 1);
                }
            }
            channelList.repaint();
        }
    }

    private class PrefsTreeListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent evt) {
            cardLayout.show(rightCardPanel, evt.getNewLeadSelectionPath().toString());
        }
    }
}
