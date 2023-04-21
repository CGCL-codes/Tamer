package de.etqw.openranked.service;

public interface SchemeService {

    /**
	 * Checks and initalizes database structure
	 * 
	 * @return true if database was already existent
	 */
    public abstract boolean createSchemeIfNecessary();
}
