package org.getopt.luke;

import org.apache.lucene.util.BytesRef;

public final class TermStats {

    public BytesRef termtext;

    public String field;

    public int docFreq;

    public long totalTermFreq;

    TermStats(String field, BytesRef termtext, int df) {
        this.termtext = new BytesRef(termtext);
        this.field = field;
        this.docFreq = df;
    }

    TermStats(String field, BytesRef termtext, int df, long tf) {
        this.termtext = new BytesRef(termtext);
        this.field = field;
        this.docFreq = df;
        this.totalTermFreq = tf;
    }

    String getTermText() {
        return termtext.utf8ToString();
    }
}
