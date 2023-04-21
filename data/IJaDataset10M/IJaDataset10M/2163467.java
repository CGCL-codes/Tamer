package com.monad.homerun.model;

import com.monad.homerun.base.Value;

/**
 * ModelStatus is the very thin interface that model sub-classes' status
 * objects must implement
 */
public interface ModelStatus {

    /**
	 * Returns the name of the model whose status it is
	 * 
	 * @return the model name
	 */
    public String getModelName();

    /**
	 * Returns the type of the model.
	 * 
	 * @return the model type
	 */
    public String getModelType();

    /**
	 * Returns the current value of the model.
	 * 
	 * @return the current model value
	 */
    public Value getModelValue();
}
