package net.sourceforge.cruisecontrol.dashboard.testhelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.LabelIncrementer;
import net.sourceforge.cruisecontrol.PluginRegistry;
import net.sourceforge.cruisecontrol.ProjectConfig;
import net.sourceforge.cruisecontrol.ProjectHelper;
import net.sourceforge.cruisecontrol.ProjectInterface;
import net.sourceforge.cruisecontrol.ProjectXMLHelper;
import net.sourceforge.cruisecontrol.ResolverHolder;
import net.sourceforge.cruisecontrol.labelincrementers.DefaultLabelIncrementer;
import net.sourceforge.cruisecontrol.util.Util;
import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * A plugin that represents the whole XML config file.
 * 
 * @author <a href="mailto:jerome@coffeebreaks.org">Jerome Lacoste</a>
 */
public class DashboardConfig {

    private static final Logger LOG = Logger.getLogger(DashboardConfig.class);

    public static final String LABEL_INCREMENTER = "labelincrementer";

    public static final boolean FAIL_UPON_MISSING_PROPERTY = false;

    private static final Set KNOWN_ROOT_CHILD_NAMES = new HashSet();

    static {
        KNOWN_ROOT_CHILD_NAMES.add("include.projects");
        KNOWN_ROOT_CHILD_NAMES.add("property");
        KNOWN_ROOT_CHILD_NAMES.add("plugin");
        KNOWN_ROOT_CHILD_NAMES.add("system");
    }

    private Map rootProperties = new HashMap();

    /**
     * Properties of a particular node. Mapped by the node name. Doesn't handle
     * rootProperties yet
     */
    private Map templatePluginProperties = new HashMap();

    private PluginRegistry rootPlugins = PluginRegistry.createRegistry();

    private Map projects = new LinkedHashMap();

    private Map projectPluginRegistries = new TreeMap();

    private final ResolverHolder resolvers;

    public DashboardConfig(final Element ccElement) throws CruiseControlException {
        this(ccElement, new ResolverHolder.DummeResolvers());
    }

    public DashboardConfig(final Element ccElement, final ResolverHolder resolvers) throws CruiseControlException {
        this.resolvers = resolvers;
        parse(ccElement);
    }

    private void parse(Element ccElement) throws CruiseControlException {
        for (Iterator i = ccElement.getChildren("property").iterator(); i.hasNext(); ) {
            handleRootProperty((Element) i.next());
        }
        for (Iterator i = ccElement.getChildren("plugin").iterator(); i.hasNext(); ) {
            handleRootPlugin((Element) i.next());
        }
        for (Iterator i = ccElement.getChildren("include.projects").iterator(); i.hasNext(); ) {
            handleIncludedProjects((Element) i.next());
        }
        for (Iterator i = ccElement.getChildren().iterator(); i.hasNext(); ) {
            Element childElement = (Element) i.next();
            final String nodeName = childElement.getName();
            if (isProject(nodeName)) {
                handleProject(childElement);
            } else if (!KNOWN_ROOT_CHILD_NAMES.contains(nodeName)) {
                throw new CruiseControlException("cannot handle child of <" + nodeName + ">");
            }
        }
    }

    private DashboardConfig(final Element includedElement, final DashboardConfig parent) throws CruiseControlException {
        resolvers = parent.resolvers;
        rootPlugins = PluginRegistry.createRegistry(parent.rootPlugins);
        rootProperties = new HashMap(parent.rootProperties);
        templatePluginProperties = new HashMap(parent.templatePluginProperties);
        parse(includedElement);
    }

    private void handleIncludedProjects(final Element includeElement) {
        String path = includeElement.getAttributeValue("file");
        if (path == null) {
            LOG.warn("include.projects element missing file attribute. Skipping.");
        }
        if (resolvers == null || resolvers.getXmlResolver() == null) {
            LOG.debug("xmlResolver not available; skipping include.projects element. ok if validating config.");
            return;
        }
        try {
            path = Util.parsePropertiesInString(rootProperties, path, FAIL_UPON_MISSING_PROPERTY);
            LOG.debug("getting included projects from " + path);
            final Element includedElement = resolvers.getXmlResolver().getElement(path);
            final DashboardConfig includedConfig = new DashboardConfig(includedElement, this);
            final Set includedProjectNames = includedConfig.getProjectNames();
            for (final Iterator iter = includedProjectNames.iterator(); iter.hasNext(); ) {
                final String name = (String) iter.next();
                if (projects.containsKey(name)) {
                    String message = "Project " + name + " included from " + path + " is a duplicate name. Omitting.";
                    LOG.error(message);
                }
                projects.put(name, includedConfig.getProject(name));
            }
        } catch (CruiseControlException e) {
            LOG.error("Exception including file " + path, e);
        }
    }

