package org.visu.postgres;

import java.sql.*;
import java.util.*;
import org.visu.VPG;
import org.visu.postgres.types.*;
import org.visu.postgres.exceptions.*;
import org.visu.ui.media.*;

/**
 * @author Vladimir Stanciu
 * @version 1.0
 */
public class Table {

    private String name;

    private String remark = "";

    private Schema schema;

    private PrimaryKey primaryKey;

    private List columns;

    /**
     * Table constructor.
     * @param schema Schema
     * @param name String
     */
    public Table(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    /**
     * Finds a table using the supplied information.
     * @param dd DatabaseDescriptor
     * @param schemaName String
     * @param tableName String
     * @throws SQLException
     * @return Table
     */
    public static Table getTable(DatabaseDescriptor dd, String schemaName, String tableName) throws SQLException {
        Table table = null;
        Connection con = dd.getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet trs = meta.getTables(null, schemaName, tableName, new String[] { "TABLE" });
        while (trs.next()) {
            String tblName = trs.getString(3);
            String tblRemarks = trs.getString(5);
            Table tbl = new Table(new Schema(dd, schemaName), tblName);
            tbl.setRemark(tblRemarks);
            table = tbl;
            break;
        }
        trs.close();
        con.close();
        return table;
    }

    public DatabaseDescriptor getDatabaseDescriptor() {
        return getSchema().getDatabase();
    }

    /**
     * Invalidates the cache for this table so the next
     * call to any method is guaranteed to return fresh data.
     * @throws TableNotFoundException
     */
    public void invalidateCache() throws TableNotFoundException {
        Table ts = null;
        try {
            ts = getSchema().getTable(getName());
        } catch (Exception ex) {
            VPG.logError(ex);
        }
        if (ts == null) {
            SQLException sqlex = new SQLException("Table " + getName() + " not found.");
            throw new TableNotFoundException(sqlex);
        }
        this.name = ts.getName();
        this.remark = ts.getRemark();
        this.columns = null;
        this.primaryKey = null;
    }

    /**
     * Adds a column for this table using the supplied information.
     * @param name String
     * @param dataType DataType
     * @param size int
     * @param digits int
     * @param def String
     * @param allowNulls boolean
     * @throws SQLException
     * @return Column
     * @see #createColumn
     */
    public Column addColumn(String name, DataType dataType, int size, int digits, String def, boolean allowNulls) throws SQLException {
        Column c = new Column(this);
        c.setName(name);
        c.setDataType(dataType);
        c.setSize(size);
        c.setDigits(digits);
        c.setDef(def);
        c.setAllowNulls(allowNulls);
        createColumn(c);
        columns = null;
        return c;
    }

    /**
     * Creates the column for this table.
     * @param c Column
     * @throws SQLException
     */
    private void createColumn(Column c) throws SQLException {
        String sql1 = "ALTER TABLE \"" + getSchema().getSchemaName() + "\".\"";
        sql1 += getName() + "\" ADD COLUMN ";
        sql1 += "\"" + c.getName() + "\" " + c.getTypeName();
        if (c.getDataType().supportsSize()) {
            sql1 += "(" + c.getSize();
            if (c.getDataType().supportsDigits()) {
                sql1 += ", " + c.getDigits();
            }
            sql1 += ")";
        }
        sql1 += ";";
        String sql2 = "";
        if (c.getDef().length() > 0) {
            sql2 += "ALTER TABLE \"" + getSchema().getSchemaName() + "\".\"" + getName() + "\" ALTER COLUMN ";
            sql2 += "\"" + c.getName() + "\" SET DEFAULT " + c.getDef() + ";";
        }
        String sql3 = "";
        if (!c.isAllowNulls()) {
            sql3 += "ALTER TABLE \"" + getSchema().getSchemaName() + "\".\"" + getName() + "\" ALTER COLUMN ";
            sql3 += "\"" + c.getName() + "\" SET NOT NULL;";
        }
        VPG.logInfo("Table " + getName() + " recieved command to create a column " + "and will try to execute the following statements:");
        VPG.logInfo(sql1);
        VPG.logInfo(sql2);
        VPG.logInfo(sql3);
        Connection con = getSchema().getDatabase().getConnection();
        try {
            con.setAutoCommit(false);
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.execute();
            ps1.close();
            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.execute();
            ps2.close();
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.execute();
            ps3.close();
            con.commit();
        } catch (SQLException ex) {
            con.rollback();
            VPG.logError("Execution failed and rolled back.", ex);
            throw ex;
        }
        con.close();
    }

    /**
     * Drops a table.
     * @param cascade boolean
     * @throws SQLException
     */
    public void dropTable(boolean cascade) throws SQLException {
        String sql = "DROP TABLE ";
        sql += "\"" + getSchema().getSchemaName() + "\".\"";
        sql += getName() + "\" ";
        if (cascade) {
            sql += " CASCADE";
        }
        VPG.logInfo("Table " + getName() + " recieved command to be dropped " + "and will try to execute the following statements:");
        VPG.logInfo(sql);
        Connection con = getDatabaseDescriptor().getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.execute();
        ps.close();
        con.close();
    }

    /**
     * Drops the supplied column.
     * @param c Column
     * @throws SQLException
     */
    public void dropColumn(Column c) throws SQLException {
        String sql = "ALTER TABLE \"" + getSchema().getSchemaName() + "\".\"";
        sql += getName() + "\" DROP COLUMN ";
        sql += "\"" + c.getName() + "\";";
        VPG.logInfo("Table " + getName() + " recieved command to drop a column " + "and will try to execute the following statements:");
        VPG.logInfo(sql);
        Connection con = getSchema().getDatabase().getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.execute();
        ps.close();
        con.close();
    }

    /**
     * Creates a Primary key for this table. If a PK already
     * exists, then it is first dropped.
     * @param cols Column[]
     * @param pkName String
     * @throws SQLException
     */
    public void createPrimaryKey(Column[] cols, String pkName) throws SQLException {
        String colStr = "";
        int clen = cols.length;
        for (int i = 0; i < clen; i++) {
            cols[i].addConstraintNotNull();
            colStr += cols[i].getName();
            if (i < clen - 1) {
                colStr += ", ";
            }
        }
        dropPrimaryKey();
        if (cols.length > 0) {
            String sql = "ALTER TABLE \"" + getSchema().getSchemaName() + "\".\"";
            sql += getName() + "\" ADD CONSTRAINT \"" + pkName + "\" PRIMARY KEY ";
            sql += "(" + colStr + ");";
            VPG.logInfo("Table " + getName() + " recieved command to add a primary key " + "and will try to execute the following statements:");
            VPG.logInfo(sql);
            Connection con = getSchema().getDatabase().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            ps.close();
            con.close();
        }
    }

    /**
     * Drops the Primary key for this table.
     * @throws SQLException
     */
    public void dropPrimaryKey() throws SQLException {
        PrimaryKey pk = getFreshPrimaryKey();
        if (pk != null) {
            String sql = "ALTER TABLE \"" + getSchema().getSchemaName() + "\".\"";
            sql += getName() + "\" DROP CONSTRAINT \"" + pk.getName() + "\" ";
            VPG.logInfo("Table " + getName() + " recieved command to drop the primary key " + "and will try to execute the following statements:");
            VPG.logInfo(sql);
            Connection con = getSchema().getDatabase().getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.execute();
            ps.close();
            con.close();
        }
    }

    /**
     * Determines the foreign keys for this table.
     * @throws SQLException
     * @return ForeignKey[]
     */
    public ForeignKey[] getForeignKeys() throws SQLException {
        HashMap tables = new HashMap();
        LinkedList list = new LinkedList();
        Connection con = getSchema().getDatabase().getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet rs = meta.getImportedKeys(null, getSchema().getSchemaName(), getName());
        while (rs.next()) {
            String pkSchemaName = rs.getString(2);
            String pkTableName = rs.getString(3);
            String pkColumnName = rs.getString(4);
            String fkColumnName = rs.getString(8);
            short updateRule = rs.getShort(10);
            short deleteRule = rs.getShort(11);
            String fkName = rs.getString(12);
            String pkName = rs.getString(13);
            short deferrability = rs.getShort(14);
            String fkCleanName = "";
            StringTokenizer stk = new StringTokenizer(fkName, "\\000");
            while (stk.hasMoreTokens()) {
                fkCleanName = stk.nextToken();
                break;
            }
            String schTblDescriptor = pkSchemaName + pkTableName + fkCleanName;
            ForeignKey existingFk = (ForeignKey) tables.get(schTblDescriptor);
            Table pkTable = getTable(getDatabaseDescriptor(), pkSchemaName, pkTableName);
            if (pkTable == null) {
                VPG.logError("ForeignKey had a primary key table that was not found." + "PKTableName: " + pkTableName);
                continue;
            }
            if (tables.containsKey(schTblDescriptor)) {
                existingFk.addRelation(this.getColumn(fkColumnName), pkTable.getColumn(pkColumnName));
            } else {
                ForeignKey fk = new ForeignKey(this, fkCleanName);
                fk.setDeferrable(Deferrable.getDeferrability(deferrability));
                fk.setUpdateRule(Rules.getRule(updateRule));
                fk.setDeleteRule(Rules.getRule(deleteRule));
                fk.setPkTable(pkTable);
                fk.addRelation(this.getColumn(fkColumnName), pkTable.getColumn(pkColumnName));
                list.add(fk);
                tables.put(schTblDescriptor, fk);
            }
        }
        rs.close();
        con.close();
        return (ForeignKey[]) (list.toArray(new ForeignKey[list.size()]));
    }

    /**
     * Creates a foreign key with the supplied values where the
     * the foreign key table is this table.
     * @param name String
     * @param fkColumns Column[]
     * @param pkTable Table
     * @param pkColumns Column[]
     * @param onUpdate String
     * @param onDelete String
     * @param deferrable String
     * @throws SQLException
     */
    public void createForeignKey(String name, Column[] fkColumns, Table pkTable, Column[] pkColumns, String onUpdate, String onDelete, String deferrable) throws SQLException {
        String sql = createForeignKeyStatement(name, fkColumns, pkTable, pkColumns, onUpdate, onDelete, deferrable);
        VPG.logInfo("TABLE " + getName() + " recieved a command to add a foreign key " + "and will try to execute the following statement: ");
        VPG.logInfo(sql);
        Connection con = getDatabaseDescriptor().getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.execute();
        ps.close();
        con.close();
    }

    private String createForeignKeyStatement(String name, Column[] fkColumns, Table pkTable, Column[] pkColumns, String onUpdate, String onDelete, String deferrable) {
        String sql = "ALTER TABLE ";
        sql += "\"" + getSchema().getSchemaName() + "\".\"" + getName() + "\" ";
        sql += "ADD CONSTRAINT \"" + name + "\" FOREIGN KEY (";
        for (int i = 0; i < fkColumns.length; i++) {
            sql += (i > 0 ? ", " : "");
            sql += "\"" + fkColumns[i].getName() + "\"";
        }
        sql += ") \n";
        sql += "REFERENCES " + "\"" + pkTable.getSchema().getSchemaName() + "\".\"";
        sql += pkTable.getName() + "\"(";
        for (int i = 0; i < pkColumns.length; i++) {
            sql += (i > 0 ? ", " : "");
            sql += "\"" + pkColumns[i].getName() + "\"";
        }
        sql += ") \n";
        sql += "ON UPDATE " + onUpdate + " \n";
        sql += "ON DELETE " + onDelete + " \n";
        sql += deferrable;
        return sql;
    }

    public void updateForeignKey(ForeignKey fk, String name, Column[] fkColumns, Table pkTable, Column[] pkColumns, String onUpdate, String onDelete, String deferrable) throws SQLException {
        Connection con = getDatabaseDescriptor().getConnection();
        con.setAutoCommit(false);
        String dropSql = dropForeignKeyStatement(fk);
        String createSql = createForeignKeyStatement(name, fkColumns, pkTable, pkColumns, onUpdate, onDelete, deferrable);
        try {
            PreparedStatement dropPs = con.prepareStatement(dropSql);
            dropPs.execute();
            dropPs.close();
            PreparedStatement createPs = con.prepareStatement(createSql);
            createPs.execute();
            createPs.close();
            con.commit();
            con.close();
        } catch (SQLException ex) {
            con.rollback();
            con.close();
            throw ex;
        }
    }

    /**
     * Drops the supplied foreign key.
     * @param fk ForeignKey FK to drop.
     * @throws SQLException Server exception.
     */
    public void dropForeignKey(ForeignKey fk) throws SQLException {
        String sql = dropForeignKeyStatement(fk);
        VPG.logInfo("Table " + getName() + " recieved a command to add a foreign key" + "and will try to execute the following statement:");
        VPG.logInfo(sql);
        Connection con = getDatabaseDescriptor().getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.execute();
        ps.close();
        con.close();
    }

    private String dropForeignKeyStatement(ForeignKey fk) {
        String sql = "ALTER TABLE ";
        sql += "\"" + getSchema().getSchemaName() + "\".\"" + getName() + "\"\n";
        sql += " DROP CONSTRAINT \"" + fk.getName() + "\" RESTRICT";
        return sql;
    }

    /**
     * Equality between two tables. For two table to be equal they
     * must have the same database, the same schema and the same name.
     * @param o Object
     * @return boolean
     */
    public boolean equals(Object o) {
        if (!(o instanceof Table)) {
            return false;
        }
        Table that = (Table) o;
        if (this.getName() == null ? that.getName() != null : !this.getName().equals(that.getName())) {
            return false;
        }
        if (this.getSchema() == null ? that.getSchema() != null : !this.getSchema().equals(that.getSchema())) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public Schema getSchema() {
        return schema;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Gets the Primary key for this table. This method returns
     * the cached value. To get a fresh value call {@link #invalidateCache} first.
     * @return PrimaryKey
     */
    public PrimaryKey getPrimaryKey() {
        if (this.primaryKey == null) {
            try {
                this.primaryKey = getFreshPrimaryKey();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return this.primaryKey;
    }

    /**
     * Gets the columns for this table. This method return the
     * cached values. To get a fresh list of columns call
     * {@link #invalidateCache} first.
     * @return List
     */
    public List getColumns() {
        if (this.columns == null) {
            try {
                this.columns = getFreshColumns();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return this.columns;
    }

    /**
     * Gets a column for the supplied column name. This
     * method searches for the column in the cache. To search in
     * a fresh list of column call {@link #invalidateCache} first.
     * @param columnName String
     * @return Column
     */
    public Column getColumn(String columnName) {
        Iterator iter = getColumns().iterator();
        while (iter.hasNext()) {
            Column c = (Column) iter.next();
            if (c.getName().equals(columnName)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Returns the primary key for this table ignoring the
     * cache. This is live data that also updates the cache.
     * @throws SQLException
     * @return PrimaryKey
     */
    public PrimaryKey getFreshPrimaryKey() throws SQLException {
        HashMap pkCols = new HashMap(4);
        Connection con = getSchema().getDatabase().getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet rs = meta.getPrimaryKeys(null, getSchema().getSchemaName(), getName());
        String pkName = "";
        while (rs.next()) {
            String colName = rs.getString(4);
            short seq = rs.getShort(5);
            pkName = rs.getString(6);
            pkCols.put(new Short(seq), colName);
        }
        rs.close();
        List cols = new LinkedList();
        for (short i = 0; i < pkCols.size(); i++) {
            Column c = new Column(this);
            c.setName((String) pkCols.get(new Short((short) (i + 1))));
            cols.add(c);
        }
        if (cols != null && pkCols.size() > 0) {
            PrimaryKey pk = new PrimaryKey(this, pkName, cols);
            this.primaryKey = pk;
            return pk;
        }
        con.close();
        return null;
    }

    /**
     * Gets a fresh list of columns for this table ignoring
     * the cache. This method also updates the cache.
     * @throws SQLException
     * @return List
     */
    public List getFreshColumns() throws SQLException {
        Connection con = getSchema().getDatabase().getConnection();
        LinkedList c = new LinkedList();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet crs = meta.getColumns(null, getSchema().getSchemaName(), getName(), null);
        while (crs.next()) {
            String colName = crs.getString(4);
            short colDataType = crs.getShort(5);
            String colDataTypeName = crs.getString(6);
            int colSize = crs.getInt(7);
            int colDecimalDigits = crs.getInt(9);
            String colRemarks = crs.getString(12);
            String colDefault = crs.getString(13);
            int colPosition = crs.getInt(17);
            String colIsNullable = crs.getString(18);
            Column col = new Column(this);
            col.setAllowNulls("YES".equalsIgnoreCase(colIsNullable));
            col.setDef(colDefault);
            col.setDigits(colDecimalDigits);
            col.setName(colName);
            col.setPosition(colPosition);
            col.setRemark(colRemarks);
            col.setSize(colSize);
            col.setDataType(DataTypeFactory.createDataType(colDataType, colDataTypeName));
            c.add(col);
        }
        crs.close();
        con.close();
        this.columns = c;
        return this.columns;
    }

    /**
     * String representation for this table. This is useful
     * for displaying the table, but there is no internationalization support.
     * @return String
     */
    public String toString() {
        return getSchema().getDatabase().getDbName() + "." + getSchema().getSchemaName() + "." + getName();
    }

    /**
     * DDL String for this table. The table
     * can always be re-created using this DDL
     * SQL String. This is very useful when dropping
     * and re-creating the table. DDL uses
     * cached data where available and creates the
     * String using the object itself without querring
     * the database unless data can only be retrieved
     * by connecting to the database (eg. foreign keys).
     * @return String
     */
    public String getDDL() {
        String ddl = "CREATE TABLE ";
        ddl += "\"" + getSchema().getSchemaName() + "\".\"" + getName() + "\" (\n";
        Iterator iter1 = getColumns().iterator();
        ArrayList commentedColumns = new ArrayList();
        boolean firstRow = true;
        while (iter1.hasNext()) {
            Column col = (Column) iter1.next();
            if (!firstRow) {
                ddl += ",\n";
            }
            ddl += "\t";
            ddl += col.getDDLParticipation();
            firstRow = false;
            if (col.getRemark() != null && col.getRemark().length() > 0) {
                commentedColumns.add(col);
            }
        }
        PrimaryKey pk = getPrimaryKey();
        if (pk != null) {
            if (!firstRow) {
                ddl += ",";
            }
            ddl += "\n\t" + pk.getDDLParticipation();
        }
        try {
            ForeignKey[] fks = getForeignKeys();
            for (int i = 0; i < fks.length; i++) {
                if (!firstRow) {
                    ddl += ",";
                }
                ddl += "\n\t" + fks[i].getDDLParticipation();
            }
        } catch (SQLException ex) {
            Message.showError(ex);
        }
        ddl += "\n);\n";
        Iterator iter3 = commentedColumns.iterator();
        while (iter3.hasNext()) {
            Column comCol = (Column) iter3.next();
            ddl += "\nCOMMENT ON COLUMN ";
            ddl += "\"" + getSchema().getSchemaName() + "\".\"";
            ddl += getName() + "\".\"" + comCol.getName() + "\" IS ";
            ddl += "\'" + comCol.getRemark() + "\';";
        }
        return ddl;
    }
}
