package org.ws4d.java.structures;

public abstract class Set extends DataStructure {

    private static final String CLASS_SHORT_NAME = "Set";

    public String getClassShortName() {
        return CLASS_SHORT_NAME;
    }

    public boolean containsAll(DataStructure data) {
        if (size() > data.size()) {
            return false;
        }
        return super.containsAll(data);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set other = (Set) obj;
        if (other.size() != size()) {
            return false;
        }
        return other.containsAll(this);
    }

    public int hashCode() {
        int hashCode = 0;
        for (Iterator it = iterator(); it.hasNext(); ) {
            Object o = it.next();
            if (o != null) {
                hashCode += o.hashCode();
            }
        }
        return hashCode;
    }
}
