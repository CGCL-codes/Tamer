package com.vlee.servlet.inventory;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.inventory.*;
import com.vlee.bean.reports.*;

public class DoStockBalanceReport implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = (String) req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter("inv-stock-balance-report-page");
        }
        if (formName.equals("popupPrint")) {
            return new ActionRouter("inv-stock-balance-report-printable-page");
        }
        if (formName.equals("setPCCenter")) {
            fnSetPCCenter(servlet, req, res);
        }
        return new ActionRouter("inv-stock-balance-report-page");
    }

    private void fnSetPCCenter(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        StockBalanceSession sbrs = (StockBalanceSession) session.getAttribute("inv-stock-balance-report-session");
        Integer iPCCenter = new Integer(req.getParameter("pcCenter"));
        try {
            sbrs.reset();
            sbrs.setPCCenter(iPCCenter);
            sbrs.generateLocationSummary();
        } catch (Exception ex) {
        }
    }
}
