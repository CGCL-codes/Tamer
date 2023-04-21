package com.apelon.dts.db.admin.table;

import java.io.*;
import java.sql.*;
import java.util.*;
import com.apelon.common.sql.*;
import com.apelon.common.util.*;
import com.apelon.common.util.db.dao.*;
import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.LogConfigLoader;
import com.apelon.dts.db.admin.*;

public class TableDTS_BIG_CONCEPT_PROPERTY extends DTSBaseTable {

    public TableDTS_BIG_CONCEPT_PROPERTY() throws SQLException {
    }

    public void createTable(String tableName) throws SQLException {
        createTableWithSequence(tableName);
    }

    protected void createIndex(boolean drop) throws SQLException {
        SQL.createIndex(targetConn, "dts_big_prop_cgid_idx", "DTS_Big_Concept_Property(CONCEPT_GID)", drop);
    }

    protected void dropIndex(String tableName) throws SQLException {
        String statement = dao.getStatement(tableName, "DROP_CGID_INDEX");
        dao.dropIndex(targetConn, statement);
    }

    protected String getExportStatement(String tableName) {
        long[] limits = GidGenerator.getNamespaceMaxAndMinGID(nameSpaceId, localNamespaceFlag);
        long minVal = limits[0];
        long maxVal = limits[1];
        return "SELECT * FROM " + tableName + " WHERE ( CONCEPT_GID >= " + minVal + ") AND (CONCEPT_GID <= " + maxVal + ")";
    }

    protected void buildTable(String tableName) throws SQLException {
        this.tableName = tableName;
        String selectStat = "SELECT archive_iid, concept_gid, property_gid, value " + " FROM DTS_BIG_CON_PROP_ARCH" + " where " + " version_in = " + this.versionGId;
        String insertStat = dao.getStatement(tableName, "INSERT");
        boolean dropConstraintsIndexes = getDropConstraintsIndexes("DTS_BIG_CON_PROP_ARCH", "DTS_Big_Concept_Property", targetConn, targetConn);
        if (dropConstraintsIndexes) {
            enableConstraints(tableName, targetConn, false);
            dropIndex(tableName);
        }
        copyTable("DTS_Big_Concept_Property", targetConn, selectStat, targetConn, insertStat);
        if (dropConstraintsIndexes) {
            enableConstraints(tableName, targetConn, true);
            createIndex(false);
        }
    }

    protected void processResult(ResultSet sourceRs, PreparedStatement targetSt) throws SQLException {
    }
}