    private boolean isProject(String nodeName) throws CruiseControlException {
        return rootPlugins.isPluginRegistered(nodeName) && ProjectInterface.class.isAssignableFrom(rootPlugins.getPluginClass(nodeName));
    }

    private boolean isProjectTemplate(Element pluginElement) {
        String pluginName = pluginElement.getAttributeValue("name");
        String pluginClassName = pluginElement.getAttributeValue("classname");
        if (pluginClassName == null) {
            pluginClassName = rootPlugins.getPluginClassname(pluginName);
        }
        try {
            Class pluginClass = rootPlugins.instanciatePluginClass(pluginClassName, pluginName);
            return ProjectInterface.class.isAssignableFrom(pluginClass);
        } catch (CruiseControlException e) {
            LOG.warn("Couldn't check if the plugin " + pluginName + " is an instance of ProjectInterface", e);
            return false;
        }
    }

    private void handleRootPlugin(Element pluginElement) throws CruiseControlException {
        String pluginName = pluginElement.getAttributeValue("name");
        if (pluginName == null) {
            LOG.warn("Config contains plugin without a name-attribute, ignoring it");
            return;
        }
        if (isProjectTemplate(pluginElement)) {
            handleNodeProperties(pluginElement, pluginName);
        }
        rootPlugins.register(pluginElement);
    }

    private void handleNodeProperties(Element pluginElement, String pluginName) {
        List properties = new ArrayList();
        for (Iterator i = pluginElement.getChildren("property").iterator(); i.hasNext(); ) {
            properties.add(i.next());
        }
        if (properties.size() > 0) {
            templatePluginProperties.put(pluginName, properties);
        }
        pluginElement.removeChildren("property");
    }

    private void handleRootProperty(final Element childElement) throws CruiseControlException {
        ProjectXMLHelper.registerProperty(rootProperties, childElement, resolvers, FAIL_UPON_MISSING_PROPERTY);
    }

    private void handleProject(final Element projectElement) throws CruiseControlException {
        final String projectName = getProjectName(projectElement);
        if (projects.containsKey(projectName)) {
            final String duplicateEntriesMessage = "Duplicate entries in config file for project name " + projectName;
            throw new CruiseControlException(duplicateEntriesMessage);
        }
        final MapWithParent nonFullyResolvedProjectProperties = new MapWithParent(rootProperties);
        LOG.debug("Setting property \"project.name\" to \"" + projectName + "\".");
        nonFullyResolvedProjectProperties.put("project.name", projectName);
        final List projectTemplateProperties = (List) templatePluginProperties.get(projectElement.getName());
        if (projectTemplateProperties != null) {
            for (int i = 0; i < projectTemplateProperties.size(); i++) {
                final Element element = (Element) projectTemplateProperties.get(i);
                ProjectXMLHelper.registerProperty(nonFullyResolvedProjectProperties, element, resolvers, FAIL_UPON_MISSING_PROPERTY);
            }
        }
        for (final Iterator projProps = projectElement.getChildren("property").iterator(); projProps.hasNext(); ) {
            final Element propertyElement = (Element) projProps.next();
            ProjectXMLHelper.registerProperty(nonFullyResolvedProjectProperties, propertyElement, resolvers, FAIL_UPON_MISSING_PROPERTY);
        }
        final Map thisProperties = nonFullyResolvedProjectProperties.thisMap;
        for (final Iterator iterator = rootProperties.keySet().iterator(); iterator.hasNext(); ) {
            final String key = (String) iterator.next();
            if (!thisProperties.containsKey(key)) {
                final String value = (String) rootProperties.get(key);
                thisProperties.put(key, Util.parsePropertiesInString(thisProperties, value, false));
            }
        }
        ProjectXMLHelper.parsePropertiesInElement(projectElement, thisProperties, FAIL_UPON_MISSING_PROPERTY);
        final PluginRegistry projectPlugins = PluginRegistry.createRegistry(rootPlugins);
        for (final Iterator pluginIter = projectElement.getChildren("plugin").iterator(); pluginIter.hasNext(); ) {
            projectPlugins.register((Element) pluginIter.next());
        }
        projectElement.removeChildren("property");
        projectElement.removeChildren("plugin");
        LOG.debug("**************** configuring project " + projectName + " *******************");
        final ProjectHelper projectHelper = new ProjectXMLHelper(thisProperties, projectPlugins, resolvers);
        final ProjectInterface project;
        try {
            project = (ProjectInterface) projectHelper.configurePlugin(projectElement, false);
        } catch (CruiseControlException e) {
            throw new CruiseControlException("error configuring project " + projectName, e);
        }
        if (project instanceof ProjectConfig) {
            final ProjectConfig projectConfig = (ProjectConfig) project;
            if (projectConfig.getLabelIncrementer() == null) {
                final Class labelIncrClass = projectPlugins.getPluginClass(LABEL_INCREMENTER);
                LabelIncrementer labelIncrementer;
                try {
                    labelIncrementer = (LabelIncrementer) labelIncrClass.newInstance();
                } catch (Exception e) {
                    LOG.error("Error instantiating label incrementer named " + labelIncrClass.getName() + "in project " + projectName + ". Using DefaultLabelIncrementer instead.", e);
                    labelIncrementer = new DefaultLabelIncrementer();
                }
                projectConfig.add(labelIncrementer);
            }
        }
        LOG.debug("**************** end configuring project " + projectName + " *******************");
        this.projects.put(projectName, project);
        this.projectPluginRegistries.put(projectName, projectPlugins);
    }

