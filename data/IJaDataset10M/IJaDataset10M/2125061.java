package org.npsnet.v.kernel;

import java.net.*;
import java.util.*;

import org.npsnet.v.services.resource.ArchiveDescriptor;
import org.npsnet.v.services.resource.ExtensionDescriptor;
import org.npsnet.v.services.resource.InterfaceArchiveDescriptor;

/**
 * A classloader for archives.
 *
 * @author Andrzej Kapolka
 */

public class ArchiveClassLoader extends URLClassLoader
{
    /**
     * The archive descriptors of all loaded archives.
     */
    private Vector archiveDescriptors;
    
    /**
     * The children of this classloader.
     */
    private Vector childClassLoaders;
    
    
    /**
     * Constructor.
     */
    public ArchiveClassLoader()
    {
        super(new URL[0]);
        
        archiveDescriptors = new Vector();
        
        childClassLoaders = new Vector();
    }
    
    /**
     * Constructor.
     *
     * @param parent the parent <code>ClassLoader</code>
     */
    public ArchiveClassLoader(ClassLoader parent)
    {
        super(new URL[0],parent);
        
        archiveDescriptors = new Vector();
        
        childClassLoaders = new Vector();
    }
    
    /**
     * Adds an element to the list of child classloaders.
     *
     * @param cl the new child classloader
     */
    public void addChildClassLoader(ClassLoader cl)
    {
        childClassLoaders.add(cl);
    }
    
    /**
     * Returns an <code>Enumeration</code> over all children
     * of this classloader.
     *
     * @return an <code>Enumeration</code> over all children
     * of this classloader
     */
    public Enumeration getChildClassLoaders()
    {
        return childClassLoaders.elements();
    }
    
    /**
     * Loads the archive described by the given archive descriptor
     * into this classloader.
     *
     * @param ad the <code>ArchiveDescriptor</code> describing the
     * archive to load
     */
    public void addArchive(ArchiveDescriptor ad)
    {
        // Add the codebase URL to the search path
        
        URL codebase = ad.getURL();
        
        addURL(codebase);
        
        
        // Add the URLs specified in the archive's classpath
        
        String[] classpath = ad.getClassPath();
        
        for(int i=0;i<classpath.length;i++)
        {
            try
            {
                addURL(
                    new URL(codebase,classpath[i])
                );
            }
            catch(MalformedURLException mue)
            {
                System.err.println("Bad URL in archive classpath: "+classpath[i]);
            }
        }
        
        
        archiveDescriptors.add(ad);
    }
    
    /**
     * Returns an <code>Enumeration</code> over the
     * <code>ArchiveDescriptor</code>s of all loaded
     * archives.
     *
     * @return an <code>Enumeration</code> over the
     * <code>ArchiveDescriptor</code>s of all loaded
     * archives
     */
    public Enumeration getArchiveDescriptors()
    {
        return archiveDescriptors.elements();
    }
    
    /**
     * Checks whether this classloader has loaded an archive that is
     * compatible with the one described.
     *
     * @param ad the descriptor of the archive of interest
     * @return <code>true</code> if this classloader has loaded an archive
     * that is compatible with the one specified, <code>false</code>
     * otherwise
     */
    public boolean hasCompatibleArchiveDescriptor(ArchiveDescriptor ad)
    {
        Enumeration enum = getArchiveDescriptors();
        
        while(enum.hasMoreElements())
        {
            if( ((ArchiveDescriptor)enum.nextElement()).isCompatibleWith(ad) )
            {
                return true;
            }
        }
        
        return false;
    }
}