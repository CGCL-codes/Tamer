package com.lb.trac.pojo.condition.order;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import com.lb.trac.pojo.comparator.OrderBy;

public class SortTable implements OrderCondition {

    private int type;

    private String property;

    public SortTable(int type, String property) {
        super();
        this.type = type;
        this.property = property;
    }

    @Override
    public Object create(Object... obj) {
        Criteria criteria = (Criteria) obj[0];
        String[] columnNames = StringUtils.split(property, ".");
        Criteria[] criterias = new Criteria[columnNames.length];
        criterias[0] = criteria;
        for (int i = 0; i < columnNames.length; i++) {
            String property = columnNames[i];
            if (i > 0) {
                criterias[i] = criterias[i - 1].createCriteria(columnNames[i - 1]);
            }
            if (i == columnNames.length - 1) {
                switch(type) {
                    case OrderBy.ASC:
                        criterias[i].addOrder(Order.asc(property));
                        break;
                    case OrderBy.DESC:
                        criterias[i].addOrder(Order.desc(property));
                        break;
                }
            }
        }
        return criteria;
    }

    @Override
    public void setType(int type) {
    }
}
