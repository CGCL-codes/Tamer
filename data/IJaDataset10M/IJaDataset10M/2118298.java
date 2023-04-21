package com.acciente.commons.loader;

/**
 * This interface abstracts access to resource to be accessed via the classloader
 *
 * @created Sep 30, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface ResourceDef {

    /**
    * Returns a fully qualified class name of the that this object manages
    *
    * @return a string class name
    */
    String getResourceName();

    /**
    * Returns true if the underlying source of the class data has changed since the
    * last call to getData()
    *
    * @return true if changed, false otherwise
    */
    boolean isModified();

    /**
    * Reloads the class byte code from the underlying source
    */
    void reload();

    /**
    * This method loads and returns the contents of this resource
    *
    * @return the contents of the resource as a byte array
    */
    byte[] getContent();
}
