package com.vlee.ejb.accounting;

import java.sql.*;
import javax.sql.*;
import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.math.BigDecimal;
import com.vlee.local.*;
import com.vlee.util.*;

public class JournalTransactionBean implements EntityBean {

    private String dsName = ServerConfig.DATA_SOURCE;

    public static final String TABLENAME = "acc_journal_transaction";

    public static final String MODULENAME = "accounting";

    protected final String strObjectName = "JournalTransactionBean: ";

    protected final String strTimeBegin = "0000-01-01 00:00:00.000000000";

    private EntityContext mContext;

    public static final String PKID = "pkid";

    public static final String PREV_JTXNID = "prev_journaltxnid";

    public static final String NEXT_JTXNID = "next_journaltxnid";

    public static final String TXNCODE = "txncode";

    public static final String PCCENTERID = "pccenterid";

    public static final String BATCHID = "batchid";

    public static final String TYPEID = "typeid";

    public static final String STATUS = "status";

    public static final String NAME = "name";

    public static final String DESCRIPTION = "description";

    public static final String AMOUNT = "amount";

    public static final String TRANSACTIONDATE = "transactiondate";

    public static final String DOC_REF = "doc_ref";

    public static final String DOC_KEY = "doc_key";

    public static final String USERID_CREATE = "userid_create";

    public static final String USERID_EDIT = "userid_edit";

    public static final String USERID_CANCEL = "userid_cancel";

    public static final String CREATE_TIME = "createtime";

    public static final String EDIT_TIME = "edittime";

    public static final String CANCEL_TIME = "canceltime";

    public static final String ACTIVE = "active";

    public static final String INACTIVE = "inactive";

    public static final String CREATED = "created";

    public static final Integer TYPEID_NOT_POSTED = new Integer(0);

    public static final Integer TYPEID_POSTED = new Integer(1);

    public static final String TXNCODE_MANUAL = "manual";

    public static final String TXNCODE_AUTO = "auto";

    public static final String TXNCODE_PROFIT = "profit";

    public static final String TXNCODE_OPEN = "open";

    public static final String STATE_CREATED = "created";

    private JournalTransactionObject valObj = null;

    public Long getPkId() {
        return this.valObj.pkId;
    }

    public void setPkId(Long jtxnid) {
        this.valObj.pkId = jtxnid;
    }

    public Long getPrevJournalTxnId() {
        return this.valObj.prevJournalTxnId;
    }

    public void setPrevJournalTxnId(Long jtxnid) {
        this.valObj.prevJournalTxnId = jtxnid;
    }

    public Long getNextJournalTxnId() {
        return this.valObj.nextJournalTxnId;
    }

    public void setNextJournalTxnId(Long jtxnid) {
        this.valObj.nextJournalTxnId = jtxnid;
    }

    public String getTxnCode() {
        return this.valObj.txnCode;
    }

    public void setTxnCode(String jcode) {
        this.valObj.txnCode = jcode;
    }

    public Integer getPCCenterId() {
        return this.valObj.pcCenterId;
    }

    public void setPCCenterId(Integer pccenterid) {
        this.valObj.pcCenterId = pccenterid;
    }

    public Integer getBatchId() {
        return this.valObj.batchId;
    }

    public void setBatchId(Integer batchid) {
        this.valObj.batchId = batchid;
    }

    public Integer getTypeId() {
        return this.valObj.typeId;
    }

    public void setTypeId(Integer intType) {
        this.valObj.typeId = intType;
        storeObject();
    }

    public String getStatus() {
        return this.valObj.status;
    }

    public void setStatus(String stts) {
        this.valObj.status = stts;
    }

    public String getName() {
        return this.valObj.name;
    }

    public void setName(String strName) {
        this.valObj.name = strName;
    }

    public String getDescription() {
        return this.valObj.description;
    }

    public void setDescription(String strDescription) {
        this.valObj.description = strDescription;
    }

    public BigDecimal getAmount() {
        return this.valObj.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.valObj.amount = amount;
    }

    public Timestamp getTransactionDate() {
        return this.valObj.transactionDate;
    }

    public void setTransactionDate(Timestamp tsTime) {
        this.valObj.transactionDate = tsTime;
    }

    public String getDocRef() {
        return this.valObj.docRef;
    }

    public void setDocRef(String docRef) {
        this.valObj.docRef = docRef;
    }

    public Long getDocKey() {
        return this.valObj.docKey;
    }

