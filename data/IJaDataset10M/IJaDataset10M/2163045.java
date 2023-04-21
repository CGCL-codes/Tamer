package net.sf.ttd.core.config;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import net.sf.ttd.common.utils.NotNull;

/**
 * @author pkrupets
 */
public interface ITaskTagDecoratorChanges {

    public boolean affects(@NotNull IMarker marker) throws CoreException;
}
