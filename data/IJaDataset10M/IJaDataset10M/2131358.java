package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.Mode;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.data.Symbol;
import edu.clemson.cs.r2jt.type.Type;
import edu.clemson.cs.r2jt.analysis.TypeResolutionException;

/**
 * <p>A <code>Ty</code> represents the <em>description</em> of a 
 * <code>Type</code>, as it is found in the RESOLVE source code.  That is, it is
 * representation of a type in the abstract syntax tree before it is translated 
 * into a true <code>Type</code>.</p>
 * 
 * <p>It can be converted into a <code>Type</code> by a type.TypeConverter.</p>
 */
public abstract class Ty extends ResolveConceptualElement implements Cloneable {

    public abstract void accept(ResolveConceptualVisitor v);

    public abstract Type accept(TypeResolutionVisitor v) throws TypeResolutionException;

    public abstract String asString(int indent, int increment);

    public Ty copy() {
        System.out.println("Shouldn't be calling Ty.copy()!");
        return null;
    }

    public void prettyPrint() {
        System.out.println("Shouldn't be calling Ty.prettyPrint()!");
    }

    public String toString(int indent) {
        return this.asString(0, 0);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError("But we are Cloneable!!!");
        }
    }
}
