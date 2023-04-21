package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.util.converter.ConverterRegistry;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

/**
 * StringConverterPropertyEditor. <br>A comma separated list of values.
 */
public abstract class StringConverterPropertyEditor extends AbstractPropertyEditor {

    private Object oldValue;

    public StringConverterPropertyEditor() {
        editor = new JTextField();
        ((JTextField) editor).setBorder(LookAndFeelTweaks.EMPTY_BORDER);
    }

    @Override
    public Object getValue() {
        String text = ((JTextComponent) editor).getText();
        if (text == null || text.trim().length() == 0) {
            return null;
        } else {
            try {
                return convertFromString(text.trim());
            } catch (Exception e) {
                return oldValue;
            }
        }
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            ((JTextComponent) editor).setText("");
        } else {
            oldValue = value;
            ((JTextComponent) editor).setText(convertToString(value));
        }
    }

    protected abstract Object convertFromString(String text);

    protected String convertToString(Object value) {
        return (String) ConverterRegistry.instance().convert(String.class, value);
    }
}
