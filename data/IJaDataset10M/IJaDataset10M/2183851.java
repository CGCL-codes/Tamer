package org.goet.datamodel.impl;

import org.goet.datamodel.*;
import org.goet.datamodel.reflect.*;
import java.util.*;

public class RestrictionImpl extends NodeImpl implements Restriction {

    protected static final Set classes = Collections.singleton(RestrictionClass.getRestrictionClass());

    public RestrictionImpl(URI uri, DataCollection dc) {
        super(uri, classes, dc);
    }

    public RestrictionImpl(URI uri) {
        this(uri, null);
    }

    public Set getClasses() {
        return classes;
    }

    public Set getOnClasses() {
        Set v = getPropertyValues(RestrictionClass.ON_CLASSES);
        return v;
    }

    public Property getProperty() {
        return (Property) getFirstValue(RestrictionClass.ON_PROPERTY);
    }

    public Set getHasClasses() {
        Set v = getPropertyValues(RestrictionClass.HAS_CLASSES);
        return v;
    }

    public Set getHasClassesQ() {
        Set v = getPropertyValues(RestrictionClass.HAS_CLASSES_Q);
        return v;
    }

    public Value getHasValue() {
        return getFirstValue(RestrictionClass.HAS_VALUE);
    }

    protected Integer getIntValue(Property property) {
        TypedValue tv = (TypedValue) getFirstValue(property);
        if (tv == null) return null;
        return (Integer) tv.getValue();
    }

    public Integer getCardinality() {
        return getIntValue(RestrictionClass.HAS_CARDINALITY);
    }

    public Integer getCardinalityQ() {
        return getIntValue(RestrictionClass.HAS_CARDINALITY_Q);
    }

    public Integer getMaxCardinality() {
        return getIntValue(RestrictionClass.HAS_MAX_CARDINALITY);
    }

    public Integer getMaxCardinalityQ() {
        return getIntValue(RestrictionClass.HAS_MAX_CARDINALITY_Q);
    }

    public Integer getMinCardinality() {
        return getIntValue(RestrictionClass.HAS_MIN_CARDINALITY);
    }

    public Integer getMinCardinalityQ() {
        return getIntValue(RestrictionClass.HAS_MIN_CARDINALITY_Q);
    }

    public Set getToClass() {
        Set v = getPropertyValues(RestrictionClass.TO_CLASSES);
        return v;
    }

    public String toString() {
        return "[restriction] " + uri;
    }
}
