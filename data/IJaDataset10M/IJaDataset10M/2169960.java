package fi.arcusys.commons.j2ee.resolver;

import java.io.Serializable;
import org.hibernate.Session;

/**
 * An intereface for specifying information and operations on referring 
 * entities in an entity delete action.
 * 
 * <p>Referring entity is an entity that refers to the entity to be deleted.</p>
 * 
 * @todo FIXME the implementation logic in doCascade() MUST be changed: the referring entity should implement it and referred should just delegate
 * @author mikko
 * @version 1.1 $Rev$
 * @see ReferredEntity
 * @see ReferenceResolver
 */
public interface ReferringEntity {

    /**
	 * The cascade operation for the referring entity when the referred entity
	 * is deleted.
	 * @return a <code>CascadeOp</code>
	 */
    CascadeOp getCascadeOp();

    /**
	 * Identifier of the referring entity.
	 * @return Identifier of the referring entity.
	 */
    Serializable getEntityId();

    /**
	 * The referring entity object.
	 * 
	 * @return the referring entity object
	 */
    Object getEntity();

    /**
	 * Called to do the specified {@link CascadeOp} operation before the
	 * referred entity is deleted.
	 * 
	 * @param referredEntity the referred entity i.e. the entity to be deleted
	 * @param session the current <code>Session</code>
	 */
    void doCascade(ReferredEntity referredEntity, Session session);

    ReferringEntity getParent();

    int getNestingLevel();
}
