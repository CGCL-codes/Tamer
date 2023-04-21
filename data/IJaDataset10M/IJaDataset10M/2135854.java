package org.starobjects.jpa.metamodel.facets.prop.manytoone;

import javax.persistence.ManyToOne;

public class SimpleObjectWithManyToOneAnnotation {

    private Long parent;

    @ManyToOne
    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }
}
