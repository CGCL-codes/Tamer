package com.rpc.core.utils.process;

import java.util.Collection;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 'Processes' are simple objects that perform some function. All processes must
 * implement this interface. The IProcess interface extends from
 * IExecutableExtension because all processes are meant to be declared as
 * executable extensions to the com.rpc.core.processes extension point.
 * 
 * Here's an example of how to declare a process extension... <extension
 * id="openAccountingPeriod" point="com.rpc.core.processes"> <process> <run
 * class="com.rpc.core.accounting.open.OpenAccountingPeriod" > <parameter
 * name="arg1" value="12345" /> </run> </process> </extension>
 * 
 * To execute a process a process's run method is invoked. A process may call
 * the getContext method to get a Map of state information that defines the
 * 'environment' in which the process is running. A process's context is similar
 * in concept to a servlet's session. A process may also put information into
 * it's context. A typlical use of the process context is to pass information to
 * subprocesses or to other process further down the chain of execution.
 * 
 * @author ted stockwell
 */
public interface IProcess extends IExecutableExtension {

    Collection getWorkflowAdapters();

    public void run(IProgressMonitor monitor) throws Exception;

    /**
     * Returns the configuration element associated with this component. This
     * element is a direct reflection of the configuration markup supplied in
     * the manifest (<code>plugin.xml</code>) file for the plug-in that
     * declares this extension.
     * 
     * @return the configuration elements associated with this extension
     */
    public IConfigurationElement getConfigurationElement();

    /**
     * Returns a map of context attributes assoicated with this process. A
     * process's context is a set of named objects that define the state in
     * which the process is being executed. Similar in concept to a servet
     * session.
     * 
     * The process context if often used to pass state information from an outer
     * process to subprocesses.
     */
    public IWorkflowContext getProcessContext();

    /**
     * This method should only be called by the 'container' that is running the
     * process.
     */
    public void setProcessContext(IWorkflowContext context);

    /**
     * Always invoked before the ISafeRunnable.run method.
     */
    public void init() throws Exception;

    /**
     * Always invoked after the process is run.
     */
    public void dispose();

    public String getId();

    public String getName();

    public String getDescription();
}
