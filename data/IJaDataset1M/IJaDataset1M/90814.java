package org.monet.bpi.java;

import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPIBehaviorNodeDocument;
import org.monet.bpi.BPINodeDocument;
import org.monet.bpi.BPISchema;

public abstract class BPINodeDocumentImpl<Schema extends BPISchema<?>, OperationEnum extends Enum<?>, Parent extends BPIBaseNode<?>> extends BPINodeImpl<Parent, Schema> implements BPINodeDocument<Parent, Schema>, BPIBehaviorNodeDocument<OperationEnum> {
}
