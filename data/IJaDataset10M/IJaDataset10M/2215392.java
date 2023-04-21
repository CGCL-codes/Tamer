package com.daffodilwoods.daffodildb.odbc;

import java.sql.Types;
import com.daffodilwoods.daffodildb.server.serversystem._Connection;
import in.co.daffodil.db.jdbc.DatabaseProperties;
import com.daffodilwoods.daffodildb.server.sql99.dql.listenerevents._SelectIterator;
import com.daffodilwoods.daffodildb.client._RecordSetBufferIterator;
import java.sql.SQLException;
import com.daffodilwoods.daffodildb.client._RecordSetBuffer;
import com.daffodilwoods.daffodildb.client.RecordSet;
import com.daffodilwoods.daffodildb.client.TempRecordSetBuffer;
import com.daffodilwoods.database.resource.DException;
import java.util.StringTokenizer;
import com.daffodilwoods.database.utility.P;
import com.daffodilwoods.daffodildb.server.sql99.dql.resultsetmetadata._RowReader;
import com.daffodilwoods.daffodildb.server.sql99.common.Datatypes;
import in.co.daffodil.db.jdbc.Utilities;

public class OdbcMetaData extends OdbcPreparedStatement {

    private static final int COLUMN_OFFSET = 1;

    public OdbcMetaData() {
    }

    public OdbcMetaData(String path) {
    }

    public Object getSQLProcedures(Object conObj, String catalog, String schema, String procedureName) throws Exception {
        _Connection con0 = (_Connection) conObj;
        String query = DatabaseProperties.getProceduresQuery(catalog, schema, procedureName);
        _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
        RecordSet rs = new RecordSet();
        rs.setSelectIterator(iter);
        return rs.getIterator();
    }

    public Object getSQLTablePrivileges(Object conObj, String catalog, String schema, String tableName) throws Exception {
        _Connection con0 = (_Connection) conObj;
        String query = DatabaseProperties.getTablePrivilegesQuery(catalog, schema, tableName);
        _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
        RecordSet rs = new RecordSet();
        rs.setSelectIterator(iter);
        return rs.getIterator();
    }

    public Object getSQLTables(Object conObj, String catalog, String schema, String tableName, String tableType) throws Exception {
        _Connection con0 = (_Connection) conObj;
        String[] type = null;
        if (tableType != null && tableType.trim().length() > 0) {
            StringTokenizer stk = new StringTokenizer(tableType, ",");
            int count = stk.countTokens();
            type = new String[count];
            for (int i = 0; i < count; i++) {
                type[i] = stk.nextToken().trim();
                if (type[i].startsWith("'")) {
                    type[i] = type[i].substring(1, type[i].length() - 1);
                }
            }
        }
        boolean isAllCatalog = catalog != null && catalog.equalsIgnoreCase("%");
        boolean tableEmptyString = tableName != null && tableName.equals("");
        boolean schemaEmptyString = schema != null && schema.equals("");
        String query = null;
        if (isAllCatalog && tableEmptyString && schemaEmptyString) {
            query = DatabaseProperties.getCatalogsQuery();
            _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
            return getTempBufferForAllCatalogs(iter).getIterator();
        }
        boolean isAllSchema = schema != null && schema.equalsIgnoreCase("%");
        boolean catalogEmptyString = catalog != null && catalog.equals("");
        if (isAllSchema && catalogEmptyString && tableEmptyString) {
            query = DatabaseProperties.getSchemasQuery();
            _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
            return getTempBufferForAllSchemas(iter).getIterator();
        }
        boolean isAllTypes = type != null && type.length == 1 && type[0].equals("%");
        if (isAllTypes && catalogEmptyString && schemaEmptyString && tableEmptyString) {
            return getTempBufferForAllTableTypes().getIterator();
        }
        return getTables(con0, catalog, schema, tableName, type);
    }

    private _RecordSetBufferIterator getTables(_Connection con0, String catalog, String schema, String tableName, String[] type) throws SQLException, DException {
        String query = DatabaseProperties.getTablesQuery(catalog, schema, tableName, type);
        _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
        RecordSet rs = new RecordSet();
        rs.setSelectIterator(iter);
        return rs.getIterator();
    }

