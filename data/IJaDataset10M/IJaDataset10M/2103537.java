package com.threerings.jpkg.ant.dpkg.dependencies.conditions;

import com.threerings.jpkg.debian.dependency.DependencyRelationships;

/**
 * A {@link Condition} describing the {@link DependencyRelationships#STRICTLY_LATER} condition.
 */
public class GreaterThan extends Condition {

    public String getFieldName() {
        return "greaterThan";
    }

    @Override
    public DependencyRelationships getRelationship() {
        return DependencyRelationships.STRICTLY_LATER;
    }
}
