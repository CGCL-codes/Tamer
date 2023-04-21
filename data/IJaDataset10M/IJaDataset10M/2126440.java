package org.imogene.ws.criteria;

import java.util.HashSet;
import java.util.Set;

public abstract class MedooJunction implements MedooCriterion {

    private Set<MedooCriterion> criterions = new HashSet<MedooCriterion>();

    /**
	 * Add a criterion
	 * @param criterion criterion to add
	 */
    public void add(MedooCriterion criterion) {
        criterions.add(criterion);
    }

    /**
	 * Get all criterions
	 * @return all criterions of this junction
	 */
    public Set<MedooCriterion> getCriterions() {
        return criterions;
    }

    /**
	 * Get the type of junction (ex: Disjunction, Conjunction ...)
	 * @return
	 */
    public abstract String getType();
}
