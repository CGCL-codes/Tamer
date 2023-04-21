package es.devel.opentrats.filter;

import es.devel.opentrats.constants.OpenTratsConstants;
import es.devel.opentrats.dao.IAuthenticationDao;
import es.devel.opentrats.dao.exception.AuthenticationDaoException;
import es.devel.opentrats.service.ILogService;
import es.devel.opentrats.service.IMailService;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *
 * @author Fran Serrano
 */
public class OpenTratsSecurityFilter implements Filter {

    @SuppressWarnings("unused")
    private FilterConfig config;

    private IAuthenticationDao authenticationDao;

    private ILogService logService;

    private IMailService mailService;

    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
    }

    public void destroy() {
        config = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    }

    protected boolean isAdmin(String userNameArg) {
        try {
            return getAuthenticationDao().isMemberOf(userNameArg, OpenTratsConstants.GROUP_ADMIN);
        } catch (AuthenticationDaoException ex) {
            ex.printStackTrace();
            getLogService().error(ex);
            return false;
        }
    }

    public IAuthenticationDao getAuthenticationDao() {
        return authenticationDao;
    }

    public void setAuthenticationDao(IAuthenticationDao authenticationDao) {
        this.authenticationDao = authenticationDao;
    }

    public ILogService getLogService() {
        return logService;
    }

    public void setLogService(ILogService logService) {
        this.logService = logService;
    }

    public IMailService getMailService() {
        return mailService;
    }

    public void setMailService(IMailService mailService) {
        this.mailService = mailService;
    }
}
