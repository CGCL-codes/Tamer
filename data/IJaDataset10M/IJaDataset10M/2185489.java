package com.apelon.dts.db.admin.table;

import com.apelon.common.log4j.Categories;
import com.apelon.common.util.GidGenerator;
import com.apelon.dts.db.admin.DTSBaseTable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class TableDTS_PROPERTY_TYPE_ARCHIVE extends DTSBaseTable {

    private long targetGID;

    private Set duplicateId;

    private Set duplicateCode;

    private Set duplicateName;

    private boolean first = true;

    public TableDTS_PROPERTY_TYPE_ARCHIVE() throws SQLException {
    }

    protected void buildTable(String tableName) throws SQLException {
        this.tableName = tableName;
        String selectStat = sourceDAO.getStatement(tableName, "SELECT");
        String insertStat = dao.getStatement(tableName, "INSERT");
        duplicateId = new HashSet();
        duplicateCode = new HashSet();
        duplicateName = new HashSet();
        propertyGIDMap = new HashMap(101);
        copyTable(tableName, sourceConn, selectStat, targetConn, insertStat);
    }

    protected void processResult(ResultSet sourceRs, PreparedStatement targetSt) throws SQLException {
        try {
            int id = sourceRs.getInt(1);
            int gid = sourceRs.getInt(2);
            String code = sourceRs.getString(3);
            String name = sourceRs.getString(4);
            String value_size = sourceRs.getString(5);
            String contains_index = sourceRs.getString(6);
            Integer idObject = new Integer(id);
            if (duplicateId.contains(idObject)) {
                logDuplicateInfo(tableName + " there is a duplicate id");
                logDuplicateDebug(tableName + " " + idObject);
                return;
            }
            duplicateId.add(idObject);
            if (duplicateCode.contains(code)) {
                logDuplicateInfo(tableName + " there is a duplicate code");
                logDuplicateDebug(tableName + " " + code);
                return;
            }
            duplicateCode.add(code);
            if (duplicateName.contains(name)) {
                logDuplicateInfo(tableName + " there is a duplicate name");
                logDuplicateDebug(tableName + " " + name);
                return;
            }
            duplicateName.add(name);
            long iid = getGlobalUniqueInstanceIdentifier(nameSpaceId, tableName);
            targetGID = GidGenerator.getGID(nameSpaceId, id);
            targetSt.setLong(1, iid);
            targetSt.setLong(2, versionGId);
            targetSt.setLong(3, getNotRetiredVersionGID());
            targetSt.setLong(4, targetGID);
            targetSt.setInt(5, id);
            targetSt.setString(6, code);
            targetSt.setString(7, name);
            targetSt.setInt(8, nameSpaceId);
            targetSt.setString(9, (String) valueSizeMap.get(value_size));
            targetSt.setString(10, "C");
            targetSt.setString(11, contains_index);
            targetSt.executeUpdate();
            propertyGIDMap.put(new Integer(gid), new Long(targetGID));
        } catch (Exception e) {
            Categories.data().error("Unable to copy data", e);
            throw new SQLException("unable to copy data " + e.getMessage());
        }
    }
}
