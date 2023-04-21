package net.sourceforge.jfl.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.sourceforge.jfl.JFLConfiguration;
import net.sourceforge.jfl.core.interval.Interval;
import net.sourceforge.jfl.core.operators.IBinaryOperator;

/**
 * This class implements the <code>IFuzzySet</code> interface, backed by a <code>HashMap</code> instance.
 * This implementation not allows <code>null<code> element.
 * 
 * 
 * @author arons777@users.sourceforge.net
 *
 * @param <T>
 */
public class DiscreteFuzzySet<T> implements IFuzzySet<T>, Cloneable {

    protected String label;

    protected Map<T, Double> map;

    protected boolean complementary;

    /** */
    public DiscreteFuzzySet() {
        map = new HashMap<T, Double>();
    }

    /**
     * @param initialCapacity initial capacity of the set.
     */
    public DiscreteFuzzySet(int initialCapacity) {
        map = new HashMap<T, Double>(initialCapacity);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getMembership(T element) {
        if (element == null) return 0.0;
        Double membership = map.get(element);
        double result;
        if (membership == null) result = 0.0; else result = membership.doubleValue();
        return complementary ? 1.0 - result : result;
    }

    /**
     * @return null if there no posibility to know the support. (example if this set is a complement 
     * of a set).
     */
    public Set<T> getSupport() {
        if (complementary) return null;
        return map.keySet();
    }

    public Set<Interval> getSupportIntervals() {
        return null;
    }

    /**
     *  If the fuzzy set previously contained an element of the set, 
     *  the old membership value of this element is replaced by membership passed. 
     */
    public void addAllElement(Set<T> elements, double membership) {
        if (elements == null) return;
        for (T element : elements) {
            addElement(element, membership);
        }
    }

    /**
     * Add the element to the set.
     *  If the fuzzy set previously contained the element, 
     *  the old membership value is replaced by membership passed. 
     *  
     * @param element the element to add
     * @param membership the membership values to set.
     */
    public void addElement(T element, double membership) {
        if (element == null || membership < 0.0 || membership > 1.0) return;
        double value = complementary ? 1.0 - membership : membership;
        if (!(!complementary && value <= 0.0)) {
            map.put(element, Double.valueOf(value));
        }
    }

    /**
     * Remove the element from the set.
     * @param element
     * @return the old memebership values
     */
    public double removeElement(T element) {
        Double value = null;
        if (complementary) {
            addElement(element, 0.0);
        } else {
            value = map.remove(element);
        }
        double result = value == null ? 0.0 : value.doubleValue();
        return complementary ? 1.0 - result : result;
    }

    /**
     * Create the complement of the fuzzy set.
     * @return
     */
    public DiscreteFuzzySet<T> complement() {
        DiscreteFuzzySet<T> result = new DiscreteFuzzySet<T>(this.map.size());
        result.complementary = !this.complementary;
        for (T element : this.map.keySet()) {
            result.addElement(element, 1.0 - this.getMembership(element));
        }
        return result;
    }

    @Override
    public DiscreteFuzzySet<T> clone() {
        DiscreteFuzzySet<T> result = new DiscreteFuzzySet<T>(this.map.size());
        result.complementary = this.complementary;
        result.map.putAll(this.map);
        result.label = this.label;
        return result;
    }

    public DiscreteFuzzySet<T> and(IFuzzySet<T> anotherSet) {
        if (anotherSet == null) return this.clone();
        DiscreteFuzzySet<T> castedSet = (DiscreteFuzzySet<T>) anotherSet;
        return computeBinaryOperatorOverSets(this, castedSet, JFLConfiguration.getAnd(), this.complementary && castedSet.complementary);
    }

    public DiscreteFuzzySet<T> or(IFuzzySet<T> anotherSet) {
        if (anotherSet == null) return this.clone();
        DiscreteFuzzySet<T> castedSet = (DiscreteFuzzySet<T>) anotherSet;
        return computeBinaryOperatorOverSets(this, castedSet, JFLConfiguration.getOr(), this.complementary || castedSet.complementary);
    }

    /**
     * Compute a IBinaryOperator passed over the two DiscreteFuzzySet.
     * 
     * @param one
     * @param two
     * @param operator
     * @return the result of the binary operation, never null.
     */
    private DiscreteFuzzySet<T> computeBinaryOperatorOverSets(DiscreteFuzzySet<T> one, DiscreteFuzzySet<T> two, IBinaryOperator operator, boolean complementary) {
        Set<T> unionOfSet = new HashSet<T>(one.map.size() + two.map.size());
        unionOfSet.addAll(one.map.keySet());
        unionOfSet.addAll(two.map.keySet());
        DiscreteFuzzySet<T> result = new DiscreteFuzzySet<T>();
        result.complementary = complementary;
        for (T element : unionOfSet) {
            double value = operator.compute(one.getMembership(element), two.getMembership(element));
            result.addElement(element, value);
        }
        return result;
    }
}
