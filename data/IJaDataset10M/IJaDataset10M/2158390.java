package org.progeeks.form.swing;

import java.awt.event.*;
import javax.swing.*;
import org.progeeks.meta.PropertyFormat;
import org.progeeks.meta.format.*;
import org.progeeks.form.*;
import org.progeeks.form.style.*;

/**
 *
 *  @version   $Revision: 4193 $
 *  @author    Paul Speed
 */
public class FormattedEditor extends AbstractSwingViewBinding {

    private JTextField component;

    private PropertyFormat format;

    private EditorListener editListener = new EditorListener();

    private String textValue;

    public FormattedEditor() {
        addStyles(BorderStyle.DEFAULT);
    }

    protected JTextField createTextField() {
        return new JTextField();
    }

    protected JComponent createComponent(View view, SwingViewEnvironment env) {
        format = env.getFormats().getFormat(view.getViewedType());
        component = createTextField();
        setComponentValue(view.getValue());
        component.addActionListener(editListener);
        component.addFocusListener(editListener);
        component.setEditable(!view.isReadOnly());
        return component;
    }

    /**
     *  Called to set the current value displayed in the component.
     */
    protected void setComponentValue(Object value) {
        String s = format.format(value);
        textValue = s;
        component.setText(s);
    }

    protected void componentChanged() {
        String text = component.getText();
        if (textValue == text || text.equals(textValue)) return;
        textValue = text;
        setPropertyValue(format.parseObject(text));
    }

    private class EditorListener implements ActionListener, FocusListener {

        public void actionPerformed(ActionEvent event) {
            componentChanged();
        }

        public void focusGained(FocusEvent event) {
        }

        public void focusLost(FocusEvent event) {
            componentChanged();
        }
    }
}
