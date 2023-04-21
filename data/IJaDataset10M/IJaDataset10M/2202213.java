package org.mobicents.slee.runtime.eventrouter.routingtask;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedList;
import java.util.Set;
import javax.slee.EventTypeID;
import javax.slee.SLEEException;
import javax.slee.resource.FailureReason;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import org.apache.log4j.Logger;
import org.mobicents.slee.container.SleeContainer;
import org.mobicents.slee.container.SleeThreadLocals;
import org.mobicents.slee.container.activity.ActivityContext;
import org.mobicents.slee.container.activity.LocalActivityContext;
import org.mobicents.slee.container.component.event.EventTypeComponent;
import org.mobicents.slee.container.component.service.ServiceComponent;
import org.mobicents.slee.container.event.EventContext;
import org.mobicents.slee.container.eventrouter.EventRoutingTask;
import org.mobicents.slee.container.eventrouter.SbbInvocationState;
import org.mobicents.slee.container.sbb.SbbObject;
import org.mobicents.slee.container.sbbentity.SbbEntity;
import org.mobicents.slee.container.sbbentity.SbbEntityID;
import org.mobicents.slee.container.transaction.SleeTransactionManager;

public class EventRoutingTaskImpl implements EventRoutingTask {

    private static final Logger logger = Logger.getLogger(EventRoutingTaskImpl.class);

    private final SleeContainer container;

    /**
	 * 
	 * @author martins
	 *
	 */
    private static enum RoutingPhase {

        DELIVERING, DELIVERED
    }

    ;

    /**
	 * processing logic to handle a tx rollback
	 */
    private static final HandleRollback handleRollback = new HandleRollback();

    /**
	 * processing logic to handle a sbb tx rollback
	 */
    private static final HandleSbbRollback handleSbbRollback = new HandleSbbRollback();

    /**
	 * processing logic to handle an event as initial
	 */
    private static final InitialEventProcessor initialEventProcessor = new InitialEventProcessor();

    /**
	 * processing logic to retrieve next sbb entity to handle an event on an activity
	 */
    private static final NextSbbEntityFinder nextSbbEntityFinder = new NextSbbEntityFinder();

    /**
	 * 
	 */
    private final EventContext eventContext;

    /**
	 * indicates which phase we are in routing of event
	 */
    private RoutingPhase routingPhase = RoutingPhase.DELIVERING;

    /**
	 * 
	 * @param eventContext
	 */
    public EventRoutingTaskImpl(EventContext eventContext, SleeContainer sleeContainer) {
        this.eventContext = eventContext;
        this.container = sleeContainer;
    }

    public EventContext getEventContext() {
        return eventContext;
    }

    public void run() {
        if (System.getSecurityManager() != null) {
            AccessController.doPrivileged(new PrivilegedAction<Object>() {

                public Object run() {
                    routeQueuedEvent();
                    return null;
                }
            });
        } else {
            routeQueuedEvent();
        }
    }

