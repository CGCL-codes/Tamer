package simpleorm.sessionjdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import simpleorm.dataset.SFieldFlags;
import simpleorm.dataset.SFieldMeta;
import simpleorm.dataset.SFieldReference;
import simpleorm.dataset.SFieldScalar;
import simpleorm.dataset.SGeneratorMode;
import simpleorm.dataset.SQuery;
import simpleorm.dataset.SQueryTable;
import simpleorm.dataset.SQueryTransient;
import simpleorm.dataset.SRecordInstance;
import simpleorm.dataset.SRecordMeta;
import simpleorm.drivers.SDriverCache;
import simpleorm.drivers.SDriverDB2_400;
import simpleorm.drivers.SDriverDerby;
import simpleorm.drivers.SDriverFirebird;
import simpleorm.drivers.SDriverH2;
import simpleorm.drivers.SDriverHSQL;
import simpleorm.drivers.SDriverInformix;
import simpleorm.drivers.SDriverInterbase;
import simpleorm.drivers.SDriverMSSQL;
import simpleorm.drivers.SDriverMySQL;
import simpleorm.drivers.SDriverOracle;
import simpleorm.drivers.SDriverPostgres;
import simpleorm.drivers.SDriverSapDB;
import simpleorm.drivers.SDriverSybase;
import simpleorm.utils.SException;
import simpleorm.utils.SLog;

/**
 * This is the internal generic database driver that contains database dependent
 * code. Specific SimpleORM drivers extend this class and specialize its methods
 * as required. The driver type is inferred from the jdbc connection's meta data.
 * <p>
 *  (Profiling suggests that memoising these generators could produce a 5-10%
 * improvement in bulk updates.)
 * <p>
 * 
 * There is now one driver instance per connection so one can say
 * SSession.getDriver().setMyFavouriteParam(...)
 * <p>
 * 
 * SQL 92 standard data types, I think:- boolean, Character(n), character
 * varying(n), date, float(p), real, double precision, smallint, int | integer,
 * decimal(p,s), numeric(p,s), time, interval, timestamp with timezone,
 */
public class SDriver {

    SSessionJdbc session;

    /**
	 * Note that these are only prototypes. New SDriver instances are created
	 * for each connection.
	 */
    private static ArrayList<SDriver> drivers = new ArrayList<SDriver>();

    static {
        SDriver[] ds = new SDriver[] { new SDriverHSQL(), new SDriverH2(), new SDriverPostgres(), new SDriverMySQL(), new SDriverOracle(), new SDriverMSSQL(), new SDriverDB2_400(), new SDriverInformix(), new SDriverInterbase(), new SDriverFirebird(), new SDriverSapDB(), new SDriverSybase(), new SDriverDerby(), new SDriverCache() };
        for (int dx = 0; dx < ds.length; dx++) ds[dx].registerDriver();
    }

    protected SDriver() {
    }

    /**
	 * Chooses default driver based on the connection's meta data.
	 * <p>
	 * 
	 * Note that if you have trouble with the driver defaulting mechanism, then
	 * specify the driver explicitly as the third parameter to
	 * SSession.open.
	 * <p>
	 * 
	 * Note also that a new driver instance is created each time.
	 */
    static SDriver newSDriver(java.sql.Connection con) {
        String databaseName = null;
        String driverName = null;
        try {
            driverName = con.getMetaData().getDriverName();
            databaseName = con.getMetaData().getDatabaseProductName();
        } catch (Exception ex) {
            throw new SException.Jdbc(ex);
        }
        for (int dx = 0; dx < drivers.size(); dx++) {
            SDriver driver = drivers.get(dx);
            if (driver.driverName().equals(driverName)) try {
                return driver.getClass().newInstance();
            } catch (Exception ex) {
                throw new SException.Error(ex);
            }
        }
        SLog.getSessionlessLogger().warn("Unknown Database '" + databaseName + "' driver '" + driverName + "'. Using generic SDriver.");
        SDriver drv = new SDriver();
        return drv;
    }

    /**
	 * Add driver to the list of possible drivers that can be found by
	 * SSession.attach. The key is returned by driverName.
	 */
    public void registerDriver() {
        drivers.add(this);
    }

