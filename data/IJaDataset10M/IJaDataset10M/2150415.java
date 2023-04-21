package net.sourceforge.javautil.grammar.impl.ast.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sourceforge.javautil.grammar.impl.ast.IASTPatternComposite;
import net.sourceforge.javautil.grammar.impl.ast.IASTPatternCompositeLogic;
import net.sourceforge.javautil.grammar.impl.ast.IASTPatternCompositeState;
import net.sourceforge.javautil.grammar.impl.ast.IASTPattern.MatchStatus;
import net.sourceforge.javautil.grammar.lexer.GrammarToken;

/**
 * The state management for 'AND' based logic {@link IASTPatternCompositeLogic}'s.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public class ASTPatternCompositeStateOr extends ASTPatternCompositeStateAbstract {

    protected int bestMatch = -1;

    protected final List<GrammarToken> tokenCache = new ArrayList<GrammarToken>();

    public ASTPatternCompositeStateOr(IASTPatternComposite composite) {
        super(composite);
    }

    public int getBestMatch() {
        return bestMatch;
    }

    public void setBestMatch(int bestMatch) {
        this.bestMatch = this.index = bestMatch;
    }

    public List<GrammarToken> getTokenCache() {
        return tokenCache;
    }

    @Override
    public void clear() {
        super.clear();
        this.bestMatch = -1;
    }
}
