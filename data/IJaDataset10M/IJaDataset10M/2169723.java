package mipt.aaf.edit.swing.form;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import mipt.aaf.edit.form.Field;
import mipt.gui.CheckingTextField;

/**
 * Implementation of FieldEditor through JTextComponent
 * @author Evdokimov
 */
public class TextFieldEditor extends SwingFieldEditor {

    private JTextComponent textComponent;

    /**
	 * 
	 */
    public TextFieldEditor() {
    }

    /**
	 * 
	 */
    public TextFieldEditor(JTextComponent textComponent) {
        setTextComponent(textComponent);
    }

    /**
	 * 
	 */
    public JTextComponent getTextComponent() {
        return textComponent;
    }

    /**
	 * 
	 */
    public void setTextComponent(JTextComponent editor) {
        this.textComponent = editor;
        textComponent.addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                fireFieldChanging();
            }
        });
        if (textComponent instanceof JTextField) {
            JTextField textField = (JTextField) textComponent;
            final boolean fireFieldChangeOnAction = !(textField instanceof CheckingTextField) && textField.getHorizontalAlignment() == JTextField.RIGHT;
            textField.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (fireFieldChangeOnAction) fireFieldChanging();
                    if (listener != null) listener.fieldActionPerformed(getField());
                }
            });
        }
    }

    /**
	 * @see mipt.gui.ComponentOwner#getComponent()
	 */
    public final Component getComponent() {
        return textComponent;
    }

    /**
	 * @see mipt.aaf.edit.form.FieldEditor#setValue(java.lang.Object)
	 */
    public void setValue(Object value) {
        value = correctValue(value);
        textComponent.setText(value == null ? getNullText() : value.toString());
    }

    /**
	 * Returns value that can be null
	 */
    public static Object correctValue(Object modelValue) {
        if ((modelValue instanceof Integer) && ((Integer) modelValue).intValue() == Integer.MIN_VALUE) return null; else if ((modelValue instanceof Double) && Double.isNaN(((Double) modelValue).doubleValue())) return null;
        return modelValue;
    }

    protected String getNullText() {
        return "";
    }

    /**
	 * @see mipt.aaf.edit.swing.form.SwingFieldEditor#getEditorValue()
	 */
    public Object getEditorValue() {
        return getEditorValue(textComponent.getText(), getField().getType());
    }

    public static Object getEditorValue(String text, int fieldType) {
        try {
            switch(fieldType) {
                case Field.BOOLEAN:
                    return text.equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE;
                case Field.INT:
                    return text.length() == 0 ? Integer.MIN_VALUE : new Integer(text);
                case Field.DOUBLE:
                    return text.length() == 0 ? Double.NaN : new Double(text);
                case Field.UNTYPED:
                case Field.ARRAY:
                case Field.PATH:
                case Field.SLASH:
                default:
                    return text;
            }
        } catch (NumberFormatException exc) {
            return text;
        }
    }
}
