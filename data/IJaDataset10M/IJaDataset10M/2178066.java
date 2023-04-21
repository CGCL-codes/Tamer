package de.tudresden.inf.lat.jcel.core.graph;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class implements a map of binary relations.
 * 
 * @author Julian Mendez
 */
public class IntegerRelationMapImpl implements IntegerRelationMap {

    private Map<Integer, IntegerBinaryRelationImpl> relationMap = new HashMap<Integer, IntegerBinaryRelationImpl>();

    private Map<Integer, Collection<Integer>> relationSetByFirst = new HashMap<Integer, Collection<Integer>>();

    private Map<Integer, Collection<Integer>> relationSetBySecond = new HashMap<Integer, Collection<Integer>>();

    /**
	 * Constructs an empty map of binary relations.
	 */
    public IntegerRelationMapImpl() {
    }

    /**
	 * Adds an empty binary relation.
	 * 
	 * @param relationId
	 *            relation identifier
	 */
    public void add(int relationId) {
        if (!this.relationMap.containsKey(relationId)) {
            this.relationMap.put(relationId, new IntegerBinaryRelationImpl());
        }
    }

    /**
	 * Adds a pair to a binary relation.
	 * 
	 * @param relationId
	 *            relation id
	 * @param first
	 *            first component
	 * @param second
	 *            second component
	 */
    public void add(int relationId, int first, int second) {
        IntegerBinaryRelationImpl relation = this.relationMap.get(relationId);
        if (relation == null) {
            relation = new IntegerBinaryRelationImpl();
            this.relationMap.put(relationId, relation);
        }
        relation.add(first, second);
        Collection<Integer> byFirst = this.relationSetByFirst.get(first);
        if (byFirst == null) {
            byFirst = new HashSet<Integer>();
            this.relationSetByFirst.put(first, byFirst);
        }
        byFirst.add(relationId);
        Collection<Integer> bySecond = this.relationSetBySecond.get(second);
        if (bySecond == null) {
            bySecond = new HashSet<Integer>();
            this.relationSetBySecond.put(second, bySecond);
        }
        bySecond.add(relationId);
    }

    @Override
    public boolean contains(int relationId) {
        return this.relationMap.containsKey(relationId);
    }

    @Override
    public boolean contains(int relationId, int first, int second) {
        boolean ret = false;
        IntegerBinaryRelation relation = this.relationMap.get(relationId);
        if (relation != null) {
            ret = relation.contains(first, second);
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        boolean ret = (this == o);
        if (!ret && o instanceof IntegerRelationMap) {
            IntegerRelationMap other = (IntegerRelationMap) o;
            ret = getElements().equals(other.getElements());
            for (Iterator<Integer> it = getElements().iterator(); ret && it.hasNext(); ) {
                Integer elem = it.next();
                ret = ret && get(elem).equals(other.get(elem));
            }
        }
        return ret;
    }

    @Override
    public IntegerBinaryRelation get(int relationId) {
        return this.relationMap.get(relationId);
    }

    @Override
    public Collection<Integer> getByFirst(int relationId, int first) {
        Collection<Integer> ret = Collections.emptySet();
        IntegerBinaryRelation relation = this.relationMap.get(relationId);
        if (relation != null) {
            ret = relation.getByFirst(first);
        }
        return ret;
    }

    @Override
    public Collection<Integer> getBySecond(int relationId, int second) {
        Collection<Integer> ret = Collections.emptySet();
        IntegerBinaryRelation relation = this.relationMap.get(relationId);
        if (relation != null) {
            ret = relation.getBySecond(second);
        }
        return ret;
    }

    /**
	 * Returns the number of elements in the internal maps that are referred by
	 * the keys, without counting the keys themselves. This method recalculates
	 * the value every time it is called.
	 * 
	 * @return the number of elements in the internal maps that are referred by
	 *         the keys, without counting the keys themselves
	 */
    public long getDeepSize() {
        long ret = 0;
        for (int key : this.relationMap.keySet()) {
            ret += this.relationMap.get(key).getDeepSize();
        }
        for (int key : this.relationSetByFirst.keySet()) {
            ret += this.relationSetByFirst.get(key).size();
        }
        for (int key : this.relationSetBySecond.keySet()) {
            ret += this.relationSetBySecond.get(key).size();
        }
        return ret;
    }

    @Override
    public Set<Integer> getElements() {
        return this.relationMap.keySet();
    }

    @Override
    public Collection<Integer> getRelationsByFirst(int first) {
        Collection<Integer> ret = this.relationSetByFirst.get(first);
        if (ret == null) {
            ret = Collections.emptySet();
        }
        return Collections.unmodifiableCollection(ret);
    }

    @Override
    public Collection<Integer> getRelationsBySecond(int second) {
        Collection<Integer> ret = this.relationSetBySecond.get(second);
        if (ret == null) {
            ret = Collections.emptySet();
        }
        return Collections.unmodifiableCollection(ret);
    }

    @Override
    public int hashCode() {
        return this.relationMap.hashCode();
    }

    @Override
    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        for (Integer relationId : getElements()) {
            sbuf.append(relationId);
            sbuf.append(" ");
            sbuf.append(this.relationMap.get(relationId).toString());
            sbuf.append("\n");
        }
        return sbuf.toString();
    }
}
