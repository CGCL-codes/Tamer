package self.micromagic.util;

import java.io.Serializable;

public class BooleanRef extends ObjectRef implements Serializable {

    public boolean value;

    public BooleanRef() {
        this.value = false;
    }

    public BooleanRef(boolean value) {
        this.value = value;
    }

    public BooleanRef(Boolean value) {
        this.value = value.booleanValue();
    }

    public boolean isBoolean() {
        return true;
    }

    public boolean booleanValue() {
        return this.value;
    }

    public static boolean getBooleanValue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        if (obj instanceof Number) {
            return ((Number) obj).doubleValue() != 0;
        }
        if (obj instanceof String) {
            return ((String) obj).equalsIgnoreCase("true");
        }
        return false;
    }

    public void setObject(Object obj) {
        this.value = BooleanRef.getBooleanValue(obj);
    }

    public Object getObject() {
        return this.value ? Boolean.TRUE : Boolean.FALSE;
    }

    public void setBoolean(boolean value) {
        this.value = value;
    }

    public boolean getBoolean() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public boolean equals(Object other) {
        int result = this.shareEqual(other, BooleanRef.class);
        if (result != MORE_EQUAL) {
            return result == TRUE_EQUAL;
        }
        return this.value == ((BooleanRef) other).value;
    }
}
