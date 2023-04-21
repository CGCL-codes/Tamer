package org.metaphile.tag.exif.interoperability;

import java.util.ArrayList;
import org.metaphile.tag.*;
import org.metaphile.type.NullTerminatedString;

public class InteroperabilityIndex implements ILookupTag, ITag, IMarkerBasedTag {

    private Object value = null;

    private ArrayList index = new ArrayList();

    public InteroperabilityIndex() {
        index.add("R98");
        index.add("THM");
    }

    public ArrayList getActualValues() {
        return index;
    }

    public ArrayList getDisplayValues() {
        return index;
    }

    public String getDescription() {
        return "Indicates the identification of the Interoperability rule.";
    }

    public int getMaximumLength() {
        return 4;
    }

    public int getMinimumLength() {
        return 4;
    }

    public String getName() {
        return "Interoperability Index";
    }

    public Class getType() {
        return NullTerminatedString.class;
    }

    public Object getValue() {
        return ((NullTerminatedString) value).getOriginalValue();
    }

    public String getValueAsString() {
        return String.valueOf(value);
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

    public int getMarker() {
        return 0x00001;
    }
}
