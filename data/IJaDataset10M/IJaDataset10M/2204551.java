package com.rooster.action.hotlist_candidate.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
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

public class BlackListThisCadidate extends Action {

    Logger logger = Logger.getLogger(BlackListThisCadidate.class);

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
                logger.debug(e);
            }
            return null;
        }
        String sUserId = String.valueOf(session.getAttribute("UserId"));
        String sRemark = req.getParameter("remark");
        Statement stmtBlackList = null;
        Connection dbCon = null;
        DataSource dbSource = null;
        int[] iResult = null;
        if (sRemark == null) {
            sRemark = "You have declined some of steps while applying job";
        }
        try {
            String sSqlBlackList = "update rooster_candidate_info set barred = 1, barred_reason='" + sRemark + "' where email_id='" + sUserId + "';";
            String sSqlInactiveLogin = "update req_userlogin set active=0;";
            dbSource = getDataSource(req);
            dbCon = dbSource.getConnection();
            stmtBlackList = dbCon.createStatement();
            stmtBlackList.addBatch(sSqlBlackList);
            stmtBlackList.addBatch(sSqlInactiveLogin);
            iResult = stmtBlackList.executeBatch();
            logger.info(DbUtil.checkUpdateCounts(iResult));
        } catch (Exception e) {
            logger.debug(e);
        } finally {
            try {
                DbUtil.closeStatement(stmtBlackList);
                DbUtil.closeConnection(dbCon);
            } catch (SQLException e) {
                logger.debug(e);
            }
        }
        return map.findForward("DeclineLogout");
    }
}
