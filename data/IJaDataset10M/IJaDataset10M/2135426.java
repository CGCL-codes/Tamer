package de.powerstaff.web.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class RedirectToStartPageFilter implements Filter {

    private static final String FIRST_USE_SESSION_TOKEN = "RedirectToStartPageFilter";

    private String startPage;

    public void destroy() {
    }

    public void doFilter(ServletRequest aRequest, ServletResponse aResponse, FilterChain aChain) throws IOException, ServletException {
        HttpServletRequest theHttpRequest = (HttpServletRequest) aRequest;
        HttpServletResponse theHttpResponse = (HttpServletResponse) aResponse;
        HttpSession theSession = theHttpRequest.getSession();
        if (theSession.getAttribute(FIRST_USE_SESSION_TOKEN) == null) {
            theSession.setAttribute(FIRST_USE_SESSION_TOKEN, Boolean.TRUE);
            theHttpResponse.sendRedirect(theHttpRequest.getContextPath() + startPage);
            return;
        }
        aChain.doFilter(aRequest, aResponse);
    }

    public void init(FilterConfig aConfig) throws ServletException {
        startPage = aConfig.getInitParameter("startPage");
    }

    /**
     * @return the startPage
     */
    public String getStartPage() {
        return startPage;
    }

    /**
     * @param startPage
     *                the startPage to set
     */
    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }
}
