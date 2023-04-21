package br.com.caelum.notasfiscais.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public class DAO<T> {

    private final Class<T> classe;

    public DAO(Class<T> classe) {
        this.classe = classe;
    }

    public void adiciona(T t) {
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        em.persist(t);
        em.getTransaction().commit();
        em.close();
    }

    public void remove(T t) {
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        em.remove(em.merge(t));
        em.getTransaction().commit();
        em.close();
    }

    public void atualiza(T t) {
        EntityManager em = new JPAUtil().getEntityManager();
        em.getTransaction().begin();
        em.merge(t);
        em.getTransaction().commit();
        em.close();
    }

    public List<T> listaTodos() {
        EntityManager em = new JPAUtil().getEntityManager();
        CriteriaQuery<T> query = em.getCriteriaBuilder().createQuery(classe);
        query.select(query.from(classe));
        List<T> lista = em.createQuery(query).getResultList();
        em.close();
        return lista;
    }

    public T buscaPorId(Long id) {
        return new JPAUtil().getEntityManager().find(classe, id);
    }

    public Long quantidadeDeRegistros() {
        return (Long) new JPAUtil().getEntityManager().createQuery("select count (*) from " + classe.getName()).getSingleResult();
    }

    public List<T> listaTodosPaginado(int inicio, int quantidade) {
        return new JPAUtil().getEntityManager().createQuery(" from " + classe.getName()).setFirstResult(inicio).setMaxResults(quantidade).getResultList();
    }
}
