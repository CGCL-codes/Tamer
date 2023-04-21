package org.formaria.aria.data.table;

import org.formaria.aria.data.DataModel;

/**
 * <p>Provides a model for table field. The field will have no children. 
 * As a Java object the fields and rows indices are zero based in contrast to 
 * the JDBC indexing setup.</p>
 * <p>The class is not intended to be used directly except in rare circumstances,
 * instead most access will be via the model and bindings or via the 
 * TableModel class</p>
 * <p>Copyright (c) Formaria Ltd. 2001-2003</p>
 * $Revision: 2.2 $
 * License: see license.txt
 */
public class FieldModel extends DataModel {

    protected int rowIdx;

    protected int fieldIdx;

    protected TableModel sourceData;

    /**
   * Create a new field model node
   */
    public FieldModel() {
    }

    /**
   * Create a new field model node
   * @param table the table model
   * @param row the row index
   * @param field the field index
   */
    public FieldModel(TableModel table, int row, int field) {
        setCellReference(table, row, field);
    }

    /**
   * Get the referenced table model
   * @return the referenced table
   */
    public TableModel getTable() {
        return sourceData;
    }

    /**
   * Get the index of this fiels
   * @return the index of the column
   */
    public int getFieldIndex() {
        return fieldIdx;
    }

    /**
   * Set the reference to the field to which this node will refer
   * @param table the table that contains the data
   * @param row the row index
   * @param field the field index
   */
    public void setCellReference(TableModel table, int row, int field) {
        sourceData = table;
        rowIdx = row;
        fieldIdx = field;
    }

    /**
   * <p>This method is required by the DataModel interface but in this case it is 
   * not implemented as it is inappropriate in the context of the class</p>
   * @param newObject ignored
   */
    public void append(DataModel newObject) {
    }

    /**
   * 
   * <p>This method is required by the DataModel interface but in this case it is 
   * not implemented as it is inappropriate in the context of the class</p>
   * @param elementName ignored
   * @return nothing, instead the method throws an UnsupportedOperationException
   */
    public double getValueAsDouble(String elementName) {
        throw new java.lang.UnsupportedOperationException("Method getValueAsDouble() not yet implemented.");
    }

    /**
   * Gets the value of the field
   * @return the value
   */
    public String getValue() {
        return sourceData.getFieldValue(rowIdx, fieldIdx);
    }

    /**
   * 
   * <p>This method is required by the DataModel interface but in this case it is 
   * not implemented as it is inappropriate in the context of the class</p>
   * @param i ignored
   * @return nothing, instead the method throws an UnsupportedOperationException
   */
    public Object getAttribValue(int i) {
        throw new java.lang.UnsupportedOperationException("Method getAttribValue() not yet implemented.");
    }

    /**
   * 
   * <p>This method is required by the DataModel interface but in this case it is 
   * not implemented as it is inappropriate in the context of the class</p>
   * @param i ignored
   * @return nothing, instead the method throws an UnsupportedOperationException
   */
    public String getAttribValueAsString(int i) {
        throw new java.lang.UnsupportedOperationException("Method getAttribValueAsString() not yet implemented.");
    }

    /**
   * Gets the value attribute of the specified node as a string.
   * <p>This method is required by the DataModel interface but in this case it is 
   * not implemented as it is inappropriate in the context of the class</p>
   * @param elementName ignored
   * @return nothing, instead the method throws an UnsupportedOperationException
   */
    public String getValueAsString(String elementName) {
        throw new java.lang.UnsupportedOperationException("Method getValueAsString() not yet implemented.");
    }

    /**
   * 
   * <p>This method is required by the DataModel interface but in this case it is 
   * not implemented as it is inappropriate in the context of the class</p>
   * @return nothing, instead the method throws an UnsupportedOperationException
   */
    public String getId() {
        throw new java.lang.UnsupportedOperationException("Method getName() not yet implemented.");
    }

    /**
   * 
   * <p>This method is required by the DataModel interface but in this case it is 
   * not implemented as it is inappropriate in the context of the class</p>
   * @param i ignored
   * @return nothing, instead the method throws an UnsupportedOperationException
   */
    public String getAttribName(int i) {
        throw new java.lang.UnsupportedOperationException("Method getAttribName() not yet implemented.");
    }

    /**
   * 
   * <p>This method is required by the DataModel interface but in this case it is 
   * not implemented as it is inappropriate in the context of the class</p>
   * @param attribName ignored
   * @return nothing, instead the method throws an UnsupportedOperationException
   */
    public int getAttribute(String attribName) {
        throw new java.lang.UnsupportedOperationException("Method getAttribute() not yet implemented.");
    }

    /**
   * Set the value of the DataModel located at the elementName
   * @param attribName The path to the DataModel in the format 'base/foo
   * @param newObject The new value of the DataModel
   */
    public void set(String attribName, Object newObject) {
    }

    /**
   * Sets the model value
   * @param s the new value
   */
    public void set(Object s) {
        fireModelUpdated();
    }

    /**
   * Not yet implemented - does nothing
   * @param model - ignored
   */
    public void remove(DataModel model) {
    }

    /**
   * Not yet implemented - does nothing
   * @param i - ignored
   * @return null
   */
    public DataModel get(int i) {
        return null;
    }

    /**
   * This method does not nothing it is provided merely as an implementation of the 
   * DataModel interface. A child or attribute can be logically appended to a table node.
   * @param id the node id
   * @return null as nothing is appended
   */
    public Object append(String id) {
        return null;
    }

    /**
   * Gets the model element tag name, e.g. 'Component' from the XML fragment
   * <Component ....
   * @return the model element name
   */
    public String getTagName() {
        return "";
    }

    /**
   * Get the field value
   * @return the field value object
   */
    public Object get() {
        return sourceData.getFieldValue(rowIdx, fieldIdx);
    }

    /**
   * 
   * <p>This method is required by the DataModel interface but in this case it is 
   * not implemented as it is inappropriate in the context of the class</p>
   * @param elementName ignored
   * @return nothing, instead the method throws an UnsupportedOperationException
   */
    public int getValueAsInt(String elementName) {
        throw new java.lang.UnsupportedOperationException("Method getValueAsInt() not yet implemented.");
    }

    /**
   * Not yet implemented - does nothing
   * @param i ignored
   * @param value ignored
   */
    public void setAttribValue(int i, Object value) {
    }

    /**
   * Not yet implemented - does nothing
   * @param i  - ignored
   * @param attribName - ignored
   * @param value  - ignored
   */
    public void setAttribValue(int i, String attribName, Object value) {
    }

    /**
   * Returns a hashcode for this node
   * @return the hash code
   */
    public int hashCode() {
        return rowIdx * 10000 + fieldIdx;
    }

    /**
   * Gets the field value as an int
   * @param i The index of the attributeValues array whose value we want
   * @return The int value of the attributeValues array at position i
   */
    public int getAttribValueAsInt(int i) {
        return Integer.parseInt(sourceData.getFieldValue(0, fieldIdx));
    }

    /**
   * Gets the field value as a double
   * @param i The index of the attributeValues array whose value we want
   * @return The int value of the attributeValues array at position i
   *
   * @deprecated use getAttribValueAsDouble( i, decimalSeparator, groupingSeparator ) 
   * instead, if the locale is different from the locale used to write the values 
   * to the model, then the parsed value may be incorrect.
   */
    public double getAttribValueAsDouble(int i) {
        return Double.parseDouble(sourceData.getFieldValue(0, fieldIdx));
    }

    /**
   * Gets the value as a string
   * @return the value as a string
   */
    public String toString() {
        return (String) get();
    }
}
