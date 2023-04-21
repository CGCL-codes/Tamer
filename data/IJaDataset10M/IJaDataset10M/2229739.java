package uk.ac.dl.dp.core.sessionbeans.session;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.ejb.EJB;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.apache.log4j.*;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import uk.ac.dl.dp.core.sessionbeans.SessionEJBObject;
import uk.ac.dl.dp.coreutil.exceptions.DataPortalException;
import uk.ac.dl.dp.coreutil.interfaces.LookupLocal;
import uk.ac.dl.dp.coreutil.interfaces.TimerServiceLocal;
import uk.ac.dl.dp.coreutil.interfaces.TimerServiceRemote;
import uk.ac.dl.dp.core.message.query.QueryManager;
import uk.ac.dl.dp.coreutil.entity.Session;
import uk.ac.dl.dp.coreutil.util.QueryRecord;
import uk.ac.dl.dp.coreutil.util.SessionUtil;
import uk.ac.dl.dp.coreutil.util.DataPortalConstants;

@Stateless(mappedName = DataPortalConstants.TIMER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class TimerServiceBean extends SessionEJBObject implements TimerServiceLocal, TimerServiceRemote {

    @Resource
    TimerService timerService;

    @EJB()
    LookupLocal lookup;

    static Logger log = Logger.getLogger(TimerServiceBean.class);

    static boolean timerCreated = false;

    public void createTimer(long starttime, long intervalDuration) {
        log.debug("createTimer()");
        if (!timerCreated) {
            log.debug("Creating timer.");
            Timer timer = timerService.createTimer(starttime, intervalDuration, "Session clean up timer");
            timerCreated = true;
        }
    }

    public void startTimeouts(Timer timer) {
        log.debug("startTimeouts()");
        try {
            timeoutSession(timer);
        } finally {
        }
        try {
            timeoutQueryManager(timer);
        } finally {
        }
        try {
            try {
                timeoutKeyword(timer);
            } catch (Exception ex) {
                log.error("Error with keywords: ", ex);
            }
        } finally {
        }
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void timeoutSession(Timer timer) {
        log.debug("Timeout occurred: timeoutSession()");
        Collection<Session> sessions = null;
        try {
            sessions = (Collection<Session>) em.createNamedQuery("Session.findAll").getResultList();
        } catch (Exception e) {
            log.warn("Error with query in Timer", e);
            return;
        }
        log.debug("Number sessions returned is " + sessions.size());
        Calendar now = GregorianCalendar.getInstance();
        for (Session ses : sessions) {
            Date date = ses.getExpireDateTime();
            GregorianCalendar expire = new GregorianCalendar();
            expire.setTime(date);
            if (now.after(expire)) {
                em.remove(ses);
                log.info("Remove old session: " + ses.getUserSessionId() + " for user " + ses.getUserId().getDn());
            }
        }
    }

    @Timeout
    public void timeoutQueryManager(Timer timer) {
        log.info("Timeout occurred: timeoutQueryManager()");
        Collection<Collection<QueryRecord>> ccqr = QueryManager.getAll();
        for (Collection<QueryRecord> cqr : ccqr) {
            QueryRecord qr = cqr.iterator().next();
            String sid = qr.getSid();
            String queryId = qr.getQueryid();
            try {
                new SessionUtil(sid, em);
            } catch (DataPortalException ex) {
                log.info("Remove old query from cache: " + queryId);
                QueryManager.removeRecord(queryId);
            }
        }
    }

    public void downloadKeywords() throws Exception {
        log.info("downloadKeywords");
    }

    public void timeoutKeyword(Timer timer) throws Exception {
        downloadKeywords();
    }

    public void removeSessionFromQueryCache(String sid) {
        Collection<String> qr_ids = QueryManager.getUserQueryIds(sid, em);
        for (String ids : qr_ids) {
            log.info("Remove old query from cache: " + ids);
            QueryManager.removeRecord(ids);
        }
    }
}
