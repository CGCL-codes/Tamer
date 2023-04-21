package com.vlee.servlet.distribution;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.math.*;
import java.sql.*;
import java.lang.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.customer.*;
import com.vlee.util.*;
import com.vlee.bean.distribution.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.inventory.*;

public class DoApprovalSalesOrderListing extends ActionDo implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        String fwdPage = req.getParameter("fwdPage");
        if (fwdPage == null) {
            fwdPage = "dist-approval-sales-order-listing-page";
        }
        if (formName == null) {
            return new ActionRouter(fwdPage);
        }
        if (formName.equals("toggleApproveSO")) {
            try {
                fnToggleApproval(servlet, req, res);
                return new ActionRouter(fwdPage);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
                ex.printStackTrace();
            }
        }
        if (formName.equals("getList")) {
            try {
                fnGetListing(servlet, req, res);
                return new ActionRouter(fwdPage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (formName.equals("popupPrint")) {
            try {
                fnGetListing(servlet, req, res);
                return new ActionRouter(fwdPage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new ActionRouter(fwdPage);
    }

    private void fnGetListing(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String branch = req.getParameter("branch");
        String dateFrom = req.getParameter("dateFrom");
        String dateTo = req.getParameter("dateTo");
        String appDateFrom = req.getParameter("approvalDateFrom");
        String appDateTo = req.getParameter("approvalDateTo");
        String approvalStatus = req.getParameter("approvalStatus");
        String approverId = req.getParameter("approverId");
        Integer iBranch = null;
        Timestamp tsFrom = TimeFormat.createTimestamp(dateFrom);
        Timestamp tsTo = TimeFormat.createTimestamp(dateTo);
        try {
            iBranch = new Integer(branch);
        } catch (Exception ex) {
        }
        HttpSession session = req.getSession();
        ApprovalSalesOrderListingForm form = (ApprovalSalesOrderListingForm) session.getAttribute("approval-sales-order-listing-form");
        form.setBranch(iBranch);
        form.setDateFrom(tsFrom);
        form.setDateTo(tsTo);
        form.setApprovalDateFrom(TimeFormat.createTimestamp(appDateFrom));
        form.setApprovalDateTo(TimeFormat.createTimestamp(appDateTo));
        Log.printVerbose("approvalStatus: " + approvalStatus);
        form.setApprovalStatus(approvalStatus);
        Log.printVerbose("approverId: " + approverId);
        form.setApproverId(new Integer(approverId));
        Vector vecResult = form.getListing();
        req.setAttribute("vecListing", vecResult);
    }

    private void fnToggleApproval(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String result = req.getParameter("approveSO");
        String pkid = req.getParameter("pkid");
        Integer userId = new Integer(req.getParameter("userId"));
        HttpSession session = req.getSession();
        ApprovalSalesOrderListingForm form = (ApprovalSalesOrderListingForm) session.getAttribute("approval-sales-order-listing-form");
        if (result != null && result.equals("yes")) {
            form.setApproval(new Integer(pkid), SalesOrderIndexBean.STATUS_APPROVED, userId);
        } else {
            form.setApproval(new Integer(pkid), SalesOrderIndexBean.STATUS_UNAPPROVED, userId);
        }
    }
}
