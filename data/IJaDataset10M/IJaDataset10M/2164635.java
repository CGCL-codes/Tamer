package com.byterefinery.rmbench.model.schema;

import com.byterefinery.rmbench.external.model.IForeignKey;
import com.byterefinery.rmbench.external.model.ITable;

/**
 * model representation of a database foreign key
 * 
 * @author cse
 */
public class ForeignKey extends Key implements Constraint {

    public static final String CONSTRAINT_TYPE = "FOREIGN KEY";

    private final IForeignKey iforeignKey = new IForeignKey() {

        public ITable getTable() {
            return ForeignKey.this.getTable().getITable();
        }

        public String getName() {
            return ForeignKey.this.getName();
        }

        public String[] getColumnNames() {
            return ForeignKey.this.getColumnNames();
        }

        public Action getUpdateAction() {
            return ForeignKey.this.updateAction;
        }

        public Action getDeleteAction() {
            return ForeignKey.this.deleteAction;
        }

        public ITable getTargetTable() {
            return target.getITable();
        }
    };

    private final Table target;

    private IForeignKey.Action deleteAction;

    private IForeignKey.Action updateAction;

    /**
     * create a foreign key with unspecified delete and update actions
     * 
     * @param name the constraint name
     * @param columns columns from the owning table
     * @param source the source (owning) table
     * @param target the target table
     */
    public ForeignKey(String name, Column[] columns, Table source, Table target) {
        super(name, columns, source);
        this.target = target;
        initTables(source, target);
        this.deleteAction = IForeignKey.NO_ACTION;
        this.updateAction = IForeignKey.NO_ACTION;
    }

    /**
     * create a foreign key with the specified values
     * 
     * @param name the constraint name
     * @param columns the column names, assumed to be valid names from the owning table
     * @param source the source (owning) table
     * @param target the target table
     * @param deleteAction the delete action
     * @param updateAction the update action
     */
    public ForeignKey(String name, String[] columns, Table source, Table target, IForeignKey.Action deleteAction, IForeignKey.Action updateAction) {
        super(name, columns, source);
        this.target = target;
        initTables(source, target);
        this.deleteAction = deleteAction != null ? deleteAction : IForeignKey.NO_ACTION;
        this.updateAction = updateAction != null ? updateAction : IForeignKey.NO_ACTION;
    }

    private void initTables(Table source, Table target) {
        source.addForeignKey(this);
        target.addReference(this);
    }

    public IForeignKey getIForeignKey() {
        return iforeignKey;
    }

    public Table getTargetTable() {
        return target;
    }

    /**
     * @param column a column which is assumed to be from this key 
     * @return the column from the primary key of the target table which 
     * corresponds to the given column
     */
    public Column getTargetColumn(Column column) {
        int index = -1;
        for (int i = 0; i < columns.length; i++) {
            if (columns[i] == column) {
                index = i;
                break;
            }
        }
        if (index < 0) {
            throw new IllegalArgumentException(column.getName());
        }
        return target.getPrimaryKey().columns[index];
    }

    /**
     * abandon this object by removing its from the source and target table
     * @return this object, for convenience
     * @see #restore()
     */
    public ForeignKey abandon() {
        table.removeForeignKey(this);
        target.removeReference(this);
        return this;
    }

    /**
     * restore this foreign key after it has previously been abandoned
     * @see #abandon()
     */
    public void restore() {
        if (!table.getForeignKeys().contains(this)) {
            table.addForeignKey(this);
            target.addReference(this);
        }
    }

    /**
     * @return the delete action, which is specified in SQL through the ON DELETE clause
     */
    public IForeignKey.Action getDeleteAction() {
        return deleteAction;
    }

    /**
     * @return the update action, which is specified in SQL through the ON UPDATE clause
     */
    public IForeignKey.Action getUpdateAction() {
        return updateAction;
    }

    /**
     * @param action the delete action, which is specified in SQL through the ON DELETE clause
     */
    public void setDeleteAction(IForeignKey.Action action) {
        deleteAction = action;
    }

    /**
     * @param action the update action, which is specified in SQL through the ON UPDATE clause
     */
    public void setUpdateAction(IForeignKey.Action action) {
        updateAction = action;
    }

    /**
     * @return true if this foreign key is fully part of the primary key also
     */
    public boolean isIdentifying() {
        for (int i = 0; i < columns.length; i++) {
            if (!columns[i].belongsToPrimaryKey()) return false;
        }
        return true;
    }

    /**
     * @return true if this foreign key has a non-null column
     */
    public boolean isExistential() {
        for (int i = 0; i < columns.length; i++) {
            if (!columns[i].getNullable()) return true;
        }
        return false;
    }

    /**
     * replace a column in this foreign key. It is assumed that the new column is type-compatible 
     * with the column to be replaced
     *  
     * @param oldColumn the column to be replaced
     * @param newColumn the new column
     * @throws IllegalArgumentException if oldColumn is not part of this key
     */
    public void replaceColumn(Column oldColumn, Column newColumn) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i] == oldColumn) {
                columns[i] = newColumn;
                return;
            }
        }
        throw new IllegalArgumentException(oldColumn.getName());
    }

    public String getConstraintType() {
        return CONSTRAINT_TYPE;
    }

    public String getConstraintBody() {
        return getColumnsList();
    }
}
