package edu.rabbit.kernel.table;

import edu.rabbit.DbException;

/**
 * @author Yuanyan<yanyan.cao@gmail.com>
 * 
 * 
 */
public interface IBtreeSchemaTable extends IBtreeTable {

    int TYPE_FIELD = 0;

    int NAME_FIELD = 1;

    int TABLE_FIELD = 2;

    int PAGE_FIELD = 3;

    int SQL_FIELD = 4;

    String getTypeField() throws DbException;

    String getNameField() throws DbException;

    String getTableField() throws DbException;

    int getPageField() throws DbException;

    String getSqlField() throws DbException;

    long insertRecord(String typeField, String nameField, String tableField, int pageField, String sqlField) throws DbException;

    boolean goToRow(long rowId) throws DbException;

    long getRowId() throws DbException;

    /**
     * @param rowId
     * @param typeField
     * @param nameField
     * @param tableField
     * @param pageField
     * @param sqlField
     * @throws DbException
     */
    void updateRecord(long rowId, String typeField, String nameField, String tableField, int pageField, String sqlField) throws DbException;
}
