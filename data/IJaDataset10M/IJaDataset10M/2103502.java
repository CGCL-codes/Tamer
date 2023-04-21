package com.pentagaia.tb.databrowser.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.pentagaia.tb.databrowser.api.ser.IClassDescField;
import com.pentagaia.tb.databrowser.api.ser.IFieldValue;
import com.pentagaia.tb.databrowser.api.ser.ISerializedClassDesc;
import com.pentagaia.tb.databrowser.api.ser.ISerializedObject;

/**
 * Field value
 * 
 * @author mepeisen
 * @version 0.1.0
 * @since 0.1.0
 */
class FieldValueImpl extends SerializedBase implements IFieldValue {

    /** Class descriptor */
    private final ISerializedClassDesc classDescriptor;

    /** Field descriptor */
    private final IClassDescField fieldDescriptor;

    /** Primitive data */
    private final byte[] primitives;

    /** Object value */
    private final ISerializedObject objectValue;

    /**
     * Constructor for primitives
     * 
     * @param classDesc
     * @param field
     * @param dataArray
     */
    public FieldValueImpl(final ISerializedClassDesc classDesc, final IClassDescField field, final byte[] dataArray) {
        this.classDescriptor = classDesc;
        this.fieldDescriptor = field;
        this.primitives = dataArray;
        this.objectValue = null;
    }

    /**
     * Constructor for primitives
     * 
     * @param classDesc
     * @param field
     * @param value
     */
    public FieldValueImpl(final ISerializedClassDesc classDesc, final IClassDescField field, final ISerializedObject value) {
        this.classDescriptor = classDesc;
        this.fieldDescriptor = field;
        this.primitives = null;
        this.objectValue = value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.IFieldValue#getClassDescriptor()
     */
    public ISerializedClassDesc getClassDescriptor() {
        return this.classDescriptor;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.IFieldValue#getFieldDescriptor()
     */
    public IClassDescField getFieldDescriptor() {
        return this.fieldDescriptor;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.IFieldValue#primitiveData()
     */
    public byte[] primitiveData() {
        return this.primitives;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.IFieldValue#getObjectData()
     */
    public ISerializedObject getObjectData() {
        return this.objectValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.impl.SerializedBase#toString(java.lang.String, java.util.List)
     */
    @Override
    public String toString(String indent, List<Object> recursion) {
        final StringBuffer buffer = new StringBuffer();
        buffer.append(indent);
        buffer.append("Field-Value(");
        final List<Object> list = new ArrayList<Object>(recursion);
        list.add(recursion);
        buffer.append(((SerializedBase) this.classDescriptor).toString("", recursion));
        buffer.append("/");
        buffer.append(this.fieldDescriptor.getName());
        buffer.append(")@");
        buffer.append(this.hashCode());
        if (recursion.contains(this)) {
            return buffer.toString();
        }
        recursion.add(this);
        buffer.append(":\n");
        buffer.append(indent);
        buffer.append("  >Value: ");
        switch(this.fieldDescriptor.getType()) {
            case TYPE_BOOLEAN:
                buffer.append("(boolean) ");
                buffer.append(this.primitives[0] == 0 ? "false" : "true");
                break;
            case TYPE_BYTE:
                buffer.append("(byte) ");
                buffer.append(this.primitives[0]);
                break;
            case TYPE_CHAR:
                buffer.append("(char) ");
                buffer.append(String.format("{0:X2}", this.primitives[0]));
                buffer.append(" ");
                buffer.append(String.format("{0:X2}", this.primitives[1]));
                break;
            case TYPE_DOUBLE:
                buffer.append("(double) ");
                try {
                    buffer.append(new DataInputStream(new ByteArrayInputStream(this.primitives)).readDouble());
                } catch (IOException e) {
                    buffer.append("???");
                }
                break;
            case TYPE_FLOAT:
                buffer.append("(float) ");
                try {
                    buffer.append(new DataInputStream(new ByteArrayInputStream(this.primitives)).readFloat());
                } catch (IOException e) {
                    buffer.append("???");
                }
                break;
            case TYPE_INTEGER:
                buffer.append("(integer) ");
                try {
                    buffer.append(new DataInputStream(new ByteArrayInputStream(this.primitives)).readInt());
                } catch (IOException e) {
                    buffer.append("???");
                }
                break;
            case TYPE_LONG:
                buffer.append("(long) ");
                try {
                    buffer.append(new DataInputStream(new ByteArrayInputStream(this.primitives)).readLong());
                } catch (IOException e) {
                    buffer.append("???");
                }
                break;
            case TYPE_SHORT:
                buffer.append("(short) ");
                try {
                    buffer.append(new DataInputStream(new ByteArrayInputStream(this.primitives)).readShort());
                } catch (IOException e) {
                    buffer.append("???");
                }
                break;
            case TYPE_OBJECT:
            case TYPE_ARRAY:
                buffer.append("(object)\n");
                buffer.append(((SerializedBase) this.objectValue).toString(indent + "    ", recursion));
                break;
        }
        return buffer.toString();
    }
}
