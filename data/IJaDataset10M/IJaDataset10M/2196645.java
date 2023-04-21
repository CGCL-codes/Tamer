package proguard.classfile;

import proguard.classfile.visitor.*;
import java.io.*;

/**
 * Representation of a 'float' entry in the ConstantPool.
 *
 * @author Mark Welsh
 * @author Eric Lafortune
 */
public class FloatCpInfo extends CpInfo {

    public int u4bytes;

    /**
     * Creates a new FloatCpInfo with the given float value.
     */
    public FloatCpInfo(float value) {
        setValue(value);
    }

    protected FloatCpInfo() {
    }

    /**
     * Returns the float value of this FloatCpInfo.
     */
    public float getValue() {
        return Float.intBitsToFloat(u4bytes);
    }

    /**
     * Sets the float value of this FloatCpInfo.
     */
    public void setValue(float value) {
        u4bytes = Float.floatToIntBits(value);
    }

    public int getTag() {
        return ClassConstants.CONSTANT_Float;
    }

    protected void readInfo(DataInput din) throws IOException {
        u4bytes = din.readInt();
    }

    protected void writeInfo(DataOutput dout) throws IOException {
        dout.writeInt(u4bytes);
    }

    public void accept(ClassFile classFile, CpInfoVisitor cpInfoVisitor) {
        cpInfoVisitor.visitFloatCpInfo(classFile, this);
    }
}
