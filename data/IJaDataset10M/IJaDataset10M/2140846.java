package com.vlee.servlet.footwear;

import java.io.*;
import java.math.*;
import java.util.*;
import java.sql.*;
import javax.servlet.http.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.supplier.*;
import com.vlee.servlet.main.*;
import com.vlee.bean.footwear.*;
import com.vlee.util.*;

public class DoInvoiceItemDiscountSummary implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter("footwear-invoice-item-discount-summary-page");
        }
        if (formName.equals("popupPrint")) {
            return new ActionRouter("footwear-invoice-item-discount-summary-printable-page");
        }
        if (formName.equals("popupPrintClosingReport")) {
            String sessionName = req.getParameter("sessionName");
            req.setAttribute("sessionName", sessionName);
            return new ActionRouter("footwear-invoice-item-discount-summary-printable-closing-report-page");
        }
        if (formName.equals("getReport")) {
            fnGetReport(servlet, req, res);
        }
        return new ActionRouter("footwear-invoice-item-discount-summary-page");
    }

    private void fnGetReport(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        InvoiceItemDiscountSummary iids = (InvoiceItemDiscountSummary) session.getAttribute("footwear-invoice-item-discount-summary");
        if (iids == null) {
            Integer userId = (Integer) session.getAttribute("userId");
            iids = new InvoiceItemDiscountSummary(userId);
            session.setAttribute("invoice-item-discount-summary", iids);
        }
        String branchId = req.getParameter("branchId");
        String dateFrom = req.getParameter("dateFrom");
        String dateTo = req.getParameter("dateTo");
        Integer iBranch = null;
        try {
            iBranch = new Integer(branchId);
        } catch (Exception ex) {
        }
        iids.setBranch(iBranch);
        iids.setDateRange(dateFrom, dateTo);
        iids.searchRecords();
    }
}
