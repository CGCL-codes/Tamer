package abc.weaving.aspectinfo;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import polyglot.types.SemanticException;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import soot.SootMethod;
import abc.weaving.matching.MatchingContext;
import abc.weaving.matching.ShadowMatch;
import abc.weaving.matching.WeavingEnv;
import abc.weaving.residues.AlwaysMatch;
import abc.weaving.residues.AndResidue;
import abc.weaving.residues.ContextValue;
import abc.weaving.residues.NeverMatch;
import abc.weaving.residues.Residue;

/** Handler for <code>args</code> condition pointcut.
 *  @author Aske Simon Christensen
 *  @author Ganesh Sittampalam
 *  @author Damien Sereni
 *  @author Eric Bodden
 */
public class Args extends DynamicValuePointcut {

    private List args;

    /** Create an <code>args</code> pointcut.
     *  @param args a list of {@link abc.weaving.aspectinfo.ArgPattern} objects
     */
    public Args(List args, Position pos) {
        super(pos);
        this.args = args;
    }

    /** Get the list of argument patterns.
     *  @return a list of {@link abc.weaving.aspectinfo.ArgPattern} objects
     */
    public List getArgs() {
        return args;
    }

    public String toString() {
        StringBuffer out = new StringBuffer("args(");
        Iterator it = args.iterator();
        while (it.hasNext()) {
            out.append(it.next());
            if (it.hasNext()) out.append(",");
        }
        out.append(")");
        return out.toString();
    }

    public Pointcut inline(Hashtable renameEnv, Hashtable typeEnv, Aspect context, int cflowdepth) {
        Iterator it = args.iterator();
        List newargs = new LinkedList();
        while (it.hasNext()) {
            ArgPattern arg = (ArgPattern) it.next();
            if (arg instanceof ArgVar) {
                ArgVar argvar = (ArgVar) arg;
                newargs.add(new ArgVar(argvar.getVar().rename(renameEnv), argvar.getPosition()));
            } else newargs.add(arg);
        }
        return new Args(newargs, getPosition());
    }

    public Residue matchesAt(MatchingContext mc) throws SemanticException {
        WeavingEnv we = mc.getWeavingEnv();
        SootMethod method = mc.getSootMethod();
        ShadowMatch sm = mc.getShadowMatch();
        if (abc.main.Debug.v().showArgsMatching) System.out.println("args=" + args + "sm=" + sm + " of type " + sm.getClass());
        Residue ret = AlwaysMatch.v();
        ListIterator formalsIt = args.listIterator();
        List actuals = sm.getArgsContextValues();
        if (abc.main.Debug.v().showArgsMatching) System.out.println("actuals are " + actuals);
        ListIterator actualsIt = actuals.listIterator();
        int fillerpos = -1;
        while (formalsIt.hasNext() && actualsIt.hasNext()) {
            ArgPattern formal = (ArgPattern) formalsIt.next();
            if (abc.main.Debug.v().showArgsMatching) System.out.println("formal is " + formal);
            if (formal instanceof ArgFill) {
                if (abc.main.Debug.v().showArgsMatching) System.out.println("filler at position " + (formalsIt.nextIndex() - 1) + " (" + formal.getPosition() + ")");
                fillerpos = formalsIt.nextIndex();
                while (formalsIt.hasNext()) formalsIt.next();
                while (actualsIt.hasNext()) actualsIt.next();
                break;
            }
            ContextValue actual = (ContextValue) actualsIt.next();
            if (abc.main.Debug.v().showArgsMatching) System.out.println("matching " + formal + " with " + actual);
            ret = AndResidue.construct(ret, formal.matchesAt(we, actual));
        }
        if (fillerpos == -1) {
            if (actualsIt.hasNext() || (formalsIt.hasNext() && !(formalsIt.next() instanceof ArgFill && !formalsIt.hasNext()))) return NeverMatch.v(); else return ret;
        }
        if (abc.main.Debug.v().showArgsMatching) System.out.println("actuals length is " + actuals.size() + " formals length is " + args.size());
        if (actuals.size() < args.size() - 1) return NeverMatch.v();
        while (formalsIt.hasPrevious() && actualsIt.hasPrevious()) {
            ArgPattern formal = (ArgPattern) formalsIt.previous();
            if (formal instanceof ArgFill) {
                return ret;
            }
            ContextValue actual = (ContextValue) actualsIt.previous();
            if (abc.main.Debug.v().showArgsMatching) System.out.println("matching " + formal + " with " + actual);
            ret = AndResidue.construct(ret, formal.matchesAt(we, actual));
        }
        if (formalsIt.hasPrevious() && formalsIt.previous() instanceof ArgFill) return ret;
        throw new InternalCompilerError("Internal error: reached the end of a args pattern list unexpectedly - " + "pattern was " + args + ", method was " + method);
    }

    public void registerSetupAdvice(Aspect aspct, Hashtable typeMap) {
    }

    public void getFreeVars(Set result) {
        Iterator it = args.iterator();
        while (it.hasNext()) ((ArgPattern) (it.next())).getFreeVars(result);
    }

    public boolean unify(Pointcut otherpc, Unification unification) {
        if (otherpc.getClass() == this.getClass()) {
            List otherargs = ((Args) otherpc).getArgs();
            List unifiedargs = new LinkedList();
            int unificationType = 2;
            Iterator it1 = args.iterator();
            Iterator it2 = otherargs.iterator();
            while (it1.hasNext() && it2.hasNext()) {
                ArgPattern pat1 = (ArgPattern) it1.next();
                ArgPattern pat2 = (ArgPattern) it2.next();
                if (pat1.unify(pat2, unification)) {
                    ArgPattern unifiedpat = unification.getArgPattern();
                    unifiedargs.add(unifiedpat);
                    switch(unificationType) {
                        case -1:
                            if (unifiedpat != pat1) unificationType = 0;
                            break;
                        case 1:
                            if (unifiedpat != pat2) unificationType = 0;
                            break;
                        case 2:
                            if (unifiedpat == pat1) {
                                unificationType = -1;
                                break;
                            }
                            if (unifiedpat == pat2) {
                                unificationType = 1;
                                break;
                            }
                            unificationType = 0;
                            break;
                    }
                } else return false;
            }
            if (it1.hasNext() || it2.hasNext()) return false;
            if (unification.unifyWithFirst()) if ((unificationType != -1) && (unificationType != 2)) throw new RuntimeException("Unfication error: restricted unification failed (If: " + "unficationType=" + unificationType + ")");
            switch(unificationType) {
                case -1:
                    unification.setPointcut(this);
                    return true;
                case 1:
                    unification.setPointcut(otherpc);
                    return true;
                case 0:
                    Args newpc = new Args(unifiedargs, getPosition());
                    unification.setPointcut(newpc);
                    return true;
                case 2:
                    unification.setPointcut(this);
                    return true;
                default:
                    throw new RuntimeException("Invalid UnificationType " + unificationType + " in Args unification");
            }
        } else return LocalPointcutVars.unifyLocals(this, otherpc, unification);
    }
}
