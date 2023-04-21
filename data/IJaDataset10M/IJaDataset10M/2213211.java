package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.Iterator;
import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.Mode;
import edu.clemson.cs.r2jt.data.Symbol;
import edu.clemson.cs.r2jt.data.PosSymbol;

public class DefinitionDec extends Dec implements ModuleParameter {

    /** The implicit member. */
    private boolean implicit;

    /** The name member. */
    private PosSymbol name;

    /** The parameters member. */
    private List<MathVarDec> parameters;

    /** The returnTy member. */
    private Ty returnTy;

    /** The base member. */
    private Exp base;

    /** The hypothesis member. */
    private Exp hypothesis;

    /** The definition member. */
    private Exp definition;

    public DefinitionDec() {
    }

    ;

    public DefinitionDec(boolean implicit, PosSymbol name, List<MathVarDec> parameters, Ty returnTy, Exp base, Exp hypothesis, Exp definition) {
        this.implicit = implicit;
        this.name = name;
        this.parameters = parameters;
        this.returnTy = returnTy;
        this.base = base;
        this.hypothesis = hypothesis;
        this.definition = definition;
    }

    /** Returns the value of the implicit variable. */
    public boolean isImplicit() {
        return implicit;
    }

    /** Returns the value of the name variable. */
    public PosSymbol getName() {
        return name;
    }

    /** Returns the value of the parameters variable. */
    public List<MathVarDec> getParameters() {
        return parameters;
    }

    /** Returns the value of the returnTy variable. */
    public Ty getReturnTy() {
        return returnTy;
    }

    /** Returns the value of the base variable. */
    public Exp getBase() {
        return base;
    }

    /** Returns the value of the hypothesis variable. */
    public Exp getHypothesis() {
        return hypothesis;
    }

    /** Returns the value of the definition variable. */
    public Exp getDefinition() {
        return definition;
    }

    /** Sets the implicit variable to the specified value. */
    public void setImplicit(boolean implicit) {
        this.implicit = implicit;
    }

    /** Sets the name variable to the specified value. */
    public void setName(PosSymbol name) {
        this.name = name;
    }

    /** Sets the parameters variable to the specified value. */
    public void setParameters(List<MathVarDec> parameters) {
        this.parameters = parameters;
    }

    /** Sets the returnTy variable to the specified value. */
    public void setReturnTy(Ty returnTy) {
        this.returnTy = returnTy;
    }

    /** Sets the base variable to the specified value. */
    public void setBase(Exp base) {
        this.base = base;
    }

    /** Sets the hypothesis variable to the specified value. */
    public void setHypothesis(Exp hypothesis) {
        this.hypothesis = hypothesis;
    }

    /** Sets the definition variable to the specified value. */
    public void setDefinition(Exp definition) {
        this.definition = definition;
    }

    /** Accepts a ResolveConceptualVisitor. */
    public void accept(ResolveConceptualVisitor v) {
        v.visitDefinitionDec(this);
    }

    /** Returns a formatted text string of this class. */
    public String asString(int indent, int increment) {
        StringBuffer sb = new StringBuffer();
        printSpace(indent, sb);
        sb.append("DefinitionDec\n");
        printSpace(indent + increment, sb);
        sb.append(implicit + "\n");
        if (name != null) {
            sb.append(name.asString(indent + increment, increment));
        }
        if (parameters != null) {
            sb.append(parameters.asString(indent + increment, increment));
        }
        if (returnTy != null) {
            sb.append(returnTy.asString(indent + increment, increment));
        }
        if (base != null) {
            sb.append(base.asString(indent + increment, increment));
        }
        if (hypothesis != null) {
            sb.append(hypothesis.asString(indent + increment, increment));
        }
        if (definition != null) {
            sb.append(definition.asString(indent + increment, increment));
        }
        return sb.toString();
    }

    public void prettyPrint() {
        if (implicit) {
            System.out.print("Implicit ");
        }
        System.out.print("Definition " + name.getName());
        System.out.print("(");
        Iterator<MathVarDec> it = parameters.iterator();
        if (it.hasNext()) {
            it.next().prettyPrint();
        }
        System.out.print(") : ");
        returnTy.prettyPrint();
        if (implicit) System.out.print(" is "); else System.out.print(" = ");
        if (base != null) {
            base.prettyPrint();
            System.out.println();
            hypothesis.prettyPrint();
        } else {
            definition.prettyPrint();
        }
    }
}
