package org.nakedobjects.reflector.java.value;

import org.nakedobjects.application.value.Color;
import org.nakedobjects.object.InvalidEntryException;
import org.nakedobjects.object.TextEntryParseException;
import org.nakedobjects.object.value.ColorValue;
import org.nakedobjects.object.value.adapter.AbstractNakedValue;

public class ColorAdapter extends AbstractNakedValue implements ColorValue {

    private Color color;

    public ColorAdapter() {
        this.color = Color.WHITE;
    }

    public ColorAdapter(final Color color) {
        this.color = color;
    }

    public byte[] asEncodedString() {
        if (color == null) {
            return "NULL".getBytes();
        } else {
            return String.valueOf(color()).getBytes();
        }
    }

    public int color() {
        return color.intValue();
    }

    public String getIconName() {
        return "color";
    }

    public Object getObject() {
        return color;
    }

    public String getValueClass() {
        return color.getClass().getName();
    }

    public void parseTextEntry(final String text) throws InvalidEntryException {
        if (text == null || text.trim().equals("")) {
            color = null;
        } else {
            try {
                if (text.startsWith("0x")) {
                    setColor(Integer.parseInt(text.substring(2), 16));
                } else if (text.startsWith("#")) {
                    setColor(Integer.parseInt(text.substring(1), 16));
                } else {
                    setColor(Integer.parseInt(text));
                }
            } catch (NumberFormatException e) {
                throw new TextEntryParseException("Invalid number", e);
            }
        }
    }

    public void restoreFromEncodedString(final byte[] data) {
        String text = new String(data);
        if (text == null || text.equals("NULL")) {
            color = null;
        } else {
            setColor(Integer.valueOf(text).intValue());
        }
    }

    public void setColor(final int color) {
        this.color = new Color(color);
    }

    public String titleString() {
        return color.title();
    }

    public String toString() {
        return "ColorAdapter #" + Integer.toHexString(color()).toUpperCase();
    }
}
