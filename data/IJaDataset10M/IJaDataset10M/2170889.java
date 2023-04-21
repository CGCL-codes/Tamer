package com.secheresse.superImageResizer.ui.field;

import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.event.DocumentEvent;

public class IntegerField extends JFormattedTextField {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4763354812880605021L;

    private int value;

    public IntegerField() {
        super();
        setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        setValue(new Integer(0));
    }

    public int getInteger() {
        return (Integer) getValue();
    }

    public void setInteger(int value) {
        setText(Integer.toString(value));
        try {
            commitEdit();
        } catch (ParseException e) {
        }
    }

    public void changedUpdate(DocumentEvent e) {
        checkValue();
    }

    public void insertUpdate(DocumentEvent e) {
        checkValue();
    }

    public void removeUpdate(DocumentEvent e) {
        checkValue();
    }

    public void checkValue() {
        try {
            int v = Integer.parseInt(getText());
            setValue(v);
        } catch (Exception ex) {
            setText(Integer.toString(value));
        }
    }
}
