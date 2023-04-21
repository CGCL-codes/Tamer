package org.coinjema.context.source;

import java.util.Collection;

/**
 * A context source has to supply a means of retrieving context scripts for creating 
 * dependency objects.  It has a name which is strictly relative to its parent
 * context, and it knows how to find all its child contexts.
 * 
 * For instance, a FileContextSource implementation returns the name of the last directory
 * in the path as its <i>name</i>, returns its subdirectories as its <i>children</i>, and
 * can provide a FileInputStream given a filename.
 * @author mstover
 *
 */
public interface ContextSource {

    /**
	 * Retrieve a collection of direct subcontexts of this context.
	 * @return
	 */
    Collection<ContextSource> getSubContexts();

    /**
	 * The relative name of this context.  The name of this context without the parent context names attached.  If
	 * the context is file based, this would be the name of the last directory in the path.
	 * @return
	 */
    String getName();

    Resource getResource(String resourceName);

    /**
	 * A more optimized way to find a specific resource if the caller knows the extension (resource type).
	 * @param resourceName
	 * @param extension
	 * @return
	 */
    Resource getResource(String resourceName, String extension);
}
