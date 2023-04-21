package de.objectcode.time4u.store.meta;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;
import org.eclipse.core.runtime.IConfigurationElement;

public class MetaCategory {

    private String m_name;

    private String m_label;

    private Map<String, MetaDefinition> m_projectProperties;

    private Map<String, MetaDefinition> m_taskProperties;

    private Map<String, MetaDefinition> m_todoProperties;

    MetaCategory() {
        m_projectProperties = new TreeMap<String, MetaDefinition>();
        m_taskProperties = new TreeMap<String, MetaDefinition>();
        m_todoProperties = new TreeMap<String, MetaDefinition>();
    }

    public void configure(IConfigurationElement element) {
        m_name = element.getAttribute("ID");
        m_label = element.getAttribute("label");
        for (IConfigurationElement child : element.getChildren()) {
            MetaDefinition definition = new MetaDefinition(child);
            if ("projectProperty".equals(child.getName())) m_projectProperties.put(definition.getName(), definition); else if ("taskProperty".equals(child.getName())) m_taskProperties.put(definition.getName(), definition); else if ("todoProperty".equals(child.getName())) m_todoProperties.put(definition.getName(), definition);
        }
    }

    public String getName() {
        return m_name;
    }

    public String getLabel() {
        return m_label;
    }

    public Collection<MetaDefinition> getProjectProperties() {
        return m_projectProperties.values();
    }

    public Collection<MetaDefinition> getTaskProperties() {
        return m_taskProperties.values();
    }

    public Collection<MetaDefinition> getTodoProperties() {
        return m_todoProperties.values();
    }
}
