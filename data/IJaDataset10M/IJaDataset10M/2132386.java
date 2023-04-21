package org.hip.vif.web.interfaces;

import org.osgi.framework.Bundle;

/**
 * Interface for classes configuring a task.
 * 
 * @author Luthiger
 * Created: 19.05.2011
 */
public interface ITaskConfiguration {

    /**
	 * 
	 * @return {@link Bundle} the bundle providing the task
	 */
    Bundle getBundle();

    /**
	 * 
	 * @return String the name of the task. The task class has to implement <code>IPluggableTask</code>
	 */
    String getTaskName();
}
