package com.rooster.action.hotlist_candidate;

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
import javax.sql.rowset.CachedRowSet;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.form.call_log.CallLogForm;
import com.sun.rowset.CachedRowSetImpl;

public class CallLogDetails extends Action {

    static Logger logger = Logger.getLogger(CallLogDetails.class.getName());

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
        db = getDataSource(req);
        CachedRowSet rs = null;
        String sEmailId = String.valueOf(session.getAttribute("UserId"));
        ArrayList alLogList = new ArrayList();
        CallLogForm dbForm = null;
        try {
            String sSql = "select clrJobId,recruiter_id,recruiter_comments,call_status,call_date,candidate_email_id from rooster_call_log_remark_mail where ";
            String sWhereClause = "";
            if ((sEmailId == null) || (sEmailId.equals(new String("")))) {
            } else {
                sWhereClause += " candidate_email_id like '%%%" + sEmailId + "%%%' and ";
            }
            sWhereClause += " 1 = 1 ";
            sWhereClause += ";";
            sSql = sSql + sWhereClause;
            rs = getRs(sSql);
            boolean bDataFound = false;
            while (rs.next()) {
                bDataFound = true;
                dbForm = new CallLogForm();
                dbForm.setClrJobId(rs.getString(1));
                dbForm.setRecruiter_id(rs.getString(2));
                dbForm.setReq_comments(rs.getString(3));
                dbForm.setStatus(rs.getString(4));
                dbForm.setCall_date(rs.getString(5));
                dbForm.setEmail_id(rs.getString(6));
                alLogList.add(dbForm);
            }
            if (bDataFound) {
                req.setAttribute("LOG_LIST", alLogList);
            } else {
                String sHtml = "<table id='demo1_table'><tr><td align='center'><div class='smallbluetext'>No Data Found For Your Search Criteria.</div></td></tr></table>";
                req.setAttribute("groupHtml", sHtml);
            }
        } catch (Exception e) {
            logger.debug(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }
        return map.findForward("success");
    }

    private CachedRowSet getRs(String sSql) {
        Connection myCon = null;
        ResultSet rs = null;
        Statement state = null;
        CachedRowSet crs = null;
        try {
            myCon = db.getConnection();
            state = myCon.createStatement();
            rs = state.executeQuery(sSql);
            crs = new CachedRowSetImpl();
            crs.populate(rs);
        } catch (Exception e) {
            logger.debug(e);
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
        return crs;
    }
}
