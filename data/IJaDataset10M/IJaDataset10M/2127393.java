package com.apelon.dts.db;

import com.apelon.common.log4j.Categories;
import com.apelon.common.sql.SQL;
import com.apelon.common.util.db.dao.GeneralDAO;
import com.apelon.dts.db.config.DTSClassifyConfig;
import com.apelon.dts.db.dao.DTSClassifyGeneralDAO;
import com.apelon.dts.db.dao.DTSDAOFactory;
import com.apelon.dts.db.dao.DTSGeneralDAO;
import com.apelon.dts.server.DTSPermission;
import com.apelon.dts.server.PermissionException;
import com.apelon.dts.util.MCServerConnection;
import com.apelon.modularclassifier.CycleException;
import com.apelon.modularclassifier.EquivalencyException;
import com.apelon.modularclassifier.UnExpectedException;
import com.apelon.modularclassifier.client.MCProxy;
import com.apelon.mc.types.*;
import java.net.ConnectException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Handles all classification processes for a local namespace
 * such as adding inferred concepts and roles.
 * Once the db layer receives the request from a client,
 * it builds the request provided by modular classifier.
 * Once the modular classifier sends back the response, the db layer
 * will write the result to the database for the persistence.
 *
 * @since DTS 3.0
 *
 * Copyright (c) 2006 Apelon, Inc. All rights reserved.
 */
public class ClassifyDb extends BasicDb {

    private static final String TABLE_KEY = "CLASSIFY_DB";

    private static DTSClassifyConfig dbConfig;

    private MCProxy proxy;

    private static final int CYCLE_ERROR_CODE = 1000000;

    private static final int EQ_ERROR_CODE = 1000001;

    private static final Map ROLE_MODE_MAP = new HashMap();

    private static final Map ROLE_MODE_INVERSE_MAP = new HashMap();

    private static final Map REF_BY_MAP = new HashMap();

    private static final String CONCEPT = "CONCEPT";

    private static final String ROLE = "ROLE";

    private static String REF_BY_KEY = "REF_BY";

    private static String REF_BY = "NAME";

    private static RefbyEnum.Enum REF_BY_NUM = RefbyEnum.NAME;

    private Map gidMap = new HashMap();

    private static final boolean DEBUG_CYCLE_ERROR = false;

    private static final boolean DEBUG_SKIP_CLASSIFY = false;

    static {
        dbConfig = new DTSClassifyConfig();
        ROLE_MODE_MAP.put(new Integer(1), RoleModifierEnum.ALL);
        ROLE_MODE_MAP.put(new Integer(2), RoleModifierEnum.SOME);
        ROLE_MODE_MAP.put(new Integer(3), RoleModifierEnum.POSS);
        ROLE_MODE_MAP.put(RoleModifierEnum.ALL, new Integer(1));
        ROLE_MODE_MAP.put(RoleModifierEnum.SOME, new Integer(2));
        ROLE_MODE_MAP.put(RoleModifierEnum.POSS, new Integer(3));
        REF_BY_MAP.put("NAME", RefbyEnum.NAME);
        REF_BY_MAP.put("CODE", RefbyEnum.CODE);
        REF_BY_MAP.put("ID", RefbyEnum.ID);
    }

    private GeneralDAO classifyDAO;

    /**
 * Statement that is reused by various methods. 
 */
    protected Statement keepAliveStmt = null;

    private static Map urlMap;

    private static final String MODE = "NAME";

    private static final String MAX_CONCEPTS = "MAX_CONCEPTS";

    private static int DEFAULT_MAX_CONCEPTS = 1000000;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    private static String rtcPath;

    /**
  * Constructor that takes a database connection object and the path to the
  * classification properties file.
  *
  * @param conn a java.sql.Connection database connection object to access the DTS schema
  * @param rtcPath a String indicating the path to access the run time classification properties file.
  *
  * @since DTS 3.0
  */
    public ClassifyDb(Connection conn, String rtcPath) throws SQLException {
        super(conn);
        init(rtcPath);
    }

