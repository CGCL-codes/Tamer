package cz.cuni.mff.ksi.jinfer.modularsimplifier.properties;

import cz.cuni.mff.ksi.jinfer.base.utils.ModuleSelectionHelper;
import cz.cuni.mff.ksi.jinfer.base.objects.AbstractPropertiesPanel;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.clustering.ClustererFactory;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.kleening.KleeneProcessorFactory;
import cz.cuni.mff.ksi.jinfer.modularsimplifier.processing.ClusterProcessorFactory;
import java.util.Properties;
import javax.swing.DefaultComboBoxModel;

/**
 * @author vektor
 */
public final class PropertiesPanel extends AbstractPropertiesPanel {

    private static final long serialVersionUID = 561241l;

    public static final String NAME = "modularsimplifier";

    public static final String CLUSTERER = "clusterer";

    public static final String CLUSTERER_DEFAULT = "Name clusterer";

    public static final String CLUSTER_PROCESSOR = "cluster.processor";

    public static final String CLUSTER_PROCESSOR_DEFAULT = "Trie";

    public static final String KLEENE_PROCESSOR = "kleene.processor";

    public static final String KLEENE_PROCESSOR_DEFAULT = "Simple Kleene processor";

    public static final String KLEENE_REPETITIONS = "kleene.repetitions";

    public static final int KLEENE_REPETITIONS_DEFAULT = 3;

    public static final String ENABLED = "enabled";

    public static final boolean ENABLED_DEFAULT = true;

    public static final String RENDER = "render";

    public static final boolean RENDER_DEFAULT = true;

    public PropertiesPanel(final Properties properties) {
        super(properties);
        initComponents();
    }

    @SuppressWarnings("PMD")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jLabel1 = new javax.swing.JLabel();
        kleeneRepetitions = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        clusterProcessor = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        enabled = new javax.swing.JCheckBox();
        render = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        kleeneProcessor = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        clusterer = new javax.swing.JComboBox();
        setLayout(new java.awt.GridBagLayout());
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel1.text"));
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel1.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel1, gridBagConstraints);
        kleeneRepetitions.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.kleeneRepetitions.toolTipText"));
        kleeneRepetitions.setMinimumSize(new java.awt.Dimension(150, 22));
        kleeneRepetitions.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(kleeneRepetitions, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel2.text"));
        jLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel2.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel2, gridBagConstraints);
        clusterProcessor.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.clusterProcessor.toolTipText"));
        clusterProcessor.setMinimumSize(new java.awt.Dimension(150, 22));
        clusterProcessor.setPreferredSize(new java.awt.Dimension(150, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(clusterProcessor, gridBagConstraints);
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 267, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 79, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        add(jPanel1, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(enabled, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.enabled.text"));
        enabled.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.enabled.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(enabled, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(render, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.render.text"));
        render.setToolTipText(org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.render.toolTipText"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(render, gridBagConstraints);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 113, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 124, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel2, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel3.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(kleeneProcessor, gridBagConstraints);
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(PropertiesPanel.class, "PropertiesPanel.jLabel4.text"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(2, 12, 2, 12);
        add(jLabel4, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        add(clusterer, gridBagConstraints);
    }

    @Override
    public void load() {
        enabled.setSelected(Boolean.parseBoolean(properties.getProperty(ENABLED, Boolean.toString(ENABLED_DEFAULT))));
        render.setSelected(Boolean.parseBoolean(properties.getProperty(RENDER, Boolean.toString(RENDER_DEFAULT))));
        clusterer.setModel(new DefaultComboBoxModel(ModuleSelectionHelper.lookupNames(ClustererFactory.class).toArray()));
        clusterer.setSelectedItem(properties.getProperty(CLUSTERER, CLUSTERER_DEFAULT));
        clusterProcessor.setModel(new DefaultComboBoxModel(ModuleSelectionHelper.lookupNames(ClusterProcessorFactory.class).toArray()));
        clusterProcessor.setSelectedItem(properties.getProperty(CLUSTER_PROCESSOR, CLUSTER_PROCESSOR_DEFAULT));
        kleeneProcessor.setModel(new DefaultComboBoxModel(ModuleSelectionHelper.lookupNames(KleeneProcessorFactory.class).toArray()));
        kleeneProcessor.setSelectedItem(properties.getProperty(KLEENE_PROCESSOR, KLEENE_PROCESSOR_DEFAULT));
        kleeneRepetitions.setValue(Integer.valueOf(properties.getProperty(KLEENE_REPETITIONS, Integer.toString(KLEENE_REPETITIONS_DEFAULT))));
    }

    @Override
    public void store() {
        properties.setProperty(ENABLED, Boolean.toString(enabled.isSelected()));
        properties.setProperty(RENDER, Boolean.toString(render.isSelected()));
        properties.setProperty(CLUSTERER, (String) clusterer.getSelectedItem());
        properties.setProperty(CLUSTER_PROCESSOR, (String) clusterProcessor.getSelectedItem());
        properties.setProperty(KLEENE_PROCESSOR, (String) kleeneProcessor.getSelectedItem());
        properties.setProperty(KLEENE_REPETITIONS, ((Integer) kleeneRepetitions.getValue()).toString());
    }

    private javax.swing.JComboBox clusterProcessor;

    private javax.swing.JComboBox clusterer;

    private javax.swing.JCheckBox enabled;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JComboBox kleeneProcessor;

    private javax.swing.JSpinner kleeneRepetitions;

    private javax.swing.JCheckBox render;
}
