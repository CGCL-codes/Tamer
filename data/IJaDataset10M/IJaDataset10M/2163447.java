package com.completex.objective.components.persistency.core;

import com.completex.objective.components.persistency.Mappable;
import com.completex.objective.components.persistency.policy.DatabasePolicy;
import com.completex.objective.util.PropertyMap;
import com.completex.objective.util.StringUtil;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents database join. Depending on the database policy it will produce either ANSI or proprietiry 
 * join SQL fragments
 * 
 * @author Gennady Krizhevsky
 */
public interface Join {

    public static final String SPC = " ";

    public static final Type JOIN = Type.JOIN;

    public static final Type INNER = Type.INNER;

    public static final Type LEFT = Type.LEFT;

    public static final Type RIGHT = Type.RIGHT;

    public static final Type NULL_JOIN = Type.NULL_JOIN;

    /**
     * @see TableIterator
     * 
     * @return TableIterator
     */
    TableIterator iterator();

    /**
     * Compiles join
     * 
     * @param policy
     * @return compiled Join (same reference)
     */
    Join compile(DatabasePolicy policy);

    /**
     * Object representation of clause like 
     *  ... LEFT JOIN <joinedTableName> ON (<firstTableColumns> = <firstTableColumns>)
     * 
     * @param joinedTableName
     * @param joinedTableAlias
     * @param firstTableColumns
     * @param joinedTableColumns
     * @return this Join
     */
    Join addLeftJoin(String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns);

    /**
     * Object representation of clause like 
     *  ... RIGHT JOIN <joinedTableName> ON (<firstTableColumns> = <firstTableColumns>)
     * 
     * @param joinedTableName
     * @param joinedTableAlias
     * @param firstTableColumns
     * @param joinedTableColumns
     * @return this Join
     */
    Join addRightJoin(String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns);

    /**
     * Object representation of clause like 
     *  ... INNER JOIN <joinedTableName> ON (<firstTableColumns> = <firstTableColumns>)
     * 
     * @param joinedTableName
     * @param joinedTableAlias
     * @param firstTableColumns
     * @param joinedTableColumns
     * @return this Join
     */
    Join addInnerJoin(String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns);

    /**
     * Add root table in join
     * 
     * @param joinedTableName
     * @param joinedTableAlias
     * @return this Join
     */
    Join addInnerJoin(String joinedTableName, String joinedTableAlias);

    Join addJoin(Type type, String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns);

    public Join addInnerJoin(String joinedTableName);

    public Join addInnerJoin(String joinedTableName, String[] firstTableColumns, String[] joinedTableColumns);

    public Join addLeftJoin(String joinedTableName, String[] firstTableColumns, String[] joinedTableColumns);

    public Join addRightJoin(String joinedTableName, String[] firstTableColumns, String[] joinedTableColumns);

    public Join addJoin(Type type, String joinedTableName, String[] firstTableColumns, String[] joinedTableColumns);

    /**
     * This method will produce join of all tables excluding the very 1st one.
     * It assumes that only the 2nd table is connected to the 1st one. Otherwise
     * it will produce result that does not make sense
     * @return join of all tables excluding the 1st one
     */
    Join joinMinusOne();

    /**
     * 
     * @return from
     */
    String getFrom();

    /**
     * Returns different result depending on whether ANSI or proprietory joing is used
     * 
     * @return where
     */
    String getWhere();

    /**
     * Returns different result depending on whether ANSI or proprietory joing is used
     * 
     * @return true if Join is compiled
     */
    boolean isCompiled();

    /**
     * @see Object#clone()
     */
    Object clone() throws CloneNotSupportedException;

    Table getLastAddedTable();

    Table getFirstAddedTable();

    void addTable(Table table);

    Join addInnerJoin(String joinedTableName, String firstTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns);

    Join addLeftJoin(String joinedTableName, String firstTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns);

    Join addRightJoin(String joinedTableName, String firstTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns);

    Join addJoin(Type type, String firstTableName, String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns);

    int size();

