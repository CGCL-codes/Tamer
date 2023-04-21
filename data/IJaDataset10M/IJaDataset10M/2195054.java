package joeq.Memory;

import joeq.Class.PrimordialClassLoader;
import joeq.Class.jq_Class;

/**
 * @author  John Whaley <jwhaley@alum.mit.edu>
 * @version $Id: Address.java 1456 2004-03-09 22:01:46Z jwhaley $
 */
public abstract class Address {

    public abstract Address peek();

    public abstract byte peek1();

    public abstract short peek2();

    public abstract int peek4();

    public abstract long peek8();

    public abstract void poke(Address v);

    public abstract void poke1(byte v);

    public abstract void poke2(short v);

    public abstract void poke4(int v);

    public abstract void poke8(long v);

    public abstract Address offset(int offset);

    public abstract Address align(int shift);

    public abstract int difference(Address v);

    public abstract boolean isNull();

    public abstract int to32BitValue();

    public abstract String stringRep();

    public static final int alignInt(int val, int shift) {
        int v = (1 << shift) - 1;
        return (val + v) & ~v;
    }

    public static final jq_Class _class;

    static {
        _class = (jq_Class) PrimordialClassLoader.loader.getOrCreateBSType("Ljoeq/Memory/Address;");
    }

    protected final Object clone() throws CloneNotSupportedException {
        throw new InternalError("cannot call clone on Address types!");
    }

    public final boolean equals(Object arg0) {
        throw new InternalError("cannot call equals on Address types!");
    }

    public final int hashCode() {
        throw new InternalError("cannot call hashCode on Address types!");
    }

    public final String toString() {
        throw new InternalError("cannot call toString on Address types!  use stringRep instead.");
    }
}
