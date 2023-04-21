package org.cleartk.classifier.util.featurevector;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * @author Philipp Wetzler
 * 
 */
public class ArrayFeatureVector extends FeatureVector {

    ArrayList<Double> features;

    public ArrayFeatureVector() {
        features = new ArrayList<Double>();
    }

    public double get(int index) {
        if (index < features.size()) return features.get(index); else return 0.0;
    }

    public java.util.Iterator<Entry> iterator() {
        return new Iterator(features);
    }

    public void set(int index, double value) throws InvalidFeatureVectorValueException {
        if (Double.isInfinite(value) || Double.isNaN(value)) throw new InvalidFeatureVectorValueException(index, value);
        for (int i = features.size(); i <= index; i++) features.add(0.0);
        features.set(index, value);
    }

    public double[] toArray() {
        double[] result = new double[features.size()];
        int index = 0;
        for (Double d : features) {
            result[index] = d;
            index += 1;
        }
        return result;
    }

    static class Iterator implements java.util.Iterator<FeatureVector.Entry> {

        int currentIndex;

        java.util.ListIterator<Double> subIterator;

        public Iterator(List<Double> features) {
            currentIndex = 0;
            subIterator = features.listIterator();
            moveToNext();
        }

        public boolean hasNext() {
            return subIterator.hasNext();
        }

        public Entry next() {
            int index = subIterator.nextIndex();
            Double value = subIterator.next();
            moveToNext();
            return new FeatureVector.Entry(index, value.doubleValue());
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        private void moveToNext() {
            while (subIterator.hasNext()) {
                Double d = subIterator.next();
                if (d != null && d.doubleValue() != 0.0) {
                    subIterator.previous();
                    return;
                }
            }
        }
    }
}
