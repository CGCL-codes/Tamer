package net.sf.crsx.generator.java.templates;

/**
 * Variable use occurring in pattern.
 * 
 * @author  <a href="mailto:villard@us.ibm.com">Lionel Villard</a>
 */
public class PatternVariableUse extends VariableUse {

    /** Position of this variable use in the pattern */
    protected final int position;

    /** Whether it's a free variable */
    protected final boolean free;

    /** Construct a variable use appearing in a pattern */
    protected PatternVariableUse(int position, boolean free) {
        super(null);
        this.free = free;
        this.position = position;
    }

    @Override
    public void normalize(Rule[] rules, NativeSink sink) {
        throw new AssertionError("Not a term-to-rewrite");
    }

    @Override
    protected void normalizeSubs(Rule[] rules, NativeSink sink) {
        throw new AssertionError("Not a term-to-rewrite");
    }

    @Override
    public boolean match(Term term, MatchingContext context) {
        if (term.getKind() != VARIABLE_KIND) return false;
        if (free) context.setVarSubstitute(position, ((VariableUse) term).var);
        return true;
    }
}
