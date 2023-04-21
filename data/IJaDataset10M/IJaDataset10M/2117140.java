package com.mindquarry.persistence.api;

import java.util.Collection;
import java.util.Map;

/**
 * This interface should be implemented by a configuration provider.
 * A configuration instance is required by the SessionFactory implementation
 * during the initialization. A configuration instance must provide 
 * all entity classes and queries that should be managed by the persistence
 * layer.   
 *
 * @author
 * <a href="mailto:bastian.steinert(at)mindquarry.com">Bastian Steinert</a>
 */
public interface Configuration {

    public Collection<Class<?>> getClasses();

    public Map<String, String> getNamedQueries();
}
