package com.hack23.cia.service.api;

import com.hack23.cia.model.internal.application.system.impl.Agency;
import com.hack23.cia.model.internal.application.system.impl.Portal;

/**
 * The Interface UserConfiguration.
 */
public interface UserConfiguration {

    /**
	 * Gets the agency.
	 *
	 * @return the agency
	 */
    Agency getAgency();

    /**
	 * Gets the portal.
	 *
	 * @return the portal
	 */
    Portal getPortal();
}
