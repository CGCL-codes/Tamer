package org.nexopenframework.core.scheduling.commonj.management;

import org.nexopenframework.core.scheduling.management.SchedulerManagedObject;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Comment here</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public interface CommonJManagedObjectMBean extends SchedulerManagedObject {

    long getPeriod();

    long getDelay();
}