    public void setDocKey(Long docKey) {
        this.valObj.docKey = docKey;
    }

    public String getState() {
        return this.valObj.state;
    }

    public void setState(String state) {
        this.valObj.state = state;
    }

    public Integer getUserIdCreate() {
        return this.valObj.userIdCreate;
    }

    public void setUserIdCreate(Integer intUserId) {
        this.valObj.userIdCreate = intUserId;
    }

    public Integer getUserIdEdit() {
        return this.valObj.userIdEdit;
    }

    public void setUserIdEdit(Integer intUserId) {
        this.valObj.userIdEdit = intUserId;
    }

    public Integer getUserIdCancel() {
        return this.valObj.userIdCancel;
    }

    public void setUserIdCancel(Integer intUserId) {
        this.valObj.userIdCancel = intUserId;
    }

    public Timestamp getTimestampCreate() {
        return this.valObj.timestampCreate;
    }

    public void setTimestampCreate(Timestamp tsTime) {
        this.valObj.timestampCreate = tsTime;
    }

    public Timestamp getTimestampEdit() {
        return this.valObj.timestampEdit;
    }

    public void setTimestampEdit(Timestamp tsTime) {
        this.valObj.timestampEdit = tsTime;
    }

    public Timestamp getTimestampCancel() {
        return this.valObj.timestampCancel;
    }

    public void setTimestampCancel(Timestamp tsTime) {
        this.valObj.timestampCancel = tsTime;
    }

    public JournalTransactionObject getValueObject() {
        return this.valObj;
    }

    public void setValueObject(JournalTransactionObject jto) throws Exception {
        if (this.valObj == null) {
            throw new Exception("Object undefined!");
        }
        Long pkid = this.valObj.pkId;
        this.valObj = jto;
        this.valObj.pkId = pkid;
        storeObject();
    }

    public void setEntityContext(EntityContext mContext) {
        Log.printVerbose(strObjectName + " In setEntityContext");
        this.mContext = mContext;
        Log.printVerbose(strObjectName + " Leaving setEntityContext");
    }

    public void unsetEntityContext() {
        Log.printVerbose(strObjectName + " In unsetEntityContext");
        this.mContext = null;
        Log.printVerbose(strObjectName + " Leaving unsetEntityContext");
    }

    public Long ejbCreate(String txnCode, Integer pcCenterId, Integer batchId, Integer typeId, String name, String description, BigDecimal amount, Timestamp transactionDate, String docRef, Long docKey, Integer userId, Timestamp tsTime) throws CreateException {
        Long newPkId = null;
        Log.printVerbose(strObjectName + " In ejbCreate");
        newPkId = insertNewRow(txnCode, pcCenterId, batchId, typeId, name, description, amount, transactionDate, docRef, docKey, userId, tsTime);
        if (newPkId != null) {
            this.valObj = new JournalTransactionObject();
            this.valObj.pkId = newPkId;
            this.valObj.prevJournalTxnId = new Long(0);
            this.valObj.nextJournalTxnId = new Long(0);
            this.valObj.txnCode = txnCode;
            this.valObj.pcCenterId = pcCenterId;
            this.valObj.batchId = batchId;
            this.valObj.typeId = typeId;
            this.valObj.status = JournalTransactionBean.ACTIVE;
            this.valObj.name = name;
            this.valObj.description = description;
            this.valObj.amount = amount;
            this.valObj.transactionDate = transactionDate;
            this.valObj.docRef = docRef;
            this.valObj.docKey = docKey;
            this.valObj.state = JournalTransactionBean.CREATED;
            this.valObj.userIdCreate = userId;
            this.valObj.userIdEdit = new Integer(0);
            this.valObj.userIdCancel = new Integer(0);
            this.valObj.timestampCreate = tsTime;
            this.valObj.timestampEdit = Timestamp.valueOf(strTimeBegin);
            this.valObj.timestampCancel = Timestamp.valueOf(strTimeBegin);
        }
        Log.printVerbose(strObjectName + " Leaving ejbCreate");
        return newPkId;
    }

    public void ejbPostCreate(String txnCode, Integer pcCenterId, Integer batchId, Integer typeId, String name, String description, BigDecimal amount, Timestamp transactionDate, String docRef, Long docKey, Integer userId, Timestamp tsTime) {
    }