    /**
     * Join type class
     */
    public static class Type implements Serializable {

        public static final Type NULL_JOIN = new Type("NULL_JOIN");

        public static final Type JOIN = new Type("JOIN");

        public static final Type INNER = new Type("INNER");

        public static final Type LEFT = new Type("LEFT");

        public static final Type RIGHT = new Type("RIGHT");

        private static final HashMap TYPES = new HashMap();

        static {
            TYPES.put(NULL_JOIN.getName(), NULL_JOIN);
            TYPES.put(JOIN.getName(), JOIN);
            TYPES.put(INNER.getName(), INNER);
            TYPES.put(LEFT.getName(), LEFT);
            TYPES.put(RIGHT.getName(), RIGHT);
        }

        private String name;

        public Type(String name) {
            this.name = name;
        }

        /**
         * Do not change it - for some stupid reason it is used in sql!!!
         * @return type name
         */
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public static Type name2type(String name) {
            return (Type) TYPES.get(name);
        }
    }

    /**
     * Join table type class
     */
    public static class Table implements Serializable, Mappable {

        public static final String[] NULL_ARRAY = new String[0];

        private Type type = NULL_JOIN;

        private String firstTableAlias;

        private String joinedTableName;

        private String joinedTableAlias;

        private String[] firstTableColumns;

        private String[] joinedTableColumns;

        private String linkName;

        private static final String TAG_JOINED_TABLE_NAME = "joinedTableName";

        private static final String TAG_JOINED_TABLE_ALIAS = "joinedTableAlias";

        private static final String TAG_FIRST_TABLE_COLUMNS = "firstTableColumns";

        private static final String TAG_JOINED_TABLE_COLUMNS = "joinedTableColumns";

        private static final String TAG_LINK_NAME = "linkName";

        private static final String TAG_TYPE = "type";

        public Table(Map map) {
            fromMap(map);
        }

        public Table(Type type, String firstTableAlias, String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
            if (type != null) {
                this.type = type;
            } else {
                this.type = INNER;
            }
            this.firstTableAlias = firstTableAlias;
            this.joinedTableName = joinedTableName;
            this.joinedTableAlias = joinedTableAlias;
            this.firstTableColumns = firstTableColumns == null ? NULL_ARRAY : firstTableColumns;
            this.joinedTableColumns = joinedTableColumns == null ? NULL_ARRAY : joinedTableColumns;
        }

        public Type getJoinType() {
            return type;
        }

        public String getJoinedTableName() {
            return joinedTableName;
        }

        public String getFullJoinedTableNameForFrom() {
            if (StringUtil.isEmpty(joinedTableAlias)) {
                return joinedTableName;
            } else {
                return new StringBuffer(joinedTableName).append(" ").append(joinedTableAlias).toString();
            }
        }

        public String getFullFirstColumnName(int index) {
            return fullColumnName(firstTableAlias, firstTableColumns[index]);
        }

        public String getFullJoinedColumnName(int index) {
            return fullColumnName(resolveJoinedTableName(), joinedTableColumns[index]);
        }

        protected String fullColumnName(String resolvedTableName, String columnName) {
            if (columnName.indexOf('.') > 0) {
                return columnName;
            } else {
                return new StringBuffer(resolvedTableName).append(".").append(columnName).toString();
            }
        }

        public String resolveJoinedTableName() {
            if (StringUtil.isEmpty(joinedTableAlias)) {
                return joinedTableName;
            } else {
                return joinedTableAlias;
            }
        }

        public String getJoinedTableAlias() {
            return joinedTableAlias;
        }

        public void setJoinedTableAlias(String joinedTableAlias) {
            this.joinedTableAlias = joinedTableAlias;
        }

        public String getFirstTableAlias() {
            return firstTableAlias;
        }

        public void setFirstTableAlias(String firstTableAlias) {
            this.firstTableAlias = firstTableAlias;
        }

        public String[] getFirstTableColumns() {
            return firstTableColumns;
        }

        public String[] getJoinedTableColumns() {
            return joinedTableColumns;
        }

