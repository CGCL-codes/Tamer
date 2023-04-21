package com.yeep.study.spring.sample.dao;

import com.yeep.study.hibernate.pojo.TestUserExtInfo;

/**
 * @author Roger.Yee
 */
public class TestUserExtInfoDao extends BaseHibernateDaoImpl {

    protected Class getEntityClass() {
        return TestUserExtInfo.class;
    }
}
