package org.nexopenframework.management.monitor.spi;

import org.nexopenframework.management.monitor.events.MonitorableEvent;
import org.nexopenframework.management.monitor.jmx.ActivatorMBean;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @see org.nexopenframework.management.monitor.events.MonitorableEvent
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public interface EventNotifier extends ActivatorMBean {

    /**
	 * <p></p>
	 * 
	 * @param event
	 */
    void sendEvent(final MonitorableEvent event);
}
