package com.zhongkai.dao.book;

import java.util.List;
import org.springframework.stereotype.Component;
import com.zhongkai.dao.BaseDao;

@Component
public class TDjCcdjxxDAO extends BaseDao {

    public List findByHql(String hql, Object[] values) {
        return getHibernateTemplate().find(hql, values);
    }
}
