package org.sgodden.entities;

import java.io.Serializable;
import org.sgodden.entities.impl.DefaultEntityInstanceImpl;

public class ToOneChildEntry implements Serializable {

    private String relationName;

    private EntityInstance instance;

    private String label;

    public ToOneChildEntry(String relationName, EntityInstance instance, String label) {
        super();
        this.relationName = relationName;
        this.instance = instance;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public EntityInstance getInstance() {
        return instance;
    }

    public void setInstance(EntityInstance instance) {
        this.instance = instance;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public ToOneChildEntry makeClone() {
        return new ToOneChildEntry(relationName, instance.makeClone(), label);
    }
}
