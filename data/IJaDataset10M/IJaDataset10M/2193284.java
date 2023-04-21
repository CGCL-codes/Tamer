package org.zamia.vhdl.ast;

import org.zamia.instgraph.IGContainer;
import org.zamia.instgraph.IGElaborationEnv;
import org.zamia.instgraph.IGType;

/**
 * The VHDL way of forward type declarations.
 * 
 * @author Guenter Bartsch
 * 
 */
@SuppressWarnings("serial")
public class TypeDefinitionIncomplete extends TypeDefinition {

    public TypeDefinitionIncomplete(VHDLNode parent_, long location_) {
        super(parent_, location_);
    }

    @Override
    public int getNumChildren() {
        return 0;
    }

    @Override
    public VHDLNode getChild(int idx_) {
        return null;
    }

    @Override
    public IGType computeIG(IGContainer aContainer, IGElaborationEnv aCache) {
        reportError("Sorry, not implemented yet.");
        return IGType.createErrorType(aCache.getZDB());
    }
}
