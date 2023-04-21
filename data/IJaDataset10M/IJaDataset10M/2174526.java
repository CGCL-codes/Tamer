package it.webscience.kpeople.service.report;

import java.util.List;
import it.webscience.kpeople.bll.exception.KPeopleBLLException;
import it.webscience.kpeople.bll.impl.EventManager;
import it.webscience.kpeople.bll.impl.ProcessEventManager;
import it.webscience.kpeople.service.datatypes.Event;
import it.webscience.kpeople.service.datatypes.ProcessEvent;
import it.webscience.kpeople.service.datatypes.converter.EventConverter;
import it.webscience.kpeople.service.datatypes.converter.EventFilterConverter;
import it.webscience.kpeople.service.datatypes.converter.ProcessEventConverter;
import it.webscience.kpeople.service.datatypes.converter.UserConverter;
import it.webscience.kpeople.service.exception.KPeopleServiceException;
import org.apache.log4j.Logger;

public class ProcessEventService {

    /** logger. */
    private Logger logger;

    /**
     * Costruttore della classe.
     */
    public ProcessEventService() {
        logger = Logger.getLogger(this.getClass().getName());
    }

    public final ProcessEvent[] getProcessEvents() throws KPeopleServiceException {
        if (logger.isDebugEnabled()) {
            logger.debug("getProcessEvents - Begin");
        }
        ProcessEventManager mng = new ProcessEventManager();
        ProcessEvent[] processEvents = null;
        try {
            List<it.webscience.kpeople.be.ProcessEvent> processEventsBe = mng.getProcessEvents();
            processEvents = ProcessEventConverter.toService(processEventsBe);
        } catch (KPeopleBLLException e) {
            e.printStackTrace();
            throw new KPeopleServiceException(e.getMessage());
        }
        return processEvents;
    }
}
