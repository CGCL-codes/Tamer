package org.vps.bcv;

class CP_Class extends ConstantPool {

    int name_index;

    String className;

    CP_Class(Rdr rdr, ClassFile cl, int tag) {
        super(cl, tag);
        name_index = rdr.read2();
    }

    String getValue() {
        return className;
    }

    void resolve() {
        className = cl.get_cp_as(name_index, CONSTANT_Utf8).getValue();
    }

    public String toString() {
        return "CPClass : " + getValue();
    }
}