    /** The driver name to be compared to getMetaData().getDriverName() */
    protected String driverName() {
        return "Generic Driver";
    }

    protected <RI extends SRecordInstance> SQueryExecute<RI> queryExecuteFactory(SSessionJdbc session, SQuery<RI> query) {
        return new SQueryExecute<RI>(session, query);
    }

    protected SQueryTransientExecute<?> queryExecuteFactory(SSessionJdbc session, SQueryTransient query) {
        return new SQueryTransientExecute(session, query);
    }

    /** Wraps identifiers in "s to avoid reserved word issues. 
	 * (Override this for dbs that use different chars, eg [xxx] for MS-SQL.
	 * We do quote these days to avoid the endless problems with reserved words.
	 */
    protected void appendQuotedIdentifier(String ident, StringBuffer buf) {
        appendQuotedIdentifier(ident, buf, '"');
    }

    protected void appendQuotedIdentifier(String ident, StringBuffer buf, char quote) {
        if (ident.length() > maxIdentNameLength()) throw new SException.Error("Identifier '" + ident + "' is longer than " + maxIdentNameLength() + " chars as permitted by " + driverName());
        buf.append(quote);
        for (int cx = 0; cx < ident.length(); cx++) {
            char ch = ident.charAt(cx);
            buf.append(ch);
            if (ch == quote) buf.append(quote);
        }
        buf.append(quote);
    }

    /** name appended, quoted if appropriate. */
    public void appendColumnName(SFieldScalar field, StringBuffer buf) {
        String name = field.getColumnName();
        if (field.quoteName) appendQuotedIdentifier(name, buf); else buf.append(name);
    }

    /** name, quoted if appropriate. */
    public void appendTableName(SRecordMeta<?> table, StringBuffer buf) {
        String name = table.getTableName();
        if (table.quoteName) {
            appendQuotedIdentifier(name, buf);
        } else {
            buf.append(name);
        }
    }

    /** append alias if necessary, and then return true. Return false otherwise */
    public boolean appendAliasIfNecessary(SQueryTable<?> table, StringBuffer buf) {
        if (!table.getAlias().equals(table.getRecordMeta().getTableName())) {
            buf.append(" ").append(table.getAlias());
            return true;
        }
        return false;
    }

    /** The maximum size for table names and foreign key constraint names. */
    public int maxIdentNameLength() {
        return 30;
    }

    /**
	 * True if exclusive locks are properly supported. Otherwise optimistic
	 * locks are always used.
	 * <p>
	 */
    public boolean supportsLocking() {
        return true;
    }

    /**
	 * Does this driver support Jdbc 2 batch updates.
	 * Specific drivers may override this implementation if the jbdc driver lies
	 * about its support, or if the jdbc implementation of batching is inefficient.
	 * @return
	 */
    public boolean supportsBatchUpdates() {
        try {
            DatabaseMetaData dbmeta = session.jdbcConnection.getMetaData();
            if (dbmeta != null) {
                if (dbmeta.supportsBatchUpdates()) {
                    return true;
                }
            }
        } catch (SQLException ex) {
            throw new SException.Jdbc("Trying to determine batch update support ", ex);
        } catch (AbstractMethodError err) {
            throw new SException.Jdbc("JDBC driver does not implement Jdbc 2.0 'supportsBatchUpdates' method", err);
        }
        return false;
    }

    /**
	 * These allow you to create a new SFieldMeta object at runtime and then
	 * update the table to include it. Eg. for end user customizations.
	 *  ## Ideally this could be further automated so that the SRecordMeta and
	 * JDBC meta data for a table could be compared and the table automatically
	 * altered.
	 */
    public String alterTableAddColumnSQL(SFieldScalar field) {
        StringBuffer sql = new StringBuffer();
        sql.append("\nALTER TABLE ");
        appendTableName(field.getRecordMeta(), sql);
        sql.append(" ADD COLUMN ");
        sql.append(wholeColumnSQL(field));
        sql.append(clauseSeparator("    "));
        return sql.toString();
    }

