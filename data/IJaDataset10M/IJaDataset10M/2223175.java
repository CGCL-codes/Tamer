package org.tuotoo.util;

import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This interface is only needed in the context of the ResourceLoader. It loads
 * resources and transforms them into concrete objects.
 * 
 * @author Rolf Wendolsky
 * @see anon.util.ResourceLoader
 */
public interface IResourceInstantiator {

    /**
     * Loads a file and transforms it into a concrete object.
     * 
     * @param a_file
     *            a File
     * @param a_topDirectory
     *            the top directory of this file; this is generally not needed
     *            to load the file
     * @throws Exception
     *             if an error occurs
     * @return an Object
     */
    public Object getInstance(File a_file, File a_topDirectory) throws Exception;

    /**
     * Loads a file and transforms it into a concrete object. Please not that
     * JDK 1.1.8 does not correctly load zip entries! Please use the method
     * ResourceLoader.loadResource(String) instead. By this way, only zip/jar
     * files in the classpath may be loaded.
     * 
     * @param a_entry
     *            a ZipEntry
     * @param a_file
     *            the ZipFile of this zip entry
     * @throws Exception
     *             if an error occurs
     * @return an Object
     */
    public Object getInstance(ZipEntry a_entry, ZipFile a_file) throws Exception;

    /**
     * An Exception that is thrown by a ResourceInstantiator if too much
     * instantiations have failed. The instantiator object will be invalid after
     * throwing this exception.
     * 
     * @author Rolf Wendolsky
     */
    public class ResourceInstantiationException extends Exception {
    }
}
