package edu.webteach.web;

import java.io.*;
import javax.servlet.*;
import javax.servlet.Filter;
import org.apache.commons.logging.*;
import org.hibernate.*;
import edu.webteach.*;

/**
 * Filter that manages a Hibernate Session for a request.
 * <p>
 * This filter should be used if your <tt>hibernate.current_session_context_class</tt>
 * configuration is set to <tt>thread</tt> and you are not using JTA or CMT.
 * <p>
 * With JTA you'd replace transaction demarcation with calls to the <tt>UserTransaction</tt> API.
 * With CMT you would remove transaction demarcation code from this filter.
 * <p>
 * An alternative, more flexible solution is <tt>SessionTransactionInterceptor</tt>
 * that can be applied to any pointcut with JBoss AOP.
 * <p>
 * Note that you should not use this interceptor out-of-the-box with enabled optimistic
 * concurrency control. Apply your own compensation logic for failed conversations, this
 * is totally dependent on your applications design.
 *
 * @see auction.persistence.SessionTransactionInterceptor
 *
 * @author Christian Bauer
 */
public class HibernateSessionRequestFilter implements Filter {

    private static Log log = LogFactory.getLog(HibernateSessionRequestFilter.class);

    private SessionFactory sf;

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            if (sf.getCurrentSession() == null) log.error("sf.getCurrentSession() == null");
            sf.getCurrentSession().beginTransaction();
            chain.doFilter(request, response);
            sf.getCurrentSession().getTransaction().commit();
        } catch (StaleObjectStateException staleEx) {
            log.error("This interceptor does not implement optimistic concurrency control!");
            log.error("Your application will not work until you add compensation actions!");
            throw staleEx;
        } catch (Throwable ex) {
            ex.printStackTrace();
            try {
                if (sf.getCurrentSession().getTransaction().isActive()) {
                    log.debug("Trying to rollback database transaction after exception");
                    sf.getCurrentSession().getTransaction().rollback();
                }
            } catch (Throwable rbEx) {
                log.error("Could not rollback transaction after exception!", rbEx);
            }
            throw new ServletException(ex);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("Initializing Hibernate filter...");
        sf = Context.getSessionFactory();
    }

    public void destroy() {
    }
}
