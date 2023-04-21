package flex.management.runtime.messaging.services;

import java.io.IOException;
import java.util.Date;
import flex.management.BaseControlMBean;

/**
 * Defines the runtime monitoring and management interface for service adapters.
 *
 * @author shodgson
 */
public interface ServiceAdapterControlMBean extends BaseControlMBean {

    /**
     * Returns <code>true</code> if the <code>ServiceAdapter</code> is running.
     *
     * @return <code>true</code> if the <code>ServiceAdapter</code> is running.
     * @throws IOException Throws IOException.
     */
    Boolean isRunning() throws IOException;

    /**
     * Returns the start timestamp for the <code>ServiceAdapter</code>.
     *
     * @return The start timestamp for the <code>ServiceAdapter</code>.
     * @throws IOException Throws IOException.
     */
    Date getStartTimestamp() throws IOException;
}
