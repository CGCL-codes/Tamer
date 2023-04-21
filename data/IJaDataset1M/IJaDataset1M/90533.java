package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
import TPI.CTpiCommand;
import TPI.CWirelessNetwork;
import TPI.CTpiCommand.CommandActionType;
import core.CProfile;
import core.CWlanManager;
import java.awt.Color;

public class CWindowJoinPreferences extends javax.swing.JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTextField txtIpAddress = new JTextField(30);

    private JTextField txtGatewayAddress = new JTextField(30);

    private JTextField txtSubnetMask = new JTextField(30);

    private JTextField txtDnsServer1 = new JTextField(30);

    private JTextField txtDnsServer2 = new JTextField(30);

    private JTextField txtPasskey = new JTextField(30);

    private JComboBox cobProfiles;

    private JButton butLoadProfile;

    private JTextField txtProfile = new JTextField(30);

    private JButton butSaveProfile;

    private JButton butCancel;

    private JButton butJoin;

    boolean m_bManual = false;

    CWirelessNetwork m_pNet = null;

    JRadioButton btDhcp = new JRadioButton("DHCP");

    JRadioButton btManual = new JRadioButton("Manual");

    public CWindowJoinPreferences(CWirelessNetwork pNetwork) {
        m_pNet = pNetwork;
        String[] sProfiles = CWlanManager.Instance().GetProfileManager().GetProfileVector().GetAllProfileNames();
        if (sProfiles != null) cobProfiles = new JComboBox(sProfiles); else cobProfiles = new JComboBox();
        initComponents();
        DisableIpField();
        setSize(400, 400);
        setTitle("Joining " + pNetwork.essid);
        setAlwaysOnTop(true);
        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        JPanel panProfiles = new JPanel();
        panProfiles.setBorder(javax.swing.BorderFactory.createTitledBorder("Profiles"));
        panProfiles.setLayout(new GridLayout(2, 2, 2, 2));
        panProfiles.add(cobProfiles);
        butLoadProfile = new JButton("Load Profile");
        butLoadProfile.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                LoadProfile((String) cobProfiles.getSelectedItem());
            }

            ;
        });
        panProfiles.add(butLoadProfile);
        panProfiles.add(txtProfile);
        butSaveProfile = new JButton("Save Profile");
        butSaveProfile.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                SaveProfile(txtProfile.getText());
            }

            ;
        });
        panProfiles.add(butSaveProfile);
        getContentPane().add(panProfiles, BorderLayout.NORTH);
        JPanel panIpSettings = new javax.swing.JPanel();
        panIpSettings.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuration Values"));
        panIpSettings.setLayout(new GridLayout(7, 2));
        btDhcp.setActionCommand("dhcp");
        btDhcp.addActionListener(this);
        btDhcp.setSelected(true);
        DisableIpField();
        btManual.setActionCommand("manual");
        btManual.addActionListener(this);
        ButtonGroup btGroup = new ButtonGroup();
        btGroup.add(btDhcp);
        btGroup.add(btManual);
        panIpSettings.add(new JLabel("Wlan Passkey: "));
        panIpSettings.add(txtPasskey);
        panIpSettings.add(btDhcp);
        panIpSettings.add(btManual);
        panIpSettings.add(new JLabel("Ip Address: "));
        panIpSettings.add(txtIpAddress);
        panIpSettings.add(new JLabel("Netmask: "));
        panIpSettings.add(txtSubnetMask);
        panIpSettings.add(new JLabel("Gateway: "));
        panIpSettings.add(txtGatewayAddress);
        panIpSettings.add(new JLabel("DNS Server1: "));
        panIpSettings.add(txtDnsServer1);
        panIpSettings.add(new JLabel("DNS Server2: "));
        panIpSettings.add(txtDnsServer2);
        getContentPane().add(panIpSettings, BorderLayout.CENTER);
        butJoin = new JButton("Join");
        butJoin.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                Join();
                dispose();
            }

            ;
        });
        butCancel = new JButton("Cancel");
        butCancel.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent evt) {
                dispose();
            }

            ;
        });
        JPanel panButtons = new JPanel();
        panButtons.add(butJoin, BorderLayout.WEST);
        panButtons.add(butCancel, BorderLayout.EAST);
        getContentPane().add(panButtons, BorderLayout.SOUTH);
    }

    private void DisableIpField() {
        m_bManual = false;
        txtIpAddress.setBackground(Color.gray);
        txtIpAddress.setEnabled(false);
        txtGatewayAddress.setBackground(Color.gray);
        txtGatewayAddress.setEnabled(false);
        txtSubnetMask.setBackground(Color.gray);
        txtSubnetMask.setEnabled(false);
        txtDnsServer1.setBackground(Color.gray);
        txtDnsServer1.setEnabled(false);
        txtDnsServer2.setBackground(Color.gray);
        txtDnsServer2.setEnabled(false);
    }

    private void EnableIpField() {
        m_bManual = true;
        txtIpAddress.setEnabled(true);
        txtGatewayAddress.setEnabled(true);
        txtSubnetMask.setEnabled(true);
        txtDnsServer1.setEnabled(true);
        txtDnsServer2.setEnabled(true);
        txtPasskey.setBackground(Color.white);
        txtIpAddress.setBackground(Color.white);
        txtGatewayAddress.setBackground(Color.white);
        txtSubnetMask.setBackground(Color.white);
        txtDnsServer1.setBackground(Color.white);
        txtDnsServer2.setBackground(Color.white);
    }

    private void LoadProfile(String sProfileName) {
        CProfile pProfile = CWlanManager.Instance().GetProfileManager().GetProfileVector().GetProfile(sProfileName);
        if (pProfile == null) {
            CWindowMain.GuiError("fatal error while fetching net");
            return;
        }
        if (pProfile.m_eAction == CommandActionType.CONFIGURE_DHCP) {
            DisableIpField();
            btDhcp.setSelected(true);
            btManual.setSelected(false);
        } else {
            EnableIpField();
            btDhcp.setSelected(false);
            btManual.setSelected(true);
        }
        txtProfile.setText(pProfile.m_sTitle);
        txtDnsServer1.setText(pProfile.m_sDnsServer1);
        txtDnsServer2.setText(pProfile.m_sDnsServer2);
        txtGatewayAddress.setText(pProfile.m_sGwAddress);
        txtIpAddress.setText(pProfile.m_sIpAddress);
        txtSubnetMask.setText(pProfile.m_sSubnetAddress);
        txtPasskey.setText(pProfile.m_sAuthKey);
    }

    private void SaveProfile(String sProfileName) {
        CProfile pProfile = new CProfile();
        if (!m_bManual) pProfile.m_eAction = CommandActionType.CONFIGURE_DHCP; else pProfile.m_eAction = CommandActionType.CONFIGURE_MANUAL;
        pProfile.m_sTitle = txtProfile.getText();
        pProfile.m_sDnsServer1 = txtDnsServer1.getText();
        pProfile.m_sDnsServer2 = txtDnsServer2.getText();
        pProfile.m_sGwAddress = txtGatewayAddress.getText();
        pProfile.m_sIpAddress = txtIpAddress.getText();
        pProfile.m_sSubnetAddress = txtSubnetMask.getText();
        pProfile.m_sAuthKey = txtPasskey.getText();
        if (CWlanManager.Instance().GetProfileManager().SaveNewProfile(pProfile)) {
            cobProfiles.addItem(sProfileName);
            cobProfiles.setSelectedItem(sProfileName);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "dhcp") {
            DisableIpField();
        } else EnableIpField();
    }

    private void Join() {
        CTpiCommand pCommand = new CTpiCommand();
        if (!m_bManual) pCommand.m_eAction = CommandActionType.CONFIGURE_DHCP; else pCommand.m_eAction = CommandActionType.CONFIGURE_MANUAL;
        pCommand.m_sDns1Address = txtDnsServer1.getText();
        pCommand.m_sDns2Address = txtDnsServer2.getText();
        pCommand.m_sESSID = m_pNet.essid;
        pCommand.m_sGWAddress = txtGatewayAddress.getText();
        pCommand.m_sIpAddress = txtIpAddress.getText();
        pCommand.m_sNetmask = txtSubnetMask.getText();
        pCommand.m_sPasskey = txtPasskey.getText();
        CWlanManager.Instance().GetTpiWorker().DoCommand(pCommand);
    }
}
