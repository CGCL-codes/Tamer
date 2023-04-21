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
import com.vlee.ejb.accounting.*;

public class DoMonitorOutstandingSuppAcc implements Action {

    private String strClassName = "DoMonitorOutstandingSuppAcc";

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String formName = (String) req.getParameter("formName");
        String fwdPage = req.getParameter("fwdPage");
        if (fwdPage == null) {
            fwdPage = "supp-monitor-outstanding-supp-acc-page";
        }
        Vector vecSuppProcCtr = SuppProcurementCenterNut.getValueObjectsGiven(SuppProcurementCenterBean.STATUS, SuppProcurementCenterBean.STATUS_ACTIVE, (String) null, (String) null);
        req.setAttribute("vecSuppProcCtr", vecSuppProcCtr);
        String strInternal = req.getParameter("strInternal");
        String strExternal = req.getParameter("strExternal");
        req.setAttribute("strInternal", strInternal);
        req.setAttribute("strExternal", strExternal);
        String suppProcCtr = req.getParameter("suppProcCtr");
        req.setAttribute("suppProcCtr", suppProcCtr);
        if (formName == null) {
            return new ActionRouter(fwdPage);
        }
        if (formName.equals("findNominalAcc")) {
            fnFindNominalAcc(servlet, req, res);
            return new ActionRouter(fwdPage);
        }
        return new ActionRouter(fwdPage);
    }

    protected void fnFindNominalAcc(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        try {
            String strInternal = req.getParameter("strInternal");
            String strExternal = req.getParameter("strExternal");
            String suppProcCtr = req.getParameter("suppProcCtr");
            Integer pcCenter = null;
            try {
                Integer iProcCtr = new Integer(suppProcCtr);
                BranchObject spcObj = BranchNut.getObject(iProcCtr);
                pcCenter = spcObj.accPCCenterId;
            } catch (Exception ex) {
            }
            String naForeignTable = NominalAccountBean.FT_SUPPLIER;
            Integer naForeignKey = null;
            Long natForeignKey = null;
            String currency = null;
            BigDecimal bdLessThan = new BigDecimal("0.00");
            BigDecimal bdMoreThan = null;
            String status = NominalAccountBean.STATUS_ACTIVE;
            Vector vecNa = NominalAccountNut.getValueObjectsGiven(pcCenter, naForeignTable, naForeignKey, currency, bdMoreThan, bdLessThan, status);
            Vector vecNominalAccExt = new Vector();
            Vector vecNominalAccInt = new Vector();
            for (int count1 = 0; count1 < vecNa.size(); count1++) {
                NominalAccountObject naObj = (NominalAccountObject) vecNa.get(count1);
                SuppAccountObject caObj = (SuppAccountObject) SuppAccountNut.getObject(naObj.foreignKey);
                if (!caObj.accType.equals(SuppAccountBean.ACCTYPE_INTERNAL_ENUM)) {
                    vecNominalAccExt.add(naObj);
                } else {
                    vecNominalAccInt.add(naObj);
                }
            }
            req.setAttribute("vecNominalAccExt", vecNominalAccExt);
            req.setAttribute("vecNominalAccInt", vecNominalAccInt);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
