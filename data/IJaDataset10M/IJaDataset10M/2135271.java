package br.ita.doacoes.core.cadastrodoacoes.dao.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.Query;
import br.ita.doacoes.core.cadastrodoacoes.dao.DAO;
import br.ita.doacoes.core.emf.EntityManagerFactorySingleton;
import br.ita.doacoes.core.templates.JPAUtil;

/**
 * @author Helder Baseado na GenericDAOJPA do Prof. Guerra
 */
public class DAOImpl<Tipo extends Object> implements DAO<Tipo> {

    protected EntityManagerFactory emf;

    private Class<Tipo> x;

    public DAOImpl(Class<Tipo> x) {
        this.x = x;
        emf = EntityManagerFactorySingleton.getEntityManagerFactory();
    }

    public void delete(Tipo o) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        t.begin();
        o = em.merge(o);
        em.remove(o);
        t.commit();
        em.close();
    }

    public Tipo getById(Tipo o) {
        Class y = x;
        Object idValue = null;
        while (idValue == null) {
            if (y.equals(Object.class)) {
                System.out.println("Anota��o de chave prim�ria n�o encontrada!");
                System.exit(-1);
            }
            try {
                for (Method f : y.getDeclaredMethods()) {
                    if (f.isAnnotationPresent(Id.class)) {
                        idValue = f.invoke(o);
                    }
                }
                for (Field f : y.getDeclaredFields()) if (f.isAnnotationPresent(Id.class)) {
                    String getterName = "get" + f.getName().substring(0, 1).toUpperCase() + f.getName().substring(1);
                    Method m = y.getMethod(getterName);
                    idValue = m.invoke(o);
                }
                y = y.getSuperclass();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        EntityManager em = emf.createEntityManager();
        Tipo retorno = em.find(x, idValue);
        em.close();
        return retorno;
    }

    @SuppressWarnings("unchecked")
    public List<Tipo> getList() {
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        t.begin();
        Query q = em.createQuery("select o from " + x.getName() + " o");
        List<Tipo> list = q.getResultList();
        t.commit();
        em.close();
        return list;
    }

    public void insert(Tipo o) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.persist(o);
        t.commit();
        em.close();
    }

    public void update(Tipo o) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        t.begin();
        em.merge(o);
        t.commit();
        em.close();
    }

    public List<Tipo> getByQuery(String query) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        t.begin();
        Query q = em.createQuery(query);
        List<Tipo> result = q.getResultList();
        t.commit();
        em.close();
        return result;
    }

    public List<Tipo> getBySQLQuery(String query, Class result) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        t.begin();
        Query q = em.createNativeQuery(query, result);
        List<Tipo> ret;
        if (query.startsWith("DELETE")) {
            ret = null;
            q.executeUpdate();
        } else {
            ret = q.getResultList();
        }
        t.commit();
        em.close();
        return ret;
    }
}
