package antlr;

/**Contains a list of all places that reference
 * this enclosing rule.  Useful for FOLLOW computations.
 */
class RuleEndElement extends BlockEndElement {

    protected Lookahead[] cache;

    protected boolean noFOLLOW;

    public RuleEndElement(Grammar g) {
        super(g);
        cache = new Lookahead[g.maxk + 1];
    }

    public Lookahead look(int k) {
        return grammar.theLLkAnalyzer.look(k, this);
    }

    public String toString() {
        return "";
    }
}
