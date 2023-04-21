package com.dcivision.form.dao;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import oracle.sql.BLOB;
import com.dcivision.form.bean.FormRoutingRule;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.DataSourceFactory;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.framework.Utility;
import com.dcivision.framework.bean.AbstractBaseObject;
import com.dcivision.framework.dao.AbstractDAObject;
import com.dcivision.framework.web.AbstractSearchForm;

/**
  FormRoutingRuleDAObject.java

  This class is the data access bean for table "FORM_ROUTING_RULE".

  @author      Vera Wang
  @company     DCIVision Limited
  @creation date   19/05/2005
  @version     $Revision: 1.3 $
*/
public class FormRoutingRuleDAObject extends AbstractDAObject {

    public static final String REVISION = "$Revision: 1.3 $";

    public static final String TABLE_NAME = "FORM_ROUTING_RULE";

    public FormRoutingRuleDAObject(SessionContainer sessionContainer, Connection dbConn) {
        super(sessionContainer, dbConn);
    }

    protected void initDBSetting() {
        this.baseTableName = TABLE_NAME;
        this.vecDBColumn.add("ID");
        this.vecDBColumn.add("FORM_RECORD_ID");
        this.vecDBColumn.add("RULE_NAME");
        this.vecDBColumn.add("RULE_TYPE");
        this.vecDBColumn.add("OPERATION_TYPE");
        this.vecDBColumn.add("FORM_EQUATION");
        this.vecDBColumn.add("FORM_OPERATION_EQUATION");
        this.vecDBColumn.add("TARGET_ELEMENT_ID");
        this.vecDBColumn.add("CHECK_RULE");
        this.vecDBColumn.add("RECORD_STATUS");
        this.vecDBColumn.add("UPDATE_COUNT");
        this.vecDBColumn.add("CREATOR_ID");
        this.vecDBColumn.add("CREATE_DATE");
        this.vecDBColumn.add("UPDATER_ID");
        this.vecDBColumn.add("UPDATE_DATE");
    }