    public String alterTableDropColumnSQL(SFieldScalar field) {
        StringBuffer sql = new StringBuffer();
        sql.append("\nALTER TABLE ");
        appendTableName(field.getRecordMeta(), sql);
        sql.append(" DROP COLUMN ");
        appendColumnName(field, sql);
        sql.append(clauseSeparator("    "));
        return sql.toString();
    }

    /**
	 * Returns a <code>CREATE TABLE</code> for this table. Delegated from
	 * SRecord. This is split up into many sub-methods so that they can be
	 * selectively specialized by other drivers.
	 */
    public String createTableSQL(SRecordMeta<?> meta) {
        StringBuffer sql = new StringBuffer(1000);
        sql.append("\nCREATE TABLE ");
        appendTableName(meta, sql);
        sql.append("(");
        for (SFieldMeta fld : meta.getFieldMetas()) {
            Object cq = null;
            if (!(fld instanceof SFieldReference) && cq == null) {
                sql.append(clauseSeparator("    "));
                sql.append(wholeColumnSQL((SFieldScalar) fld));
                sql.append(", ");
            }
        }
        sql.append(clauseSeparator("    "));
        sql.append(primaryKeySQL(meta));
        sql.append(indexKeySQL(meta));
        sql.append(foreignKeysSQL(meta));
        sql.append(postTablePreParenSQL(meta));
        sql.append(")");
        sql.append(postTablePostParenSQL(meta));
        sql.append(clauseSeparator("    "));
        return sql.toString();
    }

    /**
	 * Normally newline and indent to separate clauses of large SQL statement
	 */
    protected String clauseSeparator(String indent) {
        return "\n" + indent;
    }

    /**
	 * Returns <code>MY_COL VARCHAR(13) NOT NULL</code>.
	 * 
	 * @see SFieldFlags#SMANDATORY
	 */
    protected String wholeColumnSQL(SFieldScalar fld) {
        StringBuffer sql = new StringBuffer(60);
        appendColumnName(fld, sql);
        int len = fld.getColumnName().length();
        sql.append("                 ".substring(len > 15 ? 15 : len - 1));
        if (fld.sqlDataTypeOverride != null) sql.append(fld.sqlDataTypeOverride); else sql.append(columnTypeSQL(fld, fld.defaultSqlDataType()));
        addNull(sql, fld);
        if (fld.getGeneratorMode() == SGeneratorMode.SINSERT) sql.append(addInsertGenerator(fld));
        sql.append(postColumnSQL(fld));
        return sql.toString();
    }

    protected void addNull(StringBuffer sql, SFieldScalar fld) {
        if (fld.isPrimary() || fld.isMandatory()) sql.append(" NOT NULL"); else sql.append(" NULL");
    }

    /** Used to override the default.  
     * ONLY called if no fld.defaultSqlDataType has been specified,
     * ie. only used to override the SFieldScalar.defaultSqlDataType. 
     */
    protected String columnTypeSQL(SFieldScalar field, String defalt) {
        return defalt;
    }

    protected String addInsertGenerator(SFieldMeta fld) {
        throw new SException.Error("This database does not support SINSERT key generation");
    }

    /** After NOT NULL but before the ",", ie column specific annotations. */
    protected String postColumnSQL(SFieldMeta field) {
        return "";
    }

    /** Return <code>PRIMARY KEY(KCOL, KCOL)</code> appended to end. */
    protected String primaryKeySQL(SRecordMeta<?> meta) {
        StringBuffer pkey = new StringBuffer("PRIMARY KEY (");
        boolean firstpk = true;
        for (SFieldScalar fld : meta.getPrimaryKeys()) {
            if (!firstpk) pkey.append(", ");
            firstpk = false;
            appendColumnName(fld, pkey);
        }
        pkey.append(")");
        return pkey.toString();
    }

    /** Needed for MySQL to create indexes on foreign keys */
    protected String indexKeySQL(SRecordMeta<?> meta) {
        return "";
    }

