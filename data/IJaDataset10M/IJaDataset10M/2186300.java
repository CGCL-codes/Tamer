package se.marianna.simpleDB.BaseValues;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import se.marianna.simpleDB.SimpleDBValue;

/**
 *
 * @author darwin
 */
public class BooleanValue implements SimpleDBValue {

    public static final BooleanValue FALSE = new BooleanValue(false);

    public static final BooleanValue TRUE = new BooleanValue(true);

    final boolean value;

    public BooleanValue(boolean value) {
        this.value = value;
    }

    public SimpleDBValue fromDataInputStream(DataInputStream datainput) throws IOException {
        if (datainput.readBoolean()) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    public void toDataOutputStream(DataOutputStream dataOut) throws IOException {
        dataOut.writeBoolean(value);
    }

    public int compare(Object o1, Object o2) {
        return (((BooleanValue) o2).value == ((BooleanValue) o1).value ? 0 : (((BooleanValue) o1).value ? 1 : -1));
    }

    public boolean equals(Object other) {
        return ((BooleanValue) other).value == this.value;
    }

    public int hashCode() {
        return value ? 1 : 0;
    }

    public String toString() {
        return value ? "true" : "false";
    }

    public boolean toBoolean() {
        return value;
    }

    public SimpleDBValue fromString(String valueAsString) {
        if (valueAsString.toLowerCase().trim().equals("true")) {
            return TRUE;
        } else {
            return FALSE;
        }
    }

    public static final BooleanValue instance = new BooleanValue(false);
}
