package com.mu.jacob.core.generator;

import java.util.List;
import com.google.inject.ImplementedBy;

/**
 * Jacob configuration interface
 * @author Adam Smyczek
 */
@ImplementedBy(DefaultConfig.class)
public interface Config {

    /**
	 * Delegate for Guice injector getInstance method
	 * @param <T> to return class type
	 * @param clazz the class
	 * @return instance of the class
	 */
    public <T> T getInstance(Class<T> clazz);

    /**
	 * @return model sets list containing all model classes
	 */
    public abstract List<ModelSet> getModelSets();

    /**
	 * @return class path required be the model classes
	 */
    public abstract String getClassPath();
}
