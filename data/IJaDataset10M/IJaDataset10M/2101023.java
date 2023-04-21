package com.empower.server;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.empower.model.ContainerDetailModel;
import com.empower.model.TCConsignmentInfoModel;
import com.empower.model.TCConsolidatedInfoModel;
import com.empower.utils.DBUtil;

public class TrackCnsgmntIntfImpl implements TrackCnsgmntInterface, TrackConsignmentSQLInterface {

    public ArrayList getConsignmentStatus(String cnsgmntnbr, String cnsgneeName, String fromDate, String toDate) throws SQLException, Exception {
        ArrayList cnsgnDetails = null;
        Connection dbConn = null;
        PreparedStatement getCnsgnDetailsStmt = null;
        ResultSet cnsgnResultSet = null;
        try {
            dbConn = DBUtil.getDBconnection();
            if (null != cnsgmntnbr) {
                getCnsgnDetailsStmt = dbConn.prepareStatement(getCnsgnBasicDataByCnsgnId);
                getCnsgnDetailsStmt.setString(1, cnsgmntnbr);
                cnsgnResultSet = getCnsgnDetailsStmt.executeQuery();
            } else {
                getCnsgnDetailsStmt = dbConn.prepareStatement(getCnsgnBasicDataByOthers);
                getCnsgnDetailsStmt.setString(1, "%" + cnsgneeName + "%");
                getCnsgnDetailsStmt.setString(2, fromDate);
                getCnsgnDetailsStmt.setString(3, toDate);
                cnsgnResultSet = getCnsgnDetailsStmt.executeQuery();
            }
            cnsgnDetails = new ArrayList();
            while (cnsgnResultSet.next()) {
                TCConsignmentInfoModel tcCnsgnmntInfoMdl = new TCConsignmentInfoModel();
                tcCnsgnmntInfoMdl.setCnsgnmntNbr(cnsgnResultSet.getString("CNSGN_ID"));
                tcCnsgnmntInfoMdl.setConsigneeName(cnsgnResultSet.getString("CNSGNEE_NAME"));
                tcCnsgnmntInfoMdl.setConsignerName(cnsgnResultSet.getString("CNSGNER_NAME"));
                tcCnsgnmntInfoMdl.setCnsgnDate(cnsgnResultSet.getString("CNSGN_DT"));
                tcCnsgnmntInfoMdl.setPkgsCnt(cnsgnResultSet.getLong("NO_0F_PCKGS"));
                tcCnsgnmntInfoMdl.setSrcStn(cnsgnResultSet.getString("SRC_STN_ID"));
                tcCnsgnmntInfoMdl.setDstStn(cnsgnResultSet.getString("DST_STN_ID"));
                PreparedStatement totalWgtDtlsStmt = null;
                totalWgtDtlsStmt = dbConn.prepareStatement(getTotalWgt);
                totalWgtDtlsStmt.setString(1, tcCnsgnmntInfoMdl.getCnsgnmntNbr());
                ResultSet totalWgtResultSet = totalWgtDtlsStmt.executeQuery();
                String totalWgt = null;
                while (totalWgtResultSet.next()) {
                    totalWgt = totalWgtResultSet.getString("TOTAL_WGT");
                }
                tcCnsgnmntInfoMdl.setTotalWgt(totalWgt);
                cnsgnDetails.add(tcCnsgnmntInfoMdl);
            }
        } catch (SQLException sqlEx) {
            DBUtil.rollBack(dbConn, getCnsgnDetailsStmt);
            throw sqlEx;
        } catch (Exception sqlEx) {
            DBUtil.rollBack(dbConn, getCnsgnDetailsStmt);
            throw sqlEx;
        } finally {
            DBUtil.closeDBEntities(dbConn, getCnsgnDetailsStmt);
        }
        return cnsgnDetails;
    }

