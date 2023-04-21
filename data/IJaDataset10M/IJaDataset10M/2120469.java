package com.sun.org.apache.bcel.internal.classfile;

import com.sun.org.apache.bcel.internal.Constants;
import java.io.*;

/** 
 * This class is derived from the abstract 
 * <A HREF="com.sun.org.apache.bcel.internal.classfile.Constant.html">Constant</A> class 
 * and represents a reference to an int object.
 *
 * @version $Id: ConstantInteger.java,v 1.1.2.1 2005/07/31 23:46:36 jeffsuttor Exp $
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantInteger extends Constant implements ConstantObject {

    private int bytes;

    /** 
   * @param bytes Data
   */
    public ConstantInteger(int bytes) {
        super(Constants.CONSTANT_Integer);
        this.bytes = bytes;
    }

    /**
   * Initialize from another object.
   */
    public ConstantInteger(ConstantInteger c) {
        this(c.getBytes());
    }

    /** 
   * Initialize instance from file data.
   *
   * @param file Input stream
   * @throws IOException
   */
    ConstantInteger(DataInputStream file) throws IOException {
        this(file.readInt());
    }

    /**
   * Called by objects that are traversing the nodes of the tree implicitely
   * defined by the contents of a Java class. I.e., the hierarchy of methods,
   * fields, attributes, etc. spawns a tree of objects.
   *
   * @param v Visitor object
   */
    public void accept(Visitor v) {
        v.visitConstantInteger(this);
    }

    /**
   * Dump constant integer to file stream in binary format.
   *
   * @param file Output file stream
   * @throws IOException
   */
    public final void dump(DataOutputStream file) throws IOException {
        file.writeByte(tag);
        file.writeInt(bytes);
    }

    /**
   * @return data, i.e., 4 bytes.
   */
    public final int getBytes() {
        return bytes;
    }

    /**
   * @param bytes.
   */
    public final void setBytes(int bytes) {
        this.bytes = bytes;
    }

    /**
   * @return String representation.
   */
    public final String toString() {
        return super.toString() + "(bytes = " + bytes + ")";
    }

    /** @return Integer object
   */
    public Object getConstantValue(ConstantPool cp) {
        return new Integer(bytes);
    }
}
