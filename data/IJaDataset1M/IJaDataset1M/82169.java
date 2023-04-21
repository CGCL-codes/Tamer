package org.caleigo.core.meta;

import org.caleigo.core.*;

public abstract class AbstractMetaEntityDescriptor extends AbstractEntityDescriptor {

    /** Note that the constructor for this class is protected.
     */
    protected AbstractMetaEntityDescriptor(String codeName, String sourceName, String displayName, String entityClassName, String dataSourceClassName, int entityType, IFieldDescriptor[] fields, IEntityRelation[] relations) {
        super(codeName, sourceName, displayName, entityClassName, dataSourceClassName, entityType, fields, relations);
    }
}
