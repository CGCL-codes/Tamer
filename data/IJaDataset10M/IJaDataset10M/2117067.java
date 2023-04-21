package org.usca.workshift.webservices.jaxws;

import org.usca.workshift.database.model.BaseModel;
import org.usca.workshift.database.dao.GenericDAO;
import javax.jws.WebMethod;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class GenericService<T extends BaseModel, S extends GenericDAO<T>> {

    protected S dao;

    protected GenericService() {
        try {
            @SuppressWarnings("unchecked") Class<S> beanType = (Class<S>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1];
            dao = beanType.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dao == null) {
            throw new IllegalArgumentException("could not determine the generic type for this class please specify the class in the other constructor");
        }
    }

    @WebMethod
    public T findById(Long id) {
        return dao.findById(id);
    }

    @WebMethod
    List<T> findAll() {
        return dao.findAll();
    }

    @WebMethod
    List<T> listAll(Long lowerId) {
        return dao.listAll(lowerId);
    }

    @WebMethod
    void delete(T entity) {
        dao.delete(entity);
    }

    @WebMethod
    void saveOrUpdate(T entity) {
        dao.saveOrUpdate(entity);
    }
}
