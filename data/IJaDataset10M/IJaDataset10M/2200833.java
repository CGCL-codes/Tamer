package com.rooster.action.requirement;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.form.ReqDisplayForm;
import com.rooster.utils.CurrentDate;

public class SearchRequirementResult extends Action {

    private Logger logger = Logger.getLogger(SearchRequirementResult.class.getName());

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws Exception {
        logger.info("com.rooster.action.requirement.SearchRequirementResult.execute - Entry");
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
                logger.debug("com.rooster.action.requirement.SearchRequirementResult.execute - Error - session out" + e);
            }
            return null;
        }
        DataSource db = getDataSource(req);
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        ReqDisplayForm reqDisfrm = null;
        ArrayList reqUnAssignedList = new ArrayList();
        ArrayList reqAssignedList = new ArrayList();
        try {
            String sUnAssignedQuery = String.valueOf(session.getAttribute("UNASSIGNED_QUERY_STRING"));
            myCon = db.getConnection();
            state = myCon.createStatement();
            rs = state.executeQuery(sUnAssignedQuery);
            while (rs.next()) {
                reqDisfrm = new ReqDisplayForm();
                reqDisfrm.setReqId(rs.getString("reqId"));
                reqDisfrm.setRdDt(CurrentDate.convertDBFormatToUS(String.valueOf(rs.getDate("rdDt"))));
                reqDisfrm.setClient(rs.getString("client"));
                reqDisfrm.setClrJobId(rs.getString("clrJobId"));
                reqDisfrm.setRsDt(CurrentDate.convertDBFormatToUS(String.valueOf(rs.getDate("rsDt"))));
                reqDisfrm.setLocation(rs.getString("location"));
                reqDisfrm.setState(rs.getString("state"));
                reqDisfrm.setZip(rs.getString("zip"));
                reqDisfrm.setDuration(rs.getString("duration"));
                reqDisfrm.setTemp2perm(rs.getString("temp2perm"));
                reqDisfrm.setPriorComm(rs.getString("priorComm"));
                reqDisfrm.setMbr(rs.getString("mbr"));
                reqDisfrm.setT1099(rs.getString("t1099"));
                reqDisfrm.setW2(rs.getString("w2"));
                reqDisfrm.setH1accept(rs.getString("h1accept"));
                reqDisfrm.setPostSkill(rs.getString("postSkill"));
                reqDisfrm.setDescription(rs.getString("description"));
                reqDisfrm.setExp(rs.getString("exp"));
                reqDisfrm.setArchive(rs.getString("archive"));
                reqDisfrm.setOpening(rs.getString("opening"));
                reqDisfrm.setAssigned(rs.getString("assigned"));
                reqUnAssignedList.add(reqDisfrm);
            }
        } catch (Exception e) {
            logger.debug("com.rooster.action.requirement.SearchRequirementResult.execute - Error" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (state != null) {
                    state.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (Exception e) {
                logger.warn(e);
            }
        }
        try {
            String sUnAssignedQuery = String.valueOf(session.getAttribute("ASSIGNED_QUERY_STRING"));
            myCon = db.getConnection();
            state = myCon.createStatement();
            rs = state.executeQuery(sUnAssignedQuery);
            while (rs.next()) {
                reqDisfrm = new ReqDisplayForm();
                reqDisfrm.setReqId(rs.getString("reqId"));
                reqDisfrm.setRdDt(CurrentDate.convertDBFormatToUS(String.valueOf(rs.getDate("rdDt"))));
                reqDisfrm.setClient(rs.getString("client"));
                reqDisfrm.setClrJobId(rs.getString("clrJobId"));
                reqDisfrm.setRsDt(CurrentDate.convertDBFormatToUS(String.valueOf(rs.getDate("rsDt"))));
                reqDisfrm.setLocation(rs.getString("location"));
                reqDisfrm.setState(rs.getString("state"));
                reqDisfrm.setZip(rs.getString("zip"));
                reqDisfrm.setDuration(rs.getString("duration"));
                reqDisfrm.setTemp2perm(rs.getString("temp2perm"));
                reqDisfrm.setPriorComm(rs.getString("priorComm"));
                reqDisfrm.setMbr(rs.getString("mbr"));
                reqDisfrm.setT1099(rs.getString("t1099"));
                reqDisfrm.setW2(rs.getString("w2"));
                reqDisfrm.setH1accept(rs.getString("h1accept"));
                reqDisfrm.setPostSkill(rs.getString("postSkill"));
                reqDisfrm.setDescription(rs.getString("description"));
                reqDisfrm.setExp(rs.getString("exp"));
                reqDisfrm.setArchive(rs.getString("archive"));
                reqDisfrm.setOpening(rs.getString("opening"));
                reqDisfrm.setAssigned(rs.getString("assigned"));
                reqAssignedList.add(reqDisfrm);
            }
        } catch (Exception e) {
            logger.debug("com.rooster.action.requirement.SearchRequirementResult.execute - Error" + e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (state != null) {
                    state.close();
                }
                if (myCon != null) {
                    myCon.close();
                }
            } catch (Exception e) {
                logger.warn(e);
            }
        }
        if ((reqUnAssignedList.size() + reqAssignedList.size()) == 1) {
            req.setAttribute("clrJobId", reqDisfrm.getClrJobId());
            logger.info("com.rooster.action.requirement.SearchRequirementResult.execute - Exit");
            return map.findForward("SearchResultSingle");
        }
        if (reqUnAssignedList.size() > 0) {
            req.setAttribute("UnAssignedList", reqUnAssignedList);
        } else {
            req.setAttribute("NothingUnAssignedList", "No Data");
        }
        if (reqAssignedList.size() > 0) {
            req.setAttribute("AssignedList", reqAssignedList);
        } else {
            req.setAttribute("NothingAssignedList", "No Data");
        }
        logger.info("com.rooster.action.requirement.SearchRequirementResult.execute - Exit");
        return map.findForward("show_result");
    }
}
