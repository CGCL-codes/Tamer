package com.gever.sysman.organization.webapp.action;

import com.gever.sysman.util.BaseAction;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.gever.exception.DefaultException;
import com.gever.sysman.organization.dao.UserDAO;
import java.util.ArrayList;
import com.gever.sysman.organization.dao.OrganizationFactory;

public class GetFunctionaryAction extends BaseAction {

    public ActionForward getFunctionary(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws DefaultException {
        UserDAO userDAO = OrganizationFactory.getInstance().getUserDAO();
        ArrayList userNameList = (ArrayList) userDAO.getAllUserName();
        ArrayList userLevel = (ArrayList) userDAO.getAllLevel();
        request.setAttribute("userLevel", userLevel);
        request.setAttribute("userNameList", userNameList);
        return mapping.findForward("getfunctionary");
    }
}
