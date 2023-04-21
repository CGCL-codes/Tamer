package net.cmp4oaw.ea_com.state;

import net.cmp4oaw.ea_com.element_features.EA_Method;
import net.cmp4oaw.ea_com.visitable.EA_StateVisitable;
import net.cmp4oaw.ea_com.visitor.EA_BaseVisitor;

public class EA_StateBehavior extends EA_Method implements EA_StateVisitable {

    @Override
    public void accept(EA_BaseVisitor iter) {
        iter.getVisitor(this).visit(this);
    }
}
