package su.nsk.inp.roentgen.dao;

import java.util.List;
import su.nsk.inp.roentgen.model.HierarchicalObject;

public interface HierarchicalObjectDao<T extends HierarchicalObject> {

    T saveChain(T hobject);

    T get(Long id);

    T save(T hobject);

    void remove(Long id);

    List<T> getAll();

    List<T> getAll(String query);
}
