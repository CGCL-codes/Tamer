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
import com.dcivision.framework.bean.ProtectedResource;
import com.dcivision.framework.web.AbstractSearchForm;

/**
  ProtectedResourceDAObject.java

  This class is the data access bean for table "PROTECTED_RESOURCE".

  @author      Dick.xie
  @company     DCIVision Limited
  @creation date   19/04/2005
  @version     $Revision: 1.2 $
*/
public class ProtectedResourceDAObject extends AbstractDAObject {

    public static final String REVISION = "$Revision: 1.2 $";

    public static final String TABLE_NAME = "PROTECTED_RESOURCE";

    public ProtectedResourceDAObject(SessionContainer sessionContainer, Connection dbConn) {
        super(sessionContainer, dbConn);
    }

    protected void initDBSetting() {
        this.baseTableName = TABLE_NAME;
        this.vecDBColumn.add("ID");
        this.vecDBColumn.add("URL_PATTERN");
        this.vecDBColumn.add("NAV_MODE");
        this.vecDBColumn.add("OP_MODE");
        this.vecDBColumn.add("HELPER_CLASS");
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
                sqlStat.append("SELECT A.ID, A.URL_PATTERN, A.NAV_MODE, A.OP_MODE, A.HELPER_CLASS, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   PROTECTED_RESOURCE A ");
                sqlStat.append("WHERE  A.ID = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, id);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    ProtectedResource tmpProtectedResource = new ProtectedResource();
                    tmpProtectedResource.setID(getResultSetInteger(rs, "ID"));
                    tmpProtectedResource.setUrlPattern(getResultSetString(rs, "URL_PATTERN"));
                    tmpProtectedResource.setNavMode(getResultSetString(rs, "NAV_MODE"));
                    tmpProtectedResource.setOpMode(getResultSetString(rs, "OP_MODE"));
                    tmpProtectedResource.setHelperClass(getResultSetString(rs, "HELPER_CLASS"));
                    tmpProtectedResource.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpProtectedResource.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpProtectedResource.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpProtectedResource.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpProtectedResource.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpProtectedResource.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpProtectedResource.setCreatorName(UserInfoFactory.getUserFullName(tmpProtectedResource.getCreatorID()));
                    tmpProtectedResource.setUpdaterName(UserInfoFactory.getUserFullName(tmpProtectedResource.getUpdaterID()));
                    return (tmpProtectedResource);
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
                sqlStat.append("SELECT A.ID, A.URL_PATTERN, A.NAV_MODE, A.OP_MODE, A.HELPER_CLASS, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   PROTECTED_RESOURCE A ");
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
                    ProtectedResource tmpProtectedResource = new ProtectedResource();
                    tmpProtectedResource.setID(getResultSetInteger(rs, "ID"));
                    tmpProtectedResource.setUrlPattern(getResultSetString(rs, "URL_PATTERN"));
                    tmpProtectedResource.setNavMode(getResultSetString(rs, "NAV_MODE"));
                    tmpProtectedResource.setOpMode(getResultSetString(rs, "OP_MODE"));
                    tmpProtectedResource.setHelperClass(getResultSetString(rs, "HELPER_CLASS"));
                    tmpProtectedResource.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpProtectedResource.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpProtectedResource.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpProtectedResource.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpProtectedResource.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpProtectedResource.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpProtectedResource.setCreatorName(UserInfoFactory.getUserFullName(tmpProtectedResource.getCreatorID()));
                    tmpProtectedResource.setUpdaterName(UserInfoFactory.getUserFullName(tmpProtectedResource.getUpdaterID()));
                    tmpProtectedResource.setRecordCount(totalNumOfRecord);
                    tmpProtectedResource.setRowNum(startOffset++);
                    ++rowLoopCnt;
                    result.add(tmpProtectedResource);
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
                sqlStat.append("SELECT A.ID, A.URL_PATTERN, A.NAV_MODE, A.OP_MODE, A.HELPER_CLASS, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   PROTECTED_RESOURCE A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    ProtectedResource tmpProtectedResource = new ProtectedResource();
                    tmpProtectedResource.setID(getResultSetInteger(rs, "ID"));
                    tmpProtectedResource.setUrlPattern(getResultSetString(rs, "URL_PATTERN"));
                    tmpProtectedResource.setNavMode(getResultSetString(rs, "NAV_MODE"));
                    tmpProtectedResource.setOpMode(getResultSetString(rs, "OP_MODE"));
                    tmpProtectedResource.setHelperClass(getResultSetString(rs, "HELPER_CLASS"));
                    tmpProtectedResource.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpProtectedResource.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpProtectedResource.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpProtectedResource.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpProtectedResource.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpProtectedResource.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpProtectedResource.setCreatorName(UserInfoFactory.getUserFullName(tmpProtectedResource.getCreatorID()));
                    tmpProtectedResource.setUpdaterName(UserInfoFactory.getUserFullName(tmpProtectedResource.getUpdaterID()));
                    result.add(tmpProtectedResource);
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

    public synchronized List getListByUrl(String urlPattern) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.URL_PATTERN, A.NAV_MODE, A.OP_MODE, A.HELPER_CLASS, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   PROTECTED_RESOURCE A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND A.URL_PATTERN = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 2, urlPattern);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    ProtectedResource tmpProtectedResource = new ProtectedResource();
                    tmpProtectedResource.setID(getResultSetInteger(rs, "ID"));
                    tmpProtectedResource.setUrlPattern(getResultSetString(rs, "URL_PATTERN"));
                    tmpProtectedResource.setNavMode(getResultSetString(rs, "NAV_MODE"));
                    tmpProtectedResource.setOpMode(getResultSetString(rs, "OP_MODE"));
                    tmpProtectedResource.setHelperClass(getResultSetString(rs, "HELPER_CLASS"));
                    tmpProtectedResource.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpProtectedResource.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpProtectedResource.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpProtectedResource.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpProtectedResource.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpProtectedResource.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpProtectedResource.setCreatorName(UserInfoFactory.getUserFullName(tmpProtectedResource.getCreatorID()));
                    tmpProtectedResource.setUpdaterName(UserInfoFactory.getUserFullName(tmpProtectedResource.getUpdaterID()));
                    result.add(tmpProtectedResource);
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
        ProtectedResource tmpProtectedResource = (ProtectedResource) ((ProtectedResource) obj).clone();
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("INSERT ");
                sqlStat.append("INTO   PROTECTED_RESOURCE(ID, URL_PATTERN, NAV_MODE, OP_MODE, HELPER_CLASS, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE) ");
                sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, nextID);
                setPrepareStatement(preStat, 2, tmpProtectedResource.getUrlPattern());
                setPrepareStatement(preStat, 3, tmpProtectedResource.getNavMode());
                setPrepareStatement(preStat, 4, tmpProtectedResource.getOpMode());
                setPrepareStatement(preStat, 5, tmpProtectedResource.getHelperClass());
                setPrepareStatement(preStat, 6, GlobalConstant.RECORD_STATUS_ACTIVE);
                setPrepareStatement(preStat, 7, new Integer(0));
                setPrepareStatement(preStat, 8, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 9, currTime);
                setPrepareStatement(preStat, 10, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 11, currTime);
                preStat.executeUpdate();
                tmpProtectedResource.setID(nextID);
                tmpProtectedResource.setCreatorID(sessionContainer.getUserRecordID());
                tmpProtectedResource.setCreateDate(currTime);
                tmpProtectedResource.setUpdaterID(sessionContainer.getUserRecordID());
                tmpProtectedResource.setUpdateDate(currTime);
                tmpProtectedResource.setUpdateCount(new Integer(0));
                tmpProtectedResource.setCreatorName(UserInfoFactory.getUserFullName(tmpProtectedResource.getCreatorID()));
                tmpProtectedResource.setUpdaterName(UserInfoFactory.getUserFullName(tmpProtectedResource.getUpdaterID()));
                return (tmpProtectedResource);
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
        ProtectedResource tmpProtectedResource = (ProtectedResource) ((ProtectedResource) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("UPDATE PROTECTED_RESOURCE ");
                sqlStat.append("SET  URL_PATTERN=?, NAV_MODE=?, OP_MODE=?, HELPER_CLASS=?, UPDATE_COUNT=?, UPDATER_ID=?, UPDATE_DATE=? ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpProtectedResource.getUrlPattern());
                setPrepareStatement(preStat, 2, tmpProtectedResource.getNavMode());
                setPrepareStatement(preStat, 3, tmpProtectedResource.getOpMode());
                setPrepareStatement(preStat, 4, tmpProtectedResource.getHelperClass());
                setPrepareStatement(preStat, 5, new Integer(tmpProtectedResource.getUpdateCount().intValue() + 1));
                setPrepareStatement(preStat, 6, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 7, currTime);
                setPrepareStatement(preStat, 8, tmpProtectedResource.getID());
                setPrepareStatement(preStat, 9, tmpProtectedResource.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    tmpProtectedResource.setUpdaterID(sessionContainer.getUserRecordID());
                    tmpProtectedResource.setUpdateDate(currTime);
                    tmpProtectedResource.setUpdateCount(new Integer(tmpProtectedResource.getUpdateCount().intValue() + 1));
                    tmpProtectedResource.setCreatorName(UserInfoFactory.getUserFullName(tmpProtectedResource.getCreatorID()));
                    tmpProtectedResource.setUpdaterName(UserInfoFactory.getUserFullName(tmpProtectedResource.getUpdaterID()));
                    return (tmpProtectedResource);
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
        ProtectedResource tmpProtectedResource = (ProtectedResource) ((ProtectedResource) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   PROTECTED_RESOURCE ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpProtectedResource.getID());
                setPrepareStatement(preStat, 2, tmpProtectedResource.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    return (tmpProtectedResource);
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
        ProtectedResource tmpProtectedResource = (ProtectedResource) this.oldValue;
        if (tmpProtectedResource != null) {
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getID()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getUrlPattern()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getNavMode()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getOpMode()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getHelperClass()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getRecordStatus()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getUpdateCount()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getCreatorID()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getCreateDate()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getUpdaterID()));
            oldValues.add(toAuditTrailValue(tmpProtectedResource.getUpdateDate()));
        }
        tmpProtectedResource = (ProtectedResource) obj;
        if (tmpProtectedResource != null) {
            newValues.add(toAuditTrailValue(tmpProtectedResource.getID()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getUrlPattern()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getNavMode()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getOpMode()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getHelperClass()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getRecordStatus()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getUpdateCount()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getCreatorID()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getCreateDate()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getUpdaterID()));
            newValues.add(toAuditTrailValue(tmpProtectedResource.getUpdateDate()));
        }
        auditTrailBase(opMode, oldValues, newValues);
    }
}
