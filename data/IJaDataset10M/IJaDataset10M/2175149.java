package org.kommando.core.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.kommando.core.action.Action;
import org.kommando.core.catalog.CatalogObject;
import org.kommando.core.search.ranking.DefaultStringScorer;
import org.kommando.core.search.ranking.StringScorer;
import org.kommando.core.util.Assert;

/**
 * @author Peter De Bruycker
 */
public class ActionSearcher {

    private StringScorer stringRanker = new DefaultStringScorer();

    public List<Result<Action>> searchActions(List<Action> actions, CatalogObject directObject, String query) {
        Assert.argumentNotNull("actions", actions);
        Assert.argumentNotNull("directObject", directObject);
        List<Result<Action>> results = new ArrayList<Result<Action>>();
        for (Action action : actions) {
            if (action.isApplicable(directObject)) {
                Set<Integer> matchedIndexes = new HashSet<Integer>();
                float score = stringRanker.score(action.getName(), query, matchedIndexes);
                if (score > 0) {
                    results.add(new ScoredResult<Action>(action, score, matchedIndexes));
                }
            }
        }
        Collections.sort(results, new ResultComparator());
        return results;
    }
}
