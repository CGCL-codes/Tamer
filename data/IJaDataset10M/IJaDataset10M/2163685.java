package shiva.cfg.registrator;

import java.util.Collection;
import java.util.Set;

/**
 * @author Paulo Vitor
 * @author Roberto Su
 * 
 * @description
 *
 * @param <E>
 */
public interface Registrable<E> {

    /**
	 * 
	 * @param element
	 */
    void register(E element);

    /**
	 * 
	 * @param elements
	 */
    void registerAll(Collection<E> elements);

    /**
	 * 
	 * @param element
	 */
    void unregister(E element);

    /**
	 * 
	 * @param elements
	 */
    void unregisterAll(Collection<E> elements);

    /**
	 * 
	 * @return
	 */
    Set<E> getRegisteredObjects();

    /**
	 * 
	 * @return
	 */
    Integer registeredObjectsCount();

    /**
	 * 
	 * @param element
	 * @return
	 */
    Boolean isObjectRegistered(E element);
}
