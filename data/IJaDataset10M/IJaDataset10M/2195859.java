package dataImport.model;

import dataImport.model.abstracts.AbstractInteractableEntity;

public class Place extends AbstractInteractableEntity {

    private static final long serialVersionUID = 1059508470113692500L;

    public Place(final String id, final String name) {
        super(id, name);
    }
}
