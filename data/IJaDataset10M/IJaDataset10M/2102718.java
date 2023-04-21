package edu.rice.cs.cunit.classFile.attributes;

import edu.rice.cs.cunit.classFile.ClassFileTools;
import edu.rice.cs.cunit.classFile.attributes.visitors.IAttributeVisitor;
import edu.rice.cs.cunit.classFile.code.instructions.LineNumberTable;
import edu.rice.cs.cunit.classFile.constantPool.*;
import edu.rice.cs.cunit.classFile.constantPool.visitors.ADefaultPoolInfoVisitor;
import edu.rice.cs.cunit.classFile.constantPool.visitors.CheckClassVisitor;
import edu.rice.cs.cunit.util.Types;
import java.util.ArrayList;

/**
 * Represents the Exceptions attribute in a class file.
 *
 * @author Mathias Ricken
 */
public class ExceptionsAttributeInfo extends AAttributeInfo {

    /**
     * Constructor.
     *
     * @param name attribute name
     * @param data attribute data
     * @param cp   constant pool
     *
     * @throws ClassFormatError
     */
    public ExceptionsAttributeInfo(AUTFPoolInfo name, byte[] data, ConstantPool cp) throws ClassFormatError {
        super(name, data, cp);
    }

    /**
     * Return the number of checked exceptions.
     *
     * @return number of exceptions
     *
     * @throws ClassFormatError
     */
    public int getExceptionCount() throws ClassFormatError {
        int res = Types.ushortFromBytes(_data, 0);
        assert (res <= 0xffff);
        return res;
    }

    /**
     * Return the checked exceptions list.
     *
     * @return list of checked exceptions. May contain null!
     *
     * @throws ClassFormatError
     */
    public ClassPoolInfo[] getExceptions() throws ClassFormatError {
        int count = getExceptionCount();
        assert (count <= 0xffff);
        ClassPoolInfo[] cpi = new ClassPoolInfo[count];
        for (short i = 0; i < count; ++i) {
            cpi[i] = _constantPool.get(Types.ushortFromBytes(_data, 2 + 2 * i)).execute(new ADefaultPoolInfoVisitor<ClassPoolInfo, Object>() {

                public ClassPoolInfo defaultCase(APoolInfo host, Object o) {
                    throw new ClassFormatError("Checked exceptions list must contain class or empty items.");
                }

                public ClassPoolInfo classCase(ClassPoolInfo host, Object o) {
                    return host;
                }

                public ClassPoolInfo emptyCase(EmptyPoolInfo host, Object o) {
                    return null;
                }
            }, null);
        }
        return cpi;
    }

    /**
     * Set the checked exceptions list.
     *
     * @param cpi list of checked exceptions
     *
     * @throws ClassFormatError
     */
    public void setExceptions(ClassPoolInfo[] cpi) {
        byte[] newData = new byte[2 + 2 * cpi.length];
        Types.bytesFromShort((short) cpi.length, newData, 0);
        for (int i = 0; i < cpi.length; ++i) {
            if (cpi[i] != null) {
                Types.bytesFromShort(_constantPool.indexOf(cpi[i].execute(CheckClassVisitor.singleton(), null)), newData, 2 + 2 * i);
            } else {
                newData[2 + 2 * i] = 0;
                newData[3 + 2 * i] = 0;
            }
        }
        setData(newData);
    }

    /**
     * Return a human-readable version of this attribute.
     *
     * @return string
     */
    public String toString() {
        StringBuilder x = new StringBuilder();
        x.append(_name + " <" + getExceptionCount() + " exceptions { ");
        boolean first = true;
        for (ClassPoolInfo cpi : getExceptions()) {
            if (first) {
                first = false;
            } else {
                x.append(", ");
            }
            if (cpi != null) {
                x.append(ClassFileTools.getClassName(cpi.getName().toString()));
            } else {
                x.append("empty item");
            }
        }
        x.append(" } >");
        return x.toString();
    }

    /**
     * Execute a visitor on this attribute.
     *
     * @param visitor visitor
     * @param param   visitor-specific parameter
     *
     * @return visitor-specific return value
     */
    public <R, D> R execute(IAttributeVisitor<R, D> visitor, D param) {
        return visitor.exceptionsCase(this, param);
    }

    /**
     * Adjust program counter values contained in this attribute, starting at startPC, by adding deltaPC to them.
     *
     * @param startPC program counter to start at
     * @param deltaPC change in program counter values
     */
    public void adjustPC(int startPC, int deltaPC) {
    }

    /**
     * Translate the program counter values contained in this attribute from an old line number table to a new one.
     *
     * @param index      critical point (insertion or deletion point)
     * @param deltaIndex delta value to add to all old line numbers greater than the critical point
     * @param oldLnt     old line number table
     * @param newLnt     new line number table
     */
    public void translatePC(int index, int deltaIndex, LineNumberTable oldLnt, LineNumberTable newLnt) {
    }

    /**
     * Creates and returns a copy of this object.
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * Returns the name of the attribute as it appears in the class file.
     *
     * @return name of the attribute.
     */
    public static String getAttributeName() {
        return "Exceptions";
    }
}
