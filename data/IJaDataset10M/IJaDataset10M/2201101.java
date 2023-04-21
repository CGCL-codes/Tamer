package org.impalaframework.bootstrap;

import org.impalaframework.config.BooleanPropertyValue;
import org.impalaframework.config.IntPropertyValue;
import org.impalaframework.config.PropertySource;
import org.impalaframework.config.StringPropertyValue;
import org.impalaframework.constants.LocationConstants;
import org.impalaframework.exception.ConfigurationException;
import org.impalaframework.util.InstantiationUtils;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link ContextLocationResolver} with responsiblility for
 * determining which of the context locations to add from those available in the
 * impala-core module. Uses properties from the supplied {@link PropertySource}
 * instance to make these determiniations.
 * 
 * Also responsible for delegating to the JMX
 * {@link org.impalaframework.jmx.bootstrap.JMXContextLocationResolver} if the
 * relevant classes are present on the class path.
 * 
 * @author Phil Zoio
 */
public class SimpleContextLocationResolver implements ContextLocationResolver {

    public final boolean addContextLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
        logStandaloneProperties(configSettings, propertySource);
        if (!explicitlySetLocations(configSettings, propertySource)) {
            addProxyProperties(configSettings, propertySource);
            addCustomLocations(configSettings, propertySource);
            explicitlyAddLocations(configSettings, propertySource);
            return false;
        } else {
            return true;
        }
    }

    protected void addProxyProperties(ConfigurationSettings configSettings, PropertySource propertySource) {
        BooleanPropertyValue proxyAllowNoService = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.PROXY_ALLOW_NO_SERVICE, CoreBootstrapProperties.PROXY_ALLOW_NO_SERVICE_DEFAULT);
        BooleanPropertyValue proxySetContextClassLoader = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.PROXY_SET_CONTEXT_CLASSLOADER, CoreBootstrapProperties.PROXY_SET_CONTEXT_CLASSLOADER_DEFAULT);
        IntPropertyValue proxyRetryCount = new IntPropertyValue(propertySource, CoreBootstrapProperties.PROXY_MISSING_SERVICE_RETRY_COUNT, CoreBootstrapProperties.PROXY_MISSING_SERVICE_RETRY_COUNT_DEFAULT);
        IntPropertyValue proxyRetryInterval = new IntPropertyValue(propertySource, CoreBootstrapProperties.PROXY_MISSING_SERVICE_RETRY_INTERVAL, CoreBootstrapProperties.PROXY_MISSING_SERVICE_RETRY_INTERVAL_DEFAULT);
        configSettings.addProperty(CoreBootstrapProperties.PROXY_ALLOW_NO_SERVICE, proxyAllowNoService);
        configSettings.addProperty(CoreBootstrapProperties.PROXY_SET_CONTEXT_CLASSLOADER, proxySetContextClassLoader);
        configSettings.addProperty(CoreBootstrapProperties.PROXY_MISSING_SERVICE_RETRY_COUNT, proxyRetryCount);
        configSettings.addProperty(CoreBootstrapProperties.PROXY_MISSING_SERVICE_RETRY_INTERVAL, proxyRetryInterval);
    }

    protected void addCustomLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
        addDefaultLocations(configSettings);
        addModuleType(configSettings, propertySource);
        maybeAddJmxLocations(configSettings, propertySource);
    }

    private void logStandaloneProperties(ConfigurationSettings configSettings, PropertySource propertySource) {
        BooleanPropertyValue embeddedMode = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.EMBEDDED_MODE, CoreBootstrapProperties.EMBEDDED_MODE_DEFAULT);
        BooleanPropertyValue parentClassloaderFirst = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.PARENT_CLASS_LOADER_FIRST, !embeddedMode.getValue());
        BooleanPropertyValue loadTimeWeavingEnabled = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.LOAD_TIME_WEAVING_ENABLED, CoreBootstrapProperties.LOAD_TIME_WEAVING_ENABLED_DEFAULT);
        BooleanPropertyValue supportsModuleLibraries = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.SUPPORTS_MODULE_LIBRARIES, CoreBootstrapProperties.SUPPORTS_MODULE_LIBRARIES_DEFAULT);
        if (supportsModuleLibraries.getValue()) {
            BooleanPropertyValue exportsModuleLibraries = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.EXPORTS_MODULE_LIBRARIES, CoreBootstrapProperties.EXPORTS_MODULE_LIBRARIES_DEFAULT);
            configSettings.addProperty(CoreBootstrapProperties.EXPORTS_MODULE_LIBRARIES, exportsModuleLibraries);
            BooleanPropertyValue loadsModuleLibraryResources = new BooleanPropertyValue(propertySource, CoreBootstrapProperties.LOADS_MODULE_LIBRARY_RESOURCES, CoreBootstrapProperties.LOADS_MODULE_LIBRARY_RESOURCES_DEFAULT);
            configSettings.addProperty(CoreBootstrapProperties.LOADS_MODULE_LIBRARY_RESOURCES, loadsModuleLibraryResources);
        }
        StringPropertyValue workspaceRoot = new StringPropertyValue(propertySource, CoreBootstrapProperties.WORKSPACE_ROOT, LocationConstants.WORKSPACE_ROOT_DEFAULT);
        StringPropertyValue moduleClassDirectory = new StringPropertyValue(propertySource, CoreBootstrapProperties.MODULE_CLASS_DIRECTORY, LocationConstants.MODULE_CLASS_DIR_DEFAULT);
        StringPropertyValue moduleResourceDirectory = new StringPropertyValue(propertySource, CoreBootstrapProperties.MODULE_RESOURCE_DIRECTORY, LocationConstants.MODULE_RESOURCE_DIR_DEFAULT);
        StringPropertyValue externalRootModuleName = new StringPropertyValue(propertySource, CoreBootstrapProperties.EXTERNAL_ROOT_MODULE_NAME, CoreBootstrapProperties.EXTERNAL_ROOT_MODULE_NAME_DEFAULT);
        configSettings.addProperty(CoreBootstrapProperties.PARENT_CLASS_LOADER_FIRST, parentClassloaderFirst);
        configSettings.addProperty(CoreBootstrapProperties.SUPPORTS_MODULE_LIBRARIES, supportsModuleLibraries);
        configSettings.addProperty(CoreBootstrapProperties.LOAD_TIME_WEAVING_ENABLED, loadTimeWeavingEnabled);
        configSettings.addProperty(CoreBootstrapProperties.WORKSPACE_ROOT, workspaceRoot);
        configSettings.addProperty(CoreBootstrapProperties.MODULE_CLASS_DIRECTORY, moduleClassDirectory);
        configSettings.addProperty(CoreBootstrapProperties.MODULE_RESOURCE_DIRECTORY, moduleResourceDirectory);
        configSettings.addProperty(CoreBootstrapProperties.EMBEDDED_MODE, embeddedMode);
        configSettings.addProperty(CoreBootstrapProperties.EXTERNAL_ROOT_MODULE_NAME, externalRootModuleName);
    }

    protected boolean explicitlySetLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
        return addNamedLocations(configSettings, propertySource, CoreBootstrapProperties.ALL_LOCATIONS);
    }

    protected void addDefaultLocations(ConfigurationSettings configSettings) {
        configSettings.add("META-INF/impala-bootstrap.xml");
    }

    protected void addModuleType(ConfigurationSettings configSettings, PropertySource propertySource) {
        StringPropertyValue moduleType = new StringPropertyValue(propertySource, CoreBootstrapProperties.MODULE_TYPE, "graph");
        configSettings.addProperty(CoreBootstrapProperties.MODULE_TYPE, moduleType);
        final String value = moduleType.getValue();
        if ("shared".equalsIgnoreCase(value)) {
            configSettings.add("META-INF/impala-shared-loader-bootstrap.xml");
        } else if ("graph".equalsIgnoreCase(value)) {
            configSettings.add("META-INF/impala-graph-bootstrap.xml");
            StringPropertyValue allLocations = new StringPropertyValue(propertySource, CoreBootstrapProperties.GRAPH_BEAN_VISIBILITY_TYPE, CoreBootstrapProperties.GRAPH_BEAN_VISIBILITY_TYPE_DEFAULT);
            configSettings.addProperty(CoreBootstrapProperties.GRAPH_BEAN_VISIBILITY_TYPE, allLocations);
        } else if ("hierarchical".equalsIgnoreCase(value)) {
        } else {
            throw new ConfigurationException("Invalid value for property 'classloader.type': " + value);
        }
    }

    protected void maybeAddJmxLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
        ContextLocationResolver c = null;
        try {
            c = InstantiationUtils.instantiate("org.impalaframework.jmx.bootstrap.JMXContextLocationResolver");
            c.addContextLocations(configSettings, propertySource);
        } catch (Exception e) {
        }
    }

    public void explicitlyAddLocations(ConfigurationSettings configSettings, PropertySource propertySource) {
        addNamedLocations(configSettings, propertySource, CoreBootstrapProperties.EXTRA_LOCATIONS);
    }

    private boolean addNamedLocations(ConfigurationSettings configSettings, PropertySource propertySource, final String propertyName) {
        StringPropertyValue allLocations = new StringPropertyValue(propertySource, propertyName, null);
        configSettings.addProperty(propertyName, allLocations);
        final String allLocationsValue = allLocations.getValue();
        if (allLocationsValue != null) {
            final String[] allLocationsArray = StringUtils.tokenizeToStringArray(allLocationsValue, " ,");
            final String[] fullNamesArray = getFullNames(allLocationsArray);
            for (String location : fullNamesArray) {
                configSettings.add(location);
            }
            return true;
        }
        return false;
    }

    String[] getFullNames(String[] abridgedNames) {
        String[] fullNames = new String[abridgedNames.length];
        for (int i = 0; i < abridgedNames.length; i++) {
            if (!abridgedNames[i].endsWith(".xml")) fullNames[i] = "META-INF/impala-" + abridgedNames[i] + ".xml"; else fullNames[i] = abridgedNames[i];
        }
        return fullNames;
    }
}