    public TCConsolidatedInfoModel getConsolidatedInfo(String cnsgmntnbr) throws SQLException, Exception {
        Connection dbConn = null;
        PreparedStatement getTotalPkgsStmt = null;
        PreparedStatement getPkgCntAtDstnStnStmt = null;
        PreparedStatement getDlvrdPkgCntStmt = null;
        PreparedStatement getPkgCntOnWayStmt = null;
        PreparedStatement getLastDlvrdDateTime = null;
        ResultSet ttlPckgCntResultSet = null;
        ResultSet pkgCntAtDstnStnResultSet = null;
        ResultSet dlvrdPkgCntResultSet = null;
        ResultSet pkgCntOnWayResultSet = null;
        ResultSet lastDlvrdDateTimeResultSet = null;
        TCConsolidatedInfoModel cnsgnDetails = null;
        cnsgnDetails = new TCConsolidatedInfoModel();
        try {
            long totalPkgs = 0;
            long pkgCntDlvrd = 0;
            long pkgCntDstnStn = 0;
            long pkgCntOnWay = 0;
            dbConn = DBUtil.getDBconnection();
            if (null == cnsgmntnbr) {
                return null;
            }
            getTotalPkgsStmt = dbConn.prepareStatement(getTotalPkgs);
            getTotalPkgsStmt.setString(1, cnsgmntnbr);
            ttlPckgCntResultSet = getTotalPkgsStmt.executeQuery();
            if (ttlPckgCntResultSet.next()) {
                totalPkgs = ttlPckgCntResultSet.getLong("NO_0F_PCKGS");
                cnsgnDetails.setTotalPkgs(totalPkgs);
                cnsgnDetails.setEntryChallanId(ttlPckgCntResultSet.getString("ENTRY_CHALLAN_ID"));
            }
            getDlvrdPkgCntStmt = dbConn.prepareStatement(getDlvrdPkgCnt);
            getDlvrdPkgCntStmt.setString(1, cnsgmntnbr);
            dlvrdPkgCntResultSet = getDlvrdPkgCntStmt.executeQuery();
            String dlvryChallanId = null;
            if (dlvrdPkgCntResultSet.next()) {
                pkgCntDlvrd = dlvrdPkgCntResultSet.getLong("DLVRD_PCKG_CNT");
                cnsgnDetails.setNoOfPkgsDelivered(pkgCntDlvrd);
                dlvryChallanId = dlvrdPkgCntResultSet.getString("DLVRY_CHALLAN_ID");
                cnsgnDetails.setDeliveryChallanId(dlvryChallanId);
            }
            getPkgCntAtDstnStnStmt = dbConn.prepareStatement(getPkgCntAtDstnStn);
            getPkgCntAtDstnStnStmt.setString(1, cnsgmntnbr);
            pkgCntAtDstnStnResultSet = getPkgCntAtDstnStnStmt.executeQuery();
            if (pkgCntAtDstnStnResultSet.next()) {
                pkgCntDstnStn = pkgCntAtDstnStnResultSet.getLong("PCKG_DSTN");
                pkgCntDstnStn -= pkgCntDlvrd;
                cnsgnDetails.setNoOfPkgsAtDstStn(pkgCntDstnStn);
            }
            getPkgCntOnWayStmt = dbConn.prepareStatement(getPkgCntOnWay);
            getPkgCntOnWayStmt.setString(1, cnsgmntnbr);
            pkgCntOnWayResultSet = getPkgCntOnWayStmt.executeQuery();
            if (pkgCntOnWayResultSet.next()) {
                pkgCntOnWay = pkgCntOnWayResultSet.getLong("ON_WAY_PCKGS");
                cnsgnDetails.setNoOfPkgsOnTheWay(pkgCntOnWay);
            }
            getLastDlvrdDateTime = dbConn.prepareStatement(getLatestDlvryDt);
            getLastDlvrdDateTime.setString(1, dlvryChallanId);
            lastDlvrdDateTimeResultSet = getLastDlvrdDateTime.executeQuery();
            if (lastDlvrdDateTimeResultSet.next()) {
                cnsgnDetails.setLastDlvrDateTime(lastDlvrdDateTimeResultSet.getString("DLVERY_DT_TM"));
            }
            long pkgCntSrcStn = totalPkgs - pkgCntDstnStn - pkgCntDlvrd - pkgCntOnWay;
            cnsgnDetails.setNoOfPkgsAtSrcStn(pkgCntSrcStn);
        } catch (SQLException sqlEx) {
            DBUtil.rollBack(dbConn, getLastDlvrdDateTime);
            DBUtil.rollBack(dbConn, getTotalPkgsStmt);
            DBUtil.rollBack(dbConn, getPkgCntAtDstnStnStmt);
            DBUtil.rollBack(dbConn, getPkgCntOnWayStmt);
            DBUtil.rollBack(dbConn, getDlvrdPkgCntStmt);
            throw sqlEx;
        } catch (Exception sqlEx) {
            DBUtil.rollBack(dbConn, getLastDlvrdDateTime);
            DBUtil.rollBack(dbConn, getTotalPkgsStmt);
            DBUtil.rollBack(dbConn, getPkgCntAtDstnStnStmt);
            DBUtil.rollBack(dbConn, getPkgCntOnWayStmt);
            DBUtil.rollBack(dbConn, getDlvrdPkgCntStmt);
            throw sqlEx;
        } finally {
            dbConn.close();
            if (null != getLastDlvrdDateTime) {
                getLastDlvrdDateTime.close();
            }
            if (null != getTotalPkgsStmt) {
                getTotalPkgsStmt.close();
            }
            if (null != getPkgCntAtDstnStnStmt) {
                getPkgCntAtDstnStnStmt.close();
            }
            if (null != getPkgCntOnWayStmt) {
                getPkgCntOnWayStmt.close();
            }
            if (null != getDlvrdPkgCntStmt) {
                getDlvrdPkgCntStmt.close();
            }
        }
        return cnsgnDetails;
    }

