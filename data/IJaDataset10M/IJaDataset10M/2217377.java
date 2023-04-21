package com.inet.qlcbcc.hibernate.task;

import java.util.Calendar;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.webos.repository.hibernate.AfterSessionFactoryCreation;
import org.webos.repository.hibernate.util.HibernateCallbackException;
import com.inet.qlcbcc.service.counter.CounterService;

/**
 * PreloadComplaintCounterTask.
 *
 * @author Duyen Tang
 * @version $Id: PreloadComplaintCounterTask.java 2011-11-30 14:38:21z tttduyen $
 *
 * @since 1.0
 */
public class PreloadConstructingCounterTask implements AfterSessionFactoryCreation {

    private static final Logger LOG = LoggerFactory.getLogger(PreloadConstructingCounterTask.class);

    @Autowired
    private CounterService counterService;

    public PreloadConstructingCounterTask() {
    }

    public void doTask(SessionFactory sessionFactory) throws HibernateCallbackException {
        final Session session = SessionFactoryUtils.getSession(sessionFactory, true);
        counterService.put(getMaxNumber(session));
        SessionFactoryUtils.closeSession(session);
    }

    /**
   * Get the maximize number of {@link ComplaintInfo}
   * 
   * @param session the given {@link Session}
   * @return the maximize number
   */
    private long getMaxNumber(Session session) {
        try {
            Query query = session.createQuery("SELECT MAX(ser.number) FROM com.inet.qlcbcc.domain.ServantsInfo ser");
            return (Long) query.uniqueResult();
        } catch (Exception ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not get maximize number of complaint information from system", ex);
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("Could not get maximize number of complaint information from system with exception message [{}]", ex.getMessage());
            }
            return 0;
        }
    }
}
