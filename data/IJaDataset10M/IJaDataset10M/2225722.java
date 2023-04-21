package alchemy.nec.tree;

/**
 *
 * @author Sergey Basalaev
 */
public class FunctionType extends Type {

    public Type rettype;

    public Type[] args;

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj.getClass() == FunctionType.class)) return false;
        final FunctionType other = (FunctionType) obj;
        if (!this.rettype.equals(other.rettype)) return false;
        for (int i = 0; i < args.length; i++) {
            if (!this.args[i].equals(other.args[i])) return false;
        }
        return true;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer().append('(');
        for (int i = 0; i < args.length; i++) {
            if (i != 0) buf.append(',');
            buf.append(args[i]);
        }
        buf.append(')');
        if (!rettype.equals(BuiltinType.typeNone)) {
            buf.append(':').append(rettype);
        }
        return buf.toString();
    }
}
