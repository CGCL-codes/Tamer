package org.arastreju.core.ontology.impl;

import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;

/**
 * 
 * <p>
 *  Interface for action operating on a repository connection.
 * </p>
 * 
 * <p>
 * 	Created 11.02.2010
 * </p>
 *
 * @author Oliver Tigges
 */
public interface RepositoryAction<T> {

    T perform(RepositoryConnection con) throws RepositoryException;
}
