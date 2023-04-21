package graphmatcher.gui;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LabeledTextField extends JPanel {

    private JLabel label;

    private JTextField field;

    public LabeledTextField(String labelText) {
        this(labelText, "", false);
    }

    public LabeledTextField(String labelText, String initialValue, boolean editable) {
        setLayout(new GridLayout(1, 2));
        label = new JLabel(labelText);
        add(label);
        field = new JTextField(initialValue);
        field.setEditable(editable);
        add(field);
    }

    public void setText(String text) {
        field.setText(text);
    }

    public String getText() {
        return field.getText();
    }

    public void setEditable(boolean editable) {
        field.setEditable(editable);
    }
}
