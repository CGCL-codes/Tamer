package com.apelon.dts.db.admin.table;

import java.sql.*;
import java.util.*;
import com.apelon.dts.db.admin.*;
import com.apelon.common.log4j.Categories;

/**
 * <p>Description: Adds qualifiers to concept associations </p>
 * NOTE: Currently it creates qualifiers on SNOMED associations only...
 *
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Apelon, Inc.</p>
 * @author unascribed
 * @version 1.0
 */
public class TableDTS_QUALIFIED_CON_ASSN_ARCHIVE extends DTSBaseTable {

    protected static final String QUAL_GID = "qualGid";

    protected static final String QUAL_VALUE = "qualValue";

    protected static final String SOURCE_PROPERTY_NAME = "sourcePropertyName";

    protected static final String SOURCE_NAMESPACE_ID = "sourceNamespaceId";

    protected static final String TARGET_NAMESPACE_ID = "targetNamespaceId";

    protected static final String ASSO_NAMESPACE_ID = "assoNamespaceId";

    public TableDTS_QUALIFIED_CON_ASSN_ARCHIVE() throws SQLException {
    }

    protected void buildTable(PreparedStatement ps, Map map) throws SQLException {
        long beg = System.currentTimeMillis();
        long mapVersionId = ((Long) map.get(TableDTS_ASSOCIATION_TYPE.ASSOC_VERSIONID)).longValue();
        long qualGID = ((Long) map.get(QUAL_GID)).longValue();
        String sourcePropertyName = ((String) map.get(SOURCE_PROPERTY_NAME));
        int assoNamespaceId = ((Integer) map.get(ASSO_NAMESPACE_ID)).intValue();
        if (!sourcePropertyName.equals("SNOMED_ICD9_MAP")) return;
        PreparedStatement insertSt = getInsertStatement(targetConn, "insert into DTS_QUALIFIED_CON_ASSN_ARCHIVE values (?,?,?,?,?,?,?)");
        try {
            ResultSet rs = ps.executeQuery();
            int c = 0;
            while (rs.next()) {
                long assnIID = rs.getLong(1);
                long fromConceptGid = rs.getLong(2);
                long archive_iid = getGlobalUniqueInstanceIdentifier(nameSpaceId, tableName);
                long assn_qual_iid = getGlobalUniqueInstanceIdentifier(nameSpaceId, tableName);
                Vector valuesVec = new Vector();
                String val = "";
                String qualValue = "";
                if (TableDTS_CONCEPT_ASSN_ARCHIVE.assoIIDValueMap.containsKey(new Long(assnIID))) {
                    valuesVec = (Vector) TableDTS_CONCEPT_ASSN_ARCHIVE.assoIIDValueMap.get(new Long(assnIID));
                }
                if (valuesVec.size() > 0) {
                    for (int i = 0; i < valuesVec.size(); i++) {
                        val = (String) valuesVec.get(i);
                        qualValue = getQualifierValue(fromConceptGid, val);
                        if (qualValue == null || qualValue.equals("")) {
                            qualValue = "";
                            Categories.dataDb().debug("Qualifier value not found on:" + assnIID + "-" + fromConceptGid);
                        } else {
                            insertSt.setLong(1, archive_iid);
                            insertSt.setLong(2, mapVersionId);
                            insertSt.setLong(3, getNotRetiredVersionGID(assoNamespaceId));
                            insertSt.setLong(4, assn_qual_iid);
                            insertSt.setLong(5, assnIID);
                            insertSt.setLong(6, qualGID);
                            insertSt.setString(7, qualValue);
                            insertSt.executeUpdate();
                        }
                        if ((c % 1000) == 0) {
                            Categories.dataDb().info("TableDTS_QUALIFIED_CON_ASSN_ARCHIVE" + ": result " + c + " " + ((System.currentTimeMillis() - beg) / 1000) + " secs");
                            targetConn.commit();
                        }
                        c++;
                    }
                }
            }
            rs.close();
        } finally {
            insertSt.close();
            if (psQual != null) {
                psQual.close();
            }
        }
    }

    private String getQualifierValue(long fromConceptGid) throws SQLException {
        String qualValue = "";
        String query = "select value from " + propertyTypeTable + " where property_gid = " + getMappingCateogryGid() + " and concept_gid = ?";
        if (psQual == null) {
            psQual = targetConn.prepareStatement(query);
        }
        psQual.setLong(1, fromConceptGid);
        ResultSet rs = psQual.executeQuery();
        while (rs.next()) {
            String mapCode = rs.getString(1);
            qualValue = getMappingValue(mapCode);
        }
        rs.close();
        return qualValue;
    }

    private String getQualifierValue(long fromConceptGid, String value) throws SQLException {
        String qualValue = "";
        String valCode = value + "|" + "%";
        String query = "select value from " + propertyTypeTable + " where property_gid = " + getMappingCateogryGid() + " and concept_gid = ? and value like ?";
        if (psQual == null) {
            psQual = targetConn.prepareStatement(query);
        }
        psQual.setLong(1, fromConceptGid);
        psQual.setString(2, valCode);
        ResultSet rs = psQual.executeQuery();
        while (rs.next()) {
            String mapCode = rs.getString(1);
            qualValue = getMappingValue(mapCode);
        }
        rs.close();
        return qualValue;
    }

    private PreparedStatement getDeletePropStmt(String tableName) throws SQLException {
        PreparedStatement deletePropSt = targetConn.prepareStatement("delete from " + tableName + " where concept_gid = ? and property_gid = ?");
        return deletePropSt;
    }

    private PreparedStatement getDeletePropArchStmt(String tableName) throws SQLException {
        PreparedStatement deletePropSt = null;
        deletePropSt = targetConn.prepareStatement("delete from " + tableName + " where concept_gid = ? and property_gid = ?");
        return deletePropSt;
    }

    private long getMappingCateogryGid() throws SQLException {
        if (mappingCategoryGid <= 0) mappingCategoryGid = getSingleLong(targetConn, "DTS_PROPERTY_TYPE", "type_gid", "name", "'SNOMED_ICD9_MAP_CATEGORIZED'");
        return mappingCategoryGid;
    }

    private String getMappingValue(String mapCode) {
        String value = "undefined";
        if (mapCode.endsWith("|0")) {
            value = (String) snomedIcdMap.get("0");
        } else if (mapCode.endsWith("|1")) {
            value = (String) snomedIcdMap.get("1");
        } else if (mapCode.endsWith("|2")) {
            value = (String) snomedIcdMap.get("2");
        } else if (mapCode.endsWith("|3")) {
            value = (String) snomedIcdMap.get("3");
        } else if (mapCode.endsWith("|4")) {
            value = (String) snomedIcdMap.get("4");
        }
        return value;
    }

    private String propertyTypeTable = "DTS_INDEXABLE_CONCEPT_PROPERTY";

    private long mappingCategoryGid = 0;

    private PreparedStatement psQual;
}
