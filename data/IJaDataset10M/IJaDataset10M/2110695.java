package org.w4tj.model.mapping;

import java.util.HashSet;
import java.util.Set;
import org.w4tj.model.base.AbstractLongKeyEntity;

public class PersistedEntity extends AbstractLongKeyEntity implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String name;

    private String friendlyName;

    private Set<PersistedProperty> properties = new HashSet<PersistedProperty>();

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public String getName() {
        return this.name;
    }

    public Set<PersistedProperty> getProperties() {
        return this.properties;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProperties(Set<PersistedProperty> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
