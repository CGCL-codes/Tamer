package org.nakedobjects.nof.core.resource;

import org.nakedobjects.nof.core.conf.Configuration;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.system.InitialisationException;
import org.nakedobjects.nof.core.system.InstallResources;
import org.nakedobjects.nof.core.util.NakedObjectConfiguration;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

public class ResourcesFromProperties implements InstallResources {

    private static final char DELIMITER = '#';

    private static final Logger LOG = Logger.getLogger(ResourcesFromProperties.class);

    private static final String RESOURCES = Configuration.ROOT + "resources";

    private static final String RESOURCES_PREFIX = Configuration.ROOT + "resources.prefix";

    public Object[] getResources() {
        return installResources(NakedObjectsContext.getConfiguration());
    }

    public String getName() {
        return "resources-properties";
    }

    private Object installResource(String className) {
        Class cls;
        try {
            cls = loadClass(className);
            Object resource = (Object) cls.newInstance();
            return resource;
        } catch (InstantiationException e) {
            throw new InitialisationException("Cannot instantiate resource class " + className);
        } catch (IllegalAccessException e) {
            throw new InitialisationException("Cannot access constructor in resource class " + className);
        }
    }

    private Object[] installResources(final NakedObjectConfiguration configuration) {
        LOG.info("installing " + this.getClass().getName());
        String resourcePrefix = resourcePrefix(configuration);
        String resourceList = configuration.getString(RESOURCES);
        if (resourceList != null) {
            return installResources(resourcePrefix, resourceList);
        } else {
            throw new InitialisationException("No resources specified");
        }
    }

    private Object[] installResources(String resourcePrefix, String resourceList) {
        StringTokenizer resources = new StringTokenizer(resourceList, Configuration.LIST_SEPARATOR);
        if (!resources.hasMoreTokens()) {
            throw new InitialisationException("Resources specified, but none loaded");
        }
        Object[] array = new Object[resources.countTokens()];
        int i = 0;
        while (resources.hasMoreTokens()) {
            String resourceName = resources.nextToken().trim();
            LOG.info("  creating resource " + resourceName);
            Object resource;
            if (resourceName.indexOf(DELIMITER) == -1) {
                resource = installResource(resourcePrefix + resourceName);
            } else {
                resource = installSimpleResource(resourcePrefix, resourceName);
            }
            array[i++] = resource;
        }
        return array;
    }

    private Object installSimpleResource(String prefix, String resourceName) {
        int pos = resourceName.indexOf(DELIMITER);
        String className = prefix + resourceName.substring(pos + 1);
        Class cls = loadClass(className);
        String type = resourceName.substring(0, pos);
        if ("factory".equals(type)) {
            return new SimpleFactory(cls);
        } else if ("repository".equals(type)) {
            return new SimpleRepository(cls);
        } else {
            throw new InitialisationException("Unknown resource type " + type);
        }
    }

    private Class loadClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new InitialisationException("Cannot find resource class " + className);
        }
    }

    private String resourcePrefix(final NakedObjectConfiguration configuration) {
        String resourcePrefix = configuration.getString(RESOURCES_PREFIX);
        resourcePrefix = resourcePrefix == null ? "" : resourcePrefix.trim();
        if (resourcePrefix.length() > 0 && !resourcePrefix.endsWith(Configuration.DELIMITER)) {
            resourcePrefix = resourcePrefix + Configuration.DELIMITER;
        }
        return resourcePrefix;
    }
}
