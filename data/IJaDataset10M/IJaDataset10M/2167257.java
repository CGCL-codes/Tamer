package com.vlee.servlet.management;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.accounting.*;

public class DoMgtDailySalesReportType01 implements Action {

    String strClassName = "DoMgtDailySalesReportType01";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = (String) req.getParameter("formName");
        String fwdPage = (String) req.getParameter("fwdPage");
        Vector vecPCCenter = ProfitCostCenterNut.getValueObjectsGiven(ProfitCostCenterBean.STATUS, ProfitCostCenterBean.STATUS_ACTIVE, (String) null, (String) null);
        req.setAttribute("vecPCCenter", vecPCCenter);
        fnGetReportType01Values(servlet, req, res);
        if (fwdPage != null) {
            return new ActionRouter(fwdPage);
        }
        return new ActionRouter("mgt-daily-sales-report-type01-page");
    }

    public void fnGetReportType01Values(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        try {
            Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 1");
            String strPCCenter = req.getParameter("pcCenter");
            req.setAttribute("strPCCenter", strPCCenter);
            String currency = req.getParameter("currency");
            req.setAttribute("currency", currency);
            String strTheDate = req.getParameter("theDate");
            req.setAttribute("theDate", strTheDate);
            if (strPCCenter == null || currency == null || strTheDate == null) {
                return;
            }
            Integer pcCenter = null;
            try {
                pcCenter = new Integer(strPCCenter);
            } catch (Exception ex) {
            }
            String naForeignTable = NominalAccountBean.FT_CUSTOMER;
            Integer naForeignKey = null;
            Long natForeignKey = null;
            String natForeignTable = NominalAccountTxnBean.FT_CUST_INVOICE;
            if (strTheDate == null) {
                strTheDate = TimeFormat.strDisplayDate();
            }
            Timestamp tsFrom = TimeFormat.createTimestamp(strTheDate);
            Timestamp tsTo = TimeFormat.add(tsFrom, 0, 0, 1);
            Log.printVerbose("tsFrom = " + tsFrom.toString());
            Log.printVerbose("tsTo = " + tsTo.toString());
            String strOption = "active";
            Log.printVerbose(" before calling na nut to retrieve na txns !!");
            Vector vecNaInv = NominalAccountNut.getValueObjectsGiven(pcCenter, naForeignTable, naForeignKey, currency, natForeignTable, natForeignKey, tsFrom, tsTo, strOption);
            Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 2");
            natForeignTable = NominalAccountTxnBean.FT_CUST_RECEIPT;
            Vector vecNaRec = NominalAccountNut.getValueObjectsGiven(pcCenter, naForeignTable, naForeignKey, currency, natForeignTable, natForeignKey, tsFrom, tsTo, strOption);
            vecNaInv.addAll(vecNaRec);
            Vector vecMerged = NominalAccountNut.fnMergeNominalAccountWithSameID(vecNaInv);
            Vector vecInvRec = new Vector();
            Vector vecRecOnly = new Vector();
            Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 3");
            for (int count1 = 0; count1 < vecMerged.size(); count1++) {
                NominalAccountObject naObj = (NominalAccountObject) vecMerged.get(count1);
                BigDecimal bdTotalInvoice = new BigDecimal("0.00");
                BigDecimal bdTotalReceipt = new BigDecimal("0.00");
                Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 4");
                for (int count2 = 0; count2 < naObj.vecNominalAccountTxn.size(); count2++) {
                    NominalAccountTxnObject natObj = (NominalAccountTxnObject) naObj.vecNominalAccountTxn.get(count2);
                    if (natObj.foreignTable.equals(NominalAccountTxnBean.FT_CUST_INVOICE)) {
                        Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 1");
                        bdTotalInvoice = bdTotalInvoice.add(natObj.amount);
                    } else if (natObj.foreignTable.equals(NominalAccountTxnBean.FT_CUST_RECEIPT)) {
                        Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 1");
                        bdTotalReceipt = bdTotalReceipt.add(natObj.amount);
                    }
                }
                if (bdTotalInvoice.signum() == 0) {
                    vecRecOnly.add(naObj);
                    Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 1");
                } else {
                    vecInvRec.add(naObj);
                    Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 1");
                }
            }
            Vector vecInvRecExternal = new Vector();
            Vector vecInvRecInternal = new Vector();
            for (int count1 = 0; count1 < vecInvRec.size(); count1++) {
                NominalAccountObject naObj = (NominalAccountObject) vecInvRec.get(count1);
                if (naObj.foreignTable.equals(NominalAccountBean.FT_CUSTOMER)) {
                    try {
                        CustAccount caEJB = CustAccountNut.getHandle(naObj.foreignKey);
                        Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 1");
                        Integer iType = caEJB.getAccType();
                        if (iType.intValue() == CustAccountBean.ACCTYPE_CORPORATE.intValue()) {
                            vecInvRecInternal.add(naObj);
                        } else {
                            vecInvRecExternal.add(naObj);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            Vector vecRecOnlyExternal = new Vector();
            Vector vecRecOnlyInternal = new Vector();
            for (int count1 = 0; count1 < vecRecOnly.size(); count1++) {
                NominalAccountObject naObj = (NominalAccountObject) vecRecOnly.get(count1);
                if (naObj.foreignTable.equals(NominalAccountBean.FT_CUSTOMER)) {
                    Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxx 1");
                    try {
                        CustAccount caEJB = CustAccountNut.getHandle(naObj.foreignKey);
                        Integer iType = caEJB.getAccType();
                        if (iType.intValue() == CustAccountBean.ACCTYPE_CORPORATE.intValue()) {
                            vecRecOnlyInternal.add(naObj);
                        } else {
                            vecRecOnlyExternal.add(naObj);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
            Log.printVerbose(" after calling na nut to retrieve na txns !!");
            req.setAttribute("vecInvRecInternal", vecInvRecInternal);
            req.setAttribute("vecInvRecExternal", vecInvRecExternal);
            req.setAttribute("vecRecOnlyExternal", vecRecOnlyExternal);
            req.setAttribute("vecRecOnlyInternal", vecRecOnlyInternal);
            fnAuditTrail(servlet, req, res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void fnAuditTrail(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(true);
        Integer iUserId = (Integer) session.getAttribute("userId");
        if (iUserId != null) {
            AuditTrailObject atObj = new AuditTrailObject();
            atObj.userId = iUserId;
            atObj.auditType = AuditTrailBean.TYPE_USAGE;
            atObj.remarks = "mgt_rpt: daily-sales-report-t01";
            AuditTrailNut.fnCreate(atObj);
        }
    }
}
