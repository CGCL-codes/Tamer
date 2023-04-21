package net.sf.javaguard.classfile;

import java.io.*;

/** Representation of a 'UTF8' entry in the ConstantPool.
 *
 * @author <a href="mailto:theit@gmx.de">Thorsten Heit</a>
 * @author <a href="mailto:markw@retrologic.com">Mark Welsh</a>
 */
public class Utf8CpInfo extends CpInfo {

    /** The length of the byte array in the class file. */
    private int length;

    /** The byte array in the class file that holds the Utf8 string. */
    private byte[] dataBytes;

    /** String representation of the Utf8 string. */
    private String utf8dataString;

    /** Default constructor that creates a new Utf8CpInfo object.
   */
    protected Utf8CpInfo() {
        super(CONSTANT_Utf8);
    }

    /** Constructor that is used when appending fresh Utf8 entries to the
   * constant pool.
   * @param str the new Utf8 string
   */
    public Utf8CpInfo(String str) {
        super(CONSTANT_Utf8);
        setString(str);
        resetRefCount();
        incRefCount();
    }

    /** Decrement the reference count. Blanks the entry if no more references
   * are available.
   */
    public void decRefCount() {
        super.decRefCount();
        if (0 == getRefCount()) {
            clearString();
        }
    }

    /** Return the Utf8 data as a string.
   * @return the Utf8 data as a string
   * @see #setString
   */
    public String getString() {
        if (null == getUtf8String()) {
            try {
                setUtf8String(new String(getBytes(), "UTF8"));
            } catch (UnsupportedEncodingException uex) {
                setUtf8String(null);
            }
        }
        return getUtf8String();
    }

    /** Stores the Utf8 string in the internal byte array.
   * @param str the Utf8 string
   * @see #getString
   */
    public void setString(String str) {
        setUtf8String(str);
        byte[] bytes;
        try {
            bytes = str.getBytes("UTF8");
        } catch (UnsupportedEncodingException uex) {
            bytes = new byte[0];
        }
        setLength(bytes.length);
        setBytes(bytes);
    }

    /** Empties the internal byte array that holds the Utf8 string.
   */
    public void clearString() {
        setLength(0);
        setBytes(new byte[0]);
        setUtf8String(null);
        getString();
    }

    /** Sets the length of the Utf8 string.
   * @param len the length of the Utf8 string
   * @see #getLength
   */
    protected void setLength(int len) {
        this.length = len;
    }

    /** Returns the length of the Utf8 string.
   * @return length of the Utf8 string
   * @see #setLength
   */
    protected int getLength() {
        return length;
    }

    /** Sets the byte representation of the Utf8 string.
   * @param bytes the byte representation of the Utf8 string
   * @see #getBytes
   */
    protected void setBytes(byte[] bytes) {
        this.dataBytes = bytes;
    }

    /** Returns the byte representation of the Utf8 string.
   * @return byte representation of the Utf8 string
   * @see #setBytes
   */
    protected byte[] getBytes() {
        return dataBytes;
    }

    /** Sets the string representation of the Utf8 data.
   * @param str the string representation
   * @see #getUtf8String
   */
    protected void setUtf8String(String str) {
        this.utf8dataString = str;
    }

    /** Returns the string representation of the Utf8 data.
   * @return string representation
   * @see #setUtf8String
   */
    protected String getUtf8String() {
        return utf8dataString;
    }

    /** Read the 'info' data (the Utf8 string) following the u1tag byte.
   * @param din the input stream
   * @throws IOException if an I/O error occurs
   */
    protected void readInfo(DataInput din) throws IOException {
        setLength(din.readUnsignedShort());
        byte[] bytes = new byte[getLength()];
        din.readFully(bytes);
        setBytes(bytes);
        getString();
    }

    /** Write the 'info' data (the Utf8 string) following the u1tag byte.
   * @param dout the output stream
   * @throws IOException if an I/O error occurs
   */
    protected void writeInfo(DataOutput dout) throws IOException {
        dout.writeShort(getLength());
        if (getBytes().length > 0) {
            dout.write(getBytes());
        }
    }

    /** Dump the content of the entry to the specified file (used for debugging).
   * @param pw the print writer
   * @param cf the class file the element belongs to
   * @param index the index in the constant pool
   */
    public void dump(PrintWriter pw, ClassFile cf, int index) {
        pw.print('[');
        pw.print(index);
        pw.println("]: Utf8CpInfo");
        pw.print("  -> Utf8 string: '");
        pw.print(getString());
        pw.println('\'');
    }
}
