package wssearch.search;

import java.util.Iterator;
import wssearch.search.keywordsearch.KeywordSearch;
import wssearch.search.similaritygraph.SimilaritySearch;
import wssearch.textmining.TextMining;
import wssearch.userprofiling.UserprofileList;
import wssearch.feedback.Feedback;
import test.wssearch.StopWatch;

/**
 * Standard-Suchstrategie:
 * Führt zuerst Schlüsselwortsuche aus, dann Ähnlichkeitssuche und sortiert dann
 * anhand der personalisierten Relevanzbewertungen.
 * @author Thorsten Theelen
 *
 */
public class StandardSearch implements SearchStrategy {

    public Iterator<SearchResult> search(String searchQuery, String username) {
        ResultSet kwsResults = new ResultSet();
        ResultSet finalResults = new ResultSet();
        Iterator<VertexSearchResult> results = KeywordSearch.search(searchQuery);
        while (results.hasNext()) {
            VertexSearchResult ssr = results.next();
            kwsResults.addResult(ssr.getName(), ssr.getScore());
            finalResults.addResult(ssr.getName(), getFeedbackScore(ssr, searchQuery, username), ssr.getScore());
        }
        results = SimilaritySearch.search(kwsResults.getVertexResults());
        while (results.hasNext()) {
            VertexSearchResult ssr = results.next();
            finalResults.addResult(ssr.getName(), getFeedbackScore(ssr, searchQuery, username), ssr.getScore());
        }
        return finalResults.getSearchResults();
    }

    private double getFeedbackScore(VertexSearchResult result, String searchQuery, String username) {
        Feedback fb = Feedback.getInstance();
        TextMining tm = TextMining.getInstance();
        UserprofileList upl = UserprofileList.getInstance();
        Iterator<String> searchwords = tm.stemmer(tm.stringToHashset(searchQuery)).iterator();
        double feedbackScore = 0.0;
        int swCount = 0;
        while (searchwords.hasNext()) {
            String sw = searchwords.next();
            swCount++;
            double swFeedback = 0.5;
            if (fb.hasFeedback(result.getName(), sw)) {
                swFeedback = fb.getScore(result.getName(), sw, upl.getAttributes(username));
            }
            feedbackScore += swFeedback;
        }
        feedbackScore /= swCount;
        return feedbackScore;
    }
}
