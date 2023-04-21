package uk.icat3.logging.mdb;

import uk.icat3.logging.util.LoggingBeanRemote;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.log4j.Logger;
import uk.icat3.logging.entity.AdvInstrument;
import uk.icat3.logging.entity.AdvancedSearch;
import uk.icat3.logging.entity.LogInstrument;
import uk.icat3.logging.entity.Login;
import uk.icat3.logging.entity.Search;
import uk.icat3.logging.util.PropertyNames;
import uk.icat3.logging.util.QueueNames;

/**
 *
 * @author scb24683
 */
@MessageDriven(mappedName = QueueNames.RUN_NUMBER_QUEUE, activationConfig = { @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"), @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue") })
public class RunNumberSearchMDB implements MessageListener {

    @PersistenceContext(unitName = "icat3-logging")
    private EntityManager em;

    static Logger log;

    @EJB
    LoggingBeanRemote bean;

    public RunNumberSearchMDB() {
    }

    public RunNumberSearchMDB(EntityManager em) {
        this.em = em;
    }

    public void onMessage(Message message) {
        log = Logger.getLogger(RunNumberSearchMDB.class);
        try {
            log.info("=====================Run number message received=================");
            ObjectMessage msg = (ObjectMessage) message;
            String sessionId = msg.getStringProperty(PropertyNames.SESSION_ID);
            String userId = msg.getStringProperty(PropertyNames.USER_ID);
            String method = msg.getStringProperty(PropertyNames.METHOD);
            String time = msg.getStringProperty(PropertyNames.TIME);
            Timestamp searchTime = Timestamp.valueOf(time);
            Login login = em.find(Login.class, sessionId);
            Search search = new Search();
            search.setLogin(login);
            search.setSearchTime(searchTime);
            search.setMethod(method);
            search.setUserId(userId);
            em.persist(search);
            float startRun = msg.getFloatProperty(PropertyNames.START_RUN);
            float endRun = msg.getFloatProperty(PropertyNames.END_RUN);
            AdvancedSearch adv = new AdvancedSearch();
            adv.setSearch(search);
            adv.setRunStart(startRun);
            adv.setRunEnd(endRun);
            try {
                log.info("Getting other properties [will throw error if not found]");
                int startIndex = msg.getIntProperty(PropertyNames.START_INDEX);
                int noResults = msg.getIntProperty(PropertyNames.NO_RESULTS);
                adv.setStartIndex(new Long(startIndex));
                adv.setNoResults(new Long(noResults));
            } catch (Exception e) {
                log.warn("One or more of these properties does not exist");
            }
            em.persist(adv);
            ArrayList<String> instruments = (ArrayList<String>) msg.getObject();
            for (String ins : instruments) {
                LogInstrument i = bean.getInstrumentByName(ins);
                if (i == null) {
                    i = new LogInstrument();
                    i.setName(ins);
                    em.persist(i);
                } else {
                    AdvInstrument ai = new AdvInstrument();
                    ai.setAdvancedSearch(adv);
                    ai.setLogInstrument(i);
                    em.persist(ai);
                    log.trace("AdvInstrument completed successfully");
                }
            }
        } catch (Exception e) {
            log.fatal("Error in Run number message", e);
            e.printStackTrace();
        }
    }
}
