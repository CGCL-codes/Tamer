package br.com.arsmachina.tapestrycrud.services;

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.corelib.components.ActionLink;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.corelib.components.PageLink;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;

/**
 * Service that informs the primary key field type of a given entity class. Implementations
 * are not obliged to provide this service for any entity class
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface PrimaryKeyTypeService {

    /**
	 * <p>
	 * Returns the {@link Class} instance representing the primary key field type for a given entity
	 * class. It must return null if the given class is not supported.
	 * </p>
	 * <p>
	 * The primary key field type of a given class may not be the one used as primary key column
	 * in the database. This can be used to provide prettier URLs for {@link ActionLink}s,
	 * {@link EventLink}s or {@link PageLink}s (when no {@link ActivationContextEncoder} is 
	 * explicitly provided and one is automatically created from a {@link PrimaryKeyEncoder}).  
	 * </p>
	 * 
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link Class} or null.
	 */
    @SuppressWarnings("unchecked")
    Class getPrimaryKeyType(Class entityClass);

    /**
	 * Returns the name of the property used as primary key in a given class. The same 
	 * assumptions made to {@link #getPrimaryKeyType(Class)} applies here.
	 * 
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link String} or null.
	 */
    @SuppressWarnings("unchecked")
    String getPrimaryKeyPropertyName(Class entityClass);
}
