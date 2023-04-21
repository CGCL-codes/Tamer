package org.nakedobjects.reflector.java.value;

import org.nakedobjects.application.value.Quantity;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.value.IntegerValue;
import org.nakedobjects.object.value.adapter.AbstractNakedValue;
import java.text.NumberFormat;
import java.text.ParseException;

public class QuantityAdapter extends AbstractNakedValue implements IntegerValue {

    private static NumberFormat FORMAT = NumberFormat.getNumberInstance();

    private Quantity quantity;

    public QuantityAdapter() {
        this.quantity = null;
    }

    public QuantityAdapter(final Quantity quantity) {
        this.quantity = quantity;
    }

    public byte[] asEncodedString() {
        String asString = Integer.toString(quantity.intValue());
        return asString.getBytes();
    }

    public String getIconName() {
        return "quantity";
    }

    public Object getObject() {
        return quantity;
    }

    public String getValueClass() {
        return String.class.getName();
    }

    public int integerValue() {
        return quantity.intValue();
    }

    public void parseTextEntry(final String entry) {
        if (entry == null || entry.trim().equals("")) {
            quantity = null;
        } else {
            try {
                int intValue = FORMAT.parse(entry).intValue();
                quantity = new Quantity(intValue);
            } catch (ParseException e) {
                throw new TextEntryParseException("Invalid number", e);
            }
        }
    }

    public void restoreFromEncodedString(final byte[] data) {
        String text = new String(data);
        int value = Integer.valueOf(text).intValue();
        quantity = new Quantity(value);
    }

    public void setValue(int value) {
        quantity = new Quantity(value);
    }

    public String titleString() {
        return quantity == null ? "" : quantity.title();
    }

    public String toString() {
        return "QunatityAdapter: " + quantity;
    }
}
