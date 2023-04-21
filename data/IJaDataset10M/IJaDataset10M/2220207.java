package org.eclipse.ufacekit.core.databinding.instance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.core.databinding.observable.IObserving;

/**
 * Abstract superclass for {@link IInstanceObservedContainer}.
 */
public abstract class AbstractInstanceObservedContainer implements IInstanceObservedContainer {

    private Map observings = new HashMap();

    public void addObserving(IObserving observing, IInstanceObservedChanged instanceObservedChanged) {
        InternalInstanceObservedChanged internalInstanceChanged = (InternalInstanceObservedChanged) observings.get(observing);
        if (internalInstanceChanged == null) {
            internalInstanceChanged = new InternalInstanceObservedChanged(observing);
            observings.put(observing, internalInstanceChanged);
        }
        internalInstanceChanged.addIInstanceObservedChanged(instanceObservedChanged);
        internalInstanceChanged.currentObserved = observing.getObserved();
    }

    public void observeInstances() {
        Collection instanceObservedChangedList = observings.values();
        for (Iterator iterator = instanceObservedChangedList.iterator(); iterator.hasNext(); ) {
            InternalInstanceObservedChanged internalInstanceChanged = (InternalInstanceObservedChanged) iterator.next();
            Object oldInstance = internalInstanceChanged.currentObserved;
            IObserving observing = internalInstanceChanged.observing;
            if (isDirty(oldInstance, observing)) {
                Object newInstance = observing.getObserved();
                for (Iterator iterator2 = internalInstanceChanged.instanceObservedChangedList.iterator(); iterator2.hasNext(); ) {
                    IInstanceObservedChanged instanceObservedChanged = (IInstanceObservedChanged) iterator2.next();
                    instanceObservedChanged.instanceChanged(oldInstance, newInstance);
                }
                internalInstanceChanged.currentObserved = newInstance;
            }
        }
    }

    public void dispose() {
        Collection instanceObservedChangedList = observings.values();
        for (Iterator iterator = instanceObservedChangedList.iterator(); iterator.hasNext(); ) {
            InternalInstanceObservedChanged internalInstanceChanged = (InternalInstanceObservedChanged) iterator.next();
            Object observed = internalInstanceChanged.currentObserved;
            dispose(observed);
        }
        observings.clear();
    }

    /**
	 * Internal class to store a observing, the current observed Node instance
	 * and the list of listener {@link IInstanceObservedChanged}.
	 * 
	 */
    class InternalInstanceObservedChanged {

        public final IObserving observing;

        public Object currentObserved;

        public List instanceObservedChangedList = new ArrayList();

        public InternalInstanceObservedChanged(IObserving observing) {
            this.observing = observing;
        }

        public void addIInstanceObservedChanged(IInstanceObservedChanged instanceObservedChanged) {
            if (!instanceObservedChangedList.contains(instanceObservedChanged)) instanceObservedChangedList.add(instanceObservedChanged);
        }
    }

    /**
	 * if you wish for instance remove listener for each observed Node instance
	 * when IInstanceObservedContainer #dispose() is called.
	 * 
	 * @param observed
	 */
    protected abstract void dispose(Object observed);

    /**
	 * returns true if current Node instance (oldInstance) is dirty (instance
	 * has changed) and false otherwise.
	 * 
	 * @param oldInstance
	 * @param observing
	 * @return
	 */
    protected abstract boolean isDirty(Object oldInstance, IObserving observing);
}
