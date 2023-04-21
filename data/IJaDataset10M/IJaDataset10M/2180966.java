package net.sf.jabref.plugin.util;

import java.net.URL;
import java.util.*;
import org.java.plugin.Plugin;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension.Parameter;

/**
 * Abstract base class that provides methods for access parameters of different
 * types.
 * 
 * This base class is needed since both extensions and parameters offer access
 * to parameters (sub-parameters in the case of a parameter).
 * 
 */
public abstract class ParameterAccessor {

    /**
	 * If subclasses extend ParameterAccessor they should provide the plugin
	 * instance here from which to query the parameter.
	 * 
	 * @return The plugin that this parameter belongs to.
	 */
    public abstract Plugin getDeclaringPlugin();

    /**
	 * Should return the parameter for this accessor with the given id.
	 * 
	 * @param id
	 * @return
	 * 
	 * Will throw an exception if more than parameter is found with the given
	 * id. Use getParameters then.
	 * 
	 */
    public abstract Parameter getParameter(String id);

    /**
	 * Should get all parameters for this accessor.
	 * 
	 * @return
	 */
    public abstract Collection<Parameter> getParameters();

    /**
	 * Should return all parameters for this accessor with the given id.
	 * 
	 * @param id
	 * @return
	 */
    public abstract Collection<Parameter> getParameters(String id);

    public abstract String getId();

    /**
	 * Return the Number stored in the given parameter.
	 * 
	 * Will return null on error.
	 */
    public Number getNumberParameter(String id) {
        return Util.parameter2Number(id, getParameter(id));
    }

    /**
	 * Return all numbers stored for the given id.
	 * 
	 * Will always return a collection, which might be empty.
	 */
    public Collection<Number> getNumberParameters(String id) {
        Collection<Parameter> parameters = getParameters(id);
        Collection<Number> result = new ArrayList<Number>(parameters.size());
        for (Parameter para : parameters) {
            result.add(Util.parameter2Number(id, para));
        }
        return result;
    }

    /**
	 * Return the given parameter as a boolean.
	 * 
	 * @param id
	 *            Id for which to return a boolean parameter.
	 * @return Will return the value stored under the given string or false on
	 *         error.
	 */
    public boolean getBooleanParameter(String id) {
        return Util.parameter2Boolean(id, getParameter(id));
    }

    /**
	 * Return all booleans stored for the given id in no particular order.
	 * 
	 * Will always return a collection, which might be empty.
	 */
    public Collection<Boolean> getBooleanParameters(String id) {
        Collection<Parameter> parameters = getParameters(id);
        Collection<Boolean> result = new ArrayList<Boolean>(parameters.size());
        for (Parameter para : parameters) {
            result.add(Util.parameter2Boolean(id, para));
        }
        return result;
    }

    /**
	 * Return the string parameter for the given id using the rawValue()
	 * function of the parameter with the given id.
	 * 
	 * @param id
	 *            The id for which to retrieve the string value.
	 * @return might return null on error.
	 */
    public String getStringParameter(String id) {
        Parameter para = getParameter(id);
        if (para != null) {
            return para.rawValue();
        }
        return null;
    }

    /**
	 * Return all string values for the given id.
	 * 
	 * @param id
	 *            The id for which to retrieve the string values.
	 * 
	 * @return Will always return a collection, which might be empty.
	 */
    public Collection<String> getStringParameters(String id) {
        Collection<Parameter> parameters = getParameters(id);
        Collection<String> result = new ArrayList<String>(parameters.size());
        for (Parameter para : parameters) {
            result.add(para.rawValue());
        }
        return result;
    }

    /**
	 * Will return the resource URL stored in the parameter with the given id.
	 * 
	 * If this URL is relative it will be resolved relative to the plug-in root.
	 * 
	 * @param id
	 *            The id of the parameter from which to retrieve a resource URL.
	 * @return A resource URL for the given id or null on error.
	 */
    public URL getResourceParameter(String id) {
        return getResourceParameter(id, null);
    }

    /**
	 * Will return all resource URLs stored in parameters with the given id.
	 * 
	 * If any or these URLs are relative they will be resolved relative to the
	 * plug-in root.
	 * 
	 * @param id
	 *            The id of the parameters from which to retrieve resource URLs.
	 * @return Will always a collection of URLs which might be empty.
	 */
    public Collection<URL> getResourceParameters(String id) {
        return getResourceParameters(id, null);
    }

