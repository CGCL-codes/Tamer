package net.livebookstore.raw.hibernate;

import org.aopalliance.intercept.*;
import org.apache.commons.logging.*;
import org.hibernate.Transaction;

/**
 * To do hibernate transaction interceptor. This interceptor is another 
 * alternative way to handle transaction if do not use OpenSessionInView 
 * pattern. Not used in livebookstore.
 * 
 * @author xuefeng
 * 
 * #spring.bean id="hibernateTransactionInterceptor"
 * 
 * @deprecated
 */
public class HibernateTransactionInterceptor implements MethodInterceptor {

    private Log log = LogFactory.getLog(getClass());

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object ret = null;
        Transaction tx = null;
        try {
            log.info("Hibernate transaction forcurrent session: about to begin...");
            tx = HibernateUtil.getCurrentSession().beginTransaction();
            ret = invocation.proceed();
            log.info("Hibernate transaction for current session: about to commit...");
            tx.commit();
        } catch (Throwable t) {
            if (tx != null) {
                log.info("Hibernate transaction for current session: about to rollback...", t);
                tx.rollback();
            }
            throw t;
        }
        return ret;
    }
}
