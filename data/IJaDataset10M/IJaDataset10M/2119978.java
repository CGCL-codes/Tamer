package com.hack23.cia.model.impl.application.factory;

import com.hack23.cia.model.api.application.configuration.AgencyData;
import com.hack23.cia.model.api.application.configuration.PortalData;
import com.hack23.cia.model.api.application.factory.ApplicationConfigurationModelFactory;
import com.hack23.cia.model.impl.application.configuration.Agency;
import com.hack23.cia.model.impl.application.configuration.Portal;
import com.hack23.cia.model.impl.common.AbstractModelFactoryImpl;

/**
 * The Class ApplicationModelFactoryFactoryImpl.
 */
class ApplicationConfigurationModelFactoryImpl extends AbstractModelFactoryImpl implements ApplicationConfigurationModelFactory {

    /**
	 * Creates the agency data.
	 * 
	 * @return the agency data
	 */
    @Override
    public final AgencyData createAgencyData() {
        return new Agency();
    }

    /**
	 * Creates the portal data.
	 * 
	 * @param loadAgency the load agency
	 * 
	 * @return the portal data
	 */
    @Override
    public final PortalData createPortalData(final AgencyData loadAgency) {
        final Portal portal = new Portal();
        portal.setAgency((Agency) loadAgency);
        return portal;
    }

    /**
	 * Gets the agency data spec.
	 * 
	 * @return the agency data spec
	 */
    @Override
    public final Class<? extends AgencyData> getAgencyDataSpec() {
        return Agency.class;
    }

    @Override
    public final Class<? extends PortalData> getPortalDataSpec() {
        return Portal.class;
    }
}
