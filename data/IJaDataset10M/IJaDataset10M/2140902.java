package net.wotonomy.datastore;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
* This implementation of DataIndex wraps a TreeMap and
* adds the ability to contain objects with duplicate keys.
*/
public class DefaultDataIndex implements DataIndex {

    static final long serialVersionUID = -3759982714240822885L;

    protected String name;

    protected String property;

    private TreeMap treeMap;

    private Comparator comparator;

    public DefaultDataIndex() {
        comparator = new DefaultComparator();
        setTreeMap(new TreeMap(new DefaultComparator()));
    }

    public DefaultDataIndex(String aName, String aProperty) {
        this();
        setName(aName);
        setProperty(aProperty);
    }

    public Comparator getComparator() {
        return comparator;
    }

    public void setComparator(Comparator aComparator) {
        comparator = aComparator;
        TreeMap map = getTreeMap();
        setTreeMap(new TreeMap(comparator));
        getTreeMap().putAll(map);
    }

    public String getName() {
        return name;
    }

    ;

    public void setName(String aName) {
        name = aName;
    }

    public String getProperty() {
        return property;
    }

    ;

    public void setProperty(String aProperty) {
        property = aProperty;
    }

    public TreeMap getTreeMap() {
        return treeMap;
    }

    public void setTreeMap(TreeMap aMap) {
        treeMap = aMap;
    }

    public List query(Object beginValue, Object endValue) {
        List result = new LinkedList();
        if (endValue == null) {
            if (beginValue == null) {
                populateListFromIterator(result, treeMap.values().iterator());
                return result;
            }
            populateListFromIterator(result, treeMap.tailMap(beginValue).values().iterator());
            return result;
        } else if (beginValue == null) {
            populateListFromIterator(result, treeMap.headMap(endValue).values().iterator());
        } else {
            populateListFromIterator(result, treeMap.subMap(beginValue, endValue).values().iterator());
        }
        Object o = treeMap.get(endValue);
        if (o != null) {
            if (o instanceof DuplicateList) {
                populateListFromIterator(result, ((DuplicateList) o).iterator());
            } else {
                result.add(o);
            }
        }
        return result;
    }

    protected void populateListFromIterator(List aList, Iterator it) {
        Object o;
        while (it.hasNext()) {
            o = it.next();
            if (o instanceof DuplicateList) {
                populateListFromIterator(aList, ((DuplicateList) o).iterator());
            } else {
                aList.add(o);
            }
        }
    }

    public Object addObject(Object anObject, Object newValue) {
        Object o = treeMap.get(newValue);
        if (o != null) {
            if (o instanceof DuplicateList) {
                ((DuplicateList) o).add(anObject);
                return anObject;
            }
            DuplicateList list = new DuplicateList();
            list.add(o);
            list.add(anObject);
            anObject = list;
        }
        if (anObject == null) new RuntimeException().printStackTrace();
        treeMap.put(newValue, anObject);
        return anObject;
    }

    public Object updateObject(Object anObject, Object oldValue, Object newValue) {
        removeObject(anObject, oldValue);
        return addObject(anObject, newValue);
    }

    public Object removeObject(Object anObject, Object oldValue) {
        Object o = treeMap.get(oldValue);
        if (o != null) {
            if (o instanceof DuplicateList) {
                DuplicateList list = (DuplicateList) o;
                list.remove(anObject);
                if (list.size() > 1) return anObject;
                if (list.size() == 0) {
                    System.out.println("DefaultDataIndex.deleteObject: " + oldValue + " : list size is 1 : this should never happen.");
                    return null;
                }
                treeMap.remove(oldValue);
                treeMap.put(oldValue, list.getFirst());
                return anObject;
            }
            treeMap.remove(oldValue);
        }
        return anObject;
    }

    public void clear() {
        treeMap.clear();
    }

    public String toString() {
        return "DefaultDataIndex: " + name + " : " + property + " : " + treeMap.toString();
    }
}
