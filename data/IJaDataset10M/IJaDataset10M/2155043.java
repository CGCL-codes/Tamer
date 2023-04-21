package com.reserveamerica.elastica.cluster.factoryimpl.matcher;

import java.util.Collection;

/**
 * Performs an exact string match on the properties.
 * 
 * @author BStasyszyn
 */
public class EqualsPropertyMatcher extends StringPropertyMatcher {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(EqualsPropertyMatcher.class);

    private final boolean positive;

    /**
   * This constructor fully initializes the object.
   * 
   * @param id - The ID of the property matcher definition.
   * @param ownerId  - The ID of the delegate that owns this object.
   * @param name 
   * @param values - Collection< String>
   * @param ignoreCase boolean - if true then case is ignored when matching.
   */
    public EqualsPropertyMatcher(String id, String ownerId, String name, Collection<String> values, boolean positive, boolean ignoreCase) {
        super(id, ownerId, name, values, ignoreCase);
        this.positive = positive;
    }

    protected boolean isMatching(String val1, String val2) {
        if (log.isDebugEnabled()) log.debug("isMatching[" + id + ":" + ownerId + "] - val1=[" + val1 + "], val2=[" + val2 + "].");
        boolean result = (isIgnoreCase() ? val1.equalsIgnoreCase(val2) : val1.equals(val2));
        return positive ? result : !result;
    }
}
