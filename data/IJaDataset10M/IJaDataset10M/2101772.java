package com.hack23.cia.service.api;

import java.io.Serializable;
import java.util.List;

/**
 * The Interface DataContainer.
 * 
 * @param <T>
 *            the generic type
 * @param <ID>
 *            the generic type
 */
public interface DataContainer<T extends Serializable, ID extends Serializable> {

    /**
	 * Gets the all.
	 * 
	 * @return the all
	 */
    List<T> getAll();

    /**
	 * Load.
	 * 
	 * @param id
	 *            the id
	 * @return the t
	 */
    T load(ID id);
}
