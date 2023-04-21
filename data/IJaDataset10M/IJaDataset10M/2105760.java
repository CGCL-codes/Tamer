package com.googlecode.gaal.suffix.api;

import com.googlecode.gaal.data.api.IntSequence;
import com.googlecode.gaal.suffix.api.BinaryIntervalTree.BinaryNode;

public interface BinaryIntervalTree<T extends BinaryNode<T>> extends IntervalTree<T> {

    public int[] getChildTable();

    public int[] getExtendedLcpTable();

    public T search(IntSequence pattern);

    public T longestSearch(IntSequence pattern);

    public interface BinaryNode<T extends Interval> extends Interval {

        public int middle();

        public T leftChild();

        public T rightChild();
    }
}
