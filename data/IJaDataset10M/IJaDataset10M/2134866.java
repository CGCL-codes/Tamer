package com.ma_la.myRunning.hibernateDao;

import com.ma_la.myRunning.dao.SpecialValuationDao;
import com.ma_la.myRunning.domain.SpecialValuation;

public class HibernateSpecialValuationDao extends HibernateGenericDao implements SpecialValuationDao {

    public HibernateSpecialValuationDao() {
        super(SpecialValuation.class);
    }
}
