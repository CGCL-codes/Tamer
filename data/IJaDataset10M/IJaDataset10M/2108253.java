package ru.arriah.servicedesk.web.admin.action;

import java.io.IOException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.arriah.common.web.action.ActionMapping;
import ru.arriah.common.web.action.DumbAction;
import ru.arriah.servicedesk.bean.OrganizationBean;
import ru.arriah.servicedesk.ejb.OrganizationManagerLocalHome;
import ru.arriah.servicedesk.ejb.OrganizationManagerLocalObject;
import ru.arriah.servicedesk.help.Utils;
import ru.arriah.servicedesk.web.commonaction.exception.InternalException;

public class DisplayOrganizationFormAction extends DumbAction {

    protected OrganizationManagerLocalHome organizationManagerLocalHome;

    public DisplayOrganizationFormAction() throws NamingException {
        super();
        organizationManagerLocalHome = Utils.getOrganizationManagerHomeInterface();
    }

    public String execute(ActionMapping actionMapping, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            OrganizationManagerLocalObject organizationManager = organizationManagerLocalHome.create();
            int organizationId = idVal(request.getParameter("organization_id"));
            OrganizationBean organizationBean = organizationManager.selectOrganization(organizationId);
            request.setAttribute("organizationBean", organizationBean);
            return actionMapping.getSuccessTarget();
        } catch (Exception e) {
            handleException(e, "execute");
            throw new InternalException("ErrorMessage.internal");
        }
    }
}
