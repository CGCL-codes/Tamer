package com.vlee.servlet.customer;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;
import java.sql.*;
import java.math.*;
import com.vlee.util.*;
import com.vlee.ejb.user.*;
import com.vlee.ejb.customer.*;
import com.vlee.bean.customer.*;

public class DoCustCorporateAccountAdd implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        String parentAction = req.getParameter("parentAction");
        if (parentAction != null) {
            req.setAttribute("parentAction", parentAction);
        }
        String parentFormName = req.getParameter("parentFormName");
        if (parentFormName != null) {
            req.setAttribute("parentFormName", parentFormName);
        }
        String formName = req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter("cust-corporate-acc-add-page");
        }
        if (formName.equals("newCorporateAcc")) {
            fnNewForm(servlet, req, res);
            return new ActionRouter("cust-corporate-acc-add-page");
        }
        if (formName.equals("setDetails")) {
            try {
                fnSetDetails(servlet, req, res);
                return new ActionRouter("cust-corporate-acc-add-page");
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
        }
        if (formName.equals("createAccount")) {
            try {
                fnCreateAccount(servlet, req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
        }
        if (formName.equals("setLocation")) {
            fnSetLocation(servlet, req, res);
        }
        return new ActionRouter("cust-corporate-acc-add-page");
    }

    private void fnSetLocation(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        CreateCorporateAccountForm ccaf = (CreateCorporateAccountForm) session.getAttribute("cust-corporate-account-add-form");
        String dlPkid = req.getParameter("dlPkid");
        String dlOption = req.getParameter("dlOption");
        try {
            Long pkid = new Long(dlPkid);
            ccaf.setLocation(pkid, dlOption);
        } catch (Exception ex) {
        }
    }

    private void fnCreateAccount(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        CreateCorporateAccountForm ccaf = (CreateCorporateAccountForm) session.getAttribute("cust-corporate-account-add-form");
        ccaf.createAccount();
    }

    private void fnNewForm(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        CreateCorporateAccountForm ccaf = new CreateCorporateAccountForm(userId);
        String parentFormName = req.getParameter("parentFormName");
        String parentAction = req.getParameter("parentAction");
        Log.printVerbose("ParentAction: " + parentAction);
        Log.printVerbose("ParentFormName: " + parentFormName);
        if (parentFormName != null) {
            ccaf.setParentFormName(parentFormName);
        }
        if (parentAction != null) {
            ccaf.setParentAction(parentAction);
        }
        session.setAttribute("cust-corporate-account-add-form", ccaf);
    }

    private void fnSetDetails(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Integer userId = (Integer) session.getAttribute("userId");
        CreateCorporateAccountForm ccaf = (CreateCorporateAccountForm) session.getAttribute("cust-corporate-account-add-form");
        String codePrefix = req.getParameter("cust_code");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        Integer accType = new Integer(req.getParameter("accType"));
        String identityNumber = req.getParameter("identityNumber");
        String mainAddress1 = req.getParameter("main_address1");
        String mainAddress2 = req.getParameter("main_address2");
        String mainAddress3 = req.getParameter("main_address3");
        String mainCity = req.getParameter("main_city");
        String mainPostcode = req.getParameter("main_postcode");
        String mainState = req.getParameter("main_state");
        String mainCountry = req.getParameter("main_country");
        String telephone1 = req.getParameter("telephone1_Prefix") + "-" + req.getParameter("telephone1");
        String telephone2 = req.getParameter("telephone2_Prefix") + "-" + req.getParameter("telephone2");
        String homePhone = req.getParameter("home_phonePrefix") + "-" + req.getParameter("home_phone");
        String mobilePhone = req.getParameter("mobile_phonePrefix") + "-" + req.getParameter("mobile_phone");
        String faxNo = req.getParameter("fax_noPrefix") + "-" + req.getParameter("fax_no");
        String email1 = req.getParameter("email1");
        String homepage = req.getParameter("homepage");
        BigDecimal creditLimit = new BigDecimal(req.getParameter("creditlimit"));
        Integer creditTerms = new Integer(req.getParameter("terms"));
        String state = req.getParameter("state");
        String dealerCode = req.getParameter("dealerCode");
        String property1 = req.getParameter("property1");
        String property2 = req.getParameter("property2");
        String property3 = req.getParameter("property3");
        String property4 = req.getParameter("property4");
        String property5 = req.getParameter("property5");
        BigDecimal factorPricing = new BigDecimal(req.getParameter("factorPricing"));
        BigDecimal factorDiscount = new BigDecimal(req.getParameter("factorDiscount"));
        ccaf.setDetails(name.trim(), codePrefix.trim(), description.trim(), accType, identityNumber.trim(), mainAddress1.trim(), mainAddress2.trim(), mainAddress3.trim(), mainCity.trim(), mainPostcode.trim(), mainState.trim(), mainCountry.trim(), telephone1, telephone2, homePhone, mobilePhone, faxNo, email1.trim(), homepage.trim(), creditLimit, creditTerms, state.trim(), dealerCode.trim(), property1, property2, property3, property4, property5, factorPricing, factorDiscount, userId);
        Vector vecCheck = ccaf.getSimilarName();
        if (vecCheck.size() > 0) {
            req.setAttribute("duplicateExists", "true");
        }
        CustAccountObject custObj = new CustAccountObject();
        custObj.identityNumber = req.getParameter("identityNumber");
        if (!"".equals(custObj.identityNumber)) {
            QueryObject query = new QueryObject(new String[] { CustAccountBean.IDENTITY_NUMBER + " ~* '" + custObj.identityNumber + "' " });
            Vector vecSimilarIDNumber = new Vector(CustAccountNut.getObjects(query));
            if (vecSimilarIDNumber.size() > 0) {
                throw new Exception(" The Corporate Account Registration Number May Have been Used, please double check!");
            }
        }
        req.setAttribute("canSave", "true");
        String createNow = req.getParameter("createNow");
        Log.printVerbose("...CREATE NOW =" + createNow);
        if (createNow != null && createNow.equals("true")) {
            ccaf.createAccount();
            custObj = ccaf.getCustAccount();
            AuditTrailObject atObj = new AuditTrailObject();
            atObj.userId = (Integer) session.getAttribute("userId");
            atObj.auditType = AuditTrailBean.TYPE_CONFIG;
            atObj.time = TimeFormat.getTimestamp();
            atObj.remarks = "create customer: " + name;
            atObj.tc_entity_table = CustAccountBean.TABLENAME;
            atObj.tc_entity_id = custObj.pkid;
            atObj.tc_action = AuditTrailBean.TC_ACTION_CREATE;
            AuditTrailNut.fnCreate(atObj);
        }
    }
}
