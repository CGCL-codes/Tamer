package fr.gedeon.telnetservice.syntaxtree.ast.impl;

import fr.gedeon.telnetservice.syntaxtree.ast.EntityName;
import fr.gedeon.telnetservice.syntaxtree.ast.IdentifiedEntity;

public class IdentifiedEntityImpl implements IdentifiedEntity {

    EntityName name;

    String description;

    public IdentifiedEntityImpl(EntityName name) {
        this.name = name;
    }

    public EntityName getName() {
        return this.name;
    }

    protected void setName(EntityName name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }
}
