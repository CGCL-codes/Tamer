package com.pentagaia.tb.databrowser.impl;

import java.util.ArrayList;
import java.util.List;
import com.pentagaia.tb.databrowser.api.ser.IFieldValue;
import com.pentagaia.tb.databrowser.api.ser.ISerializedClassDesc;
import com.pentagaia.tb.databrowser.api.ser.ISerializedObject;
import com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValue;
import com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValueWr;

/**
 * An object value implementation
 * 
 * @author mepeisen
 * @version 0.1.0
 * @since 0.1.0
 */
class SerializedObjectValueWrImpl extends SerializedBase implements ISerializedObjectValueWr {

    /** The class descriptor */
    private final ISerializedClassDesc descriptor;

    /** The contents */
    private final List<IFieldValue> fields = new ArrayList<IFieldValue>();

    /** The contents */
    private final List<ISerializedObject> contents = new ArrayList<ISerializedObject>();

    /** Object values */
    private final ISerializedObjectValue[] values;

    /**
     * Constructor
     * 
     * @param desc
     * @param superObject 
     */
    public SerializedObjectValueWrImpl(final ISerializedClassDesc desc, ISerializedObject superObject) {
        this.descriptor = desc;
        final List<ISerializedObjectValue> list = new ArrayList<ISerializedObjectValue>();
        if (superObject instanceof ISerializedObjectValue) {
            for (final ISerializedObjectValue object : ((ISerializedObjectValue) superObject).getSuperObjectValues()) {
                list.add(object);
            }
            list.add((ISerializedObjectValue) superObject);
        }
        this.values = list.toArray(new ISerializedObjectValue[list.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValue#getClassDescriptor()
     */
    public ISerializedClassDesc getClassDescriptor() {
        return this.descriptor;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValue#isExtBlockClass()
     */
    public boolean isExtBlockClass() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValue#isExtClass()
     */
    public boolean isExtClass() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValue#isNoWrClass()
     */
    public boolean isNoWrClass() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValue#isWrClass()
     */
    public boolean isWrClass() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObject#isArray()
     */
    public boolean isArray() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObject#isClass()
     */
    public boolean isClass() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObject#isClassDescriptor()
     */
    public boolean isClassDescriptor() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObject#isEnum()
     */
    public boolean isEnum() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObject#isNull()
     */
    public boolean isNull() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObject#isObject()
     */
    public boolean isObject() {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObject#isString()
     */
    public boolean isString() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValueWr#getFieldValues()
     */
    public IFieldValue[] getFieldValues() {
        return this.fields.toArray(new IFieldValue[this.fields.size()]);
    }

    /**
     * Add fields
     * 
     * @param field
     */
    public void addField(final IFieldValue field) {
        this.fields.add(field);
    }

    /**
     * Add contents
     * 
     * @param content
     */
    public void addContent(final ISerializedObject content) {
        this.contents.add(content);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValueWr#getContent()
     */
    public ISerializedObject[] getContent() {
        return this.contents.toArray(new ISerializedObject[this.contents.size()]);
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
        buffer.append("Object(Serializable/Write)@");
        buffer.append(this.hashCode());
        if (recursion.contains(this)) {
            return buffer.toString();
        }
        recursion.add(this);
        buffer.append(":\n");
        buffer.append(indent);
        buffer.append("  >Class-Descriptor:\n");
        if (this.descriptor == null) {
            buffer.append(indent);
            buffer.append("    NULL");
        } else {
            buffer.append(((SerializedBase) this.descriptor).toString(indent + "    ", recursion));
        }
        buffer.append("\n");
        buffer.append(indent);
        buffer.append("  >fields:\n");
        for (final IFieldValue value : this.fields) {
            buffer.append(((SerializedBase) value).toString(indent + "    ", recursion));
            buffer.append("\n");
        }
        buffer.append("\n");
        buffer.append(indent);
        buffer.append("  >Extra Content:");
        for (final ISerializedObject object : this.contents) {
            buffer.append("\n");
            buffer.append(((SerializedBase) object).toString(indent + "    ", recursion));
        }
        return buffer.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValue#getSuperObjectValues()
     */
    public ISerializedObjectValue[] getSuperObjectValues() {
        return this.values;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObject#isBlock()
     */
    public boolean isBlock() {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.pentagaia.tb.databrowser.api.ser.ISerializedObjectValue#getSuperObjectValue()
     */
    public ISerializedObjectValue getSuperObjectValue() {
        return this.values.length == 0 ? null : this.values[this.values.length - 1];
    }
}
