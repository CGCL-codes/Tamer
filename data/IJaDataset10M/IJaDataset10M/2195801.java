package com.peusoft.widget.calendar.datepicker.celleditor;

import com.peusoft.widget.calendar.datepicker.AbstractDatePicker;
import com.peusoft.widget.calendar.datepicker.DatePicker;

/**
 *
 * @author zhenja
 */
public class DatePickerTableCellEditor extends AbstractDatePickerTableCellEditor {

    private DatePicker datePicker;

    public DatePickerTableCellEditor() {
        super();
    }

    public DatePickerTableCellEditor(int type) {
        super(type);
    }

    @Override
    protected AbstractDatePicker getDatePicker() {
        if (datePicker == null) {
            datePicker = new DatePicker();
        }
        return datePicker;
    }
}
