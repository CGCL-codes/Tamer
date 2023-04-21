package ee.ioc.cs.vsle.iconeditor;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.border.TitledBorder;
import javax.swing.*;
import ee.ioc.cs.vsle.editor.*;
import static ee.ioc.cs.vsle.iconeditor.ClassFieldsTableModel.*;

/**
 * Dialog for specifying class properties. The class (class or relation)
 * cannot be exported from the application in XML format unless all necessary
 * properties have been set. Currently the required parameters include the
 * class name, class title and a class icon in GIF format.
 * <p>Title: Class Properties Dialog</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * @author Aulo Aasmaa, Ando Saabas
 * @version 1.0
 */
public class ClassPropertiesDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private final JButton bttnOk = new JButton("OK");

    private final JButton bttnCancel = new JButton("Cancel");

    private final JButton bttnNewField = new JButton("Add New Field");

    private final JButton bttnDelField = new JButton("Delete Selected Fields");

    private final JButton bttnBrowseIcon = new JButton("...");

    private final JPanel pnlLabels = new JPanel();

    private final JPanel pnlFields = new JPanel();

    private final JPanel pnlProps = new JPanel();

    private final JPanel pnlTable = new JPanel();

    private final JPanel pnlButtons = new JPanel();

    private final JPanel pnlTableButtons = new JPanel();

    private final JPanel pnlClassIcon = new JPanel();

    private final JPanel pnlRelation = new JPanel();

    private final JLabel lblClassName = new JLabel("Class Name:");

    private final JLabel lblClassDesc = new JLabel("Class Description:");

    private final JLabel lblClassIcon = new JLabel("Class Icon:");

    private final JLabel lblRelation = new JLabel("Class Is Relation:");

    private final JTextField fldClassName = new JTextField();

    private final JTextField fldClassDesc = new JTextField();

    private final JTextField fldClassIcon = new JTextField("default.gif");

    private final JCheckBox chkRelation = new JCheckBox();

    private TitledBorder pnlTableTitle = BorderFactory.createTitledBorder("Class Fields");

    private boolean emptyValuesValid = true;

    ListSelectionListener listListener;

    private JTable tblClassFields;

    private ClassFieldsTableModel initialCfTableModel;

    private ClassFieldsTableModel cfTableModel;

    ListSelectionModel selectionModel;

    private JScrollPane spTableScrollPane;

    /**
	 * Class constructor.
	 */
    public ClassPropertiesDialog(ClassFieldsTableModel cfTblModel, boolean emptyValid) {
        this.setTitle("Class Properties");
        initialCfTableModel = cfTblModel;
        cfTableModel = new ClassFieldsTableModel();
        cfTableModel.setDataVector(new Vector<Vector<String>>(cfTblModel.getDataVector()));
        setEmptyValuesValid(emptyValid);
        tblClassFields = new JTable(cfTableModel);
        selectionModel = tblClassFields.getSelectionModel();
        spTableScrollPane = new JScrollPane(tblClassFields, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pnlTable.setBorder(pnlTableTitle);
        setEmptyValuesValid(true);
        pnlButtons.add(bttnOk);
        pnlButtons.add(bttnCancel);
        pnlLabels.setPreferredSize(new Dimension(110, 90));
        pnlLabels.setMinimumSize(pnlLabels.getPreferredSize());
        pnlLabels.setMaximumSize(pnlLabels.getPreferredSize());
        pnlLabels.setLayout(new GridLayout(4, 1));
        pnlLabels.add(lblClassName);
        pnlLabels.add(lblClassDesc);
        pnlLabels.add(lblClassIcon);
        pnlLabels.add(lblRelation);
        pnlClassIcon.setLayout(new BorderLayout());
        pnlClassIcon.add(fldClassIcon, BorderLayout.CENTER);
        pnlClassIcon.add(bttnBrowseIcon, BorderLayout.EAST);
        pnlRelation.setLayout(new BorderLayout());
        pnlRelation.add(chkRelation, BorderLayout.WEST);
        pnlRelation.add(new JLabel(" "), BorderLayout.CENTER);
        pnlFields.setPreferredSize(new Dimension(250, 90));
        pnlFields.setMinimumSize(pnlFields.getPreferredSize());
        pnlFields.setMaximumSize(pnlFields.getPreferredSize());
        pnlFields.setLayout(new GridLayout(4, 1));
        pnlFields.add(fldClassName);
        pnlFields.add(fldClassDesc);
        pnlFields.add(pnlClassIcon);
        pnlFields.add(pnlRelation);
        pnlProps.setPreferredSize(new Dimension(360, 110));
        pnlProps.setMinimumSize(pnlProps.getPreferredSize());
        pnlProps.setMaximumSize(pnlProps.getPreferredSize());
        pnlProps.add(pnlLabels);
        pnlProps.add(pnlFields);
        pnlTableButtons.setLayout(new GridLayout(1, 2));
        pnlTableButtons.add(bttnNewField);
        pnlTableButtons.add(bttnDelField);
        pnlTable.setPreferredSize(new Dimension(300, 150));
        pnlTable.setMinimumSize(pnlTable.getPreferredSize());
        pnlTable.setMaximumSize(pnlTable.getPreferredSize());
        pnlTable.setLayout(new BorderLayout());
        pnlTable.add(spTableScrollPane, BorderLayout.CENTER);
        pnlTable.add(pnlTableButtons, BorderLayout.SOUTH);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(pnlProps, BorderLayout.NORTH);
        getContentPane().add(pnlTable, BorderLayout.CENTER);
        getContentPane().add(pnlButtons, BorderLayout.SOUTH);
        initialize();
        bttnDelField.setEnabled(false);
        bttnOk.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent evt) {
                stopCellEditing();
                storeVariables();
                if (valuesValid()) {
                    dispose();
                }
            }
        });
        bttnCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        bttnBrowseIcon.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent evt) {
                browseIcon();
            }
        });
        bttnNewField.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent evt) {
                stopCellEditing();
                addEmptyClassField();
            }
        });
        bttnDelField.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent evt) {
                stopCellEditing();
                delClassField();
            }
        });
        tblClassFields.getTableHeader().addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                stopCellEditing();
                int column = tblClassFields.convertColumnIndexToModel(tblClassFields.getColumnModel().getColumnIndexAtX(e.getX()));
                if (e.getClickCount() == 1 && column != -1) {
                    sortByField(column);
                }
            }
        });
        selectionModel.addListSelectionListener(listListener = new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (tblClassFields.getSelectedRowCount() > 0) {
                    bttnDelField.setEnabled(true);
                } else {
                    bttnDelField.setEnabled(false);
                }
            }
        });
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(410, 350));
        setResizable(false);
        setModal(true);
        pack();
        setVisible(true);
    }

    /**
     * Stops cell editing if a cell is being edited to store the
     * value to the table model.
     */
    private void stopCellEditing() {
        int col = tblClassFields.getEditingColumn();
        int row = tblClassFields.getEditingRow();
        if (row > -1 && col > -1) tblClassFields.getCellEditor(row, col).stopCellEditing();
    }

    /**
	 * Add an empty row to the end of the DBResult and scroll the added row visible.
	 */
    private void addEmptyClassField() {
        String[] emptyRow = { "", "", "", "" };
        if (tblClassFields.getRowCount() > 1 && tblClassFields.getSelectedRowCount() > 0) {
            int rowIdx = tblClassFields.getSelectedRow() + tblClassFields.getSelectedRowCount();
            cfTableModel.insertRow(rowIdx, emptyRow);
            tblClassFields.setRowSelectionInterval(rowIdx, rowIdx);
        } else {
            cfTableModel.addRow(emptyRow);
            int idx = Math.max(tblClassFields.getSelectedRowCount(), tblClassFields.getRowCount() - 1);
            tblClassFields.setRowSelectionInterval(idx, idx);
        }
    }

    /**
	 * Deletes selected rows from the table.
	 */
    private void delClassField() {
        if (tblClassFields.getRowCount() > 0 && tblClassFields.getSelectedRowCount() > 0) {
            int firstSelected = tblClassFields.getSelectedRow();
            int selected = firstSelected;
            while (selected > -1) {
                cfTableModel.removeRow(selected);
                selected = tblClassFields.getSelectedRow();
            }
            firstSelected = Math.min(firstSelected, tblClassFields.getRowCount() - 1);
            if (firstSelected > -1) tblClassFields.setRowSelectionInterval(firstSelected, firstSelected);
        }
    }

    /**
	 * Initializes the property fields with runtime variables.
	 */
    private void initialize() {
        if (IconEditor.className != null) fldClassName.setText(IconEditor.className);
        if (IconEditor.classDescription != null) fldClassDesc.setText(IconEditor.classDescription);
        if (IconEditor.getClassIcon() != null) fldClassIcon.setText(IconEditor.getClassIcon());
        chkRelation.setSelected(IconEditor.classIsRelation);
    }

    /**
	 * Updates class properties. Invoked by the press
	 * on the Ok button.
	 */
    private void storeVariables() {
        String className = fldClassName.getText();
        if (className != null) className = className.trim();
        IconEditor.className = className;
        String classTitle = fldClassDesc.getText();
        if (classTitle != null) classTitle = classTitle.trim();
        IconEditor.classDescription = classTitle;
        String classIcon = fldClassIcon.getText();
        if (classIcon != null) {
            classIcon = classIcon.trim();
        }
        IconEditor.setClassIcon(classIcon);
        boolean relation = chkRelation.isSelected();
        IconEditor.classIsRelation = relation;
    }

    /**
	 * Browses for the class icon and sets the browsed path
	 * to the Class Icon text field.
	 */
    private void browseIcon() {
        JFileChooser fc = new JFileChooser(RuntimeProperties.getLastPath());
        fc.setFileFilter(IconEditor.getFileFilter("gif"));
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String packageDir = IconEditor.getPackageFile().getParent();
            if (packageDir == null || !file.getAbsolutePath().startsWith(packageDir)) {
                JOptionPane.showMessageDialog(this, "Path is not relative to the package", "Error", JOptionPane.ERROR_MESSAGE);
                fldClassIcon.setText(null);
                return;
            }
            String relativePath = file.getAbsolutePath().substring(packageDir.length() + 1);
            relativePath = relativePath.replaceAll("\\\\", "/");
            fldClassIcon.setText(relativePath);
            RuntimeProperties.setLastPath(file.getAbsolutePath());
        }
    }

    /**
	 * Validate values.
	 * @return boolean
	 */
    private boolean valuesValid() {
        boolean valid = true;
        if (!this.emptyValuesValid) {
            if (this.fldClassName.getText() == null || (this.fldClassName != null && this.fldClassName.getText().trim().length() == 0)) {
                valid = false;
                JOptionPane.showMessageDialog(null, "Please define class name.", "Missing Property", JOptionPane.INFORMATION_MESSAGE);
                fldClassName.requestFocus();
            } else if (this.fldClassDesc.getText() == null || (this.fldClassDesc != null && this.fldClassDesc.getText().trim().length() == 0)) {
                valid = false;
                JOptionPane.showMessageDialog(null, "Please define class description.", "Missing Property", JOptionPane.INFORMATION_MESSAGE);
                fldClassDesc.requestFocus();
            } else if (this.fldClassIcon.getText() == null || (this.fldClassIcon != null && this.fldClassIcon.getText().trim().length() == 0)) {
                valid = false;
                JOptionPane.showMessageDialog(null, "Please define class icon.", "Missing Property", JOptionPane.INFORMATION_MESSAGE);
                fldClassIcon.requestFocus();
            }
        }
        if (this.fldClassIcon != null && this.fldClassIcon.getText() != null) {
            String icon = this.fldClassIcon.getText().trim().toLowerCase();
            if (icon.length() > 0 && !(icon.endsWith(".gif") || icon.endsWith(".png"))) {
                valid = false;
                this.fldClassIcon.setText("");
                IconEditor.setClassIcon("");
                JOptionPane.showMessageDialog(null, "Only icons in GIF or PNG format allowed.", "Invalid icon format", JOptionPane.INFORMATION_MESSAGE);
                fldClassIcon.requestFocus();
            }
        }
        if (valid) {
            if (!classFieldsValid()) {
                valid = false;
            }
        }
        if (valid) {
            if (classFieldsValid()) {
                cfTableModel.removeEmptyRows();
                initialCfTableModel.setDataVector(new Vector<Vector<String>>(cfTableModel.getDataVector()));
            }
        }
        return valid;
    }

    /**
	 * Validate class fields. Field name cannot be undefined if field
	 * type or value are defined. The entire row can be empty, but the
	 * field name is always required if either field type or value is defined.
	 * @return boolean - class fields valid or not.
	 */
    private boolean classFieldsValid() {
        boolean valid = true;
        for (int i = 0; i < cfTableModel.getRowCount(); i++) {
            String fieldName = cfTableModel.getValueAt(i, iNAME);
            String fieldType = cfTableModel.getValueAt(i, iTYPE);
            String fieldValue = cfTableModel.getValueAt(i, iVALUE);
            boolean bFieldNameDefined = false;
            boolean bFieldTypeDefined = false;
            boolean bFieldValueDefined = false;
            if (fieldName != null) {
                fieldName = fieldName.trim();
                cfTableModel.setValueAt(fieldName, i, iNAME);
                if (fieldName.length() > 0) bFieldNameDefined = true;
            }
            if (fieldType != null) {
                fieldType = fieldType.trim();
                cfTableModel.setValueAt(fieldType, i, iTYPE);
                if (fieldType.length() > 0) bFieldTypeDefined = true;
            }
            if (fieldValue != null) {
                fieldValue = fieldValue.trim();
                cfTableModel.setValueAt(fieldValue, i, iVALUE);
                if (fieldValue.length() > 0) bFieldValueDefined = true;
            }
            if (bFieldNameDefined) {
                if (fieldName != null && fieldName.indexOf(":") > -1) {
                    valid = false;
                    tblClassFields.setRowSelectionInterval(i - 1, i - 1);
                    JOptionPane.showMessageDialog(null, "Field name cannot contain \":\"." + "\n" + "Please correct the field name at row no. " + i + ".", "Error in field name", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
            if (bFieldTypeDefined) {
                if (fieldType != null && fieldType.indexOf(":") > -1) {
                    valid = false;
                    tblClassFields.setRowSelectionInterval(i - 1, i - 1);
                    JOptionPane.showMessageDialog(null, "Field type cannot contain \":\"." + "\n" + "Please correct the field type at row no. " + i + ".", "Error in field type", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
            if ((bFieldTypeDefined || bFieldValueDefined) && !bFieldNameDefined) {
                valid = false;
                tblClassFields.setRowSelectionInterval(i - 1, i - 1);
                JOptionPane.showMessageDialog(null, "Field name is required if either field type or value is defined." + "\n" + "Please correct the field definition at row no. " + i + ".", "Error in field definition", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
        return valid;
    }

    /**
	 * Defines if empty values are valid or not. If not valid,
	 * then the dialog cannot be closed unless all class properties
	 * are defined. The latter case is used for exporting the
	 * image to Class or Relation.
	 * @param b boolean - valid or not.
	 */
    public void setEmptyValuesValid(boolean b) {
        this.emptyValuesValid = b;
    }

    /**
	 * Sort the table by {@code column}.
	 * @param column index of the sort column
	 */
    void sortByField(int column) {
        cfTableModel.sort(column);
    }

    public static void main(String[] args) {
        JDialog d = new ClassPropertiesDialog(new ClassFieldsTableModel(), true);
        d.setVisible(true);
    }
}
