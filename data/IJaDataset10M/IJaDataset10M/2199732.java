package org.ikasan.framework.event.exclusion.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;
import org.apache.log4j.Logger;
import org.ikasan.framework.component.Event;
import org.ikasan.framework.error.model.ErrorOccurrence;
import org.ikasan.framework.error.service.ErrorLoggingService;
import org.ikasan.framework.event.exclusion.dao.ExcludedEventDao;
import org.ikasan.framework.event.exclusion.model.ExcludedEvent;
import org.ikasan.framework.flow.Flow;
import org.ikasan.framework.flow.invoker.FlowInvocationContext;
import org.ikasan.framework.initiator.AbortTransactionException;
import org.ikasan.framework.initiator.Initiator;
import org.ikasan.framework.management.search.PagedSearchResult;
import org.ikasan.framework.module.Module;
import org.ikasan.framework.module.service.ModuleService;

/**
 * @author The Ikasan Development Service
 *
 */
public class ExcludedEventServiceImpl implements ExcludedEventService {

    private List<ExcludedEventListener> excludedEventListeners = new ArrayList<ExcludedEventListener>();

    private ExcludedEventDao excludedEventDao;

    private ErrorLoggingService errorLoggingService;

    private ModuleService moduleService;

    /**
	 * Only used for debugging the transaction status
	 */
    private TransactionManager transactionManager;

    private Logger logger = Logger.getLogger(ExcludedEventServiceImpl.class);

    /**
	 * @param excludedEventDao
	 * @param errorLoggingService
	 * @param listeners
	 * @param moduleService
	 */
    public ExcludedEventServiceImpl(ExcludedEventDao excludedEventDao, ErrorLoggingService errorLoggingService, List<ExcludedEventListener> listeners, ModuleService moduleService) {
        this.excludedEventDao = excludedEventDao;
        excludedEventListeners.addAll(listeners);
        this.moduleService = moduleService;
        this.errorLoggingService = errorLoggingService;
    }

    public void excludeEvent(Event event, String moduleName, String flowName) {
        Date exclusionTime = new Date();
        logger.info("excluding event from module:" + moduleName + ", flow:" + flowName);
        excludedEventDao.save(new ExcludedEvent(event, moduleName, flowName, exclusionTime));
        for (ExcludedEventListener excludedEventListener : excludedEventListeners) {
            excludedEventListener.notifyExcludedEvent(event);
        }
    }

    public PagedSearchResult<ExcludedEvent> getExcludedEvents(int pageNo, int pageSize, String orderBy, boolean orderAscending, String moduleName, String flowName) {
        if (pageNo < 0) {
            throw new IllegalArgumentException("pageNo must be >= 0");
        }
        if (pageSize < 1) {
            throw new IllegalArgumentException("pageSize must be > 0");
        }
        return excludedEventDao.findExcludedEvents(pageNo, pageSize, orderBy, orderAscending, moduleName, flowName);
    }

    public ExcludedEvent getExcludedEvent(String eventId) {
        ExcludedEvent excludedEvent = excludedEventDao.getExcludedEvent(eventId, false);
        if (excludedEvent != null) {
            List<ErrorOccurrence> errorOccurrences = errorLoggingService.getErrorOccurrences(eventId);
            excludedEvent.setErrorOccurrences(errorOccurrences);
        }
        return excludedEvent;
    }

    public void resubmit(String eventId, String resubmitter) {
        logger.info("resubmit called with eventId [" + eventId + "], resubmitter [" + resubmitter + "]");
        if (transactionManager != null) {
            try {
                int status = transactionManager.getStatus();
                if (Status.STATUS_ACTIVE != status) {
                    logger.warn("Warning! Resubmission invoked outside of an active transaction!");
                }
            } catch (SystemException e) {
                logger.error(e);
            }
        }
        ExcludedEvent excludedEvent = excludedEventDao.getExcludedEvent(eventId, false);
        if (excludedEvent == null) {
            throw new IllegalArgumentException("Cannot find Excluded Event id:" + eventId);
        }
        if (excludedEvent.isResolved()) {
            throw new IllegalStateException("Attempt made to resubmit event:" + eventId);
        }
        Module module = moduleService.getModule(excludedEvent.getModuleName());
        if (module == null) {
            throw new IllegalArgumentException("unknown Module:" + excludedEvent.getModuleName());
        }
        Flow flow = module.getFlows().get(excludedEvent.getFlowName());
        if (flow == null) {
            throw new IllegalArgumentException("unknown Flow" + excludedEvent.getFlowName());
        }
        for (Initiator initiator : module.getInitiators()) {
            logger.info("considering initiator [" + initiator + "]");
            if (initiator.getFlow().equals(flow)) {
                logger.info("matched flow [" + flow + "]");
                if (!initiator.isRunning()) {
                    throw new IllegalStateException("Cannot resubmit to Flow [" + flow.getName() + "] as not all Initiators are running");
                }
            }
        }
        logger.info("all good!");
        Event event = excludedEvent.getEvent();
        FlowInvocationContext flowInvocationContext = new FlowInvocationContext();
        try {
            flow.invoke(flowInvocationContext, event);
        } catch (Throwable throwable) {
            try {
                Event clonedEvent = event.clone();
                errorLoggingService.logError(throwable, excludedEvent.getModuleName(), excludedEvent.getFlowName(), flowInvocationContext.getLastComponentName(), clonedEvent, null);
            } catch (CloneNotSupportedException e) {
            }
            throw new AbortTransactionException("Resubmission failed!", throwable);
        }
        excludedEvent = excludedEventDao.getExcludedEvent(eventId, true);
        excludedEvent.resolveAsResubmitted(resubmitter);
        excludedEventDao.save(excludedEvent);
    }

    public void cancel(String eventId, String canceller) {
        ExcludedEvent excludedEvent = excludedEventDao.getExcludedEvent(eventId, true);
        if (excludedEvent == null) {
            throw new IllegalArgumentException("Cannot find Excluded Event id:" + eventId);
        }
        if (excludedEvent.isResolved()) {
            throw new IllegalStateException("Attempt made to resubmit event:" + eventId);
        }
        Module module = moduleService.getModule(excludedEvent.getModuleName());
        if (module == null) {
            throw new IllegalArgumentException("unknown Module:" + excludedEvent.getModuleName());
        }
        excludedEvent.resolveAsCancelled(canceller);
        excludedEventDao.save(excludedEvent);
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
