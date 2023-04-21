package org.nomadpim.ide;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IMaintenance {

    void addSources(IAdaptable selected) throws CoreException;

    void rework(IAdaptable selected) throws CoreException;

    void rework(IProgressMonitor monitor) throws CoreException;
}
