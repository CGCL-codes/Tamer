package org.eclipse.mylyn.internal.context.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

/**
 * @author Mik Kersten
 */
public interface IActiveSearchOperation {

    public void addListener(IActiveSearchListener listener);

    public void removeListener(IActiveSearchListener listener);

    public IStatus run(IProgressMonitor monitor);
}
