package org.ozoneDB.DxLib;

import java.io.*;

public class DxBoolean extends DxObject implements Externalizable {

    boolean value;

    static final long serialVersionUID = 1L;

    public DxBoolean() {
        value = false;
    }

    public DxBoolean(boolean v) {
        value = v;
    }

    public DxBoolean(DxBoolean v) {
        super();
        value = v.value;
    }

    public Object clone() {
        return new DxBoolean(value);
    }

    public boolean equals(Object obj) {
        if (obj instanceof DxBoolean && obj != null) {
            if (this == obj) {
                return true;
            }
            return value == ((DxBoolean) obj).value;
        }
        return false;
    }

    public boolean isLess(DxCompatible obj) {
        if (obj.getClass().equals(getClass())) {
            if (this == obj) {
                return false;
            }
            return !value && ((DxBoolean) obj).value;
        }
        return false;
    }

    public String toString() {
        Boolean b = new Boolean(value);
        return b.toString();
    }

    public boolean toBoolean() {
        return value;
    }

    public int hashCode() {
        Boolean b = new Boolean(value);
        return b.hashCode();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeBoolean(value);
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        value = in.readBoolean();
    }
}
