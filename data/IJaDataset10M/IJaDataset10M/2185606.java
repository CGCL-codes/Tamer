package com.rooster.action;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;
import com.rooster.form.DollertoSalaryForm;

public class DollertoSalaryAction extends Action {

    static Logger logger = Logger.getLogger(DollertoSalaryAction.class.getName());

    public ActionForward execute(ActionMapping map, ActionForm frm, HttpServletRequest req, HttpServletResponse res) throws IOException {
        DollertoSalaryForm dsf = (DollertoSalaryForm) frm;
        HttpSession session = req.getSession(false);
        if ((session == null) || (session.getAttribute("UserId") == null) || (session.getAttribute("UserId").equals(new String("")))) {
            req.setAttribute("APP_ERROR", "Your Session Got Expired. Please Re-login.");
            try {
                res.sendRedirect("/index.jsp");
            } catch (IOException e) {
            }
            return null;
        }
        String budgetUsTeam, totBudgetPA, totEmpTax, presentDol, depen, costMedi, mediInsur, totMaxDolSal, h1Sal, hourSaloutBenefit;
        budgetUsTeam = dsf.getBudgetUsTeam();
        totBudgetPA = dsf.getTotBudgetPA();
        totEmpTax = dsf.getTotEmpTax();
        presentDol = dsf.getPresentDol();
        depen = dsf.getDepen();
        costMedi = dsf.getCostMedi();
        mediInsur = dsf.getMediInsur();
        totMaxDolSal = dsf.getTotMaxDolSal();
        h1Sal = dsf.getH1Sal();
        hourSaloutBenefit = dsf.getHourSaloutBenefit();
        logger.debug("budgetUsTeam : " + budgetUsTeam);
        logger.debug("totBudgetPA : " + totBudgetPA);
        logger.debug("totEmpTax : " + totEmpTax);
        logger.debug("presentDol : " + presentDol);
        logger.debug("depen : " + depen);
        logger.debug("costMedi : " + costMedi);
        logger.debug("mediInsur : " + mediInsur);
        logger.debug("totMaxDolSal : " + totMaxDolSal);
        logger.debug("h1Sal : " + h1Sal);
        logger.debug("hourSaloutBenefit : " + hourSaloutBenefit);
        return map.findForward("dolltosalsuccess");
    }
}
