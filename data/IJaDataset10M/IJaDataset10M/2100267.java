package br.com.hsj.financeiro.dao;

import java.util.List;

/**
 * 
 * @author Hamilton dos Santos Junior
 *
 * Interface para disponibilização dos métodos genéricos disponíveis pelo JpaDAO
 *
 * @param <K> Id
 * @param <E> Classe
 */
public interface GenericDAO<K, E> {

    public void persist(E entity);

    public void remove(E entity);

    public E merge(E entity);

    public void refresh(E entity);

    public E findById(K id);

    public E flush(E entity);

    public List<E> findAll();
}
