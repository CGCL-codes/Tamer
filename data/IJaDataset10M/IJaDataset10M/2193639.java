package org.yacmmf.core;

public interface AttributeVisitor {

    public void visit(CollectionAttribute attribute);

    public void visit(MapAttribute attribute);

    public void visit(ReferenceAttribute attribute);
}
