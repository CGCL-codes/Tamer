package com.vlee.servlet.test;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import java.math.BigDecimal;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.inventory.*;
import com.vlee.ejb.customer.*;
import com.vlee.ejb.accounting.*;

public class DoTopConFixDuplicateDocLink implements Action {

    Connection con = null;

    Connection jbossCon = null;

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter("test-topcon-fix-duplicate-doclink-page");
        }
        if (formName.equals("removeDuplicate")) {
            try {
                fnRemoveDuplicate(servlet, req, res);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return new ActionRouter("test-topcon-fix-duplicate-doclink-page");
    }

    private synchronized void fnRemoveDuplicate(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String url = "jdbc:postgresql://localhost:5432/topconWsemp";
        Connection con = DriverManager.getConnection(url, "jboss", "jboss");
        String theQuery = " select  src_docid,tgt_docid,count(pkid) from acc_doclink group by ";
        theQuery += " src_docid,tgt_docid,amount,src_docref,tgt_docref having count(pkid) = 2; ";
        Statement theStmt = con.createStatement();
        ResultSet theResult = theStmt.executeQuery(theQuery);
        int counter = 0;
        while (theResult.next()) {
            counter++;
            Long lSrcDocId = new Long(theResult.getLong("src_docid"));
            Long lTgtDocId = new Long(theResult.getLong("tgt_docid"));
            Log.printVerbose("-------------------------------------");
            Log.printVerbose(" COUNTER = " + counter);
            Log.printVerbose("-------------------------------------");
            Log.printVerbose(" SRC DOCID:" + lSrcDocId.toString());
            Log.printVerbose(" TGT DOCID:" + lTgtDocId.toString());
            QueryObject query = new QueryObject(new String[] { DocLinkBean.SRC_DOCID + " ='" + lSrcDocId.toString() + "' ", DocLinkBean.TGT_DOCID + " ='" + lTgtDocId.toString() + "' ", DocLinkBean.SRC_DOCREF + " ='" + OfficialReceiptBean.TABLENAME + "' ", DocLinkBean.TGT_DOCREF + " ='" + InvoiceBean.TABLENAME + "' " });
            query.setOrder(" ORDER BY " + DocLinkBean.PKID);
            Vector vecDocLink = new Vector(DocLinkNut.getObjects(query));
            for (int cnt1 = 0; cnt1 < vecDocLink.size(); cnt1++) {
                if (cnt1 == 0) {
                    continue;
                }
                DocLinkObject dlObj = (DocLinkObject) vecDocLink.get(cnt1);
                DocLink dlEJB = DocLinkNut.getHandle(dlObj.pkid);
                Invoice invoiceEJB = InvoiceNut.getHandle(lTgtDocId);
                if (invoiceEJB != null) {
                    Log.printVerbose(" Updating Outstanding Balance of Invoice " + lTgtDocId.toString());
                    try {
                        invoiceEJB.adjustOutstanding(dlObj.amount.negate());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                Log.printVerbose(" Removing the DOCLINK..... " + dlObj.pkid.toString());
                try {
                    dlEJB.remove();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            query = new QueryObject(new String[] { DocLinkBean.SRC_DOCID + " ='" + lSrcDocId.toString() + "' ", DocLinkBean.TGT_DOCID + " ='" + lTgtDocId.toString() + "' ", DocLinkBean.SRC_DOCREF + " ='" + GenericStmtBean.TABLENAME + "' ", DocLinkBean.TGT_DOCREF + " ='" + InvoiceBean.TABLENAME + "' " });
            query.setOrder(" ORDER BY " + DocLinkBean.PKID);
            vecDocLink = new Vector(DocLinkNut.getObjects(query));
            for (int cnt1 = 0; cnt1 < vecDocLink.size(); cnt1++) {
                if (cnt1 == 0) {
                    continue;
                }
                DocLinkObject dlObj = (DocLinkObject) vecDocLink.get(cnt1);
                DocLink dlEJB = DocLinkNut.getHandle(dlObj.pkid);
                Invoice invoiceEJB = InvoiceNut.getHandle(lTgtDocId);
                if (invoiceEJB != null) {
                    Log.printVerbose(" Updating Outstanding Balance of Invoice " + lTgtDocId.toString());
                    try {
                        invoiceEJB.adjustOutstanding(dlObj.amount.negate());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                Log.printVerbose(" Removing the DOCLINK..... " + dlObj.pkid.toString());
                try {
                    dlEJB.remove();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
