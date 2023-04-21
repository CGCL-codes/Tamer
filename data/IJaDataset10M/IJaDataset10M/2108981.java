package ru.arriah.servicedesk.web.leadExecutor.action;

import java.io.IOException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.arriah.common.web.action.ActionMapping;
import ru.arriah.common.web.action.DumbAction;
import ru.arriah.servicedesk.web.commonaction.exception.InternalException;

public class DisplaySideBarListAction extends DumbAction {

    public DisplaySideBarListAction() throws NamingException {
        super();
    }

    public String execute(ActionMapping actionMapping, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Action action = Action.valueOf(actionMapping.getActionName());
            switch(action) {
                case displayRequestsList:
                    request.getSession().removeAttribute("searchRequestTemplate");
                    break;
                case displayTasksList:
                    request.getSession().removeAttribute("searchTaskTemplate");
                    break;
                default:
                    throw new RuntimeException("Unsupported action " + action);
            }
            request.getSession().setAttribute("_sideBarListType", actionMapping.getActionName());
            return actionMapping.getSuccessTarget();
        } catch (Exception e) {
            handleException(e, "execute");
            throw new InternalException("ErrorMessage.internal");
        }
    }
}
