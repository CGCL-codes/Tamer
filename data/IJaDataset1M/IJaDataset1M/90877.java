package org.dwgsoftware.raistlin.composition.model;

/**
 * The Resolver interface defines the contract for instance access and 
 * release.
 *
 * @author <a href="mailto:dev@avalon.apache.org">Avalon Development Team</a>
 * @version $Revision: 1.1 $ $Date: 2005/09/06 00:58:17 $
 */
public interface Resolver {

    /**
     * Resolve a object to a value.
     *
     * @return the resolved object
     * @throws Exception if an error occurs
     */
    Object resolve() throws Exception;

    /**
     * Resolve a object to a value.
     *
     * @param proxy if TRUE ruturn a proxied reference if the underlying component
     *   suppports proxied representation otherwise return the raw component instance
     * @return the resolved object
     * @throws Exception if an error occurs
     */
    Object resolve(boolean proxy) throws Exception;

    /**
    * Release an object.
    * 
    * @param instance the object to release
    */
    void release(Object instance);
}
