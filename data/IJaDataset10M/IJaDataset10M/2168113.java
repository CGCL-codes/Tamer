package org.mobicents.ssf.flow.engine.builder.template;

import java.util.ArrayList;
import java.util.List;

public class ActionTemplate extends AbstractAnnotatedTemplate {

    private String type;

    private List<PropertyTemplate> properties;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PropertyTemplate> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertyTemplate> properties) {
        this.properties = properties;
    }

    public void addProperty(PropertyTemplate property) {
        if (this.properties == null) {
            this.properties = new ArrayList<PropertyTemplate>();
        }
        this.properties.add(property);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ActionTemplate:");
        sb.append(super.toString());
        sb.append("[type=" + type + "]");
        if (properties != null) {
            sb.append("[properties=");
            for (PropertyTemplate p : properties) {
                sb.append(p);
            }
            sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public Object clone() {
        ActionTemplate template = (ActionTemplate) super.clone();
        for (PropertyTemplate p : this.properties) {
            template.addProperty(p);
        }
        template.type = this.type;
        return template;
    }
}
