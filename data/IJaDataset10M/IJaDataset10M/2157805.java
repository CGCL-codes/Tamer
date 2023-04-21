package org.jsresources.apps.jmvp.model;

/**	Named objects.
	This is the interface for classes that have a name that can be queried.

	@author Matthias Pfisterer
 */
public interface Named {

    /**	Get the name of the object.
		@return The name of the object.
	*/
    public String getName();
}
