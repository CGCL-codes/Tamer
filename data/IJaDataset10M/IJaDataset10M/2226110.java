package ru.arriah.servicedesk.web.leadExecutor.action;

import java.io.IOException;
import java.util.Collection;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ru.arriah.common.bean.MessageBean;
import ru.arriah.common.web.action.ActionMapping;
import ru.arriah.common.web.action.DumbAction;
import ru.arriah.servicedesk.bean.TaskBean;
import ru.arriah.servicedesk.ejb.RequestManagerLocalHome;
import ru.arriah.servicedesk.ejb.RequestManagerLocalObject;
import ru.arriah.servicedesk.help.Utils;
import ru.arriah.servicedesk.web.commonaction.exception.InternalException;

public class DisplayTaskReportFormAction extends DumbAction {

    protected RequestManagerLocalHome requestManagerLocalHome;

    protected Collection<MessageBean> messagesList;

    public DisplayTaskReportFormAction() throws NamingException {
        super();
        requestManagerLocalHome = Utils.getRequestManagerHomeInterface();
    }

    public String execute(ActionMapping actionMapping, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            RequestManagerLocalObject requestManager = requestManagerLocalHome.create();
            int taskId = idVal(request.getParameter("task_id"));
            int requestId = idVal(request.getParameter("request_id"));
            TaskBean taskBean = requestManager.selectTask(taskId);
            if (taskBean.getStatusId() == 3) {
                messagesList.add(new MessageBean(config.getProperty("ERROR.EDIT_DENIED"), true));
                request.setAttribute("messagesList", messagesList);
                request.setAttribute("taskBean", taskBean);
                requestManager.remove();
                return actionMapping.getFailureTarget();
            }
            Collection<TaskBean> tasksList = requestManager.selectTasksByRequest(requestId);
            request.setAttribute("tasksList", tasksList);
            request.setAttribute("taskBean", taskBean);
            requestManager.remove();
            return actionMapping.getSuccessTarget();
        } catch (Exception e) {
            handleException(e, "execute");
            throw new InternalException("ErrorMessage.internal");
        }
    }
}
