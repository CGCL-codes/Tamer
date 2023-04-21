package com.sodad.weka.gui;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyEditor;
import javax.swing.JTextField;

/** 
 * Support for a PropertyEditor that uses text.
 * Isn't going to work well if the property gets changed
 * somewhere other than this field simultaneously
 *
 * @author Len Trigg (trigg@cs.waikato.ac.nz)
 * @version $Revision: 1.8 $
 */
class PropertyText extends JTextField {

    /** for serialization */
    private static final long serialVersionUID = -3915342928825822730L;

    /** The property editor */
    private PropertyEditor m_Editor;

    /**
   * Sets up the editing component with the supplied editor.
   *
   * @param pe the PropertyEditor
   */
    PropertyText(PropertyEditor pe) {
        super((pe.getAsText().equals("null")) ? "" : pe.getAsText());
        m_Editor = pe;
        addKeyListener(new KeyAdapter() {

            public void keyReleased(KeyEvent e) {
                updateEditor();
            }
        });
        addFocusListener(new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                updateEditor();
            }
        });
    }

    /**
   * Attempts to update the textfield value from the editor.
   */
    protected void updateUs() {
        try {
            setText(m_Editor.getAsText());
        } catch (IllegalArgumentException ex) {
        }
    }

    /**
   * Attempts to update the editor value from the textfield.
   */
    protected void updateEditor() {
        try {
            m_Editor.setAsText(getText());
        } catch (IllegalArgumentException ex) {
        }
    }
}
