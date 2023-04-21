package mate.dart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DartBoardSubject {

    List<ThrowEventListener> listenerList = new ArrayList<ThrowEventListener>();

    Map<String, Integer> nameToIndexMap = new HashMap<String, Integer>();

    public void notifyListeners(ThrowEvent event) {
        for (ThrowEventListener l : listenerList) {
            l.newThrowEvent(event);
        }
    }

    public void addListener(ThrowEventListener listener, String name) {
        nameToIndexMap.put(name, listenerList.size());
        listenerList.add(listener);
    }

    public void removeListener(String name) {
        listenerList.remove(nameToIndexMap.get(name).intValue());
        nameToIndexMap.remove(name);
    }
}
