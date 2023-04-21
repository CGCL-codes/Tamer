package com.google.code.jqwicket.ui.datetimepicker;

import com.google.code.jqwicket.ui.datepicker.IGenericDatePicker;

/**
 * Implementation of the <a
 * href="https://github.com/trentrichardson/jQuery-Timepicker-Addon">jQuery
 * Timepicke Addon plugin</a>
 * 
 * @author mkalina
 * 
 */
public interface IDateTimePicker extends IGenericDatePicker<DateTimePickerOptions> {

    static final CharSequence JQ_COMPONENT_NAME = "datetimepicker";
}
