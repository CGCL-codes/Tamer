package org.cleartk.classifier.util.featurevector;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * 
 * 
 * @author Philipp Wetzler
 * 
 */
public class SparseFeatureVector extends FeatureVector {

    SortedMap<Integer, Double> values;

    public SparseFeatureVector() {
        values = new TreeMap<Integer, Double>();
    }

    public SparseFeatureVector(FeatureVector fv) throws InvalidFeatureVectorValueException {
        this();
        for (FeatureVector.Entry entry : fv) {
            this.set(entry.index, entry.value);
        }
    }

    public double get(int index) {
        Double returnValue = this.values.get(index);
        if (returnValue != null) return returnValue; else return 0.0;
    }

    public java.util.Iterator<Entry> iterator() {
        return new Iterator(this.values);
    }

    public void set(int index, double value) throws InvalidFeatureVectorValueException {
        if (Double.isInfinite(value) || Double.isNaN(value)) throw new InvalidFeatureVectorValueException(index, value);
        if (value != 0.0) this.values.put(index, value); else {
            if (this.values.containsKey(index)) this.values.remove(index);
        }
    }

    class Iterator implements java.util.Iterator<FeatureVector.Entry> {

        java.util.Iterator<Map.Entry<Integer, Double>> subIterator;

        SortedMap<Integer, Double> itValues;

        Iterator(SortedMap<Integer, Double> values) {
            this.itValues = values;
            this.subIterator = values.entrySet().iterator();
        }

        public boolean hasNext() {
            return this.subIterator.hasNext();
        }

        public FeatureVector.Entry next() {
            Map.Entry<Integer, Double> nextEntry = this.subIterator.next();
            int key = nextEntry.getKey();
            double value = nextEntry.getValue();
            return new FeatureVector.Entry(key, value);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public double innerProduct(FeatureVector other) {
        double result = 0.0;
        for (FeatureVector.Entry entry : this) {
            result += entry.value * other.get(entry.index);
        }
        return result;
    }
}
