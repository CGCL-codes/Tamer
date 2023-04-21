package com.objectcode.time4u.server.dao;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.objectcode.time4u.server.api.data.filter.IFilterVisitor;

public abstract class BaseFilterAdapter implements IFilterVisitor {

    Criteria criteria;

    protected BaseFilterAdapter(Criteria criteria) {
        this.criteria = criteria;
    }

    public void eq(Property property, Object value) {
        String propertyName = getPropertyName(property);
        if (propertyName != null) {
            criteria.add(Restrictions.eq(propertyName, value));
        }
    }

    public void ge(Property property, Object value) {
        String propertyName = getPropertyName(property);
        if (propertyName != null) {
            criteria.add(Restrictions.ge(propertyName, value));
        }
    }

    public void gt(Property property, Object value) {
        String propertyName = getPropertyName(property);
        if (propertyName != null) {
            criteria.add(Restrictions.gt(propertyName, value));
        }
    }

    public void in(Property property, List<?> values) {
        String propertyName = getPropertyName(property);
        if (propertyName != null) {
            if (values.size() == 1) {
                criteria.add(Restrictions.eq(propertyName, values.get(0)));
            } else {
                criteria.add(Restrictions.in(propertyName, values));
            }
        }
    }

    public void le(Property property, Object value) {
        String propertyName = getPropertyName(property);
        if (propertyName != null) {
            criteria.add(Restrictions.le(propertyName, value));
        }
    }

    public void lt(Property property, Object value) {
        String propertyName = getPropertyName(property);
        if (propertyName != null) {
            criteria.add(Restrictions.lt(propertyName, value));
        }
    }

    protected abstract String getPropertyName(Property property);
}
