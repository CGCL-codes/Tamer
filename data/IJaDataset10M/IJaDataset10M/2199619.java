package org.adapit.wctoolkit.propertyeditors.form.element.clazz;

import org.adapit.wctoolkit.models.util.TabbedPaneUpdatable;
import org.adapit.wctoolkit.uml.ext.core.IElement;

@SuppressWarnings({ "serial", "unchecked" })
public class ClassOperationEditor extends javax.swing.JPanel implements TabbedPaneUpdatable {

    private static final long serialVersionUID = 234576868684L;

    public ClassOperationEditor() {
        initComponents();
    }

    @SuppressWarnings("serial")
    private void initComponents() {
        operationsTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        moveUpButton = new javax.swing.JButton();
        moveDownButton = new javax.swing.JButton();
        addParamButton = new javax.swing.JButton();
        setLayout(null);
        operationsTable.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)), "Operations"));
        operationsTable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "Name", "Return Type", "Visibility", "Scope" }) {

            Class[] types = new Class[] { java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class };

            @SuppressWarnings("unchecked")
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        });
        operationsTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                operationsTablePropertyChange(evt);
            }
        });
        operationsTable.addInputMethodListener(new java.awt.event.InputMethodListener() {

            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }

            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                operationsTableInputMethodTextChanged(evt);
            }
        });
        add(operationsTable);
        operationsTable.setBounds(10, 30, 300, 160);
        addButton.setText("add");
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        add(addButton);
        addButton.setBounds(30, 210, 51, 23);
        editButton.setText("edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        add(editButton);
        editButton.setBounds(90, 210, 51, 23);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        add(deleteButton);
        deleteButton.setBounds(150, 210, 63, 23);
        moveUpButton.setText("move up");
        moveUpButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpButtonActionPerformed(evt);
            }
        });
        add(moveUpButton);
        moveUpButton.setBounds(220, 210, 73, 23);
        moveDownButton.setText("move down");
        moveDownButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveDownButtonActionPerformed(evt);
            }
        });
        add(moveDownButton);
        moveDownButton.setBounds(30, 240, 90, 23);
        addParamButton.setText("add Parameters");
        addParamButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addParamButtonActionPerformed(evt);
            }
        });
        add(addParamButton);
        addParamButton.setBounds(130, 240, 110, 23);
    }

    private void operationsTableInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
    }

    private void operationsTablePropertyChange(java.beans.PropertyChangeEvent evt) {
    }

    private void addParamButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void moveDownButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void moveUpButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.JButton addButton;

    private javax.swing.JButton addParamButton;

    private javax.swing.JButton deleteButton;

    private javax.swing.JButton editButton;

    private javax.swing.JButton moveDownButton;

    private javax.swing.JButton moveUpButton;

    private javax.swing.JTable operationsTable;

    public void setElement(IElement element) {
    }

    public void notifyElementChanged() {
    }
}
