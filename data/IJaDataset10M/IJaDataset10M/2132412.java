package cz.muni.fi.pclis.web.handlers;

import cz.muni.fi.pclis.domain.User;
import cz.muni.fi.pclis.web.annotations.Role;
import cz.muni.fi.pclis.domain.AcademicRole;
import cz.muni.fi.pclis.service.UserService;
import cz.muni.fi.pclis.service.UserToEducatableRelationService;
import cz.muni.fi.pclis.web.annotations.AllowedRole;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.AbstractController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Special implementation of {@code SimpleUrlHandlerMapping} which redirects the request if needed, either if there
 * are no administrators or user is not authorized to use the controller
 * User: Ľuboš Pecho
 * Date: 30.6.2009
 * Time: 19:29:34
 */
public class RedirectUrlHandlerMapping extends SimpleUrlHandlerMapping {

    private String redirectUrl;

    private RedirectHelper redirectHelper;

    private UserService userService;

    private UserToEducatableRelationService userToEducatableRelationService;

    @Override
    protected Object getHandlerInternal(HttpServletRequest httpServletRequest) throws Exception {
        String url = httpServletRequest.getRequestURL().toString();
        if (url.endsWith("/pclis/") || url.endsWith("/pclis/")) {
            exposePathWithinMapping("home.htm", httpServletRequest);
            return new RedirectController("home.htm");
        }
        String uin = (String) httpServletRequest.getSession().getAttribute("uin");
        User user = userService.searchByUIN(uin);
        if (user != null) {
            httpServletRequest.getSession().setAttribute("user", user);
            List<Role> roles = userService.getAvailableRolesForTerm(user, null);
            httpServletRequest.getSession().setAttribute("roles", roles);
        } else {
            exposePathWithinMapping("unauthorized.htm", httpServletRequest);
            if (!url.contains("unauthorized.htm") && !url.contains(redirectUrl)) {
                return new RedirectController("unauthorized.htm");
            }
        }
        if (!url.contains(redirectUrl) && redirectHelper.redirect(httpServletRequest)) {
            exposePathWithinMapping(redirectUrl, httpServletRequest);
            return new RedirectController(redirectUrl);
        }
        Object o = super.getHandlerInternal(httpServletRequest);
        if (o != null) {
            Object handler = ((HandlerExecutionChain) o).getHandler();
            AllowedRole annotation = handler.getClass().getAnnotation(AllowedRole.class);
            if (annotation != null) {
                switch(annotation.value()) {
                    case ADMIN:
                        if (!user.isAdmin()) {
                            return null;
                        }
                        break;
                    case TEACHER:
                        if (!userToEducatableRelationService.hasRole(user, AcademicRole.TEACHER)) {
                            return null;
                        }
                        break;
                    case STUDENT:
                        if (!userToEducatableRelationService.hasRole(user, AcademicRole.STUDENT)) {
                            return null;
                        }
                        break;
                }
            }
        }
        return o;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public RedirectHelper getRedirectHelper() {
        return redirectHelper;
    }

    public void setRedirectHelper(RedirectHelper redirectHelper) {
        this.redirectHelper = redirectHelper;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public UserToEducatableRelationService getUserToEducatableRelationService() {
        return userToEducatableRelationService;
    }

    public void setUserToEducatableRelationService(UserToEducatableRelationService userToEducatableRelationService) {
        this.userToEducatableRelationService = userToEducatableRelationService;
    }

    private class RedirectController extends AbstractController {

        private String redirectUrl;

        public RedirectController(String redirectUrl) {
            if (redirectUrl == null) {
                throw new NullPointerException("redirectUrl must not be null");
            }
            this.redirectUrl = redirectUrl;
        }

        protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
            return new ModelAndView("redirect:" + redirectUrl);
        }
    }
}
