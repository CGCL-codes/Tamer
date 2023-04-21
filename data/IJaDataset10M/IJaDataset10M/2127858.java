package be.vds.jtbdive.client.view.panels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import be.vds.jtbdive.client.view.events.DiverSelectionListenable;
import be.vds.jtbdive.client.view.events.DiverSelectionListener;
import be.vds.jtbdive.core.core.Diver;

public class ModificationDiverSelectListenableJPanel extends ModificationListenableJPanel implements DiverSelectionListenable {

    private Set<DiverSelectionListener> diverSelectionListeners = new HashSet<DiverSelectionListener>();

    @Override
    public void addDiverSelectionListener(DiverSelectionListener diverSelectionListener) {
        this.diverSelectionListeners.add(diverSelectionListener);
    }

    @Override
    public void removeDiverSelectionListener(DiverSelectionListener diverSelectionListener) {
        this.diverSelectionListeners.remove(diverSelectionListener);
    }

    protected void notifyDiverSelectionListeners(Diver diver) {
        List<Diver> l = new ArrayList<Diver>();
        l.add(diver);
        for (DiverSelectionListener listener : diverSelectionListeners) {
            listener.diversSelected(l);
        }
    }
}
