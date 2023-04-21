package pt.utl.ist.lucene.sort.sorters;

import org.apache.lucene.search.ScoreDocComparator;
import org.apache.lucene.index.IndexReader;
import pt.utl.ist.lucene.filter.ISpatialDistancesWrapper;
import pt.utl.ist.lucene.filter.ITimeDistancesWrapper;
import pt.utl.ist.lucene.level1query.QueryParams;
import pt.utl.ist.lucene.sort.sorters.models.LgteScoreDocComparator;
import pt.utl.ist.lucene.sort.sorters.models.SpatialDistancesScoreDocComparator;
import pt.utl.ist.lucene.sort.sorters.models.TimeDistancesScoreDocComparator;
import pt.utl.ist.lucene.sort.TimeSpatialDistancesSorterSource;
import pt.utl.ist.lucene.sort.LgteScorer;
import pt.utl.ist.lucene.sort.ModelSortDocComparator;
import java.io.IOException;

public class TimeSpatialTextSorterSource implements TimeSpatialDistancesSorterSource, LgteScorer {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ITimeDistancesWrapper iTimeDistancesWrapper;

    private ISpatialDistancesWrapper iSpatialDistancesWrapper;

    private QueryParams queryParams;

    private SpatialDistancesScoreDocComparator spatialScoreDocComparator = null;

    private TimeDistancesScoreDocComparator timeScoreDocComparator = null;

    private LgteScoreDocComparator textScoreDocComparator = null;

    private ModelSortDocComparator comparator = null;

    public TimeSpatialTextSorterSource(QueryParams queryParams) {
        addQueryParams(queryParams);
    }

    public void cleanUp() {
        iTimeDistancesWrapper = null;
        iSpatialDistancesWrapper = null;
        if (comparator != null) {
            comparator.cleanUp();
        }
    }

    public void addQueryParams(QueryParams queryParams) {
        this.queryParams = queryParams;
        if (spatialScoreDocComparator != null) spatialScoreDocComparator.addQueryParams(queryParams);
        if (timeScoreDocComparator != null) timeScoreDocComparator.addQueryParams(queryParams);
        if (textScoreDocComparator != null) textScoreDocComparator.addQueryParams(queryParams);
    }

    public float getScore(int doc, float score) {
        if (comparator != null) return comparator.getScore(doc, score);
        return score;
    }

    public float getTextScore(int doc, float score) {
        if (comparator != null) return comparator.getTextScore(doc, score);
        return score;
    }

    public float getSpatialScore(int doc, float score) {
        if (comparator != null) return comparator.getSpatialScore(doc, score);
        return score;
    }

    public float getTimeScore(int doc, float score) {
        if (comparator != null) return comparator.getTimeScore(doc, score);
        return score;
    }

    public ScoreDocComparator newComparator(IndexReader reader, String field) throws IOException {
        if (spatialScoreDocComparator == null) {
            spatialScoreDocComparator = (SpatialDistancesScoreDocComparator) queryParams.getQueryConfiguration().getPlugin("scorer.spatial.score.doc.comparator");
            timeScoreDocComparator = (TimeDistancesScoreDocComparator) queryParams.getQueryConfiguration().getPlugin("scorer.time.score.doc.comparator");
            textScoreDocComparator = (LgteScoreDocComparator) queryParams.getQueryConfiguration().getPlugin("scorer.text.score.doc.comparator");
            spatialScoreDocComparator.init(reader);
            timeScoreDocComparator.init(reader);
            textScoreDocComparator.init(reader);
            spatialScoreDocComparator.addSpaceDistancesWrapper(iSpatialDistancesWrapper);
            timeScoreDocComparator.addTimeDistancesWrapper(iTimeDistancesWrapper);
            spatialScoreDocComparator.addQueryParams(queryParams);
            timeScoreDocComparator.addQueryParams(queryParams);
        }
        comparator = (ModelSortDocComparator) queryParams.getQueryConfiguration().getPlugin("scorer.model");
        comparator.initModel(timeScoreDocComparator, spatialScoreDocComparator, textScoreDocComparator, queryParams, reader);
        return comparator;
    }

    public void addSpaceDistancesWrapper(ISpatialDistancesWrapper iSpatialDistances) {
        iSpatialDistancesWrapper = iSpatialDistances;
        if (spatialScoreDocComparator != null) {
            spatialScoreDocComparator.addSpaceDistancesWrapper(iSpatialDistances);
        }
    }

    public ISpatialDistancesWrapper getSpaceDistancesWrapper() {
        return iSpatialDistancesWrapper;
    }

    public void addTimeDistancesWrapper(ITimeDistancesWrapper iTimeDistances) {
        iTimeDistancesWrapper = iTimeDistances;
        if (timeScoreDocComparator != null) {
            timeScoreDocComparator.addTimeDistancesWrapper(iTimeDistances);
        }
    }

    public ITimeDistancesWrapper getTimeDistancesWrapper() {
        return iTimeDistancesWrapper;
    }
}
