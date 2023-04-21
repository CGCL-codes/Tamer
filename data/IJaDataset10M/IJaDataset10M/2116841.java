package org.agilercp.ui.internal.beans;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Benedikt Arnold
 */
public class ButtonClickObservable extends AbstractObservableValue {

    private final Button button;

    private final DisposeListener disposeListener = new DisposeListener() {

        public void widgetDisposed(final DisposeEvent e) {
            ButtonClickObservable.this.dispose();
        }
    };

    private boolean selectionValue;

    private final Listener updateListener = new Listener() {

        public void handleEvent(final Event event) {
            notifyChanged(false, true);
        }
    };

    /**
     * @param button
     */
    public ButtonClickObservable(final Button button) {
        this.button = button;
        this.button.addDisposeListener(disposeListener);
        init();
    }

    @Override
    public synchronized void dispose() {
        super.dispose();
        if (!button.isDisposed()) {
            button.removeListener(SWT.Selection, updateListener);
            button.removeListener(SWT.DefaultSelection, updateListener);
        }
    }

    @Override
    public Object doGetValue() {
        return button.getSelection() ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public void doSetValue(final Object value) {
        final boolean oldSelectionValue = selectionValue;
        selectionValue = value == null ? false : ((Boolean) value).booleanValue();
        button.setSelection(selectionValue);
        notifyChanged(oldSelectionValue, selectionValue);
    }

    public Object getValueType() {
        return Boolean.TYPE;
    }

    /**
     * Notifies consumers with a value change event only if a change occurred.
     * 
     * @param oldValue
     * @param newValue
     */
    void notifyChanged(final boolean oldValue, final boolean newValue) {
        fireValueChange(Diffs.createValueDiff(Boolean.FALSE, Boolean.TRUE));
    }

    private void init() {
        button.addListener(SWT.Selection, updateListener);
        button.addListener(SWT.DefaultSelection, updateListener);
    }
}
