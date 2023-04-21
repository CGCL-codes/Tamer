package org.obe.event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.obe.spi.event.AttributeInstanceEvent;
import org.obe.spi.event.AttributeInstanceListener;
import org.obe.spi.model.AttributeInstance;
import org.obe.spi.service.WorkflowEventBroker;
import org.obe.xpdl.model.data.DataField;

/**
 * Supports attribute instance listeners.  This class maintains a list of
 * {@link AttributeInstanceListener}s, and provides a set of
 * <code>fireAttributeInstance&lt;Event&gt;({@link AttributeInstance} source,
 * {@link DataField} definition)</code> methods to notify the listeners of
 * events.
 *
 * @author Adrian Price
 */
public final class AttributeInstanceListenerSupport extends AbstractListenerSupport {

    private static final Log _logger = LogFactory.getLog(AttributeInstanceListenerSupport.class);

    private static final String[] NOTIFICATION_METHODS = { "attributeInstanceCreated", "attributeInstanceDeleted", "attributeInstanceUpdated" };

    public AttributeInstanceListenerSupport(WorkflowEventBroker eventBroker) {
        super(eventBroker, AttributeInstanceEvent.class, AttributeInstanceListener.class, NOTIFICATION_METHODS);
    }

    public void fireAttributeInstanceCreated(AttributeInstance source, DataField definition) {
        fire(source, AttributeInstanceEvent.CREATED, definition, null);
    }

    public void fireAttributeInstanceDeleted(AttributeInstance source, DataField definition) {
        fire(source, AttributeInstanceEvent.DELETED, definition, null);
    }

    public void fireAttributeInstanceUpdated(AttributeInstance source, DataField definition, Object previousValue) {
        fire(source, AttributeInstanceEvent.UPDATED, definition, previousValue);
    }

    public Log getLogger() {
        return _logger;
    }
}
