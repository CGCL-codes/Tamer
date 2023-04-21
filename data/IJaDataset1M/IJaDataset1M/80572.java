package net.sf.javaguard.classfile;

import java.io.*;

/** Representation of a 'float' entry in the ConstantPool.
 *
 * @author <a href="mailto:theit@gmx.de">Thorsten Heit</a>
 * @author <a href="mailto:markw@retrologic.com">Mark Welsh</a>
 */
public class FloatCpInfo extends CpInfo {

    /** Holds the float value, stored in a 4-byte array in the class file. */
    private float value;

    /** Default constructor that creates a new FloatCpInfo object.
   */
    protected FloatCpInfo() {
        super(CONSTANT_Float);
    }

    /** Sets the float value for the entry.
   * @param value the float value for the entry
   * @see #getFloatValue
   */
    protected void setFloatValue(float value) {
        this.value = value;
    }

    /** Returns the float value for the entry.
   * @return float value for the entry.
   * @see #setFloatValue
   */
    protected float getFloatValue() {
        return value;
    }

    /** Read the 'info' data (the float value) following the u1tag byte.
   * @param din the input stream
   * @throws IOException if an I/O error occurs
   */
    protected void readInfo(DataInput din) throws IOException {
        setFloatValue(din.readFloat());
    }

    /** Write the 'info' data (the float value) following the u1tag byte.
   * @param dout the output stream
   * @throws IOException if an I/O error occurs
   */
    protected void writeInfo(DataOutput dout) throws IOException {
        dout.writeFloat(getFloatValue());
    }

    /** Dump the content of the entry to the specified file (used for debugging).
   * @param pw the print writer
   * @param cf the class file the element belongs to
   * @param index the index in the constant pool
   */
    public void dump(PrintWriter pw, ClassFile cf, int index) {
        pw.print('[');
        pw.print(index);
        pw.println("]: FloatCpInfo");
        pw.print("  -> float value: ");
        pw.println(getFloatValue());
    }
}
