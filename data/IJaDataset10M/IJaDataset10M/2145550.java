package org.metaphile.tag.exif.tiff;

import org.metaphile.tag.*;
import org.metaphile.type.Rational;

public class XResolution implements IMarkerBasedTag, ITag {

    private Object value = null;

    public int getMarker() {
        return 0x0011A;
    }

    public String getDescription() {
        return "The number of pixels per ResolutionUnit in the ImageWidth direction";
    }

    public int getMaximumLength() {
        return 1;
    }

    public int getMinimumLength() {
        return 1;
    }

    public String getName() {
        return "X Resolution";
    }

    public Class getType() {
        return Rational.class;
    }

    public String getValueAsString() {
        return String.valueOf(value);
    }

    public Object getValue() {
        return ((Rational) value).toFractionString();
    }

    public boolean isDeprecated() {
        return false;
    }

    public boolean isMandatory() {
        return false;
    }

    public boolean isRepeatable() {
        return false;
    }

    public void setValue(Object newValue) {
        this.value = newValue;
    }
}
