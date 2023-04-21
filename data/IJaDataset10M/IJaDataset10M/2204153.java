package org.gvsig.graph.gui.wizard;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import jwizardcomponent.JWizardPanel;
import org.gvsig.exceptions.BaseException;
import org.gvsig.graph.core.NetworkUtils;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JIncrementalNumberField;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.instruction.IncompatibleTypesException;
import com.hardcode.gdbms.engine.values.BooleanValue;
import com.hardcode.gdbms.engine.values.NullValue;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

/**
 * Configures the length
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
class NetPage1 extends JWizardPanel implements ActionListener {

    private NetWizard owner;

    private JComboBox cmbLengthField;

    private Hashtable exceptions;

    private JComboBox cmbTypeField;

    private JComboBox cmbSenseField;

    private JIncrementalNumberField txtUnitFactor;

    private JComboBox cmbCostField;

    private Object useLineLengthItem = "< " + PluginServices.getText(this, "use_line_length") + " >";

    private Object nullValue = "- " + PluginServices.getText(this, "none") + " -";

    private JCheckBox chkTypeField;

    private JCheckBox chkLengthField;

    private JCheckBox chkCostUnits;

    private JCheckBox chkSenseField;

    private JCheckBox chkCostField;

    private JTextField txtDigitizedDir;

    private JTextField txtReverseDigitizedDir;

    private JTextField txtFile;

    NetPage1(NetWizard wizard) {
        super(wizard.getWizardComponents());
        this.owner = wizard;
        initialize();
    }

    private void initialize() {
        GridBagLayoutPanel contentPane = new GridBagLayoutPanel();
        contentPane.setBorder(javax.swing.BorderFactory.createTitledBorder(null, PluginServices.getText(this, "field_configuration"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
        contentPane.setPreferredSize(new Dimension(520, 300));
        cmbTypeField = new JComboBox();
        cmbTypeField.addItem(nullValue);
        cmbTypeField.setToolTipText(PluginServices.getText(this, "type_field_text"));
        String[] ss = owner.getNumericLayerFieldNames();
        for (int i = 0; i < ss.length; i++) {
            cmbTypeField.addItem(ss[i]);
        }
        cmbTypeField.addActionListener(this);
        chkTypeField = new JCheckBox(PluginServices.getText(this, "select_type_field") + ":");
        chkTypeField.addActionListener(this);
        contentPane.addComponent(chkTypeField, cmbTypeField);
        cmbLengthField = new JComboBox();
        cmbLengthField.addItem(nullValue);
        cmbLengthField.setToolTipText(PluginServices.getText(this, "length_field_text"));
        ss = owner.getNumericLayerFieldNames();
        for (int i = 0; i < ss.length; i++) {
            cmbLengthField.addItem(ss[i]);
        }
        cmbLengthField.addActionListener(this);
        chkLengthField = new JCheckBox(PluginServices.getText(this, "select_length_field") + ":");
        chkLengthField.addActionListener(this);
        contentPane.addComponent(chkLengthField, cmbLengthField);
        chkCostField = new JCheckBox(PluginServices.getText(this, "cost_field") + ":");
        chkCostField.addActionListener(this);
        contentPane.addComponent(chkCostField, cmbCostField = new JComboBox());
        cmbCostField.addItem(useLineLengthItem);
        cmbCostField.setToolTipText(PluginServices.getText(this, "cost_field_text"));
        cmbCostField.addActionListener(this);
        String[] numericFields = owner.getNumericLayerFieldNames();
        for (int i = 0; i < numericFields.length; i++) cmbCostField.addItem(numericFields[i]);
        txtUnitFactor = new JIncrementalNumberField("1");
        chkCostUnits = new JCheckBox(PluginServices.getText(this, "unit_factor") + ":");
        chkCostUnits.addActionListener(this);
        contentPane.addComponent(chkCostUnits, txtUnitFactor);
        txtUnitFactor.setToolTipText(PluginServices.getText(this, "unit_factor_text"));
        cmbSenseField = new JComboBox();
        cmbSenseField.addItem(nullValue);
        String[] fieldNames = owner.getLayerFieldNames();
        for (int i = 0; i < fieldNames.length; i++) {
            cmbSenseField.addItem(fieldNames[i]);
        }
        cmbSenseField.addActionListener(this);
        cmbSenseField.setToolTipText(PluginServices.getText(this, "sense_field_text"));
        chkSenseField = new JCheckBox(PluginServices.getText(this, "select_sense_field") + ":");
        chkSenseField.addActionListener(this);
        txtDigitizedDir = new JTextField(12);
        txtReverseDigitizedDir = new JTextField(12);
        contentPane.addComponent(chkSenseField, cmbSenseField);
        contentPane.addComponent(new JLabel(PluginServices.getText(this, "digitizedDirection") + ":"), txtDigitizedDir);
        contentPane.addComponent(new JLabel(PluginServices.getText(this, "reverseDigitizedDirection")), txtReverseDigitizedDir);
        contentPane.addComponent(new JLabel(" "));
        JPanel panelFile = new JPanel();
        txtFile = new JTextField();
        txtFile.setText(NetworkUtils.getNetworkFile(owner.getLayer()).getPath());
        JButton btnJFC = new JButton("...");
        btnJFC.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileFilter() {

                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory()) return true;
                        String path = f.getPath().toLowerCase();
                        if (path.endsWith(".net")) return true;
                        return false;
                    }

                    @Override
                    public String getDescription() {
                        return (PluginServices.getText(this, "Ficheros_NET"));
                    }
                });
                int res = fileChooser.showSaveDialog(getParent());
                if (res == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getPath();
                    if (!path.toLowerCase().endsWith(".net")) path = path + ".net";
                    txtFile.setText(path);
                }
            }
        });
        panelFile.setLayout(new BorderLayout());
        panelFile.add(txtFile, BorderLayout.CENTER);
        panelFile.add(btnJFC, BorderLayout.EAST);
        contentPane.addComponent(new JLabel(PluginServices.getText(this, "save_net_file_in")), panelFile);
        actionPerformed(null);
        this.add(contentPane);
    }

    public void actionPerformed(ActionEvent e) {
        txtUnitFactor.setEnabled(!cmbCostField.getSelectedItem().equals(useLineLengthItem));
        if (chkTypeField.isSelected()) {
            owner.setTypeField(cmbTypeField.getSelectedItem().equals(nullValue) ? null : (String) cmbTypeField.getSelectedItem());
            cmbTypeField.setEnabled(true);
        } else {
            owner.setTypeField(null);
            cmbTypeField.setEnabled(false);
        }
        if (chkLengthField.isSelected()) {
            owner.setLengthField(cmbLengthField.getSelectedItem().equals(nullValue) ? null : (String) cmbLengthField.getSelectedItem());
            cmbLengthField.setEnabled(true);
        } else {
            owner.setLengthField(null);
            cmbLengthField.setEnabled(false);
        }
        if (chkSenseField.isSelected()) {
            owner.setSenseField(cmbSenseField.getSelectedItem().equals(nullValue) ? null : (String) cmbSenseField.getSelectedItem());
            cmbSenseField.setEnabled(true);
            txtDigitizedDir.setEnabled(true);
            txtReverseDigitizedDir.setEnabled(true);
            if (e.getSource() == cmbSenseField) {
                updateDirectionValues();
            }
        } else {
            owner.setSenseField(null);
            cmbSenseField.setEnabled(false);
            txtDigitizedDir.setEnabled(false);
            txtReverseDigitizedDir.setEnabled(false);
        }
        if (chkCostField.isSelected()) {
            owner.setCostField(cmbCostField.getSelectedItem().equals(useLineLengthItem) ? null : (String) cmbCostField.getSelectedItem());
            cmbCostField.setEnabled(true);
        } else {
            owner.setCostField(null);
            cmbCostField.setEnabled(false);
        }
        if (chkCostUnits.isSelected()) {
            owner.setUnitFactor(txtUnitFactor.getDouble());
            txtUnitFactor.setEnabled(true);
        } else {
            owner.setUnitFactor(1);
            txtUnitFactor.setEnabled(false);
        }
    }

    /**
	 * We collect values from sense field for let say 1000 recs.
	 * The idea is to show the user some of the possible values he may use.
	 * For example, teleatlas: FT, TF
	 * @throws BaseException 
	 */
    private void updateDirectionValues() {
        String senseField = (String) cmbSenseField.getSelectedItem();
        SelectableDataSource sds = null;
        try {
            sds = owner.getLayer().getRecordset();
            int fieldIndex = sds.getFieldIndexByName(senseField);
            Value first = null;
            Value second = null;
            sds.start();
            long hasta = Math.min(sds.getRowCount(), 1000);
            for (int i = 0; i < hasta; i++) {
                Value aux = sds.getFieldValue(i, fieldIndex);
                if (aux.toString().equalsIgnoreCase("")) continue;
                if ((first == null) && (!(aux instanceof NullValue))) first = aux;
                if (first != null) {
                    if (!((BooleanValue) first.equals(aux)).getValue()) {
                        second = aux;
                        break;
                    }
                }
            }
            sds.stop();
            if (first == null) first = ValueFactory.createValue("");
            if (second == null) second = ValueFactory.createValue("");
            String aux1 = first.toString();
            String aux2 = second.toString();
            if (aux1.equalsIgnoreCase("TF")) if (aux2.equalsIgnoreCase("FT")) {
                String b = aux2;
                aux2 = aux1;
                aux1 = b;
            }
            txtDigitizedDir.setText(aux1);
            txtReverseDigitizedDir.setText(aux2);
        } catch (ReadDriverException e) {
            e.printStackTrace();
        } catch (IncompatibleTypesException e) {
            e.printStackTrace();
        } finally {
            try {
                sds.stop();
            } catch (ReadDriverException e1) {
                e1.printStackTrace();
            }
        }
    }

    public String getTxtFile() {
        return txtFile.getText();
    }

    public String getSenseDigitalization() {
        return txtDigitizedDir.getText();
    }

    public String getSenseReverseDigitalization() {
        return txtReverseDigitizedDir.getText();
    }
}