    public ArrayList<ContainerDetailModel> getContainerDetailForOnTheWayPackages(String cnsgmntnbr) throws SQLException, Exception {
        Connection dbConn = null;
        PreparedStatement getPkgCntOnWayWithContainerStmt = null;
        ResultSet pkgCntOnWayResultSet = null;
        ArrayList<ContainerDetailModel> cnsgnWithContainerDetailList = new ArrayList<ContainerDetailModel>();
        try {
            dbConn = DBUtil.getDBconnection();
            if (null == cnsgmntnbr) {
                return null;
            }
            getPkgCntOnWayWithContainerStmt = dbConn.prepareStatement(getPkgCntOnWayWithContainer);
            getPkgCntOnWayWithContainerStmt.setString(1, cnsgmntnbr);
            pkgCntOnWayResultSet = getPkgCntOnWayWithContainerStmt.executeQuery();
            while (pkgCntOnWayResultSet.next()) {
                ContainerDetailModel cnsgnWithContainerDetail = new ContainerDetailModel();
                cnsgnWithContainerDetail.setPkgsCnt(pkgCntOnWayResultSet.getLong("ON_WAY_PCKGS"));
                cnsgnWithContainerDetail.setContainer(pkgCntOnWayResultSet.getString("CNTNR_NAME"));
                cnsgnWithContainerDetail.setSubContainer(pkgCntOnWayResultSet.getString("SUB_CNTNR_NAME"));
                cnsgnWithContainerDetailList.add(cnsgnWithContainerDetail);
            }
        } catch (SQLException sqlEx) {
            DBUtil.rollBack(dbConn, getPkgCntOnWayWithContainerStmt);
            throw sqlEx;
        } catch (Exception sqlEx) {
            DBUtil.rollBack(dbConn, getPkgCntOnWayWithContainerStmt);
            throw sqlEx;
        } finally {
            dbConn.close();
            if (null != getPkgCntOnWayWithContainerStmt) {
                getPkgCntOnWayWithContainerStmt.close();
            }
        }
        return cnsgnWithContainerDetailList;
    }
}
