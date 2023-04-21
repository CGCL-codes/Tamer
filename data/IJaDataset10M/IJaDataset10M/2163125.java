package com.jacum.cms.rcp.ui.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import com.jacum.cms.rcp.model.Host;

/**
 * Common interface for all actions with server. Uses for work with server data
 * 
 * @author rich
 */
public interface IServerAction {

    /**
	 * Execute action
	 * 
	 * @throws Exception exception thrown during action execution
	 */
    public void run() throws Exception;

    /**
	 * Set monitor object for notifying about progress and tasks names
	 * 
	 * @param monitor monitor object
	 */
    public void setProgressMonitor(IProgressMonitor monitor);

    /**
	 * Return host object 
	 * 
	 * @return host object 
	 */
    public Host getHost();
}
