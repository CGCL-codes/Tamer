package edu.jhu.joshua.decoder.feature_function.translation_model;

import java.util.List;

/**
 * 
 * @author Lane Schwartz
 * @version $LastChangedDate: 2008-09-18 13:50:51 -0400 (Thu, 18 Sep 2008) $
 */
public interface TMGrammarInterface<Symbol> {

    public Trie<Symbol, Rule> getGrammarForSentence(List<Symbol> sentence);
}