    /**
	 * Delivers to SBBs an event off the top of the queue for an activity
	 * context
	 * 
	 * @param de
	 * @return true if the event processing suceeds
	 */
    private void routeQueuedEvent() {
        boolean debugLogging = logger.isDebugEnabled();
        final SleeTransactionManager txMgr = container.getTransactionManager();
        ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            final LocalActivityContext lac = eventContext.getLocalActivityContext();
            final EventRoutingTask activityCurrentEventRoutingTask = lac.getCurrentEventRoutingTask();
            EventContext activityCurrentEventContext = activityCurrentEventRoutingTask == null ? null : activityCurrentEventRoutingTask.getEventContext();
            if (activityCurrentEventContext == null) {
                if (debugLogging) logger.debug("\n\n\nStarting routing for " + eventContext);
                activityCurrentEventContext = eventContext;
                lac.setCurrentEventRoutingTask(this);
                EventTypeComponent eventTypeComponent = container.getComponentRepository().getComponentByID(eventContext.getEventTypeId());
                if (eventTypeComponent == null) {
                    logger.error("Unable to route event, the related component is not installed");
                    eventContext.eventProcessingFailed(FailureReason.OTHER_REASON);
                    return;
                }
                if (eventContext.getService() != null) {
                    ServiceComponent serviceComponent = container.getComponentRepository().getComponentByID(eventContext.getService());
                    if (eventTypeComponent.getActiveServicesWhichDefineEventAsInitial().contains(serviceComponent)) {
                        activityCurrentEventContext.getActiveServicesToProcessEventAsInitial().add(serviceComponent);
                    }
                } else {
                    Set<ServiceComponent> services = eventTypeComponent.getActiveServicesWhichDefineEventAsInitial();
                    if (services != null) {
                        activityCurrentEventContext.getActiveServicesToProcessEventAsInitial().addAll(services);
                    }
                }
            } else {
                if (activityCurrentEventContext.isSuspendedNotTransacted()) {
                    if (debugLogging) logger.debug("\n\n\nFreezing (due to suspended context) the routing for " + eventContext);
                    activityCurrentEventContext.barrierEvent(eventContext);
                    return;
                } else {
                    if (debugLogging) logger.debug("\n\n\nResuming the routing for" + eventContext);
                    Thread.sleep(10);
                }
            }
            LinkedList<ServiceComponent> serviceComponents = activityCurrentEventContext.getActiveServicesToProcessEventAsInitial();
            if (debugLogging) logger.debug("Active services which define " + eventContext.getEventTypeId() + " as initial: " + serviceComponents);
            boolean finished;
            SbbEntityID rootSbbEntityId;
            ClassLoader invokerClassLoader;
            SbbEntity sbbEntity;
            SbbObject sbbObject;
            ServiceComponent serviceComponent;
            boolean keepSbbEntityIfTxRollbacks;
            NextSbbEntityFinder.Result nextSbbEntityFinderResult;
            ActivityContext ac = null;
            Exception caught = null;
            Set<SbbEntityID> sbbEntitiesThatHandledCurrentEvent;
            boolean deliverEvent;
            boolean rollbackTx;
            boolean rollbackOnlySet;
            boolean sbbHandledEvent = false;
            do {
                rootSbbEntityId = null;
                invokerClassLoader = null;
                sbbEntity = null;
                sbbObject = null;
                serviceComponent = null;
                keepSbbEntityIfTxRollbacks = false;
                nextSbbEntityFinderResult = null;
                finished = false;
                ac = null;
                caught = null;
                deliverEvent = true;
                rollbackTx = true;
                rollbackOnlySet = false;
                try {
                    txMgr.begin();
                    sbbEntitiesThatHandledCurrentEvent = activityCurrentEventContext.getSbbEntitiesThatHandledEvent();
                    try {
                        ac = container.getActivityContextFactory().getActivityContext(eventContext.getActivityContextHandle(), true);
                        if (ac == null) {
                            logger.error("Unable to route event " + eventContext + ". The activity context is gone");
                            try {
                                eventContext.eventProcessingFailed(FailureReason.OTHER_REASON);
                                txMgr.commit();
                            } catch (Throwable e) {
                                logger.error(e.getMessage(), e);
                            }
                            return;
                        }
                        if (routingPhase == RoutingPhase.DELIVERING) {
                            try {
                                nextSbbEntityFinderResult = nextSbbEntityFinder.next(ac, eventContext, sbbEntitiesThatHandledCurrentEvent, container);
                            } catch (Exception e) {
                                logger.warn("Failed to find next sbb entity to deliver the event " + eventContext + " in " + ac.getActivityContextHandle(), e);
                            }
                            if (!serviceComponents.isEmpty()) {
                                serviceComponent = serviceComponents.getFirst();
                            }
                            if (nextSbbEntityFinderResult == null) {
                                if (serviceComponent != null) {
                                    if (debugLogging) logger.debug("No sbb entities attached, which didn't already route the event, but " + serviceComponent + " defines the event type as initial, starting initial event processing");
                                    serviceComponents.removeFirst();
                                    sbbEntity = initialEventProcessor.processInitialEvent(serviceComponent, eventContext, container, ac);
                                    if (sbbEntity == null && serviceComponents.isEmpty()) {
                                        finished = true;
                                    }
                                } else {
                                    finished = true;
                                    if (debugLogging) logger.debug("No sbb entities attached, which didn't already route the event, and no services left to process the event as initial");
                                }
                            } else {
                                if (serviceComponent != null && serviceComponent.getDescriptor().getDefaultPriority() >= nextSbbEntityFinderResult.sbbEntity.getPriority()) {
                                    if (debugLogging) logger.debug("Found an sbb entity attached, which didn't already route the event, but " + serviceComponent + " defines the event type as initial and has the same or higher priority, starting initial event processing");
                                    serviceComponents.removeFirst();
                                    sbbEntity = initialEventProcessor.processInitialEvent(serviceComponent, eventContext, container, ac);
                                } else {
                                    if (debugLogging) logger.debug("Found an sbb entity attached, which didn't already route the event, and either there are no more services, which defines the event type as initial, or their priorities is lower than the attached sbb entity found");
                                    sbbEntity = nextSbbEntityFinderResult.sbbEntity;
                                    deliverEvent = nextSbbEntityFinderResult.deliverEvent;
                                }
                            }
                        }
                        if (sbbEntity != null) {
                            sbbEntitiesThatHandledCurrentEvent.add(sbbEntity.getSbbEntityId());
                            if (debugLogging) {
                                logger.debug("Highest priority SBB entity, which is attached to the ac " + eventContext.getActivityContextHandle() + " , to deliver the event: " + sbbEntity.getSbbEntityId());
                            }
                            invokerClassLoader = sbbEntity.getSbbComponent().getClassLoader();
                            Thread.currentThread().setContextClassLoader(invokerClassLoader);
                            if (!sbbEntity.getSbbEntityId().isRootSbbEntity()) {
                                rootSbbEntityId = sbbEntity.getSbbEntityId().getRootSBBEntityID();
                            }
                            SleeThreadLocals.setInvokingService(sbbEntity.getSbbEntityId().getServiceID());
                            if (deliverEvent) {
                                sbbEntity.assignSbbObject();
                                sbbObject = sbbEntity.getSbbObject();
                                if (sbbEntity.isCreated() && !txMgr.getRollbackOnly()) {
                                    keepSbbEntityIfTxRollbacks = true;
                                }
                                Set<EventTypeID> eventMask = sbbEntity.getMaskedEventTypes(eventContext.getActivityContextHandle());
                                if (eventMask == null || !eventMask.contains(eventContext.getEventTypeId())) {
                                    sbbObject.setSbbInvocationState(SbbInvocationState.INVOKING_EVENT_HANDLER);
                                    ac.beforeDeliveringEvent(eventContext);
                                    if (debugLogging) {
                                        logger.debug("---> Invoking event handler: ac=" + eventContext.getActivityContextHandle() + " , sbbEntity=" + sbbEntity.getSbbEntityId() + " , sbbObject=" + sbbObject);
                                    }
                                    sbbEntity.invokeEventHandler(eventContext, ac, activityCurrentEventContext);
                                    sbbHandledEvent = true;
                                    if (debugLogging) {
                                        logger.debug("<--- Invoked event handler: ac=" + eventContext.getActivityContextHandle() + " , sbbEntity=" + sbbEntity.getSbbEntityId() + " , sbbObject=" + sbbObject);
                                    }
                                    rollbackOnlySet = txMgr.getRollbackOnly();
                                    if (!rollbackOnlySet) {
                                        sbbObject.setSbbInvocationState(SbbInvocationState.NOT_INVOKING);
                                    }
                                } else {
                                    if (debugLogging) {
                                        logger.debug("Not invoking event handler since event is masked");
                                    }
                                }
                            }
                            if (!rollbackOnlySet) {
                                if (eventContext.isActivityEndEvent() && activityCurrentEventContext.getSbbEntitiesThatHandledEvent().contains(sbbEntity.getSbbEntityId())) {
                                    if (debugLogging) {
                                        logger.debug("The event is an activity end event, detaching ac=" + eventContext.getActivityContextHandle() + " , sbbEntity=" + sbbEntity.getSbbEntityId());
                                    }
                                    ac.detachSbbEntity(sbbEntity.getSbbEntityId());
                                    sbbEntity.afterACDetach(eventContext.getActivityContextHandle());
                                }
                                if (rootSbbEntityId != null) {
                                    SbbEntity rootSbbEntity = container.getSbbEntityFactory().getSbbEntity(rootSbbEntityId, false);
                                    if (rootSbbEntity == null || rootSbbEntity.getAttachmentCount() != 0) {
                                        if (debugLogging) {
                                            logger.debug("Not removing sbb entity " + sbbEntity.getSbbEntityId() + " , the attachment count is not 0");
                                        }
                                        rootSbbEntityId = null;
                                    }
                                } else {
                                    if (!sbbEntity.isRemoved() && sbbEntity.getAttachmentCount() == 0) {
                                        if (debugLogging) {
                                            logger.debug("Removing sbb entity " + sbbEntity.getSbbEntityId() + " , the attachment count is not 0");
                                        }
                                        container.getSbbEntityFactory().removeSbbEntity(sbbEntity, true);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Caught exception while routing " + eventContext, e);
                        if (sbbEntity != null) {
                            sbbObject = sbbEntity.getSbbObject();
                        }
                        caught = e;
                    } catch (Throwable e) {
                        logger.error("Caught throwable while routing " + eventContext, e);
                        if (sbbEntity != null) {
                            sbbObject = sbbEntity.getSbbObject();
                        }
                        caught = new SLEEException("Caught throwable!", e);
                    }
                    if (!finished) {
                        if (serviceComponents.isEmpty()) {
                            try {
                                if (nextSbbEntityFinder.next(ac, eventContext, sbbEntitiesThatHandledCurrentEvent, container) == null) {
                                    finished = true;
                                }
                            } catch (Throwable e) {
                                if (debugLogging) {
                                    logger.debug("failed to get next attached sbb entity to handle event", e);
                                }
                            }
                        }
                    }
                    boolean invokeSbbRolledBack = handleRollback.handleRollback(sbbObject, caught, invokerClassLoader, txMgr);
                    boolean invokeSbbRolledBackRemove = false;
                    ClassLoader rootInvokerClassLoader = null;
                    SbbEntity rootSbbEntity = null;
                    if (!invokeSbbRolledBack && rootSbbEntityId != null) {
                        caught = null;
                        try {
                            rootSbbEntity = container.getSbbEntityFactory().getSbbEntity(rootSbbEntityId, false);
                            if (rootSbbEntity != null) {
                                container.getSbbEntityFactory().removeSbbEntity(rootSbbEntity, false);
                            }
                        } catch (Exception e) {
                            logger.error("Failure while routing event; third phase. Event Posting [" + eventContext + "]", e);
                            caught = e;
                        }
                        invokeSbbRolledBackRemove = handleRollback.handleRollback(null, caught, rootSbbEntity.getSbbComponent().getClassLoader(), txMgr);
                    }
                    if (invokeSbbRolledBack && sbbEntity == null) {
                        handleSbbRollback.handleSbbRolledBack(null, sbbObject, null, null, invokerClassLoader, false, container, false);
                    } else if (sbbEntity != null && !txMgr.getRollbackOnly() && sbbEntity.getSbbObject() != null) {
                        sbbObject.sbbStore();
                        sbbEntity.passivateAndReleaseSbbObject();
                    }
                    if (txMgr.getRollbackOnly()) {
                        if (debugLogging) {
                            logger.trace("Rolling back SLEE Originated Invocation Sequence");
                        }
                        txMgr.rollback();
                    } else {
                        if (finished) {
                            switch(routingPhase) {
                                case DELIVERING:
                                    if (eventContext.unreferencedCallbackRequiresTransaction()) {
                                        finished = false;
                                        routingPhase = RoutingPhase.DELIVERED;
                                        eventContext.getReferencesHandler().remove(eventContext.getActivityContextHandle());
                                    }
                                    break;
                                case DELIVERED:
                                    if (eventContext.unreferencedCallbackRequiresTransaction()) {
                                        finished = false;
                                        eventContext.getReferencesHandler().remove(eventContext.getActivityContextHandle());
                                    }
                                    break;
                                default:
                                    logger.error("Unknown routing phase!!!");
                                    break;
                            }
                        }
                        if (debugLogging) {
                            logger.trace("Committing SLEE Originated Invocation Sequence");
                        }
                        txMgr.commit();
                        if (routingPhase != RoutingPhase.DELIVERING) {
                            finished = true;
                        }
                    }
                    if (invokeSbbRolledBack && sbbEntity != null) {
                        if (debugLogging) {
                            logger.trace("Invoking sbbRolledBack for Op Only or Op and Remove");
                        }
                        handleSbbRollback.handleSbbRolledBack(sbbEntity, null, eventContext, ac, invokerClassLoader, false, container, keepSbbEntityIfTxRollbacks);
                    }
                    if (invokeSbbRolledBackRemove) {
                        handleSbbRollback.handleSbbRolledBack(rootSbbEntity, null, null, null, rootInvokerClassLoader, true, container, keepSbbEntityIfTxRollbacks);
                    }
                    rollbackTx = false;
                } catch (RuntimeException e) {
                    logger.error("Unhandled RuntimeException in event router: ", e);
                } catch (Exception e) {
                    logger.error("Unhandled Exception in event router: ", e);
                } catch (Error e) {
                    logger.error("Unhandled Error in event router: ", e);
                    throw e;
                } catch (Throwable t) {
                    logger.error("Unhandled Throwable in event router: ", t);
                } finally {
                    try {
                        final Transaction forgottenTx = txMgr.getTransaction();
                        if (forgottenTx != null) {
                            logger.error("HOUSTON WE HAVE A PROBLEM! Transaction " + forgottenTx + " left open in event routing.");
                            if (rollbackTx) {
                                txMgr.rollback();
                            } else {
                                txMgr.commit();
                            }
                        }
                    } catch (SystemException se) {
                        logger.error(se.getMessage(), se);
                    }
                    if (sbbEntity != null) {
                        if (debugLogging) {
                            logger.debug("Finished routing for " + eventContext + "\n\n\n");
                        }
                    }
                }
                SleeThreadLocals.setInvokingService(null);
                if (activityCurrentEventContext.isSuspendedNotTransacted()) {
                    if (debugLogging) logger.debug("Suspended routing for " + eventContext + "\n\n\n");
                    return;
                }
            } while (!finished);
            eventContext.eventProcessingSucceed(sbbHandledEvent);
            lac.setCurrentEventRoutingTask(null);
            if (!eventContext.unreferencedCallbackRequiresTransaction()) {
                eventContext.getReferencesHandler().remove(eventContext.getActivityContextHandle());
            }
        } catch (Exception e) {
            logger.error("Unhandled Exception in event router top try", e);
        }
        Thread.currentThread().setContextClassLoader(oldClassLoader);
    }
}
