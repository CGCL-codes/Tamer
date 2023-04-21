package org.jpc.classfile.constantpool;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Constant pool element for an interface method reference.
 * @author Mike Moleschi
 */
public class InterfaceMethodRefInfo extends MethodRefInfo {

    InterfaceMethodRefInfo(DataInputStream in) throws IOException {
        super(in);
    }

    /**
     * Constructs an interface method reference with the given class and
     * name-and-type.
     * @param classIndex class constant pool index
     * @param nameAndTypeIndex name-and-type constant pool index
     */
    public InterfaceMethodRefInfo(int classIndex, int nameAndTypeIndex) {
        super(classIndex, nameAndTypeIndex);
    }

    int getTag() {
        return INTERFACEMETHODREF;
    }

    public String toString() {
        return "CONSTANT_InterfaceMethodRef_info : class=" + getClassIndex() + " : nameandtype=" + getNameAndTypeIndex();
    }
}
