package com.apelon.dts.db.admin.table;

import java.io.*;
import java.sql.*;
import java.util.*;
import com.apelon.common.sql.*;
import com.apelon.common.util.db.dao.*;
import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.LogConfigLoader;
import com.apelon.dts.db.admin.*;

public class TableDTS_SYNONYM_CLASS_ARCHIVE extends DTSBaseTable {

    protected static final String CONCEPT_GID = "CONCEPT_GID";

    protected static final String TERM_GID = "TERM_GID";

    protected static final String ASSOCIATION_GID = "ASSOCIATION_GID";

    protected static final String PREFERRED_TERM_FLAG = "PREFERRED_TERM_FLAG";

    protected static final String VERSION_IN = "VERSION_IN";

    protected static final String VERSION_OUT = "VERSION_OUT";

    protected static final String ARCHIVE_IID = "ARCHIVE_IID";

    private String[] columns = { CONCEPT_GID, ASSOCIATION_GID, TERM_GID, PREFERRED_TERM_FLAG, VERSION_IN, VERSION_OUT, ARCHIVE_IID };

    public TableDTS_SYNONYM_CLASS_ARCHIVE() throws SQLException {
        this.tableName = "DTS_SYNONYM_CLASS_ARCHIVE";
    }

    protected PreparedStatement getInsertStatement() throws SQLException {
        Map synValues = new HashMap();
        PreparedStatement synSt = getInsertStatement("DTS_SYNONYM_CLASS_ARCHIVE", columns, synValues);
        return synSt;
    }

    protected void dropIndex(String tableName) throws SQLException {
        this.dropIndexAll(tableName);
    }

    public static void dropIndexAll(String tableName) throws SQLException {
        String statement = dao.getStatement(tableName, "DROP_TABLE_INDEX1");
        dao.dropIndex(targetConn, statement);
        statement = dao.getStatement(tableName, "DROP_TABLE_INDEX2");
        dao.dropIndex(targetConn, statement);
        statement = dao.getStatement(tableName, "DROP_TABLE_INDEX3");
        dao.dropIndex(targetConn, statement);
    }

    protected void createIndex(boolean drop) throws SQLException {
        this.createIndex();
    }

    public static void createIndex() throws SQLException {
        SQL.createIndex(targetConn, "SYN_CLASS_ARCH_ASSN_GID_IDX", "DTS_SYNONYM_CLASS_ARCHIVE(ASSOCIATION_GID)", true);
        SQL.createIndex(targetConn, "SYN_CLASS_ARCH_CON_GID_IDX", "DTS_SYNONYM_CLASS_ARCHIVE(CONCEPT_GID)", true);
        SQL.createIndex(targetConn, "SYN_CLASS_ARCH_TERM_GID_IDX", "DTS_SYNONYM_CLASS_ARCHIVE(TERM_GID)", true);
    }

    protected void buildTable(PreparedStatement insertSt, Map map) throws SQLException {
        long conceptGID = ((Long) map.get(CONCEPT_GID)).longValue();
        long asscGID = ((Long) map.get(TableDTS_ASSOCIATION_TYPE.ASSOCIATION_GID)).longValue();
        long termGID = ((Long) map.get(TERM_GID)).longValue();
        long synonymVersionId = ((Long) map.get(TableDTS_ASSOCIATION_TYPE.ASSOC_VERSIONID)).longValue();
        String preferredFlag = (String) map.get(PREFERRED_TERM_FLAG);
        Boolean bool = Boolean.valueOf(preferredFlag);
        String flag = (bool.booleanValue() ? "T" : "F");
        long archive_iid = getGlobalUniqueInstanceIdentifier(nameSpaceId, tableName);
        insertSt.setLong(1, conceptGID);
        insertSt.setLong(2, asscGID);
        insertSt.setLong(3, termGID);
        insertSt.setString(4, flag);
        insertSt.setLong(5, synonymVersionId);
        insertSt.setLong(6, getNotRetiredVersionGID());
        insertSt.setLong(7, archive_iid);
        insertSt.executeUpdate();
    }
}
