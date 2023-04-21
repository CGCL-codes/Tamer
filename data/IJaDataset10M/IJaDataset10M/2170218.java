package com.vlee.servlet.inventory;

import com.vlee.servlet.main.*;
import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.sql.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.inventory.*;

public class DoBulkStockAdjustment implements Action {

    public static StockAdjustmentThread genThread = new StockAdjustmentThread("StkAdjustment");

    private String strClassName = "DoBulkStockAdjustment: ";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String locationId = req.getParameter("locationId");
        req.setAttribute("locationId", locationId);
        if (genThread == null) {
            genThread = new StockAdjustmentThread("StkAdjustment");
        } else {
            if (!genThread.isAlive()) {
                genThread = new StockAdjustmentThread("StkAdjustment");
                req.setAttribute("genThread", genThread);
            }
        }
        genThread.setPriority(Thread.MIN_PRIORITY);
        req.setAttribute("genThread", genThread);
        String fwdPage = req.getParameter("fwdPage");
        if (fwdPage == null) {
            fwdPage = "inv-bulk-stock-adjustment-page";
        }
        String formName = req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter(fwdPage);
        }
        if (formName.equals("showProgress")) {
            ProgressBar progressBar = genThread.getProgressBar();
            progressBar.refreshURL = "inv-bulk-stock-adjustment.do?formName=showProgress";
            req.setAttribute("progressBar", progressBar);
            fwdPage = "common-progress-bar-page";
            return new ActionRouter(fwdPage);
        }
        if (formName.equals("runGen")) {
            fnRunStockAdjustment(servlet, req, res);
            return new ActionRouter(fwdPage);
        }
        Log.printVerbose("default action router!!------------");
        return new ActionRouter(fwdPage);
    }

    private void fnRunStockAdjustment(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        if (genThread.getStatus().equals(StockAdjustmentThread.STATUS_READY)) {
            HttpSession session = req.getSession();
            Integer userId = (Integer) session.getAttribute("userId");
            Timestamp tsNextMonth = TimeFormat.getTimestamp();
            tsNextMonth = TimeFormat.set(tsNextMonth, Calendar.DATE, 1);
            tsNextMonth = TimeFormat.add(tsNextMonth, 0, 1, 0);
            String locationId = req.getParameter("locationId");
            Integer location = new Integer(locationId);
            genThread.setConfig(tsNextMonth, location, userId);
            genThread.start();
        } else {
            String strErrMsg = " The generator is not ready for new request...... ";
            req.setAttribute("strErrMsg", strErrMsg);
        }
        req.setAttribute("genThread", genThread);
    }
}
