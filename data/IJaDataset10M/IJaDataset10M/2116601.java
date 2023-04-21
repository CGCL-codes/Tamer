package edge.dataaccess.metamodel;

import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Represents DataAccess entity type. Each entity type has its own
 * collection of properties and may be thought of as a class of entity.
 *
 * @created 2008/11/02
 */
public class EntityType {

    private String name;

    private Collection<Property> properties = new ArrayList<Property>();

    EntityType(String name, Collection<Property> properties) {
        setName(name);
        if (properties != null) {
            this.properties.addAll(properties);
        }
    }

    private void setName(String name) {
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("Entity type name must not be empty or null");
        }
        this.name = name;
    }

    public Collection<Property> getProperties() {
        return Collections.unmodifiableCollection(properties);
    }

    public Property getProperty(String name) {
        for (Property property : properties) {
            if (property.getName().equals(name)) {
                return property;
            }
        }
        throw new IllegalArgumentException("Property with name: " + name + " not found " + "on entity type: " + getName());
    }

    public String getName() {
        return name;
    }
}
