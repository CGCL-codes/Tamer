package com.softwoehr.pigiron.access;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Implements and encapsulates the VSMAPI <tt>int1</tt> basic type.
 * 
 * @author jax
 * @see com.softwoehr.pigiron.access.VSMParm
 */
public class VSMInt1 implements VSMParm, VSMInt {

    private int value;

    private String formalName;

    /**
     * Type in terms of one of the formal parameter type discussed in
     * the VSMAPI documentation: int1, int4, int8, string, struct, array.
     * (Pigiron also recognizes <tt>counted_struct</tt>
     * as an extra type above and beyond the base types enumerated
     * by the VSMAPI documentation.)
     */
    public static final String FORMAL_TYPE = "int1";

    /**
     * Create an instance of undefined value.
     */
    public VSMInt1() {
    }

    /**
     * Create an instance of specified value.
     * @param value the value
     */
    public VSMInt1(int value) {
        this();
        setValue(value);
    }

    /**
     * Create an instance of specified value.
     * @param value the value
     */
    public VSMInt1(byte value) {
        this();
        setValue(value);
    }

    /** 
     * Create an instance of specified value and assign it a formal name
     * @param value the value
     * @param formalName the formal name
     */
    public VSMInt1(int value, String formalName) {
        this(value);
        setFormalName(formalName);
    }

    /**
     * Create an instance of specified value and assign it a formal name
     * @param value the value
     * @param formalName the formal name
     */
    public VSMInt1(byte value, String formalName) {
        this(value);
        setFormalName(formalName);
    }

    /**
     * Set the value.
     * @param value the value
     */
    public void setValue(byte value) {
        this.value = value;
    }

    /**
     * Set the value.
     * @param value the value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Get the value.
     * @return the value
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the length in bytes of the parameter.
     * @return the length in bytes of the parameter value.
     */
    public int paramLength() {
        return ParameterArray.SIZEOF_INT1;
    }

    /**
     * Read in a VSMInt1 from a stream.
     * @param in the input stream
     * @param length the byte length to read
     * @throws java.io.IOException on comm error
     */
    public void read(DataInputStream in, int length) throws IOException {
        setValue(in.readByte());
    }

    /**
     * Write a VSMInt1 on a stream.
     * @param out the output stream
     * @throws java.io.IOException on comm error
     */
    public void write(DataOutputStream out) throws java.io.IOException {
        out.writeByte(new Integer(value).byteValue());
    }

    /**
     * Get the formal name of the parameter conforming to
     * the VSMAPI docs for a given call.
     * @return the formal name of the parameter
     * @see com.softwoehr.pigiron.access.VSMParm
     */
    public String getFormalName() {
        return formalName;
    }

    /**
     * Set the formal name of the parameter conforming to
     * the VSMAPI docs for a given call.
     * @param formalName the formal name of the parameter
     * @see com.softwoehr.pigiron.access.VSMParm
     */
    public void setFormalName(String formalName) {
        this.formalName = formalName;
    }

    /**
     * Return a functional copy of the instance.
     * Convenience function to type-encapsulate <tt>clone()</tt>.
     * @return copy or null
     * @see #clone()
     */
    public VSMParm copyOf() {
        VSMParm bozo = null;
        bozo = VSMParm.class.cast(clone());
        return bozo;
    }

    /**
     * Clone the instance.
     * @return clone of the instance
     * @see #copyOf()
     */
    @Override
    public Object clone() {
        VSMInt1 proto = new VSMInt1();
        proto.setFormalName(formalName);
        proto.setValue(getValue());
        return proto;
    }

    /**
     * The value as a <tt>long</tt>.
     * @return the value as a <tt>long</tt>
     */
    public long getLongValue() {
        return new Long(getValue()).longValue();
    }

    /**
     * String representation of the instance for debugging.
     * @return String representation of the instance for debugging
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString());
        sb.append(" Formal Name == " + getFormalName() + " Formal Type == " + getFormalType());
        sb.append(" Value == " + value);
        return sb.toString();
    }

    /**
     * Get the formal type of the parameter conforming to
     * the VSMAPI docs.
     * @return the formal type of the parameter
     * @see com.softwoehr.pigiron.access.VSMParm
     */
    public String getFormalType() {
        return FORMAL_TYPE;
    }

    /**
     * Prettyprint the instance for debugging or simple output display.
     * @return Prettyprint of the instance for debugging or simple output display
     */
    public String prettyPrint() {
        StringBuffer sb = new StringBuffer();
        sb.append(getFormalName() + "(" + getFormalType() + ") " + getValue());
        return sb.toString();
    }
}
