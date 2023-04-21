package org.nakedobjects.reflector.original.value;

import org.nakedobjects.applib.ValueParseException;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.value.DateValue;
import org.nakedobjects.object.value.adapter.AbstractNakedValue;
import org.nakedobjects.utility.NotImplementedException;
import java.util.Date;

public class DateValueAdapter extends AbstractNakedValue implements DateValue {

    private final org.nakedobjects.applib.value.Date date;

    public DateValueAdapter(final org.nakedobjects.applib.value.Date date) {
        this.date = date;
    }

    public Class getValueClass() {
        return org.nakedobjects.applib.value.Date.class;
    }

    public Date dateValue() {
        return date.dateValue();
    }

    public void setValue(final Date date) {
        this.date.setValue(date);
    }

    public byte[] asEncodedString() {
        throw new NotImplementedException();
    }

    public void parseTextEntry(final String text) {
        try {
            date.parseUserEntry(text);
        } catch (ValueParseException e) {
            throw new TextEntryParseException(e);
        }
    }

    public void restoreFromEncodedString(final byte[] data) {
        throw new NotImplementedException();
    }

    public String getIconName() {
        return "date";
    }

    public Object getObject() {
        return date;
    }

    public String titleString() {
        return date.title();
    }
}
