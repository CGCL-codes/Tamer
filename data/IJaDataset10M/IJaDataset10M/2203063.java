package com.dcivision.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.commons.dbutils.DbUtils;
import com.dcivision.framework.ApplicationException;
import com.dcivision.framework.ErrorConstant;
import com.dcivision.framework.GlobalConstant;
import com.dcivision.framework.SessionContainer;
import com.dcivision.framework.SystemParameterConstant;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.framework.Utility;
import com.dcivision.framework.bean.AbstractBaseObject;
import com.dcivision.framework.dao.AbstractDAObject;
import com.dcivision.framework.dao.AbstractQuery;
import com.dcivision.framework.dao.SubQuery;
import com.dcivision.framework.web.AbstractSearchForm;
import com.dcivision.user.bean.UserRole;

/**
  UserRoleDAObject.java

  This class is the data access bean for table "USER_ROLE".

  @author      Rollo Chan
  @company     DCIVision Limited
  @creation date   17/07/2003
  @version     $Revision: 1.30.2.1 $
*/
public class UserRoleDAObject extends AbstractDAObject {

    public static final String REVISION = "$Revision: 1.30.2.1 $";

    public static final String TABLE_NAME = "USER_ROLE";

    public static final String ID = "ID";

    public static final String ROLE_NAME = "ROLE_NAME";

    public static final String ROLE_DESC = "ROLE_DESC";

    public UserRoleDAObject(SessionContainer sessionContainer, Connection dbConn) {
        super(sessionContainer, dbConn);
    }

