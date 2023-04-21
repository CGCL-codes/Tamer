package org.obe.spi;

import org.obe.client.api.WMClient;
import org.obe.client.api.WMClientFactory;
import org.obe.spi.event.ApplicationEvent;
import org.obe.spi.service.ServiceManager;
import org.wfmc.wapi.WMWorkflowException;

/**
 * Provides local service provider interfaces.  The local client returned by
 * {@link WMClientFactory#createClient(String)} can be cast to this interface in
 * order to access internal services.
 *
 * @author Adrian Price
 */
public interface WMLocalClient extends WMClient {

    /**
     * Provides access to OBE internal services.
     *
     * @return The service manager.
     */
    ServiceManager getServiceManager();

    /**
     * Injects a correlated application event into the workflow engine.
     *
     * @param event           The application event.
     * @param correlationKeys Correlation keys supplied to the
     *                        <code>ApplicationEventBroker</code> by the original subscription.
     * @throws WMWorkflowException
     */
    void raiseEvent(ApplicationEvent event, String[] correlationKeys) throws WMWorkflowException;

    /**
     * Refreshes the work items for all open activities in a process instance.
     * For each open activity, it re-resolves the performer list against the
     * participant repository, and ensures that work items exist for all
     * assigness of the activity.
     *
     * @param processInstanceId The ID of the process instance for which work
     *                          items are to be refreshed.
     * @throws WMWorkflowException
     */
    void refreshWorkItems(String processInstanceId) throws WMWorkflowException;
}
