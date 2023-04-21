package org.imogene.common.dao.criteria;

import java.util.HashSet;
import java.util.Set;

public abstract class ImogJunction implements ImogCriterion {

    private Set<ImogCriterion> criterions = new HashSet<ImogCriterion>();

    /**
	 * Add a criterion
	 * @param criterion criterion to add
	 */
    public void add(ImogCriterion criterion) {
        criterions.add(criterion);
    }

    /**
	 * Get all criterions
	 * @return all criterions of this junction
	 */
    public Set<ImogCriterion> getCriterions() {
        return criterions;
    }

    /**
	 * Get the type of junction (ex: Disjunction, Conjunction ...)
	 * @return
	 */
    public abstract String getType();
}
