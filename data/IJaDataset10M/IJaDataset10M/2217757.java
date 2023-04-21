package edu.uiuc.jdbv.schema;

/**
 * Representation of a column in a database.  The contents of this 
 * class cannot be altered once created.
 *
 * @author Ross Paul
 * 
 */
public class Column {

    /** The name of the column from the schema */
    private final String name;

    /** The datatype of the column */
    private final String type;

    /** Is this column part of a primary key */
    private boolean key;

    /** Constructor takes the name and type of the column.  In additon
     *  the isKey value must be specified */
    public Column(String name, String type, boolean key) {
        this.name = name;
        this.type = type;
        this.key = key;
    }

    /** Constructor takes the name, type of the column.
     *  Boolean object key is also specified to indicate
     *  the value of key - Required by XMLEncoder
     */
    public Column(String name, String type, Boolean key) {
        this.name = name;
        this.type = type;
        this.key = key.booleanValue();
    }

    /** Setter function set key boolean  */
    public void setKey(boolean key) {
        this.key = key;
    }

    /** Returns the name of the column */
    public String getName() {
        return name;
    }

    /** Returns the columns type */
    public String getType() {
        return type;
    }

    /** True if column is part of the primary key  */
    public boolean isKey() {
        return key;
    }

    /** Checks if this column is equivalant to another */
    public boolean equals(Object o) {
        if (o instanceof Column) {
            Column other = (Column) o;
            return ((key == other.isKey()) && (type.equals(other.getType())) && (name.equals(other.getName())));
        } else return false;
    }

    /** overrides the toString method to allow logical viewing */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("\t\tName: " + name);
        buf.append("\tType: " + type);
        if (key) {
            buf.append("\t<< primary key");
        }
        buf.append("\n");
        return buf.toString();
    }
}
