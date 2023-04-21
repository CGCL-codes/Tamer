package com.webapp.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import com.webapp.controllers.utils.DiscussionControllerUtil;
import com.webapp.controllers.utils.ProblemControllerUtil;

public class DiscussionController implements Handler {

    private final ProblemControllerUtil problemControllerUtil = new ProblemControllerUtil();

    private Handler successor;

    private final ApplicationContext context = new FileSystemXmlApplicationContext("controllerContext.xml");

    private final DiscussionControllerUtil discussionControllerUtil = (DiscussionControllerUtil) context.getBean("DiscussionControllerUtil");

    public DiscussionController() {
    }

    public Handler getSuccessor() {
        return successor;
    }

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, String action) throws Exception {
        if (action.equalsIgnoreCase(ActionsPageMapping.ConstantsAction.DISCUSSION)) {
            problemControllerUtil.putToSessionProblemById(request, response);
            discussionControllerUtil.putToSessionDiscussionsByProblemID(request, response);
            response.sendRedirect(ActionsPageMapping.ACTIONS_PAGES_MAPPING.get(action));
        } else if (action.equalsIgnoreCase(ActionsPageMapping.ConstantsAction.ADD_DESCUSSION)) {
            discussionControllerUtil.addDiscussion(request, response);
        } else {
            successor.execute(request, response, action);
        }
    }
}
