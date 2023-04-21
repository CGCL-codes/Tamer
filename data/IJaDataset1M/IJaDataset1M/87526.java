package tufts.vue.beans;

import java.lang.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.beans.*;

public class StringEditor extends PropertyEditorSupport {

    /** the set value **/
    String mValue = null;

    /** the custom editor **/
    private JTextField mEditor = null;

    /**
	 * Constructs a new StringEditor
	 **/
    public StringEditor() {
        super();
        mEditor = new JTextField();
    }

    /**
	 * setValue
	 * Sets the value of the editor
	 * @param Object new value
	 **/
    public void setValue(Object pValue) {
        boolean hasChanged = false;
        String newValue = null;
        if (pValue == null) {
            hasChanged = (mValue != null);
            newValue = "";
        } else if (pValue instanceof String) {
            hasChanged = ((mValue == null) || (!mValue.equals((String) pValue)));
            if (hasChanged) {
                newValue = (String) pValue;
            }
        } else {
            hasChanged = ((mValue == null) || (!mValue.equals(pValue.toString())));
            if (hasChanged) {
                newValue = pValue.toString();
            }
        }
        if (hasChanged) {
            mValue = newValue;
            firePropertyChange();
        }
    }

    /**
	 * getValue
	 * @return Object the string value
	 **/
    public Object getValue() {
        String value = mEditor.getText();
        return value;
    }

    /**
	 * isPaintable
	 * Can this editor paint a value of itself
	 * #return false nope it can't
	 **/
    public boolean isPaintable() {
        return false;
    }

    /**
	 * PaintValue
	 * Paints the value in the box provided
	 * Not supported in this editor at this time
	 **/
    public void paintValue(Graphics pGraphics, Rectangle pBox) {
    }

    /**
	 * getJavaInitializationString
	 * Returns the constructor text
	 **/
    public String getJavaInitializationString() {
        char quote = '"';
        String str = "new String";
        return str;
    }

    /**
 	 * getAsText
	 * returns the value as text
	 **/
    public String getAsText() {
        return mEditor.getText();
    }

    /**
	 * setAsText
	 * Sets teh value as a text value
	 **/
    public void setAsText(String pText) {
        setValue(pText);
    }

    /**
	 * getTags
	 * retruns tag values.  If used, setAsText must support tagged values
	 **/
    public String[] getTags() {
        return null;
    }

    /**
	 * getCustomEditor
	 * Returns the UI Component to edit the value
	 **/
    public Component getCustomEditor() {
        return mEditor;
    }

    /**
	 * Does this editor support custom UI editor component?
	 * @return true
	 **/
    public boolean supportsCustomEditor() {
        return true;
    }

    /**
	 * EditorFocusListener
	 * This inner cclass is used to listen to focus to save any changes to
	 * the users notes.
	 **/
    public class EditorFocusListener implements FocusListener {

        public EditorFocusListener() {
        }

        public void focusGained(FocusEvent pEvent) {
        }

        public void focusLost(FocusEvent pEvent) {
            setValue(mEditor.getText());
        }
    }
}
