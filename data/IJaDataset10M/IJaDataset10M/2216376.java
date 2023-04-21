package pt.utl.ist.lucene.sort.sorters.models.comparators;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SortField;
import org.apache.lucene.index.IndexReader;
import pt.utl.ist.lucene.level1query.QueryParams;
import pt.utl.ist.lucene.sort.sorters.models.LgteScoreDocComparator;
import java.io.IOException;

/**
 * @author Jorge Machado
 * @date 18/Ago/2008
 * @see pt.utl.ist.lucene.sort.sorters.models
 */
public class TextScoreDocComparator implements LgteScoreDocComparator {

    QueryParams queryParams = null;

    public int compare(ScoreDoc scoreDoc1, ScoreDoc scoreDoc2) {
        Float score1 = (Float) sortValue(scoreDoc1);
        Float score2 = (Float) sortValue(scoreDoc2);
        if (score1 > score2) return -1;
        if (score1 < score2) return 1;
        return 0;
    }

    public Comparable sortValue(ScoreDoc scoreDoc) {
        return scoreDoc.score;
    }

    public int sortType() {
        return SortField.FLOAT;
    }

    public void cleanUp() {
        queryParams = null;
    }

    public void addQueryParams(QueryParams queryParams) {
        this.queryParams = queryParams;
    }

    public void init(IndexReader reader) throws IOException {
    }
}
