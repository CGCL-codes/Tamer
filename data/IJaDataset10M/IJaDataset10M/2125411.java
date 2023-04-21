package com.guzzservices.action.console.task;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.guzz.util.Assert;
import org.guzz.util.RequestUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.guzzservices.business.Task;
import com.guzzservices.manager.Constants;
import com.guzzservices.manager.ISessionManager;
import com.guzzservices.manager.ITaskManager;
import com.guzzservices.sso.LoginUser;

/**
 * 
 * 
 * 
 * @author liukaixuan(liukaixuan@gmail.com)
 */
public class TaskRescheduleAction implements Controller {

    private ITaskManager taskManager;

    private ISessionManager sessionManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        LoginUser loginUser = sessionManager.getLoginUser(request, response);
        int id = RequestUtil.getParameterAsInt(request, "id", 0);
        String newCron = request.getParameter("cron");
        Assert.assertNotEmpty(newCron, "new cron required.");
        Task task = taskManager.getForUpdate(id);
        if (task != null) {
            this.sessionManager.assertOwner(loginUser, Constants.serviceName.TASK, String.valueOf(task.getGroupId()));
            this.taskManager.reScheduleTask(task, newCron);
        }
        return null;
    }

    public ITaskManager getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(ITaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public ISessionManager getSessionManager() {
        return sessionManager;
    }

    public void setSessionManager(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
}