    public void ejbRemove() {
        Log.printVerbose(strObjectName + " In ejbRemove");
        deleteObject(this.valObj.pkId);
        Log.printVerbose(strObjectName + " Leaving ejbRemove");
    }

    public void ejbActivate() {
        Log.printVerbose(strObjectName + " In ejbActivate");
        this.valObj = new JournalTransactionObject();
        this.valObj.pkId = (Long) mContext.getPrimaryKey();
        Log.printVerbose(strObjectName + " Leaving ejbActivate");
    }

    public void ejbPassivate() {
        Log.printVerbose(strObjectName + " In ejbPassivate");
        this.valObj = null;
        Log.printVerbose(strObjectName + " Leaving ejbPassivate");
    }

    public void ejbLoad() {
        Log.printVerbose(strObjectName + " In ejbLoad");
        loadObject();
        Log.printVerbose(strObjectName + " Leaving ejbLoad");
    }

    public void ejbStore() {
        Log.printVerbose(strObjectName + " In ejbStore");
        storeObject();
        Log.printVerbose(strObjectName + " Leaving ejbStore");
    }

    public Long ejbFindByPrimaryKey(Long pkid) throws FinderException {
        Log.printVerbose(strObjectName + " In ejbFindByPrimaryKey");
        boolean result;
        result = selectByPrimaryKey(pkid);
        Log.printVerbose(strObjectName + " Leaving ejbFindByPrimaryKey");
        if (result) {
            return pkid;
        } else {
            throw new ObjectNotFoundException("Row for id " + pkid.toString() + " not found.");
        }
    }

    public Collection ejbFindAllObjects() throws FinderException {
        Log.printVerbose(strObjectName + " In ejbFindAllObjects");
        Collection col = selectAllObjects();
        Log.printVerbose(strObjectName + " Leaving ejbFindAllObjects");
        return col;
    }

    public Collection ejbFindObjectsGiven(String fieldName, String value) throws FinderException {
        Log.printVerbose(strObjectName + " In ejbFindObjectsGiven");
        Collection col = selectObjectsGiven(fieldName, value);
        Log.printVerbose(strObjectName + " Leaving ejbFindObjectsGiven");
        return col;
    }

    public Collection ejbFindEJBsGiven(String field1, String value1, String field2, String value2, String field3, String value3, String field4, String value4, String field5, String like5, String field6, Timestamp timeAfterEq, Timestamp timeBefore) throws FinderException {
        Collection col = selectEJBsGiven(field1, value1, field2, value2, field3, value3, field4, value4, field5, like5, field6, timeAfterEq, timeBefore);
        return col;
    }

    public Vector ejbHomeGetValueObjectsGiven(String fieldName1, String value1, String fieldName2, String value2) {
        Log.printVerbose(strObjectName + " In ejbHomeGetValueObjectsGiven");
        Vector vecValObj = selectValueObjectsGiven(fieldName1, value1, fieldName2, value2);
        Log.printVerbose(strObjectName + " Leaving ejbHomeGetValueObjectsGiven");
        return vecValObj;
    }

