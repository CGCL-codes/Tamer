package com.softaspects.jsf.component.table;

import com.softaspects.jsf.component.base.WGFComponentBase;

/**
 * RowIdDataModel Component
 */
public class BaseRowIdDataModel extends WGFComponentBase {

    public static final String COMPONENT_TYPE = "com.softaspects.jsf.component.table.RowIdDataModel";

    public static final String RENDERER_TYPE = "com.softaspects.jsf.renderer.table.RowIdDataModelRenderer";

    public String getComponentTypeName() {
        return COMPONENT_TYPE;
    }

    public String getRendererType() {
        return RENDERER_TYPE;
    }

    public String getFamily() {
        return COMPONENT_TYPE;
    }

    public Class[] getPossibleChildComponents() {
        return new Class[] {};
    }

    /**
     * Setter for property 'cellDragAllowed'
     *
     * @param cellDragAllowed 'cellDragAllowed' property value
     */
    public void setCellDragAllowed(java.lang.Integer cellDragAllowed) {
        setProperty("cellDragAllowed", cellDragAllowed);
    }

    /**
     * Getter for property 'cellDragAllowed'
     *
     * @return cellDragAllowed 'cellDragAllowed' property value
     */
    public java.lang.Integer getCellDragAllowed() {
        return (java.lang.Integer) getProperty("cellDragAllowed");
    }

    /**
     * Setter for property 'cellDropAllowed'
     *
     * @param cellDropAllowed 'cellDropAllowed' property value
     */
    public void setCellDropAllowed(java.lang.Integer cellDropAllowed) {
        setProperty("cellDropAllowed", cellDropAllowed);
    }

    /**
     * Getter for property 'cellDropAllowed'
     *
     * @return cellDropAllowed 'cellDropAllowed' property value
     */
    public java.lang.Integer getCellDropAllowed() {
        return (java.lang.Integer) getProperty("cellDropAllowed");
    }

    /**
     * Setter for property 'cellEditable'
     *
     * @param cellEditable 'cellEditable' property value
     */
    public void setCellEditable(java.lang.Integer cellEditable) {
        setProperty("cellEditable", cellEditable);
    }

    /**
     * Getter for property 'cellEditable'
     *
     * @return cellEditable 'cellEditable' property value
     */
    public java.lang.Integer getCellEditable() {
        return (java.lang.Integer) getProperty("cellEditable");
    }

    /**
     * Setter for property 'columnCount'
     *
     * @param columnCount 'columnCount' property value
     */
    public void setColumnCount(java.lang.Integer columnCount) {
        setProperty("columnCount", columnCount);
    }

    /**
     * Getter for property 'columnCount'
     *
     * @return columnCount 'columnCount' property value
     */
    public java.lang.Integer getColumnCount() {
        return (java.lang.Integer) getProperty("columnCount");
    }

    /**
     * Setter for property 'columnID'
     *
     * @param columnID 'columnID' property value
     */
    public void setColumnID(java.lang.Integer columnID) {
        setProperty("columnID", columnID);
    }

    /**
     * Getter for property 'columnID'
     *
     * @return columnID 'columnID' property value
     */
    public java.lang.Integer getColumnID() {
        return (java.lang.Integer) getProperty("columnID");
    }

    /**
     * Setter for property 'columnsID'
     *
     * @param columnsID 'columnsID' property value
     */
    public void setColumnsID(java.util.List columnsID) {
        setProperty("columnsID", columnsID);
    }

    /**
     * Getter for property 'columnsID'
     *
     * @return columnsID 'columnsID' property value
     */
    public java.util.List getColumnsID() {
        return (java.util.List) getProperty("columnsID");
    }

    /**
     * Setter for property 'cursorPageSize'
     *
     * @param cursorPageSize 'cursorPageSize' property value
     */
    public void setCursorPageSize(java.lang.Integer cursorPageSize) {
        setProperty("cursorPageSize", cursorPageSize);
    }

    /**
     * Getter for property 'cursorPageSize'
     *
     * @return cursorPageSize 'cursorPageSize' property value
     */
    public java.lang.Integer getCursorPageSize() {
        return (java.lang.Integer) getProperty("cursorPageSize");
    }

    /**
     * Setter for property 'data'
     *
     * @param data 'data' property value
     */
    public void setData(java.lang.Object[] data) {
        setProperty("data", data);
    }

    /**
     * Getter for property 'data'
     *
     * @return data 'data' property value
     */
    public java.lang.Object[] getData() {
        return (java.lang.Object[]) getProperty("data");
    }

    /**
     * Setter for property 'iD'
     *
     * @param iD 'iD' property value
     */
    public void setID(java.lang.Object iD) {
        setProperty("iD", iD);
    }

    /**
     * Getter for property 'iD'
     *
     * @return iD 'iD' property value
     */
    public java.lang.Object getID() {
        return (java.lang.Object) getProperty("iD");
    }

    /**
     * Setter for property 'immediate'
     *
     * @param immediate 'immediate' property value
     */
    public void setImmediate(boolean immediate) {
        setProperty("immediate", immediate);
    }

    /**
     * Getter for property 'immediate'
     *
     * @return immediate 'immediate' property value
     */
    public boolean getImmediate() {
        return (boolean) getProperty("immediate", false);
    }

    /**
     * Getter for property 'immediate'
     *
     * @return immediate 'immediate' property value
     */
    public boolean isImmediate() {
        return (boolean) getProperty("immediate", false);
    }

    /**
     * Setter for property 'rowCount'
     *
     * @param rowCount 'rowCount' property value
     */
    public void setRowCount(java.lang.Integer rowCount) {
        setProperty("rowCount", rowCount);
    }

    /**
     * Getter for property 'rowCount'
     *
     * @return rowCount 'rowCount' property value
     */
    public java.lang.Integer getRowCount() {
        return (java.lang.Integer) getProperty("rowCount");
    }

    /**
     * Setter for property 'size'
     *
     * @param size 'size' property value
     */
    public void setSize(java.lang.Integer size) {
        setProperty("size", size);
    }

    /**
     * Getter for property 'size'
     *
     * @return size 'size' property value
     */
    public java.lang.Integer getSize() {
        return (java.lang.Integer) getProperty("size");
    }

    /**
     * Setter for property 'valueAt'
     *
     * @param valueAt 'valueAt' property value
     */
    public void setValueAt(java.lang.Object valueAt) {
        setProperty("valueAt", valueAt);
    }

    /**
     * Getter for property 'valueAt'
     *
     * @return valueAt 'valueAt' property value
     */
    public java.lang.Object getValueAt() {
        return (java.lang.Object) getProperty("valueAt");
    }
}
