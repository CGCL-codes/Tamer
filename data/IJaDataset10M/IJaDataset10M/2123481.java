package com.vlee.servlet.accounting;

import com.vlee.servlet.main.*;
import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.accounting.*;

public class DoGLMaintenance implements Action {

    private String strClassName = "DoGeneralLedger: ";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = req.getParameter("formName");
        fnPreserveParams(servlet, req, res);
        if (formName == null) {
            return new ActionRouter("acc-gl-maintenance-page");
        }
        if (formName.equals("createGL")) {
            fnCreateGeneralLedger(servlet, req, res);
        }
        return new ActionRouter("acc-gl-maintenance-page");
    }

    private void fnCreateGeneralLedger(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String glCode = req.getParameter("glCode");
        String batch = req.getParameter("batchId");
        String pcCenter = req.getParameter("pcCenter");
        String currency = req.getParameter("currency");
        Integer batchId = new Integer(batch);
        Integer iPCC = new Integer(pcCenter);
        GeneralLedgerNut.getObjectFailSafe(glCode, batchId, iPCC, currency);
    }

    private void fnPreserveParams(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        String glCodePkid = req.getParameter("glCodePkid");
        req.setAttribute("glCodePkid", glCodePkid);
        String glCode = req.getParameter("glCode");
        req.setAttribute("glCode", glCode);
        String batchId = req.getParameter("batchId");
        req.setAttribute("batchId", batchId);
    }
}
