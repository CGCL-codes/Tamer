package com.vlee.servlet.pos;

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

public class DoDebitCreditNoteReport implements Action {

    private String strClassName = "DoDebitCreditNoteReport";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = (String) req.getParameter("formName");
        String fwdPage = (String) req.getParameter("fwdPage");
        if (fwdPage == null) {
            fwdPage = "pos-debit-credit-note-report-page";
        }
        String strSvcCenter = req.getParameter("svcCenter");
        req.setAttribute("svcCenter", strSvcCenter);
        String strDateFrom = req.getParameter("dateFrom");
        req.setAttribute("dateFrom", strDateFrom);
        String strDateTo = req.getParameter("dateTo");
        req.setAttribute("dateTo", strDateTo);
        String strCustPkid = req.getParameter("custPkid");
        req.setAttribute("custPkid", strCustPkid);
        String strNoteType = req.getParameter("noteType");
        req.setAttribute("noteType", strNoteType);
        String strGLCode = req.getParameter("glCode");
        req.setAttribute("glCode", strGLCode);
        fnGetFormObjects(servlet, req, res);
        if (formName == null) {
            return new ActionRouter(fwdPage);
        }
        if (formName.equals("getDebitCreditNoteReport")) {
            String strErrMsg = null;
            strErrMsg = fnFetchResults(servlet, req, res);
            if (strErrMsg != null) {
                req.setAttribute("strErrMsg", strErrMsg);
            }
            return new ActionRouter(fwdPage);
        }
        return new ActionRouter(fwdPage);
    }

    protected String fnFetchResults(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        Integer pcCenter = null;
        String entityTable = null;
        Integer entityId = null;
        Timestamp dateFrom = null;
        Timestamp dateTo = null;
        String stmtType = null;
        String currency = null;
        BigDecimal amtMoreThan = null;
        BigDecimal amtLessThan = null;
        String txnType = null;
        String txnCode = null;
        String plFlag = null;
        String glCodeDebit = null;
        String glCodeCredit = null;
        String referenceNo = null;
        String chequeCreditCardNo = null;
        String description = null;
        String remarks = null;
        String info1 = null;
        String info2 = null;
        String state = null;
        String status = null;
        String field1 = null;
        String value1 = null;
        String strErrMsg = null;
        String strSvcCenter = req.getParameter("svcCenter");
        String strDateFrom = req.getParameter("dateFrom");
        String strDateTo = req.getParameter("dateTo");
        String strCustPkid = req.getParameter("custPkid");
        String strNoteType = req.getParameter("noteType");
        String strGLCode = req.getParameter("glCode");
        Integer svcCenter = null;
        Integer custPkid = null;
        String noteType = null;
        String glCode = null;
        try {
            svcCenter = new Integer(strSvcCenter);
        } catch (Exception ex) {
        }
        CustServiceCenterObject cscObj = null;
        if (svcCenter != null) {
            cscObj = CustServiceCenterNut.getObject(svcCenter);
            if (cscObj != null) {
                pcCenter = cscObj.accPCCenterId;
            }
        }
        try {
            dateFrom = TimeFormat.createTimestamp(strDateFrom);
        } catch (Exception ex) {
            strErrMsg = " Invalid dateFrom ";
            return strErrMsg;
        }
        try {
            dateTo = TimeFormat.createTimestamp(strDateTo);
            dateTo = TimeFormat.add(dateTo, 0, 0, 1);
        } catch (Exception ex) {
            strErrMsg = " Invalid dateTo ";
            return strErrMsg;
        }
        try {
            entityId = new Integer(strCustPkid);
        } catch (Exception ex) {
        }
        if (strNoteType != null && strNoteType.length() > 1 && !strNoteType.equals("all")) {
            stmtType = strNoteType;
        }
        String namespace = GenericStmtBean.NS_CUSTOMER;
        entityTable = CustAccountBean.TABLENAME;
        Vector vecGS = GenericStmtNut.fnAdvancedSearch(namespace, pcCenter, entityTable, entityId, dateFrom, dateTo, stmtType, currency, amtMoreThan, amtLessThan, txnType, txnCode, plFlag, glCodeDebit, glCodeCredit, referenceNo, chequeCreditCardNo, description, remarks, info1, info2, state, status, field1, value1);
        Log.printVerbose("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Log.printVerbose(" size of vecGS = " + vecGS.size());
        req.setAttribute("vecGS", vecGS);
        return strErrMsg;
    }

    protected String fnGetFormObjects(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String strErrMsg = null;
        Vector vecSvcCtr = CustServiceCenterNut.getValueObjectsGiven(CustServiceCenterBean.STATUS, CustServiceCenterBean.STATUS_ACTIVE, (String) null, (String) null);
        req.setAttribute("vecSvcCtr", vecSvcCtr);
        return strErrMsg;
    }
}
