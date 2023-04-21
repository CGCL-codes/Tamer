package org.progeeks.meta.swing.editor;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import org.progeeks.meta.*;
import org.progeeks.meta.format.*;
import org.progeeks.meta.swing.*;

/**
 *  Basic spinner control editor implementation.
 *
 *  @version   $Revision: 1.10 $
 *  @author    Paul Speed
 */
public abstract class AbstractSpinnerEditor extends AbstractPropertyEditor {

    private MetaPropertyContext viewContext;

    private JSpinner component;

    private Object spinnerValue;

    private EditorListener editListener = new EditorListener();

    protected AbstractSpinnerEditor() {
        this(null);
    }

    protected AbstractSpinnerEditor(MetaPropertyContext viewContext) {
        this.viewContext = viewContext;
        component = new JSpinner();
        component.addChangeListener(editListener);
    }

    /**
     *  Overridden to configure an appropriate text field formatter
     *  as necessary.  Default implementation calls getFormattedTextField()
     *  and installs the appropriate format factory for any non-null text
     *  field.  Subclasses can therefore avoid this behavior by overridding the
     *  getFormattedTextField() method to return null.  Alternately, if
     *  no MetaPropertyContext was given to this class then no attempt will
     *  be made to override formatting from default toString() behavior.
     */
    public void setPropertyMutator(PropertyMutator mutator) {
        if (mutator != null && viewContext != null) {
            PropertyType type = mutator.getPropertyInfo().getPropertyType();
            PropertyFormat format = viewContext.getFactoryRegistry().getFormatRegistry().getFormat(type);
            if (!format.getClass().equals(DefaultPropertyFormat.class)) {
                JFormattedTextField text = getFormattedTextField();
                if (text != null) {
                    text.setFormatterFactory(new DefaultFormatterFactory(new JFormatAdapter(format)));
                }
            }
        }
        super.setPropertyMutator(mutator);
    }

    /**
     *  Returns the component that allows modification of the
     *  associated property mutator.
     */
    public Component getUIComponent() {
        return (component);
    }

    /**
     *  Implemented by subclasses to release any component-related
     *  resources.
     */
    protected void releaseComponent() {
        component.removeChangeListener(editListener);
    }

    /**
     *  Kill this when all uses have been verified removed.
     *
     *  @deprecated
     */
    protected final JSpinner createSpinner() {
        return null;
    }

    protected abstract Object getEmptyValue();

    /**
     *  Called to set the component value to a default state.
     *  The default implementation calls setComponentValue(null).
     */
    protected void resetComponentValue() {
        spinnerValue = getEmptyValue();
        component.setValue(spinnerValue);
    }

    /**
     *  Called to set the current value displayed in the component.
     */
    protected void setComponentValue(Object value) {
        if (value == null) value = getEmptyValue();
        component.setValue(value);
    }

    protected void componentChanged() {
        Object val = component.getValue();
        if (val == spinnerValue || (val != null && val.equals(spinnerValue))) return;
        spinnerValue = val;
        setPropertyValue(val);
    }

    /**
     *  Returns the JFormattedTextField that is used to display the
     *  spinner values.  This method is provided for two reasons: 1)
     *  for convenience and 2) so that subclasses can return null if
     *  they'd rather not have their values auto-formatted.
     */
    protected JFormattedTextField getFormattedTextField() {
        JComponent editor = component.getEditor();
        if (!(editor instanceof JSpinner.DefaultEditor)) return (null);
        return (((JSpinner.DefaultEditor) editor).getTextField());
    }

    private class EditorListener implements ChangeListener {

        public void stateChanged(ChangeEvent event) {
            componentChanged();
        }
    }
}
