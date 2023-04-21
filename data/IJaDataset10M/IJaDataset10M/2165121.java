package br.gov.component.demoiselle.jpa.criteria.restriction;

import br.gov.component.demoiselle.jpa.criteria.Criteria;
import br.gov.component.demoiselle.jpa.criteria.Criterion;

/**
 * Abstract emptiness expression.
 *
 * @author CETEC/CTJEE
 */
public abstract class AbstractEmptinessExpression implements Criterion {

    protected final String property;

    /**
     * Create new emptiness expression.
     *
     * @param property property
     */
    protected AbstractEmptinessExpression(String property) {
        this.property = property;
    }

    /**
     * Check if exclude empty.
     *
     * @return true if exlude empry
     */
    protected abstract boolean excludeEmpty();

    /**
     * @see br.gov.component.demoiselle.jpa.criteria.Criterion#toSqlString(br.gov.component.demoiselle.jpa.criteria.Criteria, br.gov.component.demoiselle.jpa.criteria.Criteria.CriteriaQuery)
     */
    public final String toSqlString(Criteria criteria, Criteria.CriteriaQuery criteriaQuery) {
        return (excludeEmpty() ? "exists" : "not exists") + " (select 1 from " + criteriaQuery.getPropertyName(property, criteria) + ")";
    }
}
