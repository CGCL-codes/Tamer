package com.hilaver.dzmis.dao;

import java.util.List;
import org.hibernate.Query;
import com.hilaver.dzmis.basicinfo.BiSketch;

public class BiSketchDAO extends BaseHibernateDAO {

    public List<Integer> getMaxReferenceIndex(String ctSketchType, String ctCollarType) throws Exception {
        String hql = "select max(referenceIndex) from " + BiSketch.class.getName() + " where ctSketchType = ? and ctCollarType = ?";
        try {
            Query queryObject = getSession().createQuery(hql);
            queryObject.setParameter(0, ctSketchType);
            queryObject.setParameter(1, ctCollarType);
            return queryObject.list();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
