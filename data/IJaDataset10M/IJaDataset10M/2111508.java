package com.criteria2jpql.criteria.restrictions;

/**
 * @author Omer Sunercan
 */
public class NotEmptyExpression extends AbstractEmptinessExpression implements Criterion {

    protected NotEmptyExpression(String propertyName) {
        super(propertyName);
    }

    protected boolean excludeEmpty() {
        return true;
    }
}
