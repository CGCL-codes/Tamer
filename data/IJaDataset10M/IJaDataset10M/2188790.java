package org.proteusframework.platformservice.persistence.project;

import org.proteusframework.core.api.model.INamespace;
import org.proteusframework.core.base.Namespace;
import org.proteusframework.core.util.Assert;
import org.proteusframework.platformservice.persistence.api.IProjectDescriptor;
import java.util.*;

/**
 * @author Tacoma Four
 */
public class ProjectDescriptor extends Namespace implements IProjectDescriptor {

    protected String description = null;

    protected Map<String, Properties> propertiesMap = new HashMap<String, Properties>();

    public ProjectDescriptor(INamespace namespace) {
        super(namespace);
    }

    public ProjectDescriptor(String namespace, String name) {
        super(namespace, name);
    }

    public ProjectDescriptor(String namespace, String name, String description, Map<String, Properties> propertiesMap) {
        super(namespace, name);
        this.description = description;
        Assert.parameterNotNull(propertiesMap, "Parameter 'propertiesMap' must not be null");
        this.propertiesMap = propertiesMap;
    }

    public ProjectDescriptor(INamespace namespace, String description, Map<String, Properties> propertiesMap) {
        super(namespace);
        this.description = description;
        Assert.parameterNotNull(propertiesMap, "Parameter 'propertiesMap' must not be null");
        this.propertiesMap = propertiesMap;
    }

    @Override
    public Map<String, Properties> getPropertiesMap() {
        return propertiesMap;
    }

    @Override
    public List<String> listPropertyGroups() {
        List<String> keys = new ArrayList<String>();
        keys.addAll(propertiesMap.keySet());
        return keys;
    }

    @Override
    public List<String> listPropertyGroupKeys(String propertyGroup) {
        List<String> keys = new ArrayList<String>();
        if (propertiesMap.containsKey(propertyGroup)) {
            Properties properties = propertiesMap.get(propertyGroup);
            String[] keysArray = properties.keySet().toArray(new String[0]);
            keys.addAll(Arrays.asList(keysArray));
        }
        return keys;
    }

    @Override
    public Properties getProperties(String propertyGroup) {
        return propertiesMap.get(propertyGroup);
    }

    @Override
    public boolean hasProperty(String propertyGroup, String key) {
        Properties p = propertiesMap.get(propertyGroup);
        return (p != null && p.containsKey(key));
    }

    @Override
    public String getProperty(String propertyGroup, String key) {
        Properties p = propertiesMap.get(propertyGroup);
        return (null == p) ? null : p.getProperty(key);
    }

    @Override
    public String getName() {
        return getId();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static IProjectDescriptor cloneReadOnlyDescriptor(final IProjectDescriptor descriptor) {
        if (null != descriptor) {
            return new IProjectDescriptor() {

                @Override
                public Map<String, Properties> getPropertiesMap() {
                    return Collections.unmodifiableMap(descriptor.getPropertiesMap());
                }

                @Override
                public List<String> listPropertyGroups() {
                    return Collections.unmodifiableList(descriptor.listPropertyGroups());
                }

                @Override
                public List<String> listPropertyGroupKeys(String propertyGroup) {
                    return Collections.unmodifiableList(descriptor.listPropertyGroupKeys(propertyGroup));
                }

                @Override
                public Properties getProperties(String propertyGroup) {
                    Properties copy = new Properties();
                    Properties actual = descriptor.getProperties(propertyGroup);
                    for (Object key : actual.keySet()) {
                        copy.setProperty(key.toString(), actual.getProperty(key.toString()));
                    }
                    return copy;
                }

                @Override
                public void setDescription(String description) {
                    throw new UnsupportedOperationException("IProjectDescriptor instance is read-only");
                }

                @Override
                public boolean hasProperty(String propertyGroup, String key) {
                    return descriptor.hasProperty(propertyGroup, key);
                }

                @Override
                public String getProperty(String propertyGroup, String key) {
                    return descriptor.getProperty(propertyGroup, key);
                }

                @Override
                public String getName() {
                    return descriptor.getName();
                }

                @Override
                public String getDescription() {
                    return descriptor.getDescription();
                }

                @Override
                public String getFamily() {
                    return descriptor.getFamily();
                }

                @Override
                public String getId() {
                    return descriptor.getId();
                }

                @Override
                public String getRefId() {
                    return descriptor.getRefId();
                }

                @Override
                public boolean equals(Object o) {
                    if (this == o) {
                        return true;
                    }
                    if (!(o instanceof Namespace)) {
                        return false;
                    }
                    Namespace that = (Namespace) o;
                    return new Namespace(getFamily(), getId()).equals(new Namespace(that.getFamily(), that.getId()));
                }

                @Override
                public int hashCode() {
                    int result = getFamily().hashCode();
                    result = 31 * result + getId().hashCode();
                    return result;
                }
            };
        } else {
            return new IProjectDescriptor() {

                @Override
                public Map<String, Properties> getPropertiesMap() {
                    return new HashMap<String, Properties>();
                }

                @Override
                public List<String> listPropertyGroups() {
                    return new ArrayList<String>();
                }

                @Override
                public List<String> listPropertyGroupKeys(String propertyGroup) {
                    return new ArrayList<String>();
                }

                @Override
                public Properties getProperties(String propertyGroup) {
                    return new Properties();
                }

                @Override
                public boolean hasProperty(String propertyGroup, String key) {
                    return false;
                }

                @Override
                public String getProperty(String propertyGroup, String key) {
                    return null;
                }

                @Override
                public String getName() {
                    return ID;
                }

                @Override
                public String getDescription() {
                    return EMPTY_PROJECT_DESCRIPTOR_NS.getRefId();
                }

                @Override
                public void setDescription(String description) {
                    throw new UnsupportedOperationException("Read-only copy of IProjectDescriptor");
                }

                @Override
                public String getFamily() {
                    return EMPTY_PROJECT_DESCRIPTOR_NS.getFamily();
                }

                @Override
                public String getId() {
                    return EMPTY_PROJECT_DESCRIPTOR_NS.getId();
                }

                @Override
                public String getRefId() {
                    return EMPTY_PROJECT_DESCRIPTOR_NS.getRefId();
                }
            };
        }
    }

    @Override
    public String toString() {
        return "ProjectDescriptor{" + "description='" + description + '\'' + ", propertiesMap=" + propertiesMap + "} " + super.toString();
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    public void addGroupKey(String groupKey) {
        propertiesMap.put(groupKey, new Properties());
    }
}
