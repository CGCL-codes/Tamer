package org.opennms.dashboard.client;

/**
 * 
 * @author <a href="mailto:brozow@opennms.org">Mathew Brozowski</a>
 */
public interface Pageable {

    int getCurrentElement();

    void setCurrentElement(int element);

    int getPageSize();

    int getElementCount();
}