    /**
	 * Returns <code>FOREIGN KEY (FKCOL, FKCOL) REFERENCES FTABLE (KCOL,
	 KCOL)</code>
	 * appended to end.
	 */
    protected String foreignKeysSQL(SRecordMeta<?> meta) {
        return mapForeignKeys(meta, true);
    }

    protected String mapForeignKeys(SRecordMeta<?> meta, boolean foreignKey) {
        StringBuffer fkey = new StringBuffer("");
        List<SFieldMeta> fields = meta.getFieldMetas();
        for (SFieldMeta fld : fields) {
            int fx = fields.indexOf(fld);
            if (fld instanceof SFieldReference) {
                SFieldReference<?> fldRef = (SFieldReference<?>) fld;
                StringBuffer sbFkey = new StringBuffer(40);
                StringBuffer sbRefed = new StringBuffer(40);
                for (SFieldScalar fk : fldRef.getForeignKeyMetas()) {
                    if (sbFkey.length() > 0) {
                        sbFkey.append(", ");
                    }
                    appendColumnName(fk, sbFkey);
                    SFieldScalar pk = fldRef.getPrimaryKeyForForegnKey(fk);
                    if (sbRefed.length() > 0) {
                        sbRefed.append(", ");
                    }
                    appendColumnName(pk, sbRefed);
                }
                if (foreignKey) makeForeignKeySQL(meta, fx, fldRef, sbFkey, sbRefed, fkey); else makeForeignKeyIndexSQL(meta, fx, fldRef, sbFkey, sbRefed, fkey);
            }
        }
        return fkey.toString();
    }

    private void makeForeignKeySQL(SRecordMeta<?> meta, int fx, SFieldReference<?> fldRef, StringBuffer sbFkey, StringBuffer sbRefed, StringBuffer fkey) {
        fkey.append(",\n    CONSTRAINT ");
        String tname = meta.getTableName();
        String fxStr = fx + "";
        int spare = maxIdentNameLength() - tname.length() - fxStr.length() - 1;
        if (spare < 0) throw new SException.Error("Table name '" + tname + "' is longer than " + maxIdentNameLength() + " - " + (fxStr.length() + 1) + " chars as permitted by " + driverName() + " to allow for _nn constraint name.");
        String fkeyName = tname + "_" + fxStr;
        String fname = fldRef.getFieldName();
        if (spare > 1) fkeyName += "_";
        if (spare > fname.length()) fkeyName += fname; else if (spare > 0) fkeyName += fname.substring(0, spare - 1);
        appendQuotedIdentifier(fkeyName, fkey);
        fkey.append("\n      FOREIGN KEY (");
        fkey.append(sbFkey);
        fkey.append(")\n      REFERENCES ");
        appendTableName(fldRef.getReferencedRecordMeta(), fkey);
        fkey.append(" (");
        fkey.append(sbRefed.toString());
        fkey.append(")");
    }

    protected void makeForeignKeyIndexSQL(SRecordMeta<?> meta, int fx, SFieldReference<?> fldRef, StringBuffer sbFkey, StringBuffer sbRefed, StringBuffer fkey) {
    }

    /** Any other text to be added before the final ")" */
    protected String postTablePreParenSQL(SRecordMeta<?> meta) {
        return "";
    }

    /** Any other text to be added after the final ")". No ";". */
    protected String postTablePostParenSQL(SRecordMeta<?> meta) {
        return "";
    }

    /**
	 * Returns the SQL statement for a SELECT in a structured way. Used by
	 * findOrInsert. <code>select</code> and <code>where</code> are arrays
	 * of <code>SFieldMeta</code>s. Returns SQL statement as a string.
	 * <p>
	 * 
	 * This now quotes table and column names so that they become case
	 * independent.
	 * <p>
	 * 
	 * sps is links to the SPreparedStatement object. It can have arbitrary
	 * properties set to provide fine control over the query. Examples include
	 * limits.
	 */
    protected <RI extends SRecordInstance> String selectSQL(SFieldScalar[] select, SRecordMeta<RI> from, SFieldScalar[] where, String orderBy, boolean forUpdate) {
        StringBuffer selectBuf = new StringBuffer(100);
        boolean first = true;
        for (SFieldScalar sfld : select) {
            if (!first) {
                selectBuf.append(", ");
            }
            appendColumnName(sfld, selectBuf);
            first = false;
        }
        StringBuffer wherestr = new StringBuffer(100);
        first = true;
        for (SFieldScalar wfld : where) {
            if (!first) wherestr.append(" AND ");
            appendColumnName(wfld, wherestr);
            wherestr.append(" = ? ");
            first = false;
        }
        return selectSQL(selectBuf.toString(), fromSQL(new SQueryTable<RI>(from.getTableName(), from, select, 1)), null, wherestr.toString(), null, orderBy, forUpdate, Integer.MAX_VALUE, 0);
    }

