package com.dcivision.staff.dao;

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
import com.dcivision.framework.SystemParameterConstant;
import com.dcivision.framework.SystemParameterFactory;
import com.dcivision.framework.TextUtility;
import com.dcivision.framework.UserInfoFactory;
import com.dcivision.framework.Utility;
import com.dcivision.framework.bean.AbstractBaseObject;
import com.dcivision.framework.dao.AbstractDAObject;
import com.dcivision.framework.web.AbstractSearchForm;
import com.dcivision.staff.bean.StaffHierarchy;
import com.dcivision.staff.web.ListStaffHierarchyForm;

/**
  StaffHierarchyDAObject.java

  This class is the data access bean for table "STAFF_HIERARCHY".

  @author      Wong Yam Lee
  @company     DCIVision Limited
  @creation date   28/07/2003
  @version     $Revision: 1.15 $
*/
public class StaffHierarchyDAObject extends AbstractDAObject {

    public static final String REVISION = "$Revision: 1.15 $";

    public static final String TABLE_NAME = "STAFF_HIERARCHY";

    private final int MAX_LVL = Integer.parseInt(SystemParameterFactory.getSystemParameter(SystemParameterConstant.STAFF_HIERARCHY_LEVEL));

    public StaffHierarchyDAObject(SessionContainer sessionContainer, Connection dbConn) {
        super(sessionContainer, dbConn);
    }

