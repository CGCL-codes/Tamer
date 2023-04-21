package fr.lelouet.ga.examples.travelingSalesman;

import java.util.*;

/**
 * The world maps get the effort to travel from any point to any other point.<br />
 * It is complete, such as default length is +inf beetwen any couple of location<br >
 * Locations are integer ( = array index? )
 * 
 * @author guigolum
 */
public class WorldMap extends HashMap<Integer, HashMap<Integer, Double>> implements Iterable<Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Set of all existing towns in this maps
     */
    protected Set<Integer> sociableTowns = new HashSet<Integer>();

    /**
     * @return the distance from 'from' to 'to', or +inf if not registered
     *         (default)
     */
    public double length(final int from, final int to) {
        final Map<Integer, Double> dests = this.get(from);
        if (dests == null || !dests.containsKey(to)) {
            return Float.POSITIVE_INFINITY;
        }
        return dests.get(to);
    }

    /**
     * set the distance from one town to another on the map.<br />
     * The map is not reflexive, thus the reverse distance is not modified.
     */
    public void set(final int from, final int to, Double length) {
        if (length == null) {
            length = Double.POSITIVE_INFINITY;
        }
        HashMap<Integer, Double> dests = this.get(from);
        if (dests == null) {
            dests = new HashMap<Integer, Double>();
            this.put(from, dests);
        }
        dests.put(to, length);
        sociableTowns.add(from);
        sociableTowns.add(to);
    }

    /**
     * set the distance between two towns in a symetric way.<br />
     * Equivalent to set(to, from, length); set(from, to, length);
     * 
     * @see #set(int, int, float)
     * @param from
     *            the first town
     * @param to
     *            the second town
     * @param length
     *            the distance
     */
    public void setSym(final int from, final int to, final double length) {
        set(from, to, length);
        set(to, from, length);
    }

    /** return an unmodifiable set of all the towns that we can go from or to */
    public Set<Integer> sociableTowns() {
        return Collections.unmodifiableSet(sociableTowns);
    }

    public Iterator<Integer> iterator() {
        return sociableTowns.iterator();
    }

    /**
     * @return a (nearby) randomized generated travel, hamiltonian, which may
     *         have an infinite length.
     */
    public Travel simpleHamiltonien() {
        Travel ret = new Travel(this);
        Integer first = null;
        for (Integer i : this) {
            if (first == null) {
                first = i;
            }
            ret.add(i);
        }
        if (first != null) {
            ret.add(first);
        }
        return ret;
    }

    /**
     * load a matrix of distances to erase present distances
     * 
     * @param array
     *            the distances between the towns :array[i][j] is the distance
     *            from i to j
     */
    public void loadArray(Double[][] array) {
        for (int i = 0; i < array.length; i++) {
            Double[] dests = array[i];
            for (int j = 0; j < dests.length; j++) {
                set(i, j, dests[j]);
            }
        }
    }

    /**
     * same as {@link #loadArray(Integer[][])} but considers that dist(i, j) =
     * dist (j, i) if i>j
     * 
     * @param doubles
     *            the distances between the towns, considered as a symmetric
     *            upper matrix
     */
    public void loadSymArray(Double[][] doubles) {
        for (int i = 0; i < doubles.length; i++) {
            Double[] dests = doubles[i];
            for (int j = i; j < dests.length; j++) {
                setSym(i, j, dests[j]);
            }
        }
    }

    /**
     * 
     * @return the longest not infinite length between two towns, or +inf if no
     *         infinite
     */
    public double longestNoInfinite() {
        Double maxval = Double.NEGATIVE_INFINITY;
        for (Map<Integer, Double> m : this.values()) {
            for (double d : m.values()) {
                if (!Double.isInfinite(d) && d > maxval) {
                    maxval = d;
                }
            }
        }
        if (maxval == Double.NEGATIVE_INFINITY) {
            maxval = Double.POSITIVE_INFINITY;
        }
        return maxval;
    }
}
