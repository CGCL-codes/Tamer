package br.com.fiapbank.persistencia.dao;

import br.com.fiapbank.dominio.EntidadeBase;

/**
 * @author robson.oliveira
 *
 * @param <T>
 */
public interface GenericaDao<T extends EntidadeBase> {

    public T insert(T t) throws DaoException;

    public T update(T t) throws DaoException;

    public T find(T t) throws DaoException;

    public void delete(T t) throws DaoException;
}
