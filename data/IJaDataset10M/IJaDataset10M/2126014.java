package net.sf.rcpforms.emf;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.databinding.observable.list.ObservableList;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Content Provider which observes an writable list of EObjects for changes. It attaches to all
 * EObjects in the list and refreshes the viewer if a property of the bean changes.
 * 
 * @author vanmeegenm
 */
public class ObservableListEObjectContentProvider extends ObservableListContentProvider {

    /** observed list */
    private ObservableList observableList = null;

    private StructuredViewer viewer;

    private List<EObject> targets = new ArrayList<EObject>();

    private Adapter adapter = new AdapterImpl() {

        @Override
        public void notifyChanged(Notification msg) {
            handlePropertyChange(msg);
        }
    };

    /** change listener for bean property changes of beans contained in list */
    private IListChangeListener listener = new IListChangeListener() {

        public void handleListChange(ListChangeEvent event) {
            ListDiffEntry[] differences = event.diff.getDifferences();
            for (int i = 0; i < differences.length; i++) {
                ListDiffEntry entry = differences[i];
                EObject eobject = (EObject) entry.getElement();
                if (entry.isAddition()) {
                    hookListener(eobject);
                } else {
                    unhookListener(eobject);
                }
            }
        }
    };

    /** * Constructor for ObservableListBeanContentProvider */
    public ObservableListEObjectContentProvider() {
    }

    /**
     * * called when one of the beans in the observed list fires a property change * * @param source
     * * @param propertyName
     */
    protected void handlePropertyChange(Notification msg) {
        viewer.update(msg.getNotifier(), null);
    }

    /**
     * * (non-Javadoc) * * @see
     * org.eclipse.jface.databinding.viewers.ObservableListContentProvider#inputChanged
     * (org.eclipse.jface.viewers.Viewer, * java.lang.Object, java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        unhookAllTargets();
        if (observableList != null) {
            observableList.removeListChangeListener(listener);
            observableList = null;
        }
        super.inputChanged(viewer, oldInput, newInput);
        this.viewer = (StructuredViewer) viewer;
        observableList = (ObservableList) newInput;
        if (observableList != null) {
            observableList.addListChangeListener(listener);
            for (Iterator iterator = observableList.iterator(); iterator.hasNext(); ) {
                EObject next = (EObject) iterator.next();
                hookListener(next);
            }
        }
    }

    private void unhookAllTargets() {
        for (Notifier notifier : targets) {
            notifier.eAdapters().remove(adapter);
        }
    }

    private boolean hookListener(EObject eobject) {
        boolean success = eobject.eAdapters().add(adapter);
        if (success) {
            targets.add(eobject);
        }
        return success;
    }

    private boolean unhookListener(EObject eobject) {
        boolean success = eobject.eAdapters().remove(adapter);
        if (success) {
            targets.remove(eobject);
        }
        return success;
    }
}
