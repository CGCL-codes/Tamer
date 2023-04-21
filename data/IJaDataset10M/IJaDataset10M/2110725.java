package org.dueam.mall.dao;

import org.dueam.common.orm.hibernate.HibernateDao;
import org.dueam.mall.entity.KeyValueEntity;
import org.springframework.stereotype.Repository;

@Repository
public class KeyValueDAO extends HibernateDao<KeyValueEntity, String> {
}
