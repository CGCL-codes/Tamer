package org.ujac.util.table;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.ujac.util.CollectionUtils;

/**
 * Name: BaseRow<br>
 * Description: A base class for row implementations.
 * 
 * @author lauerc
 */
public abstract class BaseRow implements Row, Serializable {

    /** The serial version UID. */
    static final long serialVersionUID = 3096795524377585326L;

    /** The table, which contains this row. */
    protected Table table = null;

    /** The list which holds the row data. */
    protected List rowData = null;

    /** The set of hidden fields. */
    private Set hiddenFields = null;

    /** The format map. */
    private Map fieldFormats = null;

    /** The alignment map. */
    private Map fieldAlignments = null;

    /** Tells whether or not to start a new page before printing this row. */
    private boolean startNewPage = false;

    /** Tells whether or not this row starts a new row block. */
    private boolean startsBlock = false;

    /** Tells whether or not this row ends the current row block. */
    private boolean endsBlock = false;

    /**
   * Constructs a BaseRow instance with specific attributes.
   * @param table The row's table.
   */
    public BaseRow(Table table) {
        this.table = table;
        this.rowData = new ArrayList();
        expandRow();
    }

    /**
   * Constructs a BaseRow instance with specific attributes.
   * @param table The row's table.
   * @param srcRow the row to copy.
   */
    public BaseRow(Table table, Row srcRow) {
        this.table = table;
        this.rowData = new ArrayList();
        int columnCount = table.getColumnCount();
        List srcData = srcRow.asList();
        int srcSize = CollectionUtils.getSize(srcData);
        if (srcSize != columnCount) {
            throw new RuntimeException("copied table row is not compatible!");
        }
        rowData.addAll(srcData);
    }

    /**
   * Gets the table the row is member of.
   * @return The table that contains this row.
   */
    public Table getTable() {
        return table;
    }

    /**
   * Gets the list of elements from this row.
   * @return The row data as a List.
   */
    public List asList() {
        return rowData;
    }

    /**
   * Formats the given column's value for output.
   * @param columnName The name of the desired column.
   * @return The formatted value.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   * @exception TypeMismatchException In case the data conversion failed.
   */
    public String formatValue(String columnName) throws ColumnNotDefinedException, TypeMismatchException {
        Column col = table.getColumn(columnName);
        return formatValue(col.getIndex());
    }

    /**
   * Formats the given column's value for output.
   * @param columnIdx The index of the desired column.
   * @return The detected value.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   * @exception TypeMismatchException In case the data conversion failed.
   */
    public String formatValue(int columnIdx) throws ColumnNotDefinedException, TypeMismatchException {
        if (!isVisible(columnIdx)) {
            return "";
        }
        String value = getString(columnIdx);
        if (value == null) {
            return "-";
        }
        return value;
    }

