package com.rooster.action.submission.client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.action.common.DbUtil;
import com.rooster.client.Util.ClientUtil;

public class ApproveRequest extends Action {

    static Logger logger = Logger.getLogger(ApproveRequest.class.getName());

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
            }
            return null;
        }
        String sApprovedBy = String.valueOf(session.getAttribute("UserId"));
        String sCandEmail = String.valueOf(req.getParameter("CandEmail"));
        String sClientId = String.valueOf(req.getParameter("ClientId"));
        String sEmail = String.valueOf(req.getParameter(sCandEmail + "_sEmail"));
        String sFName = String.valueOf(req.getParameter(sCandEmail + "_first_name"));
        String sLName = String.valueOf(req.getParameter(sCandEmail + "_last_name"));
        String sSSn = String.valueOf(req.getParameter(sCandEmail + "_ssn"));
        String sCity = String.valueOf(req.getParameter(sCandEmail + "_location"));
        String sState = String.valueOf(req.getParameter(sCandEmail + "_state"));
        String sPhoneNo = String.valueOf(req.getParameter(sCandEmail + "_phone_no"));
        String sAvailability = String.valueOf(req.getParameter(sCandEmail + "_availability"));
        String sRelocation = String.valueOf(req.getParameter(sCandEmail + "_relocation"));
        String sWorkAuth = String.valueOf(req.getParameter(sCandEmail + "_visa_status"));
        String sRateOpt = String.valueOf(req.getParameter(sCandEmail + "_rate_opt"));
        String sRate = String.valueOf(req.getParameter(sCandEmail + "_rate"));
        String sBasicSkill = String.valueOf(req.getParameter(sCandEmail + "_basic_skill"));
        String sTraining = String.valueOf(req.getParameter(sCandEmail + "_training"));
        String sCertification = String.valueOf(sCandEmail + "_certify");
        String sReumeLinK = String.valueOf(req.getParameter(sCandEmail + "_resumeLink"));
        DataSource dbSrc = null;
        Connection dbCon = null;
        PreparedStatement stmnt = null;
        try {
            dbSrc = getDataSource(req);
            dbCon = dbSrc.getConnection();
            String sSql = "select distinct id from rooster_clnt_req_cand where email_id = '" + sCandEmail + "' and client_id='" + sClientId + "'";
            stmnt = dbCon.prepareStatement("insert into rooster_clnt_approval (email_id, first_name, last_name, ssn, state, city, phone_no, availability, relocation, work_auth, rate, rate_opt,basic_skill, training, certification, formatted_resume_link, ref_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, (" + sSql + "))");
            stmnt.setString(1, sEmail.equals("null") ? "" : sEmail);
            stmnt.setString(2, sFName.equals("null") ? "" : sFName);
            stmnt.setString(3, sLName.equals("null") ? "" : sLName);
            stmnt.setString(4, sSSn.equals("null") ? "" : sSSn);
            stmnt.setString(5, sState.equals("null") ? "" : sState);
            stmnt.setString(6, sCity.equals("null") ? "" : sCity);
            stmnt.setString(7, sPhoneNo.equals("null") ? "" : sPhoneNo);
            stmnt.setString(8, sAvailability.equals("null") ? "" : sAvailability);
            stmnt.setString(9, sRelocation.equals("null") ? "" : sRelocation);
            stmnt.setString(10, sWorkAuth.equals("null") ? "" : sWorkAuth);
            stmnt.setString(11, sRate.equals("null") ? "" : sRate);
            stmnt.setString(12, sRateOpt.equals("null") ? "" : sRateOpt);
            stmnt.setString(13, sBasicSkill.equals("null") ? "" : sBasicSkill);
            stmnt.setString(14, sTraining.equals("null") ? "" : sTraining);
            stmnt.setString(15, sCertification.equals("null") ? "" : sCertification);
            stmnt.setString(16, sReumeLinK.equals("null") ? "" : sReumeLinK);
            stmnt.executeUpdate();
            setApproved(req, sApprovedBy, sCandEmail, sClientId);
        } catch (SQLException sqle) {
            logger.debug(sqle.getMessage());
        } finally {
            try {
                DbUtil.closePreparedStatement(stmnt);
                DbUtil.closeConnection(dbCon);
            } catch (SQLException sql) {
            }
        }
        req.setAttribute("ClientId", sClientId);
        req.setAttribute("CandEmail", sCandEmail);
        return map.findForward("success");
    }

    private void setApproved(HttpServletRequest req, String sApprovedBy, String sCandEmail, String sClientId) {
        DataSource dbSrc = null;
        Connection dbCon = null;
        Statement stmnt = null;
        try {
            dbSrc = getDataSource(req);
            dbCon = dbSrc.getConnection();
            stmnt = dbCon.createStatement();
            stmnt.executeUpdate("update rooster_clnt_req_cand set approved = '1', approved_by ='" + sApprovedBy + "', approved_date='" + ClientUtil.getTodayDate() + "' where email_id='" + sCandEmail + "' and client_id='" + sClientId + "'");
        } catch (SQLException sqle) {
            logger.debug(sqle.getMessage());
        } finally {
            try {
                DbUtil.closeStatement(stmnt);
                DbUtil.closeConnection(dbCon);
            } catch (SQLException sql) {
            }
        }
    }
}
