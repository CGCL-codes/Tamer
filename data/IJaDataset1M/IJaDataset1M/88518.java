package filtres;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import database.DataBaseRefKeywords;

public class FiltreNewsKeyword implements Filter {

    private FilterConfig config;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (config.getServletContext().getAttribute("newsKeyword") == null) {
            initNewsKeyword();
        }
        chain.doFilter(request, response);
    }

    private void initNewsKeyword() {
        ArrayList newsKeyword = new DataBaseRefKeywords().tabNewsKeyword();
        config.getServletContext().setAttribute("newsKeyword", newsKeyword);
    }

    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

    public void destroy() {
    }
}
