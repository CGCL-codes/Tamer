package org.xfc.editor;

import java.awt.BorderLayout;
import javax.swing.JTextField;

/**
 *
 *
 * @author Devon Carew
 */
public class XLabelEditor extends XStringEditor {

    /**
	 * 
	 */
    public XLabelEditor() {
        super();
    }

    /**
	 * 
	 * 
	 * @param title
	 */
    public XLabelEditor(String title) {
        super(title);
    }

    public void setTextField(JTextField textField) {
        this.textField = textField;
        textField.setBorder(null);
        textField.setEditable(false);
        add(textField, BorderLayout.CENTER);
    }

    public void setValue(Object value) {
        super.setValue(value);
        invalidate();
    }
}
