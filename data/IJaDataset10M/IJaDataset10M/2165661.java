package org.tuba.spatschorke.diploma.repository.mock.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class PropertyChangeListener implements IPropertyChangeListener {

    private Map<String, List<IPropertyChangeObserver>> observerMap = new HashMap<String, List<IPropertyChangeObserver>>();

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        List<IPropertyChangeObserver> observers = observerMap.get(event.getProperty());
        if (observers == null) return;
        for (IPropertyChangeObserver observer : observers) observer.update(event.getProperty(), event.getNewValue());
    }

    public void addObserver(IPropertyChangeObserver observer, String property) {
        List<IPropertyChangeObserver> observers = observerMap.get(property);
        if (observers == null) {
            observers = new ArrayList<IPropertyChangeObserver>();
            observerMap.put(property, observers);
        }
        if (observers.contains(observer)) return;
        observers.add(observer);
    }
}
