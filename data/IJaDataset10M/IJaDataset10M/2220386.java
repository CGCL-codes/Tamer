package org.sgodden.query;

import java.util.ArrayList;
import java.util.List;

/**
 * A composite filter criterion, whose member criteria will be
 * appended to the query as an 'and' filter clause.
 * @author sgodden
 *
 */
public class AndRestriction implements CompositeRestriction {

    /**
     * The the list of criteria to be executed in an
     * 'and' relationship.
     */
    private List<Restriction> criteria = new ArrayList<Restriction>();

    /**
     * Create an AndRestriction with the specified criterions
     * @param criterion the criterion to add.
     */
    public AndRestriction() {
    }

    /**
     * Create an AndRestriction with the specified criterions
     * @param criterion the criterion to add.
     */
    public AndRestriction(Restriction... criterions) {
        for (Restriction r : criterions) criteria.add(r);
    }

    /**
     * Adds a criterion to the list that will be executed in
     * an 'and' relationship.
     * @param criterion the criterion to add.
     */
    public AndRestriction and(Restriction criterion) {
        criteria.add(criterion);
        return this;
    }

    /**
     * Returns the list of criteria to be executed in an
     * 'and' relationship.
     * @return the list of criteria.
     */
    public List<Restriction> getRestrictions() {
        return criteria;
    }
}
