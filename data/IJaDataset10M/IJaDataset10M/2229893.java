package com.hilaver.dzmis.dao;

import java.util.List;
import org.hibernate.Query;
import com.hilaver.dzmis.basicinfo.BiArmor;

public class BiArmorDAO extends BaseHibernateDAO {

    public List<Integer> getMaxReferenceIndex(String ctArmorType, String ctJauge) throws Exception {
        String hql = "select max(referenceIndex) from " + BiArmor.class.getName() + " where ctArmorType = ? and ctJauge = ?";
        try {
            Query queryObject = getSession().createQuery(hql);
            queryObject.setParameter(0, ctArmorType);
            queryObject.setParameter(1, ctJauge);
            return queryObject.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
