package net.sourceforge.gateway.sstp.faces.filters;

import net.sourceforge.gateway.sstp.constants.Constants;
import net.sourceforge.gateway.sstp.databases.RoleManager;
import net.sourceforge.gateway.sstp.databases.tables.UserDTO;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import java.io.IOException;

public class AuthorizationFilter implements Filter {

    protected static final Logger LOG = Logger.getLogger(AuthorizationFilter.class.getName());

    FilterConfig config = null;

    ServletContext servletContext = null;

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
        servletContext = config.getServletContext();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpSession session = httpServletRequest.getSession();
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO == null) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.faces");
        } else {
            String URI = ((HttpServletRequest) servletRequest).getRequestURI();
            if (URI.contains(Constants.ADMIN) && RoleManager.hasAdminRole(userDTO)) {
                chain.doFilter(servletRequest, servletResponse);
            } else if (URI.contains(Constants.TAXSTAFF) && RoleManager.hasTaxStaffRole(userDTO)) {
                chain.doFilter(servletRequest, servletResponse);
            } else if (URI.contains(Constants.REGISTRATION) && RoleManager.hasRegistrarRole(userDTO)) {
                chain.doFilter(servletRequest, servletResponse);
            } else if (URI.contains(Constants.TRANSMITTER) && RoleManager.hasTransmitterRole(userDTO)) {
                chain.doFilter(servletRequest, servletResponse);
            } else if (URI.equals(Constants.MAIN)) {
                chain.doFilter(servletRequest, servletResponse);
            } else {
                httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/login.faces");
            }
        }
    }

    /**
     * Ignored. Empty method body needed to fully implement Filter.
     */
    public void destroy() {
    }
}
