package de.mpiwg.vspace.common.extension;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import de.mpiwg.vspace.common.Activator;
import de.mpiwg.vspace.extension.ExtensionPointIDs;
import de.mpiwg.vspace.extension.interfaces.IItemPropertyDescriptorExtension;

public class ProjectObserverRegistry {

    public static final ProjectObserverRegistry REGISTRY = new ProjectObserverRegistry();

    protected static final String EXTENSION_POINT_ID = Activator.PLUGIN_ID + ".projectObserver";

    private List<Observer> observers;

    private ProjectObserverRegistry() {
        observers = new ArrayList<Observer>();
        IConfigurationElement[] configs = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID);
        for (IConfigurationElement e : configs) {
            Object o = null;
            try {
                o = e.createExecutableExtension("class");
            } catch (CoreException e1) {
                e1.printStackTrace();
                continue;
            }
            if (o != null && o instanceof Observer) {
                observers.add((Observer) o);
            }
        }
    }

    public Observer[] getProjectObservers() {
        return observers.toArray(new Observer[observers.size()]);
    }
}
