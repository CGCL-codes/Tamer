package net.cmp4oaw.ea_com.connector;

import net.cmp4oaw.ea_com.visitable.EA_ConnectorVisitable;
import net.cmp4oaw.ea_com.visitor.EA_BaseVisitor;

/**
 * @author Ueli Brawand
 */
public class EA_Dependency extends EA_Connector implements EA_ConnectorVisitable {

    public void accept(EA_BaseVisitor iter) {
        iter.getVisitor(this).visit(this);
    }
}
