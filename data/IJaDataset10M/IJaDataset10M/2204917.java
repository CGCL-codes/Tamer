package com.dcivision.framework.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.framework.Utility;
import com.dcivision.framework.bean.AbstractBaseObject;
import com.dcivision.framework.bean.SysUserDefinedIndexDetail;
import com.dcivision.framework.web.AbstractSearchForm;

/**
  SysUserDefinedIndexDetailDAObject.java

  This class is the data access bean for table "SYS_USER_DEFINED_INDEX_DETAIL".

  @author      Zoe Shum
  @company     DCIVision Limited
  @creation date   13/07/2004
  @version     $Revision: 1.20.4.1 $
*/
public class SysUserDefinedIndexDetailDAObject extends AbstractDAObject {

    public static final String REVISION = "$Revision: 1.20.4.1 $";

    public static final String TABLE_NAME = "SYS_USER_DEFINED_INDEX_DETAIL";

    public SysUserDefinedIndexDetailDAObject(SessionContainer sessionContainer, Connection dbConn) {
        super(sessionContainer, dbConn);
    }

    protected void initDBSetting() {
        this.baseTableName = TABLE_NAME;
        this.vecDBColumn.add("ID");
        this.vecDBColumn.add("USER_DEFINED_ID");
        this.vecDBColumn.add("FIELD_NAME");
        this.vecDBColumn.add("MANDATORY");
        this.vecDBColumn.add("VALUE_SUGGEST");
        this.vecDBColumn.add("FIELD_TYPE");
        this.vecDBColumn.add("REFERENCE");
        this.vecDBColumn.add("SQL_STAT");
        this.vecDBColumn.add("RECORD_STATUS");
        this.vecDBColumn.add("UPDATE_COUNT");
        this.vecDBColumn.add("CREATOR_ID");
        this.vecDBColumn.add("CREATE_DATE");
        this.vecDBColumn.add("UPDATER_ID");
        this.vecDBColumn.add("UPDATE_DATE");
        this.vecDBColumn.add("DISPLAY_SEQ");
    }

