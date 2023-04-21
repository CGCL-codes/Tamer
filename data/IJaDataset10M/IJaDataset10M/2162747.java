package net.dataforte.canyon.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;

public class AttributeDescriptor {

    String type;

    String name;

    String id;

    Map<String, String> properties;

    String parentProperty;

    public AttributeDescriptor() {
        properties = new HashMap<String, String>();
    }

    public void addProperty(String name, String value) {
        if (name.equalsIgnoreCase("id")) {
            this.setId(value);
        } else {
            properties.put(name, value);
        }
    }

    /**
	 * @return the id
	 */
    public String getId() {
        return id;
    }

    /**
	 * @return the parentProperty
	 */
    public String getParentProperty() {
        return parentProperty;
    }

    public Set<String> getProperties() {
        return properties.keySet();
    }

    public String getProperty(String name) {
        return properties.get(name);
    }

    public String getType() {
        return type;
    }

    /**
	 * @param id
	 *            the id to set
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @param parentProperty
	 *            the parentProperty to set
	 */
    public void setParentProperty(String parentProperty) {
        this.parentProperty = parentProperty;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name
	 *            the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return new ToStringBuilder(this).append("name", this.name).append("properties", this.properties).append("id", this.id).append("type", this.type).append("parentProperty", this.parentProperty).toString();
    }
}
