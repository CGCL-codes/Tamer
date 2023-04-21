package org.middleheaven.util.criteria;

public interface Criteria<T> {

    /**
     * Retrieves a <code>LogicCriterion</code> of restrictions
     * @return
     */
    public LogicCriterion constraints();

    /**
	 * Adds restrictions criterion
	 * @param criterion
	 */
    public Criteria<T> add(Criterion criterion);
}
