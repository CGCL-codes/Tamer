package com.rapidminer.gui.look;

import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import com.rapidminer.gui.look.borders.Borders;

/**
 * The editor for combo boxes.
 *
 * @author Ingo Mierswa
 */
public class RapidLookComboBoxEditor extends BasicComboBoxEditor {

    private JTextField textField;

    public static class UIResource extends RapidLookComboBoxEditor implements javax.swing.plaf.UIResource {
    }

    public void putClientProperty(Object key, Object val) {
        this.textField.putClientProperty(key, val);
    }

    public void setEnable(boolean val) {
        this.editor.setEnabled(val);
    }

    public RapidLookComboBoxEditor() {
        this.editor.removeFocusListener(this);
        this.textField = new JTextField("", 9) {

            private static final long serialVersionUID = 2183777953513585132L;

            @Override
            public void setText(String s) {
                if (getText().equals(s)) {
                    return;
                }
                super.setText(s);
            }

            @Override
            public Border getBorder() {
                return Borders.getComboBoxEditorBorder();
            }
        };
        this.editor = this.textField;
    }
}