    private _RecordSetBuffer getTempBufferForAllCatalogs(_SelectIterator iter) throws Exception {
        String[] columnNames = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS" };
        int columnCount = columnNames.length;
        int rowCount = iter.getRowCount();
        iter.beforeFirst();
        Object[] dataRetrieved = (Object[]) iter.fetchForward(rowCount);
        _RowReader rowReader = iter.getRowReader();
        Object[][] data = rowCount == 0 ? null : new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Object values = (Object[]) dataRetrieved[i];
            data[i][0] = rowReader.getObject(COLUMN_OFFSET + 0, values);
        }
        return getBufferForSQLTables(columnNames, columnCount, data);
    }

    private _RecordSetBuffer getTempBufferForAllSchemas(_SelectIterator iter) throws Exception {
        String[] columnNames = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS" };
        int columnCount = columnNames.length;
        int rowCount = iter.getRowCount();
        iter.beforeFirst();
        Object[] dataRetrieved = iter.fetchForward(rowCount);
        _RowReader rowReader = iter.getRowReader();
        Object[][] data = rowCount == 0 ? null : new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Object values = (Object[]) dataRetrieved[i];
            data[i][1] = rowReader.getObject(COLUMN_OFFSET + 0, values);
        }
        return getBufferForSQLTables(columnNames, columnCount, data);
    }

    private _RecordSetBuffer getTempBufferForAllTableTypes() throws Exception {
        String[] columnNames = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "TABLE_TYPE", "REMARKS" };
        int columnCount = columnNames.length;
        String[] allTableTypes = DatabaseProperties.getTableTypes();
        int rowCount = allTableTypes.length;
        Object[][] data = rowCount == 0 ? null : new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            data[i][3] = (String) allTableTypes[i];
            ;
        }
        return getBufferForSQLTables(columnNames, columnCount, data);
    }

    private TempRecordSetBuffer getBufferForSQLTables(String[] columnNames, int columnCount, Object[][] data) throws Exception {
        Object[][] metaData = new Object[columnCount][11];
        for (int i = 0; i < columnNames.length; i++) {
            metaData[i][0] = null;
            metaData[i][1] = null;
            metaData[i][2] = null;
            metaData[i][3] = columnNames[i];
            int type = getColumnTypeForTables(i + 1);
            metaData[i][4] = new Integer(type);
            metaData[i][5] = columnNames[i];
            int precision = getPrecision(type);
            metaData[i][6] = new Integer(precision);
            metaData[i][7] = new Integer(0);
            metaData[i][8] = Utilities.getBooleanValue(false);
            metaData[i][9] = new Integer(2);
            metaData[i][10] = type == Datatypes.VARCHAR ? metaData[i][6] : type == Datatypes.SMALLINT ? new Integer(Datatypes.SHORTSIZE) : new Integer(Datatypes.INTSIZE);
        }
        TempColumnCharacteristics cc = new TempColumnCharacteristics(metaData, columnNames);
        TempRecordSetBuffer rs = new TempRecordSetBuffer(data, cc);
        return rs;
    }

    private int getColumnTypeForTables(int index) throws Exception {
        switch(index) {
            case 1:
                return Datatypes.VARCHAR;
            case 2:
                return Datatypes.VARCHAR;
            case 3:
                return Datatypes.VARCHAR;
            case 4:
                return Datatypes.VARCHAR;
            case 5:
                return Datatypes.VARCHAR;
            default:
                DException dex = new DException("DSE504", null);
                throw dex;
        }
    }

    public Object getSQLColumnPrivileges(Object conObj, String catalog, String schema, String tableName, String columnName) throws Exception {
        _Connection con0 = (_Connection) conObj;
        String query = DatabaseProperties.getColumnPrivilegesQuery(catalog, schema, tableName, columnName);
        _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
        RecordSet rs = new RecordSet();
        rs.setSelectIterator(iter);
        return rs.getIterator();
    }

    public Object getSQLPrimaryKeys(Object conObj, String catalog, String schema, String tableName) throws Exception {
        _Connection con0 = (_Connection) conObj;
        String query = DatabaseProperties.getPrimaryKeysQuery(catalog, schema, tableName);
        _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
        RecordSet rs = new RecordSet();
        rs.setSelectIterator(iter);
        return rs.getIterator();
    }

    public Object getSQLForeignKeys(Object conObj, String pCatalog, String pSchema, String pTableName, String fCatalog, String fSchema, String fTableName) throws Exception {
        _Connection con0 = (_Connection) conObj;
        String query = DatabaseProperties.getCrossReferenceQuery(pCatalog, pSchema, pTableName, fCatalog, fSchema, fTableName);
        _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
        _RecordSetBuffer rs = getTempBufferForgetSQLForeignKeys(iter);
        return rs.getIterator();
    }

    private _RecordSetBuffer getTempBufferForgetSQLForeignKeys(_SelectIterator iter) throws Exception {
        String[] columnNames = new String[] { "PKTABLE_CAT", "PKTABLE_SCHEM", "PKTABLE_NAME", "PKCOLUMN_NAME", "FKTABLE_CAT", "FKTABLE_SCHEM", "FKTABLE_NAME", "FKCOLUMN_NAME", "KEY_SEQ", "UPDATE_RULE", "DELETE_RULE", "FK_NAME", "PK_NAME", "DEFERRABILITY" };
        int columnCount = columnNames.length;
        int rowCount = iter.getRowCount();
        iter.beforeFirst();
        Object[] dataRetrieved = (Object[]) iter.fetchForward(rowCount);
        _RowReader rowReader = iter.getRowReader();
        Object[][] data = rowCount == 0 ? null : new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Object values = dataRetrieved[i];
            Object[] objs = new Object[14];
            int j = 0;
            for (; j < 9; j++) objs[j] = rowReader.getObject(COLUMN_OFFSET + j, values);
            objs[j++] = new Short((short) getUpdateDeleteRule((String) rowReader.getObject(COLUMN_OFFSET + 9, values)));
            objs[j++] = new Short((short) getUpdateDeleteRule((String) rowReader.getObject(COLUMN_OFFSET + 10, values)));
            objs[j++] = rowReader.getObject(COLUMN_OFFSET + 11, values);
            objs[j++] = rowReader.getObject(COLUMN_OFFSET + 12, values);
            objs[j++] = new Short((short) getDeferrability((String) rowReader.getObject(COLUMN_OFFSET + 13, values), (String) rowReader.getObject(14, values)));
            data[i] = objs;
        }
        Object[][] metaData = new Object[columnCount][11];
        for (int i = 0; i < columnCount; i++) {
            metaData[i][0] = null;
            metaData[i][1] = null;
            metaData[i][2] = null;
            metaData[i][3] = columnNames[i];
            int type = getColumnTypeForCrossReference(i + 1);
            metaData[i][4] = new Integer(type);
            metaData[i][5] = columnNames[i];
            int precision = getPrecision(type);
            metaData[i][6] = new Integer(precision);
            metaData[i][7] = new Integer(0);
            metaData[i][8] = Utilities.getBooleanValue(false);
            metaData[i][9] = new Integer(2);
            metaData[i][10] = type == Datatypes.VARCHAR ? metaData[i][6] : type == Datatypes.SMALLINT ? new Integer(Datatypes.SHORTSIZE) : new Integer(Datatypes.INTSIZE);
        }
        TempColumnCharacteristics cc = new TempColumnCharacteristics(metaData, columnNames);
        TempRecordSetBuffer rs = new TempRecordSetBuffer(data, cc);
        return rs;
    }

    private int getColumnTypeForCrossReference(int index) throws Exception {
        switch(index) {
            case 1:
                return Datatypes.VARCHAR;
            case 2:
                return Datatypes.VARCHAR;
            case 3:
                return Datatypes.VARCHAR;
            case 4:
                return Datatypes.VARCHAR;
            case 5:
                return Datatypes.VARCHAR;
            case 6:
                return Datatypes.VARCHAR;
            case 7:
                return Datatypes.VARCHAR;
            case 8:
                return Datatypes.VARCHAR;
            case 9:
                return Datatypes.SMALLINT;
            case 10:
                return Datatypes.SMALLINT;
            case 11:
                return Datatypes.SMALLINT;
            case 12:
                return Datatypes.VARCHAR;
            case 13:
                return Datatypes.VARCHAR;
            case 14:
                return Datatypes.SMALLINT;
            default:
                DException dex = new DException("DSE504", null);
                throw dex;
        }
    }

    public Object getSQLProcedureColumns(Object conObj, String catalog, String schema, String procedureName, String columnName) throws Exception {
        _Connection con0 = (_Connection) conObj;
        String query = DatabaseProperties.getProcedureColumnsQuery(catalog, schema, procedureName, columnName);
        _SelectIterator iter = null;
        _RecordSetBuffer rs = getTempBufferForgetSQLProcedureColumns(iter);
        return rs.getIterator();
    }

    private _RecordSetBuffer getTempBufferForgetSQLProcedureColumns(_SelectIterator iter) throws Exception {
        String[] columnNames = new String[] { "PROCEDURE_CAT", "PROCEDURE_SCHEM", "PROCEDURE_NAME", "COLUMN_NAME", "COLUMN_TYPE", "DATA_TYPE", "TYPE_NAME", "PRECISION", "LENGTH", "SCALE", "RADIX", "NULLABLE", "REMARKS" };
        int columnCount = columnNames.length;
        Object[][] dataRetrieved = null;
        int rowCount = dataRetrieved == null ? 0 : dataRetrieved.length;
        Object[][] data = rowCount == 0 ? null : new Object[rowCount][columnCount];
        data = null;
        Object[][] metaData = new Object[columnCount][11];
        for (int i = 0; i < columnNames.length; i++) {
            metaData[i][0] = null;
            metaData[i][1] = null;
            metaData[i][2] = null;
            metaData[i][3] = columnNames[i];
            int type = getColumnTypeForProcedureColumns(i + 1);
            metaData[i][4] = new Integer(type);
            metaData[i][5] = columnNames[i];
            int precision = getPrecision(type);
            metaData[i][6] = new Integer(precision);
            metaData[i][7] = new Integer(0);
            metaData[i][8] = Utilities.getBooleanValue(false);
            metaData[i][9] = new Integer(2);
            metaData[i][10] = type == Datatypes.VARCHAR ? metaData[i][6] : type == Datatypes.SMALLINT ? new Integer(Datatypes.SHORTSIZE) : new Integer(Datatypes.INTSIZE);
        }
        TempColumnCharacteristics cc = new TempColumnCharacteristics(metaData, columnNames);
        TempRecordSetBuffer rs = new TempRecordSetBuffer(data, cc);
        return rs;
    }

    private int getColumnTypeForProcedureColumns(int index) throws Exception {
        switch(index) {
            case 1:
                return Datatypes.VARCHAR;
            case 2:
                return Datatypes.VARCHAR;
            case 3:
                return Datatypes.VARCHAR;
            case 4:
                return Datatypes.VARCHAR;
            case 5:
                return Datatypes.SMALLINT;
            case 6:
                return Datatypes.SMALLINT;
            case 7:
                return Datatypes.VARCHAR;
            case 8:
                return Datatypes.INTEGER;
            case 9:
                return Datatypes.INTEGER;
            case 10:
                return Datatypes.SMALLINT;
            case 11:
                return Datatypes.SMALLINT;
            case 12:
                return Datatypes.SMALLINT;
            case 13:
                return Datatypes.VARCHAR;
            default:
                DException dex = new DException("DSE504", null);
                throw dex;
        }
    }

    public Object getSQLColumns(Object conObj, String catalog, String schema, String tableName, String columnName) throws Exception {
        _Connection con0 = (_Connection) conObj;
        String query = DatabaseProperties.getColumnsQuery(catalog, schema, tableName, columnName);
        _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
        _RecordSetBuffer rs = getTempBufferForgetSQLColumns(iter);
        return rs.getIterator();
    }

    private _RecordSetBuffer getTempBufferForgetSQLColumns(_SelectIterator iter) throws Exception {
        String[] columnNames = { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "COLUMN_NAME", "DATA_TYPE", "TYPE_NAME", "COLUMN_SIZE", "BUFFER_LENGTH", "DECIMAL_DIGITS", "NUM_PREC_RADIX", "NULLABLE", "REMARKS", "COLUMN_DEF", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "CHAR_OCTET_LENGTH", "ORDINAL_POSITION", "IS_NULLABLE" };
        int columnCount = columnNames.length;
        int rowCount = iter.getRowCount();
        iter.beforeFirst();
        Object[] dataRetrieved = (Object[]) iter.fetchForward(rowCount);
        _RowReader rowReader = iter.getRowReader();
        Object[][] data = rowCount == 0 ? null : new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Object[] objs = new Object[columnNames.length];
            Object values = dataRetrieved[i];
            int j = 0;
            for (; j < 4; j++) objs[j] = rowReader.getObject(COLUMN_OFFSET + j, values);
            objs[5] = rowReader.getObject(COLUMN_OFFSET + 4, values);
            objs[4] = new Short((short) Utilities.getCorrespondingSqlTypeOfDatabaseType(Utilities.getDataBaseType((String) objs[5])));
            Object characterLengthObj = rowReader.getObject(COLUMN_OFFSET + 8, values);
            int characterLength = characterLengthObj == null ? 0 : characterLengthObj.hashCode();
            Integer i12 = (Integer) rowReader.getObject(COLUMN_OFFSET + 12, values);
            Integer i13 = (Integer) rowReader.getObject(COLUMN_OFFSET + 13, values);
            Integer i14 = (Integer) rowReader.getObject(COLUMN_OFFSET + 14, values);
            objs[6] = new Integer(getColumnSize(characterLength, i12 == null ? 0 : i12.intValue(), i13 == null ? 0 : i13.intValue(), i14 == null ? 0 : i14.intValue()));
            objs[7] = null;
            objs[8] = rowReader.getObject(COLUMN_OFFSET + 5, values);
            objs[9] = rowReader.getObject(COLUMN_OFFSET + 6, values);
            objs[12] = rowReader.getObject(COLUMN_OFFSET + 7, values);
            objs[13] = new Integer(((Short) objs[4]).intValue());
            objs[14] = new Integer(0);
            objs[15] = rowReader.getObject(COLUMN_OFFSET + 9, values);
            objs[11] = "null";
            objs[16] = rowReader.getObject(COLUMN_OFFSET + 10, values);
            objs[17] = rowReader.getObject(COLUMN_OFFSET + 11, values);
            objs[10] = new Integer(getNullablility((String) objs[17]));
            data[i] = objs;
        }
        Object[][] metaData = new Object[columnCount][11];
        for (int i = 0; i < columnNames.length; i++) {
            metaData[i][0] = null;
            metaData[i][1] = null;
            metaData[i][2] = null;
            metaData[i][3] = columnNames[i];
            int type = getColumnTypeForColumns(i + 1);
            metaData[i][4] = new Integer(type);
            metaData[i][5] = columnNames[i];
            int precision = getPrecision(type);
            metaData[i][6] = new Integer(precision);
            metaData[i][7] = new Integer(0);
            metaData[i][8] = Utilities.getBooleanValue(false);
            metaData[i][9] = new Integer(2);
            metaData[i][10] = type == Datatypes.VARCHAR ? metaData[i][6] : type == Datatypes.SMALLINT ? new Integer(Datatypes.SHORTSIZE) : new Integer(Datatypes.INTSIZE);
        }
        TempColumnCharacteristics cc = new TempColumnCharacteristics(metaData, columnNames);
        TempRecordSetBuffer rs = new TempRecordSetBuffer(data, cc);
        return rs;
    }

    private int getColumnTypeForColumns(int index) throws Exception {
        switch(index) {
            case 1:
                return Datatypes.VARCHAR;
            case 2:
                return Datatypes.VARCHAR;
            case 3:
                return Datatypes.VARCHAR;
            case 4:
                return Datatypes.VARCHAR;
            case 5:
                return Datatypes.SMALLINT;
            case 6:
                return Datatypes.VARCHAR;
            case 7:
                return Datatypes.INTEGER;
            case 8:
                return Datatypes.INTEGER;
            case 9:
                return Datatypes.INTEGER;
            case 10:
                return Datatypes.INTEGER;
            case 11:
                return Datatypes.INTEGER;
            case 12:
                return Datatypes.VARCHAR;
            case 13:
                return Datatypes.VARCHAR;
            case 14:
                return Datatypes.INTEGER;
            case 15:
                return Datatypes.INTEGER;
            case 16:
                return Datatypes.INTEGER;
            case 17:
                return Datatypes.INTEGER;
            case 18:
                return Datatypes.VARCHAR;
            default:
                DException dex = new DException("DSE504", null);
                throw dex;
        }
    }

    private int getColumnSize(int charLength, int numPrecision, int datePrecision, int intervalPrecision) {
        return charLength != 0 ? charLength : numPrecision != 0 ? numPrecision : datePrecision != 0 ? datePrecision : intervalPrecision;
    }

    private int getNullablility(String nullable) {
        if (nullable.equalsIgnoreCase("yes")) return 1;
        if (nullable.equalsIgnoreCase("no")) return 0;
        return 2;
    }

    private int getDeferrability(String deferrability, String initiallyDeferrable) {
        if (deferrability.equalsIgnoreCase("no")) return 7;
        return initiallyDeferrable.equalsIgnoreCase("yes") ? 6 : 5;
    }

    private int getUpdateDeleteRule(String OnUpdateDelete) {
        if (OnUpdateDelete.equalsIgnoreCase("no action")) return 3;
        if (OnUpdateDelete.equalsIgnoreCase("cascade")) return 0;
        if (OnUpdateDelete.equalsIgnoreCase("set null")) return 2;
        if (OnUpdateDelete.equalsIgnoreCase("restrict")) return 1;
        return 4;
    }

    public Object getSQLStatics(Object conObj, String catalog, String schema, String tableName, boolean bol1, boolean bol2) throws Exception {
        _Connection con0 = (_Connection) conObj;
        String query = DatabaseProperties.getIndexInfoQuery(catalog, schema, tableName, bol1, bol2);
        _SelectIterator iter = (_SelectIterator) con0.getSystemConnection().executeQuery(query, 0, 0);
        _RecordSetBuffer rs = getTempBufferForSQLStatics(iter);
        return rs.getIterator();
    }

    private _RecordSetBuffer getTempBufferForSQLStatics(_SelectIterator iter) throws Exception {
        String[] columnNames = new String[] { "TABLE_CAT", "TABLE_SCHEM", "TABLE_NAME", "NON_UNIQUE", "INDEX_QUALIFIER", "INDEX_NAME", "TYPE", "ORDINAL_POSITION", "COLUMN_NAME", "ASC_OR_DESC", "CARDINALITY", "PAGES", "FILTER_CONDITION" };
        int columnCount = columnNames.length;
        int rowCount = iter.getRowCount();
        iter.beforeFirst();
        Object[] dataRetrieved = (Object[]) iter.fetchForward(rowCount);
        _RowReader rowReader = iter.getRowReader();
        Object[][] data = rowCount == 0 ? null : new Object[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            Object values = dataRetrieved[i];
            Object[] objs1 = new Object[13];
            int j = 0;
            for (; j < 3; j++) objs1[j] = rowReader.getObject(COLUMN_OFFSET + j, values);
            objs1[j++] = new Short("1");
            objs1[j++] = null;
            objs1[j++] = rowReader.getObject(COLUMN_OFFSET + 3, values);
            objs1[j++] = new Short("3");
            objs1[j++] = new Short(((Integer) rowReader.getObject(COLUMN_OFFSET + 4, values)).shortValue());
            objs1[j++] = rowReader.getObject(COLUMN_OFFSET + 5, values);
            objs1[j++] = rowReader.getObject(COLUMN_OFFSET + 6, values).equals(Boolean.TRUE) ? "A" : "D";
            objs1[j++] = null;
            objs1[j++] = null;
            objs1[j++] = null;
            data[i] = objs1;
        }
        Object[][] metaData = new Object[columnCount][11];
        for (int i = 0; i < columnCount; i++) {
            metaData[i][0] = null;
            metaData[i][1] = null;
            metaData[i][2] = null;
            metaData[i][3] = columnNames[i];
            int type = getColumnTypeForIndexInfo(i + 1);
            metaData[i][4] = new Integer(type);
            metaData[i][5] = columnNames[i];
            int precision = getPrecision(type);
            metaData[i][6] = new Integer(precision);
            metaData[i][7] = new Integer(0);
            metaData[i][8] = Utilities.getBooleanValue(false);
            metaData[i][9] = new Integer(2);
            metaData[i][10] = type == Datatypes.VARCHAR ? metaData[i][6] : type == Datatypes.SMALLINT ? new Integer(Datatypes.SHORTSIZE) : new Integer(Datatypes.INTSIZE);
        }
        TempColumnCharacteristics cc = new TempColumnCharacteristics(metaData, columnNames);
        TempRecordSetBuffer rs = new TempRecordSetBuffer(data, cc);
        return rs;
    }

    private int getColumnTypeForIndexInfo(int index) throws DException {
        switch(index) {
            case 1:
                return Datatypes.VARCHAR;
            case 2:
                return Datatypes.VARCHAR;
            case 3:
                return Datatypes.VARCHAR;
            case 4:
                return Datatypes.SMALLINT;
            case 5:
                return Datatypes.VARCHAR;
            case 6:
                return Datatypes.VARCHAR;
            case 7:
                return Datatypes.SMALLINT;
            case 8:
                return Datatypes.SMALLINT;
            case 9:
                return Datatypes.VARCHAR;
            case 10:
                return Datatypes.VARCHAR;
            case 11:
                return Datatypes.INTEGER;
            case 12:
                return Datatypes.INTEGER;
            case 13:
                return Datatypes.VARCHAR;
            default:
                DException dex = new DException("DSE504", null);
                throw dex;
        }
    }

    public Object getSQLTypeInfo(int datatype) throws Exception {
        try {
            String[] columnNames = new String[] { "TYPE_NAME", "DATA_TYPE", "COLUMN_SIZE", "LITERAL_PREFIX", "LITERAL_SUFFIX", "CREATE_PARAMS", "NULLABLE", "CASE_SENSITIVE", "SEARCHABLE", "UNSIGNED_ATTRIBUTE", "FIXED_PREC_SCALE", "AUTO_UNIQUE_VALUE", "LOCAL_TYPE_NAME", "MINIMUM_SCALE", "MAXIMUM_SCALE", "SQL_DATA_TYPE", "SQL_DATETIME_SUB", "NUM_PREC_RADIX", "INTERVAL_PRECISION" };
            Object[][] typeInfo = getTypeInfoData(datatype);
            int columnCount = columnNames.length;
            Object[][] data = new Object[typeInfo.length][columnCount];
            for (int i = 0, size = typeInfo.length; i < size; i++) {
                Object[] currentTypeInfo = new Object[typeInfo[i].length + 2];
                System.arraycopy(typeInfo[i], 0, currentTypeInfo, 0, typeInfo[i].length);
                currentTypeInfo[columnCount - 1] = new Short("0");
                data[i] = currentTypeInfo;
            }
            Object[][] metaData = new Object[columnCount][11];
            for (int i = 0; i < columnCount; i++) {
                metaData[i][0] = null;
                metaData[i][1] = null;
                metaData[i][2] = null;
                metaData[i][3] = columnNames[i];
                int type = getColumnTypeForTypeInfo(i + 1);
                metaData[i][4] = new Integer(type);
                metaData[i][5] = columnNames[i];
                int precision = getPrecision(type);
                metaData[i][6] = new Integer(precision);
                metaData[i][7] = new Integer(0);
                metaData[i][8] = Utilities.getBooleanValue(false);
                metaData[i][9] = new Integer(2);
                metaData[i][10] = type == Datatypes.VARCHAR ? metaData[i][6] : type == Datatypes.SMALLINT ? new Integer(Datatypes.SHORTSIZE) : new Integer(Datatypes.INTSIZE);
            }
            TempColumnCharacteristics cc = new TempColumnCharacteristics(metaData, columnNames);
            TempRecordSetBuffer rs = new TempRecordSetBuffer(data, cc);
            return rs.getIterator();
        } catch (DException dse) {
            throw dse;
        }
    }

    private int getColumnTypeForTypeInfo(int index) throws DException {
        switch(index) {
            case 1:
                return Datatypes.VARCHAR;
            case 2:
                return Datatypes.SMALLINT;
            case 3:
                return Datatypes.INTEGER;
            case 4:
                return Datatypes.VARCHAR;
            case 5:
                return Datatypes.VARCHAR;
            case 6:
                return Datatypes.VARCHAR;
            case 7:
                return Datatypes.SMALLINT;
            case 8:
                return Datatypes.SMALLINT;
            case 9:
                return Datatypes.SMALLINT;
            case 10:
                return Datatypes.SMALLINT;
            case 11:
                return Datatypes.SMALLINT;
            case 12:
                return Datatypes.SMALLINT;
            case 13:
                return Datatypes.VARCHAR;
            case 14:
                return Datatypes.SMALLINT;
            case 15:
                return Datatypes.SMALLINT;
            case 16:
                return Datatypes.SMALLINT;
            case 17:
                return Datatypes.SMALLINT;
            case 18:
                return Datatypes.INTEGER;
            case 19:
                return Datatypes.SMALLINT;
            default:
                DException dex = new DException("DSE504", null);
                throw dex;
        }
    }

    private static Object[][] getTypeInfoData(int dataType) {
        switch(dataType) {
            case Types.CHAR:
                return new Object[][] { { "character", new Short("" + Types.CHAR), new Integer(4192), "\'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.CHAR), null, null }, { "char", new Short("" + Types.CHAR), new Integer(4192), "\'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.CHAR), null, null } };
            case Types.VARCHAR:
                return new Object[][] { { "character varying", new Short("" + Types.VARCHAR), new Integer(4192), "\'", "\'", "max length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARCHAR), null, null }, { "char varying", new Short("" + Types.VARCHAR), new Integer(4192), "\'", "\'", "max length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARCHAR), null, null }, { "varchar", new Short("" + Types.VARCHAR), new Integer(4192), "\'", "\'", "max length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARCHAR), null, null } };
            case Types.CLOB:
                return new Object[][] { { "character large object", new Short("" + Types.CLOB), new Integer(1073741823), "\'", "\'", "max length", new Short("1"), new Short("1"), new Short("0"), null, new Short("0"), null, null, null, null, new Integer("" + Types.CLOB), null, null }, { "char large object", new Short("" + Types.CLOB), new Integer(1073741823), "\'", "\'", "max length", new Short("1"), new Short("1"), new Short("0"), null, new Short("0"), null, null, null, null, new Integer("" + Types.CLOB), null, null }, { "clob", new Short("" + Types.CLOB), new Integer(1073741823), "\'", "\'", "max length", new Short("1"), new Short("1"), new Short("0"), null, new Short("0"), null, null, null, null, new Integer("" + Types.CLOB), null, null } };
            case Types.BLOB:
                return new Object[][] { { "binary large object", new Short("" + Types.BLOB), new Integer(1073741823), "X'", "\'", "max length", new Short("1"), new Short("1"), new Short("0"), null, new Short("0"), null, null, null, null, new Integer("" + Types.BLOB), null, null }, { "blob", new Short("" + Types.BLOB), new Integer(1073741823), "X'", "\'", "max length", new Short("1"), new Short("1"), new Short("0"), null, new Short("0"), null, null, null, null, new Integer("" + Types.BLOB), null, null } };
            case Types.BINARY:
                return new Object[][] { { "bit", new Short("" + Types.BINARY), new Integer(4192), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.BINARY), null, null }, { "binary", new Short("" + Types.BINARY), new Integer(4192), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.BINARY), null, null } };
            case Types.VARBINARY:
                return new Object[][] { { "bit varying", new Short("" + Types.VARBINARY), new Integer(4192), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARBINARY), null, null }, { "varbinary", new Short("" + Types.VARBINARY), new Integer(4192), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARBINARY), null, null } };
            case Types.NUMERIC:
                return new Object[][] { { "numeric", new Short("" + Types.NUMERIC), new Integer(28), null, null, "precision,scale", new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("28"), new Integer("" + Types.NUMERIC), null, new Integer(10) } };
            case Types.DECIMAL:
                return new Object[][] { { "decimal", new Short("" + Types.DECIMAL), new Integer(28), null, null, "precision,scale", new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("28"), new Integer("" + Types.DECIMAL), null, new Integer(10) }, { "dec", new Short("" + Types.DECIMAL), new Integer(28), null, null, "precision,scale", new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("28"), new Integer("" + Types.DECIMAL), null, new Integer(10) } };
            case Types.INTEGER:
                return new Object[][] { { "int", new Short("" + Types.INTEGER), new Integer(10), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.INTEGER), null, new Integer(10) }, { "integer", new Short("" + Types.INTEGER), new Integer(10), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.INTEGER), null, new Integer(10) } };
            case Types.BIGINT:
                return new Object[][] { { "long", new Short("" + Types.BIGINT), new Integer(19), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.BIGINT), null, new Integer(10) }, { "bigint", new Short("" + Types.BIGINT), new Integer(19), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.BIGINT), null, new Integer(10) } };
            case Types.SMALLINT:
                return new Object[][] { { "smallint", new Short("" + Types.SMALLINT), new Integer(5), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.SMALLINT), null, new Integer(10) } };
            case Types.FLOAT:
                return new Object[][] { { "float", new Short("" + Types.FLOAT), new Integer(15), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.FLOAT), null, new Integer(10) } };
            case Types.REAL:
                return new Object[][] { { "real", new Short("" + Types.REAL), new Integer(7), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.REAL), null, new Integer(10) } };
            case Types.DOUBLE:
                return new Object[][] { { "double precision", new Short("" + Types.DOUBLE), new Integer(15), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.DOUBLE), null, new Integer(10) } };
            case Types.BIT:
                return new Object[][] { { "boolean", new Short("" + Types.BIT), new Integer(1), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.BIT), null, null } };
            case Types.DATE:
                return new Object[][] { { "date", new Short("" + Types.DATE), new Integer(10), "DATE '", "\'", null, new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, new Short("0"), new Short("0"), new Integer(91), new Integer("" + Types.DATE), null } };
            case Types.TIME:
                return new Object[][] { { "time", new Short("" + Types.TIME), new Integer(9), "TIME '", "\'", "timeprecision", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, new Short("0"), new Short("0"), new Integer(92), new Integer("" + Types.TIME), null } };
            case Types.TIMESTAMP:
                return new Object[][] { { "timeStamp", new Short("" + Types.TIMESTAMP), new Integer(9), "TIMESTAMP '", "\'", "timestampprecision", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, new Short("0"), new Short("0"), new Integer(93), new Integer("" + Types.TIMESTAMP), null } };
            case Types.LONGVARBINARY:
                return new Object[][] { { "long varbinary", new Short("" + Types.LONGVARBINARY), new Integer(1073741823), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.LONGVARBINARY), null, null } };
            case Types.JAVA_OBJECT:
                return new Object[][] { { "long varbinary", new Short("" + Types.LONGVARBINARY), new Integer(1073741823), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.LONGVARBINARY), null, null } };
            case Types.OTHER:
                return new Object[][] { { "long varbinary", new Short("" + Types.LONGVARBINARY), new Integer(1073741823), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.LONGVARBINARY), null, null } };
            case Types.LONGVARCHAR:
                return new Object[][] { { "long varchar", new Short("" + Types.LONGVARCHAR), new Integer(1073741823), "\'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.LONGVARCHAR), null, null } };
            case Types.TINYINT:
                return new Object[][] { { "tinyint", new Short("" + Types.TINYINT), new Integer(5), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.TINYINT), null, new Integer(10) }, { "byte", new Short("" + Types.TINYINT), new Integer(5), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.TINYINT), null, new Integer(10) } };
            case Types.NULL:
                return getTypeInfoData();
        }
        return null;
    }

    private static Object[][] getTypeInfoData() {
        return new Object[][] { { "CHARACTER", new Short("" + Types.CHAR), new Integer(4192), "\'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.CHAR), null, null }, { "CHAR", new Short("" + Types.CHAR), new Integer(4192), "\'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.CHAR), null, null }, { "CHARACTER VARYING", new Short("" + Types.VARCHAR), new Integer(4192), "\'", "\'", "max length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARCHAR), null, null }, { "CHAR VARYING", new Short("" + Types.VARCHAR), new Integer(4192), "\'", "\'", "max length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARCHAR), null, null }, { "VARCHAR", new Short("" + Types.VARCHAR), new Integer(4192), "\'", "\'", "max length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARCHAR), null, null }, { "BIT", new Short("" + Types.BINARY), new Integer(4192), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.BINARY), null, null }, { "BINARY", new Short("" + Types.BINARY), new Integer(4192), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.BINARY), null, null }, { "BIT VARYING", new Short("" + Types.VARBINARY), new Integer(4192), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARBINARY), null, null }, { "VARBINARY", new Short("" + Types.VARBINARY), new Integer(4192), "B'| X'", "\'", "length", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, null, null, new Integer("" + Types.VARBINARY), null, null }, { "NUMERIC", new Short("" + Types.NUMERIC), new Integer(28), null, null, "precision,scale", new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("28"), new Integer("" + Types.NUMERIC), null, new Integer(10) }, { "DECIMAL", new Short("" + Types.DECIMAL), new Integer(28), null, null, "precision,scale", new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("28"), new Integer("" + Types.DECIMAL), null, new Integer(10) }, { "DEC", new Short("" + Types.DECIMAL), new Integer(28), null, null, "precision,scale", new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("28"), new Integer("" + Types.DECIMAL), null, new Integer(10) }, { "INT", new Short("" + Types.INTEGER), new Integer(10), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.INTEGER), null, new Integer(10) }, { "INTEGER", new Short("" + Types.INTEGER), new Integer(10), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.INTEGER), null, new Integer(10) }, { "LONG", new Short("" + Types.BIGINT), new Integer(19), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.BIGINT), null, new Integer(10) }, { "BIGINT", new Short("" + Types.BIGINT), new Integer(19), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.BIGINT), null, new Integer(10) }, { "SMALLINT", new Short("" + Types.SMALLINT), new Integer(5), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.SMALLINT), null, new Integer(10) }, { "FLOAT", new Short("" + Types.FLOAT), new Integer(15), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.FLOAT), null, new Integer(10) }, { "REAL", new Short("" + Types.REAL), new Integer(7), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.REAL), null, new Integer(10) }, { "DOUBLE PRECISION", new Short("" + Types.DOUBLE), new Integer(15), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.DOUBLE), null, new Integer(10) }, { "BOOLEAN", new Short("" + Types.BIT), new Integer(0), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.BIT), null, null }, { "DATE", new Short("" + Types.DATE), new Integer(10), "DATE '", "\'", null, new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, new Short("0"), new Short("0"), new Integer(91), new Integer("" + Types.DATE), null }, { "TIME", new Short("" + Types.TIME), new Integer(9), "TIME '", "\'", "timeprecision", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, new Short("0"), new Short("0"), new Integer(92), new Integer("" + Types.TIME), null }, { "TIMESTAMP", new Short("" + Types.TIMESTAMP), new Integer(9), "TIMESTAMP '", "\'", "timestampprecision", new Short("1"), new Short("0"), new Short("3"), null, new Short("0"), null, null, new Short("0"), new Short("0"), new Integer(93), new Integer("" + Types.TIMESTAMP), null }, { "TINYINT", new Short("" + Types.TINYINT), new Integer(3), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.TINYINT), null, new Integer(10) }, { "BYTE", new Short("" + Types.TINYINT), new Integer(3), null, null, null, new Short("1"), new Short("0"), new Short("2"), new Short("0"), new Short("0"), new Short("0"), null, new Short("0"), new Short("0"), new Integer("" + Types.TINYINT), null, new Integer(10) } };
    }

    public static int getPrecision(int type) throws DException {
        switch(type) {
            case Datatypes.SMALLINT:
                return Datatypes.SHORT_PRECISION;
            case Datatypes.INTEGER:
                return Datatypes.INT_PRECISION;
            case Datatypes.VARCHAR:
                return 1024;
        }
        return 0;
    }
}
