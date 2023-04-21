package net.sf.zip.internal;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

/**
 * @author allon moritz
 */
public interface IZipJobRunnable {

    /**
	 * Will be executed inside an <code>GSearchJob.run(...)</code>.
	 * 
	 * @param monitor
	 * @return the status
	 * @throws CoreException
	 */
    public IStatus run(IProgressMonitor monitor) throws CoreException;
}
