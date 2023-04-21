package com.googlecode.gaal.analysis.impl;

import com.googlecode.gaal.analysis.api.IntervalSetBuilder;
import com.googlecode.gaal.data.api.IntSequence;
import com.googlecode.gaal.data.api.IntervalSet;
import com.googlecode.gaal.data.impl.IntervalBitSet;
import com.googlecode.gaal.suffix.api.BinaryIntervalTree;
import com.googlecode.gaal.suffix.api.BinaryIntervalTree.BinaryNode;
import com.googlecode.gaal.suffix.api.SuffixArray;

public class NaiveDistinctBwtSetBuilder implements IntervalSetBuilder {

    @Override
    public <E extends BinaryNode<E>, T extends SuffixArray & BinaryIntervalTree<E>> IntervalSet<E> buildIntervalSet(T tree) {
        IntervalSet<E> distinctBwtSet = new IntervalBitSet<E>(tree);
        traverse(distinctBwtSet, tree, tree.top());
        return distinctBwtSet;
    }

    private <E extends BinaryNode<E>> int[] traverse(IntervalSet<E> distinctBwtSet, SuffixArray sa, E interval) {
        if (interval.isTerminal()) {
            int loc = sa.getSuffixTable()[interval.left()];
            IntSequence sequence = sa.getSequence();
            if (loc == 0) return new int[] { sequence.get(sequence.size() - 1) }; else return new int[] { sequence.get(loc - 1) };
        } else {
            int leftBwt[] = traverse(distinctBwtSet, sa, interval.leftChild());
            int rightBwt[] = traverse(distinctBwtSet, sa, interval.rightChild());
            if (leftBwt != null && rightBwt != null && !intersect(leftBwt, rightBwt)) {
                distinctBwtSet.add(interval);
                int[] array = new int[leftBwt.length + rightBwt.length];
                for (int i = 0; i < leftBwt.length; i++) {
                    array[i] = leftBwt[i];
                }
                for (int j = 0; j < rightBwt.length; j++) {
                    array[j + leftBwt.length] = rightBwt[j];
                }
                return array;
            } else {
                return null;
            }
        }
    }

    private boolean intersect(int[] array1, int[] array2) {
        for (int i = 0; i < array1.length; i++) {
            for (int j = 0; j < array2.length; j++) {
                if (array1[i] == array2[j]) return true;
            }
        }
        return false;
    }
}
