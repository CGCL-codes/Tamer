package de.privat.ID_System.persistence.dao;

import java.io.Serializable;
import org.hibernate.Session;
import de.privat.ID_System.domain.AbstractEntity;

public interface GenericDAO<E extends AbstractEntity, ID extends Serializable> {

    /**
	 * Get an entity using {@link Session#get(Class, Serializable)}.
	 *
	 * @param id	The id of the object.
	 * @return		The object.
	 *
	 * @throws Exception	On Error.
	 */
    public abstract E get(final ID id) throws Exception;

    /**
	 * Save an entity using {@link Session#save(Object)}.
	 *
	 * @param entity	The entity.
	 * @return	The entity with its new primary key.
	 *
	 * @throws Exception	On error.
	 */
    public abstract E insert(final E entity) throws Exception;
}
