package com.directthought.elasticweb.client;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public class EWPreferencesDialog extends JDialog {

    public static final String OK_COMMAND = "OK";

    public static final String CANCEL_COMMAND = "Cancel";

    private static final String[] refreshRates = { "5", "10", "30", "60" };

    private JComboBox refreshRate;

    private JTextField accessId;

    private JTextField secretKey;

    private JTextField netticaUsername;

    private JTextField netticaPassword;

    private JTextField domainName;

    private JTextField hostName;

    private JSpinner minServers;

    private JSpinner headroom;

    private JTextField ami;

    private JTextField keypair;

    private JTextField group;

    private Insets INSETS = new Insets(5, 5, 5, 5);

    private EWPreferencesModel model;

    private ActionListener listener;

    public EWPreferencesDialog(Frame owner, EWPreferencesModel model) {
        super(owner, "EW Preferences Configuration", true);
        this.model = model;
        buildUI();
        pack();
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dialog = getSize();
        setLocation((scr.width - dialog.width) / 2, (scr.height - dialog.height) / 2);
    }

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }

    private void buildUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("Refresh Rate:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        int selectedIdx = -1;
        for (int i = 0; i < refreshRates.length; i++) {
            if ((refreshRates[i] + "000").equals(model.refreshRate)) {
                selectedIdx = i;
            }
        }
        if (selectedIdx == -1) {
            String[] rates = new String[refreshRates.length + 1];
            rates[0] = "" + (model.refreshRate / 1000);
            for (int i = 0; i < refreshRates.length; i++) {
                rates[i + 1] = refreshRates[i];
            }
            refreshRate = new JComboBox(rates);
            selectedIdx = 0;
        } else {
            refreshRate = new JComboBox(refreshRates);
        }
        refreshRate.setEditable(true);
        refreshRate.setSelectedIndex(selectedIdx);
        add(refreshRate, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("AWS Access Id:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        accessId = new JTextField(20);
        accessId.setText(model.accessId);
        add(accessId, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("AWS Secret Key:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        secretKey = new JTextField(40);
        secretKey.setText(model.secretKey);
        add(secretKey, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("Nettica Username:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        netticaUsername = new JTextField(20);
        netticaUsername.setText(model.netticaUsername);
        add(netticaUsername, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("Nettica Password:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        netticaPassword = new JTextField(20);
        netticaPassword.setText(model.netticaPassword);
        add(netticaPassword, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("Domain Name:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        domainName = new JTextField(20);
        domainName.setText(model.domainName);
        add(domainName, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("Hostname:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        hostName = new JTextField(20);
        hostName.setText(model.hostName);
        add(hostName, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("Min Servers:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        minServers = new JSpinner(new SpinnerNumberModel(model.minServers, 2, 10, 1));
        add(minServers, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("Capacity Headroom:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        headroom = new JSpinner(new SpinnerNumberModel(model.headroom, 1, 10, 1));
        add(headroom, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("AMI ID:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        ami = new JTextField(20);
        ami.setText(model.ami);
        add(ami, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("Keypair:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        keypair = new JTextField(20);
        keypair.setText(model.keypair);
        add(keypair, gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = INSETS;
        add(new JLabel("Security Group:"), gbc);
        gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        gbc.weightx = 1.0;
        group = new JTextField(20);
        group.setText(model.group);
        add(group, gbc);
        JPanel bPanel = new JPanel();
        JButton ok = new JButton("OK");
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                model.refreshRate = Integer.parseInt(((String) refreshRate.getSelectedItem()).trim()) * 1000;
                model.accessId = accessId.getText();
                model.secretKey = secretKey.getText();
                model.netticaUsername = netticaUsername.getText();
                model.netticaPassword = netticaPassword.getText();
                model.domainName = domainName.getText();
                model.hostName = hostName.getText();
                model.minServers = ((Integer) minServers.getValue()).intValue();
                model.headroom = ((Integer) headroom.getValue()).intValue();
                model.ami = ami.getText();
                model.keypair = keypair.getText();
                model.group = group.getText();
                listener.actionPerformed(new ActionEvent(this, (int) System.currentTimeMillis(), OK_COMMAND));
            }
        });
        bPanel.add(ok);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                listener.actionPerformed(new ActionEvent(this, (int) System.currentTimeMillis(), CANCEL_COMMAND));
            }
        });
        bPanel.add(cancel);
        gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets = INSETS;
        add(bPanel, gbc);
    }
}
