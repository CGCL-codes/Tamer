package org.vps.bcv;

interface C {

    int CONSTANT_Class = 7;

    int CONSTANT_Fieldref = 9;

    int CONSTANT_Methodref = 10;

    int CONSTANT_InterfaceMethodref = 11;

    int CONSTANT_String = 8;

    int CONSTANT_Integer = 3;

    int CONSTANT_Float = 4;

    int CONSTANT_Long = 5;

    int CONSTANT_Double = 6;

    int CONSTANT_NameAndType = 12;

    int CONSTANT_Utf8 = 1;

    int CONSTANT_WASTED = 34;

    int ACC_PUBLIC = 0x0001;

    int ACC_PRIVATE = 0x0002;

    int ACC_PROTECTED = 0x0004;

    int ACC_STATIC = 0x0008;

    int ACC_FINAL = 0x0010;

    int ACC_SUPER = 0x0020;

    int ACC_SYNCHRONIZED = 0x0020;

    int ACC_VOLATILE = 0x0040;

    int ACC_TRANSIENT = 0x0080;

    int ACC_NATIVE = 0x0100;

    int ACC_INTERFACE = 0x0200;

    int ACC_ABSTRACT = 0x0400;

    int ACC_STRICT = 0x0800;

    int ACC_ANNOTATION = 0x2000;

    int ACC_ENUM = 0x4000;

    int ACC_BRIDGE = 0x0040;

    int ACC_VARARGS = 0x0080;

    int ACC_SYNTHETIC = 0x1000;

    int ITEM_Bogus = 0;

    int ITEM_Integer = 1;

    int ITEM_Float = 2;

    int ITEM_Double = 3;

    int ITEM_Long = 4;

    int ITEM_Null = 5;

    int ITEM_InitObject = 6;

    int ITEM_Object = 7;

    int ITEM_NewObject = 8;

    int I_WIDE = 0xc4;

    int T_BOOLEAN = 4;

    int T_CHAR = 5;

    int T_FLOAT = 6;

    int T_DOUBLE = 7;

    int T_BYTE = 8;

    int T_SHORT = 9;

    int T_INT = 10;

    int T_LONG = 11;
}
