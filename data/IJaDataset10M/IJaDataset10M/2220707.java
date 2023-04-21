package org.obe.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.obe.spi.event.TransitionEvent;
import org.obe.spi.event.TransitionListener;
import org.obe.spi.model.ActivityInstance;
import org.obe.spi.service.WorkflowEventBroker;
import org.obe.xpdl.model.transition.Transition;

/**
 * Supports transition listeners.  This class maintains a list of
 * {@link TransitionListener}s, and provides a set of
 * <code>fireTransition&lt;Event&gt;(String source,
 * {@link Transition} defn)</code> methods to notify the listeners of events.
 *
 * @author Adrian Price
 */
public final class TransitionListenerSupport extends AbstractListenerSupport {

    private static final Log _logger = LogFactory.getLog(TransitionListenerSupport.class);

    private static final String[] NOTIFICATION_METHODS = { "transitionFired" };

    public TransitionListenerSupport(WorkflowEventBroker eventBroker) {
        super(eventBroker, TransitionEvent.class, TransitionListener.class, NOTIFICATION_METHODS);
    }

    public void fireTransitionEvent(ActivityInstance activityInstance, int id, Transition defn) {
        fire(activityInstance, id, defn, null);
    }

    public void fireTransitionFired(ActivityInstance activityInstance, Transition defn) {
        fire(activityInstance, TransitionEvent.FIRED, defn, null);
    }

    public Log getLogger() {
        return _logger;
    }
}
