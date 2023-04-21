package com.sun.j2me.pim;

/**
 * Encapsulation of a single-value PIM field
 *
 */
class ScalarPIMField implements PIMField {

    /** Current value of the field. */
    private Object value;

    /** Field attributes. */
    private int attributes;

    /**
     * Adds a value to a PIM field.
     * @param attributes properties to update
     * @param value entry to be updated
     */
    public void addValue(int attributes, Object value) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * Gets the value of the field.
     * @param index element index
     * @return field value
     */
    public Object getValue(int index) {
        return value;
    }

    /**
     * Sets the value of the field.
     * @param attributes field attributes requested
     * @param value new value for field
     * @param index element identifier
     */
    public void setValue(int attributes, Object value, int index) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * Gets the field attributes.
     * @param index element identifier
     * @return encoded attributes
     */
    public int getAttributes(int index) {
        checkIndex(index);
        return attributes;
    }

    /**
     * Checks if the field contains data.
     * @return <code>true</code> if field contains data
     */
    public boolean containsData() {
        return true;
    }

    /**
     * Gets the number of value elements.
     * @return always returns 1
     */
    public int getValueCount() {
        return 1;
    }

    /**
     * Removes a value element.
     * @param index identifier for value to remove
     */
    public void removeValue(int index) {
    }

    /**
     * Checks if field has scalar value.
     * @return always returns true
     */
    public boolean isScalar() {
        return true;
    }

    /**
     * Ensures that index is value value offset.
     * @param index offset for value
     */
    private void checkIndex(int index) {
        if (index != 0) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }
}
