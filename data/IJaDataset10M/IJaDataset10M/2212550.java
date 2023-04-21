package com.agimatec.dbtransform;

import com.agimatec.sql.meta.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.*;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 05.06.2007 <br/>
 * Time: 15:30:57 <br/>
 * Copyright: Agimatec GmbH
 */
@XStreamAlias("conversion")
public class CatalogConversion {

    private final String name;

    private int maxLengthForConstraints;

    private boolean filterIndices;

    private final List<DataTypeTransformation> dataTypes = new ArrayList();

    private Set<String> globalUniqueNames;

    public CatalogConversion(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public DataType matchDataType(DataType from) {
        for (DataTypeTransformation entry : dataTypes) {
            if (entry.getSource().match(from)) {
                DataType dt = entry.getTarget().deepCopy();
                if (dt.isPrecisionEnabled() == null) dt.setPrecisionEnabled(from.isPrecisionEnabled());
                if (dt.getPrecision() == null && dt.isPrecisionEnabled() == Boolean.TRUE) dt.setPrecision(from.getPrecision());
                if (dt.getScale() == null) dt.setScale(from.getScale());
                return dt;
            }
        }
        return from;
    }

    public void addTransformation(DataType from, DataType to) {
        dataTypes.add(new DataTypeTransformation(from, to));
    }

    public void addTransformation(String from, String to) {
        addTransformation(new DataType(from), new DataType(to));
    }

    /** max length for constraints and index names */
    public int getMaxLengthForConstraints() {
        return maxLengthForConstraints;
    }

    public void setMaxLengthForConstraints(int maxLengthForConstraints) {
        this.maxLengthForConstraints = maxLengthForConstraints;
    }

    public boolean isFilterIndices() {
        return filterIndices;
    }

    /**
     * remove explicit indices when same columns already have a constraint (used for
     * oracle-default behavior)
     */
    public void setFilterIndices(boolean filterIndices) {
        this.filterIndices = filterIndices;
    }

    public CatalogDescription transformCatalog(CatalogDescription catalog) {
        getGlobalUniqueNames().clear();
        CatalogDescription newCatalog = catalog.deepCopy();
        List<String> tableNames = new ArrayList(newCatalog.getTables().keySet());
        Collections.sort(tableNames);
        for (String tableName : tableNames) {
            TableDescription newTable = newCatalog.getTable(tableName);
            TableDescription table = catalog.getTable(newTable.getTableName());
            transformTable(newTable, table);
        }
        return newCatalog;
    }

    public void transformTable(TableDescription newTable, TableDescription table) {
        for (int idx = 0; idx < newTable.getColumnSize(); idx++) {
            ColumnDescription column = table.getColumn(idx);
            ColumnDescription newColumn = newTable.getColumn(idx);
            transformColumn(newColumn, column);
        }
        for (int idx = 0; idx < newTable.getConstraintSize(); idx++) {
            IndexDescription constraint = table.getConstraint(idx);
            IndexDescription newContraint = newTable.getConstraint(idx);
            transformIndex(newContraint, constraint);
        }
        for (int idx = 0; idx < newTable.getForeignKeySize(); idx++) {
            ForeignKeyDescription fk = table.getForeignKey(idx);
            ForeignKeyDescription newFk = newTable.getForeignKey(idx);
            transformForeignKey(newFk, fk);
        }
        for (int idx = 0; idx < table.getIndexSize(); idx++) {
            IndexDescription index = table.getIndex(idx);
            IndexDescription newIndex = newTable.getIndex(index.getIndexName());
            if (isFilterIndices() && null != newTable.findConstraintForColumns(newIndex.getColumns())) {
                newTable.removeIndex(index.getIndexName());
            } else {
                transformIndex(newIndex, index);
            }
        }
        IndexDescription pkIndex = table.getPrimaryKey();
        IndexDescription newPkIndex = newTable.getPrimaryKey();
        if (newPkIndex != null && pkIndex != null) {
            transformIndex(newPkIndex, pkIndex);
        }
    }

    protected void transformForeignKey(ForeignKeyDescription newFk, ForeignKeyDescription fk) {
        newFk.setConstraintName(shortenName(fk.getConstraintName(), getGlobalUniqueNames()));
    }

    protected void transformIndex(IndexDescription newContraint, IndexDescription constraint) {
        newContraint.setIndexName(shortenName(constraint.getIndexName(), getGlobalUniqueNames()));
    }

    private Set getGlobalUniqueNames() {
        if (globalUniqueNames == null) globalUniqueNames = new HashSet();
        return globalUniqueNames;
    }

    public void transformColumn(ColumnDescription newColumn, ColumnDescription column) {
        DataType dt = toDataType(column);
        DataType newDT = matchDataType(dt);
        newColumn.setTypeName(newDT.getTypeName());
        newColumn.setPrecision(newDT.getPrecision() == null ? 0 : newDT.getPrecision());
        newColumn.setPrecisionEnabled(newDT.isPrecisionEnabled() != null && newDT.isPrecisionEnabled());
        newColumn.setScale(newDT.getScale() == null ? 0 : newDT.getScale());
        newColumn.setDefaultValue(convertDefaultValueFrom(newDT, dt, column.getDefaultValue()));
    }

    protected String convertDefaultValueFrom(DataType newDT, DataType dt, String defaultValue) {
        if (newDT.equals(dt) || defaultValue == null) {
            return defaultValue;
        }
        if (newDT.getTypeName().equals("NUMBER")) {
            if ("false".equalsIgnoreCase(defaultValue)) return "0";
            if ("true".equalsIgnoreCase(defaultValue)) return "1";
        }
        return defaultValue;
    }

    protected DataType toDataType(ColumnDescription column) {
        DataType dt = new DataType();
        dt.setTypeName(column.getTypeName());
        if (column.isPrecisionEnabled()) {
            dt.setPrecision(column.getPrecision());
            dt.setPrecisionEnabled(true);
        }
        if (column.getScale() != 0) {
            dt.setScale(column.getScale());
        }
        return dt;
    }

    protected String shortenName(String name, Set domain) {
        if (name == null) return name;
        if (name.length() > getMaxLengthForConstraints()) {
            String newName = name.substring(0, getMaxLengthForConstraints());
            if (domain.contains(newName)) {
                String baseName = name.substring(0, getMaxLengthForConstraints() - 1);
                for (int i = 1; i < 10; i++) {
                    newName = baseName + i;
                    if (!domain.contains(newName)) break;
                }
                if (domain.contains(newName)) {
                    baseName = name.substring(0, getMaxLengthForConstraints() - 2);
                    for (int i = 10; i < 100; i++) {
                        newName = baseName + i;
                        if (!domain.contains(newName)) break;
                    }
                    if (domain.contains(newName)) return null;
                }
            }
            domain.add(newName);
            return newName;
        } else {
            domain.add(name);
        }
        return name;
    }
}
