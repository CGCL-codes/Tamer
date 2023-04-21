package edu.uiuc.ncsa.security.storage.sql.internals;

/**
 * Describes a column in an SQL database in a platform and vendor neutral way.
 * <p>Created by Jeff Gaynor<br>
 * on 8/30/11 at  2:34 PM
 */
public class ColumnDescriptorEntry implements Comparable {

    public ColumnDescriptorEntry(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public ColumnDescriptorEntry(String name, int type, boolean nullable, boolean primaryKey) {
        this.name = name;
        this.nullable = nullable;
        this.primaryKey = primaryKey;
        this.type = type;
    }

    /**
     * If this column may be nulled.
     *
     * @return
     */
    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    boolean nullable = false;

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    boolean primaryKey = false;

    String name;

    int type;

    /**
     * The name for this column. This will be used in the resulting table.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The type as given in {@link java.sql.Types}. These (within rather narrow limits)
     * are converted by the database abstraction layer to the correct underlying
     * data type. E.g. a BLOB would become a <code>LONG VARCHAR FOR BIT DATA</code>
     * in a DB2 implementation.
     *
     * @return
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int compareTo(Object o) {
        if (!(o instanceof ColumnDescriptorEntry)) {
            throw new ClassCastException("Error: The given object must be of type ColumnDescriptor to be compared here. It is of type " + o.getClass().getName());
        }
        ColumnDescriptorEntry cd = (ColumnDescriptorEntry) o;
        if (getName() == null) {
            if (cd.getName() == null) return 0;
            return cd.getName().compareTo(null);
        }
        return getName().compareTo(cd.getName());
    }
}
