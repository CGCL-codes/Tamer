package com.android.quicksearchbox;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Tests for RankAwarePromoter
 */
@SmallTest
public class RankAwarePromoterTest extends AndroidTestCase {

    public static final int MAX_PROMOTED_CORPORA = 3;

    public static final int MAX_PROMOTED_SUGGESTIONS = 8;

    public static final String TEST_QUERY = "query";

    private CorpusRanker mRanker;

    private RankAwarePromoter mPromoter;

    @Override
    public void setUp() {
        Corpora corpora = createMockCorpora(5, MAX_PROMOTED_CORPORA);
        mRanker = new LexicographicalCorpusRanker(corpora);
        mPromoter = new RankAwarePromoter(new Config(mContext), corpora);
    }

    public void testPromotesExpectedSuggestions() {
        ArrayList<CorpusResult> suggestions = getSuggestions(TEST_QUERY);
        ListSuggestionCursor promoted = new ListSuggestionCursor(TEST_QUERY);
        mPromoter.pickPromoted(null, suggestions, MAX_PROMOTED_SUGGESTIONS, promoted);
        assertEquals(MAX_PROMOTED_SUGGESTIONS, promoted.getCount());
        int[] expectedSource = { 0, 1, 2, 0, 1, 2, 3, 4 };
        int[] expectedSuggestion = { 1, 1, 1, 2, 2, 2, 1, 1 };
        for (int i = 0; i < promoted.getCount(); i++) {
            promoted.moveTo(i);
            assertEquals("Source in position " + i, "MockSource Source" + expectedSource[i], promoted.getSuggestionSource().getLabel());
            assertEquals("Suggestion in position " + i, TEST_QUERY + "_" + expectedSuggestion[i], promoted.getSuggestionText1());
        }
    }

    private List<Corpus> getRankedCorpora() {
        return mRanker.getRankedCorpora();
    }

    private ArrayList<CorpusResult> getSuggestions(String query) {
        ArrayList<CorpusResult> suggestions = new ArrayList<CorpusResult>();
        for (Corpus corpus : getRankedCorpora()) {
            suggestions.add(corpus.getSuggestions(query, 10));
        }
        return suggestions;
    }

    private static MockCorpora createMockCorpora(int count, int defaultCount) {
        MockCorpora corpora = new MockCorpora();
        for (int i = 0; i < count; i++) {
            Source mockSource = new MockSource("Source" + i);
            Corpus mockCorpus = new MockCorpus(mockSource);
            corpora.addCorpus(mockCorpus);
            if (i < defaultCount) {
                corpora.addDefaultCorpus(mockCorpus);
            }
        }
        return corpora;
    }

    private class LexicographicalCorpusRanker extends AbstractCorpusRanker {

        public LexicographicalCorpusRanker(Corpora corpora) {
            super(corpora);
        }

        @Override
        public List<Corpus> rankCorpora(Corpora corpora) {
            ArrayList<Corpus> ordered = new ArrayList<Corpus>(corpora.getEnabledCorpora());
            Collections.sort(ordered, new Comparator<Corpus>() {

                public int compare(Corpus c1, Corpus c2) {
                    return c1.getName().compareTo(c2.getName());
                }
            });
            return ordered;
        }
    }
}
