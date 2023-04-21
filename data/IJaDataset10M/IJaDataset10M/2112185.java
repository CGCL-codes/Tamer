package org.apache.jdo.impl.enhancer.classfile;

import java.io.*;
import java.util.Stack;

/**
 * Class representing a utf8 string value in the constant pool
 */
public class ConstUtf8 extends ConstBasic {

    public static final int MyTag = CONSTANTUtf8;

    private String stringValue;

    /**
     * The tag of this constant entry
     */
    public int tag() {
        return MyTag;
    }

    /**
     * return the value associated with the entry
     */
    public String asString() {
        return stringValue;
    }

    /**
     * A printable representation
     */
    public String toString() {
        return "CONSTANTUtf8(" + indexAsString() + "): " + asString();
    }

    public boolean isEqual(Stack msg, Object obj) {
        if (!(obj instanceof ConstUtf8)) {
            msg.push("obj/obj.getClass() = " + (obj == null ? null : obj.getClass()));
            msg.push("this.getClass() = " + this.getClass());
            return false;
        }
        ConstUtf8 other = (ConstUtf8) obj;
        if (!super.isEqual(msg, other)) {
            return false;
        }
        if (!this.stringValue.equals(other.stringValue)) {
            msg.push(String.valueOf("stringValue = " + other.stringValue));
            msg.push(String.valueOf("stringValue = " + this.stringValue));
            return false;
        }
        return true;
    }

    ConstUtf8(String s) {
        stringValue = s;
    }

    void formatData(DataOutputStream b) throws IOException {
        b.writeUTF(stringValue);
    }

    static ConstUtf8 read(DataInputStream input) throws IOException {
        return new ConstUtf8(input.readUTF());
    }

    void resolve(ConstantPool p) {
    }
}
