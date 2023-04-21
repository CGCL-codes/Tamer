package fr.xebia.demo.wicket.blog.service;

import java.io.Serializable;
import java.util.List;

/**
 * 
 */
public interface Service<T> {

    /**
     * Renvoi le nombre d'enregistrement maximum ramen� par une recherche ou une r�cup�ratio nde liste d'objets
     */
    public abstract int getMaxResults();

    public abstract void save(T entity) throws ServiceException;

    public abstract T update(T entity) throws ServiceException;

    public abstract void deleteById(Serializable id) throws ServiceException;

    public abstract void delete(T entity) throws ServiceException;

    public abstract T get(Serializable id) throws ServiceException;

    public abstract List<T> list() throws ServiceException;

    public abstract List<T> search(T exampleEntity) throws ServiceException;
}
