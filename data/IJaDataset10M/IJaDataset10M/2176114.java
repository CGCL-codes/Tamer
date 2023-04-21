package org.jazzteam.snipple.data;

import java.util.List;
import org.jazzteam.snipple.model.Identifiable;

public abstract class DataServiceImp<T extends Identifiable> extends EntityManagerProvider {

    public void save(T data) {
        if (data.getId() == null) {
            getEntityManager().persist(data);
        } else {
            getEntityManager().merge(data);
        }
    }

    public abstract T find(long id);

    public void delete(long id) {
        T data = find(id);
        if (data != null) {
            getEntityManager().remove(data);
        }
    }

    public List<T> findAll() {
        return null;
    }
}
