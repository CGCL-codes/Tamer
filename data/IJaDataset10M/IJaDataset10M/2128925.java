package com.hack23.cia.service.api.common;

import java.util.List;
import com.hack23.cia.model.api.common.PersistedModelObject;

/**
 * The Interface GenericUserInterfaceLoaderService.
 */
public interface GenericUserInterfaceLoaderService<INTERFACE extends PersistedModelObject, OBJECT> {

    /**
     * Gets the all.
     * 
     * @return the all
     */
    List<INTERFACE> getAll();

    /**
	 * Gets the all implementations.
	 * 
	 * @return the all implementations
	 */
    List<OBJECT> getAllImplementations();

    /**
     * Load.
     * 
     * @param id the id
     * 
     * @return the oBJECT
     */
    OBJECT load(Long id);

    /**
	 * Load by id.
	 * 
	 * @param id the id
	 * 
	 * @return the iNTERFACE
	 */
    INTERFACE loadById(Long id);
}
