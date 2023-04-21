package org.monet.bpi.java;

import org.monet.bpi.BPIBaseNodeCatalog;
import org.monet.bpi.BPIBehaviorNodeCatalog;
import org.monet.bpi.BPISchema;

public abstract class BPIBaseNodeCatalogImpl<Schema extends BPISchema<?>, OperationEnum extends Enum<?>> extends BPIBaseNodeImpl<Schema> implements BPIBaseNodeCatalog<Schema>, BPIBehaviorNodeCatalog<OperationEnum> {
}
