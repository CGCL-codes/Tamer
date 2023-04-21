package net.woodstock.rockapi.persistence.wrapper;

import java.io.Serializable;
import net.woodstock.rockapi.persistence.PersistenceException;
import net.woodstock.rockapi.pojo.Pojo;

public interface Manager {

    void delete(Pojo o) throws PersistenceException;

    void flush() throws PersistenceException;

    Pojo get(Serializable id, Class<? extends Pojo> clazz) throws PersistenceException;

    void insert(Pojo o) throws PersistenceException;

    void update(Pojo o) throws PersistenceException;

    Transaction getTransaction() throws PersistenceException;

    void setTransaction(Transaction transaction) throws PersistenceException;
}
