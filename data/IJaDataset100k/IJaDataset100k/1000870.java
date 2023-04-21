package persistence.antlr;

abstract class BlockWithImpliedExitPath extends AlternativeBlock {

    protected int exitLookaheadDepth;

    /** lookahead to bypass block; set
     * by deterministic().  1..k of Lookahead
     */
    protected Lookahead[] exitCache = new Lookahead[grammar.maxk + 1];

    public BlockWithImpliedExitPath(Grammar g) {
        super(g);
    }

    public BlockWithImpliedExitPath(Grammar g, Token start) {
        super(g, start, false);
    }
}