    private String getProjectName(Element childElement) throws CruiseControlException {
        if (!isProject(childElement.getName())) {
            throw new IllegalStateException("Invalid Node <" + childElement.getName() + "> (not a project)");
        }
        String rawName = childElement.getAttribute("name").getValue();
        return Util.parsePropertiesInString(rootProperties, rawName, false);
    }

    public ProjectInterface getProject(String name) {
        return (ProjectInterface) this.projects.get(name);
    }

    public Set getProjectNames() {
        return Collections.unmodifiableSet(this.projects.keySet());
    }

    PluginRegistry getRootPlugins() {
        return rootPlugins;
    }

    PluginRegistry getProjectPlugins(String name) {
        return (PluginRegistry) this.projectPluginRegistries.get(name);
    }

    private static class MapWithParent implements Map {

        private Map parent;

        private Map thisMap;

        MapWithParent(Map parent) {
            this.parent = parent;
            this.thisMap = new HashMap();
        }

        public int size() {
            int size = thisMap.size();
            if (parent != null) {
                Set keys = parent.keySet();
                for (Iterator iterator = keys.iterator(); iterator.hasNext(); ) {
                    String key = (String) iterator.next();
                    if (!thisMap.containsKey(key)) {
                        size++;
                    }
                }
            }
            return size;
        }

        public boolean isEmpty() {
            boolean parentIsEmpty = parent == null || parent.isEmpty();
            return parentIsEmpty && thisMap.isEmpty();
        }

        public boolean containsKey(Object key) {
            return thisMap.containsKey(key) || (parent != null && parent.containsKey(key));
        }

        public boolean containsValue(Object value) {
            return thisMap.containsValue(value) || (parent != null && parent.containsValue(value));
        }

        public Object get(Object key) {
            Object value = thisMap.get(key);
            if (value == null && parent != null) {
                value = parent.get(key);
            }
            return value;
        }

        public Object put(Object o, Object o1) {
            return thisMap.put(o, o1);
        }

        public Object remove(Object key) {
            throw new UnsupportedOperationException("'remove' not supported on MapWithParent");
        }

        public void putAll(Map map) {
            thisMap.putAll(map);
        }

        public void clear() {
            throw new UnsupportedOperationException("'clear' not supported on MapWithParent");
        }

        public Set keySet() {
            Set keys = new HashSet(thisMap.keySet());
            if (parent != null) {
                keys.addAll(parent.keySet());
            }
            return keys;
        }

        public Collection values() {
            throw new UnsupportedOperationException("not implemented");
        }

        public Set entrySet() {
            Set entries = new HashSet(thisMap.entrySet());
            if (parent != null) {
                entries.addAll(parent.entrySet());
            }
            return entries;
        }
    }
}
