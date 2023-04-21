package com.rooster.action.c2c_candidate;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.utils.candidate.ResumeUploadUtil;

public class GetResumeAction extends Action {

    Logger logger = Logger.getLogger(GetResumeAction.class);

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) {
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
        DataSource db;
        ResumeUploadUtil oRSUtil = new ResumeUploadUtil();
        String sEmailId = String.valueOf(session.getAttribute("UserId"));
        try {
            db = getDataSource(req);
            String sFilePath = oRSUtil.getResumeFilePath(db, sEmailId);
            if (sFilePath != null && !sFilePath.equals("")) {
                req.setAttribute("ResumeLink", sFilePath);
            }
        } catch (SQLException sqle) {
            logger.debug(sqle);
            sqle.printStackTrace();
        } catch (Exception e) {
            logger.debug(e);
            e.printStackTrace();
        }
        return map.findForward("success");
    }
}
