package com.completex.objective.components.persistency;

/**
 * Object that can to be used in queries as an "ad-hoc" part of CompoundPersistentObject as return type.
 * It is convenient when you have some generated persistent objects but the query brings 
 * some extra fields. Extras can be retrieved into AdHocPersistentObject. Retrieved values 
 * can be accessed through its <code>Record</code>. In an example below retrieved is collection of 
 * compound objects consisting of Person object and extra field "rownum". When SQL gets built
 * neither AdHocPersistentObject columns are not prepended with table name in SELECT SQL clause 
 * nor table name gets into FROM SQL clause.
 * 
 * <PRE>
 * <code>
 *   Person person = new Person(new Long(1));
 *   AdHocPersistentObject adHoc = new AdHocPersistentObject(1).setColumnName(0, "rownum");
 *   CompoundPersistentObject cf = new CompoundPersistentObject(
 *                   new PersistentObject[]{person, adHoc});
 *   Query query = queryFactory.newQueryByCompoundExample(cf);
 *   Collection collection = persistency.select(query);
 *   for (Iterator it = collection.iterator(); it.hasNext();) {
 *       CompoundPersistentObject compound = (CompoundPersistentObject) it.next();
 *       person = compound.compoundEntry(0);
 *       Number rownum = compound.compoundEntry(1).record(0);
 *       ....
 *   }
 * </code>
 * </PRE>
 * 
 * 
 * @author Gennady Krizhevsky
 */
public class AdHocPersistentObject extends PersistentObject {

    public AdHocPersistentObject() {
    }

    public AdHocPersistentObject(MetaTable table) {
        String[] columns = new String[table.size()];
        for (int i = 0; i < table.size(); i++) {
            columns[i] = table.getColumn(i).getColumnName();
        }
        setupTable(table.getTableName(), columns);
    }

    public AdHocPersistentObject(String tableName, String[] columns) {
        setupTable(tableName, columns);
    }

    /**
     * 
     * @param size number of columns/retrieved feilds
     */
    public AdHocPersistentObject(int size) {
        String[] columns = new String[size];
        for (int i = 0; i < size; i++) {
            columns[i] = "column" + i;
        }
        setupTable("adHoc", columns);
    }

    /**
     * Sets column name at specific index
     * 
     * @param index column index
     * @param name column name
     * @return itself
     * @throws IndexOutOfBoundsException if the index is out of range (index
     * 		  &lt; 0 || index &gt;= record().getTable().size()).
     */
    public AdHocPersistentObject setColumnName(int index, String name) {
        record().getTable().getColumn(index).setColumnName(name);
        return this;
    }

    /**
     * Created meta data structures and Record
     * 
     * @param tableName table name
     * @param columns  column names
     */
    protected void setupTable(String tableName, String[] columns) {
        MetaTable table = new MetaTable(tableName, tableName, columns.length, 0);
        for (int i = 0; i < columns.length; i++) {
            MetaColumn column = new MetaColumn(columns[i], table);
            table.addColumn(column);
        }
        record(new Record(table));
    }
}
