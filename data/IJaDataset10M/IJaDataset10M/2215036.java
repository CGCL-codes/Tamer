package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.collections.Map;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.Mode;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.type.Type;
import edu.clemson.cs.r2jt.type.TypeMatcher;
import edu.clemson.cs.r2jt.analysis.TypeResolutionException;

public class UnaryMinusExp extends Exp {

    /** The location member. */
    private Location location;

    /** The argument member. */
    private Exp argument;

    public UnaryMinusExp() {
    }

    ;

    public UnaryMinusExp(Location location, Exp argument) {
        this.location = location;
        this.argument = argument;
    }

    public Exp substituteChildren(java.util.Map<Exp, Exp> substitutions) {
        return new UnaryMinusExp(location, substitute(argument, substitutions));
    }

    /** Returns the value of the location variable. */
    public Location getLocation() {
        return location;
    }

    /** Returns the value of the argument variable. */
    public Exp getArgument() {
        return argument;
    }

    /** Sets the location variable to the specified value. */
    public void setLocation(Location location) {
        this.location = location;
    }

    /** Sets the argument variable to the specified value. */
    public void setArgument(Exp argument) {
        this.argument = argument;
    }

    /** Accepts a ResolveConceptualVisitor. */
    public void accept(ResolveConceptualVisitor v) {
        v.visitUnaryMinusExp(this);
    }

    /** Accepts a TypeResolutionVisitor. */
    public Type accept(TypeResolutionVisitor v) throws TypeResolutionException {
        return v.getUnaryMinusExpType(this);
    }

    /** Returns a formatted text string of this class. */
    public String asString(int indent, int increment) {
        StringBuffer sb = new StringBuffer();
        printSpace(indent, sb);
        sb.append("UnaryMinusExp\n");
        if (argument != null) {
            sb.append(argument.asString(indent + increment, increment));
        }
        return sb.toString();
    }

    /** Returns true if the variable is found in any sub expression
        of this one. **/
    public boolean containsVar(String varName, boolean IsOldExp) {
        if (argument != null) {
            return argument.containsVar(varName, IsOldExp);
        }
        return false;
    }

    public List<Exp> getSubExpressions() {
        List<Exp> list = new List<Exp>();
        list.add(argument);
        return list;
    }

    public void setSubExpression(int index, Exp e) {
        argument = e;
    }

    public boolean shallowCompare(Exp e2) {
        if (!(e2 instanceof UnaryMinusExp)) {
            return false;
        }
        return true;
    }

    public void prettyPrint() {
        System.out.print("-(");
        argument.prettyPrint();
        System.out.print(")");
    }

    public Exp copy() {
        Exp newArgument = argument.copy();
        return new UnaryMinusExp(null, newArgument);
    }
}