    /**
   * Checks whether the given field is visible or not.
   * @param columnName The name of the desired column.
   * @return The visibility flag.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public boolean isVisible(String columnName) throws ColumnNotDefinedException {
        if ((hiddenFields != null) && hiddenFields.contains(columnName)) {
            return false;
        }
        Column column = table.getColumn(columnName);
        LayoutHints lh = column.getLayoutHints();
        return (lh != null) && lh.isVisible();
    }

    /**
   * Checks whether the given field is visible or not.
   * @param columnIdx The index of the desired column.
   * @return The visibility flag.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public boolean isVisible(int columnIdx) throws ColumnNotDefinedException {
        Column column = table.getColumn(columnIdx);
        return isVisible(column.getName());
    }

    /**
   * Sets the row specific visibility flag for the given field.
   * @param columnName The name of the desired column.
   * @param visible The visibility flag.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public void setVisible(String columnName, boolean visible) throws ColumnNotDefinedException {
        if (hiddenFields == null) {
            if (visible == true) {
                return;
            }
            hiddenFields = new HashSet();
        }
        if (visible) {
            hiddenFields.remove(columnName);
        } else {
            hiddenFields.add(columnName);
        }
    }

    /**
   * Sets the row specific visibility flag for the given field.
   * @param columnIdx The index of the desired column.
   * @param visible The visibility flag.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public void setVisible(int columnIdx, boolean visible) throws ColumnNotDefinedException {
        Column column = table.getColumn(columnIdx);
        setVisible(column.getName(), visible);
    }

    /**
   * Gets the format of the given field.
   * @param columnName The name of the desired column.
   * @return The field's format.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public Format getFormat(String columnName) throws ColumnNotDefinedException {
        if (fieldFormats == null) {
            return table.getColumn(columnName).getFormat();
        }
        Format format = (Format) fieldFormats.get(columnName);
        if (format != null) {
            return format;
        }
        return table.getColumn(columnName).getFormat();
    }

    /**
   * Gets the format of the given field.
   * @param columnIdx The index of the desired column.
   * @return The field's format.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public Format getFormat(int columnIdx) throws ColumnNotDefinedException {
        Column column = table.getColumn(columnIdx);
        if (fieldFormats == null) {
            return column.getFormat();
        }
        Format format = (Format) fieldFormats.get(column.getName());
        if (format != null) {
            return format;
        }
        return column.getFormat();
    }

    /**
   * Sets the row specific format for the given field.
   * @param columnName The name of the desired column.
   * @param format The format to set.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public void setFormat(String columnName, Format format) throws ColumnNotDefinedException {
        if (fieldFormats == null) {
            fieldFormats = new HashMap();
        }
        fieldFormats.put(columnName, format);
    }

    /**
   * Sets the row specific format for the given field.
   * @param columnIdx The index of the desired column.
   * @param format The format to set.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public void setFormat(int columnIdx, Format format) throws ColumnNotDefinedException {
        Column column = table.getColumn(columnIdx);
        setFormat(column.getName(), format);
    }

    /**
   * Gets the alignment of the given field.
   * @param columnName The name of the desired column.
   * @return The field's alignment.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public int getAlign(String columnName) throws ColumnNotDefinedException {
        if (fieldFormats == null) {
            LayoutHints lh = table.getColumn(columnName).getLayoutHints();
            return getFieldAlignment(lh);
        }
        Integer alignment = (Integer) fieldAlignments.get(columnName);
        if (alignment != null) {
            return alignment.intValue();
        }
        LayoutHints lh = table.getColumn(columnName).getLayoutHints();
        return getFieldAlignment(lh);
    }

    /**
   * Gets the alignment of the given field.
   * @param columnIdx The index of the desired column.
   * @return The field's alignment.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public int getAlign(int columnIdx) throws ColumnNotDefinedException {
        Column column = table.getColumn(columnIdx);
        if (fieldFormats == null) {
            LayoutHints lh = column.getLayoutHints();
            return getFieldAlignment(lh);
        }
        if (fieldAlignments != null) {
            Integer alignment = (Integer) fieldAlignments.get(column.getName());
            if (alignment != null) {
                return alignment.intValue();
            }
        }
        LayoutHints lh = column.getLayoutHints();
        return getFieldAlignment(lh);
    }

    /**
   * Gets the field alignment. 
   * @param lh The layout hints.
   * @return The field alignment from the given layout hints.
   */
    protected int getFieldAlignment(LayoutHints lh) {
        if (lh != null) {
            return lh.getAlign();
        }
        return LayoutHints.LEFT;
    }

    /**
   * Sets the row specific alignment for the given field.
   * @param columnName The name of the desired column.
   * @param alignment The format to set.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public void setAlign(String columnName, int alignment) throws ColumnNotDefinedException {
        if (fieldAlignments == null) {
            fieldAlignments = new HashMap();
        }
        fieldAlignments.put(columnName, new Integer(alignment));
    }

    /**
   * Sets the row specific alignment for the given field.
   * @param columnIdx The index of the desired column.
   * @param alignment The format to set.
   * @exception ColumnNotDefinedException In case the requested column does not exist.
   */
    public void setAlign(int columnIdx, int alignment) throws ColumnNotDefinedException {
        Column column = table.getColumn(columnIdx);
        setAlign(column.getName(), alignment);
    }

    /**
   * Tells whether or not to start a new page before printing this row.
   * @return true, if a page break is required here, else false.
   */
    public boolean isStartNewPage() {
        return startNewPage;
    }

    /**
   * Sets the startNewPage flag.
   * @param startNewPage The new value for the startNewPage attribute.
   */
    public void setStartNewPage(boolean startNewPage) {
        this.startNewPage = startNewPage;
    }

    /**
   * Tells whether or not this row starts a new row block.
   * @return true, if this row starts a new row block, else false.
   */
    public boolean isStartsBlock() {
        return startsBlock;
    }

    /**
   * Sets the startsBlock flag.
   * @param startsBlock The new value for the startsBlock attribute.
   */
    public void setStartsBlock(boolean startsBlock) {
        this.startsBlock = startsBlock;
    }

    /**
   * Tells whether or not this row ends the current row block.
   * @return true, if this row ends the current row block, else false.
   */
    public boolean isEndsBlock() {
        return endsBlock;
    }

    /**
   * Sets the endsBlock flag.
   * @param endsBlock The new value for the endsBlock attribute.
   */
    public void setEndsBlock(boolean endsBlock) {
        this.endsBlock = endsBlock;
    }

    /**
   * Expands the length of the row to the number of columns of the table.
   */
    protected void expandRow() {
        int columnCount = table.getColumnCount();
        int elementCount = rowData.size();
        for (int i = elementCount; i < columnCount; i++) {
            rowData.add(null);
        }
    }

