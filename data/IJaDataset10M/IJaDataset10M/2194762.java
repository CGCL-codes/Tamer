package org.horen.ui.editors.filters.operators;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

/**
 * An operand provider for integers consists of a text field like widget
 * where the user can enter a numeric number and/or change it with buttons.
 * 
 * @author Steffen
 */
public class IntegerOperandProvider extends FilterOperandProvider {

    private Spinner m_Spinner = null;

    private ModifyListener m_InternalListener = null;

    public IntegerOperandProvider() {
    }

    @Override
    public void createControl(Composite parent) {
        m_Spinner = new Spinner(parent, SWT.BORDER);
        m_Spinner.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
        m_InternalListener = new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                fireModifyText(e);
            }
        };
        m_Spinner.addModifyListener(m_InternalListener);
    }

    @Override
    public Control getControl() {
        return m_Spinner;
    }

    @Override
    public void dispose() {
        if (m_Spinner != null && m_Spinner.isDisposed() == false) {
            m_Spinner.dispose();
        }
    }

    @Override
    public Object getOperand() {
        return Integer.valueOf(m_Spinner.getSelection());
    }

    @Override
    public void setOperand(Object operand) {
        Integer newValue = null;
        if (operand instanceof Integer) {
            newValue = (Integer) operand;
        } else {
            newValue = Integer.valueOf(0);
        }
        m_Spinner.removeModifyListener(m_InternalListener);
        m_Spinner.setSelection(newValue.intValue());
        m_Spinner.addModifyListener(m_InternalListener);
    }

    @Override
    public Class<?> getOperandClass() {
        return Integer.class;
    }
}
