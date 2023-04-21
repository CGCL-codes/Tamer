package abc.weaving.aspectinfo;

import java.util.*;
import polyglot.util.Position;
import soot.*;
import abc.weaving.matching.WeavingEnv;
import abc.weaving.residues.*;

/** An argument pattern denoting a pointcut variable. 
 *  @author Aske Simon Christensen
 *  @author Ganesh Sittampalam
 *  @author Damien Sereni
 */
public class ArgVar extends ArgAny {

    private Var var;

    public ArgVar(Var var, Position pos) {
        super(pos);
        this.var = var;
    }

    public Var getVar() {
        return var;
    }

    public String toString() {
        return var.toString();
    }

    public Residue matchesAt(WeavingEnv we, ContextValue cv) {
        return Bind.construct(cv, we.getAbcType(var).getSootType(), we.getWeavingVar(var));
    }

    public Var substituteForPointcutFormal(Hashtable renameEnv, Hashtable typeEnv, Formal formal, List newLocals, List newCasts, Position pos) {
        Var oldvar = this.var.rename(renameEnv);
        AbcType actualType = (AbcType) typeEnv.get(var.getName());
        if (actualType == null) throw new RuntimeException(var.getName());
        if (actualType.getSootType().equals(formal.getType().getSootType())) {
            return oldvar;
        }
        String name = Pointcut.freshVar();
        Var newvar = new Var(name, pos);
        newLocals.add(new Formal(formal.getType(), name, pos));
        newCasts.add(new CastPointcutVar(newvar, oldvar, pos));
        return newvar;
    }

    public void getFreeVars(Set result) {
        result.add(var.getName());
    }

    public boolean unify(ArgPattern other, Unification unification) {
        if (other.getClass() == this.getClass()) {
            Var othervar = ((ArgVar) other).getVar();
            if (var.unify(othervar, unification)) {
                Var unifiedvar = unification.getVar();
                if (unifiedvar == var) {
                    unification.setArgPattern(this);
                    return true;
                } else {
                    if (unification.unifyWithFirst()) throw new RuntimeException("Unfication error: restricted unification failed");
                    if (unifiedvar == othervar) {
                        unification.setArgPattern(other);
                        return true;
                    } else {
                        unification.setArgPattern(new ArgVar(unifiedvar, unifiedvar.getPosition()));
                        return true;
                    }
                }
            } else return false;
        } else if (other.getClass() == ArgType.class) {
            if (abc.main.Debug.v().debugPointcutUnification) System.out.println("Trying to unify " + this + " with an ArgType: " + other);
            ArgType otherargtype = (ArgType) other;
            if (unification.getType1(this.getVar().getName()).equals(otherargtype.getType())) {
                if (abc.main.Debug.v().debugPointcutUnification) System.out.println("Succeeded!");
                unification.setArgPattern(this);
                unification.put2(this.getVar(), new VarBox());
                return true;
            } else return false;
        } else return false;
    }
}
