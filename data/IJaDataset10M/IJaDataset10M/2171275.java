package com.cosmos.swingb;

import java.awt.BorderLayout;
import java.text.Format;
import javax.swing.BorderFactory;

/**
 *
 * @author Miro
 */
public class LabeledTextField extends JBPanel {

    private JBLabel label;

    private JBFormattedTextField textField;

    public LabeledTextField() {
        this("Label:");
        textField.setText("TextField");
    }

    public LabeledTextField(String labelText) {
        this(labelText, null);
    }

    public LabeledTextField(String labelText, Format format) {
        super(new BorderLayout());
        if (labelText != null) label = new JBLabel(labelText); else label = new JBLabel();
        if (format != null) textField = new JBFormattedTextField(format); else textField = new JBFormattedTextField();
        label.setName("label");
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(label, BorderLayout.WEST);
        textField.setName("textField");
        add(textField, BorderLayout.CENTER);
    }

    public JBLabel getLabel() {
        return label;
    }

    public JBFormattedTextField getTextField() {
        return textField;
    }

    public String getLabelText() {
        return label.getText();
    }

    public void setLabelText(String labelText) {
        label.setText(labelText);
    }

    public String getText() {
        return textField.getText();
    }

    public void setText(String text) {
        textField.setText(text);
    }

    public Object getValue() {
        return textField.getValue();
    }

    public void setValue(Object value) {
        textField.setValue(value);
    }
}
