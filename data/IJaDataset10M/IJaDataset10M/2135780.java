package com.reserveamerica.elastica.cluster.factoryimpl.matcher;

import java.util.Collection;
import com.reserveamerica.elastica.cluster.AbstractPropertyMatcher;

/**
 * This property matcher is an abstract class that is the base for all basic string property matchers.
 * 
 * @author BStasyszyn
 */
public abstract class StringPropertyMatcher extends AbstractPropertyMatcher {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StringPropertyMatcher.class);

    private final boolean ignoreCase;

    private final Collection<String> values;

    /**
   * This constructor fully initializes the object.
   *
   * @param id - The ID of the property matcher.
   * @param ownerId - The owner of the property.
   * @param name - The name of the property
   * @param values - Collection< String>
   * @param ignoreCase
   */
    public StringPropertyMatcher(String id, String ownerId, String name, Collection<String> values, boolean ignoreCase) {
        super(id, ownerId, name);
        this.values = values;
        this.ignoreCase = ignoreCase;
    }

    public boolean isMatching(Object value) {
        if (log.isDebugEnabled()) log.debug("isMatching[" + super.toString() + "] - value=[" + value + "].");
        for (String val : values) {
            if (value != null && isMatching(value.toString(), val)) {
                return true;
            }
        }
        return false;
    }

    /**
   * Indicates whther or not case should be a factor in comparisons.
   * 
   * @return - true if case should play a factor.
   */
    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }

    /**
   * Subclasses implement this method in order to perform implementation specific matching.
   * 
   * @param val1
   * @param val2
   * @return - Returns true if the two values match.
   */
    protected abstract boolean isMatching(String val1, String val2);
}