    protected synchronized AbstractBaseObject getByID(Integer id) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.FORM_RECORD_ID, A.RULE_NAME, A.RULE_TYPE, A.OPERATION_TYPE, A.FORM_EQUATION, A.FORM_OPERATION_EQUATION, A.TARGET_ELEMENT_ID, A.CHECK_RULE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   FORM_ROUTING_RULE A ");
                sqlStat.append("WHERE  A.ID = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, id);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    FormRoutingRule tmpFormRoutingRule = new FormRoutingRule();
                    tmpFormRoutingRule.setID(getResultSetInteger(rs, "ID"));
                    tmpFormRoutingRule.setFormRecordID(getResultSetInteger(rs, "FORM_RECORD_ID"));
                    tmpFormRoutingRule.setRuleName(getResultSetString(rs, "RULE_NAME"));
                    tmpFormRoutingRule.setRuleType(getResultSetString(rs, "RULE_TYPE"));
                    tmpFormRoutingRule.setOperationType(getResultSetString(rs, "OPERATION_TYPE"));
                    tmpFormRoutingRule.setFormEquation(getResultSetObject(rs, "FORM_EQUATION"));
                    tmpFormRoutingRule.setFormOperationEquation(getResultSetObject(rs, "FORM_OPERATION_EQUATION"));
                    tmpFormRoutingRule.setTargetElementID(getResultSetString(rs, "TARGET_ELEMENT_ID"));
                    tmpFormRoutingRule.setCheckRule(getResultSetString(rs, "CHECK_RULE"));
                    tmpFormRoutingRule.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpFormRoutingRule.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpFormRoutingRule.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpFormRoutingRule.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpFormRoutingRule.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpFormRoutingRule.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpFormRoutingRule.setCreatorName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getCreatorID()));
                    tmpFormRoutingRule.setUpdaterName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getUpdaterID()));
                    return (tmpFormRoutingRule);
                } else {
                    throw new ApplicationException(ErrorConstant.DB_RECORD_NOT_FOUND_ERROR);
                }
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_SELECT_ERROR, e);
            } finally {
                try {
                    rs.close();
                } catch (Exception ignore) {
                } finally {
                    rs = null;
                }
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }

    protected synchronized List getList(AbstractSearchForm searchForm) throws ApplicationException {
        PreparedStatement preStat = null;
        PreparedStatement preStatCnt = null;
        ResultSet rs = null;
        ResultSet rsCnt = null;
        StringBuffer sqlStat = new StringBuffer();
        StringBuffer sqlStatCnt = new StringBuffer();
        List result = new ArrayList();
        int totalNumOfRecord = 0;
        int rowLoopCnt = 0;
        int startOffset = TextUtility.parseInteger(searchForm.getCurStartRowNo());
        int pageSize = TextUtility.parseInteger(searchForm.getPageOffset());
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.FORM_RECORD_ID, A.RULE_NAME, A.RULE_TYPE, A.OPERATION_TYPE, A.FORM_EQUATION, A.FORM_OPERATION_EQUATION, A.TARGET_ELEMENT_ID, A.CHECK_RULE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   FORM_ROUTING_RULE A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                if (searchForm.isSearchable()) {
                    String searchField = getSearchColumn(searchForm.getBasicSearchField());
                    sqlStat.append("AND  " + searchField + " " + searchForm.getBasicSearchType() + " ? ");
                }
                sqlStat = this.getFormattedSQL(sqlStat.toString());
                if (searchForm.isSortable()) {
                    String sortAttribute = searchForm.getSortAttribute();
                    if (sortAttribute.indexOf(".") < 0) {
                        sortAttribute = "A." + sortAttribute;
                    }
                    sqlStat.append("ORDER BY " + sortAttribute + " " + searchForm.getSortOrder());
                }
                sqlStatCnt = this.getSelectCountSQL(sqlStat);
                preStatCnt = dbConn.prepareStatement(sqlStatCnt.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStatCnt, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                if (searchForm.isSearchable()) {
                    String searchKeyword = this.getFormattedKeyword(searchForm.getBasicSearchKeyword(), searchForm.getBasicSearchType());
                    this.setPrepareStatement(preStatCnt, 2, searchKeyword);
                }
                rsCnt = preStatCnt.executeQuery();
                if (rsCnt.next()) {
                    totalNumOfRecord = rsCnt.getInt(1);
                }
                try {
                    rsCnt.close();
                } catch (Exception ignore) {
                } finally {
                    rsCnt = null;
                }
                try {
                    preStatCnt.close();
                } catch (Exception ignore) {
                } finally {
                    preStatCnt = null;
                }
                sqlStat = this.getSelectListSQL(sqlStat, startOffset, pageSize);
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                if (searchForm.isSearchable()) {
                    String searchKeyword = this.getFormattedKeyword(searchForm.getBasicSearchKeyword(), searchForm.getBasicSearchType());
                    this.setPrepareStatement(preStat, 2, searchKeyword);
                }
                rs = preStat.executeQuery();
                this.positionCursor(rs, startOffset, pageSize);
                while (rs.next() && rowLoopCnt < pageSize) {
                    FormRoutingRule tmpFormRoutingRule = new FormRoutingRule();
                    tmpFormRoutingRule.setID(getResultSetInteger(rs, "ID"));
                    tmpFormRoutingRule.setFormRecordID(getResultSetInteger(rs, "FORM_RECORD_ID"));
                    tmpFormRoutingRule.setRuleName(getResultSetString(rs, "RULE_NAME"));
                    tmpFormRoutingRule.setRuleType(getResultSetString(rs, "RULE_TYPE"));
                    tmpFormRoutingRule.setOperationType(getResultSetString(rs, "OPERATION_TYPE"));
                    tmpFormRoutingRule.setFormEquation(getResultSetObject(rs, "FORM_EQUATION"));
                    tmpFormRoutingRule.setFormOperationEquation(getResultSetObject(rs, "FORM_OPERATION_EQUATION"));
                    tmpFormRoutingRule.setTargetElementID(getResultSetString(rs, "TARGET_ELEMENT_ID"));
                    tmpFormRoutingRule.setCheckRule(getResultSetString(rs, "CHECK_RULE"));
                    tmpFormRoutingRule.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpFormRoutingRule.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpFormRoutingRule.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpFormRoutingRule.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpFormRoutingRule.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpFormRoutingRule.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpFormRoutingRule.setCreatorName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getCreatorID()));
                    tmpFormRoutingRule.setUpdaterName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getUpdaterID()));
                    tmpFormRoutingRule.setRecordCount(totalNumOfRecord);
                    tmpFormRoutingRule.setRowNum(startOffset++);
                    ++rowLoopCnt;
                    result.add(tmpFormRoutingRule);
                }
                return (result);
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_SELECT_ERROR, e);
            } finally {
                try {
                    rs.close();
                } catch (Exception ignore) {
                } finally {
                    rs = null;
                }
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
                try {
                    rsCnt.close();
                } catch (Exception ignore) {
                } finally {
                    rsCnt = null;
                }
                try {
                    preStatCnt.close();
                } catch (Exception ignore) {
                } finally {
                    preStatCnt = null;
                }
            }
        }
    }

    protected synchronized List getList() throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.FORM_RECORD_ID, A.RULE_NAME, A.RULE_TYPE, A.OPERATION_TYPE, A.FORM_EQUATION, A.FORM_OPERATION_EQUATION, A.TARGET_ELEMENT_ID, A.CHECK_RULE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   FORM_ROUTING_RULE A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    FormRoutingRule tmpFormRoutingRule = new FormRoutingRule();
                    tmpFormRoutingRule.setID(getResultSetInteger(rs, "ID"));
                    tmpFormRoutingRule.setFormRecordID(getResultSetInteger(rs, "FORM_RECORD_ID"));
                    tmpFormRoutingRule.setRuleName(getResultSetString(rs, "RULE_NAME"));
                    tmpFormRoutingRule.setRuleType(getResultSetString(rs, "RULE_TYPE"));
                    tmpFormRoutingRule.setOperationType(getResultSetString(rs, "OPERATION_TYPE"));
                    tmpFormRoutingRule.setFormEquation(getResultSetObject(rs, "FORM_EQUATION"));
                    tmpFormRoutingRule.setFormOperationEquation(getResultSetObject(rs, "FORM_OPERATION_EQUATION"));
                    tmpFormRoutingRule.setTargetElementID(getResultSetString(rs, "TARGET_ELEMENT_ID"));
                    tmpFormRoutingRule.setCheckRule(getResultSetString(rs, "CHECK_RULE"));
                    tmpFormRoutingRule.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpFormRoutingRule.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpFormRoutingRule.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpFormRoutingRule.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpFormRoutingRule.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpFormRoutingRule.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpFormRoutingRule.setCreatorName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getCreatorID()));
                    tmpFormRoutingRule.setUpdaterName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getUpdaterID()));
                    result.add(tmpFormRoutingRule);
                }
                return (result);
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_SELECT_ERROR, e);
            } finally {
                try {
                    rs.close();
                } catch (Exception ignore) {
                } finally {
                    rs = null;
                }
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }

    protected void validateInsert(AbstractBaseObject obj) throws ApplicationException {
    }

    protected void validateUpdate(AbstractBaseObject obj) throws ApplicationException {
    }

    protected void validateDelete(AbstractBaseObject obj) throws ApplicationException {
    }

    protected synchronized AbstractBaseObject insert(AbstractBaseObject obj) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        FormRoutingRule tmpFormRoutingRule = (FormRoutingRule) ((FormRoutingRule) obj).clone();
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                if (DataSourceFactory.DB_ORACLE != DataSourceFactory.getDatabaseType()) {
                    sqlStat.append("INSERT ");
                    sqlStat.append("INTO   FORM_ROUTING_RULE(ID, FORM_RECORD_ID, RULE_NAME, RULE_TYPE, OPERATION_TYPE, FORM_EQUATION, FORM_OPERATION_EQUATION, TARGET_ELEMENT_ID, CHECK_RULE, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE) ");
                    sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                    preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    setPrepareStatement(preStat, 1, nextID);
                    setPrepareStatement(preStat, 2, tmpFormRoutingRule.getFormRecordID());
                    setPrepareStatement(preStat, 3, tmpFormRoutingRule.getRuleName());
                    setPrepareStatement(preStat, 4, tmpFormRoutingRule.getRuleType());
                    setPrepareStatement(preStat, 5, tmpFormRoutingRule.getOperationType());
                    setPrepareStatement(preStat, 6, tmpFormRoutingRule.getFormEquation());
                    setPrepareStatement(preStat, 7, tmpFormRoutingRule.getFormOperationEquation());
                    setPrepareStatement(preStat, 8, tmpFormRoutingRule.getTargetElementID());
                    setPrepareStatement(preStat, 9, tmpFormRoutingRule.getCheckRule());
                    setPrepareStatement(preStat, 10, GlobalConstant.RECORD_STATUS_ACTIVE);
                    setPrepareStatement(preStat, 11, new Integer(0));
                    setPrepareStatement(preStat, 12, sessionContainer.getUserRecordID());
                    setPrepareStatement(preStat, 13, currTime);
                    setPrepareStatement(preStat, 14, sessionContainer.getUserRecordID());
                    setPrepareStatement(preStat, 15, currTime);
                    preStat.executeUpdate();
                } else {
                    sqlStat.append("INSERT ");
                    sqlStat.append("INTO   FORM_ROUTING_RULE(ID, FORM_RECORD_ID, RULE_NAME, RULE_TYPE, OPERATION_TYPE, FORM_EQUATION, FORM_OPERATION_EQUATION, TARGET_ELEMENT_ID, CHECK_RULE, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE) ");
                    sqlStat.append("VALUES (?, ?, ?, ?, ?, EMPTY_BLOB(), EMPTY_BLOB(), ?, ?, ?, ?, ?, ?, ?, ?) ");
                    preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    setPrepareStatement(preStat, 1, nextID);
                    setPrepareStatement(preStat, 2, tmpFormRoutingRule.getFormRecordID());
                    setPrepareStatement(preStat, 3, tmpFormRoutingRule.getRuleName());
                    setPrepareStatement(preStat, 4, tmpFormRoutingRule.getRuleType());
                    setPrepareStatement(preStat, 5, tmpFormRoutingRule.getOperationType());
                    setPrepareStatement(preStat, 6, tmpFormRoutingRule.getTargetElementID());
                    setPrepareStatement(preStat, 7, tmpFormRoutingRule.getCheckRule());
                    setPrepareStatement(preStat, 8, GlobalConstant.RECORD_STATUS_ACTIVE);
                    setPrepareStatement(preStat, 9, new Integer(0));
                    setPrepareStatement(preStat, 10, sessionContainer.getUserRecordID());
                    setPrepareStatement(preStat, 11, currTime);
                    setPrepareStatement(preStat, 12, sessionContainer.getUserRecordID());
                    setPrepareStatement(preStat, 13, currTime);
                    preStat.executeUpdate();
                    preStat.close();
                    ResultSet rs = null;
                    BLOB blob = null;
                    BLOB blob2 = null;
                    sqlStat = new StringBuffer();
                    sqlStat.append("SELECT  FORM_EQUATION, FORM_OPERATION_EQUATION ");
                    sqlStat.append("FROM  FORM_ROUTING_RULE ");
                    sqlStat.append("WHERE ID = " + nextID);
                    preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs = preStat.executeQuery();
                    if (rs.next()) {
                        blob = (BLOB) rs.getBlob("FORM_EQUATION");
                        blob2 = (BLOB) rs.getBlob("FORM_OPERATION_EQUATION");
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                        try {
                            ObjectOutputStream out = new ObjectOutputStream(baos);
                            ObjectOutputStream out2 = new ObjectOutputStream(baos2);
                            out.writeObject(tmpFormRoutingRule.getFormEquation());
                            out2.writeObject(tmpFormRoutingRule.getFormOperationEquation());
                            out.flush();
                            out2.flush();
                        } catch (java.io.IOException ioe) {
                            log.error("Cannot write object stream", ioe);
                            new java.sql.SQLException("Cannot write object stream");
                        }
                        blob.putBytes(1, baos.toByteArray());
                        blob2.putBytes(1, baos2.toByteArray());
                        sqlStat = new StringBuffer();
                        sqlStat.append("UPDATE  FORM_ROUTING_RULE ");
                        sqlStat.append("SET FORM_EQUATION = ? , FORM_OPERATION_EQUATION = ? ");
                        sqlStat.append("WHERE ID = ? ");
                        PreparedStatement preStat2 = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        preStat2.setBlob(1, blob);
                        preStat2.setBlob(2, blob2);
                        preStat2.setInt(3, nextID.intValue());
                        preStat2.executeUpdate();
                        try {
                            preStat2.close();
                        } catch (Exception ignore) {
                        } finally {
                            preStat2 = null;
                        }
                    }
                    try {
                        rs.close();
                    } catch (Exception ignore) {
                    } finally {
                        rs = null;
                    }
                }
                tmpFormRoutingRule.setID(nextID);
                tmpFormRoutingRule.setCreatorID(sessionContainer.getUserRecordID());
                tmpFormRoutingRule.setCreateDate(currTime);
                tmpFormRoutingRule.setUpdaterID(sessionContainer.getUserRecordID());
                tmpFormRoutingRule.setUpdateDate(currTime);
                tmpFormRoutingRule.setUpdateCount(new Integer(0));
                tmpFormRoutingRule.setCreatorName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getCreatorID()));
                tmpFormRoutingRule.setUpdaterName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getUpdaterID()));
                return (tmpFormRoutingRule);
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_INSERT_ERROR, e);
            } finally {
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }

    protected synchronized AbstractBaseObject update(AbstractBaseObject obj) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        FormRoutingRule tmpFormRoutingRule = (FormRoutingRule) ((FormRoutingRule) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                Timestamp currTime = Utility.getCurrentTimestamp();
                if (DataSourceFactory.DB_ORACLE != DataSourceFactory.getDatabaseType()) {
                    sqlStat.append("UPDATE FORM_ROUTING_RULE ");
                    sqlStat.append("SET  FORM_RECORD_ID=?, RULE_NAME=?, RULE_TYPE=?, OPERATION_TYPE=?, FORM_EQUATION=?, FORM_OPERATION_EQUATION=?, TARGET_ELEMENT_ID=?, CHECK_RULE=?, UPDATE_COUNT=?, UPDATER_ID=?, UPDATE_DATE=? ");
                    sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                    preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    setPrepareStatement(preStat, 1, tmpFormRoutingRule.getFormRecordID());
                    setPrepareStatement(preStat, 2, tmpFormRoutingRule.getRuleName());
                    setPrepareStatement(preStat, 3, tmpFormRoutingRule.getRuleType());
                    setPrepareStatement(preStat, 4, tmpFormRoutingRule.getOperationType());
                    setPrepareStatement(preStat, 5, tmpFormRoutingRule.getFormEquation());
                    setPrepareStatement(preStat, 6, tmpFormRoutingRule.getFormOperationEquation());
                    setPrepareStatement(preStat, 7, tmpFormRoutingRule.getTargetElementID());
                    setPrepareStatement(preStat, 8, tmpFormRoutingRule.getCheckRule());
                    setPrepareStatement(preStat, 9, new Integer(tmpFormRoutingRule.getUpdateCount().intValue() + 1));
                    setPrepareStatement(preStat, 10, sessionContainer.getUserRecordID());
                    setPrepareStatement(preStat, 11, currTime);
                    setPrepareStatement(preStat, 12, tmpFormRoutingRule.getID());
                    setPrepareStatement(preStat, 13, tmpFormRoutingRule.getUpdateCount());
                    updateCnt = preStat.executeUpdate();
                } else {
                    sqlStat.append("UPDATE FORM_ROUTING_RULE ");
                    sqlStat.append("SET  FORM_RECORD_ID=?, RULE_NAME=?, RULE_TYPE=?, OPERATION_TYPE=?, FORM_EQUATION=EMPTY_BLOB(), FORM_OPERATION_EQUATION=EMPTY_BLOB(), TARGET_ELEMENT_ID=?, CHECK_RULE=?, UPDATE_COUNT=?, UPDATER_ID=?, UPDATE_DATE=? ");
                    sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                    preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    setPrepareStatement(preStat, 1, tmpFormRoutingRule.getFormRecordID());
                    setPrepareStatement(preStat, 2, tmpFormRoutingRule.getRuleName());
                    setPrepareStatement(preStat, 3, tmpFormRoutingRule.getRuleType());
                    setPrepareStatement(preStat, 4, tmpFormRoutingRule.getOperationType());
                    setPrepareStatement(preStat, 5, tmpFormRoutingRule.getTargetElementID());
                    setPrepareStatement(preStat, 6, tmpFormRoutingRule.getCheckRule());
                    setPrepareStatement(preStat, 7, new Integer(tmpFormRoutingRule.getUpdateCount().intValue() + 1));
                    setPrepareStatement(preStat, 8, sessionContainer.getUserRecordID());
                    setPrepareStatement(preStat, 9, currTime);
                    setPrepareStatement(preStat, 10, tmpFormRoutingRule.getID());
                    setPrepareStatement(preStat, 11, tmpFormRoutingRule.getUpdateCount());
                    updateCnt = preStat.executeUpdate();
                    if (updateCnt > 0) {
                        log.debug("Begin special handling for oracle update blob");
                        ResultSet rs = null;
                        BLOB blob = null;
                        BLOB blob2 = null;
                        sqlStat = new StringBuffer();
                        sqlStat.append("SELECT  FORM_EQUATION, FORM_OPERATION_EQUATION ");
                        sqlStat.append("FROM  FORM_ROUTING_RULE ");
                        sqlStat.append("WHERE ID = " + tmpFormRoutingRule.getID());
                        preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                        rs = preStat.executeQuery();
                        if (rs.next()) {
                            blob = (BLOB) rs.getBlob("FORM_EQUATION");
                            blob2 = (BLOB) rs.getBlob("FORM_OPERATION_EQUATION");
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                            try {
                                ObjectOutputStream out = new ObjectOutputStream(baos);
                                ObjectOutputStream out2 = new ObjectOutputStream(baos2);
                                out.writeObject(tmpFormRoutingRule.getFormEquation());
                                out2.writeObject(tmpFormRoutingRule.getFormOperationEquation());
                                out.flush();
                                log.debug("Finished writing blob");
                            } catch (java.io.IOException ioe) {
                                log.error("Cannot write object stream", ioe);
                                new java.sql.SQLException("Cannot write object stream");
                            }
                            blob.putBytes(1, baos.toByteArray());
                            blob2.putBytes(1, baos2.toByteArray());
                            sqlStat = new StringBuffer();
                            sqlStat.append("UPDATE  FORM_ROUTING_RULE ");
                            sqlStat.append("SET FORM_EQUATION = ?, FORM_OPERATION_EQUATION = ? ");
                            sqlStat.append("WHERE ID = ? ");
                            PreparedStatement preStat2 = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                            preStat2.setBlob(1, blob);
                            preStat2.setBlob(2, blob2);
                            preStat2.setInt(3, tmpFormRoutingRule.getID().intValue());
                            preStat2.executeUpdate();
                            log.debug("Finished updating parameter_object");
                            try {
                                preStat2.close();
                            } catch (Exception ignore) {
                            } finally {
                                preStat2 = null;
                            }
                        }
                        try {
                            rs.close();
                        } catch (Exception ignore) {
                        } finally {
                            rs = null;
                        }
                    }
                }
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    tmpFormRoutingRule.setUpdaterID(sessionContainer.getUserRecordID());
                    tmpFormRoutingRule.setUpdateDate(currTime);
                    tmpFormRoutingRule.setUpdateCount(new Integer(tmpFormRoutingRule.getUpdateCount().intValue() + 1));
                    tmpFormRoutingRule.setCreatorName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getCreatorID()));
                    tmpFormRoutingRule.setUpdaterName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getUpdaterID()));
                    return (tmpFormRoutingRule);
                }
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_UPDATE_ERROR, e);
            } finally {
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }

    protected synchronized AbstractBaseObject delete(AbstractBaseObject obj) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        FormRoutingRule tmpFormRoutingRule = (FormRoutingRule) ((FormRoutingRule) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   FORM_ROUTING_RULE ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpFormRoutingRule.getID());
                setPrepareStatement(preStat, 2, tmpFormRoutingRule.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    return (tmpFormRoutingRule);
                }
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_DELETE_ERROR, e);
            } finally {
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }

    protected synchronized void auditTrail(String opMode, AbstractBaseObject obj) throws ApplicationException {
        Vector oldValues = new Vector();
        Vector newValues = new Vector();
        FormRoutingRule tmpFormRoutingRule = (FormRoutingRule) this.oldValue;
        if (tmpFormRoutingRule != null) {
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getID()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getFormRecordID()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getRuleName()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getRuleType()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getOperationType()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getFormEquation()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getFormOperationEquation()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getTargetElementID()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getCheckRule()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getRecordStatus()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getUpdateCount()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getCreatorID()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getCreateDate()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getUpdaterID()));
            oldValues.add(toAuditTrailValue(tmpFormRoutingRule.getUpdateDate()));
        }
        tmpFormRoutingRule = (FormRoutingRule) obj;
        if (tmpFormRoutingRule != null) {
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getID()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getFormRecordID()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getRuleName()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getRuleType()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getOperationType()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getFormEquation()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getFormOperationEquation()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getTargetElementID()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getCheckRule()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getRecordStatus()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getUpdateCount()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getCreatorID()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getCreateDate()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getUpdaterID()));
            newValues.add(toAuditTrailValue(tmpFormRoutingRule.getUpdateDate()));
        }
        auditTrailBase(opMode, oldValues, newValues);
    }

    /***********************************************************************
   * DON'T Modify the codes above unless you know what you are doing!!!  *
   * Put your own functions beblow.                                      *
   * For FINDER methods, the function name should be in the notation:    *
   *   public Object getObjectBy<Criteria>()                             *
   *   - e.g. public Object getObjectByCode()                            *
   *   public List getListBy<Criteria>()                                 *
   *   - e.g. public List getListByUserID()                              *
   * For OPERATION methods, the function name should be in the notation: *
   *   public void <Operation>ObjectBy<Criteria>()                       *
   *   - e.g. public void deleteObjectByCode()                           *
   *   public void <Operation>ListBy<Criteria>()                         *
   *   - e.g. public void deleteListByUserID()                           *
   ***********************************************************************/
    public synchronized List getListByFormRecordID(String formRecordID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.FORM_RECORD_ID, A.RULE_NAME, A.RULE_TYPE, A.OPERATION_TYPE, A.FORM_EQUATION, A.FORM_OPERATION_EQUATION, A.TARGET_ELEMENT_ID, A.CHECK_RULE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   FORM_ROUTING_RULE A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND A.FORM_RECORD_ID =? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 2, formRecordID);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    FormRoutingRule tmpFormRoutingRule = new FormRoutingRule();
                    tmpFormRoutingRule.setID(getResultSetInteger(rs, "ID"));
                    tmpFormRoutingRule.setFormRecordID(getResultSetInteger(rs, "FORM_RECORD_ID"));
                    tmpFormRoutingRule.setRuleName(getResultSetString(rs, "RULE_NAME"));
                    tmpFormRoutingRule.setRuleType(getResultSetString(rs, "RULE_TYPE"));
                    tmpFormRoutingRule.setOperationType(getResultSetString(rs, "OPERATION_TYPE"));
                    tmpFormRoutingRule.setFormEquation(getResultSetObject(rs, "FORM_EQUATION"));
                    tmpFormRoutingRule.setFormOperationEquation(getResultSetObject(rs, "FORM_OPERATION_EQUATION"));
                    tmpFormRoutingRule.setTargetElementID(getResultSetString(rs, "TARGET_ELEMENT_ID"));
                    tmpFormRoutingRule.setCheckRule(getResultSetString(rs, "CHECK_RULE"));
                    tmpFormRoutingRule.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpFormRoutingRule.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpFormRoutingRule.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpFormRoutingRule.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpFormRoutingRule.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpFormRoutingRule.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpFormRoutingRule.setCreatorName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getCreatorID()));
                    tmpFormRoutingRule.setUpdaterName(UserInfoFactory.getUserFullName(tmpFormRoutingRule.getUpdaterID()));
                    result.add(tmpFormRoutingRule);
                }
                return (result);
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_SELECT_ERROR, e);
            } finally {
                try {
                    rs.close();
                } catch (Exception ignore) {
                } finally {
                    rs = null;
                }
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }

    public void deleteByFormRecordID(Integer formRecordID) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   FORM_ROUTING_RULE ");
                sqlStat.append("WHERE  FORM_RECORD_ID= ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, formRecordID);
                updateCnt = preStat.executeUpdate();
                if (updateCnt < 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                }
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                throw new ApplicationException(ErrorConstant.DB_DELETE_ERROR, e);
            } finally {
                try {
                    preStat.close();
                } catch (Exception ignore) {
                } finally {
                    preStat = null;
                }
            }
        }
    }
}
