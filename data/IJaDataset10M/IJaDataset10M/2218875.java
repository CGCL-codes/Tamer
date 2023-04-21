package com.anasoft.os.daofusion.criteria;

import org.hibernate.Criteria;

/**
 * Visitor for specific {@link NestedPropertyCriterion} subclasses
 * that groups query constraint application logic operating on the
 * root {@link Criteria} instance.
 * 
 * @see NestedPropertyCriterion
 * @see NestedPropertyCriteria
 * 
 * @author vojtech.szocs
 */
public interface NestedPropertyCriterionVisitor {

    /**
	 * @param criterion Persistent entity property criterion to visit.
	 */
    void visit(FilterCriterion criterion);

    /**
	 * @param criterion Persistent entity property criterion to visit.
	 */
    void visit(SortCriterion criterion);
}
