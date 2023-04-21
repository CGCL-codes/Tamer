package jreceiver.server.db;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esau.ptarmigan.util.PtarURI;
import jreceiver.common.rec.Rec;
import jreceiver.common.rec.source.Mexternal;
import jreceiver.common.rec.source.MexternalRec;
import jreceiver.common.rec.source.Source;
import jreceiver.server.bus.BusException;
import jreceiver.server.bus.MimeBus;
import jreceiver.server.bus.SourceBus;
import jreceiver.server.util.db.ConnectionPool;
import jreceiver.server.util.db.DatabaseException;
import jreceiver.server.util.db.HelperDB;

/**
 * mexternal table routines for JReceiver database
 *
 * @author Reed Esau
 * @version $Revision: 1.15 $ $Date: 2003/04/27 23:14:30 $
 */
public class MexternalDB extends SourceKeyDB {

    protected static final String BASE_TABLE = "mexternal";

    public String getBaseTableName() {
        return BASE_TABLE;
    }

    /**
    * this class is implemented as a singleton
    */
    private static MexternalDB singleton;

    /**
     * obtain an instance of this singleton
     * <p>
     * Note that this uses the questionable DCL pattern (search on
     * DoubleCheckedLockingIsBroken for more info)
     * <p>
     * @return the singleton instance for this JVM
     */
    public static MexternalDB getInstance() {
        if (singleton == null) {
            synchronized (MexternalDB.class) {
                if (singleton == null) singleton = new MexternalDB();
            }
        }
        return singleton;
    }

    protected static final int KEY_COL_ID = 1;

    protected static final String[] KEY_COLUMNS = { "mexternal.src_id" };

    protected String[] getKeyColumns() {
        return KEY_COLUMNS;
    }

    public Object buildKey(ResultSet rs) throws SQLException {
        return new Integer(rs.getInt(KEY_COL_ID));
    }

    protected static final int COL_SRC_ID = 1;

    protected static final int COL_TITLE = 2;

    protected static final int COL_SRCTYPE = 3;

    protected static final int COL_MIME_ID = 4;

    protected static final int COL_CONTENT_URL = 5;

    protected static final int COL_REMOTE_URL = 6;

    protected static final int COL_CACHE_DATA = 7;

    protected static final String[] REC_COLUMNS = { "source.id", "source.title", "source.src_type", "source.mime_id", "site.content_url", "mexternal.remote_url", "mexternal.cache_data" };

    public String[] getRecColumns(Hashtable args) {
        return REC_COLUMNS;
    }

    protected Rec buildRec(ResultSet rs, Hashtable args) throws SQLException, DatabaseException {
        try {
            MimeBus mime_bus = MimeBus.getInstance();
            boolean cache_data = 0 != rs.getInt(COL_CACHE_DATA);
            int src_id = rs.getInt(COL_SRC_ID);
            String title = rs.getString(COL_TITLE);
            String src_mime = mime_bus.getTypeForId(rs.getInt(COL_MIME_ID));
            String s_direct_url = rs.getString(COL_REMOTE_URL);
            PtarURI direct_uri = null;
            if (s_direct_url != null && s_direct_url.trim().length() > 0) direct_uri = new PtarURI(new URL(s_direct_url));
            if (direct_uri == null) throw new DatabaseException("direct_uri is required");
            URL content_url = null;
            if (args != null && args.get(Source.POPULATE_CONTENT_URL) != null) {
                SourceBus src_bus = SourceBus.getInstance();
                String base_url = rs.getString(COL_CONTENT_URL);
                content_url = src_bus.composeContentURL(base_url, src_id, title, src_mime, null);
            }
            MexternalRec rec = new MexternalRec(src_id, title, rs.getInt(COL_SRCTYPE), src_mime, direct_uri, content_url, cache_data);
            if (log.isDebugEnabled()) log.debug("buildRec: rec=" + rec);
            return rec;
        } catch (MalformedURLException e) {
            throw new DatabaseException("url-problem creating mexternal rec", e);
        } catch (BusException e) {
            throw new DatabaseException("bus-problem creating mexternal rec", e);
        }
    }

    protected static final int ST_COL_REMOTE_URL = 1;

    protected static final int ST_COL_CACHE_DATA = 2;

    protected static final int ST_COL_SRC_ID = 3;

    protected static final String ST_INSERT = "INSERT INTO mexternal (remote_url" + ",cache_data" + ",src_id" + ") VALUES (?,?,?)";

    protected static final String ST_UPDATE = "UPDATE mexternal SET remote_url=?" + ",cache_data=?" + " WHERE src_id=?";

    /**
     * Return a statement to be used in locating an existing record,
     * updating an existing record, or inserting a new record.
     */
    protected String getStoreStatement(int stmt_type) {
        switch(stmt_type) {
            case STORE_FIND:
                return null;
            case STORE_UPDATE:
                return ST_UPDATE;
            case STORE_INSERT:
                return ST_INSERT;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * set params for storeRecs, using update or insert statement
     */
    protected void setStoreParams(int stmt_type, PreparedStatement stmt, Rec rec) throws SQLException {
        Mexternal mexternal = (Mexternal) rec;
        PtarURI remote_uri = mexternal.getDirectURI();
        stmt.setString(ST_COL_REMOTE_URL, (remote_uri != null ? remote_uri.getURL().toExternalForm() : ""));
        stmt.setInt(ST_COL_CACHE_DATA, mexternal.getCacheData() ? 1 : 0);
        stmt.setInt(ST_COL_SRC_ID, mexternal.getSrcId());
    }

    /**
     * given a filename and folder_id, get the associated src_id
     * <p>
     * Use this routine if you have many files in the same folder.
     * Relative pathnames are not supported here.
     *
     * @return src_id if source is present in database; 0 if not present
     */
    protected int getSrcID(Connection conn, URL remote_url) throws DatabaseException {
        if (conn == null || remote_url == null) throw new IllegalArgumentException();
        String sql = "SELECT src_id FROM mexternal WHERE remote_url=?";
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, remote_url.toExternalForm());
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) return 0;
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new DatabaseException("sql-problem getting mexternal src_id", e);
        } finally {
            HelperDB.safeClose(stmt);
        }
    }

    /**
     * given a filename and folder_id, get the associated src_id
     * <p>
     * Use this routine if you have many files in the same folder.
     * Relative pathnames are not supported here.
     *
     * @return src_id if source is present in database; 0 if not present
     */
    public int getSrcID(URL remote_url) throws DatabaseException {
        ConnectionPool pool = null;
        Connection conn = null;
        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();
            return getSrcID(conn, remote_url);
        } finally {
            if (pool != null) pool.releaseConnection(conn);
        }
    }

    /**
     * logging object
     */
    protected static Log log = LogFactory.getLog(MexternalDB.class);
}
