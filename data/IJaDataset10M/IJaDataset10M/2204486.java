package org.pocui.swing.example.composites.stammdaten.anzeigen.anschrift;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.pocui.core.actions.EmptyActionConfiguration;
import org.pocui.core.composites.CompositeInitializationException;
import org.pocui.core.resources.EmptyResourceConfiguration;
import org.pocui.swing.composites.AbsComposite;

/**
 * Composite to Visualize the "Anschrift" for a Customer
 * @author Kai Benjamin Joneleit
 *
 */
public class VisualisiereAnschriftComposite extends AbsComposite<EmptyActionConfiguration, EmptyResourceConfiguration, VisualisiereAnschriftSelectionInOut> {

    private static final long serialVersionUID = 1267356019441445414L;

    private JTextField jTextFieldStrasse;

    private JTextField jTextFieldHausnummer;

    private JTextField jTextFieldPLZ;

    private JTextField jTextFieldWohnort;

    private JComboBox jComboBoxBundesland;

    public VisualisiereAnschriftComposite() throws CompositeInitializationException {
        super(EmptyActionConfiguration.getInstance(), EmptyResourceConfiguration.getInstance());
    }

    @Override
    protected void initGUI() throws CompositeInitializationException {
        java.awt.GridBagConstraints gridBagConstraints;
        javax.swing.JLabel jLabelStrasse = new javax.swing.JLabel();
        jTextFieldStrasse = new javax.swing.JTextField();
        javax.swing.JLabel jLabelHausnummer = new javax.swing.JLabel();
        jTextFieldHausnummer = new javax.swing.JTextField();
        jTextFieldPLZ = new javax.swing.JTextField();
        javax.swing.JLabel jLabelPLZ = new javax.swing.JLabel();
        jTextFieldWohnort = new javax.swing.JTextField();
        javax.swing.JLabel jLabelWohnort = new javax.swing.JLabel();
        jComboBoxBundesland = new javax.swing.JComboBox();
        javax.swing.JLabel jLabelBundesland = new javax.swing.JLabel();
        setBorder(javax.swing.BorderFactory.createTitledBorder("Kundenanschrift visualisieren"));
        setLayout(new java.awt.GridBagLayout());
        jLabelStrasse.setText("Strasse");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 10);
        add(jLabelStrasse, gridBagConstraints);
        jTextFieldStrasse.setEditable(false);
        jTextFieldStrasse.setPreferredSize(new java.awt.Dimension(220, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        add(jTextFieldStrasse, gridBagConstraints);
        jLabelHausnummer.setText("Hausnummer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 10);
        add(jLabelHausnummer, gridBagConstraints);
        jTextFieldHausnummer.setEditable(false);
        jTextFieldHausnummer.setPreferredSize(new java.awt.Dimension(40, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        add(jTextFieldHausnummer, gridBagConstraints);
        jTextFieldPLZ.setEditable(false);
        jTextFieldPLZ.setPreferredSize(new java.awt.Dimension(65, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        add(jTextFieldPLZ, gridBagConstraints);
        jLabelPLZ.setText("Postleitzahl");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 10);
        add(jLabelPLZ, gridBagConstraints);
        jTextFieldWohnort.setEditable(false);
        jTextFieldWohnort.setPreferredSize(new java.awt.Dimension(120, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        add(jTextFieldWohnort, gridBagConstraints);
        jLabelWohnort.setText("Wohnort");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 10);
        add(jLabelWohnort, gridBagConstraints);
        jComboBoxBundesland.setPreferredSize(new java.awt.Dimension(120, 20));
        jComboBoxBundesland.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 5);
        add(jComboBoxBundesland, gridBagConstraints);
        jLabelBundesland.setText("Bundesland");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 10);
        add(jLabelBundesland, gridBagConstraints);
    }

    @Override
    protected void initListener() throws CompositeInitializationException {
    }

    @Override
    public void doSetSelection(VisualisiereAnschriftSelectionInOut oldSelection, VisualisiereAnschriftSelectionInOut selection) {
        if (!jTextFieldHausnummer.getText().equals(selection.getHausnummer())) {
            jTextFieldHausnummer.setText(selection.getHausnummer());
        }
        if (!jTextFieldPLZ.getText().equals(selection.getPostleitzahl())) {
            jTextFieldPLZ.setText(selection.getPostleitzahl() == null ? "" : "" + selection.getPostleitzahl());
        }
        if (!jTextFieldStrasse.getText().equals(selection.getStrasse())) {
            jTextFieldStrasse.setText(selection.getStrasse());
        }
        if (!jTextFieldWohnort.getText().equals(selection.getWohnort())) {
            jTextFieldWohnort.setText(selection.getWohnort());
        }
        DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBoxBundesland.getModel();
        model.removeAllElements();
        for (String bundesland : selection.getBundeslaender()) {
            model.addElement(bundesland);
        }
        model.setSelectedItem(selection.getSelektiertesBundesland());
    }

    @Override
    protected void addAllListener() {
    }

    @Override
    protected void doCleanUp() {
    }

    @Override
    protected void removeAllListener() {
    }
}
