package jp.eisbahn.eclipse.plugins.lingrclipse.view;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.core.runtime.ListenerList;
import jp.eisbahn.eclipse.plugins.lingrclipse.LingrOccupant;

public class OccupantList {

    private List<LingrOccupant> currentOccupantList;

    private List<LingrOccupant> previousOccupantList;

    private ListenerList listenerList;

    public OccupantList() {
        super();
        currentOccupantList = new LinkedList<LingrOccupant>();
        previousOccupantList = new LinkedList<LingrOccupant>();
        listenerList = new ListenerList();
    }

    public void addOccupantListener(OccupantListener listener) {
        listenerList.add(listener);
    }

    public void removeOccupantListener(OccupantListener listener) {
        listenerList.remove(listener);
    }

    public void changeOccpantList(List<LingrOccupant> occupantList) {
        previousOccupantList = currentOccupantList;
        currentOccupantList = occupantList;
        notifyOccupantChanged();
    }

    public List<LingrOccupant> getCurrentOccupantList() {
        return Collections.unmodifiableList(currentOccupantList);
    }

    public List<LingrOccupant> getPreviousOccupantList() {
        return Collections.unmodifiableList(previousOccupantList);
    }

    public void clear() {
        previousOccupantList = currentOccupantList;
        currentOccupantList = new LinkedList<LingrOccupant>();
        notifyOccupantChanged();
    }

    private void notifyOccupantChanged() {
        Object[] listeners = listenerList.getListeners();
        for (int i = 0; i < listeners.length; i++) {
            ((OccupantListener) listeners[i]).occupantChanged(previousOccupantList, currentOccupantList);
        }
    }
}
