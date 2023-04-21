package org.apache.bcel.classfile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.apache.bcel.Constants;

/** 
 * This class is derived from the abstract 
 * <A HREF="org.apache.bcel.classfile.Constant.html">Constant</A> class 
 * and represents a reference to a String object.
 *
 * @version $Id: ConstantString.java 386056 2006-03-15 11:31:56Z tcurdt $
 * @author  <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantString extends Constant implements ConstantObject {

    private int string_index;

    /**
     * Initialize from another object.
     */
    public ConstantString(ConstantString c) {
        this(c.getStringIndex());
    }

    /** 
     * Initialize instance from file data.
     *
     * @param file Input stream
     * @throws IOException
     */
    ConstantString(DataInputStream file) throws IOException {
        this(file.readUnsignedShort());
    }

    /**
     * @param string_index Index of Constant_Utf8 in constant pool
     */
    public ConstantString(int string_index) {
        super(Constants.CONSTANT_String);
        this.string_index = string_index;
    }

    /**
     * Called by objects that are traversing the nodes of the tree implicitely
     * defined by the contents of a Java class. I.e., the hierarchy of methods,
     * fields, attributes, etc. spawns a tree of objects.
     *
     * @param v Visitor object
     */
    public void accept(Visitor v) {
        v.visitConstantString(this);
    }

    /**
     * Dump constant field reference to file stream in binary format.
     *
     * @param file Output file stream
     * @throws IOException
     */
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(tag);
        file.writeShort(string_index);
    }

    /**
     * @return Index in constant pool of the string (ConstantUtf8).
     */
    public final int getStringIndex() {
        return string_index;
    }

    /**
     * @param string_index the index into the constant of the string value
     */
    public final void setStringIndex(int string_index) {
        this.string_index = string_index;
    }

    /**
     * @return String representation.
     */
    public final String toString() {
        return super.toString() + "(string_index = " + string_index + ")";
    }

    /** @return String object
     */
    public Object getConstantValue(ConstantPool cp) {
        Constant c = cp.getConstant(string_index, Constants.CONSTANT_Utf8);
        return ((ConstantUtf8) c).getBytes();
    }

    /** @return dereferenced string
     */
    public String getBytes(ConstantPool cp) {
        return (String) getConstantValue(cp);
    }
}
