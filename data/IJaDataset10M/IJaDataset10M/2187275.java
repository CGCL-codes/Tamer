package org.objectwiz.plugin.customview.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.objectwiz.core.facet.customization.EntityBase;

/**
 * A group of properties.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
public class PropertyGroup extends EntityBase {

    private ClassSettings declaringClass;

    private List<PropertySettings> properties;

    public PropertyGroup() {
    }

    @Transient
    public String getName() {
        return "g$" + this.getId();
    }

    @ManyToOne(optional = false)
    public ClassSettings getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(ClassSettings declaringClass) {
        this.declaringClass = declaringClass;
    }

    @OneToMany(mappedBy = "propertyGroup")
    public List<PropertySettings> getProperties() {
        return properties;
    }

    public void setProperties(List<PropertySettings> properties) {
        this.properties = properties;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof PropertyGroup)) return false;
        return ((PropertyGroup) obj).getName().equals(getName());
    }
}
