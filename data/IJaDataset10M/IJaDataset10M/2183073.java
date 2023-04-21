package de.objectcode.time4u.store.meta;

import org.eclipse.core.runtime.IConfigurationElement;

public class MetaDefinition {

    private String m_name;

    private String m_label;

    private MetaType m_type;

    public MetaDefinition(IConfigurationElement element) {
        m_name = element.getAttribute("ID");
        m_label = element.getAttribute("label");
        m_type = MetaType.byConfigName(element.getAttribute("type"));
    }

    public String getLabel() {
        return m_label;
    }

    public String getName() {
        return m_name;
    }

    public MetaType getType() {
        return m_type;
    }
}
