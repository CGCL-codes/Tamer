package lablog.ui.widgets;

import static java.util.Calendar.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import lablog.lib.db.entity.base.EntityFieldDescriptor;
import lablog.lib.util.DateType;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;

/**
 * A viewer for date and time values.
 * 
 */
public class DateWidget extends WidgetViewer<Date> {

    private final Composite comp;

    private final DateTime date, time;

    private final DateType type;

    public DateWidget(Composite parent, EntityFieldDescriptor efd) {
        this(parent, efd.getDateType());
    }

    public DateWidget(Composite parent, DateType dateType) {
        type = dateType;
        RowLayout rl = new RowLayout();
        rl.spacing = 0;
        comp = new Composite(parent, SWT.NONE);
        comp.setLayout(rl);
        if (type.equals(DateType.DATE)) {
            date = new DateTime(comp, SWT.DATE | SWT.DROP_DOWN);
            date.addSelectionListener(new DateListener());
            time = null;
        } else if (type.equals(DateType.TIME)) {
            date = null;
            time = new DateTime(comp, SWT.TIME);
            time.addSelectionListener(new DateListener());
        } else {
            date = new DateTime(comp, SWT.DATE | SWT.DROP_DOWN);
            date.addSelectionListener(new DateListener());
            time = new DateTime(comp, SWT.TIME);
            time.addSelectionListener(new DateListener());
        }
    }

    @Override
    public Date getValue() {
        Date d;
        if (type.equals(DateType.DATE)) {
            d = ((new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay()))).getTime();
        } else if (type.equals(DateType.TIME)) {
            d = ((new GregorianCalendar(0, 0, 0, time.getHours(), time.getMinutes()))).getTime();
        } else {
            d = ((new GregorianCalendar(date.getYear(), date.getMonth(), date.getDay(), time.getHours(), time.getMinutes()))).getTime();
        }
        return d;
    }

    @Override
    public void setInput(Object input) {
        Calendar cal = null;
        if (input instanceof Calendar) {
            cal = (Calendar) input;
        } else if (input instanceof Date) {
            Date d = (Date) input;
            cal = new GregorianCalendar();
            cal.setTimeInMillis(d.getTime());
        }
        if (cal != null) {
            if (type.equals(DateType.DATE)) {
                date.setDate(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH));
            } else if (type.equals(DateType.TIME)) {
                date.setTime(cal.get(HOUR_OF_DAY), cal.get(MINUTE), cal.get(SECOND));
            } else {
                date.setDate(cal.get(YEAR), cal.get(MONTH), cal.get(DAY_OF_MONTH));
                date.setTime(cal.get(HOUR_OF_DAY), cal.get(MINUTE), cal.get(SECOND));
            }
        }
    }

    @Override
    public Control getControl() {
        return comp;
    }

    @Override
    public ISelection getSelection() {
        return new StructuredSelection(getValue());
    }

    @Override
    public void setSelection(ISelection selection, boolean arg1) {
        setInput(((StructuredSelection) selection).getFirstElement());
    }

    private class DateListener extends SelectionAdapter {

        @Override
        public void widgetSelected(SelectionEvent e) {
            fireValueChanged();
        }
    }
}