    public Collection ejbHomeGetTransferOfPLforRealGL() {
        Collection result = new Vector();
        try {
            result = selectTransferOfPLforRealGL();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public Vector ejbHomeGetValueObjectsGivenDate(String fieldName1, String value1, String fieldName2, String value2, String fieldName3, String value3, Timestamp ts1, Timestamp ts2) {
        Log.printVerbose(strObjectName + " In ejbHomeGetValueObjectsGivenDate");
        Vector vecValObj = selectValueObjectsGivenDate(fieldName1, value1, fieldName2, value2, fieldName3, value3, ts1, ts2);
        Log.printVerbose(strObjectName + " Leaving ejbHomeGetValueObjectsGivenDate");
        return vecValObj;
    }

    public Vector ejbHomeGetImbalanceTxn(String field1, String value1, String field2, String value2, String field3, String value3) {
        Vector vecJTxn = null;
        try {
            vecJTxn = selectImbalanceTxn(field1, value1, field2, value2, field3, value3);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vecJTxn;
    }

    public Collection ejbHomeGetObjects(QueryObject query) {
        Collection col = null;
        try {
            col = selectObjects(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return col;
    }

    /** ***************** Database Routines ************************ */
    private DataSource getDataSource() {
        try {
            InitialContext ic = new InitialContext();
            DataSource ds = (DataSource) ic.lookup(dsName);
            return ds;
        } catch (NamingException ne) {
            throw new EJBException("Naming lookup failure: " + ne.getMessage());
        }
    }

    private void cleanup(Connection cn, PreparedStatement ps) {
        try {
            if (ps != null) ps.close();
        } catch (Exception e) {
            throw new EJBException(e);
        }
        try {
            if (cn != null) cn.close();
        } catch (Exception e) {
            throw new EJBException(e);
        }
    }

    private Long insertNewRow(String txnCode, Integer pcCenterId, Integer batchId, Integer typeId, String name, String description, BigDecimal amount, Timestamp transactionDate, String docRef, Long docKey, Integer userId, Timestamp tsTime) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        Long newPkId = null;
        name = StringManup.truncate(name, 500);
        description = StringManup.truncate(description, 1000);
        try {
            cn = ds.getConnection();
            newPkId = getNextPKId(cn);
            String sqlStatement = " INSERT INTO " + TABLENAME + " (pkid, prev_journaltxnid, next_journaltxnid, txncode, pccenterid, " + " batchid, typeid, status, name, description, amount," + " transactiondate, doc_ref, doc_key, state, userid_create, userid_edit, " + " userid_cancel, createtime, edittime, canceltime)" + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = cn.prepareStatement(sqlStatement);
            ps.setLong(1, newPkId.longValue());
            ps.setLong(2, 0);
            ps.setLong(3, 0);
            ps.setString(4, txnCode);
            ps.setInt(5, pcCenterId.intValue());
            ps.setInt(6, batchId.intValue());
            ps.setInt(7, typeId.intValue());
            ps.setString(8, JournalTransactionBean.ACTIVE);
            ps.setString(9, name);
            ps.setString(10, description);
            ps.setBigDecimal(11, amount);
            ps.setTimestamp(12, transactionDate);
            ps.setString(13, docRef);
            ps.setLong(14, docKey.longValue());
            ps.setString(15, STATE_CREATED);
            ps.setInt(16, userId.intValue());
            ps.setInt(17, 0);
            ps.setInt(18, 0);
            ps.setTimestamp(19, tsTime);
            ps.setTimestamp(20, Timestamp.valueOf(strTimeBegin));
            ps.setTimestamp(21, Timestamp.valueOf(strTimeBegin));
            ps.executeUpdate();
            Log.printAudit(strObjectName + " Created New Row:" + newPkId.toString());
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
        return newPkId;
    }

    private static synchronized Long getNextPKId(Connection con) throws NamingException, SQLException {
        return AppTableCounterUtil.getNextPKId(con, PKID, TABLENAME, MODULENAME, (Long) null);
    }

    private void deleteObject(Long pkid) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String sqlStatement = "DELETE FROM " + TABLENAME + " WHERE pkid = ?";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setLong(1, pkid.longValue());
            ps.executeUpdate();
            Log.printAudit(strObjectName + " Deleted Object: " + pkid.toString());
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private void loadObject() {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String sqlStatement = "SELECT * FROM " + TABLENAME + " WHERE pkid = ? ";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setLong(1, this.valObj.pkId.longValue());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                this.valObj = getObject(rs, "");
            } else {
                throw new NoSuchEntityException("Row for this EJB Object is not found in database.");
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private void storeObject() {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String sqlStatement = "UPDATE " + TABLENAME + " SET prev_journaltxnid = ?, next_journaltxnid = ?," + " txncode = ?, pccenterid = ?, batchid = ?, typeid = ?," + " status = ?, name = ?, description = ?, amount = ?, transactiondate = ?, " + " doc_ref = ?, doc_key = ?, state = ?, userid_create = ?," + " userid_edit = ?, userid_cancel = ?, createtime = ?, edittime = ?, canceltime = ? " + " WHERE pkid = ?";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            this.valObj.failSafe();
            ps.setLong(1, this.valObj.prevJournalTxnId.longValue());
            ps.setLong(2, this.valObj.nextJournalTxnId.longValue());
            ps.setString(3, this.valObj.txnCode);
            ps.setInt(4, this.valObj.pcCenterId.intValue());
            ps.setInt(5, this.valObj.batchId.intValue());
            ps.setInt(6, this.valObj.typeId.intValue());
            ps.setString(7, this.valObj.status);
            ps.setString(8, this.valObj.name);
            ps.setString(9, this.valObj.description);
            ps.setBigDecimal(10, this.valObj.amount);
            ps.setTimestamp(11, this.valObj.transactionDate);
            ps.setString(12, this.valObj.docRef);
            ps.setLong(13, this.valObj.docKey.longValue());
            ps.setString(14, this.valObj.state);
            ps.setInt(15, this.valObj.userIdCreate.intValue());
            ps.setInt(16, this.valObj.userIdEdit.intValue());
            ps.setInt(17, this.valObj.userIdCancel.intValue());
            ps.setTimestamp(18, this.valObj.timestampCreate);
            ps.setTimestamp(19, this.valObj.timestampEdit);
            ps.setTimestamp(20, this.valObj.timestampCancel);
            ps.setLong(21, this.valObj.pkId.longValue());
            int rowCount = ps.executeUpdate();
            if (rowCount == 0) {
                throw new EJBException("Storing ejb object " + this.valObj.pkId.toString() + " failed.");
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private boolean selectByPrimaryKey(Long pkid) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            String sqlStatement = "SELECT * FROM " + TABLENAME + " WHERE pkid = ?";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setLong(1, pkid.longValue());
            ResultSet rs = ps.executeQuery();
            boolean result = rs.next();
            return result;
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private Collection selectAllObjects() {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            ArrayList objectSet = new ArrayList();
            String sqlStatement = "SELECT pkid FROM " + TABLENAME + "ORDER BY transactiondate, pccenterid, pkid";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                while (rs.next()) {
                    objectSet.add(new Long(rs.getLong(1)));
                }
                return objectSet;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private Collection selectObjectsGiven(String fieldName, String value) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            ArrayList objectSet = new ArrayList();
            String sqlStatement = "SELECT pkid FROM " + TABLENAME + " WHERE " + fieldName + " = ? ORDER BY transactiondate, pccenterid, pkid ";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setString(1, value);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                while (rs.next()) {
                    objectSet.add(new Long(rs.getLong(1)));
                }
                return objectSet;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private Collection selectEJBsGiven(String field1, String value1, String field2, String value2, String field3, String value3, String field4, String value4, String field5, String like5, String field6, Timestamp timeAfterEq, Timestamp timeBefore) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            ArrayList objectSet = new ArrayList();
            String sqlStatement = "SELECT pkid FROM " + TABLENAME + " WHERE " + field1 + " = '" + value1 + "' ";
            if (field2 != null && value2 != null) {
                sqlStatement += " AND " + field2 + "='" + value2 + "' ";
            }
            if (field3 != null && value3 != null) {
                sqlStatement += " AND " + field3 + "='" + value3 + "' ";
            }
            if (field4 != null && value4 != null) {
                sqlStatement += " AND " + field4 + "='" + value4 + "' ";
            }
            if (field5 != null && like5 != null) {
                sqlStatement += " AND " + field5 + "~*'" + like5 + "' ";
            }
            if (field6 != null && timeAfterEq != null) {
                sqlStatement += " AND " + field6 + ">='" + TimeFormat.strDisplayDate(timeAfterEq) + "' ";
            }
            if (field6 != null && timeBefore != null) {
                sqlStatement += " AND " + field6 + "<'" + TimeFormat.strDisplayDate(timeBefore) + "' ";
            }
            sqlStatement += " ORDER BY pkid ";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                while (rs.next()) {
                    objectSet.add(new Long(rs.getLong(1)));
                }
                return objectSet;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
    }

    private Vector selectImbalanceTxn(String field1, String value1, String field2, String value2, String field3, String value3) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        Vector vecValObj = new Vector();
        try {
            String sqlStatement = "	SELECT * FROM " + TABLENAME + " WHERE pkid in (SELECT tmp1.pkid FROM (SELECT jt.pkid," + "  sum(je.amount) AS je_amount " + " FROM acc_journal_transaction AS jt INNER JOIN acc_journal_entry " + " AS je ON (jt.pkid = je.journaltxnid) GROUP BY jt.pkid) " + " AS tmp1 INNER JOIN acc_journal_transaction " + " AS jtxn2 ON (tmp1.pkid = jtxn2.pkid) WHERE je_amount>'0' OR je_amount<'0' " + " AND " + field1 + "='" + value1 + "' ";
            if (field2 != null && value2 != null) {
                sqlStatement = sqlStatement + " AND " + field2 + " = '" + value2 + "' ";
            }
            if (field3 != null && value3 != null) {
                sqlStatement = sqlStatement + " AND " + field3 + " = '" + value3 + "' ";
            }
            sqlStatement += " ) ";
            sqlStatement += " ORDER BY transactiondate, pkid ";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JournalTransactionObject jto = getObject(rs, "");
                vecValObj.add(jto);
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
        return vecValObj;
    }

    private Vector selectValueObjectsGiven(String fieldName1, String value1, String fieldName2, String value2) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        Vector vecValObj = new Vector();
        try {
            String sqlStatement = "SELECT pkid, prev_journaltxnid, next_journaltxnid, txncode, pccenterid, " + " batchid, typeid, status, name, description, amount," + " transactiondate, doc_ref, doc_key, state, userid_create, userid_edit, " + " userid_cancel, createtime, edittime, canceltime FROM " + TABLENAME + " WHERE " + fieldName1 + " = ?";
            if (fieldName2 != null && value2 != null) {
                sqlStatement = sqlStatement + " AND " + fieldName2 + " = ?";
            }
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setString(1, value1);
            if (fieldName2 != null && value2 != null) {
                ps.setString(2, value2);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JournalTransactionObject jto = getObject(rs, "");
                vecValObj.add(jto);
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
        return vecValObj;
    }

    private Vector selectValueObjectsGivenDate(String fieldName1, String value1, String fieldName2, String value2, String fieldName3, String value3, Timestamp ts1, Timestamp ts2) {
        DataSource ds = getDataSource();
        Connection cn = null;
        PreparedStatement ps = null;
        Vector vecValObj = new Vector();
        try {
            String sqlStatement = "SELECT pkid, prev_journaltxnid, next_journaltxnid, txncode, " + " pccenterid, batchid, typeid, status, name, description, amount," + " transactiondate, doc_ref, doc_key, state, userid_create, userid_edit, " + " userid_cancel, createtime, edittime, canceltime FROM " + TABLENAME + " WHERE " + fieldName1 + " = ?";
            String addendum = " transactiondate >= ? AND transactiondate < ?";
            if (fieldName2 != null && value2 != null) {
                sqlStatement = sqlStatement + " AND " + fieldName2 + " = ?";
                if (fieldName3 != null && value3 != null) {
                    sqlStatement = sqlStatement + " AND " + fieldName3 + " = ? AND " + addendum;
                } else {
                    sqlStatement = sqlStatement + " AND " + addendum;
                }
            } else {
                sqlStatement = sqlStatement + " AND " + addendum;
            }
            sqlStatement = sqlStatement + " ORDER BY transactiondate, pccenterid, pkid ";
            cn = ds.getConnection();
            ps = cn.prepareStatement(sqlStatement);
            ps.setString(1, value1);
            if (fieldName2 != null && value2 != null) {
                ps.setString(2, value2);
                if (fieldName3 != null && value3 != null) {
                    ps.setString(3, value3);
                    ps.setTimestamp(4, ts1);
                    ps.setTimestamp(5, ts2);
                } else {
                    ps.setTimestamp(3, ts1);
                    ps.setTimestamp(4, ts2);
                }
            } else {
                ps.setTimestamp(2, ts1);
                ps.setTimestamp(3, ts2);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                JournalTransactionObject jto = new JournalTransactionObject();
                jto.pkId = new Long(rs.getLong("pkid"));
                jto.prevJournalTxnId = new Long(rs.getLong("prev_journaltxnid"));
                jto.nextJournalTxnId = new Long(rs.getLong("next_journaltxnid"));
                jto.txnCode = rs.getString("txncode");
                jto.pcCenterId = new Integer(rs.getInt("pccenterid"));
                jto.batchId = new Integer(rs.getInt("batchid"));
                jto.typeId = new Integer(rs.getInt("typeid"));
                jto.status = rs.getString("status");
                jto.name = rs.getString("name");
                jto.description = rs.getString("description");
                jto.amount = rs.getBigDecimal("amount");
                jto.transactionDate = rs.getTimestamp("transactiondate");
                jto.docRef = rs.getString("doc_ref");
                jto.docKey = new Long(rs.getLong("doc_key"));
                jto.state = rs.getString("state");
                jto.userIdCreate = new Integer(rs.getInt("userid_create"));
                jto.userIdEdit = new Integer(rs.getInt("userid_edit"));
                jto.userIdCancel = new Integer(rs.getInt("userid_cancel"));
                jto.timestampCreate = rs.getTimestamp("createtime");
                jto.timestampEdit = rs.getTimestamp("edittime");
                jto.timestampCancel = rs.getTimestamp("canceltime");
                vecValObj.add(jto);
            }
        } catch (Exception e) {
            throw new EJBException(e);
        } finally {
            cleanup(cn, ps);
        }
        return vecValObj;
    }

    public static JournalTransactionObject getObject(ResultSet rs, String prefix) throws Exception {
        JournalTransactionObject theObj = null;
        try {
            theObj = new JournalTransactionObject();
            theObj.pkId = new Long(rs.getLong("pkid"));
            theObj.prevJournalTxnId = new Long(rs.getLong("prev_journaltxnid"));
            theObj.nextJournalTxnId = new Long(rs.getLong("next_journaltxnid"));
            theObj.txnCode = rs.getString("txncode");
            theObj.pcCenterId = new Integer(rs.getInt("pccenterid"));
            theObj.batchId = new Integer(rs.getInt("batchid"));
            theObj.typeId = new Integer(rs.getInt("typeid"));
            theObj.status = rs.getString("status");
            theObj.name = rs.getString("name");
            theObj.description = rs.getString("description");
            theObj.amount = rs.getBigDecimal("amount");
            theObj.transactionDate = rs.getTimestamp("transactiondate");
            theObj.docRef = rs.getString("doc_ref");
            theObj.docKey = new Long(rs.getLong("doc_key"));
            theObj.state = rs.getString("state");
            theObj.userIdCreate = new Integer(rs.getInt("userid_create"));
            theObj.userIdEdit = new Integer(rs.getInt("userid_edit"));
            theObj.userIdCancel = new Integer(rs.getInt("userid_cancel"));
            theObj.timestampCreate = rs.getTimestamp("createtime");
            theObj.timestampEdit = rs.getTimestamp("edittime");
            theObj.timestampCancel = rs.getTimestamp("canceltime");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return theObj;
    }

    private Collection selectObjects(QueryObject query) throws NamingException, SQLException {
        DataSource ds = getDataSource();
        Collection result = new Vector();
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " selectObjects: ");
            con = ds.getConnection();
            String selectStmt = " SELECT * FROM " + TABLENAME;
            selectStmt = query.appendQuery(selectStmt);
            Log.printVerbose(selectStmt);
            prepStmt = con.prepareStatement(selectStmt);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                JournalTransactionObject theObj = getObject(rs, "");
                if (theObj != null) {
                    result.add(theObj);
                }
            }
            Log.printVerbose(strObjectName + " Leaving selectObjects: ");
            Log.printVerbose(strObjectName + " Leaving selectObjects: ");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            cleanup(con, prepStmt);
        }
        return result;
    }

    private Collection selectTransferOfPLforRealGL() throws NamingException, SQLException {
        DataSource ds = getDataSource();
        Collection result = new Vector();
        Connection con = null;
        PreparedStatement prepStmt = null;
        try {
            Log.printVerbose(strObjectName + " selectObjects: ");
            con = ds.getConnection();
            String selectStmt = "  SELECT jtxn.pkid FROM (SELECT tbl2.*, glcat.real_temp FROM (SELECT tbl1.*,glcode.glcategoryid FROM (SELECT jentry.*,glindex.glcodeid FROM acc_journal_entry AS jentry INNER JOIN acc_general_ledger_index AS glindex ON (jentry.glaccid=glindex.pkid) ) AS tbl1 INNER JOIN acc_glcode_index AS glcode ON (tbl1.glcodeid=glcode.pkid AND glcode.code!='profitLoss')) AS tbl2 INNER JOIN acc_glcategory_index AS glcat ON (tbl2.glcategoryid = glcat.pkid) WHERE glcat.real_temp = 'real') AS tbl3 INNER JOIN acc_journal_transaction AS jtxn ON (tbl3.journaltxnid = jtxn.pkid) WHERE jtxn.txncode = 'profit';";
            Log.printVerbose(selectStmt);
            prepStmt = con.prepareStatement(selectStmt);
            ResultSet rs = prepStmt.executeQuery();
            while (rs.next()) {
                Long theId = new Long(rs.getLong(PKID));
                if (theId != null) {
                    result.add(theId);
                }
            }
            Log.printVerbose(strObjectName + " Leaving selectObjects: ");
            Log.printVerbose(strObjectName + " Leaving selectObjects: ");
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new SQLException("Repackaged Exception: " + ex.getMessage());
        } finally {
            cleanup(con, prepStmt);
        }
        return result;
    }
}
