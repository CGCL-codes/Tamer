package com.hack23.cia.web.impl.ui.viewfactory.impl.configuration;

import com.hack23.cia.web.impl.ui.viewfactory.api.configuration.AbstractConfigurationModelAndView;
import com.hack23.cia.web.impl.ui.viewfactory.impl.common.AbstractViewFactoryImpl;

/**
 * The Class AbstractConfigurationViewFactoryImpl.
 */
abstract class AbstractConfigurationViewFactoryImpl<MODELANDVIEW extends AbstractConfigurationModelAndView> extends AbstractViewFactoryImpl<MODELANDVIEW> {

    /**
	 * Instantiates a new abstract configuration view factory impl.
	 */
    protected AbstractConfigurationViewFactoryImpl() {
        super();
    }
}
