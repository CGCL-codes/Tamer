package net.sf.sail.webapp.presentation.web.filters;

import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import net.sf.sail.webapp.domain.User;
import net.sf.sail.webapp.presentation.web.listeners.PasSessionListener;
import net.sf.sail.webapp.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.Authentication;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Custom AuthenticationProcessingFilter that subclasses Acegi Security. This
 * filter upon successful authentication will retrieve a <code>User</code> and
 * put it into the http session.
 * 
 * @author Cynick Young
 * 
 * @version $Id: PasAuthenticationProcessingFilter.java 2481 2009-09-21 18:12:43Z honchikun@gmail.com $
 * 
 */
public class PasAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

    private static final Log LOGGER = LogFactory.getLog(PasAuthenticationProcessingFilter.class);

    /**
     * @see org.acegisecurity.ui.AbstractProcessingFilter#successfulAuthentication(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse,
     *      org.acegisecurity.Authentication)
     */
    @Override
    protected void successfulAuthentication(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        if (LOGGER.isDebugEnabled()) {
            logDebug(userDetails);
        }
        HttpSession session = request.getSession();
        ApplicationContext springContext = WebApplicationContextUtils.getWebApplicationContext(session.getServletContext());
        UserService userService = (UserService) springContext.getBean("userService");
        User user = userService.retrieveUser(userDetails);
        session.setAttribute(User.CURRENT_USER_SESSION_KEY, user);
        String sessionId = session.getId();
        HashMap<String, User> allLoggedInUsers = (HashMap<String, User>) session.getServletContext().getAttribute("allLoggedInUsers");
        if (allLoggedInUsers == null) {
            allLoggedInUsers = new HashMap<String, User>();
            session.getServletContext().setAttribute(PasSessionListener.ALL_LOGGED_IN_USERS, allLoggedInUsers);
        }
        allLoggedInUsers.put(sessionId, user);
        super.successfulAuthentication(request, response, authResult);
    }

    private void logDebug(UserDetails userDetails) {
        LOGGER.debug("UserDetails logging in: " + userDetails.getUsername());
    }
}
