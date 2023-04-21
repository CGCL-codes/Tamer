package com.apelon.dts.db.admin.table;

import com.apelon.common.log4j.Categories;
import com.apelon.common.sql.SQL;
import com.apelon.dts.db.admin.DTSBaseTable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableDTS_QUALIFIED_INDEX_CON_PROP extends DTSBaseTable {

    public TableDTS_QUALIFIED_INDEX_CON_PROP() throws SQLException {
    }

    public void createTable(String tableName) throws SQLException {
        createTableWithSequence(tableName);
    }

    protected void createIndex(boolean drop) throws SQLException {
        SQL.createIndex(targetConn, "qual_index_con_prop_iid_idx", "DTS_QUALIFIED_INDEX_CON_PROP(CONCEPT_PROPERTY_IID)", drop);
    }

    protected void dropIndex(String tableName) throws SQLException {
        String statement = dao.getStatement(tableName, "DROP_TABLE_INDEX1");
        dao.dropIndex(targetConn, statement);
    }

    protected void buildTable(String tableName) throws SQLException {
        this.tableName = "DTS_QUALIFIED_INDEX_CON_PROP";
        String selectStat = "select archive_iid, property_qualifier_iid, concept_property_iid, qualifier_gid, " + "qualifier_value from DTS_QUAL_INDEX_CON_PROP_ARCH where ";
        selectStat += " version_in = " + this.versionGId;
        String insertStat = "insert into " + "DTS_QUALIFIED_INDEX_CON_PROP" + " values (?,?,?,?,?)";
        boolean dropConstraintsIndexes = getDropConstraintsIndexes("DTS_QUAL_INDEX_CON_PROP_ARCH", this.tableName, targetConn, targetConn);
        if (dropConstraintsIndexes) {
            enableConstraints(this.tableName, targetConn, false);
            dropIndex(this.tableName);
        }
        copyTable(this.tableName, targetConn, selectStat, targetConn, insertStat);
        if (dropConstraintsIndexes) {
            enableConstraints(this.tableName, targetConn, true);
            createIndex(false);
        }
    }

    protected void processResult(ResultSet sourceRs, PreparedStatement targetSt) throws SQLException {
        try {
            long archive_iid = sourceRs.getLong(1);
            long property_qualifier_iid = sourceRs.getLong(2);
            long concept_property_iid = sourceRs.getLong(3);
            long qualifier_gid = sourceRs.getLong(4);
            String value = sourceRs.getString(5);
            targetSt.setLong(1, property_qualifier_iid);
            targetSt.setLong(2, concept_property_iid);
            targetSt.setLong(3, qualifier_gid);
            targetSt.setString(4, value);
            targetSt.setLong(5, archive_iid);
            targetSt.executeUpdate();
        } catch (Exception e) {
            Categories.data().error("Unable to copy data", e);
            throw new SQLException("unable to copy data " + e.getMessage());
        }
    }
}
