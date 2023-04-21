package com.sun.j2me.global;

import javax.microedition.global.ResourceException;

/**
 *  This class represents a factory for creating resource managers.
 *
 */
public abstract class ResourceManagerFactory {

    /**
     *  Every application resource file is under global directory.
     */
    protected static final String GLOBAL_PREFIX = "/global/";

    /**
     * Resource file extension constant.
     */
    protected static final String RESOURCE_FILE_EXTENSION = ".res";

    /**
     *  Creates a new instance of ResourceManagerFactory.
     */
    public ResourceManagerFactory() {
    }

    /**
     *  Returns an instance of <code>ResourceManager</code> class for the given
     *  base name and system's default locale.
     *
     * @param  baseName                     the base name
     * @return                              the resource manager for the base
     *      name
     * @throws  ResourceException           if the resources for the base name
     *      doesn't exist
     * @throws  UnsupportedLocaleException  if the resources of the specified
     *      base name aren't available for the system's default locale
     * @see
     *      javax.microedition.global.ResourceManager#getManager(String)
     */
    public abstract ResourceManager getManager(String baseName) throws ResourceException;

    /**
     *  Creates an instance of <code>ResourceManager</code> class for the given
     *  base name and locale.
     *
     * @param  baseName                     the base name
     * @param  locale                       the locale
     * @return                              the resource manager for the base
     *      name and locale
     * @throws  ResourceException           if the resources for the base name
     *      and locale doesn't exist
     * @throws  UnsupportedLocaleException  if the resources of the specified
     *      base name aren't available for the locale
     * @see
     *      javax.microedition.global.ResourceManager#getManager(String, String)
     */
    public abstract ResourceManager getManager(String baseName, String locale) throws ResourceException;

    /**
     *  Creates an instance of <code>ResourceManager</code> class for the given
     *  base name and the first matching locale in the supplied array.
     *
     * @param  baseName                     the base name
     * @param  locales                      the array of locale
     * @return                              the resource manager for the base
     *      name and the matched locale
     * @throws  ResourceException           if no resources for the base name
     *      and any of the locales in the array are found
     * @throws  UnsupportedLocaleException  if the resources of the specified
     *      base name are available for no locale from the array
     * @see
     *      javax.microedition.global.ResourceManager#getManager(String,
     *      String[])
     */
    public abstract ResourceManager getManager(String baseName, String[] locales) throws ResourceException;

    /**
     *  Returns a list of locales supported by the given baseName. A resource
     *  manager can be constructed for each locale from the list and the base
     *  name.
     *
     * @param  baseName  the base name
     * @return           the list of the supported locales
     */
    public abstract String[] getSupportedLocales(String baseName);
}
