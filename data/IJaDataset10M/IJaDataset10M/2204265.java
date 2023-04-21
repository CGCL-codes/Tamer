package netdev.base;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * This class is to list all stations and mediums available in the simulator
 * @author topaz
 *
 */
public class GlobalConfigurableManager implements ObjectsObservable {

    private Hashtable<String, Station> stations;

    private Hashtable<String, Medium> mediums;

    private LinkedList<ObjectsObserver> Observers;

    public GlobalConfigurableManager() {
        stations = new Hashtable<String, Station>();
        mediums = new Hashtable<String, Medium>();
        Observers = new LinkedList<ObjectsObserver>();
    }

    public void updateStation(Station st) {
        Station current = stations.get(st.getProperty("id"));
        if (current == null) {
            stations.put(st.getProperty("id"), st);
            current = stations.get(st.getProperty("id"));
        } else {
            current.copyProperties(st);
        }
        ListIterator<ObjectsObserver> observers = Observers.listIterator();
        while (observers.hasNext()) {
            ObjectsObserver oo = observers.next();
            oo.objectChanged(this, current);
        }
    }

    public void updateMedium(Medium md) {
        Medium current = mediums.get(md.getProperty("id"));
        if (current == null) {
            mediums.put(md.getProperty("id"), md);
            current = mediums.get(md.getProperty("id"));
        } else {
            current.copyProperties(md);
        }
        ListIterator<ObjectsObserver> observers = Observers.listIterator();
        while (observers.hasNext()) {
            ObjectsObserver oo = observers.next();
            oo.objectChanged(this, current);
        }
    }

    public List<Medium> enumerateMediums() {
        LinkedList<Medium> l = new LinkedList<Medium>();
        Enumeration<Medium> e = mediums.elements();
        while (e.hasMoreElements()) {
            l.add(e.nextElement());
        }
        return l;
    }

    public List<String> enumerateMediumsName() {
        LinkedList<String> l = new LinkedList<String>();
        Enumeration<Medium> e = mediums.elements();
        while (e.hasMoreElements()) {
            l.add(e.nextElement().getProperty("name"));
        }
        return l;
    }

    public List<Station> enumerateStations() {
        LinkedList<Station> l = new LinkedList<Station>();
        Enumeration<Station> e = stations.elements();
        while (e.hasMoreElements()) {
            l.add(e.nextElement());
        }
        return l;
    }

    public List<String> enumerateStationsName() {
        LinkedList<String> l = new LinkedList<String>();
        Enumeration<Station> e = stations.elements();
        while (e.hasMoreElements()) {
            l.add(e.nextElement().getProperty("name"));
        }
        return l;
    }

    public void addObjectsObserver(ObjectsObserver oo) {
        Observers.add(oo);
    }

    public void removeObjectsObserver(ObjectsObserver oo) {
        Observers.remove(oo);
    }
}
