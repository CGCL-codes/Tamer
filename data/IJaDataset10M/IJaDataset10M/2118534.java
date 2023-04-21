package desKit;

import java.util.*;

/**
 * Description: This a main list which contains every simulation objects and
 * what fallows each instance Activity List
 * 
 * @author: Robert Zal, Dariusz Pierzchala
 */
class PendingList {

    private LinkedList<SimObject> list;

    public PendingList() {
        list = new LinkedList<SimObject>();
    }

    public void sortPL() {
        ObjectCompare objectCompare = new ObjectCompare();
        Collections.sort(list, objectCompare);
    }

    public boolean add(SimObject o) {
        boolean pom = list.add(o);
        sortPL();
        return pom;
    }

    public SimObject getFirstSimObject() {
        return (SimObject) list.getFirst();
    }

    public SimActivity getFirstSimActivity() {
        return getFirstSimObject().getFirstSimActivity();
    }

    public boolean remove(SimObject so) {
        return list.remove(so);
    }

    public int size() {
        return list.size();
    }
}
