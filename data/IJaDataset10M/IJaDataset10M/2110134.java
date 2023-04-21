package org.monet.bpi;

public interface BPINodeCatalog<Parent extends BPIBaseNode<?>, Schema extends BPISchema<?>> extends BPINode<Parent, Schema>, BPIBaseNodeCatalog<Schema> {
}
