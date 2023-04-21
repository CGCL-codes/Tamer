package nl.utwente.ewi.portunes.model.logic.propositional;

import java.util.*;

/** Represents a Bayesian atom. All specific kinds of terms are expected to
 * implement this interface.
 */
public abstract class Term extends ArgSpec {

    /**
     * Returns the type of this term.  Throws an IllegalStateException 
     * if this term has not been compiled successfully (e.g., if this 
     * term is a variable that is not in scope).  
     */
    public abstract Type getType();

    /**
     * Returns true if the given term occurs in this term (or if the given 
     * term is equal to this term).  
     */
    public boolean containsTerm(Term target) {
        if (equals(target)) {
            return true;
        }
        for (Iterator iter = getSubExprs().iterator(); iter.hasNext(); ) {
            if (((Term) iter.next()).containsTerm(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the set of generating functions that are applied to the
     * term <code>subject</code> by this term or any of its subterms.
     *
     * <p>The default implementation returns the union of the generating 
     * functions applied in this term's sub-expressions.  This is 
     * overridden by FuncAppTerm.
     *
     * @return unmodifiable set of OriginFunc
     */
    public Set getGenFuncsApplied(Term subject) {
        Set genFuncs = new HashSet();
        for (Iterator iter = getSubExprs().iterator(); iter.hasNext(); ) {
            genFuncs.addAll(((Term) iter.next()).getGenFuncsApplied(subject));
        }
        return Collections.unmodifiableSet(genFuncs);
    }

    public boolean isNumeric() {
        return getType().isSubtypeOf(BuiltInTypes.REAL);
    }

    /**
     * Returns true if this term is the constant term that always denotes 
     * Model.NULL.  The default implementation returns false.  
     */
    public boolean isConstantNull() {
        return false;
    }
}
