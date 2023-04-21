package datadog.api;

import datadog.db.model.TableSet;
import datadog.db.model.Column;
import datadog.util.Reflector;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;

/**
 * Base behavior of all relational mappings. Set the name of foreign class we're
 * making a relation to, annd then add foreign keys to it.
 *
 * @author Justin Tomich
 */
public abstract class RelationalMappingDescriptor extends MappingDescriptor {

    String foreignClassName;

    Map localColumnToForeignColumn = new LinkedHashMap();

    boolean parentRelationship = true;

    protected RelationalMappingDescriptor() {
    }

    protected String[] columnNames() {
        Set columnNames = localColumnToForeignColumn.keySet();
        return (String[]) columnNames.toArray(new String[0]);
    }

    /**
     * Foreign clas we're mapping too. NOT the descriptor, but the actual mapped
     * class.
     *
     * @param foreignClassName
     */
    public void setForeignClass(String foreignClassName) {
        this.foreignClassName = foreignClassName;
    }

    /**
     * This mapping is one of two mappings in a bi-directional mapping, and it
     * is not the "parent" mapping.
     *
     * todo rename
     */
    public void notParentRelationship() {
        parentRelationship = false;
    }

    public boolean isParentRelationship() {
        return parentRelationship;
    }

    /**
     * @param localColumnName
     * @param foreignColumnName
     */
    public void addLocalToForeignColumn(String localColumnName, String foreignColumnName) {
        localColumnToForeignColumn.put(localColumnName, foreignColumnName);
    }

    public Map getLocalToForeignColumnNameMap() {
        return localColumnToForeignColumn;
    }

    public void validate() {
    }

    protected Class foreignClass() {
        try {
            return Reflector.classForNameThrows(foreignClassName);
        } catch (ClassNotFoundException e) {
            throw ConfigRuntimeException.classNotFound(foreignClassName, e);
        }
    }

    /**
     * @param tableSet
     * @return column -> column name
     */
    protected Map localColumnToForeignNameMap(TableSet tableSet) {
        Map columnToColumnName = new LinkedHashMap();
        Iterator localColumnNames = localColumnToForeignColumn.keySet().iterator();
        while (localColumnNames.hasNext()) {
            String localName = (String) localColumnNames.next();
            Column localColumn = getColumn(tableSet, localName);
            String foreignName = (String) localColumnToForeignColumn.get(localName);
            columnToColumnName.put(localColumn, foreignName);
        }
        return columnToColumnName;
    }

    public String toString() {
        return "RelationalMappingDescriptor{" + "foreignClassName='" + foreignClassName + "'" + ", localColumnToForeignColumn=" + localColumnToForeignColumn + "}";
    }
}
