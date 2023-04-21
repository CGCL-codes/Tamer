package bahamontes;

import java.awt.Color;
import java.awt.FileDialog;

/**
 *
 * @author  Onno Kluyt
 */
public class PrefDialog extends javax.swing.JDialog {

    /**
     * Creates new form BahamontesPrefs
     */
    public PrefDialog(java.awt.Frame parent, BahamontesPrefs prefs) {
        super(parent, true);
        thePrefs = prefs;
        initComponents();
        if (thePrefs.isRideLocationSet()) {
            locationLabel.setText(thePrefs.getRideLocation() + thePrefs.getRideFile());
        }
        cadenceField.setText(String.valueOf(thePrefs.getMinCadence()));
        speedField.setText(String.valueOf(thePrefs.getMinSpeed()));
        metricButton.setSelected(thePrefs.isMetric());
        duplicatesBox.setSelected(thePrefs.ignoreDups());
        trackColor = thePrefs.getColor();
        smoothCheck.setSelected(thePrefs.isSmooth());
    }

    private void initComponents() {
        unitsButtonGroup = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        unitsPanel = new javax.swing.JPanel();
        standardButton = new javax.swing.JRadioButton();
        metricButton = new javax.swing.JRadioButton();
        smoothPanel = new javax.swing.JPanel();
        cadenceLabel = new javax.swing.JLabel();
        speedLabel = new javax.swing.JLabel();
        cadenceField = new javax.swing.JTextField();
        speedField = new javax.swing.JTextField();
        smoothCheck = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        duplicatesBox = new javax.swing.JCheckBox();
        locationLabel = new javax.swing.JLabel();
        locationButton = new javax.swing.JButton();
        bikeLabel = new javax.swing.JLabel();
        bike = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Preferences");
        okButton.setText("OK");
        okButton.setSelected(true);
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        unitsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Units", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 2, 13)));
        unitsButtonGroup.add(standardButton);
        standardButton.setSelected(!thePrefs.isMetric());
        standardButton.setText("Standard");
        standardButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        unitsButtonGroup.add(metricButton);
        metricButton.setSelected(thePrefs.isMetric());
        metricButton.setText("Metric");
        metricButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
        org.jdesktop.layout.GroupLayout unitsPanelLayout = new org.jdesktop.layout.GroupLayout(unitsPanel);
        unitsPanel.setLayout(unitsPanelLayout);
        unitsPanelLayout.setHorizontalGroup(unitsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(unitsPanelLayout.createSequentialGroup().addContainerGap().add(unitsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(standardButton).add(metricButton)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        unitsPanelLayout.setVerticalGroup(unitsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(unitsPanelLayout.createSequentialGroup().addContainerGap().add(standardButton).add(9, 9, 9).add(metricButton).addContainerGap(69, Short.MAX_VALUE)));
        smoothPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Min. values for smoothing", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 2, 13)));
        cadenceLabel.setText("Cadence");
        speedLabel.setText("Speed");
        cadenceField.setText(String.valueOf(thePrefs.getMinCadence()));
        speedField.setText(String.valueOf(thePrefs.getMinSpeed()));
        smoothCheck.setSelected(true);
        smoothCheck.setText("Smoothing is On");
        smoothCheck.setMargin(new java.awt.Insets(0, 0, 0, 0));
        org.jdesktop.layout.GroupLayout smoothPanelLayout = new org.jdesktop.layout.GroupLayout(smoothPanel);
        smoothPanel.setLayout(smoothPanelLayout);
        smoothPanelLayout.setHorizontalGroup(smoothPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(smoothPanelLayout.createSequentialGroup().addContainerGap().add(smoothPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(smoothPanelLayout.createSequentialGroup().add(smoothPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(cadenceLabel).add(speedLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(smoothPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(speedField).add(cadenceField))).add(smoothCheck)).addContainerGap(101, Short.MAX_VALUE)));
        smoothPanelLayout.setVerticalGroup(smoothPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(smoothPanelLayout.createSequentialGroup().addContainerGap().add(smoothPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(cadenceLabel).add(cadenceField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(smoothPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(speedLabel).add(speedField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(15, 15, 15).add(smoothCheck).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "General", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 2, 13)));
        duplicatesBox.setText("Ignore duplicate rides");
        duplicatesBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        duplicatesBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                duplicatesBoxActionPerformed(evt);
            }
        });
        locationLabel.setText("not set");
        locationButton.setText("Set rides location");
        locationButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                locationButtonActionPerformed(evt);
            }
        });
        bikeLabel.setText("Default bike name:");
        bike.setText("Maximilian");
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(9, 9, 9).add(locationButton)).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(locationLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)).add(jPanel2Layout.createSequentialGroup().add(duplicatesBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(bike, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)))).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(bikeLabel))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(locationButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(locationLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(duplicatesBox).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(bikeLabel).add(bike, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(mainPanelLayout.createSequentialGroup().addContainerGap().add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(mainPanelLayout.createSequentialGroup().add(unitsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(mainPanelLayout.createSequentialGroup().add(cancelButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(okButton)).add(smoothPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))).addContainerGap()));
        mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(mainPanelLayout.createSequentialGroup().addContainerGap().add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(unitsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(smoothPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(okButton).add(cancelButton)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(mainPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(mainPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
        pack();
    }

    private void locationButtonActionPerformed(java.awt.event.ActionEvent evt) {
        FileDialog fileDialog = new FileDialog(this, "Choose directory or folder to save your rides", FileDialog.SAVE);
        fileDialog.setVisible(true);
        locationDirectory = fileDialog.getDirectory();
        locationFile = fileDialog.getFile();
        if (locationFile != null) {
            locationLabel.setText(locationDirectory + locationFile);
            filenameChanged = true;
        } else {
            filenameChanged = false;
        }
    }

    private void duplicatesBoxActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        if (filenameChanged) {
            thePrefs.setRideLocation(locationDirectory);
            thePrefs.setRideFile(locationFile);
        }
        thePrefs.setMetric(metricButton.isSelected());
        thePrefs.setMinCadence(Integer.parseInt(cadenceField.getText()));
        thePrefs.setMinSpeed(Integer.parseInt(speedField.getText()));
        thePrefs.setIgnore(duplicatesBox.isSelected());
        thePrefs.setColor(trackColor);
        thePrefs.setSmooth(smoothCheck.isSelected());
        thePrefs.setDefaultBike(bike.getText());
        thePrefs.save();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        setVisible(false);
        cadenceField.setText(String.valueOf(thePrefs.getMinCadence()));
        speedField.setText(String.valueOf(thePrefs.getMinSpeed()));
        metricButton.setSelected(thePrefs.isMetric());
        duplicatesBox.setSelected(thePrefs.ignoreDups());
        trackColor = thePrefs.getColor();
        smoothCheck.setSelected(thePrefs.isSmooth());
        bike.setText(thePrefs.getDefaultBike());
    }

    private javax.swing.JTextField bike;

    private javax.swing.JLabel bikeLabel;

    private javax.swing.JTextField cadenceField;

    private javax.swing.JLabel cadenceLabel;

    private javax.swing.JButton cancelButton;

    private javax.swing.JCheckBox duplicatesBox;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JButton locationButton;

    private javax.swing.JLabel locationLabel;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JRadioButton metricButton;

    private javax.swing.JButton okButton;

    private javax.swing.JCheckBox smoothCheck;

    private javax.swing.JPanel smoothPanel;

    private javax.swing.JTextField speedField;

    private javax.swing.JLabel speedLabel;

    private javax.swing.JRadioButton standardButton;

    private javax.swing.ButtonGroup unitsButtonGroup;

    private javax.swing.JPanel unitsPanel;

    private Color trackColor;

    private BahamontesPrefs thePrefs;

    private String locationDirectory = "";

    private String locationFile = "";

    private boolean filenameChanged = false;
}
