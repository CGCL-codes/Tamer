package org.esfinge.comparison.processor;

import org.esfinge.comparison.difference.Difference;

public interface ComparisonProcessor {

    public Difference compare(String prop, Object oldValue, Object newValue);
}
