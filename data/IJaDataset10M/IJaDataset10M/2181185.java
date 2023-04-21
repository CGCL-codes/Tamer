package net.sf.logsaw.core.logresource;

import net.sf.logsaw.core.config.IConfigurableObject;
import net.sf.logsaw.core.dialect.ILogDialect;
import net.sf.logsaw.core.dialect.ILogEntryCollector;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Philipp Nanz
 */
public interface ILogResource extends IConfigurableObject {

    /**
	 * @return the name
	 */
    String getName();

    /**
	 * @param name the name to set
	 */
    void setName(String name);

    /**
	 * @return the pk of the index
	 */
    String getPK();

    /**
	 * @param pk the pk of the index to set
	 */
    void setPK(String pk);

    /**
	 * @return the dialect
	 */
    ILogDialect getDialect();

    /**
	 * @param dialect the dialect to set
	 */
    void setDialect(ILogDialect dialect);

    /**
	 * @return the factory
	 */
    ILogResourceFactory getFactory();

    /**
	 * @param factory the factory to set
	 */
    void setFactory(ILogResourceFactory factory);

    /**
	 * Synchronizes the log index for the given log resource.
	 * @param collector the log entry collector
	 * @param monitor the progress monitor
	 * @throws CoreException if an error occurs
	 */
    void synchronize(ILogEntryCollector collector, IProgressMonitor monitor) throws CoreException;
}
