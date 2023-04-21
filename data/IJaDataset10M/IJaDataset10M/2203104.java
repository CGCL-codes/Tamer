package org.powerstone.smartpagination.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.powerstone.smartpagination.common.BaseQueryFormPagingController;

public abstract class BaseHibernateQueryFormPagingController extends BaseQueryFormPagingController<DetachedCriteria, Order> {
}
