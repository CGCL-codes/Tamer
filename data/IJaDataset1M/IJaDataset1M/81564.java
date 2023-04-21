package org.mmt.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.mmt.core.MysqlServer;
import org.mmt.core.ServersManager;

public class ServerGroupDataEditorDialog extends JDialog implements ActionListener, ListSelectionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String serverGroup;

    private boolean okPressed = false;

    private boolean wasDefaultServerGroup = false;

    private JList availableServersList = new JList();

    private JList selectedServersList = new JList();

    private JButton addServer = new JButton(">");

    private JButton removeServer = new JButton("<");

    private JTextField serverGroupNameField = new JTextField(20);

    private JButton okButton = new JButton();

    private JButton cancelButton = new JButton("Cancel");

    private JCheckBox defaultServerGroupCheckBox = new JCheckBox("Make this my default server group");

    private List<MysqlServer> availableServers = new ArrayList<MysqlServer>();

    private List<MysqlServer> selectedServers = new ArrayList<MysqlServer>();

    public ServerGroupDataEditorDialog() {
        this(null);
    }

    public ServerGroupDataEditorDialog(String serverGroup) {
        this.serverGroup = serverGroup;
        setModal(true);
        setIconImage(new ImageIcon(ClassLoader.getSystemResource("resources/icon.gif")).getImage());
        setSize(500, 250);
        setResizable(false);
        Dimension dim = getToolkit().getScreenSize();
        Rectangle abounds = getBounds();
        setLocation((dim.width - abounds.width) / 2, (dim.height - abounds.height) / 2);
        JPanel serverGroupNamePanel = new JPanel();
        serverGroupNamePanel.add(new JLabel("Server group name: "));
        serverGroupNamePanel.add(serverGroupNameField);
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        String defaultServerGroup = null;
        try {
            defaultServerGroup = Application.getProperty("defaultServerGroup", null);
        } catch (Exception e) {
            Application.showError("Unable to read the user setting for the default server group: " + e.getMessage());
        }
        if (serverGroup == null) {
            setTitle("Create new Server group");
            okButton.setText("Add server group");
            availableServers.addAll(Arrays.asList(ServersManager.getInstance().getAllServers()));
            defaultServerGroupCheckBox.setSelected(false);
        } else {
            setTitle("Edit Server group " + serverGroup);
            okButton.setText("Save");
            selectedServers.addAll(Arrays.asList(ServersManager.getInstance().getServersByGroup(serverGroup)));
            MysqlServer[] servers = ServersManager.getInstance().getAllServers();
            for (MysqlServer server : servers) {
                if (!selectedServers.contains(server)) availableServers.add(server);
            }
            serverGroupNameField.setText(serverGroup);
            if (defaultServerGroup != null && defaultServerGroup.equals(serverGroup)) {
                defaultServerGroupCheckBox.setSelected(true);
                wasDefaultServerGroup = true;
            } else defaultServerGroupCheckBox.setSelected(false);
        }
        refreshLists();
        availableServersList.addListSelectionListener(this);
        selectedServersList.addListSelectionListener(this);
        addServer.addActionListener(this);
        removeServer.addActionListener(this);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);
        addServer.setEnabled(false);
        removeServer.setEnabled(false);
        availableServersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        availableServersList.setLayoutOrientation(JList.VERTICAL);
        selectedServersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        selectedServersList.setLayoutOrientation(JList.VERTICAL);
        JPanel dataControl = new JPanel();
        dataControl.setLayout(new BoxLayout(dataControl, BoxLayout.PAGE_AXIS));
        dataControl.add(addServer);
        dataControl.add(removeServer);
        JPanel availablePanel = new JPanel();
        availablePanel.setLayout(new BorderLayout());
        availablePanel.setBorder(BorderFactory.createTitledBorder("Available servers"));
        availablePanel.add(new JScrollPane(availableServersList), BorderLayout.CENTER);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        dataPanel.add(availablePanel, c);
        c.gridx = 1;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.1;
        dataPanel.add(dataControl, c);
        JPanel selectedPanel = new JPanel();
        selectedPanel.setLayout(new BorderLayout());
        selectedPanel.setBorder(BorderFactory.createTitledBorder("Selected servers"));
        selectedPanel.add(new JScrollPane(selectedServersList), BorderLayout.CENTER);
        c.gridx = 2;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        dataPanel.add(selectedPanel, c);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        JPanel boxPanel = new JPanel();
        boxPanel.add(defaultServerGroupCheckBox);
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(boxPanel);
        southPanel.add(buttonPanel);
        setLayout(new BorderLayout());
        add(serverGroupNamePanel, BorderLayout.NORTH);
        add(dataPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
        add(new JLabel(new ImageIcon(ClassLoader.getSystemResource("resources/server.png"))), BorderLayout.WEST);
    }

    private void refreshLists() {
        availableServersList.setListData(availableServers.toArray());
        selectedServersList.setListData(selectedServers.toArray());
    }

    public static void main(String[] args) {
        new ServerGroupDataEditorDialog().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addServer) {
            MysqlServer server = (MysqlServer) availableServersList.getSelectedValue();
            if (server != null) {
                availableServers.remove(server);
                selectedServers.add(server);
                refreshLists();
            }
        } else if (e.getSource() == removeServer) {
            MysqlServer server = (MysqlServer) selectedServersList.getSelectedValue();
            if (server != null) {
                selectedServers.remove(server);
                availableServers.add(server);
                refreshLists();
            }
        } else if (e.getSource() == okButton) {
            if (isDataValid()) {
                serverGroup = serverGroupNameField.getText().trim();
                okPressed = true;
                if (defaultServerGroupCheckBox.isSelected() && !wasDefaultServerGroup) {
                    try {
                        Application.setProperty("defaultServerGroup", serverGroup);
                    } catch (Exception e1) {
                        Application.showError("Unable to save the user preference for the default server group: " + e1.getMessage());
                    }
                } else if (wasDefaultServerGroup && !defaultServerGroupCheckBox.isSelected()) {
                    try {
                        Application.setProperty("defaultServerGroup", "");
                    } catch (Exception e1) {
                        Application.showError("Unable to save the user preference for the default server group: " + e1.getMessage());
                    }
                }
                setVisible(false);
            }
        } else if (e.getSource() == cancelButton) {
            serverGroup = null;
            selectedServers = null;
            setVisible(false);
        }
    }

    private boolean isDataValid() {
        if (serverGroupNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "The server group name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        String[] serverGroups = ServersManager.getInstance().getServerGroups();
        for (String sg : serverGroups) {
            if (serverGroup != null && serverGroup.equals(sg)) continue;
            if (serverGroupNameField.getText().trim().equals(sg)) {
                JOptionPane.showMessageDialog(this, "There is already another server group named " + serverGroupNameField.getText().trim(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if (selectedServers.size() <= 0) {
            JOptionPane.showMessageDialog(this, "Select at least one server for this server group", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == availableServersList) {
            addServer.setEnabled(!availableServersList.isSelectionEmpty());
        } else if (e.getSource() == selectedServersList) {
            removeServer.setEnabled(!selectedServersList.isSelectionEmpty());
        }
    }

    public String getServerGroup() {
        return serverGroup;
    }

    public List<MysqlServer> getSelectedServers() {
        return selectedServers;
    }

    public boolean isOkPressed() {
        return okPressed;
    }
}
