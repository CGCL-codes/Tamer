package net.face2face.ui;

import net.face2face.core.config.Config;
import net.face2face.core.config.Msg;
import net.face2face.core.location.AddressLocation;
import net.face2face.core.location.Location;
import net.face2face.core.location.LocationListener;
import net.face2face.core.location.SimpleAddressLocation;
import net.face2face.core.net.NetworkTools;
import net.face2face.core.plugins.GeolocatorPlugin;
import net.face2face.core.plugins.Plugin;
import net.face2face.core.plugins.PluginsManager;
import net.face2face.ui.map.MapPanel;
import net.face2face.util.i18n.Country;
import net.face2face.util.swing.CloseActionListener;
import net.face2face.util.swing.CloseablePanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

/**
 *
 * @author  Patrice
 */
public class WizardPanel extends CloseablePanel implements LocationListener {

    MapPanel mapPanel;

    /**
     * Creates new form WizardPanel
     */
    public WizardPanel() {
        initComponents();
        initPanels();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        locationButtonGroup = new javax.swing.ButtonGroup();
        setupLineList = new javax.swing.JList();
        jPanel1 = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        continueButton = new javax.swing.JButton();
        skipButton = new javax.swing.JButton();
        finishButton = new javax.swing.JButton();
        setupPanels = new javax.swing.JPanel();
        welcomePanel = new javax.swing.JEditorPane();
        userDetailsPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        countyLabel = new javax.swing.JLabel();
        countryComboBox = new javax.swing.JComboBox();
        cityLabel = new javax.swing.JLabel();
        cityTextField = new javax.swing.JTextField();
        addressLabel = new javax.swing.JLabel();
        addressTextField = new javax.swing.JTextField();
        zipCodeLabel = new javax.swing.JLabel();
        zipCodeTextField = new javax.swing.JTextField();
        jTextPane1 = new javax.swing.JTextPane();
        jTextPane2 = new javax.swing.JTextPane();
        locatorPanel = new javax.swing.JPanel();
        locationPanel = new javax.swing.JPanel();
        resolveCoordinateButton = new javax.swing.JButton();
        hereRadioButton = new javax.swing.JRadioButton();
        siteRadioButton = new javax.swing.JRadioButton();
        locationRadioButton = new javax.swing.JRadioButton();
        latitudeTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        longitudeTextField = new javax.swing.JTextField();
        locationSubPanel = new javax.swing.JPanel();
        countrySitesComboBox = new javax.swing.JComboBox();
        siteComboBox = new javax.swing.JComboBox();
        jTextPane3 = new javax.swing.JTextPane();
        connectionPanel = new javax.swing.JPanel();
        ipAddressLabel = new javax.swing.JLabel();
        ipAddressTextFiel = new javax.swing.JTextField();
        p2pListeningPort = new javax.swing.JLabel();
        portTextField = new javax.swing.JTextField();
        testPortButton = new javax.swing.JButton();
        connectionSpeedLabel = new javax.swing.JLabel();
        connectionSpeedComboBox = new javax.swing.JComboBox();
        autoConnectsCheckBox = new javax.swing.JCheckBox();
        uuidLabel = new javax.swing.JLabel();
        uuidTextField = new javax.swing.JTextField();
        okPanel = new javax.swing.JPanel();
        jTextPane4 = new javax.swing.JTextPane();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setLayout(new java.awt.BorderLayout());
        setMaximumSize(new java.awt.Dimension(700, 800));
        setMinimumSize(new java.awt.Dimension(500, 400));
        setPreferredSize(new java.awt.Dimension(500, 400));
        setupLineList.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Welcome", "User Details", "Location", "Connection", "End" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        setupLineList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        setupLineList.setFocusable(false);
        setupLineList.setOpaque(false);
        add(setupLineList, java.awt.BorderLayout.WEST);
        jPanel1.setMaximumSize(new java.awt.Dimension(800, 50));
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        jPanel1.add(backButton);
        continueButton.setText("Continue");
        continueButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                continueButtonActionPerformed(evt);
            }
        });
        jPanel1.add(continueButton);
        skipButton.setText("Skip");
        skipButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipButtonActionPerformed(evt);
            }
        });
        jPanel1.add(skipButton);
        finishButton.setText("Finish");
        finishButton.setEnabled(false);
        finishButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                finishButtonActionPerformed(evt);
            }
        });
        jPanel1.add(finishButton);
        add(jPanel1, java.awt.BorderLayout.SOUTH);
        setupPanels.setLayout(new java.awt.CardLayout());
        setupPanels.setMaximumSize(new java.awt.Dimension(800, 250));
        setupPanels.setMinimumSize(new java.awt.Dimension(700, 210));
        setupPanels.setPreferredSize(new java.awt.Dimension(700, 210));
        welcomePanel.setText("hello and wecome");
        welcomePanel.setOpaque(false);
        setupPanels.add(welcomePanel, "card1");
        userDetailsPanel.setLayout(new java.awt.GridBagLayout());
        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nameLabel.setText(Msg.get("gui.name") + " :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipady = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        userDetailsPanel.add(nameLabel, gridBagConstraints);
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        userDetailsPanel.add(nameField, gridBagConstraints);
        countyLabel.setText("Country :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        userDetailsPanel.add(countyLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        userDetailsPanel.add(countryComboBox, gridBagConstraints);
        cityLabel.setText("City :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        userDetailsPanel.add(cityLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        userDetailsPanel.add(cityTextField, gridBagConstraints);
        addressLabel.setText("Street :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        userDetailsPanel.add(addressLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        userDetailsPanel.add(addressTextField, gridBagConstraints);
        zipCodeLabel.setText("Zip Code :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        userDetailsPanel.add(zipCodeLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        userDetailsPanel.add(zipCodeTextField, gridBagConstraints);
        jTextPane1.setEditable(false);
        jTextPane1.setText("<b>Enter your address.</b>\n(it is only used to resolve your absolute location. it won't be sent to anyone. you can skip this step if you want to resolve your location visually)");
        jTextPane1.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        userDetailsPanel.add(jTextPane1, gridBagConstraints);
        jTextPane1.getAccessibleContext().setAccessibleDescription("text/html");
        jTextPane2.setText("Please enter your user information.");
        jTextPane2.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        userDetailsPanel.add(jTextPane2, gridBagConstraints);
        setupPanels.add(userDetailsPanel, "card2");
        locatorPanel.setLayout(new java.awt.BorderLayout());
        locationPanel.setLayout(new java.awt.GridBagLayout());
        resolveCoordinateButton.setText("Resolve From Address");
        resolveCoordinateButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resolveCoordinateButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        locationPanel.add(resolveCoordinateButton, gridBagConstraints);
        locationButtonGroup.add(hereRadioButton);
        hereRadioButton.setSelected(true);
        hereRadioButton.setText("Here");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        locationPanel.add(hereRadioButton, gridBagConstraints);
        locationButtonGroup.add(siteRadioButton);
        siteRadioButton.setText("Site");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        locationPanel.add(siteRadioButton, gridBagConstraints);
        locationButtonGroup.add(locationRadioButton);
        locationRadioButton.setText("Lat");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        locationPanel.add(locationRadioButton, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        locationPanel.add(latitudeTextField, gridBagConstraints);
        jLabel5.setText("Lon");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 5);
        locationPanel.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        locationPanel.add(longitudeTextField, gridBagConstraints);
        locationSubPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        countrySitesComboBox.setModel(getCountryBoxModel());
        countrySitesComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countrySitesComboBoxActionPerformed(evt);
            }
        });
        locationSubPanel.add(countrySitesComboBox);
        siteComboBox.setModel(getSiteBoxModel(null));
        siteComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                siteComboBoxActionPerformed(evt);
            }
        });
        locationSubPanel.add(siteComboBox);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        locationPanel.add(locationSubPanel, gridBagConstraints);
        jTextPane3.setEditable(false);
        jTextPane3.setText("Select your location.");
        jTextPane3.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        locationPanel.add(jTextPane3, gridBagConstraints);
        locatorPanel.add(locationPanel, java.awt.BorderLayout.NORTH);
        setupPanels.add(locatorPanel, "card3");
        connectionPanel.setLayout(new java.awt.GridBagLayout());
        ipAddressLabel.setText("Ip Address :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        connectionPanel.add(ipAddressLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        connectionPanel.add(ipAddressTextFiel, gridBagConstraints);
        p2pListeningPort.setText("Port :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        connectionPanel.add(p2pListeningPort, gridBagConstraints);
        portTextField.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                portTextFieldFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        connectionPanel.add(portTextField, gridBagConstraints);
        testPortButton.setText("Test port");
        testPortButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testPortButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        connectionPanel.add(testPortButton, gridBagConstraints);
        connectionSpeedLabel.setText("Connection Speed :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        connectionPanel.add(connectionSpeedLabel, gridBagConstraints);
        connectionSpeedComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "56Kbits (Modem)", "512Kbits (ADSL)", "1Mbits (ADSL)", "8Mbits (Fast ADSL)", "20Mbits (ADSL 2)" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        connectionPanel.add(connectionSpeedComboBox, gridBagConstraints);
        autoConnectsCheckBox.setText("Autoconnects at Startup");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        connectionPanel.add(autoConnectsCheckBox, gridBagConstraints);
        uuidLabel.setText("UUID :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        connectionPanel.add(uuidLabel, gridBagConstraints);
        uuidTextField.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 10);
        connectionPanel.add(uuidTextField, gridBagConstraints);
        setupPanels.add(connectionPanel, "card4");
        okPanel.setLayout(new java.awt.GridBagLayout());
        jTextPane4.setText("thanks. \nConfiguration complete.\n\nWould you like to connect the network now?");
        jTextPane4.setOpaque(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        okPanel.add(jTextPane4, gridBagConstraints);
        jButton1.setText("yes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHEAST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        okPanel.add(jButton1, gridBagConstraints);
        jButton2.setText("No");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        okPanel.add(jButton2, gridBagConstraints);
        setupPanels.add(okPanel, "card5");
        add(setupPanels, java.awt.BorderLayout.CENTER);
    }

    private void finishButtonActionPerformed(java.awt.event.ActionEvent evt) {
        updateValues();
        closePanel();
    }

    private void portTextFieldFocusLost(java.awt.event.FocusEvent evt) {
        String port = portTextField.getText();
        validatePanel(port != null && !port.equals(""));
    }

    private void nameFieldFocusLost(java.awt.event.FocusEvent evt) {
        String name = nameField.getText();
        validatePanel(name != null && !name.equals(""));
    }

    private void siteComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        String site = (String) siteComboBox.getSelectedItem();
        if (site == null) return;
        Location location = Config.getSiteLocations().getSiteLocation(site);
        locationChanged(location);
        siteRadioButton.setSelected(true);
    }

    private void countrySitesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        Country country = (Country) countrySitesComboBox.getSelectedItem();
        siteComboBox.setModel(getSiteBoxModel(country));
        siteComboBoxActionPerformed(null);
    }

    private void resolveCoordinateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        Vector<Plugin> geolocators = PluginsManager.getPlugins(GeolocatorPlugin.class);
        if (geolocators.size() > 1) {
            JPopupMenu geolocatorsPopupMenu = new javax.swing.JPopupMenu();
            for (Plugin plugin : geolocators) {
                JMenuItem geolocatorMenuItem = new JMenuItem(plugin.getName(), plugin.getIcon());
                geolocatorMenuItem.addActionListener(new ResolveCoordinateActionListener((GeolocatorPlugin) plugin));
                geolocatorsPopupMenu.add(geolocatorMenuItem);
            }
            geolocatorsPopupMenu.show(locationPanel, resolveCoordinateButton.getX(), resolveCoordinateButton.getY() + resolveCoordinateButton.getHeight());
        } else {
            new ResolveCoordinateActionListener((GeolocatorPlugin) geolocators.get(0)).actionPerformed(null);
        }
    }

    private void testPortButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int port = Integer.parseInt(portTextField.getText());
        byte[] ipAddress = new byte[4];
        int err = NetworkTools.checkIpPort(ipAddress, port);
        String msg = null;
        switch(err) {
            case 0:
                String ipString = NetworkTools.getIpString(ipAddress);
                ipAddressTextFiel.setText(ipString);
                return;
            case NetworkTools.ERR_BLOCKED:
                msg = "the specified port (" + port + ") could not be contacted. \n maybe you are behind a firewall";
                break;
            case NetworkTools.ERR_CONNECTION:
                msg = "can't connect to ipPort checking host";
                break;
            case NetworkTools.ERR_PORTINUSE:
                msg = "localport already in use. please choose other";
        }
        JOptionPane.showMessageDialog(this, msg, "port check error", JOptionPane.WARNING_MESSAGE);
    }

    private void skipButtonActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(panelIndex + 1);
    }

    private void continueButtonActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(panelIndex + 1);
    }

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        showPanel(panelIndex - 1);
    }

    private int panelIndex = 0;

    private boolean[] panelsValid = { true, false, false, false, true };

    private void initPanels() {
        mapPanel = new MapPanel();
        locatorPanel.add(mapPanel, BorderLayout.CENTER);
        setupLineList.setSelectedIndex(panelIndex);
        Location defaultLocation = Config.getSiteLocations().getSiteLocation("Paris");
        initCountryList();
        initValues();
    }

    private void initCountryList() {
        Iterator<Country> countries = Country.getAvailableCountries();
        while (countries.hasNext()) {
            Country country = countries.next();
            countryComboBox.addItem(country);
        }
    }

    private void initValues() {
        nameField.setText(Config.getProperty(Config.PROP_USER_NAME));
        addressTextField.setText(Config.getProperty(Config.PROP_LOCATION_STREET));
        cityTextField.setText(Config.getProperty(Config.PROP_LOCATION_CITY));
        zipCodeTextField.setText(Config.getProperty(Config.PROP_LOCATION_POSTALCODE));
        String countryISO3 = Config.getProperty(Config.PROP_LOCATION_COUNTRY);
        Country country = null;
        if (countryISO3 == null || countryISO3.equals("")) {
            country = Country.getDefault();
        } else {
            country = new Country(countryISO3);
        }
        countrySitesComboBox.setSelectedItem(country);
        countryComboBox.setSelectedItem(country);
        latitudeTextField.setText(Config.getProperty(Config.PROP_LOCATION_LAT));
        longitudeTextField.setText(Config.getProperty(Config.PROP_LOCATION_LON));
        uuidTextField.setText(Config.getUUID().toString());
        portTextField.setText(Config.getProperty(Config.PROP_CONNECTION_PORT));
        autoConnectsCheckBox.setSelected("true".equals(Config.getProperty(Config.PROP_CONNECTION_AUTO)));
        ipAddressTextFiel.setText(Config.getProperty(Config.PROP_CONNECTION_IP));
    }

    private void updateValues() {
        Config.setProperty(Config.PROP_USER_NAME, nameField.getText());
        Config.setProperty(Config.PROP_LOCATION_STREET, addressTextField.getText());
        Config.setProperty(Config.PROP_LOCATION_CITY, cityTextField.getText());
        Config.setProperty(Config.PROP_LOCATION_POSTALCODE, zipCodeTextField.getText());
        Config.setProperty(Config.PROP_LOCATION_COUNTRY, ((Country) countryComboBox.getSelectedItem()).getISO3CountryCode());
        Config.setProperty(Config.PROP_LOCATION_LAT, latitudeTextField.getText());
        Config.setProperty(Config.PROP_LOCATION_LON, longitudeTextField.getText());
        Config.setProperty(Config.PROP_CONNECTION_PORT, portTextField.getText());
        Config.setProperty(Config.PROP_CONNECTION_AUTO, autoConnectsCheckBox.isSelected() ? "true" : "false");
        Config.setProperty(Config.PROP_CONNECTION_IP, ipAddressTextFiel.getText());
    }

    private void showPanel(int index) {
        if (index >= 4) index = 4;
        if (index < 0) index = 0;
        panelIndex = index;
        CardLayout layout = (CardLayout) setupPanels.getLayout();
        layout.show(setupPanels, "card" + (panelIndex + 1));
        setupLineList.setSelectedIndex(panelIndex);
        if (index == 4) {
            finishButton.setEnabled(true);
            continueButton.setEnabled(false);
            skipButton.setEnabled(false);
        } else {
            finishButton.setEnabled(false);
            continueButton.setEnabled(panelsValid[panelIndex]);
        }
    }

    private void validatePanel(boolean isValid) {
        panelsValid[panelIndex] = isValid;
        continueButton.setEnabled(isValid);
    }

    public void locationChanged(Location location) {
        latitudeTextField.setText("" + location.getLat());
        longitudeTextField.setText("" + location.getLon());
        locationRadioButton.setSelected(true);
        mapPanel.gotoLocation(location);
        validatePanel(true);
    }

    private ComboBoxModel getSiteBoxModel(Country country) {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        Iterator<String> sites = Config.getSiteLocations().getSites(country);
        while (sites.hasNext()) {
            String site = sites.next();
            model.addElement(site);
        }
        return model;
    }

    private ComboBoxModel getCountryBoxModel() {
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        Iterator<Country> countries = Config.getSiteLocations().getCountries();
        while (countries.hasNext()) {
            Country country = countries.next();
            model.addElement(country);
        }
        return model;
    }

    /**
     * resolves coordinates from plugin.
     */
    private class ResolveCoordinateActionListener implements ActionListener {

        private GeolocatorPlugin geolocator;

        public ResolveCoordinateActionListener(GeolocatorPlugin geolocator) {
            this.geolocator = geolocator;
        }

        public void actionPerformed(ActionEvent e) {
            SimpleAddressLocation address = new SimpleAddressLocation();
            address.setCity(cityTextField.getText());
            address.setStreet(addressTextField.getText());
            address.setZipcode(zipCodeTextField.getText());
            Country country = (Country) countryComboBox.getSelectedItem();
            address.setCountryCode(country.getISO3CountryCode());
            Vector<AddressLocation> result = null;
            try {
                result = geolocator.getLocationList(address);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(locationPanel, "Error accesing Geocoding service : " + ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (result.size() == 0) {
                JOptionPane.showMessageDialog(locationPanel, "no results where found", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            AddressLocation foundAddress = null;
            if (result.size() > 1) {
                JComboBox resultBox = new JComboBox();
                for (int cpt = 0; cpt < result.size(); cpt++) {
                    AddressLocation adr = result.get(cpt);
                    String addrString = (adr.getStreet() == null ? "" : adr.getStreet() + ",") + adr.getCity() + (adr.getZipcode() == null ? "" : "," + adr.getZipcode());
                    resultBox.addItem(addrString);
                }
                Object[] message = new Object[2];
                message[0] = "Please_pick_the_correct_address";
                message[1] = resultBox;
                JOptionPane.showOptionDialog(locationPanel, message, "pick_a_result", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                int resultIndex = resultBox.getSelectedIndex();
                foundAddress = result.get(resultIndex);
            } else {
                foundAddress = result.get(0);
            }
            cityTextField.setText(foundAddress.getCity());
            addressTextField.setText(foundAddress.getStreet());
            zipCodeTextField.setText(foundAddress.getZipcode());
            countryComboBox.setSelectedItem(new Country(foundAddress.getCountryCode()));
            latitudeTextField.setText(Double.toString(foundAddress.getLat()));
            longitudeTextField.setText(Double.toString(foundAddress.getLon()));
            locationChanged(foundAddress);
        }
    }

    javax.swing.JLabel addressLabel;

    javax.swing.JTextField addressTextField;

    javax.swing.JCheckBox autoConnectsCheckBox;

    javax.swing.JButton backButton;

    javax.swing.JLabel cityLabel;

    javax.swing.JTextField cityTextField;

    javax.swing.JPanel connectionPanel;

    javax.swing.JComboBox connectionSpeedComboBox;

    javax.swing.JLabel connectionSpeedLabel;

    javax.swing.JButton continueButton;

    javax.swing.JComboBox countryComboBox;

    javax.swing.JComboBox countrySitesComboBox;

    javax.swing.JLabel countyLabel;

    javax.swing.JButton finishButton;

    javax.swing.JRadioButton hereRadioButton;

    javax.swing.JLabel ipAddressLabel;

    javax.swing.JTextField ipAddressTextFiel;

    javax.swing.JButton jButton1;

    javax.swing.JButton jButton2;

    javax.swing.JLabel jLabel5;

    javax.swing.JPanel jPanel1;

    javax.swing.JTextPane jTextPane1;

    javax.swing.JTextPane jTextPane2;

    javax.swing.JTextPane jTextPane3;

    javax.swing.JTextPane jTextPane4;

    javax.swing.JTextField latitudeTextField;

    javax.swing.ButtonGroup locationButtonGroup;

    javax.swing.JPanel locationPanel;

    javax.swing.JRadioButton locationRadioButton;

    javax.swing.JPanel locationSubPanel;

    javax.swing.JPanel locatorPanel;

    javax.swing.JTextField longitudeTextField;

    javax.swing.JTextField nameField;

    javax.swing.JLabel nameLabel;

    javax.swing.JPanel okPanel;

    javax.swing.JLabel p2pListeningPort;

    javax.swing.JTextField portTextField;

    javax.swing.JButton resolveCoordinateButton;

    javax.swing.JList setupLineList;

    javax.swing.JPanel setupPanels;

    javax.swing.JComboBox siteComboBox;

    javax.swing.JRadioButton siteRadioButton;

    javax.swing.JButton skipButton;

    javax.swing.JButton testPortButton;

    javax.swing.JPanel userDetailsPanel;

    javax.swing.JLabel uuidLabel;

    javax.swing.JTextField uuidTextField;

    javax.swing.JEditorPane welcomePanel;

    javax.swing.JLabel zipCodeLabel;

    javax.swing.JTextField zipCodeTextField;
}
