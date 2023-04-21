package com.kescom.matrix.core.olap;

import org.hibernate.Criteria;

public interface ICriteriaEnhancer {

    Criteria enhanceCriteria(Criteria criteria);
}
