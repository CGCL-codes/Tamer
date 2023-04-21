package com.rooster.action.rss;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import javax.sql.DataSource;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

public class SaveRssCriteria extends Action {

    DataSource db;

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
        String sUserId = String.valueOf(session.getAttribute("UserId"));
        String sInput = req.getParameter("param");
        db = getDataSource(req);
        try {
            res.setContentType("text/xml");
            PrintWriter out = res.getWriter();
            String sOutPut = saveRssCriteria(sInput, sUserId);
            out.write(sOutPut);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (null);
    }

    private String saveRssCriteria(String sSql, String sEmail) {
        String sOutPut = "";
        Connection myCon2 = null;
        boolean bFound = false;
        try {
            myCon2 = db.getConnection();
            Statement stmt = myCon2.createStatement();
            ResultSet rs = stmt.executeQuery("select rss_criteria from rooster_rss_feed_criteria where email_id='" + sEmail + "';");
            while (rs.next()) {
                bFound = true;
            }
        } catch (Exception e) {
        } finally {
            try {
                if (myCon2 != null) {
                    myCon2.close();
                }
            } catch (Exception e) {
            }
        }
        try {
            myCon2 = db.getConnection();
            int i = 0;
            if (bFound) {
                PreparedStatement InsertStmt = myCon2.prepareStatement("update rooster_rss_feed_criteria set rss_criteria=? where email_id = ?");
                InsertStmt.setString(1, sSql);
                InsertStmt.setString(2, sEmail);
                i = InsertStmt.executeUpdate();
            } else {
                PreparedStatement InsertStmt = myCon2.prepareStatement("insert into rooster_rss_feed_criteria(email_id,rss_criteria) values (?,?);");
                InsertStmt.setString(1, sEmail);
                InsertStmt.setString(2, sSql);
                i = InsertStmt.executeUpdate();
            }
            if (i == 1) {
                sOutPut = "SAVED";
            }
        } catch (Exception e) {
            sOutPut = e.toString();
        } finally {
            try {
                if (myCon2 != null) {
                    myCon2.close();
                }
            } catch (Exception e) {
            }
        }
        return sOutPut;
    }
}
