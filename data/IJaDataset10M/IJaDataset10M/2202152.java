package org.identifylife.key.store.model;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlType;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author dbarnier
 *
 */
@Entity
@XmlType(name = "HierarchyRef")
public class HierarchyRef extends AbstractRef {

    private String label;

    public HierarchyRef() {
    }

    public HierarchyRef(String ref) {
        setRef(ref);
    }

    public HierarchyRef(String ref, String label) {
        setRef(ref);
        setLabel(label);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("label", getLabel()).toString();
    }
}
