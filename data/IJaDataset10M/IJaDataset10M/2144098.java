package org.contextor.content.context.term;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.contextor.content.term.SimpleTerm;
import org.contextor.content.term.Term;
import org.contextor.util.TermAndWeightComparator;

/**
 * 
 * 
 * 
 * @author Behrooz Nobakht [behrooz dot nobakht at gmail dot com]
 **/
public class SimpleWeightedTermContext extends SimpleTermContext implements WeightedTermContext {

    private static final long serialVersionUID = -5593863829363576695L;

    protected final Map<Term, Number> weights = new HashMap<Term, Number>();

    public SimpleWeightedTermContext() {
        super();
    }

    public SimpleWeightedTermContext(Map<Term, Number> terms) {
        init(terms);
    }

    public SimpleWeightedTermContext(int initialCapacity) {
        super(initialCapacity);
    }

    public SimpleWeightedTermContext(Collection<Term> terms) {
        for (Term term : terms) {
            add(term);
        }
    }

    @Override
    public boolean add(Term e) {
        boolean added = super.add(e);
        setWeight(e, 1.0);
        return added;
    }

    @Override
    public Term add(String term, Number weight) {
        Term t = new SimpleTerm(term);
        add(t, weight);
        return t;
    }

    @Override
    public void add(Term term, Number weight) {
        super.add(term);
        setWeight(term, weight);
    }

    @Override
    public void addAll(Collection<Entry<Term, Number>> terms) {
        for (Entry<Term, Number> entry : terms) {
            add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean remove(Object o) {
        int index = -1;
        do {
            index = indexOf(o);
            if (index != -1) {
                remove(index);
                weights.remove(o);
            }
        } while (index != -1);
        return true;
    }

    @Override
    public Map<Term, Number> getTermWeights() {
        return Collections.unmodifiableMap(weights);
    }

    @Override
    public Number getWeight(Term term) {
        return weights.get(term);
    }

    @Override
    public Number getWeight(String value) {
        Term term = get(value);
        if (null == term) {
            return null;
        }
        return getWeight(term);
    }

    @Override
    public Set<Term> getTermSet() {
        return weights.keySet();
    }

    @Override
    public List<Entry<Term, Number>> getTopOnes() {
        return getTopOnes(weights.size());
    }

    @Override
    public List<Entry<Term, Number>> getTopOnes(int count) {
        if (count > weights.size()) {
            count = weights.size();
        }
        List<Map.Entry<Term, Number>> sorted = new LinkedList<Entry<Term, Number>>(weights.entrySet());
        Collections.sort(sorted, getComparator());
        return sorted.subList(0, count);
    }

    @Override
    public WeightedTermContext merge(WeightedTermContext another) {
        if (null == another) {
            return this;
        }
        for (Term term : another.getTermSet()) {
            Number weight = getWeight(term);
            Number otherWeight = another.getWeight(term);
            weight = null == weight ? another.getWeight(term) : otherWeight.doubleValue() + weight.doubleValue();
            add(term, weight);
        }
        return this;
    }

    /**
	 * @param terms
	 */
    protected void init(Map<Term, Number> terms) {
        for (Map.Entry<Term, Number> e : terms.entrySet()) {
            add(e.getKey());
            setWeight(e.getKey(), e.getValue());
        }
    }

    /**
	 * @param term
	 * @param addingWeight
	 */
    protected void setWeight(Term term, Number addingWeight) {
        Number newWeight = getWeight(term);
        if (null == newWeight) {
            newWeight = 0.0;
        }
        newWeight = newWeight.doubleValue() + addingWeight.doubleValue();
        weights.put(term, newWeight);
    }

    /**
	 * @return
	 */
    protected Comparator<Map.Entry<Term, Number>> getComparator() {
        return Collections.reverseOrder(TermAndWeightComparator.INSTANCE);
    }
}
