package com.ivis.xprocess.ui.processdesigner.diagram.model;

public class CategoryImpl extends NodeImpl implements Category {

    public CategoryImpl(String uuid, int level) {
        super(uuid, level, false);
    }

    public Type getType() {
        return Type.Category;
    }

    public boolean visit(ModelVisitor visitor) {
        return visitor.visitCategory(this);
    }
}
