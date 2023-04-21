package com.vlee.servlet.trading;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import javax.sql.*;
import java.math.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.inventory.*;
import com.vlee.bean.inventory.*;

public class DoStockRequisitionPrint implements Action {

    String strClassName = "DoStockRequisitionPrint";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        String sReqPkid = req.getParameter("pkid");
        String print = (String) req.getParameter("print");
        System.out.println("sReqPkid : " + sReqPkid);
        StockRequisitionObject sReqObj = null;
        if (sReqPkid != null) {
            req.setAttribute("print", print);
            fnGetStockRequisition(servlet, req, res);
        }
        return new ActionRouter("trading-pos-stock-requisition-print-page");
    }

    private void fnGetStockRequisition(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String sReqPkid = req.getParameter("pkid");
        try {
            Long pkid = new Long(sReqPkid);
            StockRequisitionObject stockRequisition = StockRequisitionNut.getObject(pkid);
            req.setAttribute("stockRequisition", stockRequisition);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
