package org.hswgt.teachingbox.datastructures;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Hash for parameters
 */
public class ParameterList implements Serializable, Iterable<Entry<String, Double>> {

    private static final long serialVersionUID = 2500588710758665389L;

    protected LinkedHashMap<String, Double> params = new LinkedHashMap<String, Double>();

    /**
     * Default Constructor
     */
    public ParameterList() {
    }

    /**
     * Copy Constructor
     * @param plist
     */
    public ParameterList(ParameterList plist) {
        this.params = new LinkedHashMap<String, Double>(plist.params);
    }

    /**
     * Adds a new parameter to the ParameterList.
     * Important you can only get/set parameters that had been
     * initialized that way. 
     * @param paramname The parameter name
     * @param value The parameter value
     */
    public void init(String paramname, double value) {
        params.put(paramname, value);
    }

    /**
     * Get the value of a parameter
     * @param paramname The parameter name
     * @return The parameter value
     * @throws IllegalArgumentException if the parameter had not been initialized
     */
    public double get(String paramname) throws IllegalArgumentException {
        Double value = params.get(paramname);
        if (value == null) throw new IllegalArgumentException("There is no parameter `" + paramname + "` in ParameterList");
        return value;
    }

    /**
     * Sets the value for a parameter
     * @param paramname The parameter name
     * @param value The parameter value
     * @throws IllegalArgumentException if the parameter had not been initialized
     */
    public void set(String paramname, double value) throws IllegalArgumentException {
        if (!params.containsKey(paramname)) throw new IllegalArgumentException("There is no parameter `" + paramname + "` in ParameterList");
        params.put(paramname, value);
    }

    /**
     * Provides an iterator for the parameter list
     * The iterator is backed by the map, so changes to the map are reflected in the set, 
     * and vice-versa.
     * @return The Iterator for the parameter list
     */
    public Iterator<Entry<String, Double>> iterator() {
        return params.entrySet().iterator();
    }

    /**
     * Returns a collection view of the mappings contained in this list.
     * @return a collection view of the mappings contained in this list.
     */
    public Set<Entry<String, Double>> entrySet() {
        return params.entrySet();
    }

    /**
     * Returns a set view of the keys contained in this map. 
     * The set is backed by the map, so changes to the map are reflected in the set, 
     * and vice-versa.
     * @return a set view of the keys contained in this map
     */
    public Set<String> keySet() {
        return params.keySet();
    }

    /**
     * Returns a collection view of the values contained in this map. 
     * The collection is backed by the map, so changes to the map are reflected in the collection, 
     * and vice-versa.
     * @return a collection view of the values contained in this map
     */
    public Collection<Double> valueSet() {
        return params.values();
    }

    /**
     * Returns true if this list contains the parameter specified
     * @param paramname The name of the parameter
     * @return true if this list contains the parameter specified
     */
    public boolean hasParameter(String paramname) {
        return params.containsKey(paramname);
    }
}
