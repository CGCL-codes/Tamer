package de.enough.polish.android.pim.enough;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import de.enough.polish.android.pim.Contact;
import de.enough.polish.android.pim.PIMException;
import de.enough.polish.android.pim.PIMItem;
import de.enough.polish.android.pim.PIMList;
import de.enough.polish.android.pim.UnsupportedFieldException;

public class ContactImpl implements Contact {

    protected static class Field {

        protected final FieldInfo fieldInfo;

        protected ArrayList values;

        protected ArrayList attributes;

        public Field(FieldInfo fieldInfo) {
            this.fieldInfo = fieldInfo;
            this.values = new ArrayList();
            this.attributes = new ArrayList();
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(this.fieldInfo);
            buffer.append("\n");
            int numberOfValues = this.values.size();
            for (int i = 0; i < numberOfValues; i++) {
                Object value = this.values.get(i);
                if (value instanceof Object[]) {
                    Object[] array = (Object[]) value;
                    value = Arrays.toString(array);
                }
                buffer.append("Value[" + i + "]:" + value + ".");
                buffer.append("Attr[" + i + "]:" + this.attributes.get(i) + ".");
                buffer.append("\n");
            }
            buffer.append("\n");
            return buffer.toString();
        }
    }

    private final ContactListImpl contactList;

    private final ArrayList fields;

    private boolean isModified;

    private final long id;

    ContactImpl(long id, ContactListImpl contactList) {
        this.id = id;
        this.contactList = contactList;
        this.fields = new ArrayList();
    }

    ContactImpl(ContactListImpl contactList) {
        this(-1, contactList);
    }

