package com.rooster.action.candidate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

public class ShowSSNConfirmation extends Action {

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
        DataSource db = null;
        Connection conSSN = null;
        Statement stmtSSN = null;
        ResultSet rsSSN = null;
        String sUserId = String.valueOf(session.getAttribute("UserId"));
        String sJobId = String.valueOf(req.getAttribute("jobId"));
        String sSSN = new String("");
        try {
            String sSql = "select ssn from rooster_candidate_info where email_id='" + sUserId + "';";
            db = getDataSource(req);
            conSSN = db.getConnection();
            stmtSSN = conSSN.createStatement();
            rsSSN = stmtSSN.executeQuery(sSql);
            while (rsSSN.next()) {
                sSSN = rsSSN.getString("ssn") == null ? "0000" : rsSSN.getString("ssn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rsSSN != null) {
                    rsSSN.close();
                }
                if (stmtSSN != null) {
                    stmtSSN.close();
                }
                if (conSSN != null) {
                    conSSN.close();
                }
            } catch (SQLException e) {
            }
        }
        req.setAttribute("ssn", sSSN);
        if (sSSN.toString().equals("null")) {
            req.setAttribute("ssn", "0000");
        } else {
            req.setAttribute("ssn", sSSN);
        }
        req.setAttribute("jobId", sJobId);
        return map.findForward("SSNConfirm");
    }
}
