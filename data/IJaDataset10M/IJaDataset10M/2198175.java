package uturismu.service;

import java.io.Serializable;
import java.util.List;

/**
 * @author "LagrecaSpaccarotella" team.
 * 
 */
public interface GenericService<T extends Serializable> {

    public T findById(Long id);

    public List<T> findAll();

    public Long save(T entity);

    public void delete(T entity);

    public void update(T entity);

    public Long rowCount();
}
