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

public class DoStockMovement implements Action {

    private String strClassName = "DoStockMovement";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = (String) req.getParameter("formName");
        try {
            fnPopulateSearchForm(servlet, req, res);
            if (formName == null) {
                return new ActionRouter("inv-stock-movement-page");
            }
            if (formName.equals("popupPrint")) {
                fnGetStockDelta(servlet, req, res);
                return new ActionRouter("inv-printable-stock-movement-page");
            }
            if (formName.equals("getStkDelta")) {
                fnGetStockDelta(servlet, req, res);
            }
        } catch (Exception ex) {
            req.setAttribute("errMsg", ex.getMessage());
            ex.printStackTrace();
        }
        return new ActionRouter("inv-stock-movement-page");
    }

    private void fnGetStockDelta(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");
        String itemCode = req.getParameter("itemCode");
        Timestamp txnTimeAfterOrEqual = TimeFormat.createTimestamp(fromDate);
        Timestamp txnTimeBefore = TimeFormat.createTimestamp(toDate);
        ItemObject itmObj = null;
        itemCode = itemCode.trim();
        try {
            Item itmEJB = ItemNut.getObjectByCode(itemCode);
            itmObj = itmEJB.getObject();
        } catch (Exception ex) {
            req.setAttribute("errMsg", " Invalid Item Code!! ");
            return;
        }
        String[] location = (String[]) req.getParameterValues("location");
        if (location == null) {
            throw new Exception("Please select Location!");
        }
        String csvString = "";
        if (location != null) {
            for (int cnt1 = 0; cnt1 < location.length; cnt1++) {
                if (cnt1 > 0) {
                    csvString += ",";
                }
                csvString += location[cnt1];
            }
        }
        BigDecimal bdPrevBal = StockDeltaNut.getNetMovementMultiLocation(itmObj.pkid, txnTimeAfterOrEqual, csvString);
        Vector vecStkDelta = StockDeltaNut.getStockMovementReport(itmObj.pkid, txnTimeAfterOrEqual, txnTimeBefore, csvString);
        if (bdPrevBal == null) {
            bdPrevBal = new BigDecimal("0.00");
        }
        TreeMap tmp = new TreeMap();
        String orderBy = req.getParameter("orderBy");
        for (int i = 0; i < vecStkDelta.size(); i++) {
            StockDeltaObject stkDeltaObj = (StockDeltaObject) vecStkDelta.get(i);
            if (orderBy.equals("creationDate")) {
                tmp.put(stkDeltaObj.timeEdit, stkDeltaObj);
            } else {
                tmp.put(stkDeltaObj.txnTime, stkDeltaObj);
            }
        }
        vecStkDelta = new Vector(tmp.values());
        req.setAttribute("location", csvString);
        req.setAttribute("bdPrevBal", bdPrevBal);
        req.setAttribute("itmObj", itmObj);
        req.setAttribute("vecStkDelta", vecStkDelta);
    }

    private void fnPopulateSearchForm(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        Vector vecLocation = LocationNut.getValueObjectsGiven(LocationBean.STATUS, LocationBean.STATUS_ACTIVE, (String) null, (String) null);
        req.setAttribute("vecLocation", vecLocation);
        String fromDate = req.getParameter("fromDate");
        req.setAttribute("fromDate", fromDate);
        String toDate = req.getParameter("toDate");
        req.setAttribute("toDate", toDate);
        String itemCode = req.getParameter("itemCode");
        if (itemCode != null) {
            req.setAttribute("itemCode", itemCode.trim());
        }
        String locationId = req.getParameter("locationId");
        req.setAttribute("locationId", locationId);
    }

    private void fnPrintGetStockDelta(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String fromDate = req.getParameter("fromDate");
        String toDate = req.getParameter("toDate");
        String itemCode = req.getParameter("itemCode");
        String locationId = req.getParameter("locationId");
        Timestamp txnTimeAfterOrEqual = TimeFormat.createTimestamp(fromDate);
        Timestamp txnTimeBefore = TimeFormat.createTimestamp(toDate);
        txnTimeBefore = TimeFormat.add(txnTimeBefore, 0, 0, 1);
        ItemObject itmObj = null;
        itemCode = itemCode.trim();
        try {
            Item itmEJB = ItemNut.getObjectByCode(itemCode);
            itmObj = itmEJB.getObject();
        } catch (Exception ex) {
            req.setAttribute("errMsg", " Invalid Item Code!! ");
            return;
        }
        Integer iLoc = new Integer(locationId);
        Stock stkEJB = StockNut.getObjectBy(itmObj.pkid, iLoc, new Integer(StockNut.STK_COND_GOOD), "");
        StockObject stkObj = null;
        try {
            stkObj = stkEJB.getObject();
        } catch (Exception ex) {
            req.setAttribute("errMsg", " The is no such stock at this location. ");
            return;
        }
        Vector vecStkDelta = StockDeltaNut.getValueObjectsGiven((String) null, (String) null, (String) null, (String) null, (Integer) null, (Integer) null, stkObj.pkid, (Integer) null, itmObj.pkid, (String) null, (String) null, txnTimeAfterOrEqual, txnTimeBefore, (String) null, (Integer) null, (String) null, (Long) null, (String) null, StockDeltaBean.STATUS_ACTIVE);
        BigDecimal bdPrevBal = StockDeltaNut.getNetMovement(stkObj.pkid, (Timestamp) null, txnTimeAfterOrEqual, StockDeltaBean.STATUS, StockDeltaBean.STATUS_ACTIVE, (String) null, (String) null, (String) null, (String) null, (String) null);
        if (bdPrevBal == null) {
            bdPrevBal = new BigDecimal("0.00");
        }
        req.setAttribute("bdPrevBal", bdPrevBal);
        req.setAttribute("itmObj", itmObj);
        req.setAttribute("vecStkDelta", vecStkDelta);
    }
}
