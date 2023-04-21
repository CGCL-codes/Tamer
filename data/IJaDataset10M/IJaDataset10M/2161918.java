package org.goniolab.unitcircle.controls;

import java.util.logging.Logger;
import javax.swing.JComboBox;
import org.goniolab.unitcircle.Options;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.StatusDisplayer;

/**
 *
 * @author Patrik Karlsson <patrik@trixon.se>
 */
@ConvertAsProperties(dtd = "-//org.goniolab.unitcircle.controls//System//EN", autostore = false)
public final class SystemTopComponent extends TopComponent {

    private static SystemTopComponent instance;

    private static final String PREFERRED_ID = "SystemTopComponent";

    public SystemTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(SystemTopComponent.class, "CTL_SystemTopComponent"));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.FALSE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.FALSE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.FALSE);
        init();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        intervalLabel = new javax.swing.JLabel();
        intervalComboBox = new javax.swing.JComboBox();
        northingLabel = new javax.swing.JLabel();
        northingComboBox = new javax.swing.JComboBox();
        setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 5));
        jPanel1.setOpaque(false);
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel2.setOpaque(false);
        jPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(jPanel2, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(intervalLabel, org.openide.util.NbBundle.getMessage(SystemTopComponent.class, "SystemTopComponent.intervalLabel.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(intervalLabel, gridBagConstraints);
        intervalComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        intervalComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                intervalComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(intervalComboBox, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(northingLabel, org.openide.util.NbBundle.getMessage(SystemTopComponent.class, "SystemTopComponent.northingLabel.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(northingLabel, gridBagConstraints);
        northingComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "X", "Y" }));
        northingComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                northingComboBoxActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel1.add(northingComboBox, gridBagConstraints);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE));
    }

    private void northingComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        Options.getInstance().putInteger(Options.IntegerPref.NORTHING, northingComboBox.getSelectedIndex());
        String statusMessage = NbBundle.getMessage(SystemTopComponent.class, "SystemTopComponent.northingLabel.text") + ": " + (String) northingComboBox.getModel().getSelectedItem();
        StatusDisplayer.getDefault().setStatusText(statusMessage);
    }

    private void intervalComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        Options.getInstance().putInteger(Options.IntegerPref.INTERVAL, intervalComboBox.getSelectedIndex());
        String statusMessage = NbBundle.getMessage(SystemTopComponent.class, "SystemTopComponent.intervalLabel.text") + ": " + (String) intervalComboBox.getModel().getSelectedItem();
        StatusDisplayer.getDefault().setStatusText(statusMessage);
    }

    private javax.swing.JComboBox intervalComboBox;

    private javax.swing.JLabel intervalLabel;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JComboBox northingComboBox;

    private javax.swing.JLabel northingLabel;

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain SystemTopComponent, use {@link #findInstance}.
     */
    public static synchronized SystemTopComponent getDefault() {
        if (instance == null) {
            instance = new SystemTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the SystemTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized SystemTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(SystemTopComponent.class.getName()).warning("Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof SystemTopComponent) {
            return (SystemTopComponent) win;
        }
        Logger.getLogger(SystemTopComponent.class.getName()).warning("There seem to be multiple components with the '" + PREFERRED_ID + "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    JComboBox getIntervalComboBox() {
        return intervalComboBox;
    }

    JComboBox getNorthingComboBox() {
        return northingComboBox;
    }

    Object readProperties(java.util.Properties p) {
        if (instance == null) {
            instance = this;
        }
        instance.readPropertiesImpl(p);
        return instance;
    }

    void writeProperties(java.util.Properties p) {
        p.setProperty("version", "1.0");
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
    }

    private void init() {
        initStates();
    }

    private void initStates() {
        intervalComboBox.setSelectedIndex(Options.getInstance().getInteger(Options.IntegerPref.INTERVAL));
        northingComboBox.setSelectedIndex(Options.getInstance().getInteger(Options.IntegerPref.NORTHING));
    }
}
