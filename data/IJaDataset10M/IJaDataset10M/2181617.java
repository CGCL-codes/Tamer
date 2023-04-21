package org.gecko.jee.community.mobidick.datarepository.persistence.impl;

import java.util.Set;
import org.gecko.jee.community.mobidick.datarepository.persistence.mapping.Level;

public class Sibling implements Level {

    private PropertyAncestorSibling propertyAncestorSibling;

    private Set<SiblingProperty> siblingProperties;

    private Set<Sibling> siblings;

    public PropertyAncestorSibling getPropertyAncestorSibling() {
        return propertyAncestorSibling;
    }

    public Set<SiblingProperty> getSiblingProperties() {
        return siblingProperties;
    }

    public Set<Sibling> getSiblings() {
        return siblings;
    }

    public void setPropertyAncestorSibling(final PropertyAncestorSibling propertyAncestorSibling) {
        this.propertyAncestorSibling = propertyAncestorSibling;
    }

    public void setSiblingProperties(final Set<SiblingProperty> siblingProperties) {
        this.siblingProperties = siblingProperties;
    }

    public void setSiblings(final Set<Sibling> siblings) {
        this.siblings = siblings;
    }
}