    /**
   * @see java.lang.Object#toString()
   */
    public String toString() {
        StringBuffer buf = new StringBuffer(getType()).append(": [");
        try {
            Table tab = getTable();
            int numColumns = tab.getColumnCount();
            for (int i = 0; i < numColumns; i++) {
                if (i > 0) {
                    buf.append(", ");
                }
                buf.append(getString(i));
            }
        } catch (TableException ex) {
            ex.printStackTrace();
        }
        buf.append("]");
        if (startNewPage) {
            buf.append(", startNewPage!");
        }
        if (startsBlock) {
            buf.append(", startsBlock!");
        }
        if (endsBlock) {
            buf.append(", endsBlock!");
        }
        return buf.toString();
    }

    /**
   * Writes the object's data to the given stream.
   * @param s The stream to write to
   * @exception IOException In case the data output failed.
   */
    protected void writeData(ObjectOutputStream s) throws IOException {
        s.writeObject(table);
        s.writeObject(rowData);
        s.writeObject(hiddenFields);
        s.writeObject(fieldFormats);
        s.writeObject(fieldAlignments);
        s.writeBoolean(startNewPage);
        s.writeBoolean(startsBlock);
        s.writeBoolean(endsBlock);
    }

    /**
   * Reads the object's data from the given stream.
   * @param s The stream to read from.
   * @exception IOException In case the data reading failed.
   * @exception ClassNotFoundException In case the class to deserialize could not be found
   */
    protected void readData(ObjectInputStream s) throws IOException, ClassNotFoundException {
        table = (Table) s.readObject();
        rowData = (List) s.readObject();
        hiddenFields = (Set) s.readObject();
        fieldFormats = (Map) s.readObject();
        fieldAlignments = (Map) s.readObject();
        startNewPage = s.readBoolean();
        startsBlock = s.readBoolean();
        endsBlock = s.readBoolean();
    }

    public void clear() {
        int size = rowData.size();
        for (int i = 0; i < size; i++) {
            rowData.set(i, null);
        }
    }

    public boolean containsKey(Object key) {
        if (key == null) {
            return false;
        }
        String colName = key.toString();
        try {
            table.getColumn(colName);
            return true;
        } catch (TableException ex) {
            return false;
        }
    }

    public boolean containsValue(Object value) {
        return rowData.contains(value);
    }

    public Set entrySet() {
        Set entrySet = new HashSet();
        int numCols = table.getColumnCount();
        for (int i = 0; i < numCols; i++) {
            try {
                final Column column = table.getColumn(i);
                entrySet.add(new Map.Entry() {

                    private Column col = null;

                    {
                        this.col = column;
                    }

                    public Object setValue(Object value) {
                        Object curVal = null;
                        try {
                            int idx = col.getIndex();
                            curVal = getObject(idx);
                            setObject(idx, value);
                        } catch (TableException ex) {
                            throw new RuntimeException(ex);
                        }
                        return curVal;
                    }

                    public Object getValue() {
                        Object curVal = null;
                        try {
                            int idx = col.getIndex();
                            curVal = getObject(idx);
                        } catch (TableException ex) {
                            throw new RuntimeException(ex);
                        }
                        return curVal;
                    }

                    public Object getKey() {
                        return col.getName();
                    }
                });
            } catch (TableException ex) {
            }
        }
        return entrySet;
    }

    public Object get(Object key) {
        if (key == null) {
            return null;
        }
        String colName = key.toString();
        try {
            return getObject(colName);
        } catch (TableException ex) {
            return null;
        }
    }

    public boolean isEmpty() {
        return rowData.isEmpty();
    }

    public Set keySet() {
        int numCols = table.getColumnCount();
        Set keySet = new HashSet();
        for (int i = 0; i < numCols; i++) {
            try {
                keySet.add(table.getColumn(i).getName());
            } catch (TableException ex) {
            }
        }
        return keySet;
    }

    public Object put(Object key, Object value) {
        if (key == null) {
            return null;
        }
        String colName = key.toString();
        Object curVal;
        try {
            curVal = getObject(colName);
            setObject(colName, value);
        } catch (TableException ex) {
            throw new RuntimeException(ex);
        }
        return curVal;
    }

    public void putAll(Map valueMap) {
        Set entrySet = valueMap.entrySet();
        Iterator iterEntries = entrySet.iterator();
        while (iterEntries.hasNext()) {
            Map.Entry entry = (Map.Entry) iterEntries.next();
            Object keyObj = entry.getKey();
            if (keyObj == null) {
                continue;
            }
            String key = keyObj.toString();
            Object value = entry.getValue();
            try {
                setObject(key, value);
            } catch (TableException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public Object remove(Object key) {
        if (key == null) {
            return null;
        }
        String colName = key.toString();
        Object curVal;
        try {
            curVal = getObject(colName);
            setObject(colName, null);
        } catch (TableException ex) {
            throw new RuntimeException(ex);
        }
        return curVal;
    }

    public int size() {
        return rowData.size();
    }

    public Collection values() {
        return rowData;
    }
}
