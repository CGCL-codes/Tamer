package org.nomadpim.module.schedule.core;

import java.util.ArrayList;
import java.util.List;
import org.nomadpim.core.util.event.IExtendedContainerChangeListener;
import org.nomadpim.core.util.event.PropertyChangeEvent;

public abstract class AbstractDateInformationSource implements IDateInformationSource {

    private List<IExtendedContainerChangeListener<IDateInformation>> listeners = new ArrayList<IExtendedContainerChangeListener<IDateInformation>>();

    public void addListener(IExtendedContainerChangeListener<IDateInformation> l) {
        listeners.add(l);
    }

    protected void fireAdd(IDateInformation o) {
        for (IExtendedContainerChangeListener<IDateInformation> l : listeners) {
            l.objectAdded(o);
        }
    }

    protected void fireChange(IDateInformation o, PropertyChangeEvent event) {
        for (IExtendedContainerChangeListener<IDateInformation> l : listeners) {
            l.objectChanged(o, event);
        }
    }

    protected void fireRemove(IDateInformation o) {
        for (IExtendedContainerChangeListener<IDateInformation> l : listeners) {
            l.objectRemoved(o);
        }
    }

    public void removeListener(IExtendedContainerChangeListener<IDateInformation> l) {
        listeners.remove(l);
    }
}
