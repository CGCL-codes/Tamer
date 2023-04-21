package org.fcrepo.client.utility.validate.types;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple immutable implementation of {@link ContentModelInfo}.
 * 
 * @author Jim Blake
 */
public class BasicContentModelInfo implements ContentModelInfo {

    private final ObjectInfo baseObject;

    private final Set<DsTypeModel> typeModels;

    public BasicContentModelInfo(ObjectInfo baseObject, Collection<DsTypeModel> typeModels) {
        this.baseObject = baseObject;
        this.typeModels = new HashSet<DsTypeModel>(typeModels);
    }

    public String getPid() {
        return baseObject.getPid();
    }

    public Set<DsTypeModel> getTypeModels() {
        return new HashSet<DsTypeModel>(typeModels);
    }
}