    protected synchronized AbstractBaseObject getByID(Integer id) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.USER_DEFINED_ID, A.FIELD_NAME, A.MANDATORY, A.VALUE_SUGGEST, A.FIELD_TYPE, A.REFERENCE, A.SQL_STAT, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   SYS_USER_DEFINED_INDEX_DETAIL A ");
                sqlStat.append("WHERE  A.ID = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, id);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = new SysUserDefinedIndexDetail();
                    tmpSysUserDefinedIndexDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpSysUserDefinedIndexDetail.setUserDefinedID(getResultSetInteger(rs, "USER_DEFINED_ID"));
                    tmpSysUserDefinedIndexDetail.setFieldName(getResultSetString(rs, "FIELD_NAME"));
                    tmpSysUserDefinedIndexDetail.setMandatory(getResultSetString(rs, "MANDATORY"));
                    tmpSysUserDefinedIndexDetail.setValueSuggest(getResultSetString(rs, "VALUE_SUGGEST"));
                    tmpSysUserDefinedIndexDetail.setFieldType(getResultSetString(rs, "FIELD_TYPE"));
                    tmpSysUserDefinedIndexDetail.setReference(getResultSetString(rs, "REFERENCE"));
                    tmpSysUserDefinedIndexDetail.setSqlStat(getResultSetString(rs, "SQL_STAT"));
                    tmpSysUserDefinedIndexDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpSysUserDefinedIndexDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpSysUserDefinedIndexDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpSysUserDefinedIndexDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpSysUserDefinedIndexDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getCreatorID()));
                    tmpSysUserDefinedIndexDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getUpdaterID()));
                    return (tmpSysUserDefinedIndexDetail);
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
                sqlStat.append("SELECT A.ID, A.USER_DEFINED_ID, A.VALUE_SUGGEST, A.FIELD_NAME, A.MANDATORY, A.FIELD_TYPE, A.REFERENCE, A.SQL_STAT, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   SYS_USER_DEFINED_INDEX_DETAIL A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                sqlStat.append("ORDER BY DISPLAY_SEQ");
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
                    SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = new SysUserDefinedIndexDetail();
                    tmpSysUserDefinedIndexDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpSysUserDefinedIndexDetail.setUserDefinedID(getResultSetInteger(rs, "USER_DEFINED_ID"));
                    tmpSysUserDefinedIndexDetail.setFieldName(getResultSetString(rs, "FIELD_NAME"));
                    tmpSysUserDefinedIndexDetail.setMandatory(getResultSetString(rs, "MANDATORY"));
                    tmpSysUserDefinedIndexDetail.setValueSuggest(getResultSetString(rs, "VALUE_SUGGEST"));
                    tmpSysUserDefinedIndexDetail.setFieldType(getResultSetString(rs, "FIELD_TYPE"));
                    tmpSysUserDefinedIndexDetail.setReference(getResultSetString(rs, "REFERENCE"));
                    tmpSysUserDefinedIndexDetail.setSqlStat(getResultSetString(rs, "SQL_STAT"));
                    tmpSysUserDefinedIndexDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpSysUserDefinedIndexDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpSysUserDefinedIndexDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpSysUserDefinedIndexDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpSysUserDefinedIndexDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getCreatorID()));
                    tmpSysUserDefinedIndexDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getUpdaterID()));
                    tmpSysUserDefinedIndexDetail.setRecordCount(totalNumOfRecord);
                    tmpSysUserDefinedIndexDetail.setRowNum(startOffset++);
                    ++rowLoopCnt;
                    result.add(tmpSysUserDefinedIndexDetail);
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
                sqlStat.append("SELECT A.ID, A.USER_DEFINED_ID, A.FIELD_NAME, A.MANDATORY, ,A.VALUE_SUGGEST, A.FIELD_TYPE, A.REFERENCE, A.SQL_STAT, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   SYS_USER_DEFINED_INDEX_DETAIL A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                sqlStat.append("ORDER BY DISPLAY_SEQ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = new SysUserDefinedIndexDetail();
                    tmpSysUserDefinedIndexDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpSysUserDefinedIndexDetail.setUserDefinedID(getResultSetInteger(rs, "USER_DEFINED_ID"));
                    tmpSysUserDefinedIndexDetail.setFieldName(getResultSetString(rs, "FIELD_NAME"));
                    tmpSysUserDefinedIndexDetail.setMandatory(getResultSetString(rs, "MANDATORY"));
                    tmpSysUserDefinedIndexDetail.setValueSuggest(getResultSetString(rs, "VALUE_SUGGEST"));
                    tmpSysUserDefinedIndexDetail.setFieldType(getResultSetString(rs, "FIELD_TYPE"));
                    tmpSysUserDefinedIndexDetail.setReference(getResultSetString(rs, "REFERENCE"));
                    tmpSysUserDefinedIndexDetail.setSqlStat(getResultSetString(rs, "SQL_STAT"));
                    tmpSysUserDefinedIndexDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpSysUserDefinedIndexDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpSysUserDefinedIndexDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpSysUserDefinedIndexDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpSysUserDefinedIndexDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getCreatorID()));
                    tmpSysUserDefinedIndexDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getUpdaterID()));
                    result.add(tmpSysUserDefinedIndexDetail);
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
        SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = (SysUserDefinedIndexDetail) ((SysUserDefinedIndexDetail) obj).clone();
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("INSERT ");
                sqlStat.append("INTO   SYS_USER_DEFINED_INDEX_DETAIL(ID, USER_DEFINED_ID, FIELD_NAME, MANDATORY, VALUE_SUGGEST , FIELD_TYPE, REFERENCE, SQL_STAT, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE, DISPLAY_SEQ) ");
                sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, nextID);
                setPrepareStatement(preStat, 2, tmpSysUserDefinedIndexDetail.getUserDefinedID());
                setPrepareStatement(preStat, 3, tmpSysUserDefinedIndexDetail.getFieldName());
                setPrepareStatement(preStat, 4, tmpSysUserDefinedIndexDetail.getMandatory());
                setPrepareStatement(preStat, 5, tmpSysUserDefinedIndexDetail.getValueSuggest());
                setPrepareStatement(preStat, 6, tmpSysUserDefinedIndexDetail.getFieldType());
                setPrepareStatement(preStat, 7, tmpSysUserDefinedIndexDetail.getReference());
                setPrepareStatement(preStat, 8, tmpSysUserDefinedIndexDetail.getSqlStat());
                setPrepareStatement(preStat, 9, GlobalConstant.RECORD_STATUS_ACTIVE);
                setPrepareStatement(preStat, 10, new Integer(0));
                if (sessionContainer != null && sessionContainer.getUserRecordID() != null) {
                    setPrepareStatement(preStat, 11, sessionContainer.getUserRecordID());
                } else {
                    setPrepareStatement(preStat, 11, tmpSysUserDefinedIndexDetail.getCreatorID());
                }
                setPrepareStatement(preStat, 12, currTime);
                if (sessionContainer != null && sessionContainer.getUserRecordID() != null) {
                    setPrepareStatement(preStat, 13, sessionContainer.getUserRecordID());
                } else {
                    setPrepareStatement(preStat, 13, tmpSysUserDefinedIndexDetail.getCreatorID());
                }
                setPrepareStatement(preStat, 14, currTime);
                if (Utility.isEmpty(tmpSysUserDefinedIndexDetail.getDisplaySeq())) {
                    setPrepareStatement(preStat, 15, new Integer(0));
                } else {
                    setPrepareStatement(preStat, 15, tmpSysUserDefinedIndexDetail.getDisplaySeq());
                }
                preStat.executeUpdate();
                tmpSysUserDefinedIndexDetail.setID(nextID);
                if (sessionContainer != null && sessionContainer.getUserRecordID() != null) {
                    tmpSysUserDefinedIndexDetail.setCreatorID(sessionContainer.getUserRecordID());
                } else {
                    tmpSysUserDefinedIndexDetail.setCreatorID(tmpSysUserDefinedIndexDetail.getCreatorID());
                }
                tmpSysUserDefinedIndexDetail.setCreateDate(currTime);
                if (sessionContainer != null && sessionContainer.getUserRecordID() != null) {
                    tmpSysUserDefinedIndexDetail.setUpdaterID(sessionContainer.getUserRecordID());
                } else {
                    tmpSysUserDefinedIndexDetail.setUpdaterID(tmpSysUserDefinedIndexDetail.getCreatorID());
                }
                tmpSysUserDefinedIndexDetail.setUpdateDate(currTime);
                tmpSysUserDefinedIndexDetail.setUpdateCount(new Integer(0));
                tmpSysUserDefinedIndexDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getCreatorID()));
                tmpSysUserDefinedIndexDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getUpdaterID()));
                return (tmpSysUserDefinedIndexDetail);
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
        SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = (SysUserDefinedIndexDetail) ((SysUserDefinedIndexDetail) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("UPDATE SYS_USER_DEFINED_INDEX_DETAIL ");
                sqlStat.append("SET  USER_DEFINED_ID=?, FIELD_NAME=?, MANDATORY=?, VALUE_SUGGEST=?, FIELD_TYPE=?, REFERENCE=?, SQL_STAT=?, UPDATE_COUNT=?, UPDATER_ID=?, UPDATE_DATE=?, DISPLAY_SEQ=? ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpSysUserDefinedIndexDetail.getUserDefinedID());
                setPrepareStatement(preStat, 2, tmpSysUserDefinedIndexDetail.getFieldName());
                setPrepareStatement(preStat, 3, tmpSysUserDefinedIndexDetail.getMandatory());
                setPrepareStatement(preStat, 4, tmpSysUserDefinedIndexDetail.getValueSuggest());
                setPrepareStatement(preStat, 5, tmpSysUserDefinedIndexDetail.getFieldType());
                setPrepareStatement(preStat, 6, tmpSysUserDefinedIndexDetail.getReference());
                setPrepareStatement(preStat, 7, tmpSysUserDefinedIndexDetail.getSqlStat());
                setPrepareStatement(preStat, 8, new Integer(tmpSysUserDefinedIndexDetail.getUpdateCount().intValue() + 1));
                setPrepareStatement(preStat, 9, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 10, currTime);
                if (Utility.isEmpty(tmpSysUserDefinedIndexDetail.getDisplaySeq())) {
                    setPrepareStatement(preStat, 11, new Integer(0));
                } else {
                    setPrepareStatement(preStat, 11, tmpSysUserDefinedIndexDetail.getDisplaySeq());
                }
                setPrepareStatement(preStat, 12, tmpSysUserDefinedIndexDetail.getID());
                setPrepareStatement(preStat, 13, tmpSysUserDefinedIndexDetail.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    tmpSysUserDefinedIndexDetail.setUpdaterID(sessionContainer.getUserRecordID());
                    tmpSysUserDefinedIndexDetail.setUpdateDate(currTime);
                    tmpSysUserDefinedIndexDetail.setUpdateCount(new Integer(tmpSysUserDefinedIndexDetail.getUpdateCount().intValue() + 1));
                    tmpSysUserDefinedIndexDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getCreatorID()));
                    tmpSysUserDefinedIndexDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getUpdaterID()));
                    return (tmpSysUserDefinedIndexDetail);
                }
            } catch (ApplicationException appEx) {
                throw appEx;
            } catch (SQLException sqle) {
                log.error(sqle, sqle);
                throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
            } catch (Exception e) {
                log.error(e, e);
                return null;
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
        SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = (SysUserDefinedIndexDetail) ((SysUserDefinedIndexDetail) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   SYS_USER_DEFINED_INDEX_DETAIL ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpSysUserDefinedIndexDetail.getID());
                setPrepareStatement(preStat, 2, tmpSysUserDefinedIndexDetail.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    return (tmpSysUserDefinedIndexDetail);
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
        SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = (SysUserDefinedIndexDetail) this.oldValue;
        if (tmpSysUserDefinedIndexDetail != null) {
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getID()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getUserDefinedID()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getFieldName()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getMandatory()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getValueSuggest()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getFieldType()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getReference()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getSqlStat()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getRecordStatus()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getUpdateCount()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getCreatorID()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getCreateDate()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getUpdaterID()));
            oldValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getUpdateDate()));
        }
        tmpSysUserDefinedIndexDetail = (SysUserDefinedIndexDetail) obj;
        if (tmpSysUserDefinedIndexDetail != null) {
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getID()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getUserDefinedID()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getFieldName()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getMandatory()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getValueSuggest()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getFieldType()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getReference()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getSqlStat()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getRecordStatus()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getUpdateCount()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getCreatorID()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getCreateDate()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getUpdaterID()));
            newValues.add(toAuditTrailValue(tmpSysUserDefinedIndexDetail.getUpdateDate()));
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
    public synchronized List getListBySysUserDefinedIndexID(Integer id) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.USER_DEFINED_ID, A.FIELD_NAME, A.MANDATORY, A.VALUE_SUGGEST, A.FIELD_TYPE, A.REFERENCE, A.SQL_STAT, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE, A.DISPLAY_SEQ  ");
                sqlStat.append("FROM   SYS_USER_DEFINED_INDEX_DETAIL A ");
                sqlStat.append("WHERE  A.USER_DEFINED_ID = ? AND RECORD_STATUS = ? ");
                sqlStat.append("ORDER BY A.DISPLAY_SEQ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, id);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = new SysUserDefinedIndexDetail();
                    tmpSysUserDefinedIndexDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpSysUserDefinedIndexDetail.setUserDefinedID(getResultSetInteger(rs, "USER_DEFINED_ID"));
                    tmpSysUserDefinedIndexDetail.setFieldName(getResultSetString(rs, "FIELD_NAME"));
                    tmpSysUserDefinedIndexDetail.setMandatory(getResultSetString(rs, "MANDATORY"));
                    tmpSysUserDefinedIndexDetail.setValueSuggest(getResultSetString(rs, "VALUE_SUGGEST"));
                    tmpSysUserDefinedIndexDetail.setFieldType(getResultSetString(rs, "FIELD_TYPE"));
                    tmpSysUserDefinedIndexDetail.setReference(getResultSetString(rs, "REFERENCE"));
                    tmpSysUserDefinedIndexDetail.setSqlStat(getResultSetString(rs, "SQL_STAT"));
                    tmpSysUserDefinedIndexDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpSysUserDefinedIndexDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpSysUserDefinedIndexDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpSysUserDefinedIndexDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpSysUserDefinedIndexDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setDisplaySeq(getResultSetInteger(rs, "DISPLAY_SEQ"));
                    tmpSysUserDefinedIndexDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getCreatorID()));
                    tmpSysUserDefinedIndexDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getUpdaterID()));
                    if (SysUserDefinedIndexDetail.FIELD_TYPE_SELECT_DATABASE.equals(tmpSysUserDefinedIndexDetail.getFieldType())) {
                        PreparedStatement preStatFieldValue = null;
                        ResultSet rsFieldValue = null;
                        StringBuffer sqlStatFieldValue = new StringBuffer();
                        String tempValue = "";
                        String defaultValue = "";
                        synchronized (dbConn) {
                            try {
                                sqlStatFieldValue.append(tmpSysUserDefinedIndexDetail.getSqlStat());
                                preStatFieldValue = dbConn.prepareStatement(sqlStatFieldValue.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                this.setPrepareStatement(preStatFieldValue, 1, tmpSysUserDefinedIndexDetail.getID());
                                rsFieldValue = preStatFieldValue.executeQuery();
                                while (rsFieldValue.next()) {
                                    tempValue = tempValue + getResultSetString(rsFieldValue, "FIELD_VALUE") + ";";
                                }
                                tmpSysUserDefinedIndexDetail.setFieldValue(tempValue);
                                tmpSysUserDefinedIndexDetail.setDefaultValue(defaultValue);
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
                                    rsFieldValue.close();
                                } catch (Exception ignore) {
                                } finally {
                                    rsFieldValue = null;
                                }
                                try {
                                    preStatFieldValue.close();
                                } catch (Exception ignore) {
                                } finally {
                                    preStatFieldValue = null;
                                }
                            }
                        }
                    }
                    result.add(tmpSysUserDefinedIndexDetail);
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

    public synchronized AbstractBaseObject getObjectByName(Integer udfID, String name) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = null;
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.USER_DEFINED_ID, A.FIELD_NAME, A.MANDATORY, A.VALUE_SUGGEST, A.FIELD_TYPE, A.REFERENCE, A.SQL_STAT, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   SYS_USER_DEFINED_INDEX_DETAIL A ");
                sqlStat.append("WHERE  A.USER_DEFINED_ID = ? AND A.FIELD_NAME = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, udfID);
                this.setPrepareStatement(preStat, 2, name);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    tmpSysUserDefinedIndexDetail = new SysUserDefinedIndexDetail();
                    tmpSysUserDefinedIndexDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpSysUserDefinedIndexDetail.setUserDefinedID(getResultSetInteger(rs, "USER_DEFINED_ID"));
                    tmpSysUserDefinedIndexDetail.setFieldName(getResultSetString(rs, "FIELD_NAME"));
                    tmpSysUserDefinedIndexDetail.setMandatory(getResultSetString(rs, "MANDATORY"));
                    tmpSysUserDefinedIndexDetail.setValueSuggest(getResultSetString(rs, "VALUE_SUGGEST"));
                    tmpSysUserDefinedIndexDetail.setFieldType(getResultSetString(rs, "FIELD_TYPE"));
                    tmpSysUserDefinedIndexDetail.setReference(getResultSetString(rs, "REFERENCE"));
                    tmpSysUserDefinedIndexDetail.setSqlStat(getResultSetString(rs, "SQL_STAT"));
                    tmpSysUserDefinedIndexDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpSysUserDefinedIndexDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpSysUserDefinedIndexDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpSysUserDefinedIndexDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpSysUserDefinedIndexDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getCreatorID()));
                    tmpSysUserDefinedIndexDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getUpdaterID()));
                }
                return (tmpSysUserDefinedIndexDetail);
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

    public synchronized List getListUDFDetailByUDFName(String udfName) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        SysUserDefinedIndexDetail tmpSysUserDefinedIndexDetail = null;
        List udfDetailList = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.USER_DEFINED_ID, A.FIELD_NAME, A.MANDATORY, A.VALUE_SUGGEST, A.FIELD_TYPE, A.REFERENCE, A.SQL_STAT, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   SYS_USER_DEFINED_INDEX_DETAIL A, SYS_USER_DEFINED_INDEX S ");
                sqlStat.append("WHERE  S.USER_DEFINED_TYPE = ? AND S.ID=A.USER_DEFINED_ID ");
                sqlStat.append("ORDER BY A.DISPLAY_SEQ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, udfName);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    tmpSysUserDefinedIndexDetail = new SysUserDefinedIndexDetail();
                    tmpSysUserDefinedIndexDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpSysUserDefinedIndexDetail.setUserDefinedID(getResultSetInteger(rs, "USER_DEFINED_ID"));
                    tmpSysUserDefinedIndexDetail.setFieldName(getResultSetString(rs, "FIELD_NAME"));
                    tmpSysUserDefinedIndexDetail.setMandatory(getResultSetString(rs, "MANDATORY"));
                    tmpSysUserDefinedIndexDetail.setValueSuggest(getResultSetString(rs, "VALUE_SUGGEST"));
                    tmpSysUserDefinedIndexDetail.setFieldType(getResultSetString(rs, "FIELD_TYPE"));
                    tmpSysUserDefinedIndexDetail.setReference(getResultSetString(rs, "REFERENCE"));
                    tmpSysUserDefinedIndexDetail.setSqlStat(getResultSetString(rs, "SQL_STAT"));
                    tmpSysUserDefinedIndexDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpSysUserDefinedIndexDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpSysUserDefinedIndexDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpSysUserDefinedIndexDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpSysUserDefinedIndexDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpSysUserDefinedIndexDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getCreatorID()));
                    tmpSysUserDefinedIndexDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpSysUserDefinedIndexDetail.getUpdaterID()));
                    udfDetailList.add(tmpSysUserDefinedIndexDetail);
                }
                return (udfDetailList);
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

    /**
   * 
   * @param lastUpdateDate
   * @return
   * @throws ApplicationException
   */
    public synchronized boolean isRequiredUpdate(java.sql.Timestamp lastUpdateDate) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT COUNT(*) ");
                sqlStat.append("FROM   SYS_USER_DEFINED_INDEX_DETAIL ");
                sqlStat.append("WHERE   UPDATE_DATE > ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, lastUpdateDate);
                rs = preStat.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    return false;
                }
                return true;
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

    public synchronized Timestamp getMaxUpdateDate() throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT MAX(UPDATE_DATE) AS MAX_UPDATE_DATE ");
                sqlStat.append("FROM   SYS_USER_DEFINED_INDEX_DETAIL ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    return getResultSetTimestamp(rs, "MAX_UPDATE_DATE");
                } else {
                    return null;
                }
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
}
