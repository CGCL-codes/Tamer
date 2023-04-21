package net.sourceforge.javautil.grammar.impl.parser.patterns.token.logic;

import net.sourceforge.javautil.common.CollectionUtil;
import net.sourceforge.javautil.grammar.impl.parser.patterns.IGrammarTokenComposite;
import net.sourceforge.javautil.grammar.lexer.GrammarToken;

/**
 * An {@link IASTPatternCompositeLogic} implementation for sequential comparison.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class GrammarTokenCompositeLogicAnd extends GrammarTokenCompositeLogicAndAbstract {

    public GrammarTokenCompositeLogicAnd() {
        super("AND");
    }
}
