package com.vlee.servlet.supplier;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.supplier.*;
import com.vlee.bean.supplier.*;

public class DoSuppDetailedPurchaseRecords implements Action {

    private String strClassName = "DoSuppDetailedPurchaseRecords";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = (String) req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter("supp-detailed-purchase-records-page");
        }
        if (formName.equals("popupPrint")) {
            return new ActionRouter("printable-supp-detailed-purchase-records-page");
        }
        if (formName.equals("generateReport")) {
            fnGenerateReport(servlet, req, res);
        }
        if (formName.equals("getReport")) {
            fnGetReport(servlet, req, res);
        }
        if (formName.equals("setSupplier")) {
            try {
                setSupplier(servlet, req, res);
                return new ActionRouter("supp-detailed-purchase-records-page");
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
                return new ActionRouter("supp-detailed-purchase-records-page");
            }
        }
        return new ActionRouter("supp-detailed-purchase-records-page");
    }

    private void fnGetReport(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        PurchaseRecordsBySupplierReport prbsr = (PurchaseRecordsBySupplierReport) session.getAttribute("purchase-records-by-supplier-report");
        String codeStart = req.getParameter("codeStart");
        String codeEnd = req.getParameter("codeEnd");
        String sortBy = req.getParameter("sortBy");
        boolean useCode = false;
        String useCodeRange = req.getParameter("useCodeRange");
        if (useCodeRange != null && useCodeRange.equals("true")) {
            useCode = true;
        }
        prbsr.setCodeRange(useCode, codeStart, codeEnd);
        Timestamp dateFrom = TimeFormat.createTimestamp(req.getParameter("dateFrom"));
        Timestamp dateTo = TimeFormat.createTimestamp(req.getParameter("dateTo"));
        prbsr.setSort(sortBy);
        prbsr.setDateRange(dateFrom, dateTo);
        prbsr.generateReport("testing");
    }

    private void fnGenerateReport(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        PurchaseRecordsBySupplierReport prbsr2 = (PurchaseRecordsBySupplierReport) session.getAttribute("purchase-records-by-supplier-report");
        prbsr2.generateReport("testing");
    }

    public void setSupplier(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        try {
            HttpSession session = req.getSession();
            PurchaseRecordsBySupplierReport prbsr3 = (PurchaseRecordsBySupplierReport) session.getAttribute("purchase-records-by-supplier-report");
            if (prbsr3 == null) {
                prbsr3 = new PurchaseRecordsBySupplierReport();
                session.setAttribute("purchase-records-by-supplier-report", prbsr3);
            }
            String strSuppAcc = (String) req.getParameter("suppPkid");
            Integer iSuppAcc = new Integer(strSuppAcc);
            SuppAccountObject suppAccObj = SuppAccountNut.getObject(iSuppAcc);
            if (suppAccObj == null) {
                throw new Exception(" No such customer!! ");
            }
            if (!prbsr3.setSupplier(iSuppAcc)) {
                throw new Exception("Invalid Supplier Account");
            }
        } catch (Exception ex) {
            throw new Exception("Invalid Supplier PKID: " + ex.getMessage());
        }
    }
}
