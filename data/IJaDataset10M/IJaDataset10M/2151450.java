package org.pocui.swing.example.composites.stammdaten.anzeigen.vertragsdaten;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.pocui.core.actions.EmptyActionConfiguration;
import org.pocui.core.composites.CompositeInitializationException;
import org.pocui.core.resources.EmptyResourceConfiguration;
import org.pocui.swing.composites.AbsComposite;

/**
 * Composite to visualize Vertragsdaten
 * 
 * @author Kai Benjamin Joneleit
 *
 */
public class VisualisiereVertragsdatenComposite extends AbsComposite<EmptyActionConfiguration, EmptyResourceConfiguration, VisualisiereVertragsdatenSelectionInOut> {

    private static SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");

    private static final long serialVersionUID = 4554461969690610596L;

    private JComboBox jComboBoxProdukt;

    private JTextField jTextFieldEnde;

    private JTextField jTextFieldBeginn;

    public VisualisiereVertragsdatenComposite() throws CompositeInitializationException {
        super(EmptyActionConfiguration.getInstance(), EmptyResourceConfiguration.getInstance());
    }

    @Override
    protected void initGUI() throws CompositeInitializationException {
        java.awt.GridBagConstraints gridBagConstraints;
        jComboBoxProdukt = new javax.swing.JComboBox();
        javax.swing.JLabel jLabelProdukt = new javax.swing.JLabel();
        javax.swing.JLabel jLabelEnde = new javax.swing.JLabel();
        javax.swing.JLabel jLabelBeginn = new javax.swing.JLabel();
        jTextFieldEnde = new javax.swing.JTextField();
        jTextFieldBeginn = new javax.swing.JTextField();
        setBorder(javax.swing.BorderFactory.createTitledBorder("Vertragsdaten visualisieren"));
        setLayout(new java.awt.GridBagLayout());
        jComboBoxProdukt.setEnabled(false);
        jComboBoxProdukt.setPreferredSize(new java.awt.Dimension(180, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        add(jComboBoxProdukt, gridBagConstraints);
        jLabelProdukt.setText("Produkt");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 10);
        add(jLabelProdukt, gridBagConstraints);
        jLabelEnde.setText("Vertragsende");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 10);
        add(jLabelEnde, gridBagConstraints);
        jLabelBeginn.setText("Vertragsbeginn");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 10);
        add(jLabelBeginn, gridBagConstraints);
        jTextFieldEnde.setEditable(false);
        jTextFieldEnde.setPreferredSize(new java.awt.Dimension(160, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        add(jTextFieldEnde, gridBagConstraints);
        jTextFieldBeginn.setEditable(false);
        jTextFieldBeginn.setPreferredSize(new java.awt.Dimension(160, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 2, 0);
        add(jTextFieldBeginn, gridBagConstraints);
    }

    @Override
    protected void initListener() throws CompositeInitializationException {
    }

    @Override
    public void doSetSelection(VisualisiereVertragsdatenSelectionInOut oldSelection, VisualisiereVertragsdatenSelectionInOut selection) {
        if (selection.getVertragsbeginn() != null) {
            jTextFieldBeginn.setText(dateformat.format(new Date(selection.getVertragsbeginn())));
        } else {
            jTextFieldBeginn.setText("unbekannt");
        }
        if (selection.getVertragsende() != null) {
            jTextFieldEnde.setText(dateformat.format(new Date(selection.getVertragsende())));
        } else {
            jTextFieldEnde.setText("unbekannt/offen");
        }
        DefaultComboBoxModel model = (DefaultComboBoxModel) jComboBoxProdukt.getModel();
        model.removeAllElements();
        for (String produkt : selection.getProdukte()) {
            model.addElement(produkt);
        }
        model.setSelectedItem(selection.getSelektiertesProdukt());
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
