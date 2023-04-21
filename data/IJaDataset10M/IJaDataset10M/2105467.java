package su.nsk.inp.roentgen.dao;

import java.util.List;
import su.nsk.inp.roentgen.model.AccessoryObject;

public interface AccessoryObjectDao<T extends AccessoryObject> {

    T fill(T aobject);

    T get(Long id);

    T save(T aobject);

    void remove(Long id);

    List<T> getAll();

    List<T> getAll(String query);
}
