package research.domain;

import java.util.Set;
import research.entity.Entity;
import research.entity.EntityType;

public final class Component extends Entity {

    private String name;

    private String description;

    private ComponentType componentType;

    private Set<ComponentValue> componentValues;

    public Component() {
        super(EntityType.Component);
    }

    @Override
    public String getDisplayName() {
        return this.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ComponentValue> getComponentValues() {
        return componentValues;
    }

    public void setComponentValues(Set<ComponentValue> component_values) {
        this.componentValues = component_values;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public static Component getNew() {
        Component res = new Component();
        res.setName("");
        res.setDescription("");
        return res;
    }
}
