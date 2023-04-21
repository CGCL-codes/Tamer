package org.jpublish;

import com.anthonyeden.lib.config.Configuration;
import com.anthonyeden.lib.config.ConfigurationException;
import org.jpublish.util.vfs.VFSFile;
import org.jpublish.util.JPublishCache;
import java.util.Iterator;

/** The TemplateManager is a central access point for locating templates.  
    Implementations of the TemplateManager interface will provide the template
    loading behavior.

    @author Anthony Eden
    @since 1.1
*/
public interface TemplateManager {

    /** Set the SiteContext.
    
        @param siteContext The site context
    */
    public void setSiteContext(SiteContext siteContext);

    /** Get a Template instance from the given path.  If no template can be 
        found then this method must throw an Exception.
    
        @param path The template path
        @return The Template
        @throws Exception Any Exception
    */
    public Template getTemplate(String path) throws Exception;

    /** Put the template in the location specified by the given path.
    
        @param path The template path
        @param template The Template
        @throws Exception
    */
    public void putTemplate(String path, Template template) throws Exception;

    /** Remove the template at the specified path.
    
        @param path The template path
        @throws Exception
    */
    public void removeTemplate(String path) throws Exception;

    /** Make the directory for the specified path.  Parent directories
        will also be created if they do not exist.
        
        @param path The directory path
    */
    public void makeDirectory(String path);

    /** Remove the directory for the specified path.  The directory
        must be empty.
    
        @param path The path
        @throws Exception
    */
    public void removeDirectory(String path) throws Exception;

    /** Get an Iterator for the Templates known to the template manager.
        
        @return An Iterator of Templates
        @throws Exception
    */
    public Iterator getTemplates() throws Exception;

    /** Get an Iterator for the Templates known to the template manager
        starting at the specified base path.
        
        @param path The base path
        @return An Iterator of Templates
        @throws Exception
    */
    public Iterator getTemplates(String path) throws Exception;

    /** Get the Virtual File System root file.  The Virtual File System
        provides a datasource-independent way of navigating through all
        items known to the TemplateManager.
        
        @return The root VFSFile
        @throws Exception
    */
    public VFSFile getVFSRoot() throws Exception;

    /** Load the PageManager's configuration.
    
        @param configuration The Configuration object
        @throws ConfigurationException
    */
    public void loadConfiguration(Configuration configuration) throws ConfigurationException;

    public abstract void setCache(JPublishCache cache);
}
