package org.ncsa.foodlog.rcp.widgets;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import com.gface.date.DatePicker;
import com.gface.date.DateSelectedEvent;
import com.gface.date.DateSelectionListener;

public class DatePickerWithSelectionProvider implements ISelectionProvider {

    public class DateSelectionListenerWrapper implements DateSelectionListener {

        private ISelectionChangedListener listener;

        public DateSelectionListenerWrapper(ISelectionChangedListener listener) {
            super();
            this.listener = listener;
        }

        public void dateSelected(DateSelectedEvent e) {
            listener.selectionChanged(new SelectionChangedEvent((ISelectionProvider) this, new DateSelection(e.date)));
        }
    }

    private DatePicker datePicker;

    private Map listenerMap = new HashMap();

    public DatePickerWithSelectionProvider(Composite main) {
        datePicker = new DatePicker(main, SWT.NONE);
    }

    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        DateSelectionListenerWrapper lw = new DateSelectionListenerWrapper(listener);
        datePicker.addDateSelectionListener(lw);
        listenerMap.put(listener, lw);
    }

    public DatePicker getDatePicker() {
        return datePicker;
    }

    public ISelection getSelection() {
        return new DateSelection(datePicker.getDate());
    }

    public void removeSelectionChangedListener(ISelectionChangedListener listener) {
        DateSelectionListenerWrapper lw = (DateSelectionListenerWrapper) listenerMap.get(listener);
        datePicker.removeDateSelectionListener(lw);
        listenerMap.remove(listener);
    }

    public void setSelection(ISelection selection) {
        DateSelection ds = (DateSelection) selection;
        datePicker.setDate(ds.getDate());
    }
}
