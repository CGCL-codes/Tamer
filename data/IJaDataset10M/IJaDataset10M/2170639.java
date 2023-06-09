package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.collections.Map;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.Mode;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.type.Type;
import edu.clemson.cs.r2jt.analysis.TypeResolutionException;
import edu.clemson.cs.r2jt.collections.Iterator;

public class ProgramParamExp extends ProgramExp {

    /** The location member. */
    private Location location;

    /** The name member. */
    private PosSymbol name;

    /** The arguments member. */
    private List<ProgramExp> arguments;

    /** The semanticExp member. */
    private ProgramExp semanticExp;

    public ProgramParamExp() {
    }

    ;

    public ProgramParamExp(Location location, PosSymbol name, List<ProgramExp> arguments, ProgramExp semanticExp) {
        this.location = location;
        this.name = name;
        this.arguments = arguments;
        this.semanticExp = semanticExp;
    }

    public Exp substituteChildren(java.util.Map<Exp, Exp> substitutions) {
        Exp retval;
        List<ProgramExp> newArguments = new List<ProgramExp>();
        for (ProgramExp a : arguments) {
            newArguments.add((ProgramExp) substitute(a, substitutions));
        }
        retval = new ProgramParamExp(location, name, newArguments, (ProgramExp) substitute(semanticExp, substitutions));
        retval.setType(type);
        return retval;
    }

    /** Returns the value of the location variable. */
    public Location getLocation() {
        return location;
    }

    /** Returns the value of the name variable. */
    public PosSymbol getName() {
        return name;
    }

    /** Returns the value of the arguments variable. */
    public List<ProgramExp> getArguments() {
        return arguments;
    }

    /** Returns the value of the semanticExp variable. */
    public ProgramExp getSemanticExp() {
        return semanticExp;
    }

    /** Sets the location variable to the specified value. */
    public void setLocation(Location location) {
        this.location = location;
    }

    /** Sets the name variable to the specified value. */
    public void setName(PosSymbol name) {
        this.name = name;
    }

    /** Sets the arguments variable to the specified value. */
    public void setArguments(List<ProgramExp> arguments) {
        this.arguments = arguments;
    }

    /** Sets the semanticExp variable to the specified value. */
    public void setSemanticExp(ProgramExp semanticExp) {
        this.semanticExp = semanticExp;
    }

    /** Accepts a ResolveConceptualVisitor. */
    public void accept(ResolveConceptualVisitor v) {
        v.visitProgramParamExp(this);
    }

    /** Accepts a TypeResolutionVisitor. */
    public Type accept(TypeResolutionVisitor v) throws TypeResolutionException {
        return v.getProgramParamExpType(this);
    }

    /** Returns a formatted text string of this class. */
    public String asString(int indent, int increment) {
        StringBuffer sb = new StringBuffer();
        printSpace(indent, sb);
        sb.append("ProgramParamExp\n");
        if (name != null) {
            sb.append(name.asString(indent + increment, increment));
        }
        if (arguments != null) {
            sb.append(arguments.asString(indent + increment, increment));
        }
        if (semanticExp != null) {
            sb.append(semanticExp.asString(indent + increment, increment));
        }
        return sb.toString();
    }

    /** Returns a formatted text string of this class. */
    public String toString(int indent) {
        StringBuffer sb = new StringBuffer();
        printSpace(indent, sb);
        if (semanticExp != null) {
            sb.append(semanticExp.toString(0));
        } else {
            printSpace(indent, sb);
            if (name != null) {
                sb.append(name.getName().toString());
            }
            if (arguments != null) {
                sb.append("(" + argumentsToString(arguments) + ")");
            }
        }
        return sb.toString();
    }

    String argumentsToString(List<ProgramExp> arguments) {
        String str = new String();
        Iterator<ProgramExp> i = arguments.iterator();
        while (i.hasNext()) {
            ProgramExp exp = (ProgramExp) i.next();
            str = str.concat(exp.toString(0));
            if (i.hasNext()) str = str.concat(", ");
        }
        return str;
    }

    public String toString() {
        return name.getName();
    }

    /** Returns true if the variable is found in any sub expression
        of this one. **/
    public boolean containsVar(String varName, boolean IsOldExp) {
        Iterator<ProgramExp> i = arguments.iterator();
        while (i.hasNext()) {
            ProgramExp temp = i.next();
            if (temp != null) {
                if (temp.containsVar(varName, IsOldExp)) {
                    return true;
                }
            }
        }
        if (semanticExp != null) {
            if (semanticExp.containsVar(varName, IsOldExp)) {
                return true;
            }
        }
        return false;
    }

    public List<Exp> getSubExpressions() {
        List<Exp> list = new List<Exp>();
        Iterator<ProgramExp> argIt = arguments.iterator();
        while (argIt.hasNext()) {
            list.add((Exp) (argIt.next()));
        }
        return list;
    }

    public void setSubExpression(int index, Exp e) {
        arguments.set(index, (ProgramExp) e);
    }
}
