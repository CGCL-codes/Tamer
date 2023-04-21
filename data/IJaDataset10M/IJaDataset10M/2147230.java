package com.google.code.projects.flex.business;

import com.google.code.projects.flex.model.MyUser;
import javax.persistence.PersistenceContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

@Transactional
public class MyServiceImpl implements MyService {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public MyServiceImpl() {
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(MyUser user) {
        if (user.getId() != null) {
            entityManager.merge(user);
        } else {
            entityManager.persist(user);
        }
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<MyUser> list() {
        Query query = entityManager.createQuery("select obj from MyUser obj order by obj.id");
        return query.getResultList();
    }

    ;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void remove(Long userId) {
        MyUser forDel = entityManager.find(MyUser.class, userId);
        if (forDel != null) {
            entityManager.remove(forDel);
        } else {
            System.out.println("for del is null !!!");
        }
    }

    ;
}
