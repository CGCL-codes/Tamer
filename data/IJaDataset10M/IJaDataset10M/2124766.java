package jcc;

public interface EVMConst {

    public static final int EVM_CLASS_ACC_PUBLIC = 0x01;

    public static final int EVM_CLASS_ACC_FINAL = 0x10;

    public static final int EVM_CLASS_ACC_SUPER = 0x20;

    public static final int EVM_CLASS_ACC_INTERFACE = 0x40;

    public static final int EVM_CLASS_ACC_ABSTRACT = 0x80;

    public static final int EVM_FIELD_ACC_PUBLIC = 0x01;

    public static final int EVM_FIELD_ACC_PRIVATE = 0x02;

    public static final int EVM_FIELD_ACC_PROTECTED = 0x04;

    public static final int EVM_FIELD_ACC_STATIC = 0x08;

    public static final int EVM_FIELD_ACC_FINAL = 0x10;

    public static final int EVM_FIELD_ACC_VOLATILE = 0x40;

    public static final int EVM_FIELD_ACC_TRANSIENT = 0x80;

    public static final int EVM_METHOD_ACC_PUBLIC = 0x01;

    public static final int EVM_METHOD_ACC_PRIVATE = 0x02;

    public static final int EVM_METHOD_ACC_PROTECTED = 0x04;

    public static final int EVM_METHOD_ACC_STATIC = 0x08;

    public static final int EVM_METHOD_ACC_FINAL = 0x10;

    public static final int EVM_METHOD_ACC_SYNCHRONIZED = 0x20;

    public static final int EVM_METHOD_ACC_NATIVE = 0x40;

    public static final int EVM_METHOD_ACC_ABSTRACT = 0x80;

    public static final int EVM_CONSTANT_Utf8 = 1;

    public static final int EVM_CONSTANT_Unicode = 2;

    public static final int EVM_CONSTANT_Integer = 3;

    public static final int EVM_CONSTANT_Float = 4;

    public static final int EVM_CONSTANT_Long = 5;

    public static final int EVM_CONSTANT_Double = 6;

    public static final int EVM_CONSTANT_Class = 7;

    public static final int EVM_CONSTANT_String = 8;

    public static final int EVM_CONSTANT_Fieldref = 9;

    public static final int EVM_CONSTANT_Methodref = 10;

    public static final int EVM_CONSTANT_InterfaceMethodref = 11;

    public static final int EVM_CONSTANT_NameAndType = 12;

    public static final int EVM_CONSTANT_POOL_ENTRY_RESOLVED = 0x80;

    public static final int EVM_CONSTANT_POOL_ENTRY_RESOLVING = 0x40;

    public static final int EVM_CONSTANT_POOL_ENTRY_TYPEMASK = 0x3F;

    public static final int EVM_INVOKE_JAVA_METHOD = 0;

    public static final int EVM_INVOKE_JAVA_SYNC_METHOD = 1;

    public static final int EVM_INVOKE_EVM_METHOD = 2;

    public static final int EVM_INVOKE_JNI_METHOD = 3;

    public static final int EVM_INVOKE_JNI_SYNC_METHOD = 4;

    public static final int EVM_INVOKE_ABSTRACT_METHOD = 5;

    public static final int ObjHeaderWords = 2;

    public static final int BytesPerWord = 4;
}
