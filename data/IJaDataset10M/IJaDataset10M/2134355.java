package br.com.gesclub.business.service;

import br.com.gesclub.business.model.BaseEntity;
import java.io.Serializable;

/**
 * Service com operações básicas de CRUD.
 * @param <T>
 */
public interface CrudService<T extends BaseEntity, ID extends Serializable> {

    /**
     * Persiste um objeto
     * @param t
     * @return a instância T modificada
     */
    ID save(T t);

    /**
     * Persiste ou atualiza um objeto.
     * @param t
     */
    void saveOrUpdate(T t);
}
