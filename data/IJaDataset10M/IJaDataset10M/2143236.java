package nl.kbna.dioscuri.module.cpu32;

import java.io.*;

public class FieldInfo {

    private int accessFlags;

    private int nameIndex;

    private int descriptorIndex;

    private int attributesCount;

    private AttributeInfo[] attributes;

    public static final int PUBLIC = 0x0001;

    public static final int PRIVATE = 0x0002;

    public static final int PROTECTED = 0x0004;

    public static final int STATIC = 0x0008;

    public static final int FINAL = 0x0010;

    public static final int VOLATILE = 0x0040;

    public static final int TRANSIENT = 0x0080;

    public static final int SYNTHETIC = 0x1000;

    public static final int ENUM = 0x4000;

    public FieldInfo(DataInputStream in, ConstantPoolInfo[] pool) throws IOException {
        accessFlags = in.readUnsignedShort();
        nameIndex = in.readUnsignedShort();
        descriptorIndex = in.readUnsignedShort();
        attributesCount = in.readUnsignedShort();
        attributes = new AttributeInfo[attributesCount];
        for (int i = 0; i < attributesCount; i++) attributes[i] = AttributeInfo.construct(in, pool);
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeShort(accessFlags);
        out.writeShort(nameIndex);
        out.writeShort(descriptorIndex);
        out.writeShort(attributesCount);
        for (int i = 0; i < attributesCount; i++) attributes[i].write(out);
    }
}
