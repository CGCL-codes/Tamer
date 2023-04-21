package net.sf.eclint.ui.properties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Composite;

public interface ICompositeAdapter {

    public Composite getComposite(Composite parent, int style, IProject project) throws CoreException;
}
