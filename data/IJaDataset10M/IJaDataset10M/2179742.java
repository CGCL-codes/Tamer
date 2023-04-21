package ao.prophet.impl;

import ao.prophet.impl.cluster.FinalPly;
import ao.prophet.impl.cluster.LeafCluster;
import ao.prophet.ItemFilter;
import java.util.*;

/**
 * Stores the asFloat associated with each item and allows
 *  to pic the lightest or heaviest ones.
 *
 * One of these for each user for who predictions are being made.
 */
public class ItemWeights<I> {

    private final float WEIGHTS[];

    private final FinalPly.Lookup<I> FOR_PLY;

    public ItemWeights(int size, FinalPly.Lookup<I> forPly) {
        WEIGHTS = new float[size];
        FOR_PLY = forPly;
    }

    public void add(int label, float weight) {
        WEIGHTS[label] += weight;
    }

    public void add(int headLabel, int tailLabel, float weight) {
        for (int label = headLabel; label <= tailLabel; label++) {
            WEIGHTS[label] += weight;
        }
    }

    public void clear() {
        Arrays.fill(WEIGHTS, 0.0f);
    }

    public Collection<I> heaviest(int howMany, ItemFilter<I> filter) {
        if (WEIGHTS.length == 0) return Collections.emptyList();
        SortedSet<WeightedItemLabel> weightedItemLabels = new TreeSet<WeightedItemLabel>();
        int curInitIndex = 0;
        for (int added = 0; curInitIndex < WEIGHTS.length && added < howMany; curInitIndex++) {
            I item = FOR_PLY.itemLabled(curInitIndex);
            if (!filter.accept(item)) continue;
            weightedItemLabels.add(new WeightedItemLabel(curInitIndex, WEIGHTS[curInitIndex]));
            added++;
        }
        float minHeavyWeight = weightedItemLabels.first().weight;
        for (int i = curInitIndex; i < WEIGHTS.length; i++) {
            if (!filter.accept(FOR_PLY.itemLabled(i))) continue;
            if (WEIGHTS[i] > minHeavyWeight) {
                WeightedItemLabel lightestHeavyWeight = weightedItemLabels.first();
                weightedItemLabels.remove(lightestHeavyWeight);
                lightestHeavyWeight.set(i, WEIGHTS[i]);
                weightedItemLabels.add(lightestHeavyWeight);
                minHeavyWeight = weightedItemLabels.first().weight;
            }
        }
        LinkedList<I> heaviest = new LinkedList<I>();
        for (WeightedItemLabel weightedItemLabel : weightedItemLabels) {
            heaviest.addFirst(FOR_PLY.itemLabled(weightedItemLabel.label));
        }
        return heaviest;
    }

    public static class Translator<I> {

        private final FinalPly.Lookup<I> OLD_PLY;

        private final FinalPly.Lookup<I> NEW_PLY;

        private final int NEW_FROM_OLD[];

        public Translator(int newFromOld[], FinalPly.Lookup<I> oldPly, FinalPly.Lookup<I> newPly) {
            OLD_PLY = oldPly;
            NEW_PLY = newPly;
            NEW_FROM_OLD = newFromOld;
        }

        public ItemWeights<I> translate(ItemWeights<I> from, float commonFactor) {
            if (from.FOR_PLY == NEW_PLY) return from;
            ItemWeights<I> to = NEW_PLY.newWeights();
            if (from.FOR_PLY == OLD_PLY) {
                fastTranslate(from, to, commonFactor);
            } else {
                generalTranslate(from, to, commonFactor);
            }
            return to;
        }

        private void generalTranslate(ItemWeights<I> from, ItemWeights<I> to, float commonFactor) {
            for (int label = 0; label < from.WEIGHTS.length; label++) {
                I item = from.FOR_PLY.itemLabled(label);
                LeafCluster<I> toCluster = to.FOR_PLY.leafClusterOf(item);
                if (toCluster != null) {
                    to.WEIGHTS[toCluster.label()] = commonFactor * from.WEIGHTS[label];
                }
            }
        }

        private void fastTranslate(ItemWeights<I> from, ItemWeights<I> to, float commonFactor) {
            for (int i = 0; i < NEW_FROM_OLD.length; i++) {
                if (NEW_FROM_OLD[i] >= 0) {
                    to.WEIGHTS[i] = commonFactor * from.WEIGHTS[NEW_FROM_OLD[i]];
                }
            }
        }
    }

    private static class WeightedItemLabel implements Comparable<WeightedItemLabel> {

        public int label;

        public float weight;

        public WeightedItemLabel(int label, float weight) {
            set(label, weight);
        }

        public void set(int label, float weight) {
            this.label = label;
            this.weight = weight;
        }

        public int compareTo(WeightedItemLabel o) {
            int weightCmp = Float.compare(weight, o.weight);
            return (weightCmp != 0 ? weightCmp : (label - o.label));
        }

        public boolean equals(Object obj) {
            assert obj instanceof WeightedItemLabel;
            return label == ((WeightedItemLabel) obj).label;
        }
    }
}
