package org.anddev.andengine.util;

import java.util.ArrayList;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 16:54:24 - 07.11.2010
 */
public class ProbabilityGenerator<T> {

    private float mProbabilitySum;

    private final ArrayList<Entry<T>> mEntries = new ArrayList<Entry<T>>();

    public void add(final float pFactor, final T... pElements) {
        this.mProbabilitySum += pFactor;
        this.mEntries.add(new Entry<T>(pFactor, pElements));
    }

    public T next() {
        float random = MathUtils.random(0, this.mProbabilitySum);
        final ArrayList<Entry<T>> factors = this.mEntries;
        for (int i = factors.size() - 1; i >= 0; i--) {
            final Entry<T> entry = factors.get(i);
            random -= entry.mFactor;
            if (random <= 0) {
                return entry.getReturnValue();
            }
        }
        final Entry<T> lastEntry = factors.get(factors.size() - 1);
        return lastEntry.getReturnValue();
    }

    public void clear() {
        this.mProbabilitySum = 0;
        this.mEntries.clear();
    }

    private static class Entry<T> {

        public final float mFactor;

        public final T[] mData;

        public Entry(final float pFactor, final T[] pData) {
            this.mFactor = pFactor;
            this.mData = pData;
        }

        public T getReturnValue() {
            if (this.mData.length == 1) {
                return this.mData[0];
            } else {
                return ArrayUtils.random(mData);
            }
        }
    }
}
