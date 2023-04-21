package com.vlee.servlet.trading;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.inventory.*;

public class DoJobsheetPrint implements Action {

    private String strClassName = "DoPrintPopupInvoice";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        Long jobsheetId = new Long(req.getParameter("jobsheetId"));
        String formName = (String) req.getParameter("formName");
        req.setAttribute("print", req.getParameter("print"));
        JobsheetIndexObject jobsheet = JobsheetIndexNut.getObjectTree(jobsheetId);
        req.setAttribute("jobsheet", jobsheet);
        if (jobsheet != null) {
            if (formName == null) {
                if (jobsheet.displayFormat.equals("")) {
                    return new ActionRouter("trading-jobsheet-print-page");
                }
            } else if (formName.equals("noHeader")) {
                return new ActionRouter("trading-jobsheet-print-without-header-page");
            } else if (formName.equals("asDO")) {
                return new ActionRouter("trading-jobsheet-print-page");
            }
        }
        return new ActionRouter("trading-jobsheet-print-page");
    }
}
