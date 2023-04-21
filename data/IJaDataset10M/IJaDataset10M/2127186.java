package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.collections.Map;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.Mode;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.type.Type;
import edu.clemson.cs.r2jt.type.TypeMatcher;
import edu.clemson.cs.r2jt.analysis.TypeResolutionException;

public class DoubleExp extends Exp {

    /** The location member. */
    private Location location;

    /** The value member. */
    private double value;

    public DoubleExp() {
    }

    ;

    public DoubleExp(Location location, double value) {
        this.location = location;
        this.value = value;
    }

    /** Returns the value of the location variable. */
    public Location getLocation() {
        return location;
    }

    /** Returns the value of the value variable. */
    public double getValue() {
        return value;
    }

    /** Sets the location variable to the specified value. */
    public void setLocation(Location location) {
        this.location = location;
    }

    /** Sets the value variable to the specified value. */
    public void setValue(double value) {
        this.value = value;
    }

    public Exp substituteChildren(java.util.Map<Exp, Exp> substitutions) {
        return new DoubleExp(location, value);
    }

    /** Accepts a ResolveConceptualVisitor. */
    public void accept(ResolveConceptualVisitor v) {
        v.visitDoubleExp(this);
    }

    /** Accepts a TypeResolutionVisitor. */
    public Type accept(TypeResolutionVisitor v) throws TypeResolutionException {
        return v.getDoubleExpType(this);
    }

    /** Returns a formatted text string of this class. */
    public String asString(int indent, int increment) {
        StringBuffer sb = new StringBuffer();
        printSpace(indent, sb);
        sb.append("DoubleExp\n");
        printSpace(indent + increment, sb);
        sb.append(value + "\n");
        return sb.toString();
    }

    /** Returns true if the variable is found in any sub expression
        of this one. **/
    public boolean containsVar(String varName, boolean IsOldExp) {
        return false;
    }

    public List<Exp> getSubExpressions() {
        return new List<Exp>();
    }

    public void setSubExpression(int index, Exp e) {
    }

    public boolean shallowCompare(Exp e2) {
        if (!(e2 instanceof DoubleExp)) {
            return false;
        }
        if (value != ((DoubleExp) e2).getValue()) {
            return false;
        }
        return true;
    }

    public void prettyPrint() {
        System.out.print(value);
    }

    public Exp copy() {
        return new DoubleExp(null, value);
    }
}