    protected void initDBSetting() {
        this.baseTableName = TABLE_NAME;
        this.vecDBColumn.add("ID");
        this.vecDBColumn.add("ROLE_NAME");
        this.vecDBColumn.add("ROLE_DESC");
        this.vecDBColumn.add("ROLE_TYPE");
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
                sqlStat.append("SELECT A.ID, A.ROLE_NAME, A.ROLE_DESC, A.ROLE_TYPE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   USER_ROLE A ");
                sqlStat.append("WHERE  A.ID = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, id);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    UserRole tmpUserRole = new UserRole();
                    tmpUserRole.setID(getResultSetInteger(rs, "ID"));
                    tmpUserRole.setRoleName(getResultSetString(rs, "ROLE_NAME"));
                    tmpUserRole.setRoleDesc(getResultSetString(rs, "ROLE_DESC"));
                    tmpUserRole.setRoleType(getResultSetString(rs, "ROLE_TYPE"));
                    tmpUserRole.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUserRole.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUserRole.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUserRole.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUserRole.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUserRole.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUserRole.setCreatorName(UserInfoFactory.getUserFullName(tmpUserRole.getCreatorID()));
                    tmpUserRole.setUpdaterName(UserInfoFactory.getUserFullName(tmpUserRole.getUpdaterID()));
                    return (tmpUserRole);
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
                sqlStat.append("SELECT A.ID, A.ROLE_NAME, A.ROLE_DESC, A.ROLE_TYPE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   USER_ROLE A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                sqlStat.append(this.getIDFilterWhereClause("A"));
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
                preStatCnt = dbConn.prepareStatement(sqlStatCnt.toString());
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
                    UserRole tmpUserRole = new UserRole();
                    tmpUserRole.setID(getResultSetInteger(rs, "ID"));
                    tmpUserRole.setRoleName(getResultSetString(rs, "ROLE_NAME"));
                    tmpUserRole.setRoleDesc(getResultSetString(rs, "ROLE_DESC"));
                    tmpUserRole.setRoleType(getResultSetString(rs, "ROLE_TYPE"));
                    tmpUserRole.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUserRole.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUserRole.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUserRole.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUserRole.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUserRole.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUserRole.setCreatorName(UserInfoFactory.getUserFullName(tmpUserRole.getCreatorID()));
                    tmpUserRole.setUpdaterName(UserInfoFactory.getUserFullName(tmpUserRole.getUpdaterID()));
                    tmpUserRole.setRecordCount(totalNumOfRecord);
                    tmpUserRole.setRowNum(startOffset++);
                    ++rowLoopCnt;
                    result.add(tmpUserRole);
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
                sqlStat.append("SELECT A.ID, A.ROLE_NAME, A.ROLE_DESC, A.ROLE_TYPE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   USER_ROLE A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                sqlStat.append(this.getIDFilterWhereClause("A"));
                sqlStat.append("ORDER BY A.ROLE_NAME ASC ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    UserRole tmpUserRole = new UserRole();
                    tmpUserRole.setID(getResultSetInteger(rs, "ID"));
                    tmpUserRole.setRoleName(getResultSetString(rs, "ROLE_NAME"));
                    tmpUserRole.setRoleDesc(getResultSetString(rs, "ROLE_DESC"));
                    tmpUserRole.setRoleType(getResultSetString(rs, "ROLE_TYPE"));
                    tmpUserRole.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUserRole.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUserRole.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUserRole.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUserRole.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUserRole.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUserRole.setCreatorName(UserInfoFactory.getUserFullName(tmpUserRole.getCreatorID()));
                    tmpUserRole.setUpdaterName(UserInfoFactory.getUserFullName(tmpUserRole.getUpdaterID()));
                    result.add(tmpUserRole);
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
        UserRole userRole = (UserRole) obj;
        StringBuffer sbSqlStat = new StringBuffer();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        sbSqlStat.append("SELECT COUNT(*) ");
        sbSqlStat.append("FROM   USER_ROLE ");
        sbSqlStat.append("WHERE  ROLE_NAME = ? AND RECORD_STATUS = ? ");
        sbSqlStat.append(this.getIDFilterWhereClause(null));
        try {
            pstat = this.dbConn.prepareStatement(sbSqlStat.toString());
            this.setPrepareStatement(pstat, 1, userRole.getRoleName());
            this.setPrepareStatement(pstat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
            rs = pstat.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new ApplicationException(com.dcivision.user.UserErrorConstant.DUPLICATE_ROLE_NAME);
            }
        } catch (java.sql.SQLException sqle) {
            throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            } finally {
                rs = null;
            }
            try {
                pstat.close();
            } catch (Exception ignore) {
            } finally {
                pstat = null;
            }
        }
    }

    protected void validateUpdate(AbstractBaseObject obj) throws ApplicationException {
        UserRole userRole = (UserRole) obj;
        StringBuffer sbSqlStat = new StringBuffer();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        sbSqlStat.append("SELECT COUNT(*) ");
        sbSqlStat.append("FROM   USER_ROLE ");
        sbSqlStat.append("WHERE  ROLE_NAME = ? AND RECORD_STATUS = ? AND ID <> ? ");
        sbSqlStat.append(this.getIDFilterWhereClause(null));
        try {
            pstat = this.dbConn.prepareStatement(sbSqlStat.toString());
            this.setPrepareStatement(pstat, 1, userRole.getRoleName());
            this.setPrepareStatement(pstat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
            this.setPrepareStatement(pstat, 3, userRole.getID());
            rs = pstat.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new ApplicationException(com.dcivision.user.UserErrorConstant.DUPLICATE_ROLE_NAME);
            }
        } catch (java.sql.SQLException sqle) {
            throw new ApplicationException(ErrorConstant.DB_GENERAL_ERROR, sqle, sqle.toString());
        } finally {
            try {
                rs.close();
            } catch (Exception ignore) {
            } finally {
                rs = null;
            }
            try {
                pstat.close();
            } catch (Exception ignore) {
            } finally {
                pstat = null;
            }
        }
    }

    protected void validateDelete(AbstractBaseObject obj) throws ApplicationException {
    }

    protected synchronized AbstractBaseObject insert(AbstractBaseObject obj) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        UserRole tmpUserRole = (UserRole) ((UserRole) obj).clone();
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("INSERT ");
                sqlStat.append("INTO   USER_ROLE(ID, ROLE_NAME, ROLE_DESC, ROLE_TYPE, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE) ");
                sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, nextID);
                setPrepareStatement(preStat, 2, tmpUserRole.getRoleName());
                setPrepareStatement(preStat, 3, tmpUserRole.getRoleDesc());
                setPrepareStatement(preStat, 4, tmpUserRole.getRoleType());
                setPrepareStatement(preStat, 5, GlobalConstant.RECORD_STATUS_ACTIVE);
                setPrepareStatement(preStat, 6, new Integer(0));
                setPrepareStatement(preStat, 7, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 8, currTime);
                setPrepareStatement(preStat, 9, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 10, currTime);
                preStat.executeUpdate();
                tmpUserRole.setID(nextID);
                tmpUserRole.setCreatorID(sessionContainer.getUserRecordID());
                tmpUserRole.setCreateDate(currTime);
                tmpUserRole.setUpdaterID(sessionContainer.getUserRecordID());
                tmpUserRole.setUpdateDate(currTime);
                tmpUserRole.setUpdateCount(new Integer(0));
                tmpUserRole.setCreatorName(UserInfoFactory.getUserFullName(tmpUserRole.getCreatorID()));
                tmpUserRole.setUpdaterName(UserInfoFactory.getUserFullName(tmpUserRole.getUpdaterID()));
                return (tmpUserRole);
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
        UserRole tmpUserRole = (UserRole) ((UserRole) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("UPDATE USER_ROLE ");
                sqlStat.append("SET  ROLE_NAME=?, ROLE_DESC=?, ROLE_TYPE=?, UPDATE_COUNT=?, UPDATER_ID=?, UPDATE_DATE=? ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpUserRole.getRoleName());
                setPrepareStatement(preStat, 2, tmpUserRole.getRoleDesc());
                setPrepareStatement(preStat, 3, tmpUserRole.getRoleType());
                setPrepareStatement(preStat, 4, new Integer(tmpUserRole.getUpdateCount().intValue() + 1));
                setPrepareStatement(preStat, 5, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 6, currTime);
                setPrepareStatement(preStat, 7, tmpUserRole.getID());
                setPrepareStatement(preStat, 8, tmpUserRole.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    tmpUserRole.setUpdaterID(sessionContainer.getUserRecordID());
                    tmpUserRole.setUpdateDate(currTime);
                    tmpUserRole.setUpdateCount(new Integer(tmpUserRole.getUpdateCount().intValue() + 1));
                    tmpUserRole.setCreatorName(UserInfoFactory.getUserFullName(tmpUserRole.getCreatorID()));
                    tmpUserRole.setUpdaterName(UserInfoFactory.getUserFullName(tmpUserRole.getUpdaterID()));
                    return (tmpUserRole);
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
        UserRole tmpUserRole = (UserRole) ((UserRole) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   USER_ROLE ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpUserRole.getID());
                setPrepareStatement(preStat, 2, tmpUserRole.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    return (tmpUserRole);
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
        UserRole tmpUserRole = (UserRole) this.oldValue;
        if (tmpUserRole != null) {
            oldValues.add(toAuditTrailValue(tmpUserRole.getID()));
            oldValues.add(toAuditTrailValue(tmpUserRole.getRoleName()));
            oldValues.add(toAuditTrailValue(tmpUserRole.getRoleDesc()));
            oldValues.add(toAuditTrailValue(tmpUserRole.getRoleType()));
            oldValues.add(toAuditTrailValue(tmpUserRole.getRecordStatus()));
            oldValues.add(toAuditTrailValue(tmpUserRole.getUpdateCount()));
            oldValues.add(toAuditTrailValue(tmpUserRole.getCreatorID()));
            oldValues.add(toAuditTrailValue(tmpUserRole.getCreateDate()));
            oldValues.add(toAuditTrailValue(tmpUserRole.getUpdaterID()));
            oldValues.add(toAuditTrailValue(tmpUserRole.getUpdateDate()));
        }
        tmpUserRole = (UserRole) obj;
        if (tmpUserRole != null) {
            newValues.add(toAuditTrailValue(tmpUserRole.getID()));
            newValues.add(toAuditTrailValue(tmpUserRole.getRoleName()));
            newValues.add(toAuditTrailValue(tmpUserRole.getRoleDesc()));
            newValues.add(toAuditTrailValue(tmpUserRole.getRoleType()));
            newValues.add(toAuditTrailValue(tmpUserRole.getRecordStatus()));
            newValues.add(toAuditTrailValue(tmpUserRole.getUpdateCount()));
            newValues.add(toAuditTrailValue(tmpUserRole.getCreatorID()));
            newValues.add(toAuditTrailValue(tmpUserRole.getCreateDate()));
            newValues.add(toAuditTrailValue(tmpUserRole.getUpdaterID()));
            newValues.add(toAuditTrailValue(tmpUserRole.getUpdateDate()));
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
    public synchronized List getListByUserRecordID(Integer userRecordID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ROLE_NAME, A.ROLE_DESC, A.ROLE_TYPE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   USER_ROLE A, MTM_USER_RECORD_USER_ROLE B ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND B.USER_RECORD_ID = ? AND A.ID = B.USER_ROLE_ID ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 2, userRecordID);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    UserRole tmpUserRole = new UserRole();
                    tmpUserRole.setID(getResultSetInteger(rs, "ID"));
                    tmpUserRole.setRoleName(getResultSetString(rs, "ROLE_NAME"));
                    tmpUserRole.setRoleDesc(getResultSetString(rs, "ROLE_DESC"));
                    tmpUserRole.setRoleType(getResultSetString(rs, "ROLE_TYPE"));
                    tmpUserRole.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUserRole.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUserRole.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUserRole.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUserRole.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUserRole.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUserRole.setCreatorName(UserInfoFactory.getUserFullName(tmpUserRole.getCreatorID()));
                    tmpUserRole.setUpdaterName(UserInfoFactory.getUserFullName(tmpUserRole.getUpdaterID()));
                    result.add(tmpUserRole);
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

    /**
   * getListSQLByUserRecordID
   * get the subQuery of the user Role
   * @param userRecordID
   * @return a subQuery of user role
   */
    public synchronized AbstractQuery getListSQLByUserRecordID(Integer userRecordID) {
        AbstractQuery subQuery = new SubQuery();
        StringBuffer sqlStat = new StringBuffer();
        sqlStat.append("SELECT A.ID ");
        sqlStat.append("FROM   USER_ROLE A, MTM_USER_RECORD_USER_ROLE B ");
        sqlStat.append("WHERE  A.RECORD_STATUS = ? AND B.USER_RECORD_ID = ? AND A.ID = B.USER_ROLE_ID ");
        subQuery.append(sqlStat.toString());
        subQuery.setParameter(1, GlobalConstant.RECORD_STATUS_ACTIVE);
        subQuery.setParameter(2, userRecordID);
        return subQuery;
    }

    public synchronized AbstractQuery getListSQLByUserRecordIDs(List userRecordIDs) {
        AbstractQuery subQuery = new SubQuery();
        StringBuffer sqlStat = new StringBuffer();
        sqlStat.append("SELECT A.ID ");
        sqlStat.append("FROM   USER_ROLE A, MTM_USER_RECORD_USER_ROLE B ");
        sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
        sqlStat.append(" AND ( ");
        for (int i = 0; i < userRecordIDs.size(); i++) {
            if (i == 0) {
                sqlStat.append(" B.USER_RECORD_ID = ? ");
            } else {
                sqlStat.append(" OR B.USER_RECORD_ID = ? ");
            }
        }
        sqlStat.append(" ) ");
        sqlStat.append(" AND A.ID = B.USER_ROLE_ID ");
        subQuery.append(sqlStat.toString());
        subQuery.setParameter(1, GlobalConstant.RECORD_STATUS_ACTIVE);
        for (int y = 0; y < userRecordIDs.size(); y++) {
            subQuery.setParameter(y + 2, userRecordIDs.get(y));
        }
        subQuery.pack();
        return subQuery;
    }

    public synchronized List getListByUserGroupID(Integer userGroupID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ROLE_NAME, A.ROLE_DESC, A.ROLE_TYPE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   USER_ROLE A, MTM_USER_GROUP_USER_ROLE B ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND B.USER_GROUP_ID = ? AND A.ID = B.USER_ROLE_ID ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 2, userGroupID);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    UserRole tmpUserRole = new UserRole();
                    tmpUserRole.setID(getResultSetInteger(rs, "ID"));
                    tmpUserRole.setRoleName(getResultSetString(rs, "ROLE_NAME"));
                    tmpUserRole.setRoleDesc(getResultSetString(rs, "ROLE_DESC"));
                    tmpUserRole.setRoleType(getResultSetString(rs, "ROLE_TYPE"));
                    tmpUserRole.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUserRole.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUserRole.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUserRole.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUserRole.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUserRole.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUserRole.setCreatorName(UserInfoFactory.getUserFullName(tmpUserRole.getCreatorID()));
                    tmpUserRole.setUpdaterName(UserInfoFactory.getUserFullName(tmpUserRole.getUpdaterID()));
                    result.add(tmpUserRole);
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

    /**
   * Return the array of string[] to show the permission of roles.<br>
   * [0] User Role ID<br>
   * [1] Role Name<br>
   * [2] Subject Type(Should be "R")<br>
   * [3] Permission String<br>
   * [4] Start Time<br>
   * [5] End Time<br>
   * [6] Permission must flag<br>
   * [7] Permimssion ID<br>
   *
   * @param objType
   * @param objID
   * @return
   * @throws ApplicationException
   */
    public synchronized String[][] getHasPermissionArrayByObjectTypeObjectID(String objType, Integer objID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ROLE_NAME, B.PERMISSION, B.MUST_FLAG, B.START_TIME, B.END_TIME, B.ID AS PERM_ID ");
                sqlStat.append("FROM   USER_ROLE A, USER_ROLE_PERMISSION B ");
                sqlStat.append("WHERE  A.ID > 0 AND A.ID = B.USER_ROLE_ID AND A.RECORD_STATUS = ? AND B.RECORD_STATUS = ? AND B.OBJECT_TYPE = ? AND B.OBJECT_ID = ? ");
                sqlStat.append("ORDER BY A.ROLE_NAME ASC ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 3, objType);
                this.setPrepareStatement(preStat, 4, objID);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    String startTime = TextUtility.formatDate(getResultSetTimestamp(rs, "START_TIME"), SystemParameterFactory.getSystemParameter(SystemParameterConstant.DB_DATETIME_FORMAT));
                    String endTime = TextUtility.formatDate(getResultSetTimestamp(rs, "END_TIME"), SystemParameterFactory.getSystemParameter(SystemParameterConstant.DB_DATETIME_FORMAT));
                    result.add(new String[] { getResultSetString(rs, "ID"), getResultSetString(rs, "ROLE_NAME"), GlobalConstant.SUBJECT_TYPE_ROLE, getResultSetString(rs, "PERMISSION"), startTime, endTime, (GlobalConstant.TRUE.equals(getResultSetString(rs, "MUST_FLAG")) ? GlobalConstant.TRUE : GlobalConstant.FALSE), getResultSetString(rs, "PERM_ID") });
                }
                String[][] tmpAry = new String[result.size()][];
                for (int i = 0; i < result.size(); i++) {
                    tmpAry[i] = (String[]) result.get(i);
                }
                return (tmpAry);
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
   * Return the array of string[] to show the roles who have no permission.<br>
   * [0] User Role ID<br>
   * [1] Role Name<br>
   * [2] Subject Type(Should be "U")<br>
   * [3] Empty Permission String<br>
   * [4] Empty Start Time<br>
   * [5] Empty End Time<br>
   * [6] "N" for initial must flag<br>
   * [7] "-1" for new permimssion ID<br>
   *
   * @param objType
   * @param objID
   * @return
   * @throws ApplicationException
   */
    public synchronized String[][] getHasNoPermissionArrayByObjectTypeObjectID(String objType, Integer objID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        String[][] hasAry = this.getHasPermissionArrayByObjectTypeObjectID(objType, objID);
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ROLE_NAME ");
                sqlStat.append("FROM   USER_ROLE A ");
                sqlStat.append("WHERE  A.ID > 0 AND A.RECORD_STATUS = ? ");
                if (hasAry.length > 0) {
                    sqlStat.append("AND (");
                    for (int i = 0; i < hasAry.length; i++) {
                        sqlStat.append(" A.ID <> ");
                        sqlStat.append(hasAry[i][0]);
                        sqlStat.append(" AND");
                    }
                    sqlStat.delete(sqlStat.length() - 3, sqlStat.length());
                    sqlStat.append(") ");
                }
                sqlStat.append("ORDER BY A.ROLE_NAME ASC ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    result.add(new String[] { getResultSetString(rs, "ID"), getResultSetString(rs, "ROLE_NAME"), GlobalConstant.SUBJECT_TYPE_ROLE, "", "", "", GlobalConstant.FALSE, "-1" });
                }
                String[][] tmpAry = new String[result.size()][];
                for (int i = 0; i < result.size(); i++) {
                    tmpAry[i] = (String[]) result.get(i);
                }
                return (tmpAry);
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
   * Return the array of string[] to show the permission of roles.<br>
   * [0] User Role ID<br>
   * [1] Role Name<br>
   * [2] Subject Type(Should be "R")<br>
   * [3] Permission String<br>
   * [4] Start Time<br>
   * [5] End Time<br>
   * [6] Permission must flag<br>
   * [7] Permimssion ID<br>
   * [8] Updater full name 
   *
   * @param objType
   * @param objID
   * @return
   * @throws ApplicationException
   */
    public synchronized String[][] getHasPermissionAndUpdaterArrayByObjectTypeObjectID(String objType, Integer objID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ROLE_NAME, B.PERMISSION, B.MUST_FLAG, B.START_TIME, B.END_TIME, B.ID AS PERM_ID ,C.FULL_NAME AS UPDATER_FULL_NAME ");
                sqlStat.append("FROM   USER_ROLE A, USER_ROLE_PERMISSION B ,USER_RECORD C ");
                sqlStat.append("WHERE  A.ID > 0 AND A.ID = B.USER_ROLE_ID  AND C.ID = B.UPDATER_ID AND A.RECORD_STATUS = ? AND B.RECORD_STATUS = ? AND B.OBJECT_TYPE = ? AND B.OBJECT_ID = ? ");
                sqlStat.append("ORDER BY A.ROLE_NAME ASC ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 3, objType);
                this.setPrepareStatement(preStat, 4, objID);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    String startTime = TextUtility.formatDate(getResultSetTimestamp(rs, "START_TIME"), SystemParameterFactory.getSystemParameter(SystemParameterConstant.DB_DATETIME_FORMAT));
                    String endTime = TextUtility.formatDate(getResultSetTimestamp(rs, "END_TIME"), SystemParameterFactory.getSystemParameter(SystemParameterConstant.DB_DATETIME_FORMAT));
                    result.add(new String[] { getResultSetString(rs, "ID"), getResultSetString(rs, "ROLE_NAME"), GlobalConstant.SUBJECT_TYPE_ROLE, getResultSetString(rs, "PERMISSION"), startTime, endTime, (GlobalConstant.TRUE.equals(getResultSetString(rs, "MUST_FLAG")) ? GlobalConstant.TRUE : GlobalConstant.FALSE), getResultSetString(rs, "PERM_ID"), getResultSetString(rs, "UPDATER_FULL_NAME") });
                }
                String[][] tmpAry = new String[result.size()][];
                for (int i = 0; i < result.size(); i++) {
                    tmpAry[i] = (String[]) result.get(i);
                }
                return (tmpAry);
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
   * Return the array of string[] to show the roles who have no permission.<br>
   * [0] User Role ID<br>
   * [1] Role Name<br>
   * [2] Subject Type(Should be "U")<br>
   * [3] Empty Permission String<br>
   * [4] Empty Start Time<br>
   * [5] Empty End Time<br>
   * [6] "N" for initial must flag<br>
   * [7] "-1" for new permimssion ID<br>
   * [8] Updater full name
   * @param objType
   * @param objID
   * @return
   * @throws ApplicationException
   */
    public synchronized String[][] getHasNoPermissionAndUpdaterArrayByObjectTypeObjectID(String objType, Integer objID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        String[][] hasAry = this.getHasPermissionAndUpdaterArrayByObjectTypeObjectID(objType, objID);
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ROLE_NAME ");
                sqlStat.append("FROM   USER_ROLE A ");
                sqlStat.append("WHERE  A.ID > 0 AND A.RECORD_STATUS = ? ");
                if (hasAry.length > 0) {
                    sqlStat.append("AND (");
                    for (int i = 0; i < hasAry.length; i++) {
                        sqlStat.append(" A.ID <> ");
                        sqlStat.append(hasAry[i][0]);
                        sqlStat.append(" AND");
                    }
                    sqlStat.delete(sqlStat.length() - 3, sqlStat.length());
                    sqlStat.append(") ");
                }
                sqlStat.append("ORDER BY A.ROLE_NAME ASC ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    result.add(new String[] { getResultSetString(rs, "ID"), getResultSetString(rs, "ROLE_NAME"), GlobalConstant.SUBJECT_TYPE_ROLE, "", "", "", GlobalConstant.FALSE, "-1" });
                }
                String[][] tmpAry = new String[result.size()][];
                for (int i = 0; i < result.size(); i++) {
                    tmpAry[i] = (String[]) result.get(i);
                }
                return (tmpAry);
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

    public AbstractBaseObject getObjectByRoleName(String roleName) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ROLE_NAME, A.ROLE_DESC, A.ROLE_TYPE, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   USER_ROLE A ");
                sqlStat.append("WHERE  A.ROLE_NAME = ? AND A.ROLE_TYPE = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, roleName);
                this.setPrepareStatement(preStat, 2, UserRole.ROLE_TYPE_USER_DEFINED);
                this.setPrepareStatement(preStat, 3, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    UserRole tmpUserRole = new UserRole();
                    tmpUserRole.setID(getResultSetInteger(rs, "ID"));
                    tmpUserRole.setRoleName(getResultSetString(rs, "ROLE_NAME"));
                    tmpUserRole.setRoleDesc(getResultSetString(rs, "ROLE_DESC"));
                    tmpUserRole.setRoleType(getResultSetString(rs, "ROLE_TYPE"));
                    tmpUserRole.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpUserRole.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpUserRole.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpUserRole.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpUserRole.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpUserRole.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpUserRole.setCreatorName(UserInfoFactory.getUserFullName(tmpUserRole.getCreatorID()));
                    tmpUserRole.setUpdaterName(UserInfoFactory.getUserFullName(tmpUserRole.getUpdaterID()));
                    return (tmpUserRole);
                } else {
                    return null;
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

    public List getRoleList() {
        List result = new ArrayList();
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        try {
            synchronized (dbConn) {
                sqlStat.append("SELECT A.ID, A.ROLE_NAME ");
                sqlStat.append("FROM   USER_ROLE A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = 'A' ");
                sqlStat.append("ORDER BY A.ROLE_NAME ASC ");
                preStat = dbConn.prepareStatement(sqlStat.toString());
                rs = preStat.executeQuery();
                while (rs.next()) {
                    List tempRecord = new ArrayList();
                    tempRecord.add(rs.getString("ID"));
                    tempRecord.add(rs.getString("ROLE_NAME"));
                    result.add(tempRecord);
                }
                return result;
            }
        } catch (SQLException sqle) {
            log.error(sqle, sqle);
        } catch (Exception e) {
            log.error(e, e);
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
        return null;
    }

    public List getRoleRecordsAndPageInfo(String searchRoleName, String startOffset, String pageSize, String orderBy, String req) {
        List result = new ArrayList();
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        PreparedStatement preStatCnt = null;
        ResultSet rsCnt = null;
        StringBuffer sqlStatCnt = null;
        String cnt = null;
        if (Utility.isEmpty(orderBy)) {
            orderBy = this.ROLE_NAME;
            req = "ASC";
        }
        try {
            synchronized (dbConn) {
                sqlStat.append("SELECT ID, ROLE_NAME, ROLE_DESC ");
                sqlStat.append("FROM USER_ROLE ");
                sqlStat.append("WHERE RECORD_STATUS = 'A' ");
                if (!Utility.isEmpty(searchRoleName)) {
                    sqlStat.append(" AND ROLE_NAME LIKE '%" + searchRoleName + "%' ");
                }
                sqlStat.append("ORDER BY " + orderBy + " " + req);
                sqlStatCnt = this.getSelectCountSQL(sqlStat);
                preStatCnt = dbConn.prepareStatement(sqlStatCnt.toString());
                rsCnt = preStatCnt.executeQuery();
                while (rsCnt.next()) {
                    cnt = rsCnt.getString(1);
                }
                sqlStat = this.getSelectListSQL(sqlStat, Integer.parseInt(startOffset), Integer.parseInt(pageSize));
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                rs = preStat.executeQuery();
                int rowLoopCnt = 0;
                while (rs.next() && rowLoopCnt < Integer.parseInt(pageSize)) {
                    List tempRecord = new ArrayList();
                    tempRecord.add(rs.getString(this.ID));
                    tempRecord.add(rs.getString(this.ROLE_NAME));
                    tempRecord.add(rs.getString(this.ROLE_DESC));
                    ++rowLoopCnt;
                    result.add(tempRecord);
                }
                StringBuffer pageInfomation = new StringBuffer();
                pageInfomation.append("startOffset:" + startOffset + ";");
                pageInfomation.append("pageSize:" + pageSize + ";");
                pageInfomation.append("orderBy:" + orderBy + ";");
                pageInfomation.append("req:" + req + ";");
                pageInfomation.append("total:" + cnt + "");
                result.add(pageInfomation.toString());
                return result;
            }
        } catch (SQLException sqle) {
            log.error(sqle, sqle);
        } catch (Exception e) {
            log.error(e, e);
        } finally {
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(preStat);
            DbUtils.closeQuietly(rsCnt);
            DbUtils.closeQuietly(preStatCnt);
        }
        return null;
    }
}
