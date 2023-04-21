package com.lb.trac.pojo.condition.order;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.lb.trac.pojo.Utenti;
import com.lb.trac.pojo.comparator.OrderBy;
import com.lb.trac.util.SQLCondition;

public class FindUtentiOrderByCognome implements OrderCondition {

    private int type;

    public FindUtentiOrderByCognome(int type) {
        setType(type);
    }

    public FindUtentiOrderByCognome() {
    }

    @Override
    public Object create(Object... objects) {
        Criteria criteria = (Criteria) objects[0];
        return type == OrderBy.DESC ? criteria.addOrder(Order.desc("cognome")) : criteria.addOrder(Order.asc("cognome"));
    }

    @Override
    public void setType(int type) {
        this.type = type;
    }
}
