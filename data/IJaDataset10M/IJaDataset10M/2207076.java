package com.dcivision.dms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import com.dcivision.dms.bean.DmsArchive;
import com.dcivision.dms.bean.DmsArchiveDetail;
import com.dcivision.dms.bean.DmsVersion;
import com.dcivision.dms.web.ListDmsArchiveDetailForm;
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

/**
  DmsArchiveDetailDAObject.java

  This class is the data access bean for table "DMS_ARCHIVE_DETAIL".

  @author      Zoe Shum
  @company     DCIVision Limited
  @creation date   23/10/2003
  @version     $Revision: 1.18.26.1 $
*/
public class DmsArchiveDetailDAObject extends AbstractDAObject {

    public static final String REVISION = "$Revision: 1.18.26.1 $";

    public static final String TABLE_NAME = "DMS_ARCHIVE_DETAIL";

    public DmsArchiveDetailDAObject(SessionContainer sessionContainer, Connection dbConn) {
        super(sessionContainer, dbConn);
    }

    protected void initDBSetting() {
        this.baseTableName = TABLE_NAME;
        this.vecDBColumn.add("ID");
        this.vecDBColumn.add("ARCHIVE_ID");
        this.vecDBColumn.add("DOCUMENT_ID");
        this.vecDBColumn.add("VERSION_ID");
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
                sqlStat.append("SELECT A.ID, A.ARCHIVE_ID, A.DOCUMENT_ID, A.VERSION_ID,A.ARCHIVE_SEGMENT_ID,A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL A ");
                sqlStat.append("WHERE  A.ID = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, id);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    DmsArchiveDetail tmpDmsArchiveDetail = new DmsArchiveDetail();
                    tmpDmsArchiveDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpDmsArchiveDetail.setArchiveID(getResultSetInteger(rs, "ARCHIVE_ID"));
                    tmpDmsArchiveDetail.setDocumentID(getResultSetInteger(rs, "DOCUMENT_ID"));
                    tmpDmsArchiveDetail.setVersionID(getResultSetInteger(rs, "VERSION_ID"));
                    tmpDmsArchiveDetail.setArchiveSegmentID(getResultSetInteger(rs, "ARCHIVE_SEGMENT_ID"));
                    tmpDmsArchiveDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpDmsArchiveDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpDmsArchiveDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpDmsArchiveDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpDmsArchiveDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpDmsArchiveDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpDmsArchiveDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getCreatorID()));
                    tmpDmsArchiveDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getUpdaterID()));
                    return (tmpDmsArchiveDetail);
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
        ListDmsArchiveDetailForm listForm = (ListDmsArchiveDetailForm) searchForm;
        boolean allowSpecificVersion = Boolean.valueOf(SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_ARCHIVE_ALLOW_RESTORE_SPECIFIC_VERSION)).booleanValue();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ARCHIVE_ID, A.DOCUMENT_ID, A.VERSION_ID,A.ARCHIVE_SEGMENT_ID, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE, B.DOCUMENT_NAME, D.ARCHIVE_NAME ");
                if (!allowSpecificVersion) {
                    sqlStat.append(", C.VERSION_LABEL ");
                }
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL A, DMS_DOCUMENT B, DMS_ARCHIVE D ");
                if (!allowSpecificVersion) {
                    sqlStat.append(", DMS_VERSION C ");
                }
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND A.DOCUMENT_ID = B.ID AND A.ARCHIVE_ID = D.ID AND D.ARCHIVE_TYPE = ? ");
                if (!allowSpecificVersion) {
                    sqlStat.append("AND A.DOCUMENT_ID = C.DOCUMENT_ID AND A.VERSION_ID = C.ID ");
                }
                if (!Utility.isEmpty(listForm.getArchiveID())) {
                    sqlStat.append("AND A.ARCHIVE_ID = ? ");
                }
                if (!Utility.isEmpty(listForm.getArchiveName())) {
                    sqlStat.append("AND D.ARCHIVE_NAME LIKE ? ");
                }
                if (!Utility.isEmpty(listForm.getDocumentName())) {
                    sqlStat.append("AND B.DOCUMENT_NAME LIKE ? ");
                }
                if (!Utility.isEmpty(listForm.getArchivedFrom())) {
                    sqlStat.append("AND A.CREATE_DATE >= ? ");
                }
                if (!Utility.isEmpty(listForm.getArchivedTo())) {
                    sqlStat.append("AND A.CREATE_DATE <= ? ");
                }
                if (searchForm.isSearchable()) {
                    String searchField = getSearchColumn(searchForm.getBasicSearchField());
                    sqlStat.append("AND  " + searchField + " " + searchForm.getBasicSearchType() + " ? ");
                }
                if (allowSpecificVersion) {
                    sqlStat.append("GROUP BY A.ARCHIVE_ID, A.DOCUMENT_ID ");
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
                if (allowSpecificVersion) {
                    StringBuffer sbResult = new StringBuffer("SELECT COUNT(DISTINCT A.DOCUMENT_ID) FROM ");
                    int idx = sqlStatCnt.indexOf("FROM ");
                    sbResult.append(sqlStatCnt.substring(idx + 5));
                    if (sbResult.indexOf("GROUP BY") >= 0) {
                        sbResult = new StringBuffer(sbResult.substring(0, sbResult.indexOf("GROUP BY") - 1));
                    }
                    sqlStatCnt = sbResult;
                }
                preStatCnt = dbConn.prepareStatement(sqlStatCnt.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                int i = 1;
                this.setPrepareStatement(preStatCnt, i++, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStatCnt, i++, DmsArchive.ARCHIVE_TYPE);
                if (!Utility.isEmpty(listForm.getArchiveID())) {
                    this.setPrepareStatement(preStatCnt, i++, listForm.getArchiveID());
                }
                if (!Utility.isEmpty(listForm.getArchiveName())) {
                    String searchKeyword = this.getExactMatchKeyword(listForm.getArchiveName(), true);
                    this.setPrepareStatement(preStatCnt, i++, searchKeyword);
                }
                if (!Utility.isEmpty(listForm.getDocumentName())) {
                    String searchKeyword = this.getExactMatchKeyword(listForm.getDocumentName(), true);
                    this.setPrepareStatement(preStatCnt, i++, searchKeyword);
                }
                if (!Utility.isEmpty(listForm.getArchivedFrom())) {
                    this.setPrepareStatement(preStatCnt, i++, listForm.getArchivedFrom());
                }
                if (!Utility.isEmpty(listForm.getArchivedTo())) {
                    this.setPrepareStatement(preStatCnt, i++, listForm.getArchivedTo());
                }
                if (searchForm.isSearchable()) {
                    String searchKeyword = this.getFormattedKeyword(searchForm.getBasicSearchKeyword(), searchForm.getBasicSearchType());
                    this.setPrepareStatement(preStatCnt, i++, searchKeyword);
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
                i = 1;
                this.setPrepareStatement(preStat, i++, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, i++, DmsArchive.ARCHIVE_TYPE);
                if (!Utility.isEmpty(listForm.getArchiveID())) {
                    this.setPrepareStatement(preStat, i++, listForm.getArchiveID());
                }
                if (!Utility.isEmpty(listForm.getArchiveName())) {
                    String searchKeyword = this.getExactMatchKeyword(listForm.getArchiveName(), true);
                    this.setPrepareStatement(preStat, i++, searchKeyword);
                }
                if (!Utility.isEmpty(listForm.getDocumentName())) {
                    String searchKeyword = this.getExactMatchKeyword(listForm.getDocumentName(), true);
                    this.setPrepareStatement(preStat, i++, searchKeyword);
                }
                if (!Utility.isEmpty(listForm.getArchivedFrom())) {
                    this.setPrepareStatement(preStat, i++, listForm.getArchivedFrom());
                }
                if (!Utility.isEmpty(listForm.getArchivedTo())) {
                    this.setPrepareStatement(preStat, i++, listForm.getArchivedTo());
                }
                if (searchForm.isSearchable()) {
                    String searchKeyword = this.getFormattedKeyword(searchForm.getBasicSearchKeyword(), searchForm.getBasicSearchType());
                    this.setPrepareStatement(preStat, i++, searchKeyword);
                }
                rs = preStat.executeQuery();
                this.positionCursor(rs, startOffset, pageSize);
                while (rs.next() && rowLoopCnt < pageSize) {
                    DmsArchiveDetail tmpDmsArchiveDetail = new DmsArchiveDetail();
                    tmpDmsArchiveDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpDmsArchiveDetail.setArchiveID(getResultSetInteger(rs, "ARCHIVE_ID"));
                    tmpDmsArchiveDetail.setDocumentID(getResultSetInteger(rs, "DOCUMENT_ID"));
                    tmpDmsArchiveDetail.setVersionID(getResultSetInteger(rs, "VERSION_ID"));
                    tmpDmsArchiveDetail.setArchiveSegmentID(getResultSetInteger(rs, "ARCHIVE_SEGMENT_ID"));
                    tmpDmsArchiveDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpDmsArchiveDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpDmsArchiveDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpDmsArchiveDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpDmsArchiveDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpDmsArchiveDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpDmsArchiveDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getCreatorID()));
                    tmpDmsArchiveDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getUpdaterID()));
                    tmpDmsArchiveDetail.setRecordCount(totalNumOfRecord);
                    tmpDmsArchiveDetail.setRowNum(startOffset++);
                    tmpDmsArchiveDetail.setArchiveName(getResultSetString(rs, "ARCHIVE_NAME"));
                    if (!allowSpecificVersion) {
                        tmpDmsArchiveDetail.setVersionLabel(getResultSetString(rs, "VERSION_LABEL"));
                    }
                    tmpDmsArchiveDetail.setDocumentName(getResultSetString(rs, "DOCUMENT_NAME"));
                    ++rowLoopCnt;
                    result.add(tmpDmsArchiveDetail);
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
                sqlStat.append("SELECT A.ID, A.ARCHIVE_ID, A.DOCUMENT_ID, A.VERSION_ID,A.ARCHIVE_SEGMENT_ID, A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE ");
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL A ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    DmsArchiveDetail tmpDmsArchiveDetail = new DmsArchiveDetail();
                    tmpDmsArchiveDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpDmsArchiveDetail.setArchiveID(getResultSetInteger(rs, "ARCHIVE_ID"));
                    tmpDmsArchiveDetail.setDocumentID(getResultSetInteger(rs, "DOCUMENT_ID"));
                    tmpDmsArchiveDetail.setVersionID(getResultSetInteger(rs, "VERSION_ID"));
                    tmpDmsArchiveDetail.setArchiveSegmentID(getResultSetInteger(rs, "ARCHIVE_SEGMENT_ID"));
                    tmpDmsArchiveDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpDmsArchiveDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpDmsArchiveDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpDmsArchiveDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpDmsArchiveDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpDmsArchiveDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpDmsArchiveDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getCreatorID()));
                    tmpDmsArchiveDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getUpdaterID()));
                    result.add(tmpDmsArchiveDetail);
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
        DmsArchiveDetail tmpDmsArchiveDetail = (DmsArchiveDetail) ((DmsArchiveDetail) obj).clone();
        Integer creatorID = sessionContainer.getUserRecordID();
        Integer updaterID = sessionContainer.getUserRecordID();
        if (!Utility.isEmpty(tmpDmsArchiveDetail.getCreatorID())) {
            creatorID = tmpDmsArchiveDetail.getCreatorID();
        }
        if (!Utility.isEmpty(tmpDmsArchiveDetail.getUpdaterID())) {
            updaterID = tmpDmsArchiveDetail.getUpdaterID();
        }
        synchronized (dbConn) {
            try {
                Integer nextID = getNextPrimaryID();
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("INSERT ");
                sqlStat.append("INTO   DMS_ARCHIVE_DETAIL(ID, ARCHIVE_ID, DOCUMENT_ID, VERSION_ID, ARCHIVE_SEGMENT_ID, RECORD_STATUS, UPDATE_COUNT, CREATOR_ID, CREATE_DATE, UPDATER_ID, UPDATE_DATE) ");
                sqlStat.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?) ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, nextID);
                setPrepareStatement(preStat, 2, tmpDmsArchiveDetail.getArchiveID());
                setPrepareStatement(preStat, 3, tmpDmsArchiveDetail.getDocumentID());
                setPrepareStatement(preStat, 4, tmpDmsArchiveDetail.getVersionID());
                setPrepareStatement(preStat, 5, tmpDmsArchiveDetail.getArchiveSegmentID());
                setPrepareStatement(preStat, 6, GlobalConstant.RECORD_STATUS_ACTIVE);
                setPrepareStatement(preStat, 7, new Integer(0));
                setPrepareStatement(preStat, 8, creatorID);
                setPrepareStatement(preStat, 9, currTime);
                setPrepareStatement(preStat, 10, updaterID);
                setPrepareStatement(preStat, 11, currTime);
                preStat.executeUpdate();
                tmpDmsArchiveDetail.setID(nextID);
                tmpDmsArchiveDetail.setCreatorID(creatorID);
                tmpDmsArchiveDetail.setCreateDate(currTime);
                tmpDmsArchiveDetail.setUpdaterID(updaterID);
                tmpDmsArchiveDetail.setUpdateDate(currTime);
                tmpDmsArchiveDetail.setUpdateCount(new Integer(0));
                tmpDmsArchiveDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getCreatorID()));
                tmpDmsArchiveDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getUpdaterID()));
                return (tmpDmsArchiveDetail);
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
        DmsArchiveDetail tmpDmsArchiveDetail = (DmsArchiveDetail) ((DmsArchiveDetail) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                Timestamp currTime = Utility.getCurrentTimestamp();
                sqlStat.append("UPDATE DMS_ARCHIVE_DETAIL ");
                sqlStat.append("SET  ARCHIVE_ID=?, DOCUMENT_ID=?, VERSION_ID=?, ARCHIVE_SEGMENT_ID=?, UPDATE_COUNT=?, UPDATER_ID=?, UPDATE_DATE=? ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpDmsArchiveDetail.getArchiveID());
                setPrepareStatement(preStat, 2, tmpDmsArchiveDetail.getDocumentID());
                setPrepareStatement(preStat, 3, tmpDmsArchiveDetail.getVersionID());
                setPrepareStatement(preStat, 4, tmpDmsArchiveDetail.getArchiveSegmentID());
                setPrepareStatement(preStat, 5, new Integer(tmpDmsArchiveDetail.getUpdateCount().intValue() + 1));
                setPrepareStatement(preStat, 6, sessionContainer.getUserRecordID());
                setPrepareStatement(preStat, 7, currTime);
                setPrepareStatement(preStat, 8, tmpDmsArchiveDetail.getID());
                setPrepareStatement(preStat, 9, tmpDmsArchiveDetail.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    tmpDmsArchiveDetail.setUpdaterID(sessionContainer.getUserRecordID());
                    tmpDmsArchiveDetail.setUpdateDate(currTime);
                    tmpDmsArchiveDetail.setUpdateCount(new Integer(tmpDmsArchiveDetail.getUpdateCount().intValue() + 1));
                    tmpDmsArchiveDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getCreatorID()));
                    tmpDmsArchiveDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getUpdaterID()));
                    return (tmpDmsArchiveDetail);
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
        DmsArchiveDetail tmpDmsArchiveDetail = (DmsArchiveDetail) ((DmsArchiveDetail) obj).clone();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL ");
                sqlStat.append("WHERE  ID=? AND UPDATE_COUNT=? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, tmpDmsArchiveDetail.getID());
                setPrepareStatement(preStat, 2, tmpDmsArchiveDetail.getUpdateCount());
                updateCnt = preStat.executeUpdate();
                if (updateCnt == 0) {
                    throw new ApplicationException(ErrorConstant.DB_CONCURRENT_ERROR);
                } else {
                    return (tmpDmsArchiveDetail);
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
        DmsArchiveDetail tmpDmsArchiveDetail = (DmsArchiveDetail) this.oldValue;
        if (tmpDmsArchiveDetail != null) {
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getID()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getArchiveID()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getDocumentID()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getVersionID()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getArchiveSegmentID()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getRecordStatus()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getUpdateCount()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getCreatorID()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getCreateDate()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getUpdaterID()));
            oldValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getUpdateDate()));
        }
        tmpDmsArchiveDetail = (DmsArchiveDetail) obj;
        if (tmpDmsArchiveDetail != null) {
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getID()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getArchiveID()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getDocumentID()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getVersionID()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getArchiveSegmentID()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getRecordStatus()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getUpdateCount()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getCreatorID()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getCreateDate()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getUpdaterID()));
            newValues.add(toAuditTrailValue(tmpDmsArchiveDetail.getUpdateDate()));
        }
        auditTrailBase(opMode, oldValues, newValues);
    }

    /**
   * Gets the list of the archive detail by the ListDmsArchiveDetailForm object with the 
   * specified ListDmsArchiveDetailForm.getArchiveID() and ListDmsArchiveDetailForm.getArchiveSegmentID().
   * 
   * @param searchForm ListDmsArchiveDetailForm object
   * @return List of the DmsArchiveDetail object
   * @throws ApplicationException
   * @see com.dcivision.dms.bean.DmsArchiveDetail
   */
    public synchronized List getListByArchiveIDSegmentID(AbstractSearchForm searchForm) throws ApplicationException {
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
        ListDmsArchiveDetailForm listForm = (ListDmsArchiveDetailForm) searchForm;
        boolean allowSpecificVersion = Boolean.valueOf(SystemParameterFactory.getSystemParameter(SystemParameterConstant.DMS_ARCHIVE_ALLOW_RESTORE_SPECIFIC_VERSION)).booleanValue();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ARCHIVE_ID, A.DOCUMENT_ID, A.VERSION_ID, A.ARCHIVE_SEGMENT_ID,A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE, B.DOCUMENT_NAME, D.ARCHIVE_NAME ");
                if (!allowSpecificVersion) {
                    sqlStat.append(", C.VERSION_LABEL ");
                }
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL A, DMS_DOCUMENT B, DMS_ARCHIVE D ");
                if (!allowSpecificVersion) {
                    sqlStat.append(", DMS_VERSION C ");
                }
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND A.DOCUMENT_ID = B.ID AND A.ARCHIVE_ID = D.ID AND D.ARCHIVE_TYPE = ? ");
                if (!allowSpecificVersion) {
                    sqlStat.append("AND A.DOCUMENT_ID = C.DOCUMENT_ID AND A.VERSION_ID = C.ID ");
                }
                sqlStat.append("AND A.ARCHIVE_ID = ? AND A.ARCHIVE_SEGMENT_ID=? ");
                if (!Utility.isEmpty(listForm.getArchiveName())) {
                    sqlStat.append("AND D.ARCHIVE_NAME LIKE ? ");
                }
                if (!Utility.isEmpty(listForm.getDocumentName())) {
                    sqlStat.append("AND B.DOCUMENT_NAME LIKE ? ");
                }
                if (!Utility.isEmpty(listForm.getArchivedFrom())) {
                    sqlStat.append("AND A.CREATE_DATE >= ? ");
                }
                if (!Utility.isEmpty(listForm.getArchivedTo())) {
                    sqlStat.append("AND A.CREATE_DATE <= ? ");
                }
                if (searchForm.isSearchable()) {
                    String searchField = getSearchColumn(searchForm.getBasicSearchField());
                    sqlStat.append("AND  " + searchField + " " + searchForm.getBasicSearchType() + " ? ");
                }
                if (allowSpecificVersion) {
                    sqlStat.append("GROUP BY A.ARCHIVE_ID, A.DOCUMENT_ID ");
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
                if (allowSpecificVersion) {
                    StringBuffer sbResult = new StringBuffer("SELECT COUNT(DISTINCT A.DOCUMENT_ID) FROM ");
                    int idx = sqlStatCnt.indexOf("FROM ");
                    sbResult.append(sqlStatCnt.substring(idx + 5));
                    if (sbResult.indexOf("GROUP BY") >= 0) {
                        sbResult = new StringBuffer(sbResult.substring(0, sbResult.indexOf("GROUP BY") - 1));
                    }
                    sqlStatCnt = sbResult;
                }
                preStatCnt = dbConn.prepareStatement(sqlStatCnt.toString());
                int i = 1;
                this.setPrepareStatement(preStatCnt, i++, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStatCnt, i++, DmsArchive.ARCHIVE_TYPE);
                this.setPrepareStatement(preStatCnt, i++, listForm.getArchiveID());
                this.setPrepareStatement(preStatCnt, i++, listForm.getArchiveSegmentID());
                if (!Utility.isEmpty(listForm.getArchiveName())) {
                    String searchKeyword = this.getExactMatchKeyword(listForm.getArchiveName(), true);
                    this.setPrepareStatement(preStatCnt, i++, searchKeyword);
                }
                if (!Utility.isEmpty(listForm.getDocumentName())) {
                    String searchKeyword = this.getExactMatchKeyword(listForm.getDocumentName(), true);
                    this.setPrepareStatement(preStatCnt, i++, searchKeyword);
                }
                if (!Utility.isEmpty(listForm.getArchivedFrom())) {
                    this.setPrepareStatement(preStatCnt, i++, listForm.getArchivedFrom());
                }
                if (!Utility.isEmpty(listForm.getArchivedTo())) {
                    this.setPrepareStatement(preStatCnt, i++, listForm.getArchivedTo());
                }
                if (searchForm.isSearchable()) {
                    String searchKeyword = this.getFormattedKeyword(searchForm.getBasicSearchKeyword(), searchForm.getBasicSearchType());
                    this.setPrepareStatement(preStatCnt, i++, searchKeyword);
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
                i = 1;
                this.setPrepareStatement(preStat, i++, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, i++, DmsArchive.ARCHIVE_TYPE);
                this.setPrepareStatement(preStat, i++, listForm.getArchiveID());
                this.setPrepareStatement(preStat, i++, listForm.getArchiveSegmentID());
                if (!Utility.isEmpty(listForm.getArchiveName())) {
                    String searchKeyword = this.getExactMatchKeyword(listForm.getArchiveName(), true);
                    this.setPrepareStatement(preStat, i++, searchKeyword);
                }
                if (!Utility.isEmpty(listForm.getDocumentName())) {
                    String searchKeyword = this.getExactMatchKeyword(listForm.getDocumentName(), true);
                    this.setPrepareStatement(preStat, i++, searchKeyword);
                }
                if (!Utility.isEmpty(listForm.getArchivedFrom())) {
                    this.setPrepareStatement(preStat, i++, listForm.getArchivedFrom());
                }
                if (!Utility.isEmpty(listForm.getArchivedTo())) {
                    this.setPrepareStatement(preStat, i++, listForm.getArchivedTo());
                }
                if (searchForm.isSearchable()) {
                    String searchKeyword = this.getFormattedKeyword(searchForm.getBasicSearchKeyword(), searchForm.getBasicSearchType());
                    this.setPrepareStatement(preStat, i++, searchKeyword);
                }
                rs = preStat.executeQuery();
                this.positionCursor(rs, startOffset, pageSize);
                while (rs.next() && rowLoopCnt < pageSize) {
                    DmsArchiveDetail tmpDmsArchiveDetail = new DmsArchiveDetail();
                    tmpDmsArchiveDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpDmsArchiveDetail.setArchiveID(getResultSetInteger(rs, "ARCHIVE_ID"));
                    tmpDmsArchiveDetail.setDocumentID(getResultSetInteger(rs, "DOCUMENT_ID"));
                    tmpDmsArchiveDetail.setVersionID(getResultSetInteger(rs, "VERSION_ID"));
                    tmpDmsArchiveDetail.setArchiveSegmentID(getResultSetInteger(rs, "ARCHIVE_SEGMENT_ID"));
                    tmpDmsArchiveDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpDmsArchiveDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpDmsArchiveDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpDmsArchiveDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpDmsArchiveDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpDmsArchiveDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpDmsArchiveDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getCreatorID()));
                    tmpDmsArchiveDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getUpdaterID()));
                    tmpDmsArchiveDetail.setRecordCount(totalNumOfRecord);
                    tmpDmsArchiveDetail.setRowNum(startOffset++);
                    tmpDmsArchiveDetail.setArchiveName(getResultSetString(rs, "ARCHIVE_NAME"));
                    if (!allowSpecificVersion) {
                        tmpDmsArchiveDetail.setVersionLabel(getResultSetString(rs, "VERSION_LABEL"));
                    }
                    tmpDmsArchiveDetail.setDocumentName(getResultSetString(rs, "DOCUMENT_NAME"));
                    ++rowLoopCnt;
                    result.add(tmpDmsArchiveDetail);
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

    /**
   * Gets list of the DmsArchiveDetail object by the archived ID.
   * 
   * @param archiveID the archive ID
   * @return List of the DmsArchiveDetail object
   * @throws ApplicationException
   * @see com.dcivision.dms.bean.DmsArchiveDetail
   */
    public synchronized List getListByArchiveID(Integer archiveID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List resultList = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT DISTINCT A.ARCHIVE_SEGMENT_ID ");
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL A ");
                sqlStat.append("WHERE  A.ARCHIVE_ID = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, archiveID);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    DmsArchiveDetail tmpDmsArchiveDetail = new DmsArchiveDetail();
                    tmpDmsArchiveDetail.setArchiveSegmentID(getResultSetInteger(rs, "ARCHIVE_SEGMENT_ID"));
                    resultList.add(tmpDmsArchiveDetail);
                }
                return resultList;
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
   * Gets the Segment ID by the document id.
   * 
   * @param docId the document ID
   * @return Segment ID
   * @throws ApplicationException
   */
    public synchronized Integer getSegmentIDByDocID(Integer docId) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        Integer tmpSegmentID = null;
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ARCHIVE_SEGMENT_ID ");
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL A ");
                sqlStat.append("WHERE  A.DOCUMENT_ID = ? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, docId);
                this.setPrepareStatement(preStat, 2, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    DmsArchiveDetail tmpDmsArchiveDetail = new DmsArchiveDetail();
                    tmpSegmentID = getResultSetInteger(rs, "ARCHIVE_SEGMENT_ID");
                }
                return (tmpSegmentID);
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
   * Gets the archive detail by archive id, document id and the version ID.
   * 
   * @param archiveID the archive ID
   * @param docId the document ID
   * @param versionID the version ID
   * @return the DmsArchiveDetail object matched
   * @throws ApplicationException
   * @see com.dcivision.dms.bean.DmsArchiveDetail
   */
    public synchronized AbstractBaseObject getSegmentByArchiveIDDocIDVersionID(Integer archiveID, Integer docId, Integer versionID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        Integer tmpSegmentID = null;
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ARCHIVE_ID, A.DOCUMENT_ID, A.VERSION_ID,A.ARCHIVE_SEGMENT_ID,A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE  ");
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL A ");
                sqlStat.append("WHERE  ARCHIVE_ID=? AND A.DOCUMENT_ID = ? AND VERSION_ID=? AND A.RECORD_STATUS = ? ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                this.setPrepareStatement(preStat, 1, archiveID);
                this.setPrepareStatement(preStat, 2, docId);
                this.setPrepareStatement(preStat, 3, versionID);
                this.setPrepareStatement(preStat, 4, GlobalConstant.RECORD_STATUS_ACTIVE);
                rs = preStat.executeQuery();
                if (rs.next()) {
                    DmsArchiveDetail tmpDmsArchiveDetail = new DmsArchiveDetail();
                    tmpDmsArchiveDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpDmsArchiveDetail.setArchiveID(getResultSetInteger(rs, "ARCHIVE_ID"));
                    tmpDmsArchiveDetail.setDocumentID(getResultSetInteger(rs, "DOCUMENT_ID"));
                    tmpDmsArchiveDetail.setVersionID(getResultSetInteger(rs, "VERSION_ID"));
                    tmpDmsArchiveDetail.setArchiveSegmentID(getResultSetInteger(rs, "ARCHIVE_SEGMENT_ID"));
                    tmpDmsArchiveDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpDmsArchiveDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpDmsArchiveDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpDmsArchiveDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpDmsArchiveDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpDmsArchiveDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpDmsArchiveDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getCreatorID()));
                    tmpDmsArchiveDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getUpdaterID()));
                    return (tmpDmsArchiveDetail);
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

    /**
   * Delete the archive detail of the table "DMS_ARCHIVE_DETAIL" by document ID
   * 
   * @param documentID the document ID
   * @throws ApplicationException
   */
    public synchronized void deleteByDocumentID(Integer documentID) throws ApplicationException {
        PreparedStatement preStat = null;
        StringBuffer sqlStat = new StringBuffer();
        synchronized (dbConn) {
            try {
                int updateCnt = 0;
                sqlStat.append("DELETE ");
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL ");
                sqlStat.append("WHERE  DOCUMENT_ID=?  ");
                preStat = dbConn.prepareStatement(sqlStat.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                setPrepareStatement(preStat, 1, documentID);
                updateCnt = preStat.executeUpdate();
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

    public List getListByArchiveIDSegmentID(Integer archiveID, Integer segmentID) throws ApplicationException {
        PreparedStatement preStat = null;
        ResultSet rs = null;
        StringBuffer sqlStat = new StringBuffer();
        List result = new ArrayList();
        synchronized (dbConn) {
            try {
                sqlStat.append("SELECT A.ID, A.ARCHIVE_ID, A.DOCUMENT_ID, A.VERSION_ID, A.ARCHIVE_SEGMENT_ID,A.RECORD_STATUS, A.UPDATE_COUNT, A.CREATOR_ID, A.CREATE_DATE, A.UPDATER_ID, A.UPDATE_DATE, B.DOCUMENT_NAME, D.ARCHIVE_NAME ");
                sqlStat.append("FROM   DMS_ARCHIVE_DETAIL A, DMS_DOCUMENT B, DMS_ARCHIVE D ");
                sqlStat.append("WHERE  A.RECORD_STATUS = ? AND A.DOCUMENT_ID = B.ID AND A.ARCHIVE_ID = D.ID AND D.ARCHIVE_TYPE = ? ");
                sqlStat.append("AND A.ARCHIVE_ID = ? AND A.ARCHIVE_SEGMENT_ID=? AND B.ITEM_STATUS=?");
                sqlStat = this.getFormattedSQL(sqlStat.toString());
                preStat = dbConn.prepareStatement(sqlStat.toString());
                int i = 1;
                this.setPrepareStatement(preStat, i++, GlobalConstant.RECORD_STATUS_ACTIVE);
                this.setPrepareStatement(preStat, i++, DmsArchive.ARCHIVE_TYPE);
                this.setPrepareStatement(preStat, i++, archiveID);
                this.setPrepareStatement(preStat, i++, segmentID);
                this.setPrepareStatement(preStat, i++, DmsVersion.ARCHIVED_STATUS);
                rs = preStat.executeQuery();
                while (rs.next()) {
                    DmsArchiveDetail tmpDmsArchiveDetail = new DmsArchiveDetail();
                    tmpDmsArchiveDetail.setID(getResultSetInteger(rs, "ID"));
                    tmpDmsArchiveDetail.setArchiveID(getResultSetInteger(rs, "ARCHIVE_ID"));
                    tmpDmsArchiveDetail.setDocumentID(getResultSetInteger(rs, "DOCUMENT_ID"));
                    tmpDmsArchiveDetail.setVersionID(getResultSetInteger(rs, "VERSION_ID"));
                    tmpDmsArchiveDetail.setArchiveSegmentID(getResultSetInteger(rs, "ARCHIVE_SEGMENT_ID"));
                    tmpDmsArchiveDetail.setRecordStatus(getResultSetString(rs, "RECORD_STATUS"));
                    tmpDmsArchiveDetail.setUpdateCount(getResultSetInteger(rs, "UPDATE_COUNT"));
                    tmpDmsArchiveDetail.setCreatorID(getResultSetInteger(rs, "CREATOR_ID"));
                    tmpDmsArchiveDetail.setCreateDate(getResultSetTimestamp(rs, "CREATE_DATE"));
                    tmpDmsArchiveDetail.setUpdaterID(getResultSetInteger(rs, "UPDATER_ID"));
                    tmpDmsArchiveDetail.setUpdateDate(getResultSetTimestamp(rs, "UPDATE_DATE"));
                    tmpDmsArchiveDetail.setCreatorName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getCreatorID()));
                    tmpDmsArchiveDetail.setUpdaterName(UserInfoFactory.getUserFullName(tmpDmsArchiveDetail.getUpdaterID()));
                    tmpDmsArchiveDetail.setArchiveName(getResultSetString(rs, "ARCHIVE_NAME"));
                    result.add(tmpDmsArchiveDetail);
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
}
