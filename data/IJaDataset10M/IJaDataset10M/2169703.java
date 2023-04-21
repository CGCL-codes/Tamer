package com.completex.objective.components.persistency.core.impl;

import com.completex.objective.components.persistency.Mappable;
import com.completex.objective.components.persistency.OdalRuntimePersistencyException;
import com.completex.objective.components.persistency.policy.DatabasePolicy;
import com.completex.objective.components.persistency.core.Join;
import com.completex.objective.util.PropertyMap;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Gennady Krizhevsky
 */
public class JoinImpl implements Join, Cloneable, Serializable, Mappable {

    static final long serialVersionUID = 1L;

    private LinkedHashMap tables;

    private String from;

    private String where;

    private boolean compiled;

    private static final String TAG_TABLES = "tables";

    private static final String TAG_FROM = "from";

    private static final String TAG_WHERE = "where";

    private static final String TAG_COMPILED = "compiled";

    public JoinImpl(Map map) {
        fromMap(map);
    }

    public JoinImpl(JoinImpl join) {
        tables = join.tables;
        from = join.from;
        where = join.where;
        compiled = join.compiled;
    }

    public JoinImpl(String firstTable) {
        addInnerJoin(firstTable, null);
    }

    public JoinImpl(String firstTable, String firstTableAlias) {
        addInnerJoin(firstTable, firstTableAlias);
    }

    public boolean isCompiled() {
        return compiled;
    }

    public void setCompiled(boolean compiled) {
        this.compiled = compiled;
    }

    public void decompile() {
        setCompiled(false);
    }

    public String getFrom() {
        return from;
    }

    public String getWhere() {
        return where;
    }

    public Join addInnerJoin(String joinedTableName) {
        return addInnerJoin(joinedTableName, null, null);
    }

    public Join addInnerJoin(String joinedTableName, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(INNER, joinedTableName, null, firstTableColumns, joinedTableColumns);
    }

    public Join addLeftJoin(String joinedTableName, String[] firstTableColumns, String[] joinedTableColumns) {
        return addJoin(LEFT, joinedTableName, firstTableColumns, joinedTableColumns);
    }

    public Join addRightJoin(String joinedTableName, String[] firstTableColumns, String[] joinedTableColumns) {
        return addJoin(RIGHT, joinedTableName, firstTableColumns, joinedTableColumns);
    }

    public Join addJoin(Type type, String joinedTableName, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(type, joinedTableName, null, firstTableColumns, joinedTableColumns);
    }

    /**
     * Add root table in join
     *
     * @param joinedTableName
     * @param joinedTableAlias
     * @return self
     */
    public Join addInnerJoin(String joinedTableName, String joinedTableAlias) {
        return add(null, joinedTableName, joinedTableAlias, null, null);
    }