    protected String selectSQL(String select, String fromClause, String joinClause, String where, String groupBy, String orderBy, boolean forUpdate, long limit, long offset) {
        StringBuffer sqlbuf = new StringBuffer(200);
        sqlbuf.append("SELECT ").append(select);
        sqlbuf.append(clauseSeparator(""));
        sqlbuf.append(fromClause);
        sqlbuf.append(clauseSeparator(""));
        if (joinClause != null) {
            sqlbuf.append(" ").append(joinClause).append(" ");
        }
        sqlbuf.append(postFromSQL(forUpdate));
        if (where != null) {
            sqlbuf.append(" WHERE " + where);
            sqlbuf.append(clauseSeparator(""));
        }
        if (groupBy != null) {
            sqlbuf.append(" GROUP BY " + groupBy);
            sqlbuf.append(clauseSeparator(""));
        }
        if (orderBy != null) {
            sqlbuf.append(" ORDER BY " + orderBy);
            sqlbuf.append(clauseSeparator(""));
        }
        sqlbuf.append(forUpdateSQL(forUpdate));
        if ((offset != 0 || limit < Integer.MAX_VALUE) && getOffsetStrategy().equals(OffsetStrategy.QUERY)) {
            if (orderBy == null) {
                getLogger().warn("Order by is strongly recommended when using limit/offset");
            }
            sqlbuf.append(limitSQL(offset, limit));
        }
        return sqlbuf.toString();
    }

    /**
	 * Returns update clause, may not be valid in certain lock modes etc. Right
	 * at the end of the query.
	 * <p>
	 * 
	 * Oracle, Postgresql, and new in MS SQL 2005 support data versioning or
	 * snapshots. This means that repeatable read is achieved by caching the
	 * previous value read instead of using read locks. This approach makes it
	 * critical to add FOR UPDATE where appropriate or there is effectively no
	 * locking.
	 * <p>
	 * 
	 * Indeed, in Oracle, you are guaranteed that several SELECTS will return
	 * the same value, but a subsequent SELECT FOR UPDATE in the same
	 * transaction may return a different value.
	 * <p>
	 * 
	 */
    protected String forUpdateSQL(boolean forUpdate) {
        if (supportsLocking() && forUpdate) return " FOR UPDATE"; else return "";
    }

    /**
	 * Drivers that choose to implement a QUERY offset strategy
	 * should return the limit statement here
	 * @param offset number of rows to skip
	 * @param limit number of rows to retrieve
	 * @return LIMIT + OFFSET string statement
	 */
    public String limitSQL(long offset, long limit) {
        getLogger().warn("Warning, inconsistent SDriver. Limit won't work");
        return "";
    }

    /** Returns the FROM Table clause */
    protected String fromSQL(SQueryTable<?> from) {
        StringBuffer res = new StringBuffer();
        res.append(" FROM ");
        appendTableName(from.getRecordMeta(), res);
        appendAliasIfNecessary(from, res);
        res.append(" ");
        return res.toString();
    }

