package com.googlecode.mjorm.query;

import com.mongodb.BasicDBObject;

public class ElemMatchCriterion implements Criterion {

    private Query queryCriterion;

    public ElemMatchCriterion(Query queryCriterion) {
        this.queryCriterion = queryCriterion;
    }

    public ElemMatchCriterion() {
        this.queryCriterion = new Query();
    }

    /**
	 * @return the queryCriterion
	 */
    public Query getQuery() {
        return queryCriterion;
    }

    /**
	 * {@inheritDoc}
	 */
    public Object toQueryObject() {
        return new BasicDBObject("$elemMatch", queryCriterion.toQueryObject());
    }
}
