package com.semp.jadoma.core.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import com.semp.jadoma.core.model.Dog;
import com.semp.jadoma.core.model.Dog.SexType;
import com.semp.jadoma.core.util.HibernateUtil;

public class DogDAO {

    private static final String FIND_LIKE_NAME = "from Dog d where d.name like :name";

    private static final String FIND_LIKE_NAME_AND_SEX = "from Dog d where d.name like :name and d.sex = :sex";

    private static final String FIND_LIKE_NAME_WITHOUT_FAMILY = "from Dog d where d.name like :name and d.familyId IS NULL";

    public static final Dog find(Long dogId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Dog toReturn = (Dog) session.get(Dog.class, dogId);
        session.close();
        return toReturn;
    }

    public static final List findLikeName(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery(FIND_LIKE_NAME);
        query.setParameter("name", "%" + name + "%");
        List toReturn = (List) query.list();
        session.close();
        return toReturn;
    }

    public static final List findLikeNameWithoutFamily(String name) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery(FIND_LIKE_NAME_WITHOUT_FAMILY);
        query.setParameter("name", "%" + name + "%");
        List toReturn = (List) query.list();
        session.close();
        return toReturn;
    }

    public static final List findLikeNameAndSex(String name, SexType sextype) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Query query = session.createQuery(FIND_LIKE_NAME_AND_SEX);
        query.setParameter("name", "%" + name + "%");
        query.setParameter("sex", sextype.getCode());
        List toReturn = (List) query.list();
        session.close();
        return toReturn;
    }

    public static final void update(Dog dog) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.saveOrUpdate(dog);
        session.getTransaction().commit();
        session.close();
    }
}
