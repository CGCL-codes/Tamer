package com.vlee.servlet.officeadmin;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.sql.*;
import java.util.*;
import java.math.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.accounting.*;

public class DoEnterReceivedInvoices implements Action {

    String strClassName = "DoEnterReceivedInvoices";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        String fwdPage = req.getParameter("fwdPage");
        if (fwdPage == null) {
            fwdPage = "ofad-enter-received-invoices-page";
        }
        if (formName == null) {
            fnGetObjectsForForm(servlet, req, res);
            return new ActionRouter(fwdPage);
        }
        if (formName.equals("enterReceivedInvoices")) {
            String strErrMsg = fnCreateStmt(servlet, req, res);
            req.setAttribute("strErrMsg", strErrMsg);
            if (strErrMsg != null) {
                return new ActionRouter("ofad-enter-received-invoices-page");
            } else {
                return new ActionRouter("ofad-enter-received-invoices-02-page");
            }
        }
        Log.printVerbose(strClassName + ": returning default ActionRouter");
        return new ActionRouter(fwdPage);
    }

    protected String fnCreateStmt(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String strErrMsg = null;
        String strEntityAccountId = (String) req.getParameter("entityAccountId");
        String strPCCenter = (String) req.getParameter("pcCenter");
        String strStmtType = (String) req.getParameter("stmtType");
        String strRefNum = (String) req.getParameter("refNum");
        String strGLCode = (String) req.getParameter("glCode");
        String strGLCodeText = (String) req.getParameter("glCodeText");
        String currency = (String) req.getParameter("currency");
        String strStmtAmt = (String) req.getParameter("stmtAmount");
        String strDateStmt = (String) req.getParameter("dateStmt");
        String strEffFromDate = (String) req.getParameter("effFromDate");
        String strEffToDate = (String) req.getParameter("effToDate");
        String strTsCreate = (String) req.getParameter("tsCreate");
        String strCreditType = (String) req.getParameter("creditType");
        String strDateDue = (String) req.getParameter("dateDue");
        String strCreditDays = (String) req.getParameter("creditDays");
        String strRemarks = (String) req.getParameter("remarks");
        String userName = (String) req.getParameter("userName");
        Integer entityAccId = null;
        try {
            entityAccId = new Integer(strEntityAccountId);
        } catch (Exception ex) {
            strErrMsg = " Invalid integer for entity account id ";
            return strErrMsg;
        }
        if (strGLCodeText != null && strGLCodeText.length() > 0) {
            GLCode glcodeEJB = GLCodeNut.getObjectByCode(strGLCodeText);
            if (glcodeEJB == null) {
                strErrMsg = " Please enter a valid GL Code ";
                return strErrMsg;
            }
            strGLCode = strGLCodeText;
        } else if (strGLCode.length() < 1) {
            strErrMsg = " Please select a valid GL Code ";
            return strErrMsg;
        }
        GenericEntityAccountObject geaObj = GenericEntityAccountNut.getObject(entityAccId);
        if (geaObj == null) {
            strErrMsg = " Invalid Generic Entity Account ";
            return strErrMsg;
        }
        GenericStmtObject gsObj = new GenericStmtObject();
        gsObj.namespace = GenericStmtBean.NS_OFFICEADMIN;
        gsObj.dateStmt = TimeFormat.createTimestamp(strDateStmt);
        gsObj.glCodeDebit = strGLCode;
        gsObj.glCodeCredit = geaObj.entityType;
        try {
            if (strCreditType.equals("days")) {
                Integer idays = new Integer(strCreditDays);
                gsObj.dateDue = TimeFormat.add(gsObj.dateStmt, 0, 0, idays.intValue());
            } else {
                gsObj.dateDue = TimeFormat.createTimeStamp(strDateDue);
            }
        } catch (Exception ex) {
            strErrMsg = " Error creating due date ";
            return strErrMsg;
        }
        gsObj.foreignEntityTable = GenericEntityAccountBean.TABLENAME;
        gsObj.foreignEntityKey = entityAccId;
        gsObj.pcCenter = new Integer(strPCCenter);
        gsObj.stmtType = strStmtType;
        gsObj.referenceNo = strRefNum;
        gsObj.currency = currency;
        gsObj.remarks = strRemarks;
        try {
            gsObj.amount = new BigDecimal(strStmtAmt);
            BigDecimal zero = new BigDecimal("0.00");
            if (gsObj.amount.compareTo(zero) == 0) {
                strErrMsg = " The amount must be non-zero! ";
                return strErrMsg;
            }
        } catch (Exception ex) {
            strErrMsg = " Error creating the amount ";
            return strErrMsg;
        }
        try {
            gsObj.dateStmt = TimeFormat.createTimestamp(strDateStmt);
            gsObj.dateStart = TimeFormat.createTimestamp(strEffFromDate);
            gsObj.dateEnd = TimeFormat.createTimestamp(strEffToDate);
            gsObj.dateCreated = TimeFormat.createTimestamp(strTsCreate);
            gsObj.dateApproved = TimeFormat.createTimestamp(strDateStmt);
            gsObj.dateVerified = TimeFormat.createTimestamp(strDateStmt);
            gsObj.dateUpdate = TimeFormat.createTimestamp(strTsCreate);
        } catch (Exception ex) {
            strErrMsg = " Error creating dates ";
            return strErrMsg;
        }
        try {
            gsObj.userIdCreate = UserNut.getUserId(userName);
        } catch (Exception ex) {
            strErrMsg = " Error fetching userId ";
            return strErrMsg;
        }
        NominalAccountObject naObj = NominalAccountNut.getObject(GenericEntityAccountBean.TABLENAME, gsObj.pcCenter, gsObj.foreignEntityKey, gsObj.currency);
        if (naObj == null) {
            naObj = new NominalAccountObject();
            naObj.namespace = NominalAccountBean.NS_GENERAL;
            naObj.foreignTable = gsObj.foreignEntityTable;
            naObj.foreignKey = gsObj.foreignEntityKey;
            naObj.accountType = geaObj.entityType;
            naObj.currency = gsObj.currency;
            naObj.amount = new BigDecimal("0.00");
            naObj.remarks = " ";
            naObj.accPCCenterId = gsObj.pcCenter;
            naObj.lastUpdate = TimeFormat.getTimestamp();
            naObj.userIdUpdate = gsObj.userIdCreate;
            NominalAccount naEJB = NominalAccountNut.fnCreate(naObj);
            gsObj.nominalAccount = naObj.pkid;
            if (naEJB == null) {
                strErrMsg = " Unable to create nominal account ";
                return strErrMsg;
            }
        }
        NominalAccountTxnObject natObj = new NominalAccountTxnObject();
        natObj.nominalAccount = naObj.pkid;
        natObj.foreignTable = GenericStmtBean.TABLENAME;
        natObj.description = gsObj.remarks;
        natObj.glCodeDebit = gsObj.glCodeDebit;
        natObj.glCodeCredit = gsObj.glCodeCredit;
        natObj.currency = gsObj.currency;
        natObj.amount = gsObj.amount.negate();
        natObj.timeOption1 = NominalAccountTxnBean.TIME_STMT;
        natObj.timeParam1 = gsObj.dateStmt;
        natObj.state = NominalAccountTxnBean.ST_ACTUAL;
        natObj.status = NominalAccountTxnBean.STATUS_ACTIVE;
        natObj.lastUpdate = TimeFormat.getTimestamp();
        natObj.userIdUpdate = gsObj.userIdCreate;
        GenericStmt gsEJB = GenericStmtNut.fnCreate(gsObj);
        if (gsEJB == null) {
            strErrMsg = " Unable to create Generic Stmt Bean ";
            return strErrMsg;
        }
        natObj.foreignKey = gsObj.pkid;
        NominalAccountTxn natEJB = NominalAccountTxnNut.fnCreate(natObj);
        if (natEJB == null) {
            try {
                gsEJB.remove();
            } catch (Exception ex) {
                ex.printStackTrace();
                strErrMsg = " Unable to create Nominal Account Txn ";
                return strErrMsg;
            }
        }
        naObj.amount = naObj.amount.add(natObj.amount);
        req.setAttribute("geaObj", geaObj);
        req.setAttribute("gsObj", gsObj);
        req.setAttribute("naObj", naObj);
        req.setAttribute("natObj", natObj);
        return strErrMsg;
    }

    protected void fnGetObjectsForForm(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        Vector vecPCCenter = ProfitCostCenterNut.getValueObjectsGiven(ProfitCostCenterBean.STATUS, ProfitCostCenterBean.STATUS_ACTIVE, (String) null, (String) null);
        Vector vecGLCatTree = GLCategoryNut.getValueObjectsTree((String) null, (String) null, (String) null, (String) null, (String) null, (String) null);
        req.setAttribute("vecPCCenter", vecPCCenter);
        req.setAttribute("vecGLCatTree", vecGLCatTree);
    }
}
