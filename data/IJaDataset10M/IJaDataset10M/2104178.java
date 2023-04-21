package org.jmonks.dms.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.jmonks.dms.util.DMSUtil;
import org.jmonks.dms.util.SessionManager;
import org.jmonks.dms.util.UserManager;
import org.jmonks.dms.util.UserManager.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author w951h8m
 */
public class DeleteUserController implements Controller {

    protected static final Logger logger = Logger.getLogger(DeleteUserController.class);

    /** Creates a new instance of DeleteUserController */
    public DeleteUserController() {
        logger.info("DeleteUserController has been called");
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map model = new HashMap();
        String viewName = "manageusers";
        User user = SessionManager.getUserFromSession(request);
        if (user == null) {
            request.getRequestDispatcher("/login.scr").forward(request, response);
        } else {
            if (!user.isAdmin()) {
                viewName = DMSUtil.noAccessNotAllowed(model);
            } else {
                String userName = request.getParameter("userName");
                UserManager.getInstance().deleteUser(userName);
                model.put("userList", UserManager.getInstance().getAllUsers());
            }
        }
        return new ModelAndView(viewName, model);
    }
}
