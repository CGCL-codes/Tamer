package es.org.nms.common;

import java.io.Serializable;

/**
 * The Nameable interface.
 * <p>
 * Entities with a name must implement this interface.
 * </p>
 * 
 * @author daviz
 * 
 * @param <T>
 *            The name type (typically String)
 */
public interface INameable<T extends Serializable> {

    /**
	 * Obtain the name.
	 * 
	 * @return The name.
	 */
    public T getName();

    /**
	 * Set the name.
	 * 
	 * @param name
	 *            The name.
	 */
    public void setName(T name);
}
