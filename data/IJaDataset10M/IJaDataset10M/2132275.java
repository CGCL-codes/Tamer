package com.stehno.esm.service;

import org.springframework.core.io.Resource;
import com.stehno.esm.event.IServiceStatusEventListener;

public interface IEmbeddedService {

    /**
	 * Used to retrieve the human-friendly label for the service. This will be displayed
	 * in the service manager.
	 *
	 * @return the human-friendly label for the service
	 */
    public String getLabel();

    public boolean isRunning();

    public void addServiceStatusListener(IServiceStatusEventListener listener);

    /**
	 * Used to retrieve the icon representing this service. If no icon is specified a default
	 * icon will be used.
	 *
	 * @return the icon used to represent this service (or null)
	 */
    public Resource getIcon();

    /**
	 * Used to retrieve the priority setting for this service. A higher priority setting will 
	 * cause the service to be loaded before those with a lower priority. The property must be
	 * specified as a positive integer (0 is acceptable).
	 *
	 * @return the priority setting for the service
	 */
    public int getPriority();

    /**
	 * Used to start the service.
	 */
    public void start();

    /**
	 * Used to stop the service.
	 */
    public void stop();
}
