package com.dashboard.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author Joao
 * 
 */
public class GenericoDaoImpl<T, ID extends Serializable> implements GenericoDao<T, ID> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> classePersistente;

    /**
	 * Construtor default.
	 */
    @SuppressWarnings("unchecked")
    public GenericoDaoImpl() {
        this.classePersistente = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
	 * M&eacute;todo get da classe de persistencia manipulada.
	 * 
	 * @return a classe manipulada.
	 */
    public Class<T> getClassePersistente() {
        return classePersistente;
    }

    /**
	 * {@inheritDoc}
	 */
    public T recuperar(ID id) {
        return entityManager.find(classePersistente, id);
    }

    /**
	 * {@inheritDoc}
	 */
    public void persistir(T entidade) {
        entityManager.persist(entidade);
    }

    /**
	 * {@inheritDoc}
	 */
    public void atualizar(T entidade) {
        entityManager.merge(entidade);
    }

    /**
	 * {@inheritDoc}
	 */
    public void apagar(T entidade) {
        entityManager.remove(entidade);
    }

    /**
	 * {@inheritDoc}
	 */
    @SuppressWarnings("unchecked")
    public List<T> recuperarTodos() {
        return entityManager.createQuery("Select t from " + classePersistente.getSimpleName() + " t").getResultList();
    }

    /**
	 * M&eacute;todo set do entity manager.
	 * 
	 * @param entityManager a ser setado.
	 */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
