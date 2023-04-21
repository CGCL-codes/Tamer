package abc.weaving.aspectinfo;

import java.util.Hashtable;
import java.util.Set;
import polyglot.types.SemanticException;
import polyglot.util.Position;
import abc.weaving.matching.MatchingContext;
import abc.weaving.residues.OrResidue;
import abc.weaving.residues.Residue;

/** Pointcut disjunction. 
 *  @author Aske Simon Christensen
 *  @author Ganesh Sittampalam
 *  @author Damien Sereni
 *  @author Eric Bodden
 */
public class OrPointcut extends Pointcut {

    private Pointcut pc1;

    private Pointcut pc2;

    private OrPointcut(Pointcut pc1, Pointcut pc2, Position pos) {
        super(pos);
        this.pc1 = pc1;
        this.pc2 = pc2;
    }

    public static Pointcut construct(Pointcut pc1, Pointcut pc2, Position pos) {
        if (pc2 instanceof EmptyPointcut || pc1 instanceof FullPointcut) return pc1;
        if (pc1 instanceof EmptyPointcut || pc2 instanceof FullPointcut) return pc2;
        return new OrPointcut(pc1, pc2, pos);
    }

    public Pointcut getLeftPointcut() {
        return pc1;
    }

    public Pointcut getRightPointcut() {
        return pc2;
    }

    public Residue matchesAt(MatchingContext mc) throws SemanticException {
        return OrResidue.construct(pc1.matchesAt(mc), pc2.matchesAt(mc));
    }

    public Pointcut inline(Hashtable renameEnv, Hashtable typeEnv, Aspect context, int cflowdepth) {
        Pointcut pc1 = this.pc1.inline(renameEnv, typeEnv, context, cflowdepth);
        Pointcut pc2 = this.pc2.inline(renameEnv, typeEnv, context, cflowdepth);
        if (pc1 == this.pc1 && pc2 == this.pc2) return this; else return construct(pc1, pc2, getPosition());
    }

    public DNF dnf() {
        DNF dnf1 = pc1.dnf();
        DNF dnf2 = pc2.dnf();
        return DNF.or(dnf1, dnf2);
    }

    public String toString() {
        return "(" + pc1 + ") || (" + pc2 + ")";
    }

    public void registerSetupAdvice(Aspect context, Hashtable typeMap) {
        pc1.registerSetupAdvice(context, typeMap);
        pc2.registerSetupAdvice(context, typeMap);
    }

    public void getFreeVars(Set result) {
        pc1.getFreeVars(result);
        pc2.getFreeVars(result);
    }

    public boolean unify(Pointcut otherpc, Unification unification) {
        if (otherpc.getClass() == this.getClass()) {
            OrPointcut oth = (OrPointcut) otherpc;
            if (pc1.unify(oth.getLeftPointcut(), unification)) {
                Pointcut pc1new = unification.getPointcut();
                if (pc2.unify(oth.getRightPointcut(), unification)) {
                    Pointcut pc2new = unification.getPointcut();
                    if ((pc1new == pc1) && (pc2new == pc2)) unification.setPointcut(this); else {
                        if (unification.unifyWithFirst()) throw new RuntimeException("Unfication error: restricted unification failed");
                        if ((pc1new == oth.getLeftPointcut()) && (pc2new == oth.getRightPointcut())) unification.setPointcut(otherpc); else unification.setPointcut(OrPointcut.construct(pc1new, pc2new, getPosition()));
                    }
                    return true;
                } else return false;
            } else return false;
        } else return LocalPointcutVars.unifyLocals(this, otherpc, unification);
    }
}
