package uk.ac.bolton.archimate.editor.ui.services;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;

/**
 * Component Selection Changed Manager - used for Hints View
 * 
 * @author Phillip Beauvoir
 */
public final class ComponentSelectionManager {

    public static final ComponentSelectionManager INSTANCE = new ComponentSelectionManager();

    private List<IComponentSelectionListener> listeners = new ArrayList<IComponentSelectionListener>();

    public void addSelectionListener(IComponentSelectionListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeSelectionListener(IComponentSelectionListener listener) {
        listeners.remove(listener);
    }

    public void fireSelectionEvent(final Object source, final Object selection) {
        if (selection == null) {
            return;
        }
        Object[] listenersArray = listeners.toArray();
        for (int i = 0; i < listenersArray.length; i++) {
            final IComponentSelectionListener l = (IComponentSelectionListener) listenersArray[i];
            SafeRunner.run(new SafeRunnable() {

                public void run() {
                    l.componentSelectionChanged(source, selection);
                }
            });
        }
    }
}
