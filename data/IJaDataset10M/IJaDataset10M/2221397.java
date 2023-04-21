package net.sf.imageCave.database;

import java.lang.String;
import java.util.*;

/**
 * An object that represents a table in the <code>Database</code>. Each instance can
 * have several <code>DataRows</code>.
 *
 * @see Database
 * @see DataRow
 * @see DataTableBuilder
 *
 * @author Stuart T. Tett, disc0stu@sf.net
 * @version %I%, %G%
 * @since 13 March, 2004
 */
public class DataTable {

    /**
     * Holds all the DataRow instances that belong to this <code>DataTable</code>.
     */
    public Collection dataRows = new LinkedHashSet();

    /** Name of the table */
    private String name;

    /** Array of the names of the fields */
    public String[] fieldNames;

    /**
     * Constructor
     */
    public DataTable(String name) {
        this.name = name;
    }

    public void setFieldNames(String[] f) {
        this.fieldNames = new String[f.length];
        System.arraycopy(f, 0, this.fieldNames, 0, this.fieldNames.length);
    }

    /**
     * <p>To select rows, either just to grab information, or to
     * select rows to update, or delete, a filter is used for which rows to select.</p>
     *
     * <p>Each string in the array should be of the format: <b>"field=value"</b>
     * the "=" can be replaced with any of these "&lt;","&gt;","!=","&lt;=","&gt;="</p>
     * 
     * @return a tempory table that holds only the selected rows which meet the filter.. 
     * @param filter a string of boolean logical statements of fields and their values.
     */
    public DataTable select(String filter) {
        DataTable selectionTable = new DataTable(this.getName() + randomString());
        selectionTable.setFieldNames(this.fieldNames);
        DataQueryInterpreter dqi = new DataQueryInterpreter();
        dqi.read(filter);
        for (Iterator i = this.dataRows.iterator(); i.hasNext(); ) {
            DataRow r = (DataRow) i.next();
            if (dqi.isMatch(r, this)) {
                selectionTable.insert(r);
            }
        }
        return selectionTable;
    }

    public int fieldIndex(String fieldName) {
        for (int i = 0; i <= fieldNames.length; i++) {
            if ((fieldNames[i]).equals(fieldName)) return i;
        }
        return -1;
    }

    /**
     * <p>Update rows in this <code>DataTable</code> using <code>rowsToUpdate</code>
     * to filter which rows should be affected. String arrays of field names and their
     * new values should line up, repectively.</p>
     * 
     * @return true if successful, false otherwise
     * @param rowsToUpdate
     * @param fieldsToUpdate
     * @param newValues
     */
    public boolean update(DataTable rowsToUpdate, String[] fieldsToUpdate, String[] newValues) {
        if (rowsToUpdate.width() != this.width() || fieldsToUpdate.length >= this.width()) return false;
        String[] updateFields = rowsToUpdate.getFields();
        for (int i = 0; i < this.width(); i++) {
            if (!(updateFields[i].equals(this.fieldNames[i]))) return false;
        }
        boolean success = true;
        for (Iterator i = this.dataRows.iterator(); i.hasNext(); ) {
            DataRow r = (DataRow) i.next();
            if (rowsToUpdate.contains(r)) {
                for (int j = 0; j < fieldsToUpdate.length; j++) {
                    success = success & r.update(this.fieldIndex(fieldsToUpdate[j]), newValues[j]);
                }
            }
        }
        return success;
    }

    /**
     * <p>Delete rows in this <code>DataTable</code> using <code>rowsToDelete</code>
     * to filter which rows should be affected.</p>
     * 
     * @return true if successful, false otherwis
     * @param rowsToDelete
     */
    public boolean delete(DataTable rowsToDelete) {
        int k = 0;
        for (Iterator j = rowsToDelete.dataRows.iterator(); j.hasNext(); ) {
            DataRow dtj = (DataRow) j.next();
            this.delete(dtj);
            k++;
        }
        return true;
    }

    /**
     * <p>Delete row in this <code>DataTable</code> using <code>rowToDelete</code>
     * to filter which row should be affected.</p>
     * 
     * @return true if successful, false otherwis
     * @param rowToDelete
     */
    public boolean delete(DataRow rowToDelete) {
        for (Iterator i = this.dataRows.iterator(); i.hasNext(); ) {
            DataRow dr = (DataRow) i.next();
            if (dr.equals(rowToDelete)) {
                i.remove();
            }
        }
        int j = 0;
        return true;
    }

    /**
     * <p>Insert a new <code>DataRow</code> into this <code>DataTable</code>.</p>
     * 
     * @return true if successful, false otherwis
     * @param row
     */
    public boolean insert(DataRow row) {
        if (rowSizeMatches(row)) {
            dataRows.add(row);
            return true;
        }
        return false;
    }

    /**
     * <p>Add a new field to the list of fields in this <code>DataTable</code>.</p>
     * @param f the name of the new field to add.
     */
    public void addField(String f) {
        if (this.fieldNames == null) {
            this.fieldNames = new String[1];
            this.fieldNames[0] = f;
        } else if (!stringInArray(f, this.fieldNames)) {
            String[] tmp = new String[this.fieldNames.length + 1];
            System.arraycopy(fieldNames, 0, tmp, 0, fieldNames.length);
            tmp[tmp.length - 1] = f;
            this.fieldNames = tmp;
        } else {
        }
    }

    public boolean contains(DataRow dr) {
        for (Iterator i = this.dataRows.iterator(); i.hasNext(); ) {
            DataRow currDr = (DataRow) i.next();
            if (currDr.equals(dr)) return true;
        }
        return false;
    }

    public int size() {
        return this.dataRows.size();
    }

    public int width() {
        return this.fieldNames.length;
    }

    public boolean isEmpty() {
        return this.dataRows.isEmpty();
    }

    public Iterator iterator() {
        return dataRows.iterator();
    }

    public String getName() {
        return this.name;
    }

    public String[] getFields() {
        return this.fieldNames;
    }

    private boolean stringInArray(String find, String[] array) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equalsIgnoreCase(find)) return true;
        }
        return false;
    }

    private boolean rowSizeMatches(DataRow r) {
        if (this.width() == r.width()) return true;
        return false;
    }

    private static String randomString() {
        int n = rand(5, 25);
        byte b[] = new byte[n];
        for (int i = 0; i < n; i++) b[i] = (byte) rand('a', 'z');
        return new String(b);
    }

    private static int rand(int lo, int hi) {
        Random rn = new Random();
        int n = hi - lo + 1;
        int i = rn.nextInt() % n;
        if (i < 0) i = -i;
        return lo + i;
    }
}
