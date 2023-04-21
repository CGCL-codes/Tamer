package com.vlee.servlet.inventory;

import java.math.*;
import java.sql.*;
import java.util.*;
import javax.servlet.http.*;
import com.vlee.bean.distribution.EditSalesOrderSession;
import com.vlee.bean.inventory.*;
import com.vlee.bean.pos.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.inventory.*;
import com.vlee.servlet.main.*;
import com.vlee.util.*;

public class DoStockAdjustmentListing implements Action {

    String strClassName = "DoStockAdjustment";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        try {
            String formName = req.getParameter("formName");
            Log.printVerbose("formName: " + formName);
            if (formName == null) {
                HttpSession session = req.getSession();
                Integer iUser = (Integer) session.getAttribute("userId");
                session.setAttribute("inv-stock-adjustment-listing-form", new StockAdjustmentListingForm(iUser));
                Log.printVerbose(strClassName + "formName: null");
                return new ActionRouter("inv-stock-adjustment-listing-page");
            } else if (formName.equals("setDetails")) {
                try {
                    fnSetSvcCtr(servlet, req, res);
                } catch (Exception ex) {
                    req.setAttribute("errMsg", ex.getMessage());
                    return new ActionRouter("inv-stock-adjustment-listing-page");
                }
                setDateFrom(servlet, req, res);
                setDateTo(servlet, req, res);
                fnConfirmAndSave(servlet, req, res);
            }
            if (formName.equals("confirmAndSave")) {
                try {
                    fnConfirmAndSave(servlet, req, res);
                } catch (Exception ex) {
                    req.setAttribute("errMsg", ex.getMessage());
                }
                return new ActionRouter("inv-stock-adjustment-listing-page");
            }
            if (formName.equals("popupPrint")) {
                return new ActionRouter("inv-print-stock-adjustment-listing-page");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ActionRouter("inv-stock-adjustment-listing-page");
    }

    private void setDateFrom(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        StockAdjustmentListingForm stkAdjFrm = (StockAdjustmentListingForm) session.getAttribute("inv-stock-adjustment-listing-form");
        String FromDate = req.getParameter("txFromDate");
        Timestamp TestDateFrom = TimeFormat.createTimestamp(FromDate);
        stkAdjFrm.setDateFrom(TestDateFrom);
        session.setAttribute("inv-stock-adjustment-listing-form", stkAdjFrm);
    }

    private void setDateTo(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        StockAdjustmentListingForm stkAdjFrm = (StockAdjustmentListingForm) session.getAttribute("inv-stock-adjustment-listing-form");
        String ToDate = req.getParameter("txToDate");
        Timestamp TestDateTo = TimeFormat.createTimestamp(ToDate);
        stkAdjFrm.setDateTo(TestDateTo);
        session.setAttribute("inv-stock-adjustment-listing-form", stkAdjFrm);
    }

    private void fnSetSvcCtr(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        StockAdjustmentListingForm stkAdjFrm = (StockAdjustmentListingForm) session.getAttribute("inv-stock-adjustment-listing-form");
        String branch = req.getParameter("branch");
        stkAdjFrm.setBranch(new Integer(branch));
        session.setAttribute("inv-stock-adjustment-listing-form", stkAdjFrm);
    }

    protected void fnConfirmAndSave(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        StockAdjustmentListingForm stkAdjFrm = (StockAdjustmentListingForm) session.getAttribute("inv-stock-adjustment-listing-form");
        try {
            System.out.println("inside fnConfirmAndSave");
            stkAdjFrm.generateListing();
            Log.printVerbose("list generated.");
            if (stkAdjFrm.getStockAdjustmentCollection() != null) {
                Log.printVerbose("there is something in here.");
            }
            session.setAttribute("inv-stock-adjustment-listing-form", stkAdjFrm);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
