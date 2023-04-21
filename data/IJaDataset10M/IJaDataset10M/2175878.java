package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.Mode;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.type.Type;
import edu.clemson.cs.r2jt.analysis.TypeResolutionException;

public abstract class ProgramExp extends Exp {

    public abstract void accept(ResolveConceptualVisitor v);

    public abstract Type accept(TypeResolutionVisitor v) throws TypeResolutionException;

    public abstract String asString(int indent, int increment);

    public abstract String toString(int indent);

    /** Should be overridden by classes extending ProgramExp. **/
    public boolean containsVar(String varName, boolean IsOldExp) {
        return false;
    }
}
