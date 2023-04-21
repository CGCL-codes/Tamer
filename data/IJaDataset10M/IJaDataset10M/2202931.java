package org.vps.bcv;

abstract class ConstantPool extends CFO implements C {

    int tag;

    ConstantPool(ClassFile cl, int tag) {
        super(cl);
        this.tag = tag;
    }

    static ConstantPool read(Rdr rdr, ClassFile cl) {
        int tag = rdr.read1();
        switch(tag) {
            case CONSTANT_Class:
                return new CP_Class(rdr, cl, tag);
            case CONSTANT_Fieldref:
                return new CP_FieldRef(rdr, cl, tag);
            case CONSTANT_Methodref:
            case CONSTANT_InterfaceMethodref:
                return new CP_MethodRef(rdr, cl, tag);
            case CONSTANT_String:
                return new CP_String(rdr, cl, tag);
            case CONSTANT_Integer:
            case CONSTANT_Float:
                return new CP_U4(rdr, cl, tag);
            case CONSTANT_Long:
            case CONSTANT_Double:
                return new CP_U8(rdr, cl, tag);
            case CONSTANT_NameAndType:
                return new CP_NAT(rdr, cl, tag);
            case CONSTANT_Utf8:
                return new CP_UTF(rdr, cl, tag);
            default:
                throw new Stop(VerifierResults.EC_CP_UNKNOWN, "Unknown constant pool tag " + tag);
        }
    }

    abstract void resolve();

    public abstract String toString();

    String getValue() {
        return null;
    }
}
