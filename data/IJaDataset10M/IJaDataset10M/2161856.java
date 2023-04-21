package pl.edu.agh.ssm.persistence.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import pl.edu.agh.ssm.persistence.IEntity;

public interface GenericDao<E extends IEntity> {

    void delete(E entity) throws DataAccessException;

    void delete(Integer id) throws DataAccessException;

    void create(E entity) throws DataAccessException;

    void update(E entity) throws DataAccessException;

    boolean exist(Integer id) throws DataAccessException;

    List<E> getAll() throws DataAccessException;

    List<Integer> getAllIds() throws DataAccessException;

    E getById(Integer id) throws DataAccessException;

    Class<E> getEntityClass();

    void deleteAll();
}
