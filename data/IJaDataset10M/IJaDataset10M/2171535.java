package org.force4spring.query;

/**
 * Interface for a factory object capable of producing query instances.
 * 
 * @author Max Rudman
 */
public interface QueryFactory {

    public Query createQuery();

    public Query createSubQuery();
}