    private static synchronized void loadMCProperties(Map map) {
        if (urlMap != null) {
            return;
        }
        urlMap = map;
        String maxConcepts = getMCProperty(MAX_CONCEPTS);
        if (maxConcepts != null) {
            DEFAULT_MAX_CONCEPTS = Integer.parseInt(maxConcepts);
        }
        String mode = getMCProperty(REF_BY_KEY);
        if (mode != null) {
            REF_BY = mode;
            REF_BY_NUM = (RefbyEnum.Enum) REF_BY_MAP.get(REF_BY);
        }
    }

    private static String getMCProperty(String key) {
        return (String) urlMap.get(key.toLowerCase());
    }

    public static void loadDAO(String type) {
        DTSDAOFactory.getDAOFactory(type, dbConfig);
    }

    public void close() throws SQLException {
        super.close();
        if (keepAliveStmt != null) {
            keepAliveStmt.close();
            keepAliveStmt = null;
        }
    }

    private void init(String path) throws SQLException {
        rtcPath = path;
        loadMCProperties(MCServerConnection.getConnectionMap(path));
        getClassifyDAO(this.fSqlTarget);
        keepAliveStmt = conn.createStatement();
        proxy = new MCProxy();
    }

    private void getClassifyDAO(String type) {
        classifyDAO = DTSDAOFactory.getDAOFactory(type, dbConfig);
    }

    /**
  * Gets the GeneralDAO object specific to classification which
  * holds a collection of SQL statements retrieved from an xml file.
  *
  * @since DTS 3.0.0
  */
    public GeneralDAO getClassifyDAO() {
        return classifyDAO;
    }

    /**
  * Gets the DTS DAO object cast from a GeneralDAO and
  * specific to classification.
  *
  * @return a DTSGeneralDAO cast from a GeneralDAO
  *
  * @since DTS 3.0.0
  */
    public DTSGeneralDAO getDTSClassifyDAO() {
        return (DTSGeneralDAO) classifyDAO;
    }

    /**
  * Classifies a given namespace if the user has write permission.
  *
  * @param namespaceId The id of the namespace to classify.
  * @param permission DTSPermission object holding user name and namespaces with write permission.
  *
  * @return a boolean indicating if the classification was successful.
  *
  * @since DTS 3.0
  */
    public boolean classifyByNamespaceId(int namespaceId, DTSPermission permission) throws SQLException {
        ExtNamespaceEntry extNamespace = fetchExtNamespaceEntry(namespaceId);
        if (extNamespace == null) {
            throw new SQLException("Namespace [ ID=" + namespaceId + " ] is not found.");
        }
        if (extNamespace.baseName == null) {
            throw new SQLException("Namespace [ " + extNamespace.name + " ] is not an Ontylog Extension Namespace");
        }
        if (!canClassifyNamespace(extNamespace, permission)) {
            return false;
        }
        String url = getMCServerUrl(extNamespace);
        gidMap.clear();
        List requests = buildMCRequest(extNamespace);
        if (requests.size() == 0) {
            return true;
        }
        List resultConcepts = sendMCRequest(requests, extNamespace, url);
        processMCResult(resultConcepts, namespaceId);
        return true;
    }

    private boolean canClassifyNamespace(ExtNamespaceEntry extNamespace, DTSPermission permission) throws SQLException {
        try {
            checkPermission(permission, String.valueOf(extNamespace.id));
        } catch (PermissionException e) {
            throw new SQLException("You do not have permission to classify namespace [ " + extNamespace.name + " ]");
        }
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "GET_CLASSIFY_MONITOR");
        sql += extNamespace.id;
        ResultSet rs = null;
        try {
            rs = keepAliveStmt.executeQuery(sql);
            if (rs.next()) {
                String status = rs.getString(1);
                return (status.equals("Y") ? Boolean.valueOf("TRUE").booleanValue() : Boolean.valueOf("FALSE").booleanValue());
            }
            throw new SQLException("No classification monitor entry found for namespace - " + extNamespace.name);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private List buildMCRequest(ExtNamespaceEntry extNamespace) throws SQLException {
        ResultSet rs = null;
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "GET_CONCEPT_" + REF_BY);
        sql = getClassifyDAO().getStatement(sql, 1, String.valueOf(extNamespace.id));
        sql = getClassifyDAO().getStatement(sql, 2, String.valueOf(extNamespace.baseId));
        String defConceptSql = getClassifyDAO().getStatement(TABLE_KEY, "GET_DEFINING_CONCEPT_" + REF_BY);
        PreparedStatement defineConceptStmt = conn.prepareStatement(defConceptSql);
        String defRoleSql = getClassifyDAO().getStatement(TABLE_KEY, "GET_DEFINING_ROLE_" + REF_BY);
        PreparedStatement defineRoleStmt = conn.prepareStatement(defRoleSql);
        try {
            long start = System.currentTimeMillis();
            rs = keepAliveStmt.executeQuery(sql);
            long end = System.currentTimeMillis();
            List concepts = new ArrayList();
            while (rs.next()) {
                Concept con = Concept.Factory.newInstance();
                String conceptValueByRef = rs.getString(1);
                String kindName = rs.getString(2);
                long conceptGID = rs.getLong(3);
                String primitive = rs.getString(4);
                boolean bPrimitive = (primitive.equals("T")) ? true : false;
                con.setIdentifier(conceptValueByRef);
                con.setKind(kindName);
                con.setPrimitive(bPrimitive);
                gidMap.put(conceptValueByRef, new Long(conceptGID));
                getDefiningConcepts(con, conceptGID, extNamespace.id, defineConceptStmt);
                getDefiningRoles(con, conceptGID, extNamespace.id, defineRoleStmt);
                concepts.add(con);
            }
            return concepts;
        } finally {
            closeDBResources(new ResultSet[] { rs }, new Statement[] { defineConceptStmt, defineRoleStmt });
        }
    }

