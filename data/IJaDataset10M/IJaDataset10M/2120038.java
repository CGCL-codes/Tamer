package org.tolven.teditor.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.tolven.menuStructure.ColumnType;
import org.tolven.menuStructure.PlaceholderField;
import org.tolven.teditor.controller.CommandCenter;
import org.tolven.teditor.model.TreeNodes.DuplicateNodeException;

public class PlaceholderFieldEditor extends AbstractEditor implements DocumentListener, ActionListener {

    private JTextField nameField;

    private JTextField internalField;

    private JComboBox typeField;

    private JButton resetButton;

    private JButton saveButton;

    private JButton discardButton;

    private PlaceholderField model;

    /**
	 * Create the panel
	 */
    public PlaceholderFieldEditor(CommandCenter cmd) {
        super(cmd);
        createGUI();
        populateFields();
    }

    private void populateFields() {
        try {
            this.model = (PlaceholderField) cmd.getData();
            nameField.getDocument().removeDocumentListener(this);
            internalField.getDocument().removeDocumentListener(this);
            typeField.removeActionListener(this);
            nameField.setText(model.getName());
            internalField.setText(model.getInternal());
            String tField = "";
            if (model.getType() != null) tField = model.getType().value();
            typeField.setSelectedItem(tField);
            nameField.getDocument().addDocumentListener(this);
            internalField.getDocument().addDocumentListener(this);
            typeField.addActionListener(this);
            saveButton.setEnabled(false);
            discardButton.setEnabled(false);
            setModified(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        setBorder(new TitledBorder(null, "PlaceholderField", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        JLabel nameLabel = new JLabel("Name");
        JLabel internalLabel = new JLabel("Internal");
        JLabel typeLabel = new JLabel("Type");
        nameField = new JTextField();
        internalField = new JTextField();
        typeField = new JComboBox();
        typeField.addItem("");
        for (ColumnType type : ColumnType.values()) {
            typeField.addItem(type);
        }
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Thread doreset = new Thread() {

                    public void run() {
                        reset();
                    }
                };
                doreset.start();
            }
        });
        saveButton = new JButton();
        saveButton.setText("Save");
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                Thread dosave = new Thread() {

                    public void run() {
                        save();
                    }
                };
                dosave.start();
            }
        });
        discardButton = new JButton();
        discardButton.setText("Discard");
        discardButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent arg0) {
                Thread dodiscard = new Thread() {

                    public void run() {
                        discard();
                    }
                };
                dodiscard.start();
            }
        });
        final GroupLayout groupLayout = new GroupLayout((JComponent) this);
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addGap(30, 30, 30).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addComponent(resetButton).addGap(10, 10, 10).addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE).addGap(10, 10, 10).addComponent(discardButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)).addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(typeLabel, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE).addComponent(internalLabel, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE).addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE).addComponent(getPathLabel(), GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)).addGap(63, 63, 63).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(getPathField(), GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE).addComponent(nameField, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE).addComponent(internalField, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE).addComponent(typeField, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))))).addGap(41, 41, 41)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(getPathLabel()).addComponent(getPathField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(internalLabel).addComponent(internalField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(typeLabel).addComponent(typeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(16, 16, 16).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(resetButton).addComponent(saveButton).addComponent(discardButton))));
        setLayout(groupLayout);
    }

    public void reset() {
        cmd.resetData(null);
        populateFields();
        resetButton.setEnabled(false);
    }

    public void save() {
        model.setName(this.nameField.getText());
        if (typeField.getSelectedIndex() <= 0) {
            model.setType(null);
        } else {
            model.setType((ColumnType) typeField.getSelectedItem());
        }
        model.setInternal(internalField.getText());
        try {
            cmd.setData(model);
            saveButton.setEnabled(false);
            resetButton.setEnabled(true);
            discardButton.setEnabled(false);
            setModified(false);
        } catch (DuplicateNodeException e) {
            showWarningMessageAndSetFocus(e.toString(), nameField);
        }
    }

    public void discard() {
        populateFields();
    }

    public void changedUpdate(DocumentEvent arg0) {
        Thread checklater = new Thread() {

            public void run() {
                checkFields();
            }
        };
        checklater.start();
    }

    public void insertUpdate(DocumentEvent e) {
        Thread checklater = new Thread() {

            public void run() {
                checkFields();
            }
        };
        checklater.start();
    }

    public void removeUpdate(DocumentEvent e) {
        Thread checklater = new Thread() {

            public void run() {
                checkFields();
            }
        };
        checklater.start();
    }

    private void checkFields() {
        String tField = "";
        if (typeField.getSelectedIndex() >= 0) {
            tField = typeField.getSelectedItem().toString();
        }
        if (nameField.getText().equals(this.model.getName()) && internalField.getText().equals(this.model.getInternal()) && tField.equals(this.model.getType())) {
            saveButton.setEnabled(false);
            discardButton.setEnabled(false);
            setModified(false);
        } else {
            saveButton.setEnabled(true);
            discardButton.setEnabled(true);
            setModified(true);
        }
    }

    public void actionPerformed(ActionEvent e) {
        Thread checklater = new Thread() {

            public void run() {
                checkFields();
            }
        };
        checklater.start();
    }
}
