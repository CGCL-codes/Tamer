package org.objectstyle.cayenne.query;

import org.objectstyle.cayenne.exp.Expression;

/**
 * An abstract superclass of queries with Expression qualifiers.
 * 
 * @author Andrei Adamchik
 */
public abstract class QualifiedQuery extends AbstractQuery {

    protected Expression qualifier;

    /**
     * Sets new query qualifier.
     */
    public void setQualifier(Expression qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * Returns query qualifier.
     */
    public Expression getQualifier() {
        return qualifier;
    }

    /**
     * Adds specified qualifier to the existing qualifier joining it using "AND".
     */
    public void andQualifier(Expression e) {
        qualifier = (qualifier != null) ? qualifier.andExp(e) : e;
    }

    /**
     * Adds specified qualifier to the existing qualifier joining it using "OR".
     */
    public void orQualifier(Expression e) {
        qualifier = (qualifier != null) ? qualifier.orExp(e) : e;
    }
}
