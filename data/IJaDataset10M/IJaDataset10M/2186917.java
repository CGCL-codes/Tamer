package org.helianto.process.filter.classic;

import org.helianto.core.criteria.OrmCriteriaBuilder;
import org.helianto.core.filter.classic.AbstractUserBackedCriteriaFilter;

/**
 * Cause filter.
 * 
 * @author Mauricio Fernandes de Castro
 */
public class CauseFilter extends AbstractUserBackedCriteriaFilter {

    private static final long serialVersionUID = 1L;

    @Override
    public void doFilter(OrmCriteriaBuilder mainCriteriaBuilder) {
    }

    @Override
    protected void doSelect(OrmCriteriaBuilder mainCriteriaBuilder) {
    }

    public String getObjectAlias() {
        return "cause";
    }

    public void reset() {
    }
}
