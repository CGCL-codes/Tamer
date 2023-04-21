package com.jujunie.integration.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Julien BETI
 * @since 0.06.01
 */
public class OracleGenericDAO extends DAO implements MetaInfoDAO {

    /** Defining encodings mappings */
    private static final Map<String, String> ENCODINGS;

    static {
        ENCODINGS = new HashMap<String, String>();
        ENCODINGS.put("UTF8", "UTF-8");
        ENCODINGS.put("AL32UTF8", "UTF-8");
        ENCODINGS.put("WE8ISO8859P1", "ISO8859-1");
    }

    /** Logger */
    private static final Log log = LogFactory.getLog(OracleGenericDAO.class);

    /** Tablaspace names and statuses*/
    private static final String SQL_TABLESPACE_LIST = "select   TABLESPACE_NAME, STATUS from dba_tablespaces order by TABLESPACE_NAME ";

    /** Tablespace names and statuses */
    private static final String SQL_TABLESPACE_SIZES = "select a.TABLESPACE_NAME, " + "       a.BYTES bytes_used, " + "       b.BYTES bytes_free " + " from     " + "    ( " + "        select  TABLESPACE_NAME, " + "            sum(BYTES) BYTES  " + "        from    dba_data_files  " + "        group   by TABLESPACE_NAME " + "    ) " + "    a, " + "    ( " + "        select  TABLESPACE_NAME, " + "            sum(BYTES) BYTES , " + "            max(BYTES) largest  " + "        from    dba_free_space  " + "        group   by TABLESPACE_NAME " + "    ) " + "    b " + " where   a.TABLESPACE_NAME=b.TABLESPACE_NAME " + "order   by 1 desc";

    /** Tablespace used by DB objects */
    private static final String SQL_TABLESPACE_USED = "select count(*) from dba_segments where tablespace_name = ?";

    /** Schema size */
    private static final String SQL_SCHEMA_SIZE = "select sum(bytes) from  sys.dba_segments where owner = ?";

    /** Schema info */
    private static final String SQL_SCHEMA_BASE_INFO = "select created, default_tablespace, temporary_tablespace, account_status from dba_users where username = ?";

    /** Encoding */
    private static final String SQL_DB_ENCODING = "select * from v$nls_parameters where parameter in ('NLS_LANGUAGE','NLS_TERRITORY','NLS_CHARACTERSET')" + " order by parameter";

    /**
     * @see com.jujunie.integration.database.MetaInfoDAO#getSchemaMetaInfo(com.jujunie.integration.database.SchemaInfo)
     */
    public void getSchemaMetaInfo(SchemaInfo s) throws DBException {
        assert s != null : "Cannot populate null...";
        log.debug(s.getDatabase().getDatasourceName());
        try {
            PreparedStatement ps = super.getPreparedStatement(SQL_SCHEMA_SIZE, s.getDatabase().getDatasourceName());
            ps.setString(1, s.getName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                s.setSize(rs.getLong(1));
                rs.close();
            }
            ps = super.getPreparedStatement(SQL_SCHEMA_BASE_INFO, s.getDatabase().getDatasourceName());
            ps.setString(1, s.getName());
            rs = ps.executeQuery();
            if (rs.next()) {
                s.setCreation(rs.getTimestamp(1));
                s.setDefaultTablespace(rs.getString(2));
                s.setTemporaryTablespace(rs.getString(3));
                s.setStatus(rs.getString(4));
                s.setEncoding(s.getDatabase().getEncoding());
                s.setJavaEncoding(s.getDatabase().getJavaEncoding());
                rs.close();
            }
        } catch (SQLException e) {
            s.setError(new DBException("SQLException while getting info of Schema: " + s.getName(), e));
        }
    }

    /**
     * @see com.jujunie.integration.database.MetaInfoDAO#getDatabaseMetaInfo(com.jujunie.integration.database.DatabaseInfo)
     */
    public void getDatabaseMetaInfo(DatabaseInfo di) throws DBException {
        assert di != null : "Cannot populate null...";
        log.debug(di.getDatasourceName());
        try {
            PreparedStatement ps = super.getPreparedStatement(SQL_TABLESPACE_LIST, di.getDatasourceName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TablespaceInfo ti = null;
                do {
                    ti = new TablespaceInfo();
                    ti.setName(rs.getString(1));
                    ti.setStatus(rs.getString(2));
                    di.addTablespace(ti);
                } while (rs.next());
                rs.close();
            }
            ps = super.getPreparedStatement(SQL_DB_ENCODING, di.getDatasourceName());
            rs = ps.executeQuery();
            if (rs.next()) {
                String characterset = rs.getString(2);
                rs.next();
                String language = rs.getString(2);
                rs.next();
                String territory = rs.getString(2);
                di.setEncoding(language + '_' + territory + '.' + characterset);
                if (ENCODINGS.containsKey(characterset)) {
                    di.setJavaEncoding(ENCODINGS.get(characterset));
                } else {
                    String message = "Cannot find a java encoding corresponding to Oracle characterset: " + characterset;
                    log.warn(message);
                    di.setError(new DBException(message));
                }
                rs.close();
            }
        } catch (SQLException e) {
            di.setError(new DBException("SQLException while getting tablespaces of database: " + di.getDatasourceName(), e));
        }
    }

    public void getTablespaceMetaIfo(DatabaseInfo di) throws DBException {
        try {
            PreparedStatement ps = super.getPreparedStatement(SQL_TABLESPACE_SIZES, di.getDatasourceName());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                TablespaceInfo ti = null;
                do {
                    ti = di.getTablespaceInfo(rs.getString(1));
                    ti.setBytesUsed(rs.getLong(2));
                    ti.setBytesLeft(rs.getLong(3));
                } while (rs.next());
                rs.close();
            }
            ps = super.getPreparedStatement(SQL_TABLESPACE_USED, di.getDatasourceName());
            for (TablespaceInfo ti : di.getTablespaces()) {
                ps.setString(1, ti.getName());
                rs = ps.executeQuery();
                if (rs.next()) {
                    do {
                        ti.setUsedByDBObjects(rs.getInt(1) != 0);
                    } while (rs.next());
                    rs.close();
                }
            }
        } catch (SQLException e) {
            di.setError(new DBException("SQLException while getting tablespaces of database: " + di.getDatasourceName(), e));
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
