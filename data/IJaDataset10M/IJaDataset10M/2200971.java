package tinlizard.dao;

import tinlizard.model.PersistentObject;
import java.util.List;

public interface PersistentObjectDao<E extends PersistentObject> {

    void add(E obj);

    void update(E obj);

    void delete(E obj);

    E findByPrimaryKey(Integer id);

    List<E> findAll();
}
