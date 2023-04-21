package com.rstr.resume.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class EmailVerification extends Action {

    Connection myCon = null;

    ResultSet rs;

    Statement state;

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) {
        String sEmail = req.getParameter("email");
        int count = 0;
        if (sEmail.equals(new String(""))) {
        } else {
            try {
                String sSql = "";
                sSql = "select count(email_id) from rooster_candidate_info_test where email_id = '" + sEmail + "';";
                DataSource dsTest1 = getDataSource(req);
                myCon = dsTest1.getConnection();
                state = myCon.createStatement();
                rs = state.executeQuery(sSql);
                System.out.println("This is query" + sSql);
                while (rs.next()) {
                    count = rs.getInt(1);
                }
                System.out.println("count value is" + count);
                if (count == 0) {
                    count = 0;
                } else {
                    count = 1;
                }
                res.setContentType("text/xml");
                System.out.println("count value is" + count);
                PrintWriter out = res.getWriter();
                out.write(String.valueOf(count));
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
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
                } catch (SQLException e) {
                }
            }
        }
        return (null);
    }
}
