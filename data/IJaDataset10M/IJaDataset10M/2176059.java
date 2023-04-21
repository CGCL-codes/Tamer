package joshua.util.lexprob;

import joshua.sarray.MatchedHierarchicalPhrases;
import joshua.util.Pair;

/**
 * Represents lexical probability distributions in both directions.
 * 
 * @author Lane Schwartz
 * @version $LastChangedDate: 2009-04-05 00:52:31 -0400 (Sun, 05 Apr 2009) $
 */
public interface LexicalProbabilities {

    /**
	 * 
	 * @param sourceWord
	 * @param targetWord
	 * @return
	 */
    public float sourceGivenTarget(Integer sourceWord, Integer targetWord);

    /**
	 * 
	 * @param targetWord
	 * @param sourceWord
	 * @return
	 */
    public float targetGivenSource(Integer targetWord, Integer sourceWord);

    /**
	 * 
	 * @param sourceWord
	 * @param targetWord
	 * @return
	 */
    public float sourceGivenTarget(String sourceWord, String targetWord);

    /**
	 * 
	 * @param targetWord
	 * @param sourceWord
	 * @return
	 */
    public float targetGivenSource(String targetWord, String sourceWord);

    public Pair<Float, Float> calculateLexProbs(MatchedHierarchicalPhrases sourcePhrase, int sourcePhraseIndex);
}
