package com.vividsolutions.jump.workbench.model;

/**
 * Whether a Feature was added, removed, or modified.
 */
public class FeatureEventType {

    public static final FeatureEventType ADDED = new FeatureEventType("ADDED");

    public static final FeatureEventType DELETED = new FeatureEventType("DELETED");

    public static final FeatureEventType GEOMETRY_MODIFIED = new FeatureEventType("GEOMETRY MODIFIED");

    public static final FeatureEventType ATTRIBUTES_MODIFIED = new FeatureEventType("ATTRIBUTES MODIFIED");

    public static final FeatureEventType RELOADED = new FeatureEventType("RELOADED");

    private String name;

    public String toString() {
        return name;
    }

    private FeatureEventType(String name) {
        this.name = name;
    }
}
