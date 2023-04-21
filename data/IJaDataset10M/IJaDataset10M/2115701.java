package com.addictedtor.orchestra.rengine;

import org.rosuda.REngine.REngine;

/**
 * service that provides R engines. At the moment the only implementation
 * available relies on the JRIEngine, but it is anticipated that other 
 * ways of communicating with R might be implemented through this service 
 * 
 * @author Romain Francois <francoisromain@free.fr>
 *
 */
public abstract class REngineService {

    /**
	 * @return the REngine supplied by the provider
	 */
    public abstract REngine getEngine();
}
