package com.rcreations.persist.test;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 */
@Table(name = "AUTH_FEATURE")
public class FeatureDb {

    String id;

    String featureName;

    /**
    * Override so hashes can match different instances of the same primary key.
    * {@inheritDoc}
    */
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (getClass().isInstance(other) == false) {
            return false;
        }
        FeatureDb dbOther = (FeatureDb) other;
        return id.equals(dbOther.id);
    }

    /**
    * Override so hashes can match different instances of the same primary key.
    * {@inheritDoc}
    */
    public int hashCode() {
        return id.hashCode();
    }

    /**
    */
    @Column(name = "FEATURE_ID")
    public String getId() {
        return id;
    }

    /**
    */
    public void setId(String value) {
        id = value;
    }

    /**
    */
    @Column(name = "FEATURE_NAME")
    public String getFeatureName() {
        return featureName;
    }

    /**
    */
    public void setFeatureName(String value) {
        featureName = value;
    }
}
