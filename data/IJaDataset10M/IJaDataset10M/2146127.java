package br.gov.component.demoiselle.jpa.criteria.restriction;

import br.gov.component.demoiselle.jpa.criteria.Criteria;
import br.gov.component.demoiselle.jpa.criteria.Criterion;

/**
 * Logical expression.
 *
 * @author CETEC/CTJEE
 */
public class LogicalExpression implements Criterion {

    private final Criterion lhs;

    private final Criterion rhs;

    private final String operator;

    /**
     * Create new logical expression.
     *
     * @param lhs left critetion
     * @param rhs right criterion
     * @param operator operator
     */
    protected LogicalExpression(Criterion lhs, Criterion rhs, String operator) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.operator = operator;
    }

    /**
     * @see br.gov.component.demoiselle.jpa.criteria.Criterion#toSqlString(br.gov.component.demoiselle.jpa.criteria.Criteria, br.gov.component.demoiselle.jpa.criteria.Criteria.CriteriaQuery)
     */
    public String toSqlString(Criteria criteria, Criteria.CriteriaQuery criteriaQuery) {
        return "(" + lhs.toSqlString(criteria, criteriaQuery) + " " + operator + " " + rhs.toSqlString(criteria, criteriaQuery) + ")";
    }
}
