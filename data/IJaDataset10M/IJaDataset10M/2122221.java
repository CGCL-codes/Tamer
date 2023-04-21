package com.ibm.JikesRVM.opt;

import java.util.Enumeration;

public abstract class OPT_SortedGraphNode extends OPT_SpaceEffGraphNode {

    public abstract Enumeration getInNodes();

    public abstract Enumeration getOutNodes();

    public OPT_SortedGraphNode getSortedNext(boolean forward) {
        if (forward) return sortedNext; else return sortedPrev;
    }

    public OPT_SortedGraphNode getForwardSortedNext() {
        return sortedNext;
    }

    public OPT_SortedGraphNode getBackwardSortedNext() {
        return sortedPrev;
    }

    public void setSortedNext(OPT_SortedGraphNode next, boolean forward) {
        if (forward) sortedNext = next; else sortedPrev = next;
    }

    public void setForwardSortNumber(int number) {
        forwardSortNumber = number;
    }

    public int getForwardSortNumber() {
        return forwardSortNumber;
    }

    public void setBackwardSortNumber(int number) {
        backwardSortNumber = number;
    }

    public int getBackwardSortNumber() {
        return backwardSortNumber;
    }

    public void setSortNumber(int number, boolean forward) {
        if (forward) forwardSortNumber = number; else backwardSortNumber = number;
    }

    public int getSortNumber(boolean forward) {
        if (forward) return forwardSortNumber; else return backwardSortNumber;
    }

    public void setSortNumber(int number) {
        forwardSortNumber = number;
    }

    public static int getNewSortMarker(OPT_SortedGraphNode anchor) {
        if (currentSortMarker == Integer.MAX_VALUE) {
            OPT_SortedGraphNode current;
            for (current = anchor; current != null; current = current.sortedPrev) {
                current.sortMarker = Integer.MIN_VALUE;
            }
            for (current = anchor; current != null; current = current.sortedNext) {
                current.sortMarker = Integer.MIN_VALUE;
            }
            currentSortMarker = Integer.MIN_VALUE;
        }
        ;
        return ++currentSortMarker;
    }

    int sortMarker = Integer.MIN_VALUE;

    private static int currentSortMarker = Integer.MIN_VALUE;

    public int getSortMarker() {
        return sortMarker;
    }

    public void setSortMarker(int sortMarker) {
        this.sortMarker = sortMarker;
    }

    public boolean isSortMarkedWith(int sortMarker) {
        return (this.sortMarker >= sortMarker);
    }

    protected OPT_SortedGraphNode sortedPrev = null, sortedNext = null;

    protected int forwardSortNumber;

    protected int backwardSortNumber;
}
