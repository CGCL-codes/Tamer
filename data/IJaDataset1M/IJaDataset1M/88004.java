package com.hilaver.dzmis.dao;

import java.util.List;
import org.hibernate.Query;
import com.hilaver.dzmis.order.OrderColorTest;

public class OrderColorTestDAO extends BaseHibernateDAO {

    public List<Integer> getMaxReferenceIndex() throws Exception {
        String hql = "select max(referenceIndex) from " + OrderColorTest.class.getName();
        try {
            Query queryObject = getSession().createQuery(hql);
            return queryObject.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
