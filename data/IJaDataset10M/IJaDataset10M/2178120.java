package org.springframework.richclient.forms;

import java.awt.Component;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;
import org.springframework.util.ObjectUtils;

/**
 * @author keith
 */
public abstract class CustomPropertyEditorSupport extends PropertyEditorSupport {

    public void setValue(Object value) {
        Object old = getValue();
        if (!ObjectUtils.nullSafeEquals(old, value)) {
            super.setValue(value);
        }
    }

    public final Component getCustomEditor() {
        return createCustomEditorControl();
    }

    protected abstract JComponent createCustomEditorControl();

    public boolean supportsCustomEditor() {
        return true;
    }

    public JComponent bind(JTextComponent component, PropertyEditor customPropertyEditor) {
        return bind(component, customPropertyEditor, ValueCommitPolicy.AS_YOU_TYPE);
    }

    public JComponent bind(JTextComponent component, final PropertyEditor customPropertyEditor, ValueCommitPolicy policy) {
        if (policy == ValueCommitPolicy.AS_YOU_TYPE) {
            new AsYouTypeTextValueSetter(component) {

                protected void componentValueChanged(Object newValue) {
                    customPropertyEditor.setAsText((String) newValue);
                }
            };
        }
        return component;
    }
}
