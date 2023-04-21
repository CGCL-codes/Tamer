package org.bionote.webapp.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bionote.om.service.SpaceService;
import org.bionote.om.service.UserService;
import org.bionote.om.service.exception.ServiceException;
import org.bionote.webapp.container.SessionContainer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author mbreese
 *
 */
public class SessionVariableReattachFilter implements Filter {

    protected final Log log = LogFactory.getLog(getClass());

    public void init(FilterConfig arg0) throws ServletException {
        log.debug("Intializing SessionVariableReattachFilter filter");
    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) arg0;
        HttpServletResponse resp = (HttpServletResponse) arg1;
        SessionContainer container = SessionContainer.getSessionContainer(req);
        if (container != null) {
            WebApplicationContext springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getSession().getServletContext());
            SpaceService spaceService = (SpaceService) springContext.getBean("spaceService");
            UserService userService = (UserService) springContext.getBean("userService");
            if (container.getSpace() != null) {
                try {
                    log.debug("reattaching space");
                    spaceService.reattach(container.getSpace());
                } catch (ServiceException e) {
                    log.debug("Exception!");
                    log.error(e);
                }
            }
            if (container.getUser() != null) {
                try {
                    log.debug("reattaching user");
                    userService.reattach(container.getUser());
                } catch (ServiceException e) {
                    log.debug("Exception!");
                    log.error(e);
                }
            }
        }
        chain.doFilter(arg0, arg1);
    }

    public void destroy() {
    }
}
