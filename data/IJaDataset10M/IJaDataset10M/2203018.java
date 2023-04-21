package com.acciente.commons.loader;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * This is an interface that abstracts the loading of class definitions.
 *
 * @created Feb 27, 2008
 *
 * @author Adinath Raveendra Raj
 */
public interface ClassDefLoader {

    /**
    * Load the byte code and other information about the specified class
    *
    * @param sClassName the name of the class for which a definition is requested
    * @return a class definition if this class definition loader is able to locate and successfully
    * load the byte code for the class. If the class could not be located a null is returned, if the
    * class was located but the load failed an exception is thrown.
    * @throws ClassNotFoundException if this loader was able to locate the class definition was
    * unable to sucessfully load it
    */
    ClassDef getClassDef(String sClassName) throws ClassNotFoundException;

    /**
    * Load the data and other information about the specified resource
    *
    * @param sResourceName the name of the class for which a definition is requested
    * @return a resource definition if this class definition loader is able to locate and successfully
    * load the data for the resource. If the resource could not be located a null is returned, if the
    * resource was located but the load failed again a null is returned (since the getResource() and
    * getResourceAsStreamMethods() do not have checked exceptions in their signature).
    */
    ResourceDef getResourceDef(String sResourceName);

    /**
    * Searches for class names that match the specified package root and regex. This method is expected to search and
    * return a list of fully qualified classnames.
    *
    * @param asPackageNames an array of starting package names to start the search, if the package names arg
    * is null all package names are searched
    * @param oClassNamePattern a java regex that defines the classnames that match the search
    * @return a set  of string classnames that match the specified regex
    */
    Set findClassNames(String[] asPackageNames, Pattern oClassNamePattern);
}
