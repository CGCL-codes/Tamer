package org.nakedobjects.reflector.original.value;

import org.nakedobjects.applib.ValueParseException;
import org.nakedobjects.applib.value.SimpleBusinessValue;
import org.nakedobjects.object.NakedValue;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.value.adapter.AbstractNakedValue;

/**
 * Generic value adapter for all SimpleBusinessValue objects.
 */
public class SimpleBusinessValueAdapter extends AbstractNakedValue implements NakedValue {

    private SimpleBusinessValue value;

    public SimpleBusinessValueAdapter(final SimpleBusinessValue value) {
        this.value = value;
    }

    public Class getValueClass() {
        return value.getClass();
    }

    public void setValue(final SimpleBusinessValue value) {
        this.value = value;
    }

    public byte[] asEncodedString() {
        return value.asEncodedString().getBytes();
    }

    public void parseTextEntry(final String text) {
        try {
            value.parseUserEntry(text);
        } catch (ValueParseException e) {
            throw new TextEntryParseException("Can't parse " + text, e);
        }
    }

    public void restoreFromEncodedString(final byte[] data) {
        value.restoreFromEncodedString(new String(data));
    }

    public String getIconName() {
        String name = getValueClass().getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    public Object getObject() {
        return value;
    }

    public String titleString() {
        return value.toString();
    }
}
