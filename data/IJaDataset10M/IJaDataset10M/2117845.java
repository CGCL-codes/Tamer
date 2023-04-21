package es.seat131.javi.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import es.seat131.javi.common.Action;
import es.seat131.javi.common.ActionForward;
import es.seat131.javi.common.ParamKey;
import es.seat131.javi.config.ApplicationProperties;
import es.seat131.javi.dto.UserDto;

public class LoginFilter implements Filter {

    private FilterConfig filterConfig = null;

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request_, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) request_;
        String login = request.getParameter(ParamKey.ACTION.toString());
        if (login != null && login.equals(Action.LOGIN.toString()) || (request.getRequestURL().toString() != null && request.getRequestURL().toString().endsWith("css")) || (request.getRequestURL().toString() != null && request.getRequestURL().toString().endsWith("jpg")) || (request.getRequestURL().toString() != null && request.getRequestURL().toString().endsWith("gif"))) {
            chain.doFilter(request, response);
        } else if (!validateUser(request, response)) {
            request.getRequestDispatcher(ActionForward.LOGIN.getPage()).forward(request, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    public void init(FilterConfig arg0) throws ServletException {
        this.filterConfig = filterConfig;
        try {
            ApplicationProperties.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean validateUser(ServletRequest request, ServletResponse response) {
        HttpSession httpSession = ((HttpServletRequest) request).getSession(true);
        UserDto usuario = (UserDto) httpSession.getAttribute(ParamKey.USUARIO.toString());
        if (usuario == null) {
            return false;
        }
        return true;
    }
}
