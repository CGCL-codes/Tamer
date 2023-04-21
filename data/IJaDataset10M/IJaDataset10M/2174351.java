package com.bm.inject;

/**
 * Every Injector intance use factories to create the objects to inject.
 * @author Daniel Wiese
 *
 * @param <H> - the type of the handle
 */
public interface DependencyInjectionFactory<H> {

    /**
	 * Gets called if the injector has identified 
	 * a field or method for dependency injection.
	 * @param type - which type should be created
	 * @return - the created injection instance, contains the crated object
	 * and a handle
	 */
    Injection<H> create(Class type);
}
