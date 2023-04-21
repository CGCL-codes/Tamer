package net.sourceforge.jcabare.cbr;

import java.util.Set;

/**
 * 
 * @author Oswaldo Cavalcanti Dantas J�nior <oscadaj@users.sourceforge.net>
 */
public interface ISimilarityAlgorithm {

    Set<ICaseSimilarity> getSimilarity(ICase theCase, Set<ICase> similarCases, double threshold, Object... params);
}
