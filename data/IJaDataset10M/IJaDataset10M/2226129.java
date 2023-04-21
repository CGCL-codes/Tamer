package org.eclipse.swordfish.tooling.platform;

import java.io.IOException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IDynamicVariable;
import org.eclipse.core.variables.IDynamicVariableResolver;

/**
 * class DynamicVariableResolver
 * 
 * @author amarkevich
 */
public class DynamicVariableResolver implements IDynamicVariableResolver {

    /**
	 * {@inheritDoc}
	 */
    public String resolveValue(IDynamicVariable variable, String argument) throws CoreException {
        String location = null;
        try {
            location = FileLocator.getBundleFile(Activator.getDefault().getBundle()).toString();
        } catch (IOException e) {
            throw new CoreException(new Status(IStatus.ERROR, Activator.getDefault().getId(), e.getMessage(), e));
        }
        return location;
    }
}
