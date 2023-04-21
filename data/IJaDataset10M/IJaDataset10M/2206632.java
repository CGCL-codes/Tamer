package org.directdemocracyportal.democracy.web.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.acegisecurity.Authentication;
import org.acegisecurity.context.HttpSessionContextIntegrationFilter;
import org.acegisecurity.context.SecurityContext;
import org.directdemocracyportal.democracy.model.application.User;
import org.directdemocracyportal.democracy.service.UserService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * 
 * @author Pether Sorling
 * 
 */
public class UserController extends MultiActionController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public ModelAndView memberHandler(final HttpServletRequest request, final HttpServletResponse response) {
        ModelAndView view;
        Map<String, Object> model = new HashMap<String, Object>();
        String id = request.getParameter("id");
        if (id != null) {
            Long userId = Long.valueOf(id);
            model.put("member", userService.getUser(userId));
        } else {
            SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(HttpSessionContextIntegrationFilter.ACEGI_SECURITY_CONTEXT_KEY);
            Authentication authentication = securityContext.getAuthentication();
            if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
                model.put("member", authentication.getPrincipal());
            }
        }
        view = new ModelAndView("member", model);
        return view;
    }
}
