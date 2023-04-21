package org.columba.core.context.base;

import org.columba.core.context.base.api.IContextFactory;
import org.columba.core.context.base.api.IStructureType;
import org.columba.core.context.base.api.IStructureValue;

public class ContextFactory implements IContextFactory {

    public ContextFactory() {
        super();
    }

    public IStructureType createStructure(String name, String namespace) {
        return new StructureType(name, namespace);
    }

    public IStructureValue createValue(String name, String namespace, IStructureType type) {
        return new StructureValue(name, namespace, type);
    }
}
