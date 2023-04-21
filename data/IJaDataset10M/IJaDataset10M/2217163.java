package edu.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import edu.DAOImpl.HibernateUtil;

/**
 * @author Administrator
 *
 */
public class CloseSessionWithCommitFilter implements Filter {

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter((HttpServletRequest) request, (HttpServletResponse) response);
        } finally {
            try {
                HibernateUtil.commitTransaction();
            } catch (Exception e) {
                HibernateUtil.rollbackTransaction();
            } finally {
                HibernateUtil.closeSession();
            }
        }
    }

    public void init(FilterConfig arg0) throws ServletException {
    }
}
