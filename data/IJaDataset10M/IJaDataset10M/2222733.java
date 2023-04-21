package cz.cuni.mff.ksi.jinfer.basicdtd.properties;

import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import java.util.Properties;

/**
 * Properties panel of the Basic DTD Exporter module.
 *
 * @author vektor
 */
@SuppressWarnings("PMD.SingularField")
public final class DTDExportPropertiesPanel extends AbstractPropertiesPanel {

    private static final long serialVersionUID = 5421231l;

    public static final String NAME = "basicdtdexporter";

    public static final String MAX_ENUM_SIZE = "max.enum.size";

    public static final int MAX_ENUM_SIZE_DEFAULT = 3;

    public static final String MIN_DEFAULT_RATIO = "min.default.ratio";

    public static final float MIN_DEFAULT_RATIO_DEFAULT = 0.67f;

    public DTDExportPropertiesPanel(final Properties properties) {
        super(properties);
        initComponents();
    }

    @SuppressWarnings("PMD")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jLabel1 = new javax.swing.JLabel();
        maxEnumSize = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        minDefaultRatio = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        setMinimumSize(new java.awt.Dimension(500, 300));
        setPreferredSize(new java.awt.Dimension(500, 300));
        setLayout(new java.awt.GridBagLayout());
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.jLabel1.text"));
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.jLabel1.toolTipText"));
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 14));
        jLabel1.setMinimumSize(new java.awt.Dimension(100, 14));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel1, gridBagConstraints);
        maxEnumSize.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        maxEnumSize.setToolTipText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.maxEnumSize.toolTipText"));
        maxEnumSize.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        maxEnumSize.setMinimumSize(new java.awt.Dimension(40, 22));
        maxEnumSize.setPreferredSize(new java.awt.Dimension(60, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(maxEnumSize, gridBagConstraints);
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.jLabel3.text"));
        jLabel3.setToolTipText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.jLabel3.toolTipText"));
        jLabel3.setMaximumSize(new java.awt.Dimension(120, 14));
        jLabel3.setMinimumSize(new java.awt.Dimension(120, 14));
        jLabel3.setPreferredSize(new java.awt.Dimension(120, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel3, gridBagConstraints);
        minDefaultRatio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        minDefaultRatio.setText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.minDefaultRatio.text"));
        minDefaultRatio.setToolTipText(org.openide.util.NbBundle.getMessage(DTDExportPropertiesPanel.class, "DTDExportPropertiesPanel.minDefaultRatio.toolTipText"));
        minDefaultRatio.setMaximumSize(new java.awt.Dimension(32767, 32767));
        minDefaultRatio.setMinimumSize(new java.awt.Dimension(40, 22));
        minDefaultRatio.setPreferredSize(new java.awt.Dimension(60, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(minDefaultRatio, gridBagConstraints);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 500, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 248, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);
        jPanel2.setPreferredSize(new java.awt.Dimension(20, 52));
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 202, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 52, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel2, gridBagConstraints);
    }

    @Override
    public void load() {
        maxEnumSize.setValue(Integer.valueOf(properties.getProperty(MAX_ENUM_SIZE, Integer.toString(MAX_ENUM_SIZE_DEFAULT))));
        minDefaultRatio.setValue(properties.getProperty(MIN_DEFAULT_RATIO, Float.toString(MIN_DEFAULT_RATIO_DEFAULT)));
    }

    @Override
    public void store() {
        properties.setProperty(MAX_ENUM_SIZE, ((Integer) maxEnumSize.getValue()).toString());
        properties.setProperty(MIN_DEFAULT_RATIO, minDefaultRatio.getText());
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JSpinner maxEnumSize;

    private javax.swing.JFormattedTextField minDefaultRatio;
}
