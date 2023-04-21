package com.vlee.servlet.customer;

import java.math.*;
import java.sql.*;
import javax.servlet.http.*;
import java.util.*;
import com.vlee.bean.customer.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.accounting.*;
import com.vlee.servlet.main.*;
import com.vlee.util.*;

public class DoOpenCreditReceiptListing implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter("cust-popup-open-credit-receipt-listing-page");
        }
        if (formName.equals("getListing")) {
            fnGetListing(servlet, req, res);
        }
        return new ActionRouter("cust-popup-open-credit-receipt-listing-page");
    }

    private void fnGetListing(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        QueryObject query = new QueryObject(new String[] { OfficialReceiptBean.OPEN_BALANCE + " > '0' ", OfficialReceiptBean.STATE + " ='" + OfficialReceiptBean.ST_CREATED + "' " });
        query.setOrder(" ORDER BY " + OfficialReceiptBean.PAYMENT_TIME + ", " + OfficialReceiptBean.ENTITY_NAME);
        Vector vecReceipt = new Vector(OfficialReceiptNut.getObjects(query));
        req.setAttribute("vecReceipt", vecReceipt);
    }
}
