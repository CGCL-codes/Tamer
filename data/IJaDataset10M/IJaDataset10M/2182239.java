package com.cosylab.vdct.rdb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import com.cosylab.vdct.db.DBData;

/**
 * @author ssah
 *
 */
public class RdbDataMapper {

    private RdbConnection helper = null;

    public RdbDataMapper() throws Exception {
        helper = new RdbConnection();
    }

    public void setConnectionParameters(String host, String database, String user, String password) {
        helper.setParameters(host, database, user, password);
    }

    public DBData loadRdbData(Object dsId, RdbDataId dataId) throws Exception {
        DBData data = null;
        Exception exception = null;
        try {
            RdbDbContext context = new RdbDbContext(dsId, dataId, helper);
            data = context.load();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            exception = new Exception("Error while loading database: " + sqlException.getMessage());
        } catch (IllegalArgumentException illegalArgumentException) {
        }
        if (exception != null) {
            throw exception;
        }
        return data;
    }

    public boolean saveRdbData(Object dsId, RdbDataId dataId) throws Exception {
        boolean success = false;
        Exception exception = null;
        try {
            RdbDbContext context = new RdbDbContext(dsId, dataId, helper);
            context.save();
            helper.commit();
            success = true;
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            exception = new Exception("Error while saving database: " + sqlException.getMessage());
            helper.rollbackConnection();
        } catch (IllegalArgumentException illegalArgumentException) {
        }
        if (exception != null) {
            throw exception;
        }
        return success;
    }

    public Connection createNewConnection() throws SQLException {
        return helper.createConnection();
    }

    public boolean isConnection() {
        return helper.isConnection();
    }

    public void closeConnection() throws SQLException {
        helper.closeConnection();
    }

    public int createAnIoc() throws SQLException {
        int iocId = saveIoc();
        helper.commit();
        return iocId;
    }

    /** Returns Vector of String objects representing IOCs.
	 */
    public Vector getIocs() throws SQLException {
        Object[] columns = { "ioc_id" };
        Object[][] conditions = { {}, {} };
        ResultSet set = helper.loadRows("ioc", columns, conditions, null, "ioc_id");
        Vector iocs = new Vector();
        while (set.next()) {
            iocs.add(set.getString(1));
        }
        return iocs;
    }

    /** Returns Vector of String objects representing db files under the given IOC.
	 */
    public Vector getRdbDatas(String iocId) throws SQLException {
        Object[] columns = { "p_db_file_name" };
        Object[][] conditions = { { "ioc_id_FK" }, { iocId } };
        ResultSet set = helper.loadRows("p_db", columns, conditions, null, "p_db_file_name");
        Vector groups = new Vector();
        while (set.next()) {
            groups.add(set.getString(1));
        }
        return groups;
    }

    /** Returns Vector of String objects representing versions of the given group.
	 */
    public Vector getVersions(String group, String iocId) throws SQLException {
        Object[] columns = { "p_db_version" };
        Object[][] conditions = { { "ioc_id_FK", "p_db_file_name" }, { iocId, group } };
        ResultSet set = helper.loadRows("p_db", columns, conditions, null, "p_db_version");
        Vector versions = new Vector();
        while (set.next()) {
            versions.add(set.getString(1));
        }
        return versions;
    }

    public void addRdbDataId(RdbDataId dataId, String desription) throws SQLException {
        Object[][] keyPairs = { { "p_db_file_name", "ioc_id_FK", "p_db_version" }, { dataId.getFileName(), dataId.getIoc(), dataId.getVersion() } };
        Object[][] valuePairs = { { "p_db_desc" }, { desription } };
        helper.saveRow("p_db", keyPairs, valuePairs);
        helper.commit();
    }

    private int saveIoc() throws SQLException {
        Object[][] keyPairs = { {}, {} };
        Object[][] valuePairs = { {}, {} };
        helper.appendRow("ioc", keyPairs, valuePairs);
        Object[] columns = { "ioc_id" };
        ResultSet set = helper.loadRows("ioc", columns, keyPairs);
        if (!set.next()) {
            throw new SQLException("Could not create new ioc.");
        }
        return set.getInt(1);
    }
}
