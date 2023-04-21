package net.sourceforge.javautil.grammar.impl.ast.patterns;

import net.sourceforge.javautil.grammar.impl.ast.ASTGrammarContext;
import net.sourceforge.javautil.grammar.impl.ast.IASTPattern;
import net.sourceforge.javautil.grammar.impl.ast.IASTPatternComposite;
import net.sourceforge.javautil.grammar.impl.ast.IASTPatternCompositeLogic;
import net.sourceforge.javautil.grammar.impl.ast.IASTPatternCompositeState;
import net.sourceforge.javautil.grammar.impl.ast.IASTPattern.MatchResult;
import net.sourceforge.javautil.grammar.lexer.GrammarToken;

/**
 * The base for most {@link IASTPatternComposite}'s.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public abstract class ASTPatternCompositeAbstract extends ASTPatternAbstract implements IASTPatternComposite {

    protected final IASTPatternCompositeLogic logic;

    public ASTPatternCompositeAbstract(int order, int minimumOccurence, int maximumOccurence, IASTPatternCompositeLogic logic) {
        super(order, minimumOccurence, maximumOccurence);
        this.logic = logic;
    }

    public void forceComplete(ASTGrammarContext ctx) {
        IASTPatternCompositeState state = ctx.getCurrentState().getStateFor(this);
        ctx.push(state);
        try {
            this.forceCompleteInternal(ctx, state);
        } finally {
            ctx.pop(state);
        }
    }

    public void flushCompleted(ASTGrammarContext ctx) {
        IASTPatternCompositeState state = ctx.getCurrentState().getStateFor(this);
        ctx.push(state);
        try {
            this.flushCompletedInternal(ctx, state);
        } finally {
            ctx.pop(state);
        }
    }

    protected void flushCompletedInternal(ASTGrammarContext ctx, IASTPatternCompositeState state) {
        logic.flushCompleted(ctx, state, getPatterns());
    }

    protected void forceCompleteInternal(ASTGrammarContext ctx, IASTPatternCompositeState state) {
        logic.forceComplete(ctx, state, getPatterns());
    }

    @Override
    protected MatchResult compareInternal(ASTGrammarContext ctx, GrammarToken... tokens) {
        IASTPatternCompositeState state = ctx.getCurrentState().getStateFor(this);
        ctx.push(state);
        try {
            return this.compareInternal(ctx, state, tokens);
        } finally {
            ctx.pop(state);
        }
    }

    public IASTPatternCompositeLogic getLogic() {
        return logic;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Composite[ ");
        sb.append(super.toString());
        sb.append(" ]: \n");
        for (IASTPattern pattern : getPatterns()) {
            sb.append(pattern.toString().replaceAll("^", "\t"));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
	 * NOTE: If sub-classes need to override general comparison flow this method should be overriden and
	 * not the {@link #compareInternal(ASTGrammarContext, GrammarToken...)} method. 
	 * 
	 * @param ctx The context in which to compare the composite pattern
	 * @param state The state related to this composite
	 * @param tokens The tokens to compare to
	 * @return The result of the comparison
	 */
    protected MatchResult compareInternal(ASTGrammarContext ctx, IASTPatternCompositeState state, GrammarToken... tokens) {
        return logic.compare(ctx, state, getPatterns(), tokens);
    }
}
