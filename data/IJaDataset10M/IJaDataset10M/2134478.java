package org.melati.poem;

import org.melati.poem.generated.TableInfoBase;

/**
 * All the data defining a {@link Table}; actually a {@link Persistent} 
 * from the {@link TableInfoTable}.
 * 
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>Persistent</code> <code>TableInfo</code> object.
 * 
 * <p> 
 * Description: 
 *   Configuration information about a table in the database. 
 * </p>
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>TableInfo</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> The Table Row Object ID </td></tr> 
 * <tr><td> name </td><td> String </td><td> A code-name for the table 
 * </td></tr> 
 * <tr><td> displayname </td><td> String </td><td> A user-friendly name for 
 * the table </td></tr> 
 * <tr><td> description </td><td> String </td><td> A brief description of the 
 * table's function </td></tr> 
 * <tr><td> displayorder </td><td> Integer </td><td> A rank determining where 
 * the table appears in the list of all tables </td></tr> 
 * <tr><td> defaultcanread </td><td> Capability </td><td> The capability 
 * required, by default, for reading the table's records </td></tr> 
 * <tr><td> defaultcanwrite </td><td> Capability </td><td> The capability 
 * required, by default, for updating the table's records </td></tr> 
 * <tr><td> defaultcandelete </td><td> Capability </td><td> The capability 
 * required, by default, for deleting the table's records </td></tr> 
 * <tr><td> cancreate </td><td> Capability </td><td> The capability required, 
 * by default, for creating records in the table </td></tr> 
 * <tr><td> cachelimit </td><td> Integer </td><td> The maximum number of 
 * records from the table to keep in the cache </td></tr> 
 * <tr><td> seqcached </td><td> Boolean </td><td> Whether the display 
 * sequence for the table's records is cached </td></tr> 
 * <tr><td> category </td><td> TableCategory </td><td> Which category the 
 * table falls into </td></tr> 
 * </table> 
 * 
 * @generator org.melati.poem.prepro.TableDef#generateMainJava 
 */
public class TableInfo extends TableInfoBase {

    /**
  * Constructor 
  * for a <code>Persistent</code> <code>TableInfo</code> object.
  * <p>
  * Description: 
  *   Configuration information about a table in the database. 
  * </p>
  * 
  * @generator org.melati.poem.prepro.TableDef#generateMainJava 
  */
    public TableInfo() {
    }

    private Table _actualTable = null;

    public Table actualTable() {
        if (_actualTable == null && troid() != null) _actualTable = getDatabase().tableWithTableInfoID(troid().intValue());
        return _actualTable;
    }

    /**
  * Allow this object to be read by anyone.
  * 
  * @param token any {@link AccessToken}
  */
    public void assertCanRead(AccessToken token) {
    }

    public TableInfo(String name, String displayName, int displayOrder, String description, Integer cacheLimit, boolean rememberAllTroids, TableCategory category) {
        setName_unsafe(name);
        setDisplayname_unsafe(displayName);
        setDisplayorder_unsafe(new Integer(displayOrder));
        setDescription_unsafe(description);
        setCachelimit_unsafe(cacheLimit);
        setSeqcached_unsafe(rememberAllTroids ? Boolean.TRUE : Boolean.FALSE);
        setCategory_unsafe(category.troid());
    }

    public void setName(String name) {
        String current = getName();
        if (current != null && !current.equals(name)) throw new TableRenamePoemException(name);
        super.setName(name);
    }

    public String displayString() throws AccessPoemException {
        return getDisplayname();
    }

    public void setSeqcached(Boolean b) throws AccessPoemException {
        super.setSeqcached(b);
        Table t = actualTable();
        if (t != null) t.rememberAllTroids(b.booleanValue());
    }

    public void setCachelimit(Integer limit) throws AccessPoemException {
        super.setCachelimit(limit);
        Table t = actualTable();
        if (t != null) t.setCacheLimit(limit);
    }
}
