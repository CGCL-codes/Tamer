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
import org.tolven.menuStructure.Instance;
import org.tolven.teditor.controller.CommandCenter;
import org.tolven.teditor.model.TreeNodes.DuplicateNodeException;

public class InstanceEditor extends AbstractEditor implements DocumentListener, ActionListener {

    private static final long serialVersionUID = 1L;

    private JTextField headingField;

    private JTextField pageField;

    private JTextField titleField;

    private JTextField nameField;

    private JComboBox visibilityField;

    private JButton resetButton;

    private JButton saveButton;

    private JButton discardButton;

    private Instance model;

    /**
	 * Create the panel
	 */
    public InstanceEditor(CommandCenter cmd) {
        super(cmd);
        createGUI();
        populateFields();
    }

    private void populateFields() {
        try {
            this.model = (Instance) cmd.getData();
            nameField.getDocument().removeDocumentListener(this);
            titleField.getDocument().removeDocumentListener(this);
            pageField.getDocument().removeDocumentListener(this);
            headingField.getDocument().removeDocumentListener(this);
            visibilityField.removeActionListener(this);
            nameField.setText(model.getName());
            titleField.setText(model.getTitle());
            pageField.setText(model.getPage());
            visibilityField.setSelectedItem(model.getVisible());
            headingField.setText(model.getHeading());
            nameField.getDocument().addDocumentListener(this);
            titleField.getDocument().addDocumentListener(this);
            pageField.getDocument().addDocumentListener(this);
            headingField.getDocument().addDocumentListener(this);
            visibilityField.addActionListener(this);
            saveButton.setEnabled(false);
            discardButton.setEnabled(false);
            setModified(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        setBorder(new TitledBorder(null, "Instance", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        JLabel nameLabel = new JLabel("Name");
        JLabel label = new JLabel("Title");
        JLabel visibleLabel = new JLabel("Visibility");
        JLabel pageLabel = new JLabel("Page");
        JLabel headingLabel = new JLabel("Heading");
        nameField = new JTextField();
        titleField = new JTextField();
        pageField = new JTextField();
        headingField = new JTextField();
        String[] visibility = { "true", "false", "never" };
        visibilityField = new JComboBox(visibility);
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
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addGap(30, 30, 30).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addComponent(resetButton).addGap(10, 10, 10).addComponent(saveButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE).addGap(10, 10, 10).addComponent(discardButton, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)).addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(headingLabel, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE).addComponent(pageLabel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE).addComponent(label, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE).addComponent(nameLabel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE).addComponent(getPathLabel(), GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE).addComponent(visibleLabel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)).addGap(63, 63, 63).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(visibilityField, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(getPathField(), GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE).addComponent(nameField, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE).addComponent(titleField, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE).addComponent(pageField, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE).addComponent(headingField, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))))).addGap(41, 41, 41)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addContainerGap().addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(getPathLabel()).addComponent(getPathField(), GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label).addComponent(titleField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(pageLabel).addComponent(pageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(headingLabel).addComponent(headingField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(visibleLabel).addComponent(visibilityField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(10, 10, 10).addGap(16, 16, 16).addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(resetButton).addComponent(saveButton).addComponent(discardButton))));
        setLayout(groupLayout);
    }

    public void reset() {
        cmd.resetData(null);
        populateFields();
        resetButton.setEnabled(false);
    }

    public void save() {
        model.setName(this.nameField.getText());
        model.setPage(pageField.getText());
        model.setTitle(titleField.getText());
        if (visibilityField.getSelectedItem() != null) model.setVisible(visibilityField.getSelectedItem().toString());
        model.setHeading(headingField.getText());
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
        String vField = "";
        if (visibilityField.getSelectedIndex() != -1) {
            vField = visibilityField.getSelectedItem().toString();
        }
        if (nameField.getText().equals(this.model.getName()) && titleField.getText().equals(this.model.getTitle()) && pageField.getText().equals(this.model.getPage()) && headingField.getText().equals(this.model.getHeading()) && vField.equals(this.model.getVisible())) {
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
