package org.butu.gui.filters;

import java.util.Calendar;
import org.butu.gui.field.IField;
import org.butu.gui.field.ValueChangeListener;
import org.butu.gui.field.datetime.FieldDate;
import org.butu.utils.compare.CompareUtils;
import org.butu.utils.date.Interval;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;

public class VFilterPeriod extends AVFilter {

    private FieldDate dcStart;

    private FieldDate dcEnd;

    public VFilterPeriod() {
        super("������:", true);
    }

    public VFilterPeriod(boolean enabled) {
        super("������:", enabled);
    }

    protected void doCreatePartControl(Composite parent, Object layoutData) {
        final Composite dates = new Composite(parent, SWT.NONE);
        RowLayout layout = new RowLayout(SWT.HORIZONTAL);
        layout.marginTop = 0;
        layout.marginBottom = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        dates.setLayout(layout);
        dates.setLayoutData(layoutData);
        dcStart = new FieldDate(dates, SWT.BORDER);
        dcStart.addValueChangeListener(new ValueChangeListener() {

            public void valueChanged(Object newValue, IField source) {
                Interval interval = (Interval) _value;
                Calendar calendar = interval.getStart();
                if (!CompareUtils.nullsOrEquals(calendar, newValue)) {
                    interval.setStart((Calendar) newValue);
                    notifyValue();
                }
            }
        });
        dcEnd = new FieldDate(dates, SWT.BORDER);
        dcEnd.addValueChangeListener(new ValueChangeListener() {

            public void valueChanged(Object newValue, IField source) {
                Interval interval = (Interval) _value;
                Calendar calendar = interval.getEnd();
                if (!CompareUtils.nullsOrEquals(calendar, newValue)) {
                    interval.setEnd((Calendar) newValue);
                    notifyValue();
                }
            }
        });
        Interval interval = (Interval) _value;
        if (interval != null) {
            dcStart.setValue(interval.getStart());
            dcEnd.setValue(interval.getEnd());
        }
    }

    public Interval getValue() {
        return (Interval) super.getValue();
    }

    public Object doSetValue(Object value) {
        Interval interval = (Interval) value;
        if (dcStart != null && !dcStart.isDisposed() && dcEnd != null && !dcEnd.isDisposed()) {
            if (interval != null) {
                dcStart.setValue(interval.getStart());
                dcEnd.setValue(interval.getEnd());
            } else {
                dcStart.setValue(null);
                dcEnd.setValue(null);
            }
        }
        return interval;
    }
}
