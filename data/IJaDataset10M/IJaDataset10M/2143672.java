package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class CreateInputSpecDialog extends JDialog {

    public static final int SAVE = 0;

    public static final int EDIT = 1;

    public static final int CANCEL = 2;

    public static final int NEW = 3;

    private JLabel fieldNameLabel;

    private JLabel fieldValueLabel;

    private JTextField fieldNameField;

    private JTextField fieldValueField;

    private JButton saveButton;

    private JButton cancelButton;

    private CreateInputSpecDialog self;

    private int mode;

    public CreateInputSpecDialog() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        self = this;
        this.setTitle("Create/Edit Input Specification");
        fieldNameLabel = new JLabel("Field Name: ");
        fieldValueLabel = new JLabel("Field Value: ");
        fieldNameField = new JTextField(20);
        fieldValueField = new JTextField(20);
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");
        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                self.setMode(CreateInputSpecDialog.SAVE);
                self.setModal(false);
                self.setVisible(false);
            }
        });
        cancelButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                self.setMode(CreateInputSpecDialog.CANCEL);
                fieldNameField.setText(null);
                fieldValueField.setText(null);
                self.setModal(false);
                self.setVisible(false);
            }
        });
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        add(fieldNameLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        add(fieldNameField, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        add(fieldValueLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        add(fieldValueField, c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        add(saveButton, c);
        c.gridx = 3;
        c.gridy = 2;
        c.gridwidth = 1;
        add(cancelButton, c);
    }

    public String getFieldName() {
        return this.fieldNameField.getText();
    }

    public String getFieldValue() {
        return this.fieldValueField.getText();
    }

    public void setFieldName(String name) {
        this.fieldNameField.setText(name);
    }

    public void setFieldValue(String value) {
        this.fieldValueField.setText(value);
    }

    public void setMode(int i) {
        mode = i;
    }

    public int getMode() {
        return mode;
    }

    public void reset() {
        this.setFieldName(null);
        this.setFieldValue(null);
    }
}
