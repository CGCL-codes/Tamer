package jgrail.lexicon;

import jpl.Term;

public class SemanticClauseFactory {

    public static SemanticClause parseEasyLexicon(Term term) {
        return new SemanticClause(term);
    }
}
