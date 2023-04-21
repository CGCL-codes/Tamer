package org.remus.infomngmnt.common.ui.databinding;

import org.eclipse.jface.databinding.swt.WidgetValueProperty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Slider;

/**
 * An implementation of the DataBindings IObservableValue interface for the
 * Nebula CDateTime control.
 * 
 * @author pcentgraf
 * @since Mar 8, 2007
 */
public class SliderObservableValue extends WidgetValueProperty {

    public SliderObservableValue() {
        super(SWT.Selection);
    }

    public Object getValueType() {
        return Long.class;
    }

    @Override
    protected Object doGetValue(Object source) {
        return Long.valueOf(((Slider) source).getSelection());
    }

    @Override
    protected void doSetValue(final Object source, Object value) {
        ((Slider) source).setSelection(Integer.parseInt(Long.toString((Long) value)));
    }
}