    public void addBinary(int fieldId, int attributes, byte[] value, int offset, int length) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.BINARY);
        if (value == null) {
            throw new NullPointerException("The parameter value is null.");
        }
        if (length < 1) {
            throw new IllegalArgumentException("The parameter 'length' violates contraint 'length > 0'");
        }
        if (length > value.length) {
            throw new IllegalArgumentException("The parameter 'length' violates contraint 'length <= value.length'");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("The parameter 'offset' violates contraint 'offset > 0'");
        }
        if (offset >= value.length) {
            throw new IllegalArgumentException("The parameter 'offset' violates contraint 'offset < value.length'");
        }
        byte[] result = new byte[length];
        System.arraycopy(value, offset, result, 0, length);
        Field field = findOrCreateField(fieldInfo);
        field.values.add(result);
        field.attributes.add(new Integer(attributes));
        this.isModified = true;
    }

    public void addBoolean(int fieldId, int attributes, boolean value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.BOOLEAN);
        Field field = findOrCreateField(fieldInfo);
        field.values.add(new Boolean(value));
        field.attributes.add(new Integer(attributes));
        this.isModified = true;
    }

    public void addDate(int fieldId, int attributes, long value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.DATE);
        Field field = findOrCreateField(fieldInfo);
        field.values.add(new Date(value));
        field.attributes.add(new Integer(attributes));
        this.isModified = true;
    }

    public void addInt(int fieldId, int attributes, int value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.INT);
        Field field = findOrCreateField(fieldInfo);
        field.values.add(new Integer(value));
        field.attributes.add(new Integer(attributes));
        this.isModified = true;
    }

    public void addString(int fieldId, int attributes, String value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.STRING);
        if (value == null) {
            throw new NullPointerException("Parameter 'value' must not be null.");
        }
        Field field = findOrCreateField(fieldInfo);
        field.values.add(value);
        field.attributes.add(new Integer(attributes));
        this.isModified = true;
    }

    public void addStringArray(int fieldId, int attributes, String[] value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.STRING_ARRAY);
        if (value == null) {
            throw new NullPointerException("Parameter 'value' must not be null.");
        }
        Field field = findOrCreateField(fieldInfo);
        field.values.add(value);
        field.attributes.add(new Integer(attributes));
        this.isModified = true;
    }

    public void addToCategory(String category) throws PIMException {
        throw new UnsupportedOperationException();
    }

    public void commit() throws PIMException {
        if (!this.isModified) {
            return;
        }
        this.contactList.persist(this);
    }

    public int countValues(int fieldId) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        Field field = findField(fieldInfo, false);
        if (field == null) {
            return 0;
        }
        return field.values.size();
    }

    public int getAttributes(int fieldId, int index) {
        if (index < 0) {
            throw new IndexOutOfBoundsException("The index '" + index + "' must not be < 0.");
        }
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates contraint 'index > 0'");
        }
        int lastValidIndex = field.attributes.size() - 1;
        if (index > lastValidIndex) {
            throw new IndexOutOfBoundsException("The index '" + index + "' is larger then the last valid index of '" + lastValidIndex + "'");
        }
        Integer attributes = (Integer) field.attributes.get(index);
        return attributes.intValue();
    }

    public byte[] getBinary(int fieldId, int index) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.BINARY);
        Field field = findField(fieldInfo);
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates constraint 'index < numberOfValues'.");
        }
        byte[] result = (byte[]) field.values.get(index);
        return result;
    }

    public boolean getBoolean(int fieldId, int index) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.BOOLEAN);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates constraint 'index < numberOfValues'.");
        }
        Boolean result = (Boolean) field.values.get(index);
        return result.booleanValue();
    }

    public String[] getCategories() {
        return new String[0];
    }

    public long getDate(int fieldId, int index) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.DATE);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates constraint 'index < numberOfValues'.");
        }
        Date result = (Date) field.values.get(index);
        return result.getTime();
    }

    public int[] getFields() {
        int numberOfFields = this.fields.size();
        int[] fieldIds = new int[numberOfFields];
        int i = 0;
        for (Iterator iterator = this.fields.iterator(); iterator.hasNext(); ) {
            Field field = (Field) iterator.next();
            fieldIds[i] = field.fieldInfo.id;
            i++;
        }
        int[] returnFields = new int[i];
        while (--i >= 0) returnFields[i] = fieldIds[i];
        return returnFields;
    }

    public int getInt(int fieldId, int index) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.INT);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates constraint 'index < numberOfValues'.");
        }
        Integer result = (Integer) field.values.get(index);
        return result.intValue();
    }

    public PIMList getPIMList() {
        return this.contactList;
    }

    public int getPreferredIndex(int fieldId) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        return fieldInfo.preferredIndex;
    }

    public String getString(int fieldId, int index) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.STRING);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates constraint 'index < numberOfValues'.");
        }
        String result = (String) field.values.get(index);
        return result;
    }

    public String[] getStringArray(int fieldId, int index) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        if (fieldInfo == null) {
            throw new UnsupportedFieldException();
        }
        if (PIMItem.STRING_ARRAY != fieldInfo.type) {
            throw new IllegalArgumentException("The field 'fieldId' is not of type PIMItem.STRING_ARRAY");
        }
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates contraint 'index >= 0'");
        }
        if (index >= field.values.size()) {
            throw new IndexOutOfBoundsException("The parameter 'index' violates contraint 'index < numberOfValuesInField'");
        }
        return (String[]) field.values.get(index);
    }

    public boolean isModified() {
        return this.isModified;
    }

    public int maxCategories() {
        return 0;
    }

    public void removeFromCategory(String category) {
        if (category == null) {
            throw new NullPointerException();
        }
    }

    public void removeValue(int fieldId, int index) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        Field field = findField(fieldInfo);
        if (field == null) {
            throw new IllegalArgumentException();
        }
        if (index < 0) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index < numberOfValues'");
        }
        field.values.remove(index);
        field.attributes.remove(index);
    }

    public void setBinary(int fieldId, int index, int attributes, byte[] value, int offset, int length) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.BINARY);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index < numberOfValues'");
        }
        if (value == null) {
            throw new NullPointerException("Parameter 'value' must not be null.");
        }
        if (value.length == 0) {
            throw new IllegalArgumentException("Array value of parameter 'value' must not have zero elements.");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Parameter 'offset' must not have a negative value.");
        }
        if (offset >= numberOfValues) {
            throw new IllegalArgumentException("Parameter 'offset' must not be larger then number of values which is '" + numberOfValues + "'");
        }
        if (length <= 0) {
            throw new IllegalArgumentException("Parameter 'length' must not have a negative value.");
        }
        if (length > numberOfValues) {
            throw new IllegalArgumentException("Parameter 'length' must not have a value which exceeds the number of values in the field which is '" + numberOfValues + "'");
        }
        if (offset + length > numberOfValues) {
            throw new IllegalArgumentException("The sum of 'offset' and 'length' must not exceed the number of values whish is '" + numberOfValues + "'");
        }
        byte[] result = new byte[length];
        System.arraycopy(value, offset, result, 0, length);
        field.values.set(index, result);
        field.attributes.set(index, new Integer(attributes));
        this.isModified = true;
    }

    public void setBoolean(int fieldId, int index, int attributes, boolean value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.BOOLEAN);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index < numberOfValues'");
        }
        field.values.set(index, new Boolean(value));
        field.attributes.set(index, new Integer(attributes));
        this.isModified = true;
    }

    public void setDate(int fieldId, int index, int attributes, long value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.DATE);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index < numberOfValues'");
        }
        field.values.set(index, new Date(value));
        field.attributes.set(index, new Integer(attributes));
        this.isModified = true;
    }

    public void setInt(int fieldId, int index, int attributes, int value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.INT);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index < numberOfValues'");
        }
        field.values.set(index, new Integer(value));
        field.attributes.set(index, new Integer(attributes));
        this.isModified = true;
    }

    public void setString(int fieldId, int index, int attributes, String value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.STRING);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index < numberOfValues'");
        }
        field.values.set(index, value);
        field.attributes.set(index, new Integer(attributes));
        this.isModified = true;
    }

    public void setStringArray(int fieldId, int index, int attributes, String[] value) {
        FieldInfo fieldInfo = findFieldInfo(fieldId);
        checkFieldType(fieldInfo, PIMItem.STRING_ARRAY);
        Field field = findField(fieldInfo);
        if (index < 0) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index >= 0'");
        }
        int numberOfValues = field.values.size();
        if (index >= numberOfValues) {
            throw new IndexOutOfBoundsException("Parameter 'index' violates constraint 'index < numberOfValues'");
        }
        field.values.set(index, value);
        field.attributes.set(index, new Integer(attributes));
        this.isModified = true;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Contact (" + this.id + ")");
        buffer.append("\n");
        for (Iterator iterator = this.fields.iterator(); iterator.hasNext(); ) {
            Field field = (Field) iterator.next();
            buffer.append(field);
            buffer.append("\n");
        }
        buffer.append("\n");
        return buffer.toString();
    }

    void setModified(boolean modified) {
        this.isModified = modified;
    }

    /**
	 * Checks, if the fieldId corresponds with a given type.
	 * @param fieldId
	 * @param type
	 * @return
	 * @throws UnsupportedFieldException if the field itself is available for this class
	 * @throws IllegalArgumentException if the type of the field is not valid
	 */
    private void checkFieldType(FieldInfo fieldInfo, int type) {
        if (fieldInfo.type != type) {
            throw new IllegalArgumentException("The field with metadata '" + fieldInfo + "' is not of type '" + type + "'.");
        }
    }

    /**
	 * Finds the field with the given field metadata. Throws an exception if it can not be found.
	 * @param fieldInfo
	 * @return
	 */
    private Field findField(FieldInfo fieldInfo) {
        return findField(fieldInfo, true);
    }

    private Field findField(FieldInfo fieldInfo, boolean throwException) {
        for (Iterator iterator = this.fields.iterator(); iterator.hasNext(); ) {
            Field field = (Field) iterator.next();
            if (field.fieldInfo.equals(fieldInfo)) {
                return field;
            }
        }
        if (throwException) {
            throw new IndexOutOfBoundsException("The field with metadata '" + fieldInfo + "' does not have any values.");
        }
        return null;
    }

    /**
	 * 
	 * @param fieldId
	 * @return the FieldInfo object with the given fieldId. Returns never null.
	 * @throws UnsupportedFieldException if field is not supported by ContactList.
	 */
    private FieldInfo findFieldInfo(int fieldId) {
        FieldInfo fieldInfo = this.contactList.findFieldInfo(fieldId);
        if (fieldInfo == null) {
            throw new UnsupportedFieldException("The field with id '" + fieldId + "' is not supported.");
        }
        return fieldInfo;
    }

    private Field findOrCreateField(FieldInfo fieldInfo) {
        Field field = findField(fieldInfo, false);
        if (field == null) {
            field = new Field(fieldInfo);
            this.fields.add(field);
        }
        return field;
    }

    boolean isNew() {
        return this.id == -1;
    }

    long getId() {
        return this.id;
    }
}
