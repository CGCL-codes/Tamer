package ru.arriah.servicedesk.web.chief.action;

import java.io.IOException;
import java.util.Collection;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.arriah.common.web.action.ActionMapping;
import ru.arriah.common.web.action.DumbAction;
import ru.arriah.servicedesk.bean.RequestBean;
import ru.arriah.servicedesk.bean.TaskBean;
import ru.arriah.servicedesk.ejb.RequestManagerLocalHome;
import ru.arriah.servicedesk.ejb.RequestManagerLocalObject;
import ru.arriah.servicedesk.help.Utils;
import ru.arriah.servicedesk.web.commonaction.exception.InternalException;

public class DisplayRequestDetailsAction extends DumbAction {

    protected RequestManagerLocalHome requestManagerHome;

    public DisplayRequestDetailsAction() throws NamingException {
        super();
        requestManagerHome = Utils.getRequestManagerHomeInterface();
    }

    public String execute(ActionMapping actionMapping, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int requestId = idVal(request.getParameter("request_id"));
            RequestManagerLocalObject requestManager = requestManagerHome.create();
            RequestBean requestBean = requestManager.selectRequestDetails(requestId);
            Collection<TaskBean> tasksList = requestManager.selectTasksByRequest(requestId, "executor", "ASC");
            request.setAttribute("requestBean", requestBean);
            request.setAttribute("tasksList", tasksList);
            requestManager.remove();
            return actionMapping.getSuccessTarget();
        } catch (Exception e) {
            handleException(e, "execute");
            throw new InternalException("ErrorMessage.internal");
        }
    }
}