    protected String joinSQL(SQueryTable<?> table) {
        StringBuffer res = new StringBuffer(800);
        res.append(table.getType().toString()).append(" JOIN ");
        appendTableName(table.getRecordMeta(), res);
        res.append(" ");
        appendAliasIfNecessary(table, res);
        res.append(" ON ");
        if (table.getRawOnClause() != null) res.append(table.getRawOnClause()); else {
            boolean first = true;
            boolean oneToManyJoin = (table.getFieldReference().getRecordMeta() == table.getRecordMeta() && table.getFieldReference().getReferencedRecordMeta() != table.getRecordMeta());
            for (SFieldScalar fkey : table.getFieldReference().getForeignKeyMetas()) {
                if (!first) res.append(" AND ");
                if (oneToManyJoin) {
                    appendField(table, fkey, res);
                    res.append(" = ");
                    appendField(table.getFromTable(), table.getFieldReference().getPrimaryKeyForForegnKey(fkey), res);
                } else {
                    appendField(table.getFromTable(), fkey, res);
                    res.append(" = ");
                    appendField(table, table.getFieldReference().getPrimaryKeyForForegnKey(fkey), res);
                }
                first = false;
            }
        }
        return res.toString();
    }

    /** For MSSQL. Just after all the tables in the From clause. */
    protected String postFromSQL(boolean forUpdate) {
        return "";
    }

    /**
	 * Returns the SQL statement for an UPDATE in a structured way. Used by
	 * flush(). <code>updates</code> and <code>where</code> are SSArrayLists
	 * of SFieldMetas. Returns SQL statement as a string.
	 */
    protected String updateSQL(ArrayList<SFieldScalar> updates, SRecordMeta<?> from, ArrayList<SFieldScalar> where, SRecordInstance instance, Object[] keyMetaValues) {
        StringBuffer ret = new StringBuffer(200);
        ret.append("UPDATE ");
        appendTableName(from, ret);
        ret.append(" SET ");
        for (int sx = 0; sx < updates.size(); sx++) {
            if (sx > 0) ret.append(", ");
            SFieldScalar sfld = updates.get(sx);
            appendColumnName(sfld, ret);
            ret.append(" = ?");
        }
        whereSQL(ret, where, instance, keyMetaValues);
        return ret.toString();
    }

    /**
	 * Returns the SQL statement for an INSERT in a structured way. Used by
	 * flush(). <code>updates</code> and <code>where</code> are SSArrayLists
	 * of SFieldMetas. Returns SQL statement as a string.
	 */
    protected String insertSQL(ArrayList<SFieldScalar> updates, SRecordMeta<?> from) {
        StringBuffer ret = new StringBuffer(800);
        ret.append("INSERT INTO ");
        appendTableName(from, ret);
        ret.append(" (");
        for (int sx = 0; sx < updates.size(); sx++) {
            if (sx > 0) ret.append(", ");
            SFieldScalar sfld = updates.get(sx);
            appendColumnName(sfld, ret);
        }
        ret.append(") VALUES (");
        for (int sx = 0; sx < updates.size(); sx++) {
            if (sx > 0) ret.append(", ");
            ret.append("?");
        }
        ret.append(")");
        return ret.toString();
    }

    /**
	 * Returns the SQL statement for an DELETE in a structured way. Used by
	 * flush(). <code>where</code> are SSArrayLists of SFieldMetas. Returns
	 * SQL statement as a string.
	 */
    protected String deleteSQL(SRecordMeta<?> from, ArrayList<SFieldScalar> where, SRecordInstance instance, Object[] keyMetaValues) {
        StringBuffer ret = new StringBuffer(200);
        ret.append("DELETE FROM ");
        appendTableName(from, ret);
        whereSQL(ret, where, instance, keyMetaValues);
        return ret.toString();
    }

    /**
	 * Produces the WHERE clause of UPDATE and DELETE statements. Needs to know
	 * the instance values so that it can use the IS NULL test (for optimistic
	 * locking).
	 */
    protected void whereSQL(StringBuffer ret, ArrayList<SFieldScalar> where, SRecordInstance instance, Object[] keyMetaValues) {
        ret.append(" WHERE ");
        for (int wx = 0; wx < where.size(); wx++) {
            if (wx > 0) ret.append(" AND ");
            SFieldScalar wfld = where.get(wx);
            Object value = keyMetaValues[wx];
            appendColumnName(wfld, ret);
            ret.append(value != null ? " = ? " : " IS NULL ");
        }
    }

