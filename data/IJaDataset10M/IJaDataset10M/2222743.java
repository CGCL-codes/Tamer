package edu.rabbit.kernel.schema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.antlr.runtime.tree.CommonTree;
import edu.rabbit.DbErrorCode;
import edu.rabbit.DbException;
import edu.rabbit.schema.IColumnConstraint;
import edu.rabbit.schema.IColumnDef;
import edu.rabbit.schema.IColumnPrimaryKey;
import edu.rabbit.schema.IColumnUnique;
import edu.rabbit.schema.ITableConstraint;
import edu.rabbit.schema.ITableDef;
import edu.rabbit.schema.ITablePrimaryKey;
import edu.rabbit.schema.ITableUnique;

/**
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 */
public class TableDef implements ITableDef {

    private static String AUTOINDEX = "sqlite_autoindex_%s_%d";

    private final String name;

    private final String databaseName;

    private final boolean temporary;

    private final boolean ifNotExists;

    private final List<IColumnDef> columns;

    private final List<ITableConstraint> constraints;

    private int page;

    private long rowId;

    private boolean rowIdPrimaryKey;

    private boolean autoincremented;

    private String primaryKeyIndexName;

    private String rowIdPrimaryKeyColumnName;

    private int rowIdPrimaryKeyColumnIndex = -1;

    private final List<String> primaryKeyColumns = new ArrayList<String>();

    private final Map<String, ColumnIndexConstraint> columnConstraintsIndexCache = new TreeMap<String, ColumnIndexConstraint>(String.CASE_INSENSITIVE_ORDER);

    private final Map<String, TableIndexConstraint> tableConstrainsIndexCache = new TreeMap<String, TableIndexConstraint>(String.CASE_INSENSITIVE_ORDER);

    TableDef(String name, String databaseName, boolean temporary, boolean ifNotExists, List<IColumnDef> columns, List<ITableConstraint> constraints, int page, long rowid) {
        this.name = name;
        this.databaseName = databaseName;
        this.temporary = temporary;
        this.ifNotExists = ifNotExists;
        this.columns = Collections.unmodifiableList(columns);
        this.constraints = Collections.unmodifiableList(constraints);
        this.page = page;
        this.rowId = rowid;
    }

    public TableDef(CommonTree ast, int page) throws DbException {
        CommonTree optionsNode = (CommonTree) ast.getChild(0);
        temporary = hasOption(optionsNode, "temporary");
        ifNotExists = hasOption(optionsNode, "exists");
        CommonTree nameNode = (CommonTree) ast.getChild(1);
        name = nameNode.getText();
        databaseName = nameNode.getChildCount() > 0 ? nameNode.getChild(0).getText() : null;
        List<IColumnDef> columns = new ArrayList<IColumnDef>();
        List<ITableConstraint> constraints = new ArrayList<ITableConstraint>();
        if (ast.getChildCount() > 2) {
            CommonTree defNode = (CommonTree) ast.getChild(2);
            if ("columns".equalsIgnoreCase(defNode.getText())) {
                for (int i = 0; i < defNode.getChildCount(); i++) {
                    columns.add(new ColumnDef((CommonTree) defNode.getChild(i)));
                }
                if (ast.getChildCount() > 3) {
                    CommonTree constraintsNode = (CommonTree) ast.getChild(3);
                    assert "constraints".equalsIgnoreCase(constraintsNode.getText());
                    for (int i = 0; i < constraintsNode.getChildCount(); i++) {
                        CommonTree constraintRootNode = (CommonTree) constraintsNode.getChild(i);
                        assert "table_constraint".equalsIgnoreCase(constraintRootNode.getText());
                        CommonTree constraintNode = (CommonTree) constraintRootNode.getChild(0);
                        String constraintType = constraintNode.getText();
                        String constraintName = constraintRootNode.getChildCount() > 1 ? constraintRootNode.getChild(1).getText() : null;
                        if ("primary".equalsIgnoreCase(constraintType)) {
                            constraints.add(new TablePrimaryKey(constraintName, constraintNode));
                        } else if ("unique".equalsIgnoreCase(constraintType)) {
                            constraints.add(new TableUnique(constraintName, constraintNode));
                        } else if ("check".equalsIgnoreCase(constraintType)) {
                            constraints.add(new TableCheck(constraintName, constraintNode));
                        } else if ("foreign".equalsIgnoreCase(constraintType)) {
                            constraints.add(new TableForeignKey(constraintName, constraintNode));
                        } else {
                            assert false;
                        }
                    }
                }
            } else {
            }
        }
        this.columns = Collections.unmodifiableList(columns);
        this.constraints = Collections.unmodifiableList(constraints);
        this.page = page;
        resolveConstraints();
    }

