package es.devel.opentrats.filter;

import es.devel.opentrats.constants.OpenTratsConstants;
import es.devel.opentrats.dao.exception.AuthenticationDaoException;
import es.devel.opentrats.model.User;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Fran Serrano
 */
public class BookingSecurityFilter extends OpenTratsSecurityFilter {

    @SuppressWarnings("unused")
    private FilterConfig config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;
    }

    @Override
    public void destroy() {
        config = null;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Logger.getRootLogger().debug("Se ha accedido al filtro de seguridad de la agenda");
        String remoteAddress = request.getRemoteAddr();
        Logger.getRootLogger().debug(remoteAddress);
        HttpServletRequest req = (HttpServletRequest) request;
        User user = (User) req.getSession().getAttribute("user");
        try {
            if (getAuthenticationDao().isMemberOf(user.getUser(), OpenTratsConstants.GROUP_BOOKING) == false) {
                getLogService().debug("#########################################################################################################");
                getLogService().debug("### Intento de acceso del usuario " + user.getUser() + " al m�dulo de agenda: Acceso denegado ###");
                getLogService().debug("#########################################################################################################");
                String messageBody = "<p>El usuario " + user.getUser() + " ha intenado acceder al modulo de agenda. No est&aacute; autorizado.</p>";
                getMailService().sendMail(new String[] { "fran@devel.es" }, "apps@devel.es", "OpenTrats: Filtro de seguridad de agenda", "Intento de acceso no autorizado: " + remoteAddress, messageBody);
                ((HttpServletResponse) response).sendRedirect("accessDenied.jsp");
            }
        } catch (AuthenticationDaoException ex) {
            getLogService().error(ex);
            ex.printStackTrace();
        }
        chain.doFilter(request, response);
    }
}
