package com.googlecode.gaal.analysis.impl;

import com.googlecode.gaal.analysis.api.IntervalSetBuilder;
import com.googlecode.gaal.data.api.IntervalSet;
import com.googlecode.gaal.suffix.api.BinaryIntervalTree;
import com.googlecode.gaal.suffix.api.SuffixArray;
import com.googlecode.gaal.suffix.api.BinaryIntervalTree.BinaryNode;

public class SupermaximalSetBuilder implements IntervalSetBuilder {

    @Override
    public <E extends BinaryNode<E>, T extends SuffixArray & BinaryIntervalTree<E>> IntervalSet<E> buildIntervalSet(T tree) {
        IntervalSet<E> distinctBwtSet = new NaiveDistinctBwtSetBuilder().buildIntervalSet(tree);
        distinctBwtSet.intersect(new LocalMaximumSetBuilder().buildIntervalSet(tree));
        return distinctBwtSet;
    }
}
