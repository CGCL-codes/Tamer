package com.bluesky.plum.uimodels.standard.components;

import java.util.Date;
import com.bluesky.plum.richdomain.RichDomainField;
import com.bluesky.plum.uimodels.standard.FieldUIComponent;

public class DateTimePicker extends FieldUIComponent {

    private Date datetime;

    public DateTimePicker(RichDomainField field) {
        super(field);
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    @Override
    public Object getDomainFieldValue() {
        return getDatetime();
    }

    @Override
    public void setDomainFieldValue(Object value) {
        setDatetime((Date) value);
    }
}
