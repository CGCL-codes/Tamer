package joj.core;

import joj.web.core.RequestExecutionContext;
import org.picocontainer.MutablePicoContainer;

public interface Modules {

    /**
	 * Retrieve the container instance for a requesting module,
	 * hooked into the modules container.
	 * @return
	 */
    MutablePicoContainer createModuleContainer();

    /**
	 * <p>
	 * Takes a partially populated request execution context and attempts
	 * to do something with it.
	 * </p>
	 * @param request The {@link RequestExecutionContext} of the request
	 * @return <code>true if the request was satisfied, <code>false</code> otherwise.
	 */
    boolean findAndExecute(RequestExecutionContext request) throws Exception;

    /**
	 * Returns the number of active modules in the system.
	 * The count may be inaccurate, since reloading may
	 * potentially be occurring in the background.
	 */
    int getModuleCount();
}
