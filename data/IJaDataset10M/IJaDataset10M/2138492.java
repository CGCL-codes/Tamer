package edu.asu.vspace.dspace.dspaceMetamodel.extension.datamodel;

import edu.asu.vspace.dspace.dspaceMetamodel.extension.datamodel.impl.Collection;

public class CollectionCreator implements ICreator {

    public IDSpaceObject create(String handle) {
        return new Collection(handle);
    }
}
