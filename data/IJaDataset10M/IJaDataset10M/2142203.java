package net.sourceforge.jd.classfile.attributes;

import java.io.DataInputStream;
import java.io.IOException;
import net.sourceforge.jd.classfile.constants.ConstantPool;

/**

    Code_attribute {
        u2 attribute_name_index;
        u4 attribute_length;
        u2 max_stack;
        u2 max_locals;
        u4 code_length;
        u1 code[code_length];
        u2 exception_table_length;
        {       u2 start_pc;
                u2 end_pc;
                u2 handler_pc;
                u2 catch_type;
        }   exception_table[exception_table_length];
        u2 attributes_count;
        attribute_info attributes[attributes_count];
    }

 */
public class AttributeCode extends DefaultAttribute {

    private int maxStack;

    private int maxLocals;

    private int codeLength;

    private byte[] code;

    private int exceptionTableLength;

    private ExceptionTable[] exceptionTable;

    private int attributesCount;

    private DefaultAttribute[] attributes;

    public AttributeCode(int name_index, int length, ConstantPool constantPool, DataInputStream dis) throws IOException {
        super(name_index, length, constantPool, dis);
        this.maxStack = readUnsignedShort();
        this.maxLocals = readUnsignedShort();
        readCode();
        readExceptions();
        readAttributes();
    }

    public AttributeCode(int name_index, int length, int maxStack, int maxLocals, ConstantPool constantPool) {
        super(name_index, length, constantPool);
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
    }

    public AttributeCode(int nameIndex, int length, int maxStack, int maxLocals, byte[] code, ExceptionTable[] exceptionTable, DefaultAttribute[] attributes, ConstantPool constantPool) {
        super(nameIndex, length, constantPool);
        this.maxStack = maxStack;
        this.maxLocals = maxLocals;
        this.code = code;
        setExceptionTable(exceptionTable);
        setAttribute(attributes);
    }

    private void readCode() throws IOException {
        codeLength = readInt();
        code = new byte[codeLength];
        read(code);
    }

    private void readExceptions() throws IOException {
        exceptionTableLength = readUnsignedShort();
        exceptionTable = new ExceptionTable[exceptionTableLength];
        for (int i = 0; i < exceptionTableLength; i++) {
            exceptionTable[i] = new ExceptionTable(getDis());
        }
    }

    /**
     * Read attributes for field or method 
     * @param dis DataInputStream
     * @throws IOException
     */
    private void readAttributes() throws IOException {
        attributesCount = readUnsignedShort();
        attributes = new DefaultAttribute[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            attributes[i] = DefaultAttribute.readAttribute(getDis(), constantPool);
        }
    }

    private void setExceptionTable(ExceptionTable[] exceptionTable) {
        if (exceptionTable != null) {
            exceptionTableLength = exceptionTable.length;
            this.exceptionTable = exceptionTable;
        }
    }

    private void setAttribute(DefaultAttribute[] attributes) {
        if (attributes != null) {
            attributesCount = attributes.length;
            this.attributes = attributes;
        }
    }
}
