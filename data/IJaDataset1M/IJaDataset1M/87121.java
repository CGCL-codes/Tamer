package com.intrigueit.myc2i.common.filter;

import java.io.IOException;
import javax.faces.application.ViewExpiredException;
import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.intrigueit.myc2i.common.Myc2iException;

public class ErrorReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String INIT_PARAM_ERROR_PAGE = "errorPage";

    private FacesServlet delegate;

    private String errorPage;

    public static final String ERROR_CAUSE = "error_message";

    public static final String ERROR_LEVEL = "error_level";

    public static final String ERROR_REMEDY = "error_remedy";

    public void init(ServletConfig servletConfig) throws ServletException {
        delegate = new FacesServlet();
        delegate.init(servletConfig);
        errorPage = servletConfig.getInitParameter(INIT_PARAM_ERROR_PAGE);
    }

    public void destroy() {
        delegate.destroy();
    }

    public ServletConfig getServletConfig() {
        return delegate.getServletConfig();
    }

    public String getServletInfo() {
        return delegate.getServletInfo();
    }

    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        try {
            delegate.service(request, response);
        } catch (Throwable e) {
            ((HttpServletRequest) request).getSession().setAttribute(ERROR_LEVEL, 3);
            if (e.getCause() instanceof ViewExpiredException) {
                ((HttpServletRequest) request).getSession().setAttribute(ERROR_LEVEL, 100);
                ((HttpServletRequest) request).getSession().setAttribute(ERROR_CAUSE, "Your session has been expired");
                ((HttpServletRequest) request).getSession().setAttribute(ERROR_REMEDY, "Please go to the login page and login again");
            } else if (e.getCause() instanceof Myc2iException) {
                ((HttpServletRequest) request).getSession().setAttribute(ERROR_CAUSE, "error.page.agent.error");
                ((HttpServletRequest) request).getSession().setAttribute(ERROR_REMEDY, "Contact myc2i support team");
            } else if (e.getCause() instanceof ServletException) {
                ((HttpServletRequest) request).getSession().setAttribute(ERROR_CAUSE, "error.page.servlet.error");
                ((HttpServletRequest) request).getSession().setAttribute(ERROR_REMEDY, "Servlet error");
            } else {
                ((HttpServletRequest) request).getSession().setAttribute(ERROR_CAUSE, "error.page.unknown.error");
                ((HttpServletRequest) request).getSession().setAttribute(ERROR_REMEDY, "Unspecified error");
            }
            e.printStackTrace();
            redirectToErrorPage((HttpServletRequest) request, (HttpServletResponse) response);
        }
    }

    private void redirectToErrorPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!"".equals(errorPage)) {
            response.sendRedirect(request.getContextPath() + errorPage);
        }
    }
}
