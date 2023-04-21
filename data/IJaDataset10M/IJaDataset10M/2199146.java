package net.sf.logsaw.ui.model;

import net.sf.logsaw.core.logresource.ILogResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * @author Philipp Nanz
 */
public class LogResourceWorkbenchAdapterFactory implements IAdapterFactory {

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Object adaptableObject, Class adapterType) {
        if ((adapterType == IWorkbenchAdapter.class) && (adaptableObject instanceof ILogResource)) {
            return new LogResourceWorkbenchAdapter((ILogResource) adaptableObject);
        }
        return null;
    }

    @Override
    public Class<?>[] getAdapterList() {
        return new Class<?>[] { IWorkbenchAdapter.class };
    }
}
