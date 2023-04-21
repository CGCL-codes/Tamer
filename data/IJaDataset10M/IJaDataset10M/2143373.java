package net.sf.resultsetmapper.jelly;

import java.util.List;
import net.sf.resultsetmapper.jelly.dao.JellyDAO;

public abstract class JellyRepositorySupport<T extends Jelly> {

    protected JellyDAO<T> jellyDAO;

    public T find(Long jellyId) {
        return jellyDAO.find(jellyId);
    }

    public List<T> find() {
        return jellyDAO.find();
    }

    public void addAggregateClass(Class aggregateClass) {
        jellyDAO.addAggregateClass(aggregateClass);
    }
}
