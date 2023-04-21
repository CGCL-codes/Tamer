package moduledefault.classify.c45.rafael.jadti;

import java.util.*;

/**
 * This class holds an ordered set of weighted items.<p>
 * All the methods of this class are similar to those of its superclass adapted
 * to handle weights.<p>
 * The algorithms involving unknown values are those of <i>C4.5</i> (Ross
 * Quinlan).
 **/
public class WeightedItemSet extends ItemSet {

    Vector weights;

    double weightsSum;

    /**
     * Builds a new empty weighted item set.
     *
     * @param attributeSet The set of attributes describing the items of the
     *                     set.
     **/
    public WeightedItemSet(AttributeSet attributeSet) {
        super(attributeSet);
        weights = new Vector();
        weightsSum = 0.;
    }

    /**
     * Adds a weighted item to the set.  This item must be compatible with this
     * set's attribute set.
     *
     * @param item An item.
     * @param weight The item weight.
     **/
    public void add(Item item, double weight) {
        super.add(item);
        weights.add(new Double(weight));
        weightsSum += weight;
    }

    /**
     * Adds a weighted item to the set.  This item must be compatible with this
     * set's attribute set.  The weight is set to 1.
     *
     * @param item An item.
     **/
    public void add(Item item) {
        add(item, 1.);
    }

    /**
     * Adds all the items of a vector.  All the elements of the vector must
     * be items compatible with this set's attribute set.
     *
     * @param items A vector of {@link Item Items}.
     * @param weights A vector of weights associated to items, represented as
     *                Double.
     **/
    public void add(Vector items, Vector weights) {
        if (items.size() != weights.size()) throw new IllegalArgumentException("Vectors must have the same " + "size");
        for (int i = 0; i < items.size(); i++) add((Item) items.elementAt(i), ((Double) weights.elementAt(i)).doubleValue());
    }

    /**
     * Builds a new weighted item set that contains the same items as an
     * {@link ItemSet ItemSet}, with weights set to 1.
     *
     * @param itemSet A set of items.
     **/
    public WeightedItemSet(ItemSet itemSet) {
        this(itemSet.attributeSet);
        for (int i = 0; i < itemSet.items.size(); i++) add((Item) itemSet.items.elementAt(i), 1.);
    }

    /**
     * Returns the sum of this set's items weights. (This can be see as the
     * size of this set if items are partially counted according to their
     * weight.)
     *
     * @return The sum of this set's items weights.
     **/
    public double size() {
        return weightsSum;
    }

    /**
     * Returns the weight associated to a specific item.
     *
     * @param index The index of the item.
     * @return The item weight.
     **/
    public double weight(int index) {
        return ((Double) weights.elementAt(index)).doubleValue();
    }

    protected TestScore bestSplitTest(Attribute testAttribute, SymbolicAttribute goalAttribute) {
        ItemSet knownItems = new ItemSet(attributeSet);
        int nbKnown = 0;
        for (int i = 0; i < items.size(); i++) {
            Item item = (Item) items.elementAt(i);
            if (!item.valueOf(attributeSet, testAttribute).isUnknown()) {
                knownItems.add(item);
                nbKnown++;
            }
        }
        if (nbKnown == 0) {
            Test test;
            if (testAttribute instanceof SymbolicAttribute) test = new SymbolicTest((SymbolicAttribute) testAttribute, new KnownSymbolicValue[] { new KnownSymbolicValue(0) }); else test = new NumericalTest((NumericalAttribute) testAttribute, 0.);
            return new TestScore(test, 0.);
        }
        TestScore knownTestScore = knownItems.bestSplitTest(testAttribute, goalAttribute);
        return new TestScore(knownTestScore.test, knownTestScore.score * (double) nbKnown / (double) items.size());
    }

    /**
     * Computes the entropy of the set regarding a given symbolic attribute.
     * The frequency of each value of this attribute is counted according to the
     * weights. The value of this attribute must be known for all the items of
     * this set.
     *
     * @param attribute The attribute agains which to compute the entropy.
     * @return The computed entropy.
     **/
    @Override
    public double entropy(SymbolicAttribute attribute) {
        if (!attributeSet.contains(attribute)) throw new IllegalArgumentException("Unknown attribute");
        if (entropy < 0. || !entropyAttribute.equals(attribute)) {
            double[] frequencies = new double[attribute.nbValues];
            for (int i = 0; i < items.size(); i++) {
                KnownSymbolicValue sv = (KnownSymbolicValue) item(i).valueOf(attributeSet.indexOf(attribute));
                frequencies[sv.intValue] += ((Double) weights.elementAt(i)).doubleValue();
            }
            entropy = Entropy.entropy(frequencies);
            entropyAttribute = attribute;
        }
        return entropy;
    }

    /**
     * Splits the set according to a test.
     *
     * @param test A test on an attribute of this set's items.
     * @return The resulting sets split by the test.  The <code>i</code>-th
     *         element of the array matches the <code>i</code>-th issue of the
     *         test.
     **/
    public ItemSet[] split(Test test) {
        WeightedItemSet[] sets = new WeightedItemSet[test.nbIssues()];
        double[] setSizes = new double[test.nbIssues()];
        double setSizeSum = 0.;
        Vector unknownItems = new Vector();
        Vector unknownItemsWeights = new Vector();
        for (int i = 0; i < sets.length; i++) sets[i] = new WeightedItemSet(attributeSet);
        for (int i = 0; i < items.size(); i++) {
            Item item = (Item) items.elementAt(i);
            double weight = ((Double) weights.elementAt(i)).doubleValue();
            AttributeValue value = item.valueOf(attributeSet, test.attribute);
            if (value.isUnknown()) {
                unknownItems.add(item);
                unknownItemsWeights.add(new Double(weight));
            } else sets[test.perform(value)].add(item, weight);
        }
        for (int i = 0; i < sets.length; i++) {
            setSizes[i] = sets[i].size();
            setSizeSum += setSizes[i];
        }
        for (int i = 0; i < unknownItems.size(); i++) {
            Item item = (Item) unknownItems.elementAt(i);
            double weight = ((Double) unknownItemsWeights.elementAt(i)).doubleValue();
            for (int i2 = 0; i2 < sets.length; i2++) sets[i2].add(item, weight * setSizes[i2] / setSizeSum);
        }
        return sets;
    }
}
