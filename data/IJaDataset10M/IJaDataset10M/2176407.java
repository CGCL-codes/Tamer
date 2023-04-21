package org.apache.lucene.index;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SegmentTermContextDistanceDocs extends SegmentTermDocs {

    static class SegmentTermDocsWrapper {

        String field;

        SegmentTermDocs segmentTermDocs;

        int dist;

        boolean go = true;

        public SegmentTermDocsWrapper(String field, SegmentTermDocs segmentTermDocs, int dist) {
            this.field = field;
            this.segmentTermDocs = segmentTermDocs;
            this.dist = dist;
        }

        public int getDist() {
            return dist;
        }

        public void setDist(int dist) {
            this.dist = dist;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public SegmentTermDocs getSegmentTermDocs() {
            return segmentTermDocs;
        }

        public void setSegmentTermDocs(SegmentTermDocs segmentTermDocs) {
            this.segmentTermDocs = segmentTermDocs;
        }

        public boolean next() throws IOException {
            go = segmentTermDocs.next();
            return go;
        }

        public boolean isGo() {
            return go;
        }
    }

    List<SegmentTermDocsWrapper> segmentTermDocsDistant;

    int[] docs;

    int[][] freqsDist;

    int[] freqs;

    int maxPosition;

    SegmentTermContextDistanceDocs(SegmentReader parent) throws IOException {
        super(parent);
    }

    public void seek(Term term) throws IOException {
        super.seek(term);
        if (segmentTermDocsDistant == null) {
            segmentTermDocsDistant = new ArrayList<SegmentTermDocsWrapper>();
            SegmentTermDocs distanceSegmentTermDocs;
            TermInfo distanceTi;
            for (int i = 0; (distanceTi = parent.tis.get(new Term(term.field() + i, term.text()))) != null; i++) {
                distanceSegmentTermDocs = new SegmentTermDocs(parent);
                distanceSegmentTermDocs.seek(distanceTi);
                SegmentTermDocsWrapper wrapper = new SegmentTermDocsWrapper(term.field() + i, distanceSegmentTermDocs, i);
                segmentTermDocsDistant.add(wrapper);
                wrapper.next();
            }
        } else if (segmentTermDocsDistant.size() > 0) {
            for (SegmentTermDocsWrapper segmentTermDocs : segmentTermDocsDistant) {
                TermInfo ti = segmentTermDocs.getSegmentTermDocs().parent.tis.get(new Term(segmentTermDocs.getField(), term.text()));
                segmentTermDocs.getSegmentTermDocs().seek(ti);
                segmentTermDocs.next();
            }
        }
    }

    public void seek(TermEnum termEnum) throws IOException {
        super.seek(termEnum);
        if (segmentTermDocsDistant != null) for (SegmentTermDocsWrapper segmentTermDocs : segmentTermDocsDistant) {
            TermInfo ti;
            if (termEnum instanceof SegmentTermEnum && ((SegmentTermEnum) termEnum).fieldInfos == parent.fieldInfos) ti = ((SegmentTermEnum) termEnum).termInfo(); else ti = parent.tis.get(new Term(termEnum.term().field(), termEnum.term().text()));
            segmentTermDocs.getSegmentTermDocs().seek(ti);
        }
    }

    public void close() throws IOException {
        super.close();
        if (segmentTermDocsDistant != null) for (SegmentTermDocsWrapper segmentTermDocs : segmentTermDocsDistant) {
            segmentTermDocs.getSegmentTermDocs().close();
        }
    }

    /** Optimized implementation. */
    public int read(final int[] docs, final int[] freqs) throws IOException {
        maxPosition = super.read(docs, freqs) - 1;
        this.docs = docs;
        this.freqs = freqs;
        this.freqsDist = new int[docs.length][segmentTermDocsDistant.size()];
        buildDistancesFreqs();
        return maxPosition + 1;
    }

    private void buildDistancesFreqs() throws IOException {
        for (SegmentTermDocsWrapper segmentTermDocs : segmentTermDocsDistant) {
            for (int i = 0; i <= maxPosition && segmentTermDocs.getSegmentTermDocs().doc() <= docs[maxPosition] && segmentTermDocs.isGo(); i++) {
                if (segmentTermDocs.getSegmentTermDocs().doc() == docs[i]) {
                    freqsDist[i][segmentTermDocs.getDist()] = segmentTermDocs.getSegmentTermDocs().freq();
                    if (!segmentTermDocs.getSegmentTermDocs().next()) {
                        break;
                    }
                }
            }
        }
    }

    public int[][] getFreqsDist() {
        return freqsDist;
    }
}