    private ExtensionRoleType[] fetchExtensionRoleTypes(int namespaceId) throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "GET_ROLE_TYPE_" + REF_BY);
        sql = getClassifyDAO().getStatement(sql, 1, String.valueOf(namespaceId));
        ResultSet rset = keepAliveStmt.executeQuery(sql);
        ArrayList rtList = new ArrayList();
        while (rset.next()) {
            ExtensionRoleType rt = ExtensionRoleType.Factory.newInstance();
            rt.setIdentifier(rset.getString(1));
            rt.setNamespaceId(String.valueOf(namespaceId));
            rt.setDomainKind(rset.getString(2));
            rt.setRangeKind(rset.getString(3));
            String rightId = rset.getString(4);
            if (rightId != null) {
                RoleRef roleRef = RoleRef.Factory.newInstance();
                roleRef.setIdentifier(rightId);
                roleRef.setNamespaceId(rset.getString(5));
                rt.setRightId(roleRef);
            }
            String parent = rset.getString(6);
            if (parent != null) {
                RoleRef roleRef = RoleRef.Factory.newInstance();
                roleRef.setIdentifier(parent);
                roleRef.setNamespaceId(rset.getString(7));
                rt.setParentRole(roleRef);
            }
            rtList.add(rt);
        }
        rset.close();
        return (ExtensionRoleType[]) rtList.toArray(new ExtensionRoleType[0]);
    }

    private List sendMCRequest(List concepts, ExtNamespaceEntry extNamespace, String url) throws SQLException {
        if (concepts.size() > DEFAULT_MAX_CONCEPTS) {
            throw new SQLException("The number of concepts in " + extNamespace.name + " should be less than or equal to " + DEFAULT_MAX_CONCEPTS);
        }
        try {
            ClassifierSpaceDocument cct = ClassifierSpaceDocument.Factory.newInstance();
            ClassifierSpace cs = cct.addNewClassifierSpace();
            Concept[] cons = new Concept[concepts.size()];
            for (int i = 0; i < concepts.size(); i++) {
                cons[i] = (Concept) concepts.get(i);
            }
            cs.setConceptArray(cons);
            cs.setExtensionNamespaceId(String.valueOf(extNamespace.id));
            cs.setExtensionRoleTypeArray(fetchExtensionRoleTypes(extNamespace.id));
            cs.setNamespaceId(String.valueOf(extNamespace.baseId));
            cs.setRefby(REF_BY_NUM);
            String version = this.getVersionName(extNamespace.baseId);
            cs.setVersion(version);
            proxy.setServiceURL(url);
            cct = proxy.classifyConcepts(cct);
            return Arrays.asList(cct.getClassifierSpace().getConceptArray());
        } catch (CycleException e) {
            String errorMsg = addCycleError(e, extNamespace.id);
            throw toSQLException(e, errorMsg, CYCLE_ERROR_CODE);
        } catch (EquivalencyException e) {
            String errorMsg = addEqError(e, extNamespace.id);
            throw toSQLException(e, errorMsg, EQ_ERROR_CODE);
        } catch (ConnectException e) {
            Categories.dataServer().error("Classifier could not connect to Web server" + " at " + url);
            throw toSQLException(e, "Classifier could not connect to Web server" + " at " + url);
        } catch (UnExpectedException e) {
            throw toSQLException(e);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw toRuntimeException(e);
        }
    }

    private String getMCServerUrl(ExtNamespaceEntry extNamespace) throws SQLException {
        String url = getMCProperty(extNamespace.name);
        if (url == null) {
            url = getMCProperty(extNamespace.baseName);
        }
        if (url == null || url.length() == 0) {
            urlMap = MCServerConnection.getConnectionMap(rtcPath);
            loadMCProperties(urlMap);
            url = getMCProperty(extNamespace.name);
            if (url == null) {
                url = getMCProperty(extNamespace.baseName);
            }
            if (url == null || url.length() == 0) {
                throw new SQLException("URL for " + extNamespace.name + " or " + extNamespace.baseName + " is not congigured.");
            }
        }
        return url;
    }

    private void deleteData(String sql, String errorMsg) throws SQLException {
        ResultSet rs = null;
        try {
            keepAliveStmt.executeUpdate(sql);
            rs = keepAliveStmt.getResultSet();
            if (rs != null) {
                throw new SQLException(errorMsg);
            }
            int count = keepAliveStmt.getUpdateCount();
            if (count == -1) {
                throw new SQLException("update does not produce any result");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
   * This method would be called before the classification to clean up the table.
   *
   */
    private void deleteCycleError(int namespaceId) throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "DELETE_CLASSIFY_CYCLE_ERROR");
        sql += namespaceId;
        deleteData(sql, "deleteCycleError could not get updateCount");
    }

    /**
   * This method would be called before the classification to clean up the table.
   *
   */
    private void deleteEqError(int namespaceId) throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "DELETE_CLASSIFY_EQ_ERROR");
        sql += namespaceId;
        deleteData(sql, "deleteEqError could not get updateCount");
    }

    private void deleteInferredConcepts(int namespaceId) throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "DELETE_INFERRED_CONCEPT");
        sql += namespaceId;
        deleteData(sql, "deleteInferredConcepts could not get updateCount");
    }

    private void deleteInferredRoles(int namespaceId) throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "DELETE_INFERRED_ROLE");
        sql = getClassifyDAO().getStatement(sql, 1, String.valueOf(namespaceId));
        deleteData(sql, "deleteInferredRoles could not get updateCount");
    }

    private String addCycleError(CycleException e, int namespaceId) throws SQLException {
        List l = Arrays.asList(e.getArrayOfCycleErrors());
        int size = l.size();
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "ADD_CLASSIFY_CYCLE_ERROR");
        PreparedStatement ps = null;
        conn.setAutoCommit(false);
        try {
            deleteCycleError(namespaceId);
            deleteEqError(namespaceId);
            ps = conn.prepareStatement(sql);
            long conceptGID = -1;
            long roleGID = -1;
            for (int i = 0; i < size; i++) {
                CycleError error = (CycleError) l.get(i);
                ConceptRef ref1 = error.getConcept();
                String cycleIdStr = error.getCycleId();
                int cycleId = Integer.parseInt(cycleIdStr);
                conceptGID = getConceptGID(ref1, namespaceId);
                String roleName = error.getRole();
                if (!roleName.equals("-1")) {
                    roleGID = getObjectGID(roleName, getLinkedNamespaceId(namespaceId), ROLE);
                }
                addCycleError(ps, cycleId, conceptGID, roleGID, namespaceId);
            }
            conn.commit();
            return "CycleException: Concept: " + conceptGID + " namespaceId: " + namespaceId + " role: " + roleGID + ((size > 1) ? "...... more" : "");
        } catch (SQLException sqle) {
            conn.rollback();
            throw sqle;
        } catch (Exception ex) {
            conn.rollback();
            throw toSQLException(ex, "cannot add cycle errors");
        } finally {
            conn.setAutoCommit(true);
            if (ps != null) {
                ps.close();
            }
        }
    }

    private void addCycleError(PreparedStatement ps, int nextId, long conceptGID, long roleGID, int namespaceId) throws SQLException {
        ps.setInt(1, nextId);
        ps.setLong(2, conceptGID);
        ps.setLong(3, roleGID);
        ps.setInt(4, namespaceId);
        int result = ps.executeUpdate();
        if (result == 0) {
            throw new SQLException("unable to add cycle error: conceptGID: " + conceptGID);
        }
    }

    private int getCycleErrorId() throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "GET_CLASSIFY_CYCLE_ERROR_ID");
        return getInt(sql);
    }

    private String addEqError(EquivalencyException e, int namespaceId) throws SQLException {
        List l = Arrays.asList(e.getListOfEqErrors());
        int size = l.size();
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "ADD_CLASSIFY_EQ_ERROR");
        PreparedStatement ps = null;
        conn.setAutoCommit(false);
        try {
            deleteCycleError(namespaceId);
            deleteEqError(namespaceId);
            long conceptGID1 = -1;
            long conceptGID2 = -1;
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < l.size(); i++) {
                EqError error = (EqError) l.get(i);
                ConceptRef ref1 = error.getConcept1();
                ConceptRef ref2 = error.getConcept2();
                conceptGID1 = getConceptGID(ref1, namespaceId);
                conceptGID2 = getConceptGID(ref2, namespaceId);
                ps.setLong(1, conceptGID1);
                ps.setLong(2, conceptGID2);
                ps.setInt(3, namespaceId);
                int result = ps.executeUpdate();
                if (result == 0) {
                    throw new SQLException("unable to add eq error: " + sql);
                }
            }
            conn.commit();
            return "EquivalencyException: Concept: " + conceptGID1 + " namespaceId: " + namespaceId + " conceptGID2: " + conceptGID2 + ((size > 1) ? "...... more" : "");
        } catch (SQLException sqle) {
            conn.rollback();
            throw sqle;
        } catch (Exception ex) {
            conn.rollback();
            throw toSQLException(ex, "cannot add eq errors");
        } finally {
            conn.setAutoCommit(true);
            if (ps != null) {
                ps.close();
            }
        }
    }

    private long getConceptGID(ConceptRef ref, int localNamespaceId) throws SQLException {
        String identifier = ref.getIdentifier();
        int conceptNamespaceId = localNamespaceId;
        if (!ref.getNamespaceId().equals(String.valueOf(localNamespaceId))) {
            conceptNamespaceId = this.getLinkedNamespaceId(localNamespaceId);
        }
        return getObjectGID(identifier, conceptNamespaceId, CONCEPT);
    }

    /**
   * This method cleans up the classification state and repouplate the status of new request.
   *
   */
    private void processMCResult(List concepts, int namespaceId) throws SQLException {
        int defaultLevel = conn.getTransactionIsolation();
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "ADD_INFERRED_CONCEPT_PARENT");
        PreparedStatement addParentStmt = conn.prepareStatement(sql);
        sql = getClassifyDAO().getStatement(TABLE_KEY, "ADD_INFERRED_CONCEPT_CHILD");
        PreparedStatement addChildStmt = conn.prepareStatement(sql);
        sql = getClassifyDAO().getStatement(TABLE_KEY, "ADD_INFERRED_ROLE_BY_" + REF_BY_NUM);
        PreparedStatement addRoleStmt = conn.prepareStatement(sql);
        try {
            ((DTSClassifyGeneralDAO) getClassifyDAO()).getNamespaceLock(namespaceId, this.keepAliveStmt);
            cleanLocalNamespace(namespaceId);
            String seqName = getClassifyDAO().getStatement(TABLE_KEY, "GET_COMPLETE_ROLE_CON_SEQ");
            Set set = new HashSet();
            for (int i = 0; i < concepts.size(); i++) {
                Concept c = (Concept) concepts.get(i);
                String gid = c.getIdentifier();
                long conceptGID = ((Long) gidMap.get(gid)).longValue();
                List children = Arrays.asList(c.getInferredChildArray());
                addInferredConcepts(gidMap, set, conceptGID, gid, namespaceId, children, addChildStmt);
                List parents = Arrays.asList(c.getInferredParentArray());
                addInferredConcepts(gidMap, set, conceptGID, gid, namespaceId, parents, addParentStmt);
                addInferredRoles(gidMap, set, conceptGID, gid, namespaceId, Arrays.asList(c.getInferredRoleArray()), addRoleStmt, seqName);
            }
            sql = getClassifyDAO().getStatement(TABLE_KEY, "UPDATE_CLASSIFY_MONITOR");
            sql = getClassifyDAO().getStatement(sql, 1, "N");
            sql = getClassifyDAO().getStatement(sql, 2, sdf.format(new java.util.Date(System.currentTimeMillis())));
            sql += namespaceId;
            int result = keepAliveStmt.executeUpdate(sql);
            if (result == 0) {
                throw new SQLException("unable to update classification status");
            }
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } catch (Exception e) {
            conn.rollback();
            throw toSQLException(e, "unable to process result: ");
        } finally {
            conn.setTransactionIsolation(defaultLevel);
            conn.setAutoCommit(true);
            closeDBResources(new Statement[] { addParentStmt, addChildStmt, addRoleStmt });
        }
    }

    private String getKey(long l1, long l2) {
        if (l1 > l2) {
            return l2 + " " + l1;
        }
        return l1 + " " + l2;
    }

    private void addInferredConcepts(Map gidMap, Set set, long conceptGID, String gid, int namespaceId, List concepts, PreparedStatement preStat) throws SQLException {
        for (int i = 0; i < concepts.size(); i++) {
            ConceptRef ref = (ConceptRef) concepts.get(i);
            String name = ref.getIdentifier();
            int conNsId = Integer.parseInt(ref.getNamespaceId());
            Long nameGID = (Long) gidMap.get(name);
            if (nameGID == null) {
                nameGID = loadGID(name, conNsId);
                gidMap.put(name, nameGID);
            }
            String key = getKey(conceptGID, nameGID.longValue());
            if (set.contains(key)) {
                continue;
            } else {
                set.add(key);
            }
            preStat.setLong(1, conceptGID);
            preStat.setLong(2, nameGID.longValue());
            preStat.setInt(3, namespaceId);
            try {
                int status = preStat.executeUpdate();
                if (status != 1) {
                    throw new SQLException("unable to add concepts: " + conceptGID + " " + name);
                }
            } catch (SQLException e) {
                isDuplicationError(e);
            }
        }
    }

    private void isDuplicationError(SQLException e) throws SQLException {
        int errorCode = ((DTSClassifyGeneralDAO) getClassifyDAO()).getDuplicationErrorCode();
        if (e.getErrorCode() != errorCode) {
            throw e;
        }
    }

    private Long loadGID(String value, int namespaceId) throws SQLException {
        long oGID = getObjectGID(value, namespaceId, CONCEPT);
        return new Long(oGID);
    }

    private void addInferredRoles(Map gidMap, Set set, long conceptGID, String gid, int namespaceId, List roles, PreparedStatement preStat, String seqName) throws SQLException {
        for (int i = 0; i < roles.size(); i++) {
            Role role = (Role) roles.get(i);
            int group = role.getRoleGroup();
            ConceptRef ref = role.getRoleValue();
            String value = ref.getIdentifier();
            int valNsId = Integer.parseInt(ref.getNamespaceId());
            Long nameGID = (Long) gidMap.get(value);
            if (nameGID == null) {
                nameGID = loadGID(value, valNsId);
                gidMap.put(value, nameGID);
            }
            String name = role.getIdentifier();
            RoleModifierEnum.Enum mod = role.getRoleModifier();
            int typeNsId = Integer.parseInt(role.getNamespaceId());
            int modifier = ((Integer) ROLE_MODE_MAP.get(mod)).intValue();
            String key = conceptGID + " " + name + " " + typeNsId + " " + value + " " + modifier + " " + group;
            if (set.contains(key)) {
                continue;
            } else {
                set.add(key);
            }
            ((DTSClassifyGeneralDAO) getClassifyDAO()).setRoleParameters(conceptGID, name, typeNsId, nameGID.longValue(), modifier, group, preStat, seqName, conn);
            try {
                int status = preStat.executeUpdate();
                if (status != 1) {
                    throw new SQLException("unable to add concepts: " + conceptGID + " " + name);
                }
            } catch (SQLException e) {
                isDuplicationError(e);
            }
        }
    }

    private long getObjectGID(String refValue, int namespaceId, String mode) throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "GET_" + mode + "_GID_BY_" + REF_BY_NUM);
        sql = getClassifyDAO().getStatement(sql, 1, String.valueOf(namespaceId));
        sql += "'" + SQL.escapeSingleQoute(refValue) + "'";
        ResultSet rs = null;
        try {
            rs = keepAliveStmt.executeQuery(sql);
            long gid = -1;
            while (rs.next()) {
                gid = rs.getLong(1);
            }
            return gid;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private String getVersionName(int baseNamespaceId) throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "GET_CLASSIFY_VERSION");
        sql = getClassifyDAO().getStatement(sql, 1, String.valueOf(baseNamespaceId));
        ResultSet rs = null;
        try {
            rs = keepAliveStmt.executeQuery(sql);
            String versionName = null;
            while (rs.next()) {
                versionName = rs.getString(1);
            }
            return versionName;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
   * Updates the version information of the given linked subscription namespace
   * in the dts_classifier_graph table to have the latest version name.
   * This should be called after an Ontylog Extension namespace is successfully
   * classified. This will synchronize the version information if there was a
   * subscription update on the linked namespace.
   *
   * @param namespaceId   The linked subscription namespace id.
   *
   * @throws SQLException If an error occurs while updating the table.
   */
    private void updateVersionName(int namespaceId) throws SQLException {
        String versionName = getVersionName(namespaceId);
        String updateSql = getClassifyDAO().getStatement(TABLE_KEY, "UPDATE_CLASSIFY_VERSION");
        updateSql = getClassifyDAO().getStatement(updateSql, 1, versionName);
        updateSql = getClassifyDAO().getStatement(updateSql, 2, String.valueOf(namespaceId));
        keepAliveStmt.executeUpdate(updateSql);
    }

    private void cleanLocalNamespace(int namespaceId) throws SQLException {
        deleteCycleError(namespaceId);
        deleteEqError(namespaceId);
        deleteInferredConcepts(namespaceId);
        deleteInferredRoles(namespaceId);
    }

    private void getDefiningConcepts(Concept con, long conceptGID, int localNamespaceId, PreparedStatement defineConceptStmt) throws SQLException {
        defineConceptStmt.setLong(1, conceptGID);
        ResultSet rs = null;
        boolean isRoot = true;
        try {
            rs = defineConceptStmt.executeQuery();
            ArrayList defConList = new ArrayList();
            while (rs.next()) {
                isRoot = false;
                int definingNamespaceId = rs.getInt(3);
                String conceptRefBy = rs.getString(2);
                long defConceptGID = rs.getLong(1);
                gidMap.put(conceptRefBy, new Long(defConceptGID));
                ConceptRef ref = ConceptRef.Factory.newInstance();
                ref.setIdentifier(conceptRefBy);
                ref.setNamespaceId(String.valueOf(definingNamespaceId));
                defConList.add(ref);
            }
            if (defConList.size() > 0) {
                con.setDefinedParentArray((ConceptRef[]) defConList.toArray(new ConceptRef[0]));
            }
            if (isRoot) {
                throw new SQLException("No defining Super Concepts");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private void getDefiningRoles(Concept con, long conceptGID, int localNamespaceId, PreparedStatement defineRoleStmt) throws SQLException {
        defineRoleStmt.setLong(1, conceptGID);
        ResultSet rs = null;
        try {
            rs = defineRoleStmt.executeQuery();
            ArrayList roleList = new ArrayList();
            while (rs.next()) {
                String typeIdentifer = rs.getString(1);
                int typeNamespaceId = rs.getInt(2);
                String valueConIdentifer = rs.getString(3);
                int valueConNamespaceId = rs.getInt(4);
                int roleGroup = rs.getInt(5);
                int roleMod = rs.getInt(6);
                long valueConceptGID = rs.getLong(7);
                gidMap.put(valueConIdentifer, new Long(valueConceptGID));
                ConceptRef ref = ConceptRef.Factory.newInstance();
                ref.setIdentifier(valueConIdentifer);
                ref.setNamespaceId(String.valueOf(valueConNamespaceId));
                Role r = Role.Factory.newInstance();
                r.setIdentifier(typeIdentifer);
                r.setNamespaceId(String.valueOf(typeNamespaceId));
                r.setRoleValue(ref);
                r.setRoleModifier((RoleModifierEnum.Enum) ROLE_MODE_MAP.get(new Integer(roleMod)));
                r.setRoleGroup(roleGroup);
                roleList.add(r);
            }
            con.setDefinedRoleArray((Role[]) roleList.toArray(new Role[0]));
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private int getLinkedNamespaceId(int localNamespaceId) throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "GET_SUBSCRIPTION_NAMESPACE_ID");
        int linkedNamespaceId = getInt(sql + localNamespaceId);
        if (linkedNamespaceId == -1) {
            throw new SQLException("invalid linked namespace for " + localNamespaceId);
        }
        return linkedNamespaceId;
    }

    private int getInt(String sql) throws SQLException {
        ResultSet rs = null;
        try {
            rs = keepAliveStmt.executeQuery(sql);
            int nextId = -1;
            while (rs.next()) {
                nextId = rs.getInt(1);
                if (nextId == 0) {
                    nextId = 1;
                }
            }
            return nextId;
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    private SQLException toSQLException(Exception t) {
        SQLException sqle = new SQLException(t.getClass().getName() + ": " + t.getMessage());
        sqle.setStackTrace(t.getStackTrace());
        return sqle;
    }

    private SQLException toSQLException(Exception t, String message) {
        SQLException sqle = new SQLException(t.getClass().getName() + ": " + message);
        sqle.setStackTrace(t.getStackTrace());
        return sqle;
    }

    /**
   * This is only for cycle error and eq error.
   **/
    private SQLException toSQLException(Exception t, String message, int errorCode) {
        SQLException sqle = new SQLException(String.valueOf(errorCode), "NORMAL", errorCode);
        sqle.setStackTrace(t.getStackTrace());
        return sqle;
    }

    private RuntimeException toRuntimeException(Exception t) {
        RuntimeException rte = new RuntimeException(t.getMessage() + ": " + t.getMessage());
        rte.setStackTrace(t.getStackTrace());
        return rte;
    }

    private RuntimeException toRuntimeException(Exception t, String message) {
        RuntimeException rte = new RuntimeException(t.getMessage() + ": " + t.getMessage());
        rte.setStackTrace(t.getStackTrace());
        return rte;
    }

    /**
   * This method closes all the statements passed as an argument.
   * If there is SQLException during the close, the exception will be 
   * saved and continue to close the rest of the statements.
   *
   * Once all the resultsets and statements are attempted to close, the exception will be thrown
   *
   */
    private void closeDBResources(ResultSet rs[], Statement stat[]) throws SQLException {
        SQLException[] rsSQLException = closeDBResources(rs);
        SQLException[] statSQLException = closeDBResources(stat);
        for (int i = 0; i < rsSQLException.length; i++) {
            if (rsSQLException[i] != null) {
                throw rsSQLException[i];
            }
        }
        for (int i = 0; i < statSQLException.length; i++) {
            if (statSQLException[i] != null) {
                throw statSQLException[i];
            }
        }
    }

    private SQLException[] closeDBResources(Object[] objs) {
        int i = 0;
        SQLException sqle[] = new SQLException[objs.length];
        retry: for (; i < objs.length; i++) {
            try {
                if (objs[i] != null) {
                    if (objs[i] instanceof ResultSet) {
                        ((ResultSet) objs[i]).close();
                    } else {
                        ((Statement) objs[i]).close();
                    }
                }
            } catch (SQLException e) {
                sqle[i] = e;
                continue retry;
            }
        }
        return sqle;
    }

    private ExtNamespaceEntry fetchExtNamespaceEntry(int extNamespaceId) throws SQLException {
        String sql = getClassifyDAO().getStatement(TABLE_KEY, "GET_EXT_NAMESPACE");
        sql += extNamespaceId;
        ExtNamespaceEntry extNs = null;
        ResultSet rs = null;
        try {
            rs = keepAliveStmt.executeQuery(sql);
            while (rs.next()) {
                extNs = new ExtNamespaceEntry(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return extNs;
    }

    static class ExtNamespaceEntry {

        final int id;

        final String name;

        final int baseId;

        final String baseName;

        public ExtNamespaceEntry(int id, String name, int baseId, String baseName) {
            this.id = id;
            this.name = name;
            this.baseId = baseId;
            this.baseName = baseName;
        }
    }
}
