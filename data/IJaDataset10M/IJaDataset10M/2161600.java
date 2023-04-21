package org.cleartk.classifier.feature;

import java.util.Collection;
import java.util.Map;

/**
 * <br>
 * Copyright (c) 2009, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * @author Philip Wetzler
 */
public class Counts {

    public Counts(String featureName, String identifier, Map<? extends Object, Integer> countsMap) {
        this.featureName = featureName;
        this.identifier = identifier;
        this.countsMap = countsMap;
    }

    public Counts(String featureName, Map<? extends Object, Integer> countsMap) {
        this(featureName, null, countsMap);
    }

    public String getFeatureName() {
        return featureName;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Collection<? extends Object> getValues() {
        return countsMap.keySet();
    }

    public int getCount(Object o) {
        return countsMap.get(o);
    }

    public int getTotalCount() {
        int totalCount = 0;
        for (Object o : countsMap.keySet()) totalCount += countsMap.get(o);
        return totalCount;
    }

    private String featureName;

    private String identifier;

    private Map<? extends Object, Integer> countsMap;
}
