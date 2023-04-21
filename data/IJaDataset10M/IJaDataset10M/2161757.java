package com.antilia.hibernate.command;

import java.io.Serializable;
import java.util.Collection;
import org.hibernate.Session;

/**
 * 
 *
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class PersistAllCommand<E extends Serializable> extends AbstractPersistentCommand<E, Collection<E>> {

    private Collection<E> entities;

    @SuppressWarnings("unchecked")
    public PersistAllCommand(Collection<E> entities) {
        super((entities != null && entities.size() > 0) ? (Class<E>) entities.iterator().next().getClass() : null);
        this.entities = entities;
    }

    @Override
    protected Collection<E> doExecute() throws Throwable {
        Session session = getSession();
        for (E entity : getEntities()) {
            session.persist(entity);
        }
        return getEntities();
    }

    /**
	 * @return the entities
	 */
    public Collection<E> getEntities() {
        return entities;
    }
}
