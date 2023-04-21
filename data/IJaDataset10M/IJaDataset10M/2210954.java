package edu.clemson.cs.r2jt.absyn;

public class DefinitionBody extends ResolveConceptualElement {

    /** The base member. */
    private Exp base;

    /** The hypothesis member. */
    private Exp hypothesis;

    /** The definition member. */
    private Exp definition;

    private boolean isInductive;

    public DefinitionBody(Exp base, Exp hypothesis, Exp definition) {
        this.base = base;
        this.hypothesis = hypothesis;
        this.definition = definition;
        this.isInductive = (base != null);
    }

    public boolean isInductive() {
        return this.isInductive;
    }

    @Override
    public void accept(ResolveConceptualVisitor v) {
    }

    @Override
    public String asString(int indent, int increment) {
        StringBuffer sb = new StringBuffer();
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

    public Exp getBase() {
        return base;
    }

    public Exp getHypothesis() {
        return hypothesis;
    }

    public Exp getDefinition() {
        return definition;
    }
}
