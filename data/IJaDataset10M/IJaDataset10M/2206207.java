package com.rooster.action.submission.client;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.form.client.CandInfoDisplayForm;

public class MailCompose extends Action {

    static Logger logger = Logger.getLogger(MailCompose.class.getName());

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
        String sClientId = String.valueOf(req.getAttribute("ClientId"));
        String sCandEmail = String.valueOf(req.getAttribute("CandEmail"));
        ArrayList<CandInfoDisplayForm> alCandInfo = getMailDetails(req, sCandEmail, sClientId);
        req.setAttribute("MailInfo", alCandInfo);
        req.setAttribute("ClientEmail", getClientEmail(req, sClientId));
        req.setAttribute("CandEmail", sCandEmail);
        return map.findForward("success");
    }

    private ArrayList<CandInfoDisplayForm> getMailDetails(HttpServletRequest req, String sCandEmail, String sClientId) {
        ArrayList<CandInfoDisplayForm> alCandInfo = new ArrayList<CandInfoDisplayForm>();
        CandInfoDisplayForm candInfo = new CandInfoDisplayForm();
        DataSource dbSrc = null;
        Connection dbCon = null;
        ResultSet rs = null;
        Statement stmnt = null;
        try {
            dbSrc = getDataSource(req);
            dbCon = dbSrc.getConnection();
            stmnt = dbCon.createStatement();
            rs = stmnt.executeQuery("select * from rooster_clnt_approval where ref_id= (select distinct id from rooster_clnt_req_cand where approved = '1' and email_id='" + sCandEmail + "' and client_id='" + sClientId + "')");
            while (rs.next()) {
                candInfo.setFName(rs.getString("first_name"));
                candInfo.setLName(rs.getString("last_name"));
                candInfo.setSsn(rs.getString("ssn"));
                ;
                candInfo.setCity(rs.getString("city"));
                candInfo.setState(rs.getString("state"));
                candInfo.setEmailId(rs.getString("email_id"));
                candInfo.setPhoneNo(rs.getString("phone_no"));
                candInfo.setAvailability(rs.getString("availability"));
                candInfo.setRelocation(rs.getString("relocation"));
                candInfo.setWorkAuth(rs.getString("work_auth"));
                candInfo.setRateOpt(rs.getString("rate"));
                candInfo.setRate(rs.getString("rate_opt"));
                candInfo.setBasicSkill(rs.getString("basic_skill"));
                candInfo.setTraining(rs.getString("training"));
                candInfo.setCertification(rs.getString("certification"));
                candInfo.setFormatted_resume_link(rs.getString("formatted_actual_path"));
            }
            alCandInfo.add(candInfo);
        } catch (SQLException sqle) {
            logger.debug(sqle.getMessage());
        } finally {
            try {
                if (stmnt != null) {
                    stmnt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (dbCon != null) {
                    dbCon.close();
                }
            } catch (SQLException sql) {
            }
        }
        return alCandInfo;
    }

    private String getClientEmail(HttpServletRequest req, String sClientId) {
        DataSource dbSrc = null;
        Connection dbCon = null;
        Statement stmnt = null;
        ResultSet rs = null;
        String sClientEmail = new String();
        try {
            dbSrc = getDataSource(req);
            dbCon = dbSrc.getConnection();
            stmnt = dbCon.createStatement();
            rs = stmnt.executeQuery("select distinct client_mailid from hotlist_client_tbl where client_id='" + sClientId + "'");
            while (rs.next()) {
                sClientEmail = rs.getString(1);
            }
        } catch (SQLException sqle) {
            logger.debug(sqle.getMessage());
        } finally {
            try {
                if (stmnt != null) {
                    stmnt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (dbCon != null) {
                    dbCon.close();
                }
            } catch (SQLException sql) {
            }
        }
        return sClientEmail;
    }
}
