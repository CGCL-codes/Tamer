package org.monet.bpi.java;

import java.util.Date;
import org.monet.bpi.BPIFieldDate;
import org.monet.bpi.types.DateTime;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.library.LibraryDate;
import org.monet.kernel.model.Indicator;
import org.monet.kernel.model.Language;
import org.monet.kernel.utils.DateFormat;

public class BPIFieldDateImpl extends BPIFieldImpl<DateTime> implements BPIFieldDate {

    @Override
    public DateTime get() {
        String formattedValue = this.getIndicatorValue(Indicator.VALUE);
        String dateValue = this.getIndicatorValue(Indicator.INTERNAL);
        Date date = null;
        if (dateValue != "") date = LibraryDate.parseDate(dateValue);
        return new DateTime(date, formattedValue);
    }

    public void set(Date value) {
        this.set(new DateTime(value, ""));
    }

    @Override
    public void set(DateTime value) {
        String dateInternal = null;
        Date dateValue;
        if (value.getValue() != null) {
            dateInternal = LibraryDate.getDateAndTimeString(value.getValue(), Language.getCurrent(), LibraryDate.Format.INTERNAL, true, Strings.BAR45);
            dateValue = value.getValue();
        } else {
            dateInternal = (String) value.getFormattedValue();
            dateValue = LibraryDate.parseDate(dateInternal);
        }
        this.setIndicatorValue(Indicator.INTERNAL, dateInternal);
        this.setIndicatorValue(Indicator.VALUE, DateFormat.format("dd/MM/yyyy", dateValue));
    }

    @Override
    public boolean equals(DateTime value) {
        return this.get().equals(value);
    }

    @Override
    public void clear() {
        this.set((Date) null);
    }
}
