package tinlizard.dao.jpa;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.log4j.Logger;

/**
 * Servlet filter to clean up EntityManager instance at the end of a request.
 *
 * XXX: research http://www.redhat.com/docs/en-US/JBoss_Enterprise_Application_Platform/4.3.0.cp04/html/Entity_Manager_User_Guide/Transactions_and_Concurrency.html
 * XXX: may be overkill for any AJaX, or make smarter, skip /resources/*
 *
 */
public final class JpaFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(JpaFilter.class);

    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("PRE");
        }
        chain.doFilter(request, response);
        JpaDao dao = JpaDao.getInstance();
        if (dao != null) {
            dao.closeEm();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("POST");
        }
    }
}