        public StringBuffer joinTableToString() {
            StringBuffer joinTable = new StringBuffer();
            joinTable.append(" (").append(joinedTableName).append(") ").append(joinedTableAlias).append(" ");
            return joinTable;
        }

        public String getLinkName() {
            return linkName;
        }

        public void setLinkName(String linkName) {
            this.linkName = linkName;
        }

        public boolean equals(Object value) {
            if (this == value) return true;
            if (value == null || getClass() != value.getClass()) return false;
            final Table table = (Table) value;
            if (!Arrays.equals(firstTableColumns, table.firstTableColumns)) return false;
            if (joinedTableAlias != null ? !joinedTableAlias.equals(table.joinedTableAlias) : table.joinedTableAlias != null) return false;
            if (!Arrays.equals(joinedTableColumns, table.joinedTableColumns)) return false;
            if (joinedTableName != null ? !joinedTableName.equals(table.joinedTableName) : table.joinedTableName != null) return false;
            if (type != null ? !type.equals(table.type) : table.type != null) return false;
            return true;
        }

        public int hashCode() {
            int result;
            result = (type != null ? type.hashCode() : 0);
            result = 29 * result + (joinedTableName != null ? joinedTableName.hashCode() : 0);
            result = 29 * result + (joinedTableAlias != null ? joinedTableAlias.hashCode() : 0);
            result = 29 * result + (linkName != null ? linkName.hashCode() : 0);
            return result;
        }

        public String toString() {
            return " { " + "joinedTableName = " + joinedTableName + "; joinedTableAlias = " + joinedTableAlias + "; firstTableColumns = " + Arrays.asList(firstTableColumns) + "; joinedTableColumns = " + Arrays.asList(joinedTableColumns) + "; linkName = " + linkName + "; type = " + type + " }";
        }

        public Map toMap() {
            HashMap map = new HashMap();
            map.put(TAG_JOINED_TABLE_NAME, joinedTableName);
            map.put(TAG_JOINED_TABLE_ALIAS, joinedTableAlias);
            map.put(TAG_FIRST_TABLE_COLUMNS, Arrays.asList(firstTableColumns));
            map.put(TAG_JOINED_TABLE_COLUMNS, Arrays.asList(joinedTableColumns));
            map.put(TAG_LINK_NAME, linkName);
            map.put(TAG_TYPE, type.getName());
            return map;
        }

        public void fromMap(Map map) {
            PropertyMap propertyMap = PropertyMap.toPropertyMap(map);
            joinedTableName = propertyMap.getProperty(TAG_JOINED_TABLE_NAME, true);
            joinedTableAlias = propertyMap.getProperty(TAG_JOINED_TABLE_ALIAS);
            firstTableColumns = extractColumns(propertyMap, TAG_FIRST_TABLE_COLUMNS);
            joinedTableColumns = extractColumns(propertyMap, TAG_JOINED_TABLE_COLUMNS);
            linkName = propertyMap.getProperty(TAG_LINK_NAME);
            String typeName = propertyMap.getProperty(TAG_TYPE, true);
            type = Type.name2type(typeName);
        }

        private String[] extractColumns(PropertyMap propertyMap, String tag) {
            String[] firstTableColumns = null;
            List firstTableColumnsList = propertyMap.getList(tag);
            if (firstTableColumnsList != null) {
                firstTableColumns = (String[]) firstTableColumnsList.toArray(new String[firstTableColumnsList.size()]);
            }
            return firstTableColumns;
        }
    }

    /**
     * Join table iterator
     */
    public static class TableIterator {

        private Iterator joinedTablesIterator;

        public TableIterator(LinkedHashMap joinedTables) {
            if (joinedTables != null) {
                this.joinedTablesIterator = joinedTables.entrySet().iterator();
            }
        }

        public boolean hasNext() {
            return joinedTablesIterator != null && joinedTablesIterator.hasNext();
        }

        public Table next() {
            Map.Entry entry = (Map.Entry) joinedTablesIterator.next();
            return (Table) entry.getValue();
        }
    }
}
