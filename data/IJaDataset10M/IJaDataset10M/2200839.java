package com.vlee.servlet.loyal;

import com.vlee.servlet.main.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.math.*;
import java.util.Vector;
import com.vlee.ejb.customer.*;
import com.vlee.bean.loyalty.*;
import com.vlee.ejb.user.*;
import com.vlee.util.*;

public class DoMemberCardEdit extends ActionDo implements Action {

    public ActionRouter perform(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws java.io.IOException, javax.servlet.ServletException {
        fnCreateSessionObject(servlet, req, res);
        String formName = req.getParameter("formName");
        if (formName == null) {
            return new ActionRouter("loyal-membercard-edit-page");
        }
        if (formName.equals("setCardNo")) {
            try {
                fnSetCardNo(servlet, req, res);
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
        }
        if (formName.equals("setBranch")) {
            fnSetBranch(servlet, req, res);
        }
        if (formName.equals("setDetails")) {
            try {
                String error = fnSetDetails(servlet, req, res);
                if ("".equals(error)) req.setAttribute("popupAlert", "Successfully saved the changes!");
            } catch (Exception ex) {
                req.setAttribute("errMsg", ex.getMessage());
            }
        }
        return new ActionRouter("loyal-membercard-edit-page");
    }

    public void fnSetCardNo(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        EditMemberCardSession cmcs = (EditMemberCardSession) session.getAttribute("loyal-membercard-edit-session");
        String cardNo = req.getParameter("theCardNo");
        if (cardNo != null) {
            cmcs.setMemberCard(cardNo);
        }
    }

    public String fnSetDetails(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) throws Exception {
        String error = "";
        HttpSession session = req.getSession();
        EditMemberCardSession cmcs = (EditMemberCardSession) session.getAttribute("loyal-membercard-edit-session");
        if (cmcs == null) {
            return error;
        }
        String cardNo = req.getParameter("cardNo");
        System.out.println("originalCardNo" + cmcs.originalCardNo);
        System.out.println("cardNo" + cardNo);
        if (!(cmcs.originalCardNo).equals(cardNo)) {
            System.out.println("originalCardNo" + cmcs.originalCardNo);
            System.out.println("cardNo" + cardNo);
            MemberCardObject memberObj = MemberCardNut.getObjectByCardNo(cardNo);
            if (memberObj != null) {
                throw new Exception("The card number exists in the database!!");
            }
        }
        if ("".equals(cardNo)) {
            System.out.println("cardNo" + cardNo);
            throw new Exception("The card number cannot be blank!");
        }
        String identityNumber = req.getParameter("identityNumber");
        System.out.println("original identityNumber" + cmcs.getCard().identityNumber);
        System.out.println("identityNumber" + identityNumber);
        if (!(cmcs.getCard().identityNumber).equals(identityNumber)) {
            System.out.println("original identityNumber" + cmcs.getCard().identityNumber);
            System.out.println("identityNumber" + identityNumber);
            QueryObject query = new QueryObject(new String[] { MemberCardBean.IDENTITY_NUMBER + " = '" + identityNumber + "'" });
            Vector vecDuplicatedIdentity = new Vector(MemberCardNut.getObjects(query));
            if (vecDuplicatedIdentity.size() > 0) {
                req.setAttribute("vecDuplicatedIdentity", vecDuplicatedIdentity);
                error = "duplicated identity number";
                return error;
            }
        }
        Integer userSales = new Integer(0);
        try {
            userSales = new Integer(req.getParameter("userSales"));
        } catch (Exception ex) {
        }
        String nameFirst = req.getParameter("nameFirst");
        String nameLast = req.getParameter("nameLast");
        String gender = req.getParameter("gender");
        String dateOfBirth = req.getParameter("dateOfBirth");
        String cardType = req.getParameter("cardType");
        String cardNo4 = req.getParameter("cardNo4");
        String membershipFeeAnnual = req.getParameter("membershipFeeAnnual");
        String membershipFeeDue = req.getParameter("membershipFeeDue");
        String mainAddress1 = req.getParameter("mainAdd1");
        String mainAddress2 = req.getParameter("mainAdd2");
        String mainAddress3 = req.getParameter("mainAdd3");
        String mainPostcode = req.getParameter("mainPostcode");
        String mainCity = req.getParameter("mainCity");
        String mainState = req.getParameter("mainState");
        String mainCountry = req.getParameter("mainCountry");
        String mainTelephone1 = req.getParameter("membertelephone1_Prefix") + "-" + req.getParameter("membertelephone1") + "Ext" + req.getParameter("memberext");
        String mainTelephone2 = req.getParameter("membertelephone2_Prefix") + "-" + req.getParameter("membertelephone2");
        String mainHomePhone = req.getParameter("memberhome_phonePrefix") + "-" + req.getParameter("memberhome_phone");
        String mainMobilePhone = req.getParameter("membermobile_phonePrefix") + "-" + req.getParameter("membermobile_phone");
        String mainFaxNo = req.getParameter("memberfax_noPrefix") + "-" + req.getParameter("memberfax_no");
        String mainEmail1 = req.getParameter("mainEmail1");
        String mainEmail2 = req.getParameter("mainEmail2");
        String contactAddress1 = req.getParameter("contactAdd1");
        String contactAddress2 = req.getParameter("contactAdd2");
        String contactAddress3 = req.getParameter("contactAdd3");
        String contactPostcode = req.getParameter("contactPostcode");
        String contactState = req.getParameter("contactState");
        String contactCountry = req.getParameter("contactCountry");
        String businessAddress1 = req.getParameter("businessAdd1");
        String businessAddress2 = req.getParameter("businessAdd2");
        String businessAddress3 = req.getParameter("businessAdd3");
        String businessPostcode = req.getParameter("businessPostcode");
        String businessState = req.getParameter("businessState");
        String businessCountry = req.getParameter("businessCountry");
        String businessNature = req.getParameter("businessNature");
        String designation = req.getParameter("designation");
        String ethnic = req.getParameter("ethnic");
        String eduLevel = req.getParameter("eduLevel");
        String nationality = req.getParameter("nationality");
        String maritalStatus = req.getParameter("maritalStatus");
        String grossHouseholdIncome = req.getParameter("grossHouseholdIncome");
        String profession = req.getParameter("profession");
        String householdNumber = req.getParameter("householdNumber");
        String householdJunior = req.getParameter("householdJunior");
        String householdSenior = req.getParameter("householdSenior");
        String dateValidFrom = req.getParameter("dateValidFrom");
        String dateGoodThru = req.getParameter("dateGoodThru");
        Integer iHNumber = new Integer(0);
        Integer iHJunior = new Integer(0);
        Integer iHSenior = new Integer(0);
        try {
            iHNumber = new Integer(householdNumber);
        } catch (Exception ex) {
        }
        try {
            iHJunior = new Integer(householdJunior);
        } catch (Exception ex) {
        }
        try {
            iHSenior = new Integer(householdSenior);
        } catch (Exception ex) {
        }
        try {
            BigDecimal bdAnnual = new BigDecimal(membershipFeeAnnual);
            BigDecimal bdDue = new BigDecimal(membershipFeeDue);
            cmcs.setMembershipFee(bdAnnual, bdDue);
        } catch (Exception ex) {
        }
        cmcs.setDetails(userSales, cardNo, nameFirst, nameLast, gender, dateOfBirth, identityNumber, cardType, cardNo4, mainAddress1, mainAddress2, mainAddress3, mainPostcode, mainCity, mainState, mainCountry, mainTelephone1, mainTelephone2, mainHomePhone, mainMobilePhone, mainFaxNo, mainEmail1, mainEmail2, contactAddress1, contactAddress2, contactAddress3, contactPostcode, contactState, contactCountry, businessAddress1, businessAddress2, businessAddress3, businessPostcode, businessState, businessCountry, nationality, maritalStatus, grossHouseholdIncome, profession, iHNumber, iHJunior, iHSenior, businessNature, designation, ethnic, eduLevel, dateValidFrom, dateGoodThru);
        return error;
    }

    public void fnSetBranch(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = (HttpSession) req.getSession();
        EditMemberCardSession cmcs = (EditMemberCardSession) session.getAttribute("loyal-membercard-edit-session");
        String strBranch = req.getParameter("branchId");
        Integer iBranch = new Integer(strBranch);
        cmcs.setBranch(iBranch);
    }

    public void fnCreateSessionObject(HttpServlet servlet, HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = (HttpSession) req.getSession();
        EditMemberCardSession cmcs = (EditMemberCardSession) session.getAttribute("loyal-membercard-edit-session");
        String reset = req.getParameter("reset");
        if (cmcs == null || reset != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            cmcs = new EditMemberCardSession(userId);
            session.setAttribute("loyal-membercard-edit-session", cmcs);
        }
        String cardNo = req.getParameter("cardNo");
        if (cardNo == null) {
            cardNo = "";
        }
        req.setAttribute("theCardNo", cardNo);
    }
}
