package org.qtitools.qti.ext;

/**
 * Interface for classes that want to hear about Lifecycle events.
 * 
 * @author  David McKain
 * @version $Revision: 2381 $
 */
public interface LifecycleListener {

    void lifecycleEvent(LifecycleEventType eventType);
}
