package com.dcivision.alert.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.dcivision.alert.bean.UpdateAlertLogAction;
import com.dcivision.framework.ApplicationException;
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
  UpdateAlertLogActionDAObject.java

  This class is the data access bean for table "UPDATE_ALERT_LOG_ACTION".

  @author      Zoe Shum
  @company     DCIVision Limited
  @creation date   28/01/2004
  @version     $Revision: 1.7 $
*/
public class UpdateAlertLogActionDAObject extends AbstractDAObject {

    public static final String REVISION = "$Revision: 1.7 $";

    public static final String TABLE_NAME = "UPDATE_ALERT_LOG_ACTION";

    public UpdateAlertLogActionDAObject(SessionContainer sessionContainer, Connection dbConn) {
        super(sessionContainer, dbConn);
    }

    protected void initDBSetting() {
        this.baseTableName = TABLE_NAME;
        this.vecDBColumn.add("ID");
        this.vecDBColumn.add("UPDATE_ALERT_ID");
        this.vecDBColumn.add("UPDATE_ALERT_TYPE_ID");
        this.vecDBColumn.add("UPDATE_ALERT_SYSTEM_LOG_ID");
        this.vecDBColumn.add("ACTION_TYPE");
        this.vecDBColumn.add("ACTION_TYPE_LEVEL");
        this.vecDBColumn.add("DESCRIPTION");
        this.vecDBColumn.add("ACTOR_ID");
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
                sqlStat.append("SELECT A.ID, A.UPDATE_ALERT_ID, A.UPDATE_ALERT_TYPE_ID, A.UPDATE_ALERT_SYSTEM_LOG_ID, A.ACTION_TYPE, A.ACTION_TYPE_LEVEL, A.DESCRIPTION, A.ACTOR_ID, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   UPDATE_ALERT_LOG_ACTION A ");
                sqlStat.append("WHERE  A.ID = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, id);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    UpdateAlertLogAction tmpUpdateAlertLogAction = new UpdateAlertLogAction();
                    tmpUpdateAlertLogAction.setID(getResultSetInteger(rs, "ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertID(getResultSetInteger(rs, "UPDATE_ALERT_ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertTypeID(getResultSetInteger(rs, "UPDATE_ALERT_TYPE_ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertSystemLogID(getResultSetInteger(rs, "UPDATE_ALERT_SYSTEM_LOG_ID"));
                    tmpUpdateAlertLogAction.setActionType(getResultSetString(rs, "ACTION_TYPE"));
                    tmpUpdateAlertLogAction.setActionTypeLevel(getResultSetString(rs, "ACTION_TYPE_LEVEL"));
                    tmpUpdateAlertLogAction.setDescription(getResultSetString(rs, "DESCRIPTION"));
                    tmpUpdateAlertLogAction.setActorID(getResultSetInteger(rs, "ACTOR_ID"));
                    tmpUpdateAlertLogAction.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUpdateAlertLogAction.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUpdateAlertLogAction.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUpdateAlertLogAction.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUpdateAlertLogAction.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUpdateAlertLogAction.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUpdateAlertLogAction.setCreatorName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getCreatorID()));
                    tmpUpdateAlertLogAction.setUpdaterName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getUpdaterID()));
                    return (tmpUpdateAlertLogAction);
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
                sqlStat.append("SELECT A.ID, A.UPDATE_ALERT_ID, A.UPDATE_ALERT_TYPE_ID, A.UPDATE_ALERT_SYSTEM_LOG_ID, A.ACTION_TYPE, A.ACTION_TYPE_LEVEL, A.DESCRIPTION, A.ACTOR_ID, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   UPDATE_ALERT_LOG_ACTION A ");
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
                    UpdateAlertLogAction tmpUpdateAlertLogAction = new UpdateAlertLogAction();
                    tmpUpdateAlertLogAction.setID(getResultSetInteger(rs, "ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertID(getResultSetInteger(rs, "UPDATE_ALERT_ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertTypeID(getResultSetInteger(rs, "UPDATE_ALERT_TYPE_ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertSystemLogID(getResultSetInteger(rs, "UPDATE_ALERT_SYSTEM_LOG_ID"));
                    tmpUpdateAlertLogAction.setActionType(getResultSetString(rs, "ACTION_TYPE"));
                    tmpUpdateAlertLogAction.setActionTypeLevel(getResultSetString(rs, "ACTION_TYPE_LEVEL"));
                    tmpUpdateAlertLogAction.setDescription(getResultSetString(rs, "DESCRIPTION"));
                    tmpUpdateAlertLogAction.setActorID(getResultSetInteger(rs, "ACTOR_ID"));
                    tmpUpdateAlertLogAction.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUpdateAlertLogAction.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUpdateAlertLogAction.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUpdateAlertLogAction.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUpdateAlertLogAction.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUpdateAlertLogAction.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUpdateAlertLogAction.setCreatorName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getCreatorID()));
                    tmpUpdateAlertLogAction.setUpdaterName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getUpdaterID()));
                    tmpUpdateAlertLogAction.setRecordCount(totalNumOfRecord);
                    tmpUpdateAlertLogAction.setRowNum(startOffset++);
                    ++rowLoopCnt;
                    result.add(tmpUpdateAlertLogAction);
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
                sqlStat.append("SELECT A.ID, A.UPDATE_ALERT_ID, A.UPDATE_ALERT_TYPE_ID, A.UPDATE_ALERT_SYSTEM_LOG_ID, A.ACTION_TYPE, A.ACTION_TYPE_LEVEL, A.DESCRIPTION, A.ACTOR_ID, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   UPDATE_ALERT_LOG_ACTION A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    UpdateAlertLogAction tmpUpdateAlertLogAction = new UpdateAlertLogAction();
                    tmpUpdateAlertLogAction.setID(getResultSetInteger(rs, "ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertID(getResultSetInteger(rs, "UPDATE_ALERT_ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertTypeID(getResultSetInteger(rs, "UPDATE_ALERT_TYPE_ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertSystemLogID(getResultSetInteger(rs, "UPDATE_ALERT_SYSTEM_LOG_ID"));
                    tmpUpdateAlertLogAction.setActionType(getResultSetString(rs, "ACTION_TYPE"));
                    tmpUpdateAlertLogAction.setActionTypeLevel(getResultSetString(rs, "ACTION_TYPE_LEVEL"));
                    tmpUpdateAlertLogAction.setDescription(getResultSetString(rs, "DESCRIPTION"));
                    tmpUpdateAlertLogAction.setActorID(getResultSetInteger(rs, "ACTOR_ID"));
                    tmpUpdateAlertLogAction.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUpdateAlertLogAction.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUpdateAlertLogAction.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUpdateAlertLogAction.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUpdateAlertLogAction.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUpdateAlertLogAction.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUpdateAlertLogAction.setCreatorName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getCreatorID()));
                    tmpUpdateAlertLogAction.setUpdaterName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getUpdaterID()));
                    result.add(tmpUpdateAlertLogAction);
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

    public synchronized List getListByUpdateAlertTypeID(Integer alertTypeID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.UPDATE_ALERT_ID, A.UPDATE_ALERT_TYPE_ID, A.UPDATE_ALERT_SYSTEM_LOG_ID, A.ACTION_TYPE, A.ACTION_TYPE_LEVEL, A.DESCRIPTION, A.ACTOR_ID, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   UPDATE_ALERT_LOG_ACTION A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND A.UPDATE_ALERT_TYPE_ID= ?");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 2, alertTypeID);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    UpdateAlertLogAction tmpUpdateAlertLogAction = new UpdateAlertLogAction();
                    tmpUpdateAlertLogAction.setID(getResultSetInteger(rs, "ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertID(getResultSetInteger(rs, "UPDATE_ALERT_ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertTypeID(getResultSetInteger(rs, "UPDATE_ALERT_TYPE_ID"));
                    tmpUpdateAlertLogAction.setUpdateAlertSystemLogID(getResultSetInteger(rs, "UPDATE_ALERT_SYSTEM_LOG_ID"));
                    tmpUpdateAlertLogAction.setActionType(getResultSetString(rs, "ACTION_TYPE"));
                    tmpUpdateAlertLogAction.setActionTypeLevel(getResultSetString(rs, "ACTION_TYPE_LEVEL"));
                    tmpUpdateAlertLogAction.setDescription(getResultSetString(rs, "DESCRIPTION"));
                    tmpUpdateAlertLogAction.setActorID(getResultSetInteger(rs, "ACTOR_ID"));
                    tmpUpdateAlertLogAction.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUpdateAlertLogAction.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUpdateAlertLogAction.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUpdateAlertLogAction.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUpdateAlertLogAction.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUpdateAlertLogAction.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUpdateAlertLogAction.setCreatorName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getCreatorID()));
                    tmpUpdateAlertLogAction.setUpdaterName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getUpdaterID()));
                    result.add(tmpUpdateAlertLogAction);
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
        UpdateAlertLogAction tmpUpdateAlertLogAction = (UpdateAlertLogAction) ((UpdateAlertLogAction) obj).clone();
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("INSERT ");
                sqlStat.append("INTO   UPDATE_ALERT_LOG_ACTION(ID, UPDATE_ALERT_ID, UPDATE_ALERT_TYPE_ID, UPDATE_ALERT_SYSTEM_LOG_ID, ACTION_TYPE, ACTION_TYPE_LEVEL, DESCRIPTION, ACTOR_ID, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE) ");
                sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, nextID);
                setPrepareStatement(preStat, 2, tmpUpdateAlertLogAction.getUpdateAlertID());
                setPrepareStatement(preStat, 3, tmpUpdateAlertLogAction.getUpdateAlertTypeID());
                setPrepareStatement(preStat, 4, tmpUpdateAlertLogAction.getUpdateAlertSystemLogID());
                setPrepareStatement(preStat, 5, tmpUpdateAlertLogAction.getActionType());
                setPrepareStatement(preStat, 6, tmpUpdateAlertLogAction.getActionTypeLevel());
                setPrepareStatement(preStat, 7, tmpUpdateAlertLogAction.getDescription());
                setPrepareStatement(preStat, 8, tmpUpdateAlertLogAction.getActorID());
                setPrepareStatement(preStat, 9, GlobalConstant.RECORD_STATUS_ACTIVE);
                setPrepareStatement(preStat, 10, new Integer(0));
                setPrepareStatement(preStat, 11, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 12, currTime);
                setPrepareStatement(preStat, 13, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 14, currTime);
                preStat.executeUpdate();
                tmpUpdateAlertLogAction.setID(nextID);
                tmpUpdateAlertLogAction.setCreatorID(sessionContainer.getUserRecordID());
                tmpUpdateAlertLogAction.setCreateDate(currTime);
                tmpUpdateAlertLogAction.setUpdaterID(sessionContainer.getUserRecordID());
                tmpUpdateAlertLogAction.setUpdateDate(currTime);
                tmpUpdateAlertLogAction.setUpdateCount(new Integer(0));
                tmpUpdateAlertLogAction.setCreatorName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getCreatorID()));
                tmpUpdateAlertLogAction.setUpdaterName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getUpdaterID()));
                return (tmpUpdateAlertLogAction);
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
        UpdateAlertLogAction tmpUpdateAlertLogAction = (UpdateAlertLogAction) ((UpdateAlertLogAction) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("UPDATE UPDATE_ALERT_LOG_ACTION ");
                sqlStat.append("SET  UPDATE_ALERT_ID=?, UPDATE_ALERT_TYPE_ID=?, UPDATE_ALERT_SYSTEM_LOG_ID=?, ACTION_TYPE=?, ACTION_TYPE_LEVEL=?, DESCRIPTION=?, ACTOR_ID=?, UPDATE_COUNT=?, UPDATER_ID=?, UPDATE_DATE=? ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpUpdateAlertLogAction.getUpdateAlertID());
                setPrepareStatement(preStat, 2, tmpUpdateAlertLogAction.getUpdateAlertTypeID());
                setPrepareStatement(preStat, 3, tmpUpdateAlertLogAction.getUpdateAlertSystemLogID());
                setPrepareStatement(preStat, 4, tmpUpdateAlertLogAction.getActionType());
                setPrepareStatement(preStat, 5, tmpUpdateAlertLogAction.getActionTypeLevel());
                setPrepareStatement(preStat, 6, tmpUpdateAlertLogAction.getDescription());
                setPrepareStatement(preStat, 7, tmpUpdateAlertLogAction.getActorID());
                setPrepareStatement(preStat, 8, new Integer(tmpUpdateAlertLogAction.getUpdateCount().intValue() + 1));
                setPrepareStatement(preStat, 9, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 10, currTime);
                setPrepareStatement(preStat, 11, tmpUpdateAlertLogAction.getID());
                setPrepareStatement(preStat, 12, tmpUpdateAlertLogAction.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    tmpUpdateAlertLogAction.setUpdaterID(sessionContainer.getUserRecordID());
                    tmpUpdateAlertLogAction.setUpdateDate(currTime);
                    tmpUpdateAlertLogAction.setUpdateCount(new Integer(tmpUpdateAlertLogAction.getUpdateCount().intValue() + 1));
                    tmpUpdateAlertLogAction.setCreatorName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getCreatorID()));
                    tmpUpdateAlertLogAction.setUpdaterName(UserInfoFactory.getUserFullName(tmpUpdateAlertLogAction.getUpdaterID()));
                    return (tmpUpdateAlertLogAction);
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
        UpdateAlertLogAction tmpUpdateAlertLogAction = (UpdateAlertLogAction) ((UpdateAlertLogAction) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   UPDATE_ALERT_LOG_ACTION ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpUpdateAlertLogAction.getID());
                setPrepareStatement(preStat, 2, tmpUpdateAlertLogAction.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    return (tmpUpdateAlertLogAction);
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
        UpdateAlertLogAction tmpUpdateAlertLogAction = (UpdateAlertLogAction) this.oldValue;
        if (tmpUpdateAlertLogAction != null) {
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getID()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateAlertID()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateAlertTypeID()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateAlertSystemLogID()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getActionType()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getActionTypeLevel()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getDescription()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getActorID()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getRecordStatus()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateCount()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getCreatorID()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getCreateDate()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdaterID()));
            oldValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateDate()));
        }
        tmpUpdateAlertLogAction = (UpdateAlertLogAction) obj;
        if (tmpUpdateAlertLogAction != null) {
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getID()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateAlertID()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateAlertTypeID()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateAlertSystemLogID()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getActionType()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getActionTypeLevel()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getDescription()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getActorID()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getRecordStatus()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateCount()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getCreatorID()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getCreateDate()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdaterID()));
            newValues.add(toAuditTrailValue(tmpUpdateAlertLogAction.getUpdateDate()));
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
    public synchronized boolean getBySystemLogByActionType(Integer systemLogID, String actionType) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.UPDATE_ALERT_ID, A.UPDATE_ALERT_TYPE_ID, A.UPDATE_ALERT_SYSTEM_LOG_ID, A.ACTION_TYPE, A.ACTION_TYPE_LEVEL, A.DESCRIPTION, A.ACTOR_ID, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   UPDATE_ALERT_LOG_ACTION A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND A.UPDATE_ALERT_SYSTEM_LOG_ID = ? AND A.ACTION_TYPE = ? AND A.ACTOR_ID = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 2, systemLogID);
                this.setPrepareStatement(preStat, 3, actionType);
                this.setPrepareStatement(preStat, 4, sessionContainer.getUserRecordID());
                rs = preStat.executeQuery();
                if (rs.next()) {
                    return (true);
                } else {
                    return (false);
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

    public synchronized void deleteObjectBySystemLogID(Integer systemLogID) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   UPDATE_ALERT_LOG_ACTION ");
                sqlStat.append("WHERE  UPDATE_ALERT_SYSTEM_LOG_ID =? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, systemLogID);
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
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

    public synchronized void deleteObjectBySystemLogIDActorID(Integer systemLogID) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   UPDATE_ALERT_LOG_ACTION ");
                sqlStat.append("WHERE  UPDATE_ALERT_SYSTEM_LOG_ID =? ");
                sqlStat.append("AND ACTOR_ID = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, systemLogID);
                setPrepareStatement(preStat, 2, sessionContainer.getUserRecordID());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
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