    public Join addInnerJoin(String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(INNER, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
    }

    public Join addLeftJoin(String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(LEFT, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
    }

    public Join addRightJoin(String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(RIGHT, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
    }

    public Join addJoin(Type type, String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(type, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
    }

    public Join addInnerJoin(String joinedTableName, String firstTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(INNER, firstTableName, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
    }

    public Join addLeftJoin(String joinedTableName, String firstTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(LEFT, firstTableName, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
    }

    public Join addRightJoin(String joinedTableName, String firstTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(RIGHT, firstTableName, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
    }

    public Join addJoin(Type type, String firstTableName, String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        return add(type, firstTableName, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
    }

    public TableIterator iterator() {
        return new TableIterator(tables);
    }

    protected JoinImpl add(Type type, String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        add(type, null, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
        return this;
    }

    protected JoinImpl add(Type type, String firstTableName, String joinedTableName, String joinedTableAlias, String[] firstTableColumns, String[] joinedTableColumns) {
        if (joinedTableName == null || joinedTableName.length() == 0) {
            throw new IllegalArgumentException("Table name is empty");
        }
        Table table = new Table(type, firstTableName, joinedTableName, joinedTableAlias, firstTableColumns, joinedTableColumns);
        if (!lazyTables().containsValue(table)) {
            addTable(table);
        }
        return this;
    }

    public void addTable(Table table) {
        lazyTables().put(table.getFullJoinedTableNameForFrom(), table);
    }

    private void validate() {
        if (tables == null || tables.size() == 0) {
            throw new RuntimeException("Join does not contain any tables");
        }
    }

    /**
     * Composes ansiJoin type join sting
     * FROM (Table1 AS a RIGHT JOIN Table2 AS b ON (a.Field1 = b.Field1) AND (a.ID = b.ID)) LEFT JOIN Table3 ON a.ID = Table3.ID;
     *
     * @return self
     */
    private String ansiJoin() {
        validate();
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (TableIterator iterator = iterator(); iterator.hasNext(); i++) {
            Table table = iterator.next();
            if (i > 0) {
                buffer.append(SPC).append(table.getJoinType()).append(SPC).append(JOIN).append(SPC);
            }
            buffer.append(table.getFullJoinedTableNameForFrom());
            if (i > 0) {
                if (table.getFirstTableColumns().length != table.getJoinedTableColumns().length) {
                    throw new RuntimeException("Size of first table columns [" + table.getFirstTableColumns().length + "] is not equal to size of joined table columns [" + table.getJoinedTableColumns().length + "]");
                }
                for (int k = 0; k < table.getFirstTableColumns().length; k++) {
                    if (k == 0) {
                        buffer.append(SPC).append("ON").append(SPC);
                    } else if (k > 0) {
                        buffer.append(SPC).append("AND").append(SPC);
                    }
                    String fullJoinedColumnName = table.getFullJoinedColumnName(k);
                    String firstTableColumnName = table.getFullFirstColumnName(k);
                    buffer.append("(").append(firstTableColumnName).append(" = ").append(fullJoinedColumnName).append(")");
                }
            }
        }
        return buffer.toString();
    }

    private String proprietoryJoin(DatabasePolicy policy) {
        validate();
        return policy.proprietoryJoin(this);
    }

    private void populateProprietoryFrom() {
        StringBuffer buffer = new StringBuffer();
        int i = 0;
        for (TableIterator iterator = iterator(); iterator.hasNext(); i++) {
            Table table = iterator.next();
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(table.getFullJoinedTableNameForFrom());
        }
        from = buffer.toString();
    }

    public synchronized Join compile(DatabasePolicy policy) {
        if (!compiled) {
            if (policy.useAnsiJoin()) {
                from = ansiJoin();
            } else {
                populateProprietoryFrom();
                where = proprietoryJoin(policy);
            }
        }
        compiled = true;
        return this;
    }

    private LinkedHashMap lazyTables() {
        if (tables == null) {
            tables = new LinkedHashMap();
        }
        return tables;
    }

    public LinkedHashMap getTables() {
        return tables;
    }

    public int size() {
        return tables == null ? 0 : tables.size();
    }

    public Object clone() {
        JoinImpl clonedJoin;
        try {
            clonedJoin = (JoinImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new OdalRuntimePersistencyException("Cannot clone join", e);
        }
        LinkedHashMap clonedTables;
        if (tables != null) {
            clonedTables = (LinkedHashMap) tables.clone();
            clonedJoin.tables = clonedTables;
        }
        return clonedJoin;
    }

    /**
     * This method will produce join of all tables excluding the very 1st one.
     * It assumes that only the 2nd table is connected to the 1st one. Otherwise
     * it will produce result that does not make sense
     *
     * @return join of all tables excluding the 1st one
     */
    public Join joinMinusOne() {
        return joinMinusOne(0);
    }

    protected Join joinMinusOne(int tableIndexToExclude) {
        int i = 0;
        int realIndex = 0;
        JoinImpl join = null;
        for (TableIterator it = iterator(); it.hasNext(); i++) {
            Table table = it.next();
            if (i != tableIndexToExclude) {
                if (realIndex == 0) {
                    join = new JoinImpl(table.getJoinedTableName());
                } else {
                    join.addJoin(table.getJoinType(), table.getFirstTableAlias(), table.getJoinedTableName(), table.getJoinedTableName(), table.getFirstTableColumns(), table.getJoinedTableColumns());
                }
                realIndex++;
            }
        }
        return join;
    }

    public Table getLastAddedTable() {
        Table lastAddedTable = null;
        Collection collection = lazyTables().values();
        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            lastAddedTable = (Table) iterator.next();
        }
        return lastAddedTable;
    }

    public Table getFirstAddedTable() {
        TableIterator tableIterator = iterator();
        if (tableIterator.hasNext()) {
            return tableIterator.next();
        }
        return null;
    }

    public Map toMap() {
        LinkedHashMap map = new LinkedHashMap();
        LinkedHashMap tablesMap = new LinkedHashMap();
        for (Iterator it = tables.keySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
            Table value = (Table) tables.get(key);
            tablesMap.put(key, value.toMap());
        }
        map.put(TAG_TABLES, tablesMap);
        map.put(TAG_FROM, from);
        map.put(TAG_WHERE, where);
        map.put(TAG_COMPILED, Boolean.valueOf(compiled));
        return map;
    }

    public void fromMap(Map map) {
        PropertyMap propertyMap = PropertyMap.toPropertyMap(map, true);
        Map tablesMap = propertyMap.getMap(TAG_TABLES, true);
        tables = new LinkedHashMap(tablesMap.size());
        for (Iterator it = tablesMap.keySet().iterator(); it.hasNext(); ) {
            Object key = it.next();
            Map value = (Map) tablesMap.get(key);
            Table table = new Table(value);
            tables.put(key, table);
        }
        from = propertyMap.getProperty(TAG_FROM);
        where = propertyMap.getProperty(TAG_WHERE);
        compiled = propertyMap.getBoolean(TAG_COMPILED);
    }
}