    private void resolveConstraints() throws DbException {
        int columnIndex = 0, autoindexNumber = 0;
        for (IColumnDef column : columns) {
            for (IColumnConstraint constraint : column.getConstraints()) {
                if (constraint instanceof IColumnPrimaryKey) {
                    ColumnPrimaryKey pk = (ColumnPrimaryKey) constraint;
                    primaryKeyColumns.add(column.getName());
                    if (column.hasExactlyIntegerType()) {
                        rowIdPrimaryKeyColumnName = column.getName();
                        rowIdPrimaryKeyColumnIndex = columnIndex;
                        rowIdPrimaryKey = true;
                        autoincremented = pk.isAutoincremented();
                    } else {
                        pk.setIndexName(primaryKeyIndexName = generateAutoIndexName(getName(), ++autoindexNumber));
                        columnConstraintsIndexCache.put(pk.getIndexName(), pk);
                    }
                } else if (constraint instanceof IColumnUnique) {
                    ColumnUnique uc = (ColumnUnique) constraint;
                    uc.setIndexName(generateAutoIndexName(getName(), ++autoindexNumber));
                    columnConstraintsIndexCache.put(uc.getIndexName(), uc);
                }
            }
            columnIndex++;
        }
        for (ITableConstraint constraint : constraints) {
            if (constraint instanceof ITablePrimaryKey) {
                boolean b = false;
                TablePrimaryKey pk = (TablePrimaryKey) constraint;
                assert primaryKeyColumns.isEmpty();
                primaryKeyColumns.addAll(pk.getColumns());
                if (pk.getColumns().size() == 1) {
                    final String n = pk.getColumns().get(0);
                    final IColumnDef c = getColumn(n);
                    if (null == c) {
                        throw new DbException(DbErrorCode.ERROR, "Wrong column '" + n + "' in PRIMARY KEY");
                    } else if (c.hasExactlyIntegerType()) {
                        rowIdPrimaryKeyColumnName = n;
                        rowIdPrimaryKeyColumnIndex = getColumnNumber(n);
                        rowIdPrimaryKey = true;
                        b = true;
                    }
                }
                if (!b) {
                    pk.setIndexName(primaryKeyIndexName = generateAutoIndexName(getName(), ++autoindexNumber));
                    tableConstrainsIndexCache.put(pk.getIndexName(), pk);
                }
            } else if (constraint instanceof ITableUnique) {
                TableUnique uc = (TableUnique) constraint;
                uc.setIndexName(generateAutoIndexName(getName(), ++autoindexNumber));
                tableConstrainsIndexCache.put(uc.getIndexName(), uc);
            }
        }
    }

    private static String generateAutoIndexName(String tableName, int i) {
        return String.format(AUTOINDEX, tableName, i);
    }

    private boolean hasOption(CommonTree optionsNode, String name) {
        for (int i = 0; i < optionsNode.getChildCount(); i++) {
            CommonTree optionNode = (CommonTree) optionsNode.getChild(i);
            if (name.equalsIgnoreCase(optionNode.getText())) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public boolean isKeepExisting() {
        return ifNotExists;
    }

    public List<IColumnDef> getColumns() {
        return columns;
    }

    public IColumnDef getColumn(String name) {
        for (IColumnDef column : getColumns()) {
            if (column.getName().equalsIgnoreCase(name)) {
                return column;
            }
        }
        return null;
    }

    public int getColumnNumber(String name) {
        int i = 0;
        for (IColumnDef column : getColumns()) {
            if (column.getName().equalsIgnoreCase(name)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public List<ITableConstraint> getConstraints() {
        return constraints;
    }

    public boolean isRowIdPrimaryKey() {
        return rowIdPrimaryKey;
    }

    public boolean isAutoincremented() {
        return autoincremented;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    /**
     * Returns name of the primary key index.
     */
    public String getPrimaryKeyIndexName() {
        return primaryKeyIndexName;
    }

    public String getRowIdPrimaryKeyColumnName() {
        return rowIdPrimaryKeyColumnName;
    }

    public int getRowIdPrimaryKeyColumnIndex() {
        return rowIdPrimaryKeyColumnIndex;
    }

    public List<String> getPrimaryKeyColumnNames() {
        return primaryKeyColumns;
    }

    public ColumnIndexConstraint getColumnIndexConstraint(String indexName) {
        return columnConstraintsIndexCache.get(indexName);
    }

    public TableIndexConstraint getTableIndexConstraint(String indexName) {
        return tableConstrainsIndexCache.get(indexName);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getPage());
        buffer.append("/");
        buffer.append(getRowId());
        buffer.append(": ");
        buffer.append(toSQL(false));
        return buffer.toString();
    }

    public String toSQL() {
        return toSQL(true);
    }

    public String toSQL(boolean schemaStrict) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE ");
        if (isTemporary()) {
            buffer.append("TEMPORARY ");
        }
        buffer.append("TABLE ");
        if (!schemaStrict) {
            if (isKeepExisting()) {
                buffer.append("IF NOT EXISTS ");
            }
            if (getDatabaseName() != null) {
                buffer.append(getDatabaseName());
                buffer.append('.');
            }
        }
        buffer.append(getName());
        buffer.append(" (");
        List<IColumnDef> columns = getColumns();
        for (int i = 0; i < columns.size(); i++) {
            if (i > 0) {
                buffer.append(", ");
            }
            buffer.append(columns.get(i).toString());
        }
        List<ITableConstraint> constraints = getConstraints();
        for (int i = 0; i < constraints.size(); i++) {
            buffer.append(", ");
            buffer.append(constraints.get(i).toString());
        }
        buffer.append(')');
        return buffer.toString();
    }
}
