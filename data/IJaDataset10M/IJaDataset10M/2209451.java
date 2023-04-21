package org.jpc.classfile.constantpool;

import java.io.*;

/**
 * Abstract superclass of all constant pool entry objects.
 * @author Mike Moleschi
 */
public abstract class ConstantPoolInfo {

    static final int UTF8 = 1;

    static final int INTEGER = 3;

    static final int FLOAT = 4;

    static final int LONG = 5;

    static final int DOUBLE = 6;

    static final int CLASS = 7;

    static final int STRING = 8;

    static final int FIELDREF = 9;

    static final int METHODREF = 10;

    static final int INTERFACEMETHODREF = 11;

    static final int NAMEANDTYPE = 12;

    /**
     * Hashcode value set by a subclass and returned by this classes final
     * method.
     */
    protected int hashCode;

    public final int hashCode() {
        return hashCode;
    }

    /**
     * Write this constant pool element to the supplied <code>DataOutputStream</code>.
     * @param out stream to write to
     * @throws java.io.IOException on an underlying stream error
     */
    public abstract void write(DataOutputStream out) throws IOException;

    public abstract boolean equals(Object obj);

    /**
     * Reads the supplied input stream and constructs an appropriate
     * <code>ConstantPoolInfo</code> subclass.
     * @param in stream to read from
     * @return relevant <code>ConstantPoolInfo</code> subclass
     * @throws java.io.IOException on an underlying stream error.
     */
    public static ConstantPoolInfo construct(DataInputStream in) throws IOException {
        switch(in.readUnsignedByte()) {
            case CLASS:
                return new ClassInfo(in);
            case FIELDREF:
                return new FieldRefInfo(in);
            case METHODREF:
                return new MethodRefInfo(in);
            case INTERFACEMETHODREF:
                return new InterfaceMethodRefInfo(in);
            case STRING:
                return new StringInfo(in);
            case INTEGER:
                return new IntegerInfo(in);
            case FLOAT:
                return new FloatInfo(in);
            case LONG:
                return new LongInfo(in);
            case DOUBLE:
                return new DoubleInfo(in);
            case NAMEANDTYPE:
                return new NameAndTypeInfo(in);
            case UTF8:
                return new Utf8Info(in);
        }
        return null;
    }
}
