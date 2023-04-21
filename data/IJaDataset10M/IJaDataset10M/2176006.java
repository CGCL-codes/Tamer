package edu.clemson.cs.r2jt.absyn;

import edu.clemson.cs.r2jt.collections.List;
import edu.clemson.cs.r2jt.data.Location;
import edu.clemson.cs.r2jt.data.Mode;
import edu.clemson.cs.r2jt.data.PosSymbol;
import edu.clemson.cs.r2jt.data.Symbol;

public class ParameterVarDec extends Dec {

    /** The mode member. */
    private Mode mode;

    /** The name member. */
    private PosSymbol name;

    /** The ty member. */
    private Ty ty;

    public ParameterVarDec() {
    }

    ;

    public ParameterVarDec(Mode mode, PosSymbol name, Ty ty) {
        this.mode = mode;
        this.name = name;
        this.ty = ty;
    }

    /** Returns the value of the mode variable. */
    public Mode getMode() {
        return mode;
    }

    /** Returns the value of the name variable. */
    public PosSymbol getName() {
        return name;
    }

    /** Returns the value of the ty variable. */
    public Ty getTy() {
        return ty;
    }

    /** Sets the mode variable to the specified value. */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /** Sets the name variable to the specified value. */
    public void setName(PosSymbol name) {
        this.name = name;
    }

    /** Sets the ty variable to the specified value. */
    public void setTy(Ty ty) {
        this.ty = ty;
    }

    /** Accepts a ResolveConceptualVisitor. */
    public void accept(ResolveConceptualVisitor v) {
        v.visitParameterVarDec(this);
    }

    /** Returns a formatted text string of this class. */
    public String asString(int indent, int increment) {
        StringBuffer sb = new StringBuffer();
        printSpace(indent, sb);
        sb.append("ParameterVarDec\n");
        if (mode != null) {
            printSpace(indent + increment, sb);
            sb.append(mode.toString() + "\n");
        }
        if (name != null) {
            sb.append(name.asString(indent + increment, increment));
        }
        if (ty != null) {
            sb.append(ty.asString(indent + increment, increment));
        }
        return sb.toString();
    }

    public Object clone() {
        ParameterVarDec clone = new ParameterVarDec();
        clone.setName(createPosSymbol(this.getName().toString()));
        clone.setTy((Ty) this.getTy().clone());
        clone.setMode((Mode) this.getMode());
        return clone;
    }

    private PosSymbol createPosSymbol(String name) {
        PosSymbol posSym = new PosSymbol();
        posSym.setSymbol(Symbol.symbol(name));
        return posSym;
    }
}
