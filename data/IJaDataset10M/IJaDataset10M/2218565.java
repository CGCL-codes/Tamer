package org.formaria.editor.project.dialog;

import java.io.BufferedReader;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.formaria.debug.DebugLogger;
import org.formaria.editor.project.EditorProject;
import org.formaria.xml.XmlElement;
import org.formaria.xml.XmlSource;

/**
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) Formaria Ltd., 1998-2003</p>
 * $Revision: 1.3 $
 * License: see license.txt
 */
public class ValidationDlg extends JDialog implements ListSelectionListener, ItemListener, ActionListener {

    JList lstValidations;

    JComboBox cmbValidationTypes;

    JButton btnNew, btnSave, btnCancel;

    JTextField txtMin, txtMax, txtValidationName;

    JLabel lblMin, lblMax, lblValidationName;

    JCheckBox chkMandatory;

    JPanel pnlMinMaxControls;

    JPanel pnlButtons;

    XmlElement nodValidations;

    boolean bRet = false;

    public static final String VALIDATION_MANDATORY = "MandatoryValidator";

    public static final String VALIDATION_MINMAX = "MinMaxValidator";

    private EditorProject currentProject;

    public ValidationDlg(EditorProject project) {
        setModal(true);
        currentProject = project;
        getContentPane().setLayout(null);
        setSize(300, 400);
        setupDlg();
    }

    private void setupDlg() {
        pnlMinMaxControls = new JPanel(null);
        pnlMinMaxControls.setBounds(1, 200, 298, 50);
        getContentPane().add(pnlMinMaxControls);
        pnlButtons = new JPanel(new FlowLayout());
        btnNew = new JButton("New");
        pnlButtons.add(btnNew);
        btnNew.addActionListener(this);
        btnSave = new JButton("Save");
        btnSave.addActionListener(this);
        pnlButtons.add(btnSave);
        btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(this);
        pnlButtons.add(btnCancel);
        pnlButtons.setBounds(1, 270, 298, 29);
        getContentPane().add(pnlButtons);
        lstValidations = new JList();
        lstValidations.setBounds(1, 1, 298, 150);
        lstValidations.setSelectionMode(lstValidations.getSelectionModel().SINGLE_SELECTION);
        lstValidations.addListSelectionListener(this);
        getContentPane().add(lstValidations);
        cmbValidationTypes = new JComboBox();
        cmbValidationTypes.addItemListener(this);
        cmbValidationTypes.addItem(VALIDATION_MANDATORY);
        cmbValidationTypes.addItem(VALIDATION_MINMAX);
        cmbValidationTypes.setBounds(10, 155, 180, 20);
        getContentPane().add(cmbValidationTypes);
        lblValidationName = new JLabel("Validation name :");
        lblValidationName.setBounds(10, 175, 100, 20);
        getContentPane().add(lblValidationName);
        txtValidationName = new JTextField();
        txtValidationName.setBounds(115, 175, 70, 20);
        getContentPane().add(txtValidationName);
        lblMin = new JLabel("Min");
        lblMin.setBounds(5, 5, 40, 20);
        pnlMinMaxControls.add(lblMin);
        txtMin = new JTextField();
        txtMin.setBounds(50, 5, 30, 20);
        pnlMinMaxControls.add(txtMin);
        lblMax = new JLabel("Max");
        lblMax.setBounds(85, 5, 40, 20);
        pnlMinMaxControls.add(lblMax);
        txtMax = new JTextField();
        txtMax.setBounds(130, 5, 30, 20);
        pnlMinMaxControls.add(txtMax);
        chkMandatory = new JCheckBox("Mandatory");
        chkMandatory.setBounds(10, 25, 100, 20);
        pnlMinMaxControls.add(chkMandatory);
        populateList();
    }

    private void openValidationsDoc() {
        BufferedReader r = null;
        try {
            r = currentProject.getBufferedReader("validations.xml", null);
            nodValidations = XmlSource.read(r);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void populateList() {
        lstValidations.removeAll();
        DefaultListModel mdl = new DefaultListModel();
        String[] validations = currentProject.getValidations();
        for (int i = 0; i < validations.length; i++) {
            mdl.addElement(validations[i]);
        }
        lstValidations.setModel(mdl);
    }

    public void valueChanged(ListSelectionEvent evt) {
        XmlElement ele = currentProject.getValidation((String) lstValidations.getSelectedValue());
        DebugLogger.logWarning(ele.getAttribute("type"));
        cmbValidationTypes.setSelectedItem(ele.getAttribute("type"));
    }

    public void itemStateChanged(ItemEvent evt) {
        if (cmbValidationTypes.getSelectedItem() == null) {
            pnlMinMaxControls.setVisible(false);
        } else if (((String) cmbValidationTypes.getSelectedItem()).compareTo(this.VALIDATION_MINMAX) == 0) {
            pnlMinMaxControls.setVisible(true);
        } else {
            pnlMinMaxControls.setVisible(false);
        }
    }

    public void changeValidation(String validationName) {
    }

    public String addValidation(String newValidation) {
        txtValidationName.setEnabled(true);
        txtValidationName.setText(newValidation);
        btnNew.setEnabled(false);
        btnSave.setEnabled(true);
        btnCancel.setEnabled(true);
        lstValidations.setEnabled(false);
        lstValidations.setSelectedIndex(-1);
        setVisible(true);
        if (bRet) return txtValidationName.getText(); else return null;
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(btnSave)) {
            bRet = true;
            setVisible(false);
        } else if (evt.getSource().equals(btnCancel)) {
            bRet = false;
            setVisible(false);
        }
    }
}
