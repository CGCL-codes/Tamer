package org.hibnet.lune.ui.model;

import java.io.IOException;
import org.apache.lucene.document.Document;
import org.hibnet.lune.core.Searcher;

public class ScoreDoc {

    public float score;

    public int num;

    public Document doc;

    public ScoreDoc(Searcher searcher, org.apache.lucene.search.ScoreDoc scoredoc) throws IOException {
        num = scoredoc.doc;
        score = scoredoc.score;
        doc = searcher.doc(num);
    }
}
