package com.rooster.action.c2c_candidate.edit;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

public class SaveSSN extends Action {

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
        PreparedStatement stmtSSN = null;
        String sUserId = String.valueOf(session.getAttribute("UserId"));
        String sSSN = String.valueOf(req.getParameter("conSSN"));
        try {
            String sSql = "update rooster_candidate_info set ssn = ? where email_id=?;";
            db = getDataSource(req);
            conSSN = db.getConnection();
            stmtSSN = conSSN.prepareStatement(sSql);
            stmtSSN.setString(1, sSSN);
            stmtSSN.setString(2, sUserId);
            stmtSSN.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmtSSN != null) {
                    stmtSSN.close();
                }
                if (conSSN != null) {
                    conSSN.close();
                }
            } catch (SQLException e) {
            }
        }
        return map.findForward("PreSubAuth");
    }
}