    protected void initDBSetting() {
        this.baseTableName = TABLE_NAME;
        this.vecDBColumn.add("ID");
        this.vecDBColumn.add("NAME");
        this.vecDBColumn.add("LEVEL_NO");
        this.vecDBColumn.add("PARENT_ID");
        this.vecDBColumn.add("ADDRESS");
        this.vecDBColumn.add("COUNTRY_ID");
        this.vecDBColumn.add("PHONE_NO");
        this.vecDBColumn.add("FAX_NO");
        this.vecDBColumn.add("URL");
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
                sqlStat.append("SELECT A.ID, A.NAME, A.LEVEL_NO, A.PARENT_ID, A.ADDRESS, A.COUNTRY_ID, A.PHONE_NO, A.FAX_NO, A.URL, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   STAFF_HIERARCHY A ");
                sqlStat.append("WHERE  A.ID = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, id);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    StaffHierarchy tmpStaffHierarchy = new StaffHierarchy();
                    tmpStaffHierarchy.setID(getResultSetInteger(rs, "ID"));
                    tmpStaffHierarchy.setName(getResultSetString(rs, "NAME"));
                    tmpStaffHierarchy.setLevelNo(getResultSetInteger(rs, "LEVEL_NO"));
                    tmpStaffHierarchy.setParentID(getResultSetInteger(rs, "PARENT_ID"));
                    tmpStaffHierarchy.setAddress(getResultSetString(rs, "ADDRESS"));
                    tmpStaffHierarchy.setCountryID(getResultSetInteger(rs, "COUNTRY_ID"));
                    tmpStaffHierarchy.setPhoneNo(getResultSetString(rs, "PHONE_NO"));
                    tmpStaffHierarchy.setFaxNo(getResultSetString(rs, "FAX_NO"));
                    tmpStaffHierarchy.setUrl(getResultSetString(rs, "URL"));
                    tmpStaffHierarchy.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpStaffHierarchy.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpStaffHierarchy.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpStaffHierarchy.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpStaffHierarchy.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpStaffHierarchy.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpStaffHierarchy.setCreatorName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getCreatorID()));
                    tmpStaffHierarchy.setUpdaterName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getUpdaterID()));
                    return (tmpStaffHierarchy);
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
        ListStaffHierarchyForm listStaffHierarchyForm = (ListStaffHierarchyForm) searchForm;
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.NAME, A.LEVEL_NO, A.PARENT_ID, A.ADDRESS, A.COUNTRY_ID, A.PHONE_NO, A.FAX_NO, A.URL, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   STAFF_HIERARCHY A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND A.PARENT_ID = ? ");
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
                this.setPrepareStatement(preStatCnt, 2, new Integer(listStaffHierarchyForm.getParentID()));
                if (searchForm.isSearchable()) {
                    String searchKeyword = this.getFormattedKeyword(searchForm.getBasicSearchKeyword(), searchForm.getBasicSearchType());
                    this.setPrepareStatement(preStatCnt, 3, searchKeyword);
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
                this.setPrepareStatement(preStat, 2, new Integer(listStaffHierarchyForm.getParentID()));
                if (searchForm.isSearchable()) {
                    String searchKeyword = this.getFormattedKeyword(searchForm.getBasicSearchKeyword(), searchForm.getBasicSearchType());
                    this.setPrepareStatement(preStat, 3, searchKeyword);
                }
                rs = preStat.executeQuery();
                this.positionCursor(rs, startOffset, pageSize);
                while (rs.next() && rowLoopCnt < pageSize) {
                    StaffHierarchy tmpStaffHierarchy = new StaffHierarchy();
                    tmpStaffHierarchy.setID(getResultSetInteger(rs, "ID"));
                    tmpStaffHierarchy.setName(getResultSetString(rs, "NAME"));
                    tmpStaffHierarchy.setLevelNo(getResultSetInteger(rs, "LEVEL_NO"));
                    tmpStaffHierarchy.setParentID(getResultSetInteger(rs, "PARENT_ID"));
                    tmpStaffHierarchy.setAddress(getResultSetString(rs, "ADDRESS"));
                    tmpStaffHierarchy.setCountryID(getResultSetInteger(rs, "COUNTRY_ID"));
                    tmpStaffHierarchy.setPhoneNo(getResultSetString(rs, "PHONE_NO"));
                    tmpStaffHierarchy.setFaxNo(getResultSetString(rs, "FAX_NO"));
                    tmpStaffHierarchy.setUrl(getResultSetString(rs, "URL"));
                    tmpStaffHierarchy.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpStaffHierarchy.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpStaffHierarchy.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpStaffHierarchy.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpStaffHierarchy.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpStaffHierarchy.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpStaffHierarchy.setCreatorName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getCreatorID()));
                    tmpStaffHierarchy.setUpdaterName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getUpdaterID()));
                    tmpStaffHierarchy.setRecordCount(totalNumOfRecord);
                    tmpStaffHierarchy.setRowNum(startOffset++);
                    ++rowLoopCnt;
                    result.add(tmpStaffHierarchy);
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
                sqlStat.append("SELECT A.ID, A.NAME, A.LEVEL_NO, A.PARENT_ID, A.ADDRESS, A.COUNTRY_ID, A.PHONE_NO, A.FAX_NO, A.URL, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   STAFF_HIERARCHY A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    StaffHierarchy tmpStaffHierarchy = new StaffHierarchy();
                    tmpStaffHierarchy.setID(getResultSetInteger(rs, "ID"));
                    tmpStaffHierarchy.setName(getResultSetString(rs, "NAME"));
                    tmpStaffHierarchy.setLevelNo(getResultSetInteger(rs, "LEVEL_NO"));
                    tmpStaffHierarchy.setParentID(getResultSetInteger(rs, "PARENT_ID"));
                    tmpStaffHierarchy.setAddress(getResultSetString(rs, "ADDRESS"));
                    tmpStaffHierarchy.setCountryID(getResultSetInteger(rs, "COUNTRY_ID"));
                    tmpStaffHierarchy.setPhoneNo(getResultSetString(rs, "PHONE_NO"));
                    tmpStaffHierarchy.setFaxNo(getResultSetString(rs, "FAX_NO"));
                    tmpStaffHierarchy.setUrl(getResultSetString(rs, "URL"));
                    tmpStaffHierarchy.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpStaffHierarchy.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpStaffHierarchy.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpStaffHierarchy.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpStaffHierarchy.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpStaffHierarchy.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpStaffHierarchy.setCreatorName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getCreatorID()));
                    tmpStaffHierarchy.setUpdaterName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getUpdaterID()));
                    result.add(tmpStaffHierarchy);
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
        StaffHierarchy staffHierarchy = (StaffHierarchy) obj;
        if (Utility.isEmpty(staffHierarchy.getName().trim())) {
            throw new ApplicationException("error.staff.region", null);
        }
        TextUtility.stringValidation(staffHierarchy.getName());
        StringBuffer sbSqlStat = new StringBuffer();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        sbSqlStat.append("SELECT COUNT(*) ");
        sbSqlStat.append("FROM   STAFF_HIERARCHY ");
        sbSqlStat.append("WHERE  NAME = ? AND PARENT_ID = ? AND RECORD_STATUS = ? ");
        try {
            pstat = this.dbConn.prepareStatement(sbSqlStat.toString());
            this.setPrepareStatement(pstat, 1, staffHierarchy.getName());
            this.setPrepareStatement(pstat, 2, staffHierarchy.getParentID());
            this.setPrepareStatement(pstat, 3, GlobalConstant.RECORD_STATUS_ACTIVE);
            rs = pstat.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                int level = staffHierarchy.getLevelNo().intValue();
                if (level <= MAX_LVL) {
                    throw new ApplicationException(com.dcivision.staff.StaffErrorConstant.DUPLICATE_STAFF_NAME[level]);
                } else {
                    throw new ApplicationException(com.dcivision.staff.StaffErrorConstant.DUPLICATE_STAFF_NAME[0]);
                }
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
        StaffHierarchy staffHierarchy = (StaffHierarchy) obj;
        if (Utility.isEmpty(staffHierarchy.getName().trim())) {
            throw new ApplicationException("error.staff.region", null);
        }
        TextUtility.stringValidation(staffHierarchy.getName());
        StringBuffer sbSqlStat = new StringBuffer();
        PreparedStatement pstat = null;
        ResultSet rs = null;
        sbSqlStat.append("SELECT COUNT(*) ");
        sbSqlStat.append("FROM   STAFF_HIERARCHY ");
        sbSqlStat.append("WHERE  NAME = ? AND PARENT_ID = ? AND ID<>? AND RECORD_STATUS = ? ");
        try {
            pstat = this.dbConn.prepareStatement(sbSqlStat.toString());
            this.setPrepareStatement(pstat, 1, staffHierarchy.getName());
            this.setPrepareStatement(pstat, 2, staffHierarchy.getParentID());
            this.setPrepareStatement(pstat, 3, staffHierarchy.getID());
            this.setPrepareStatement(pstat, 4, GlobalConstant.RECORD_STATUS_ACTIVE);
            rs = pstat.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                int level = staffHierarchy.getLevelNo().intValue();
                if (level <= MAX_LVL) {
                    throw new ApplicationException(com.dcivision.staff.StaffErrorConstant.DUPLICATE_STAFF_NAME[level]);
                } else {
                    throw new ApplicationException(com.dcivision.staff.StaffErrorConstant.DUPLICATE_STAFF_NAME[0]);
                }
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
        StaffHierarchy tmpStaffHierarchy = (StaffHierarchy) ((StaffHierarchy) obj).clone();
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("INSERT ");
                sqlStat.append("INTO   STAFF_HIERARCHY(ID, NAME, LEVEL_NO, PARENT_ID, ADDRESS, COUNTRY_ID, PHONE_NO, FAX_NO, URL, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE) ");
                sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, nextID);
                setPrepareStatement(preStat, 2, tmpStaffHierarchy.getName());
                setPrepareStatement(preStat, 3, tmpStaffHierarchy.getLevelNo());
                setPrepareStatement(preStat, 4, tmpStaffHierarchy.getParentID());
                setPrepareStatement(preStat, 5, tmpStaffHierarchy.getAddress());
                setPrepareStatement(preStat, 6, tmpStaffHierarchy.getCountryID());
                setPrepareStatement(preStat, 7, tmpStaffHierarchy.getPhoneNo());
                setPrepareStatement(preStat, 8, tmpStaffHierarchy.getFaxNo());
                setPrepareStatement(preStat, 9, tmpStaffHierarchy.getUrl());
                setPrepareStatement(preStat, 10, GlobalConstant.RECORD_STATUS_ACTIVE);
                setPrepareStatement(preStat, 11, new Integer(0));
                setPrepareStatement(preStat, 12, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 13, currTime);
                setPrepareStatement(preStat, 14, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 15, currTime);
                preStat.executeUpdate();
                tmpStaffHierarchy.setID(nextID);
                tmpStaffHierarchy.setCreatorID(sessionContainer.getUserRecordID());
                tmpStaffHierarchy.setCreateDate(currTime);
                tmpStaffHierarchy.setUpdaterID(sessionContainer.getUserRecordID());
                tmpStaffHierarchy.setUpdateDate(currTime);
                tmpStaffHierarchy.setUpdateCount(new Integer(0));
                tmpStaffHierarchy.setCreatorName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getCreatorID()));
                tmpStaffHierarchy.setUpdaterName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getUpdaterID()));
                return (tmpStaffHierarchy);
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
        StaffHierarchy tmpStaffHierarchy = (StaffHierarchy) ((StaffHierarchy) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("UPDATE STAFF_HIERARCHY ");
                sqlStat.append("SET  NAME=?, LEVEL_NO=?, PARENT_ID=?, ADDRESS=?, COUNTRY_ID=?, PHONE_NO=?, FAX_NO=?, URL=?, UPDATE_COUNT=?, UPDATER_ID=?, UPDATE_DATE=? ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpStaffHierarchy.getName());
                setPrepareStatement(preStat, 2, tmpStaffHierarchy.getLevelNo());
                setPrepareStatement(preStat, 3, tmpStaffHierarchy.getParentID());
                setPrepareStatement(preStat, 4, tmpStaffHierarchy.getAddress());
                setPrepareStatement(preStat, 5, tmpStaffHierarchy.getCountryID());
                setPrepareStatement(preStat, 6, tmpStaffHierarchy.getPhoneNo());
                setPrepareStatement(preStat, 7, tmpStaffHierarchy.getFaxNo());
                setPrepareStatement(preStat, 8, tmpStaffHierarchy.getUrl());
                setPrepareStatement(preStat, 9, new Integer(tmpStaffHierarchy.getUpdateCount().intValue() + 1));
                setPrepareStatement(preStat, 10, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 11, currTime);
                setPrepareStatement(preStat, 12, tmpStaffHierarchy.getID());
                setPrepareStatement(preStat, 13, tmpStaffHierarchy.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    tmpStaffHierarchy.setUpdaterID(sessionContainer.getUserRecordID());
                    tmpStaffHierarchy.setUpdateDate(currTime);
                    tmpStaffHierarchy.setUpdateCount(new Integer(tmpStaffHierarchy.getUpdateCount().intValue() + 1));
                    tmpStaffHierarchy.setCreatorName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getCreatorID()));
                    tmpStaffHierarchy.setUpdaterName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getUpdaterID()));
                    return (tmpStaffHierarchy);
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
        StaffHierarchy tmpStaffHierarchy = (StaffHierarchy) ((StaffHierarchy) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   STAFF_HIERARCHY ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpStaffHierarchy.getID());
                setPrepareStatement(preStat, 2, tmpStaffHierarchy.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    return (tmpStaffHierarchy);
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
        StaffHierarchy tmpStaffHierarchy = (StaffHierarchy) this.oldValue;
        if (tmpStaffHierarchy != null) {
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getID()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getName()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getLevelNo()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getParentID()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getAddress()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getCountryID()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getPhoneNo()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getFaxNo()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getUrl()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getRecordStatus()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getUpdateCount()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getCreatorID()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getCreateDate()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getUpdaterID()));
            oldValues.add(toAuditTrailValue(tmpStaffHierarchy.getUpdateDate()));
        }
        tmpStaffHierarchy = (StaffHierarchy) obj;
        if (tmpStaffHierarchy != null) {
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getID()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getName()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getLevelNo()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getParentID()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getAddress()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getCountryID()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getPhoneNo()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getFaxNo()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getUrl()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getRecordStatus()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getUpdateCount()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getCreatorID()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getCreateDate()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getUpdaterID()));
            newValues.add(toAuditTrailValue(tmpStaffHierarchy.getUpdateDate()));
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
    public synchronized List getFullList() throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT    A.ID, A.NAME, A.LEVEL_NO, A.PARENT_ID, A.ADDRESS, A.COUNTRY_ID, A.PHONE_NO, A.FAX_NO, A.URL, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM      STAFF_HIERARCHY A ");
                sqlStat.append("WHERE     A.RECORD_STATUS = ? ");
                sqlStat.append("ORDER BY  A.NAME ASC");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    StaffHierarchy tmpStaffHierarchy = new StaffHierarchy();
                    tmpStaffHierarchy.setID(getResultSetInteger(rs, "ID"));
                    tmpStaffHierarchy.setName(getResultSetString(rs, "NAME"));
                    tmpStaffHierarchy.setLevelNo(getResultSetInteger(rs, "LEVEL_NO"));
                    tmpStaffHierarchy.setParentID(getResultSetInteger(rs, "PARENT_ID"));
                    tmpStaffHierarchy.setAddress(getResultSetString(rs, "ADDRESS"));
                    tmpStaffHierarchy.setCountryID(getResultSetInteger(rs, "COUNTRY_ID"));
                    tmpStaffHierarchy.setPhoneNo(getResultSetString(rs, "PHONE_NO"));
                    tmpStaffHierarchy.setFaxNo(getResultSetString(rs, "FAX_NO"));
                    tmpStaffHierarchy.setUrl(getResultSetString(rs, "URL"));
                    tmpStaffHierarchy.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpStaffHierarchy.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpStaffHierarchy.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpStaffHierarchy.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpStaffHierarchy.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpStaffHierarchy.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpStaffHierarchy.setCreatorName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getCreatorID()));
                    tmpStaffHierarchy.setUpdaterName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getUpdaterID()));
                    result.add(tmpStaffHierarchy);
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

    public synchronized List getListByLevel(Integer level) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT    A.ID, A.NAME, A.LEVEL_NO, A.PARENT_ID, A.ADDRESS, A.COUNTRY_ID, A.PHONE_NO, A.FAX_NO, A.URL, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM      STAFF_HIERARCHY A ");
                sqlStat.append("WHERE     A.RECORD_STATUS = ? AND A.LEVEL_NO = ? ");
                sqlStat.append("ORDER BY  A.NAME ASC");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, 2, level);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    StaffHierarchy tmpStaffHierarchy = new StaffHierarchy();
                    tmpStaffHierarchy.setID(getResultSetInteger(rs, "ID"));
                    tmpStaffHierarchy.setName(getResultSetString(rs, "NAME"));
                    tmpStaffHierarchy.setLevelNo(getResultSetInteger(rs, "LEVEL_NO"));
                    tmpStaffHierarchy.setParentID(getResultSetInteger(rs, "PARENT_ID"));
                    tmpStaffHierarchy.setAddress(getResultSetString(rs, "ADDRESS"));
                    tmpStaffHierarchy.setCountryID(getResultSetInteger(rs, "COUNTRY_ID"));
                    tmpStaffHierarchy.setPhoneNo(getResultSetString(rs, "PHONE_NO"));
                    tmpStaffHierarchy.setFaxNo(getResultSetString(rs, "FAX_NO"));
                    tmpStaffHierarchy.setUrl(getResultSetString(rs, "URL"));
                    tmpStaffHierarchy.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpStaffHierarchy.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpStaffHierarchy.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpStaffHierarchy.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpStaffHierarchy.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpStaffHierarchy.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpStaffHierarchy.setCreatorName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getCreatorID()));
                    tmpStaffHierarchy.setUpdaterName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getUpdaterID()));
                    result.add(tmpStaffHierarchy);
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

    public synchronized AbstractBaseObject getByName(String name) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.NAME, A.LEVEL_NO, A.PARENT_ID, A.ADDRESS, A.COUNTRY_ID, A.PHONE_NO, A.FAX_NO, A.URL, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   STAFF_HIERARCHY A ");
                sqlStat.append("WHERE  A.NAME = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, name);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    StaffHierarchy tmpStaffHierarchy = new StaffHierarchy();
                    tmpStaffHierarchy.setID(getResultSetInteger(rs, "ID"));
                    tmpStaffHierarchy.setName(getResultSetString(rs, "NAME"));
                    tmpStaffHierarchy.setLevelNo(getResultSetInteger(rs, "LEVEL_NO"));
                    tmpStaffHierarchy.setParentID(getResultSetInteger(rs, "PARENT_ID"));
                    tmpStaffHierarchy.setAddress(getResultSetString(rs, "ADDRESS"));
                    tmpStaffHierarchy.setCountryID(getResultSetInteger(rs, "COUNTRY_ID"));
                    tmpStaffHierarchy.setPhoneNo(getResultSetString(rs, "PHONE_NO"));
                    tmpStaffHierarchy.setFaxNo(getResultSetString(rs, "FAX_NO"));
                    tmpStaffHierarchy.setUrl(getResultSetString(rs, "URL"));
                    tmpStaffHierarchy.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpStaffHierarchy.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpStaffHierarchy.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpStaffHierarchy.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpStaffHierarchy.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpStaffHierarchy.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpStaffHierarchy.setCreatorName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getCreatorID()));
                    tmpStaffHierarchy.setUpdaterName(UserInfoFactory.getUserFullName(tmpStaffHierarchy.getUpdaterID()));
                    return (tmpStaffHierarchy);
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

    public List getStaffHierarchyList() {
        List result = new ArrayList();
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        try {
            synchronized (dbConn) {
                sqlStat.append("SELECT    A.ID, A.NAME, A.LEVEL_NO, A.PARENT_ID, A.ADDRESS, A.COUNTRY_ID, A.PHONE_NO, A.FAX_NO, A.URL, A.RECORD_STATUS ");
                sqlStat.append("FROM      STAFF_HIERARCHY A ");
                sqlStat.append("WHERE     A.RECORD_STATUS = 'A' ");
                sqlStat.append("ORDER BY  A.NAME ASC");
                preStat = dbConn.prepareStatement(sqlStat.toString());
                rs = preStat.executeQuery();
                while (rs.next()) {
                    List tempRecord = new ArrayList();
                    tempRecord.add(rs.getString("ID"));
                    tempRecord.add(rs.getString("NAME"));
                    tempRecord.add(rs.getString("LEVEL_NO"));
                    tempRecord.add(rs.getString("PARENT_ID"));
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
}
