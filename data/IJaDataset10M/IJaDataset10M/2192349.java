package edu.nus.iss.ejava.team4.persist.dao;

import java.io.Serializable;
import java.util.List;
import edu.nus.iss.ejava.team4.ejb.exception.DAOException;

public interface AssessItDAO<T, ID extends Serializable> {

    public abstract T create(T type) throws DAOException;

    public abstract List<T> create(List<T> type) throws DAOException;

    public abstract void delete(T type) throws DAOException;

    public abstract void save(T type) throws DAOException;

    public List<T> deleteAll(List<T> type) throws DAOException;

    public List<T> updateAll(List<T> type) throws DAOException;
}
