package com.apelon.dts.db.admin.table;

import com.apelon.common.log4j.Categories;
import com.apelon.common.sql.SQL;
import com.apelon.dts.db.admin.DTSBaseTable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TableTRANSFER_LINK extends DTSBaseTable {

    private long beg;

    protected static List mappingList;

    public TableTRANSFER_LINK() throws SQLException {
    }

    protected void buildTable(String tableName) throws SQLException {
        TableDTS_ASSOCIATION_TYPE_ARCHIVE asscArchTable = new TableDTS_ASSOCIATION_TYPE_ARCHIVE();
        asscArchTable.buildTableFromLink("DTS_ASSOCIATION_TYPE_ARCHIVE");
        if (!SQL.checkTableExists(sourceConn, "dl_link_def")) {
            return;
        }
        String selectStat = "select LINK_DEF_ID, FROM_CON_GID, TO_CON_GID " + "from DL_LINK_CONCEPT";
        String insertStat = "insert into DTS_CONCEPT_ASSN_ARCHIVE values (?,?,?,?,?,?,?)";
        copyTable("DTS_CONCEPT_ASSN_ARCHIVE", sourceConn, selectStat, targetConn, insertStat);
        TableDTS_ASSOCIATION_TYPE asscTable = new TableDTS_ASSOCIATION_TYPE();
        asscTable.buildTableFromLink("DTS_ASSOCIATION_TYPE");
    }

    protected void processResult(ResultSet sourceRs, PreparedStatement targetSt) throws SQLException {
        try {
            int linkDefId = sourceRs.getInt(1);
            int fromConceptId = sourceRs.getInt(2);
            int toConGid = sourceRs.getInt(3);
            Long lFrom = (Long) conceptGIDMap.get(new Integer(fromConceptId));
            long tFromConceptId = -1;
            if (lFrom != null) tFromConceptId = lFrom.longValue();
            Long lTo = ((Long) conceptGIDMap.get(new Integer(toConGid)));
            long tToConceptId = -1;
            if (lTo != null) tToConceptId = lTo.longValue();
            Long ll = (Long) associationGIDMap.get(new Integer(linkDefId));
            long tlinkDefId = -1;
            if (ll != null) tlinkDefId = ll.longValue();
            long archive_iid = getGlobalUniqueInstanceIdentifier(nameSpaceId, tableName);
            long association_iid = getGlobalUniqueInstanceIdentifier(nameSpaceId, tableName);
            if ((tFromConceptId >= 0) && (tToConceptId >= 0) && (tlinkDefId >= 0)) {
                targetSt.setLong(1, archive_iid);
                targetSt.setLong(2, versionGId);
                targetSt.setLong(3, getNotRetiredVersionGID());
                targetSt.setLong(4, association_iid);
                targetSt.setLong(5, tFromConceptId);
                targetSt.setLong(6, tlinkDefId);
                targetSt.setLong(7, tToConceptId);
                targetSt.executeUpdate();
            }
        } catch (Exception e) {
            Categories.data().error("Unable to copy data", e);
            throw new SQLException("unable to copy data " + e.getMessage());
        }
    }
}
