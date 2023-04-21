package net.sf.hippopotam.presentation.field;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import net.sf.hippopotam.util.ObjectUtil;

public class TextField extends JTextArea implements PropertyField, PropertyChangeListener {

    public TextField() {
        setWrapStyleWord(true);
        setLineWrap(true);
        addPropertyChangeListener("enabled", this);
    }

    public TextField(int rows, int colums) {
        super(rows, colums);
        setWrapStyleWord(true);
        setLineWrap(true);
    }

    public TextField(String value) {
        this();
        setStringValue(value);
    }

    public void setStringValue(String value) {
        setText(value);
    }

    public String getStringValue() {
        return ObjectUtil.trimString(getText());
    }

    public void setObjectValue(Object value) {
        setStringValue((String) value);
    }

    public Object getObjectValue() {
        return getStringValue();
    }

    public boolean validateValue() {
        return true;
    }

    public void propertyChange(PropertyChangeEvent aEvt) {
        if ("enabled".equals(aEvt.getPropertyName())) {
            if (((Boolean) aEvt.getNewValue())) {
                setBackground(UIManager.getColor("TextArea.background"));
            } else {
                setBackground(UIManager.getColor("TextField.disabledBackground"));
            }
        }
    }

    public boolean isEmpty() {
        try {
            return getObjectValue() == null;
        } catch (Exception e) {
            return false;
        }
    }
}
