package com.apelon.dts.db.admin.table;

import com.apelon.common.log4j.Categories;
import com.apelon.common.sql.SQL;
import com.apelon.dts.db.admin.DTSBaseTable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TableDTS_COMPLETE_ROLE_CON_ARCHIVE extends DTSBaseTable {

    private HashSet duplicate;

    public TableDTS_COMPLETE_ROLE_CON_ARCHIVE() throws SQLException {
    }

    protected void createIndex(boolean drop) throws SQLException {
        SQL.createIndex(targetConn, "dts_cp_role_con_arch_cid_idx", "DTS_COMPLETE_ROLE_CON_ARCHIVE(CONCEPT_GID)", drop);
        SQL.createIndex(targetConn, "dts_cp_role_con_arch_role_idx", "DTS_COMPLETE_ROLE_CON_ARCHIVE(ROLE_GID)", drop);
        SQL.createIndex(targetConn, "dts_cp_role_con_arch_vcid_idx", "DTS_COMPLETE_ROLE_CON_ARCHIVE(VALUE_CONCEPT_GID)", drop);
    }

    protected void dropIndex(String tableName) throws SQLException {
        String statement = dao.getStatement(tableName, "DROP_CID_INDEX");
        dao.dropIndex(targetConn, statement);
        statement = dao.getStatement(tableName, "DROP_ROLE_INDEX");
        dao.dropIndex(targetConn, statement);
        statement = dao.getStatement(tableName, "DROP_VCID_INDEX");
        dao.dropIndex(targetConn, statement);
    }

    protected void buildTable(String tableName) throws SQLException {
        this.tableName = tableName;
        Map values = new HashMap();
        duplicate = new HashSet();
        values.put(new Integer(4), String.valueOf(versionGId));
        String selectStat = "select con, role, modifier, val, rolegroup from COMPLETE_ROLE_CON";
        String insertStat = "insert into DTS_COMPLETE_ROLE_CON_ARCHIVE values (?,?,?,?,?,?,?,?,?)";
        boolean dropConstraintsIndexes = getDropConstraintsIndexes("COMPLETE_ROLE_CON", tableName, sourceConn, targetConn);
        if (dropConstraintsIndexes) {
            enableConstraints(tableName, targetConn, false);
            dropIndex(tableName);
        }
        copyTable(tableName, sourceConn, selectStat, targetConn, insertStat);
        if (dropConstraintsIndexes) {
            enableConstraints(tableName, targetConn, true);
            createIndex(false);
        }
    }

    protected void processResult(ResultSet sourceRs, PreparedStatement targetSt) throws SQLException {
        try {
            long iid = getGlobalUniqueInstanceIdentifier(nameSpaceId, tableName);
            long role_iid = getGlobalUniqueInstanceIdentifier(nameSpaceId, tableName);
            int cGID = sourceRs.getInt(1);
            if (sourceRs.wasNull()) return;
            int rGID = sourceRs.getInt(2);
            if (sourceRs.wasNull()) return;
            int modifier = sourceRs.getInt(3);
            if (sourceRs.wasNull()) return;
            int vCGID = sourceRs.getInt(4);
            if (sourceRs.wasNull()) return;
            int roleGroup = sourceRs.getInt(5);
            long tcGID = -1;
            Long cgid = (Long) conceptGIDMap.get(new Integer(cGID));
            if (cgid != null) tcGID = cgid.longValue();
            long trGID = -1;
            Long rgid = (Long) roleGIDMap.get(new Integer(rGID));
            if (rgid != null) trGID = rgid.longValue();
            long tvCGID = -1;
            Long vgid = (Long) conceptGIDMap.get(new Integer(vCGID));
            if (vgid != null) tvCGID = vgid.longValue();
            if (tcGID >= 1 && trGID >= 1 && tvCGID >= 1) {
                targetSt.setLong(1, iid);
                targetSt.setLong(2, versionGId);
                targetSt.setLong(3, getNotRetiredVersionGID());
                targetSt.setLong(4, role_iid);
                targetSt.setLong(5, tcGID);
                targetSt.setLong(6, trGID);
                targetSt.setLong(7, tvCGID);
                targetSt.setInt(8, modifier);
                targetSt.setInt(9, roleGroup);
                targetSt.executeUpdate();
            } else Categories.dataDb().debug("This entry not added to db concept_gid=" + cGID + ", role_gid=" + rGID + ", value_concept_gid=" + vCGID);
        } catch (Exception e) {
            Categories.data().error("Unable to copy data", e);
            throw new SQLException("unable to copy data " + e.getMessage());
        }
    }
}
