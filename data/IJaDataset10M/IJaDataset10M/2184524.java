package org.ashkelon.manager;

import java.sql.*;
import java.util.*;
import org.ashkelon.API;
import org.ashkelon.APISet;
import org.ashkelon.JPackage;
import org.ashkelon.db.*;
import org.ashkelon.util.*;
import org.jibx.runtime.JiBXException;
import com.sun.javadoc.*;

/**
 * @author Eitan Suez
 */
public class Ashkelon extends Doclet {

    private RootDoc root = null;

    private DBMgr dbmgr;

    private Connection conn;

    private Logger log;

    private PKManager pkmgr;

    private DBProc proc;

    /** required for Doclet inheritance */
    public static boolean start(RootDoc root) {
        Ashkelon ashkelonDoclet = new Ashkelon(root);
        if (!ashkelonDoclet.init()) return false;
        ashkelonDoclet.doAdd();
        return ashkelonDoclet.finish();
    }

    public Ashkelon() {
        log = Logger.getInstance();
        log.setPrefix("Ashkelon");
    }

    public Ashkelon(RootDoc root) {
        this();
        this.root = root;
    }

    public boolean init() {
        dbmgr = DBMgr.getInstance();
        conn = null;
        try {
            conn = dbmgr.getConnection();
        } catch (SQLException ex) {
            DBUtils.logSQLException(ex);
            if (conn != null) dbmgr.releaseConnection(conn);
            return false;
        }
        pkmgr = PKManager.getInstance();
        proc = new DBProc();
        return true;
    }

    public boolean finish() {
        dbmgr.releaseConnection(conn);
        pkmgr.save();
        return true;
    }

    protected void finalize() throws Throwable {
        root = null;
        if (conn != null) dbmgr.releaseConnection(conn);
        conn = null;
        dbmgr = null;
        log = null;
        pkmgr = null;
    }

    private void doAdd() {
        long start = new java.util.Date().getTime();
        boolean refsonly = Options.getCommandLineOption("-refsonly", root.options());
        boolean norefs = Options.getCommandLineOption("-norefs", root.options());
        if (!refsonly) {
            int apiId = Options.getCommandLineOption("-api", root.options(), -1);
            if (apiId == -1) {
                log.error("Failed to resolve api in db");
                return;
            }
            try {
                API api = API.makeAPIFor(conn, apiId);
                if (api == null || root.specifiedPackages().length == 0) {
                    log.error("No api to add (or 0 packages)..exiting.");
                    return;
                }
                populateAPI(api);
            } catch (SQLException ex) {
                log.error("Failed to load api (id " + apiId + ") from db..");
                log.error("SQLException: " + ex.getMessage());
                return;
            }
        }
        long addtime = new java.util.Date().getTime() - start;
        log.traceln("Add Time: " + addtime / 1000 + " seconds");
        if (!norefs) {
            updateInternalRefs();
        }
        long reftime = new java.util.Date().getTime() - start - addtime;
        log.traceln("Ref. Time: " + reftime / 1000 + " seconds");
        try {
            conn.commit();
        } catch (SQLException ex) {
            log.error("jdbc commit failed");
            DBUtils.logSQLException(ex);
        }
        log.traceln("done");
    }

    private void populateAPI(API api) {
        try {
            if (api.isPopulated()) {
                log.traceln("Skipping API " + api.getName() + " (already populated)");
                return;
            }
            api.setPopulated(true);
            String sql = "update API set populated=1 where name=?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, api.getName());
            pstmt.executeUpdate();
            pstmt.close();
            storePackagesAndClasses(api);
            conn.commit();
        } catch (SQLException ex) {
            log.error("Store (api: " + api.getName() + ") failed!");
            DBUtils.logSQLException(ex);
            log.error("Rolling back..");
            try {
                conn.rollback();
            } catch (SQLException inner_ex) {
                log.error("rollback failed!");
            }
        }
    }

    private void storePackagesAndClasses(API api) throws SQLException {
        delIndices();
        PackageDoc[] packages = root.specifiedPackages();
        log.traceln(packages.length + " packages to process..");
        for (int i = 0; i < packages.length; i++) {
            log.traceln("Processing package " + packages[i].name() + "..");
            new JPackage(packages[i], true, api).store(conn);
            log.verbose("Package: " + packages[i].name() + " stored");
        }
    }

    private void addIndices() {
        try {
            proc.doAction(conn, "add_idx");
        } catch (SQLException ex) {
            log.verbose("no add/remove index optimizations during population for current db");
        }
    }

    private void delIndices() {
        try {
            proc.doAction(conn, "del_idx");
        } catch (SQLException ex) {
            log.verbose("no add/remove index optimizations during population for current db");
        }
    }

    public void updateInternalRefs() {
        log.traceln("Updating Internal References..");
        addIndices();
        new RefManager(conn).setInternalReferences();
        try {
            new AncestorPopulator();
        } catch (SQLException ex) {
            log.error("Failed to populate class ancestors table");
            DBUtils.logSQLException(ex);
        }
    }

    public void doRemove(String apiname, boolean withApiRecord) {
        try {
            API api = API.makeAPIFor(conn, apiname);
            if (api == null) {
                log.error("No api named " + apiname + " found in db");
                return;
            }
            api.delete(conn, withApiRecord);
            conn.commit();
        } catch (Exception ex) {
            log.error("Exception: " + ex.getMessage());
        }
        log.traceln("remove done");
    }

    private void callStoredProc(String action) throws SQLException {
        String sql = "{call db_proc(?)}";
        CallableStatement cstmt = conn.prepareCall(sql);
        cstmt.setString(1, action);
        cstmt.executeUpdate();
        cstmt.close();
        conn.commit();
    }

    /** required for Doclet inheritance */
    public static int optionLength(String option) {
        if (option.equals("-api")) return 2;
        return 0;
    }

    public void reset() throws SQLException {
        proc.doAction(conn, "reset");
    }

    public List listAPINames(boolean pending) throws SQLException {
        int populatedVal = (pending) ? 0 : 1;
        String sql = "select name from API where populated = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, populatedVal);
        ResultSet rset = pstmt.executeQuery();
        List names = new ArrayList();
        String name;
        while (rset.next()) {
            name = rset.getString(1);
            names.add(name);
        }
        return names;
    }

    public void dumpAPISet() throws SQLException, JiBXException {
        new APISet().dump(conn);
    }

    private void addSequences() {
        try {
            String seqs[] = { "PKG_SEQ", "CLASSTYPE_SEQ", "AUTHOR_SEQ", "DOC_SEQ", "MEMBER_SEQ" };
            for (int i = 0; i < seqs.length; i++) {
                pkmgr.addSequence(conn, seqs[i], 100);
            }
            conn.commit();
            log.traceln("added (committed) ashkelon sequences to db");
        } catch (SQLException ex) {
            DBUtils.logSQLException(ex);
            log.error("Rolling back..");
            try {
                conn.rollback();
            } catch (SQLException inner_ex) {
                log.error("rollback failed!");
            }
        }
    }
}
