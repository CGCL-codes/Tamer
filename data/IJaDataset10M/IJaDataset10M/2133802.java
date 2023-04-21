package pt.utl.ist.lucene.sort.sorters.models;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SortField;
import org.apache.lucene.index.IndexReader;
import pt.utl.ist.lucene.level1query.QueryParams;
import pt.utl.ist.lucene.sort.ModelSortDocComparator;
import pt.utl.ist.lucene.ModelManager;
import java.util.HashMap;

/**
 * @author Jorge Machado
 * @date 17/Ago/2008
 * @see pt.utl.ist.lucene.sort
 */
public class DefaultModelSortDocComparator implements ModelSortDocComparator {

    HashMap<Integer, Float> scoresCache;

    HashMap<Integer, Float> textScoresCache;

    protected float timeFactor;

    protected float spatialFactor;

    protected float textFactor;

    protected QueryParams queryParams;

    protected IndexReader reader;

    protected SpatialDistancesScoreDocComparator spatialScoreDocComparator = null;

    protected TimeDistancesScoreDocComparator timeScoreDocComparator = null;

    protected LgteScoreDocComparator textScoreDocComparator = null;

    public void initModel(TimeDistancesScoreDocComparator time, SpatialDistancesScoreDocComparator spatial, LgteScoreDocComparator text, QueryParams queryParams, IndexReader reader) {
        this.spatialScoreDocComparator = spatial;
        this.timeScoreDocComparator = time;
        this.textScoreDocComparator = text;
        this.reader = reader;
        this.queryParams = queryParams;
        this.timeFactor = queryParams.getQueryConfiguration().getFloatProperty("scorer.default.model.time.factor");
        spatialFactor = queryParams.getQueryConfiguration().getFloatProperty("scorer.default.model.spatial.factor");
        textFactor = queryParams.getQueryConfiguration().getFloatProperty("scorer.default.model.text.factor");
        scoresCache = new HashMap<Integer, Float>();
        textScoresCache = new HashMap<Integer, Float>();
    }

    public void cleanUp() {
        if (spatialScoreDocComparator != null) spatialScoreDocComparator.cleanUp();
        if (timeScoreDocComparator != null) timeScoreDocComparator.cleanUp();
        if (textScoreDocComparator != null) textScoreDocComparator.cleanUp();
        queryParams = null;
        scoresCache.clear();
    }

    public float getScore(int doc, float score) {
        return (Float) sortValue(new ScoreDoc(doc, score));
    }

    public float getTimeScore(int doc, float score) {
        return (Float) timeScoreDocComparator.sortValue(new ScoreDoc(doc, score));
    }

    public float getSpatialScore(int doc, float score) {
        return (Float) spatialScoreDocComparator.sortValue(new ScoreDoc(doc, score));
    }

    public float getTextScore(int doc, float score) {
        return getTextScore(new ScoreDoc(doc, score));
    }

    boolean first = false;

    public float getTextScore(ScoreDoc scoreDoc) {
        if (first == false) {
            first = true;
            System.out.println("A primeira vez que o DefaultScoreComparator e chamado");
        }
        Float scoreCache = textScoresCache.get(scoreDoc.doc);
        if (scoreCache != null) return scoreCache;
        scoreCache = (Float) textScoreDocComparator.sortValue(scoreDoc);
        scoreCache = ModelManager.getInstance().getModel().getDocumentFinalScorer().computeFinalScore(scoreCache, reader, getDocLength(scoreDoc.doc));
        textScoresCache.put(scoreDoc.doc, scoreCache);
        return scoreCache;
    }

    public int compare(ScoreDoc scoreDoc1, ScoreDoc scoreDoc2) {
        Float score1 = (Float) sortValue(scoreDoc1);
        Float score2 = (Float) sortValue(scoreDoc2);
        if (score1 > score2) return -1;
        if (score1 < score2) return 1;
        return 0;
    }

    public Comparable sortValue(ScoreDoc scoreDoc) {
        Float score = scoresCache.get(scoreDoc.doc);
        if (score != null) return score;
        float spaceScore1 = 0f;
        float timeScore1 = 0f;
        float textScore1 = 0f;
        if (queryParams.getOrder().isSpatial() && spatialScoreDocComparator != null) {
            spaceScore1 = (Float) spatialScoreDocComparator.sortValue(scoreDoc);
        }
        if (queryParams.getOrder().isTime() && timeScoreDocComparator != null) {
            timeScore1 = (Float) timeScoreDocComparator.sortValue(scoreDoc);
        }
        if (queryParams.getOrder().isScore() && textScoreDocComparator != null) {
            textScore1 = getTextScore(scoreDoc);
        }
        score = merge(timeScore1, spaceScore1, textScore1);
        scoresCache.put(scoreDoc.doc, score);
        return score;
    }

    protected float merge(float time, float spatial, float text) {
        return time * (timeFactor) + spatial * (spatialFactor) + text * (textFactor);
    }

    public int sortType() {
        return SortField.FLOAT;
    }

    private int getDocLength(int doc) {
        int docLen = 0;
        try {
            docLen = reader.getDocLength(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return docLen;
    }
}
