package com.vlee.servlet.finance;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.math.*;
import com.vlee.ejb.accounting.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;
import com.vlee.bean.finance.*;

public class DoCashTransfer extends ActionDo implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter("finance-cash-transfer-page");
        }
        if (formName.equals("setPCCenter")) {
            fnSetPCCenter(servlet, req, res);
        }
        if (formName.equals("setCashbookFrom")) {
            try {
                fnSetCashbookFrom(servlet, req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
        }
        if (formName.equals("setCashbookTo")) {
            try {
                fnSetCashbookTo(servlet, req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
        }
        if (formName.equals("setDetails")) {
            fnSetDetails(servlet, req, res);
        }
        if (formName.equals("save")) {
            String notification = fnSaveTransfer(servlet, req, res);
            req.setAttribute("successNotify", notification);
        }
        return new ActionRouter("finance-cash-transfer-page");
    }

    private String fnSaveTransfer(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        CashTransferSession ctfr = (CashTransferSession) session.getAttribute("finance-cash-transfer-session");
        try {
            ctfr.saveCashTransfer();
            return "success";
        } catch (Exception ex) {
            return "fail";
        }
    }

    private void fnSetCashbookFrom(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        CashTransferSession ctfr = (CashTransferSession) session.getAttribute("finance-cash-transfer-session");
        Integer cashbook = new Integer(req.getParameter("cashbookFrom"));
        ctfr.setCashbookFrom(cashbook);
    }

    private void fnSetCashbookTo(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        CashTransferSession ctfr = (CashTransferSession) session.getAttribute("finance-cash-transfer-session");
        Integer cashbook = new Integer(req.getParameter("cashbookTo"));
        try {
            ctfr.setCashbookTo(cashbook);
        } catch (Exception ex) {
            throw ex;
        }
    }

    private void fnSetPCCenter(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        CashTransferSession ctfr = (CashTransferSession) session.getAttribute("finance-cash-transfer-session");
        Integer pcCenter = new Integer(req.getParameter("pcCenter"));
        ctfr.setPCCenter(pcCenter);
    }

    private void fnSetDetails(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        CashTransferSession ctfr = (CashTransferSession) session.getAttribute("finance-cash-transfer-session");
        String remarks = req.getParameter("remarks");
        BigDecimal amount = new BigDecimal(0);
        try {
            amount = new BigDecimal(req.getParameter("amount"));
        } catch (Exception ex) {
        }
        String chequeNo = req.getParameter("chequeNo");
        Timestamp chequeDate = TimeFormat.getTimestamp();
        Timestamp txnDate = TimeFormat.getTimestamp();
        try {
            chequeDate = TimeFormat.createTimestamp(req.getParameter("chequeDate"));
            txnDate = TimeFormat.createTimestamp(req.getParameter("txnDate"));
        } catch (Exception ex) {
        }
        ctfr.setRemarks(remarks);
        ctfr.setAmount(amount);
        ctfr.setChequeNo(chequeNo);
        ctfr.setChequeDate(chequeDate);
        ctfr.setTxnDate(txnDate);
    }
}
