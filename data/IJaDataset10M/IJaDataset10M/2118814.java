package org.monet.kernel.model;

import org.monet.kernel.components.ComponentPersistence;

public class ReferenceProvider implements ReferenceLink {

    private ComponentPersistence componentPersistence;

    private static ReferenceProvider istance;

    private ReferenceProvider() {
        this.componentPersistence = ComponentPersistence.getInstance();
    }

    public static synchronized ReferenceProvider getInstance() {
        if (istance == null) istance = new ReferenceProvider();
        return istance;
    }

    @Override
    public Reference loadNodeReference(Node node) {
        return this.componentPersistence.loadNodeReference(node);
    }

    @Override
    public Reference loadNodeReference(Node node, String code) {
        return this.componentPersistence.loadNodeReference(node, code);
    }
}
