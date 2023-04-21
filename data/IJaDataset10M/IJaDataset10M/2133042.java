package org.mb.services;

import org.mb.micromvc.model.AbstractModel;

/**
 * this interface orders to services to have buildModel method
 *
 * @author gubriansky
 *
 */
public interface BaseService {

    /**
	 * builds model for controller
	 *
	 * @return AbstractModel
	 */
    public AbstractModel buildModel();
}
