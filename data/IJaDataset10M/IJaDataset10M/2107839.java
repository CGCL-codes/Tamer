package uk.ac.lkl.common.util.collections;

import java.util.*;
import uk.ac.lkl.common.util.collections.event.*;

/**
 * A collection of (notifying) sets, each of which has a name.
 * 
 * This can store several sets, e.g. a set of bird species, a set of mammal species, etc. 
 */
public class NamedSetMap<E> {

    /**
     * This listener(s) is created to listen to a NotifyingSet. 
     */
    private class ChainingSetListener implements SetListener<E> {

        private String setName;

        public ChainingSetListener(String setName) {
            this.setName = setName;
        }

        @SuppressWarnings("unused")
        public String getSetName() {
            return setName;
        }

        @Override
        public void elementAdded(SetEvent<E> e) {
            fireElementAdded(setName, e.getElement());
        }

        @Override
        public void elementRemoved(SetEvent<E> e) {
            fireElementRemoved(setName, e.getElement());
        }
    }

    ;

    private List<NamedSetMapListener<E>> listeners;

    private Map<String, ChainingSetListener> chainingSetListenerMap;

    private NotifyingMap<String, NotifyingSet<E>> setMap;

    private Class<E> elementClass;

    public NamedSetMap(Class<E> elementClass) {
        this.elementClass = elementClass;
        setMap = new NotifyingMap<String, NotifyingSet<E>>();
        listeners = new ArrayList<NamedSetMapListener<E>>();
        chainingSetListenerMap = new HashMap<String, ChainingSetListener>();
    }

    public String getFirstName() {
        return setMap.firstKey();
    }

    /**
     * Returns the names of the sets in this map.
     * 
     * @return the names of the sets in this map.
     */
    public Set<String> getSetNames() {
        return setMap.keySet();
    }

    /**
     * Creates a new set with the given name.
     * 
     * @param name the name
     */
    public void addSet(String name) {
        addSet(name, new NotifyingSet<E>(elementClass));
    }

    /**
     * Creates a new set with the given name, initialising it with the 
     * given elements. 
     * 
     * @param name the name
     * @param elements the elements
     */
    public void addSet(String name, E... elements) {
        addSet(name, Arrays.asList(elements));
    }

    /**
     * Creates a new set with the given name, initialising it with the 
     * given elements. 
     * 
     * @param name the name
     * @param elements the elements
     */
    public void addSet(String name, Collection<E> elements) {
        NotifyingSet<E> set = new NotifyingSet<E>(elementClass);
        addSet(name, set);
        set.addAll(elements);
    }

    private void addSet(String name, NotifyingSet<E> set) {
        NotifyingSet<E> currentSet = setMap.get(name);
        if (currentSet != null) throw new IllegalArgumentException("Set already defined for name '" + name + "'");
        setMap.put(name, set);
        ChainingSetListener chainingSetListener = new ChainingSetListener(name);
        chainingSetListenerMap.put(name, chainingSetListener);
        set.addSetListener(chainingSetListener);
    }

    /**
     * Removes a set from this map.
     * 
     * @param name the name of the set. 
     * 
     * @return true if the set existed and was removed successfully, false otherwise.
     */
    public boolean removeSet(String name) {
        NotifyingSet<E> set = setMap.remove(name);
        if (set == null) return false;
        ChainingSetListener chainingSetListener = chainingSetListenerMap.remove(name);
        set.removeSetListener(chainingSetListener);
        return true;
    }

    /**
     * Adds an element to the set with the given name. 
     * 
     * If the set does not exist, it is created. 
     * 
     * @param name the name
     * @param element the element to be added
     */
    public boolean addElement(String name, E element) {
        NotifyingSet<E> set = setMap.get(name);
        if (set == null) {
            set = new NotifyingSet<E>(elementClass);
            addSet(name, set);
        }
        return set.add(element);
    }

    /**
     * Adds given elements to the set with the given name. 
     * 
     * If the set does not exist, it is created. 
     * 
     * @param name the name of the set
     * @param elements the elements
     */
    public boolean addElements(String name, Collection<E> elements) {
        NotifyingSet<E> set = setMap.get(name);
        if (set == null) {
            set = new NotifyingSet<E>(elementClass);
            addSet(name, set);
        }
        return set.addAll(elements);
    }

    public boolean removeElement(String name, E element) {
        NotifyingSet<E> set = setMap.get(name);
        if (set == null) {
            set = new NotifyingSet<E>(elementClass);
            addSet(name, set);
        }
        return set.remove(element);
    }

    /**
     * Returns true if an element exists in a set that exists with the given name 
     * in this map, false otherwise.
     * 
     * @param name the name of the map 
     * @param element the element
     * 
     * @return true if an element exists in a set that exists, false otherwise 
     */
    public boolean containsElement(String name, E element) {
        NotifyingSet<E> set = setMap.get(name);
        if (set == null) return false;
        return set.contains(element);
    }

    /**
     * Returns the (notifying) set of the given name.
     * 
     * @param name the name of the set.
     * 
     * @return the set
     * 
     * @throws IllegalArgumentException if no set exists for that name
     */
    public NotifyingSet<E> getSet(String name) {
        NotifyingSet<E> set = setMap.get(name);
        if (set == null) {
            throw new IllegalArgumentException("No set defined for name '" + name + "'");
        }
        return set;
    }

    /**
     * Returns true if a set exists for this name, false otherwise. 
     *  
     * @param name the name
     * 
     * @return true if a set exists for this name, false otherwise.
     */
    public boolean setExists(String name) {
        return setMap.containsKey(name);
    }

    /**
     * Returns the number of (notifying) sets in this map
     * 
     * @return the number of (notifying) sets in this map
     */
    public int size() {
        return setMap.size();
    }

    /**
     * Returns the size of the set of the given name.
     * 
     * @param name the name
     * 
     * @return the size of the set of the given name.
     * 
     * @throws IllegalArgumentException if there is no set for this name
     */
    public int getSize(String name) {
        NotifyingSet<E> set = setMap.get(name);
        if (set == null) {
            throw new IllegalArgumentException("No set defined for name '" + name + "'");
        }
        return set.size();
    }

    private void fireElementAdded(String setName, E element) {
        NamedSetMapEvent<E> e = new NamedSetMapEvent<E>(this, setName, element);
        for (NamedSetMapListener<E> listener : listeners) listener.elementAdded(e);
    }

    private void fireElementRemoved(String setName, E element) {
        NamedSetMapEvent<E> e = new NamedSetMapEvent<E>(this, setName, element);
        for (NamedSetMapListener<E> listener : listeners) listener.elementRemoved(e);
    }

    public void addMapListener(MapListener<String, NotifyingSet<E>> listener) {
        setMap.addMapListener(listener);
    }

    public void removeMapListener(MapListener<String, NotifyingSet<E>> listener) {
        setMap.removeMapListener(listener);
    }

    public void addNamedSetMapListener(NamedSetMapListener<E> listener) {
        listeners.add(listener);
    }

    public void removeNamedSetMapListener(NamedSetMapListener<E> listener) {
        listeners.remove(listener);
    }
}
