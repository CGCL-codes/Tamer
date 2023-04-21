package com.ncs.mail.dao.impl;

import com.ncs.common.dao.impl.BaseDaoImpl;
import com.ncs.mail.dao.StateDao;
import com.ncs.mail.to.StateTo;
import org.springframework.stereotype.Repository;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.util.List;

@Repository("mailStateDao")
public class StateDaoImpl extends BaseDaoImpl<StateTo, String> implements StateDao {

    @Resource
    private EntityManagerFactory entityManagerFactory;

    public List<StateTo> getAllState() {
        Query query = entityManager.createQuery("select s from StateTo s order by s.name");
        return query.getResultList();
    }
}
