package org.apache.lucene.spatial.tier;

import java.io.IOException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.ScoreDocComparator;
import org.apache.lucene.search.SortComparatorSource;
import org.apache.lucene.search.SortField;

public class DistanceSortSource implements SortComparatorSource {

    private static final long serialVersionUID = 1L;

    private DistanceFilter distanceFilter;

    private DistanceScoreDocLookupComparator dsdlc;

    public DistanceSortSource(Filter distanceFilter) {
        this.distanceFilter = (DistanceFilter) distanceFilter;
    }

    public void cleanUp() {
        distanceFilter = null;
        if (dsdlc != null) dsdlc.cleanUp();
        dsdlc = null;
    }

    public ScoreDocComparator newComparator(IndexReader reader, String field) throws IOException {
        dsdlc = new DistanceScoreDocLookupComparator(reader, distanceFilter);
        return dsdlc;
    }

    private class DistanceScoreDocLookupComparator implements ScoreDocComparator {

        private DistanceFilter distanceFilter;

        public DistanceScoreDocLookupComparator(IndexReader reader, DistanceFilter distanceFilter) {
            this.distanceFilter = distanceFilter;
            return;
        }

        public int compare(ScoreDoc aDoc, ScoreDoc bDoc) {
            double a = distanceFilter.getDistance(aDoc.doc);
            double b = distanceFilter.getDistance(bDoc.doc);
            if (a > b) return 1;
            if (a < b) return -1;
            return 0;
        }

        public int sortType() {
            return SortField.DOUBLE;
        }

        public Comparable sortValue(ScoreDoc iDoc) {
            return distanceFilter.getDistance(iDoc.doc);
        }

        public void cleanUp() {
            distanceFilter = null;
        }
    }
}