    /**
	 * Used for column names where they must be preceded by their table or alias name
	 * @param tbl
	 * @param sclField
	 * @param buf
	 */
    protected void appendField(SQueryTable<?> tbl, SFieldScalar sclField, StringBuffer buf) {
        boolean aliased = appendAliasIfNecessary(tbl, buf);
        if (!aliased) appendTableName(tbl.getRecordMeta(), buf);
        buf.append(".");
        appendColumnName(sclField, buf);
    }

    /**
	 * JDBC preferred, if jdbc driver has a sensible scrollable resultset implementation
	 * QUERY if database would handle it better via some proprietary sql extension (eg. LIMIT)
	 * BULK Simpleorm will get the whole resultset and skip the first row. Worst case scenario :(
	 * 
	 * Defaults to BULK
	 */
    public OffsetStrategy getOffsetStrategy() {
        return OffsetStrategy.SCAN;
    }

    /**
	 * Generates a new key using SELECT MAX+1. This will (hopefully) be
	 * specialized for each database driver to be correct. Note that there is a
	 * global counter kept so it will actually work OK if all the updates are
	 * from one JVM. Amazing that there is still no standard way to do this in
	 * SQL.
	 * <p>
	 *  ## (There is scope to optimize this at some point so that one JDBC call
	 * can both generate the sequence number and insert a new record. But that
	 * means that the new record's key is not available until insert time which
	 * causes problems for foreign keys. Alternatively one can get batches of 10
	 * (say) sequences at a time and then use an internal counter, but this will
	 * leave big holes in the sequence. Defer this to version 1.)
	 */
    protected long generateKeySelectMax(SRecordMeta<?> rec, SFieldScalar keyFld) {
        StringBuffer qry = new StringBuffer("SELECT MAX(");
        appendColumnName(keyFld, qry);
        qry.append(") FROM ");
        appendTableName(rec, qry);
        Object next = getSession().rawQuerySingle(qry.toString(), false);
        long db;
        if (next == null) db = 0; else if (next instanceof Number) db = ((Number) next).longValue(); else db = Long.parseLong(next.toString());
        return keyFld.nextGeneratedValue(db + 1);
    }

    /** Supports sequence generators that are separate from the Table; (Oracle Style) ie. CREATE SEQUENCE FOO */
    public boolean supportsKeySequences() {
        return false;
    }

    protected long generateKeySequence(SRecordMeta<?> rec, SFieldScalar keyFld) {
        throw new SException.Error("Database does not support SEQUENCES");
    }

    protected String createSequenceDDL(String name) {
        throw new SException.Error("Sequences not supported");
    }

    protected String dropSequenceDDL(String name) {
        throw new SException.Error("Sequences not supported");
    }

    /** Supports keys created during INSERTion of new records (MSSQL & MySQL Style). */
    public boolean supportsInsertKeyGeneration() {
        return false;
    }

    protected boolean includeGeneratedKeysInInsertValues() {
        return true;
    }

    protected long retrieveInsertedKey(SRecordMeta<?> rec, SFieldScalar keyFld) {
        throw new SException.Error("Database does not support INSERT IDENTITY");
    }

    /**
	 * Utility routine for dropping tables called by SSession. Driver
	 * specific versions should only hide table non existant errors (and not
	 * warn about them).
	 */
    public void dropTableNoError(String table) {
        Connection con = session.getJdbcConnection();
        try {
            PreparedStatement ps = con.prepareStatement("DROP TABLE " + table);
            ps.executeUpdate();
        } catch (SQLException ex) {
            getLogger().warn("DROPPING " + table + ": " + ex);
        }
    }

    /**
	 * OffsetStrategy implemented by drivers.
	 * JDBC means driver relies on jdbc driver to scroll through the result set
	 * QUERY means driver will trick to scrolling through the result set with custom
	 * sql query, like LIMIT/OFFSET
	 * SCAN is the dumber implementation, that is get all records and skip the first ones
	 */
    public enum OffsetStrategy {

        JDBC, QUERY, SCAN
    }

    public SSessionJdbc getSession() {
        return session;
    }

    public SLog getLogger() {
        return getSession().getLogger();
    }
}