    /**
	 * Will return the resource URL stored in the parameter with the given id.
	 * In contrast to {@link #getResourceParameter(String)} this method will
	 * append the given relative path component to the end of the URL.
	 * 
	 * This is useful if the resource URL points to a directory from which you
	 * want to load a specific file.
	 * 
	 * If the stored URL is relative it will be resolved relative to the plug-in
	 * root.
	 * 
	 * @param id
	 *            The id of the parameter from which to retrieve a resource URL.
	 * @param relative
	 *            A relative path component to append to the end of the stored
	 *            URL.
	 * 
	 * The method will take care to put a path separator between stored URL and
	 * given relative path.
	 * 
	 * @return A resource URL for the given id or null on error.
	 */
    public URL getResourceParameter(String id, String relative) {
        return Util.parameter2URL(getParameter(id), relative, getDeclaringPlugin());
    }

    /**
	 * Will return the resource URLs stored in the parameter with the given id.
	 * In contrast to {@link #getResourceParameters(String)} this method will
	 * append the given relative path component to the end of each URL.
	 * 
	 * This is useful if the resource URLs points to directories from which you
	 * want to load specific (albeit identically named) files .
	 * 
	 * If the stored URLs are relative they will be resolved relative to the
	 * plug-in root.
	 * 
	 * @param id
	 *            The id of the parameters from which to retrieve resource URLs.
	 * @param relative
	 *            A relative path component to append to the end of the stored
	 *            URLs.
	 * 
	 * The method will take care to put a path separator between each stored URL
	 * and the given relative path.
	 * 
	 * @return Will always return a collection which might be empty.
	 */
    public Collection<URL> getResourceParameters(String id, String relative) {
        Collection<Parameter> parameters = getParameters(id);
        Collection<URL> result = new ArrayList<URL>(parameters.size());
        Plugin plugin = getDeclaringPlugin();
        for (Parameter para : parameters) {
            result.add(Util.parameter2URL(para, relative, plugin));
        }
        return result;
    }

    /**
	 * Return the Date stored in the given parameter.
	 * 
	 * Will return null on error.
	 */
    public Date getDateParameter(String id) {
        return Util.parameter2Date(id, getParameter(id));
    }

    /**
	 * Return all dates stored for the given id.
	 * 
	 * Will always return a collection, which might be empty.
	 */
    public Collection<Date> getDateParameters(String id) {
        Collection<Parameter> parameters = getParameters(id);
        Collection<Date> result = new ArrayList<Date>(parameters.size());
        for (Parameter para : parameters) {
            result.add(Util.parameter2Date(id, para));
        }
        return result;
    }

    HashMap<String, Object> singletonMap = new HashMap<String, Object>();

    /**
	 * Will return an singleton instance for the given class parameter.
	 * 
	 * A class parameter is essentially a string parameter that has been marked
	 * using a custom-data attribute in the extension point. The string value
	 * provided by the extension of the plugin will be used as a class name to
	 * create the instance from.
	 * 
	 * The instance will be created on the first call to this method using the
	 * ClassLoader of the declaring plugin.
	 * 
	 * A call to this method will activate the plug-in if not already activated.
	 * 
	 * @param id
	 * @return
	 */
    public Object getClassParameter(String id) {
        if (!singletonMap.containsKey(id)) {
            PluginManager manager = getDeclaringPlugin().getManager();
            try {
                manager.activatePlugin(getDeclaringPlugin().getDescriptor().getId());
                ClassLoader classLoader = manager.getPluginClassLoader(getDeclaringPlugin().getDescriptor());
                Class<?> classToInstantiate = classLoader.loadClass(getParameter(id).valueAsString());
                singletonMap.put(id, classToInstantiate.newInstance());
            } catch (Exception e) {
                return null;
            }
        }
        return singletonMap.get(id);
    }

    HashMap<String, Collection<Object>> singletonCollectionMap = new HashMap<String, Collection<Object>>();

    /**
	 * See {@link #getClassParameter(String)} for details how singleton
	 * instances are created.
	 * 
	 * This method will create a singleton instance for each parameter with the
	 * given id.
	 * 
	 * @param id
	 *            The id for which to retrieve parameters.
	 * @return A unmodifyable collection containing all singleton instances
	 *         created.
	 */
    public Collection<Object> getClassParameters(String id) {
        if (!singletonCollectionMap.containsKey(id)) {
            PluginManager manager = getDeclaringPlugin().getManager();
            try {
                manager.activatePlugin(getDeclaringPlugin().getDescriptor().getId());
                ClassLoader classLoader = manager.getPluginClassLoader(getDeclaringPlugin().getDescriptor());
                Collection<Object> instances = new LinkedList<Object>();
                for (String classNamesToLoad : getStringParameters(id)) {
                    Class<?> classToInstantiate = classLoader.loadClass(classNamesToLoad);
                    instances.add(classToInstantiate.newInstance());
                }
                singletonCollectionMap.put(id, Collections.unmodifiableCollection(instances));
            } catch (Exception e) {
                return null;
            }
        }
        return singletonCollectionMap.get(id);
    }
}
