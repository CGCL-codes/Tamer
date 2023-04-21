package org.epistem.code.type;

/**
 * A method signature.
 *  
 * @author dmain
 */
public final class Signature {

    /** The method name */
    public final String name;

    /** The parameter types */
    public final ValueType[] paramTypes;

    private int hashcode = -1;

    public Signature(String name, ValueType... paramTypes) {
        this.paramTypes = paramTypes;
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Signature)) return false;
        Signature sig = (Signature) obj;
        if (!sig.name.equals(name)) return false;
        if (sig.paramTypes.length != paramTypes.length) return false;
        for (int i = 0; i < paramTypes.length; i++) {
            if (!paramTypes[i].equals(sig.paramTypes[i])) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        if (hashcode == -1) {
            hashcode = name.hashCode();
            for (ValueType vt : paramTypes) {
                hashcode *= vt.hashCode();
            }
        }
        return hashcode;
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder(name);
        buff.append("(");
        boolean first = true;
        for (ValueType vt : paramTypes) {
            if (first) first = false; else buff.append(",");
            buff.append(vt.name);
        }
        buff.append(")");
        return buff.toString();
    }
}
