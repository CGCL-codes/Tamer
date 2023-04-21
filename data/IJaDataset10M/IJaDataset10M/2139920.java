package com.xiyou.cms.struts.affiche.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;
import com.xiyou.cms.hibernate.dao.XyAfficheDAO;
import com.xiyou.cms.hibernate.dao.XyUserDAO;
import com.xiyou.cms.hibernate.mapping.XyAffiche;
import com.xiyou.cms.hibernate.mapping.XyUser;
import java.text.SimpleDateFormat;
import java.util.Date;

/** 
 * MyEclipse Struts
 * Creation date: 10-24-2008
 * 
 * XDoclet definition:
 * @struts.action path="/affiche/afficheAdd" name="afficheAddForm" input="/form/afficheAdd.jsp" scope="request" validate="true"
 * @struts.action-forward name="goto" path="/affiche/showAfficheList.do"
 */
public class AfficheAddAction extends Action {

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        DynaValidatorForm afficheAddForm = (DynaValidatorForm) form;
        Date date = new Date();
        String format = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String nowDate = sdf.format(date);
        XyAffiche affiche = new XyAffiche();
        XyAfficheDAO afficheDAO = new XyAfficheDAO();
        affiche.setAfficheTitle((String) afficheAddForm.get("afficheTitle"));
        affiche.setAfficheContext((String) afficheAddForm.get("afficheContext"));
        affiche.setAfficheCreateTime(nowDate);
        affiche.setXyUser((XyUser) request.getSession().getAttribute("user"));
        System.out.println("afficheaddtwo");
        afficheDAO.save(affiche);
        System.out.println("afficheaddtwo2");
        return mapping.findForward("goto");
    }
}
