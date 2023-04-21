package net.sourceforge.javautil.grammar.impl.ast;

/**
 * Implemented by {@link IASTPattern}'s that can flush matched patterns that have been cached due to
 * ambiguous pattern resolution.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public interface IASTFlushable {

    /**
	 * @param ctx The context in which to flush completed matches
	 */
    void flushCompleted(ASTGrammarContext ctx);

    /**
	 * @param ctx The context in which to force completion of matches
	 */
    void forceComplete(ASTGrammarContext ctx);
}
