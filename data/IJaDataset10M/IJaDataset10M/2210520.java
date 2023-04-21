package com.rooster.action.employee;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.rooster.action.common.UserCheck;

public class AjaxUserCheck extends Action {

    private Logger logger = Logger.getLogger(AjaxUserCheck.class);

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws IOException {
        logger.info("com.rooster.action.employee.AjaxUserCheck.execute - Entry");
        String sEmail = req.getParameter("input");
        String sOutput = "";
        boolean bExist = false;
        if (sEmail != null) {
            DataSource dbSrc = getDataSource(req);
            bExist = UserCheck.isUserExist(dbSrc, sEmail, new String(), false);
            sOutput = String.valueOf(bExist);
        }
        try {
            res.setContentType("text/xml");
            PrintWriter out = res.getWriter();
            out.write(sOutput);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.warn("com.rooster.action.employee.AjaxUserCheck.execute - Error" + e);
        }
        logger.info("com.rooster.action.employee.AjaxUserCheck.execute - Exit");
        return null;
    }
}
