package com.noumenonsoftware.open.cas;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Bryant Larsen
 */
public class CasInterceptor extends HandlerInterceptorAdapter {

    String casLoginUrl;

    Cas20ServiceTicketValidator ticketValidator;

    String serviceUrl;

    boolean autoUrl = false;

    UserFactory ldapFactory = null;

    CasCompatibleLoginManager loginManager = null;

    List<String> portableHosts;

    public CasInterceptor() {
        super();
    }

    /**
     * Set the url to the login page.
     * The user will be directed there if they do not have a session,
     * or if they are not setting one up.
     * @param casLoginUrl the url for login.
     */
    public void setCasLoginUrl(String casLoginUrl) {
        this.casLoginUrl = casLoginUrl;
    }

    /**
     * Set the ticket Validator.
     * @param ticketValidator
     */
    public void setTicketValidator(Cas20ServiceTicketValidator ticketValidator) {
        this.ticketValidator = ticketValidator;
    }

    /**
     * This is the url of the site that cas will compare with the ticket.
     * @param serviceUrl
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /**
     * This method allows you to set an ldap factory.
     * @param ldapFactory
     */
    public void setLdapFactory(UserFactory ldapFactory) {
        this.ldapFactory = ldapFactory;
    }

    /**
     * Provides the ability for the cas interceptor to figure out what the service url should be.
     * If set to true, the interceptor will build a service url out of the current request.  Includes things like port number etc..
     * @param autoUrl
     */
    public void setAutoUrl(boolean autoUrl) {
        this.autoUrl = autoUrl;
    }

    /**
     * Provides ability to set a manager that will perform extra tasks after the initial session has been established.
     * @param loginManager
     */
    public void setLoginManager(CasCompatibleLoginManager loginManager) {
        this.loginManager = loginManager;
    }

    public void setPortableHosts(List<String> portableHosts) {
        this.portableHosts = portableHosts;
    }

    /**
     * This method is run before any request is allowed to complete.
     * The idea here is:
     * if they already have a session, AND THEY ARE AUTHENTICATED then they are passed along.
     * If they do not have a session OR THE SESSION IS NOT AUTHENTICATED, they are dispatched to the login/welcome screen.
     *       If they had a query arg, it is stored somewhere, either in the session or a cookie.
     *
     * @return true if a valid session is found. Else assumes that the request has been handled and returns false.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Logger.getLogger(CasInterceptor.class.getName()).log(Level.FINE, "Entering interceptor.");
        String localServiceUrl = this.serviceUrl;
        if (this.autoUrl) {
            StringBuffer url = new StringBuffer();
            url.append(request.getScheme());
            url.append("://");
            url.append(request.getServerName());
            if (portableHosts != null && portableHosts.contains(request.getServerName())) {
                if (request.getLocalPort() != 80 && request.getLocalPort() != 443) {
                    url.append(":" + request.getLocalPort());
                }
            }
            url.append(request.getRequestURI());
            localServiceUrl = url.toString();
        }
        if (CasSessionManager.isAuthenticated(request)) {
            Logger.getLogger(CasInterceptor.class.getName()).log(Level.FINE, "Found session, allowing request.");
            return true;
        } else if (request.getParameter("ticket") != null) {
            Logger.getLogger(CasInterceptor.class.getName()).log(Level.FINE, "Found ticket, validating...");
            String myTicket = request.getParameter("ticket");
            try {
                Assertion casAssertion = this.ticketValidator.validate(myTicket, localServiceUrl);
                String user = casAssertion.getPrincipal().getName();
                CasSessionManager.setUser(request, user);
                if (this.ldapFactory != null) {
                    CasSessionManager.setUserPrivate(request, this.ldapFactory.getPrivateUserByUser(user));
                }
                if (this.loginManager != null) {
                    this.loginManager.completeLoginProcedure(user, request);
                }
                Logger.getLogger(CasInterceptor.class.getName()).log(Level.FINE, "..Validation passed.  Creating session.");
            } catch (TicketValidationException e) {
                Logger.getLogger(CasInterceptor.class.getName()).log(Level.FINE, "..Validation failed, directing to login page.");
                response.sendRedirect(this.casLoginUrl + "?service=" + localServiceUrl);
                return false;
            } catch (UnauthorizedUserException e) {
                Logger.getLogger(CasInterceptor.class.getName()).log(Level.FINE, "User was un-authorized. directing to login page.");
                response.sendRedirect(this.casLoginUrl + "?service=" + localServiceUrl);
                return false;
            }
            return true;
        } else {
            Logger.getLogger(CasInterceptor.class.getName()).log(Level.FINE, "No ticket, No Session.  redirect to login");
            CasSessionManager.setRequestParams(request);
            response.sendRedirect(this.casLoginUrl + "?service=" + localServiceUrl);
            return false;
        }
    }
}
